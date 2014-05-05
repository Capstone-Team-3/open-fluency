package com.openfluency.flashcard

import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.media.Image
import com.openfluency.auth.User
import com.openfluency.flashcard.Share
import com.openfluency.language.Language
import grails.transaction.Transactional
import com.openfluency.auth.User
import com.openfluency.course.Chapter
import com.openfluency.Constants
import com.openfluency.algorithm.*

@Transactional
class DeckService {

	def springSecurityService
	def flashcardService
	def algorithmService
	def flashcardInfoService
    def languageService

	/**
	* Retrieves the next flashcard that the user should view according SpacedRepetition
	* @param cardUsage: the usage for the flashcard being ranked
	* @param ranking that the user gave to this flashcard
	* @return the new usage for the next flashcard
	*/
	CardUsage getNextFlashcard(Deck deckInstance, String cardUsageId, Integer ranking, Integer rankingType) {

		log.info "deckInstance: ${deckInstance.id} - CardUsageID: ${cardUsageId} - Ranking: $ranking - Ranking Type: ${rankingType}"

		User theUser = User.load(springSecurityService.principal.id)
		CardUsage cardUsage = CardUsage.get(cardUsageId)

		if(cardUsage) {
			// Close the current CardUsage
			cardUsage.endTime = new Date()
			cardUsage.ranking = ranking
			cardUsage.save(failOnError: true)

			// Create the CardRanking for the card (or update it)
			CardRanking cardRanking = CardRanking.findOrCreateByUserAndFlashcard(User.load(springSecurityService.principal.id), cardUsage.flashcard)

			// Save the meaning depending on the type
			if(rankingType == Constants.MEANING) {
				log.info "Saving meaningRanking ${ranking}"
				cardRanking.meaningRanking = ranking
			}
			else if(rankingType == Constants.PRONUNCIATION) {
				log.info "Saving pronunciationRanking ${ranking}"
				cardRanking.pronunciationRanking = ranking
			}
            else if(rankingType == Constants.SYMBOL) {
                log.info "Saving symbolRanking ${ranking}"
                cardRanking.symbolRanking = ranking
            }

            cardRanking.save(failOnError: true)

			//use the algo and card usage to update the flashcardInfo and Queue for the given card
			flashcardInfoService.updateViewedFlashcardInfo(deckInstance, cardUsage)
			
		}

		Flashcard flashcardInstance = flashcardInfoService.nextFlashcardInfo(theUser, deckInstance, rankingType).flashcard

		// Now create a new CardUsage for this new card and return it
		return new CardUsage(flashcard: flashcardInstance, 
			user: User.load(springSecurityService.principal.id), 
			ranking: null, rankingType: rankingType).save(failOnError: true)

	}

	/**
	* Calculate the progress that a particular user has made on a deck
	* @return 	Map that contains as keys the types of rankings that a user can give (Meaning, Pronunciation, etc)
	*			and as values the progress for each
	*/
	List getDeckProgress(Deck deckInstance, Long userId) {
		// Progress calculation:
		// - When a user ranks a card, it gives him a certain number of points
		// 		- easy  	--> 3 points
		// 		- medium 	--> 2 points
		// 		- hard 		--> 1 points
		//		- unranked	--> 0 points
		// Then the progress is the total points for the user / max points
		Integer flashcardCount = deckInstance.flashcardCount
		List totalPoints = Flashcard.executeQuery("""
			SELECT sum(meaningRanking), sum(pronunciationRanking), sum(symbolRanking) FROM CardRanking 
			WHERE user.id = ?
			AND flashcard.id in (SELECT id FROM Flashcard WHERE deck.id = ?))
		""", [userId, deckInstance.id])[0]
		
		Double meaningProgress = totalPoints[Constants.MEANING] ? totalPoints[Constants.MEANING]*100 / (flashcardCount*Constants.EASY) : 0
		Double pronunciationProgress = totalPoints[Constants.PRONUNCIATION] ? totalPoints[Constants.PRONUNCIATION]*100 / (flashcardCount*Constants.EASY) : 0
        Double symbolProgress = totalPoints[Constants.SYMBOL] ? totalPoints[Constants.SYMBOL]*100 / (flashcardCount*Constants.EASY) : 0

        return [meaningProgress.round(2), pronunciationProgress.round(2), symbolProgress.round(2)]
    }

    /**
    * Get the deck progress for the logged user
    */
    List getDeckProgress(Deck deckInstance){
        return getDeckProgress(deckInstance, springSecurityService.principal.id)
    }

	/**
    * Create a new deck owned by the currently logged in user
    */
    Deck createDeck(String title, String description, String languageId, String sourceLanguageId, String cardServerName) {
    	User theUser = User.load(springSecurityService.principal.id)
    	
    	Deck deck = new Deck(title: title, 
    		description: description, 
    		owner: theUser, 
    		language: Language.load(languageId),
    		sourceLanguage: Language.load(sourceLanguageId),
    		cardServerName: cardServerName)
    	deck.save()

    	flashcardInfoService.resetDeckFlashcardInfo(theUser, deck)
        
    	return deck
    }

