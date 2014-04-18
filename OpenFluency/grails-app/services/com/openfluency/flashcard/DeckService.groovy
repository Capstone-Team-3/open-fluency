package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.flashcard.Share
import com.openfluency.language.Language
import grails.transaction.Transactional
import com.openfluency.auth.User
import com.openfluency.Constants

@Transactional
class DeckService {

	def springSecurityService

	/**
	* Retrieves the next flashcard that the user should view according SpacedRepetition
	* @param cardUsage: the usage for the flashcard being ranked
	* @param ranking that the user gave to this flashcard
	* @return the new usage for the next flashcard
	*/
	CardUsage getNextFlashcard(Deck deckInstance, String cardUsageId, Integer ranking) {

		log.info "CardUsageID: ${cardUsageId} - Ranking: $ranking"

		CardUsage cardUsage = CardUsage.get(cardUsageId)

		if(cardUsage) {
			// Close the current CardUsage
			cardUsage.endTime = new Date()
			cardUsage.ranking = ranking
			cardUsage.save(failOnError: true)

			// Create the CardRankingCurrent for the card (or update it)
			CardRankingCurrent cardRankingCurrent = CardRankingCurrent.withCriteria {
				user {
					eq('id', springSecurityService.principal.id)
				}
				eq('flashcard', cardUsage.flashcard)
			}[0]

			// Save the card ranking
			if(!cardRankingCurrent) {
				new CardRankingCurrent(flashcard: cardUsage.flashcard, 
					user: User.load(springSecurityService.principal.id), 
					ranking: ranking).save()
			} 
			else {
				cardRankingCurrent.ranking = ranking
				cardRankingCurrent.save(failOnError: true)
			}
		}

		// Retrieve the next card - faking, random for now
		java.util.Random rand = new java.util.Random()
		int max = Flashcard.countByDeck(deckInstance) - 1
		def params = [offset: rand.nextInt(max+1), max: 1]

		Flashcard flashcardInstance = Flashcard.findByDeck(deckInstance, params)

		// Now create a new CardUsage for this new card and return it
		return new CardUsage(flashcard: flashcardInstance, 
					user: User.load(springSecurityService.principal.id), 
					ranking: null).save(failOnError: true)

	}

	/**
	* Calculate the progress that a particular user has made on a deck
	*/
	Double getDeckProgress(Deck deckInstance) {
		// Progress calculation:
		// - When a user ranks a card, it gives him a certain number of points
		// 		- easy  	--> 3 points
		// 		- medium 	--> 2 points
		// 		- hard 		--> 1 points
		//		- unranked	--> 0 points
		// Then the progress is the total points for the user / max points
		Integer flashcardCount = deckInstance.flashcardCount
		Integer totalPoints = Flashcard.executeQuery("""
			SELECT sum(ranking) FROM CardRankingCurrent 
			WHERE user.id = ?
			AND flashcard.id in (SELECT id FROM Flashcard WHERE deck.id = ?))
			""", [springSecurityService.principal.id, deckInstance.id])[0]
		
		Double progress = totalPoints ? totalPoints*100 / (flashcardCount*Constants.EASY) : 0

		log.info "FlashcardCount: ${flashcardCount} - TotalPoints: ${totalPoints} - Progress: ${progress.round(2)}"

		return progress.round(2)
	}

	/**
    * Create a new deck owned by the currently logged in user
    */
    Deck createDeck(String title, String description, String languageId) {
    	Deck deck = new Deck(title: title, description: description, owner: User.load(springSecurityService.principal.id), language: Language.load(languageId))
    	deck.save()
    	return deck
    }

	/**
    * Add a deck to my list of shared courses
    */
    Share addDeck(Deck deck) {
        return new Share(deck: deck, receiver: User.load(springSecurityService.principal.id)).save(flush: true);
    }

    Boolean removeDeck(Deck deck) {
        Share share = Share.findByReceiverAndDeck(User.load(springSecurityService.principal.id), deck)
        if(share) {
            try {
                share.delete()
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
}