    /** 
    * Update an existing deck
    */
    void updateDeck(Deck deckInstance, String title, String description, String languageId, String sourceLanguageId, String cardServerName) {
    	deckInstance.title = title
    	deckInstance.description = description
    	deckInstance.language = Language.load(languageId)
    	deckInstance.sourceLanguage = Language.load(sourceLanguageId)
    	deckInstance.cardServerName = cardServerName
    	deckInstance.save()
    }   	

    /**
    * Add a deck to my list of shared courses
    */
    Share addDeck(Deck deckInstance) {
    	User userInstance = User.load(springSecurityService.principal.id)
    	
    	// Check if the user already has this deck
    	if(Share.findByReceiverAndDeck(userInstance, deckInstance)) {
    		return null
    	}

    	// Create the Share
    	flashcardInfoService.resetDeckFlashcardInfo(userInstance, deckInstance)
    	return new Share(deck: deckInstance, receiver: userInstance).save(flush: true);
    }

    Boolean removeDeck(Deck deck) {
    	User theUser = User.load(springSecurityService.principal.id)

    	Share share = Share.findByReceiverAndDeck(theUser, deck)
    	if(share) {
    		try {
    			share.delete()
    			flashcardInfoService.removeDeckFlashcardInfo(theUser, deck)
    			return true
    		} 
    		catch(Exception e) {
    			return false
    		}
    	}
    }

    /**
    * Search for Decks
    */
    List<Deck> searchDecks(Long languageId, String keyword) {
    	log.info "Searching Decks with languageId: $languageId and Keywords: $keyword"

    	Deck.withCriteria {

            // Apply language criteria
            if(languageId) {
            	language {
            		eq('id', languageId)
            	}
            }

            // Search using keywords in the title or description
            if(keyword) {
            	or {
            		ilike("title", "%${keyword}%")
            		ilike("description", "%${keyword}%")
            	}
            }
        }
    }

    /**
    * Delete deck
    */
    void deleteDeck(Deck deckInstance) {
    	// First delete all flashcards
    	Flashcard.findAllByDeck(deckInstance).each {
    		flashcardService.deleteFlashcard(it)
    	}

    	// Delete all shares
    	Share.findAllByDeck(deckInstance).each {
    		it.delete()
    	}

    	// Delete all chapters associated with this deck
    	Chapter.findAllByDeck(deckInstance).each {
    		it.delete()
    	}

    	// Now delete it
    	deckInstance.delete()
    }

    /**
    * Get a random flashcard from a deck where the given flashcard lives but is not the given flashcard
    */
    Flashcard getRandomFlashcard(Flashcard flashcardInstance) {
    	Flashcard.executeQuery('FROM Flashcard WHERE deck = ? AND id <> ? ORDER BY rand()', [flashcardInstance.deck, flashcardInstance.id], [max: 1])[0]
    }

    /**
    * Load a deck from a CSV - returns a list with any errors that might have happened during upload
    */
    List loadFlashcardsFromCSV(Deck deckInstance, def f) {
        List result

        if(f.fileItem){
            // Create a temporary file with the uploaded contents
            def extension = f.fileItem.name.lastIndexOf('.').with {it != -1 ? f.fileItem.name.substring(it + 1) : f.fileItem.name}
            def outputFile = new File("${new Date().time}.${extension}")
            f.transferTo(outputFile)

            // Validate the file first
            result = validateCSV(outputFile.path)
            if(!result.isEmpty()) {
                return result
            }
            
            // Everything looks ok, lets save
            new File(outputFile.path).toCsvReader(['skipLines':1]).eachLine { tokens ->
                String symbolString = tokens[0]
                String meaningString = tokens[1]
                String pronunciationString = tokens[2]
                String imageURL = tokens[3]

                // Objects to build flashcard
                Unit symbol = languageService.getUnit(symbolString, deckInstance.language)
                Unit meaning = languageService.getUnit(meaningString, deckInstance.sourceLanguage)
                Pronunciation pronunciation = languageService.getPronunciation(pronunciationString, symbol, deckInstance.language)
                UnitMapping unitMapping = languageService.getUnitMapping(symbol, meaning)
                
                // Now build the card
                flashcardService.createFlashcard(symbol.id.toString(), unitMapping.id.toString(), pronunciation.id.toString(), imageURL, null, deckInstance.id.toString())
            }

            // Cleanup
            outputFile.delete()
        } 
        else {
            result << "File not found"
        }

        return result
    }

    /**
    * Check that each row has a unit, a meaning and a pronunciation
    * Returns a list with any errors
    */
    List validateCSV(String filePath) {
        List result = []
        int i = 0
        new File(filePath).toCsvReader(['skipLines':1]).eachLine { tokens ->

            // Check that there's a meaning a pronunciation and a symbol
            if(!tokens[0]) {
                result << "Row ${i} is missing a symbol"
            }

            if(!tokens[1]) {
                result << "Row ${i} is missing a meaning"   
            }
            
            if(!tokens[2]) {
                result << "Row ${i} is missing a pronunciation"
            }

            i++
        }

        return result
    }
}