package com.openfluency.flashcard

import com.openfluency.course.Question
import com.openfluency.course.QuestionOption
import com.openfluency.auth.User
import com.openfluency.flashcard.Share
import com.openfluency.media.Customization
import com.openfluency.language.Language
import com.openfluency.language.Alphabet
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.media.Image
import com.openfluency.media.Audio
import grails.transaction.Transactional

class FlashcardService {

	def springSecurityService 
    def mediaService
    def deckService
    def flashcardInfoService

	/**
	* Creates a new flashcard - the alternative is to pass a Map as an argument 
    * does not currently support audio file storage
	*/
	@Transactional
    Flashcard createFlashcard(String unitId, String unitMappingId, String pronunciationId, String imageLink, String audioId, String deckId) {

        Unit unitInstance = Unit.load(unitId)
		if (pronunciationId== null) {
			println "....${unitInstance.literal}... missing pronunciation  - skipped"
			return null	
		} else
			println "....${unitInstance.literal}...."
        Image imageInstance = mediaService.createImage(imageLink, unitMappingId)
        Audio audioInstance = null;
        if (audioId != "") { audioInstance = Audio.load(audioId); }

        //build info to add flashcard to queueing system
        Deck deckInstance = Deck.load(deckId)
		def flashcardInstance = new Flashcard(
            primaryAlphabet: unitInstance?.alphabet, 
            unitMapping: UnitMapping.load(unitMappingId), 
            pronunciation: Pronunciation.load(pronunciationId), 
            image: imageInstance, 
            audio: audioInstance, 
            deck: deckInstance).save(flush: true, failOnError: true)

        //build the flashcardInfo objs for all users following this deck
        flashcardInfoService.addNewFlashcardInfoAllDeckUsers(deckInstance, flashcardInstance)

        println "Created flashcard $flashcardInstance"
        return flashcardInstance
    }

	@Transactional
    Flashcard createFlashcardUsingDictionaryInfo(String primaryString, String otherString, String pronunciationString, int deckId, String imageLink, String audioLink) {

        Unit primaryUnit = Unit.findByLiteral(primaryString);
        if(primaryUnit == null)
        {
           def alphabet = Alphabet.findByName("Kanji"); 
           assert(alphabet != null);
           primaryUnit = new Unit(alphabet : alphabet, literal : primaryString);
           primaryUnit.save(flush: true, failOnError: true);
        }

        Unit secondaryUnit = Unit.findByLiteral(otherString);
        if(secondaryUnit == null)
        {
           def alphabet = Alphabet.findByName("Latin"); 
           assert(alphabet != null);
           secondaryUnit = new Unit(alphabet: alphabet, literal: otherString);
           secondaryUnit.save(flush: true, failOnError: true);
        }

        UnitMapping mapping = UnitMapping.findByUnit1AndUnit2(primaryUnit, secondaryUnit);
        if(mapping == null)
        {
            mapping = new UnitMapping(unit1: primaryUnit, unit2 : secondaryUnit);
            mapping.save(flush: true, failOnError: true);
        }

        def pronunciation = Pronunciation.findByLiteral(pronunciationString);
        if(pronunciation == null)
        {
           def alphabet = Alphabet.findByName("Latin"); 
           assert(alphabet != null);
           pronunciation = new Pronunciation(unit: primaryUnit, alphabet : alphabet, literal : pronunciationString).save(flush: true, failOnError: true);
        }

		def audio=null
		if (audioLink) {
		    audio = mediaService.createAudio(audioLink,null,pronunciation.id.toString())
		}
		
        def image = null;
        if(imageLink?.trim())
        {
            image = mediaService.createImage(imageLink, mapping.id.toString()) 
        }

		def flashcardInstance = new Flashcard(
            primaryAlphabet: primaryUnit.alphabet, 
            unitMapping: mapping, 
            pronunciation: pronunciation, 
            image: image, 
            audio: audio, 
            deck: Deck.load(deckId)).save(flush: true, failOnError: true)
        return flashcardInstance;
    }

	
	@Transactional
	void reassignFlashcard(Flashcard card, Deck dest){
		card.deck = dest;
	}

    /**
    * Get the last ranking that the user gave this flashcard
    */
	@Transactional(readOnly = true)
    CardRanking getLastRanking(Long flashcardId) {
        return CardRanking.findByFlashcardAndUser(Flashcard.load(flashcardId), User.load(springSecurityService.principal.id))
    }

    /**
    * For development purposes
    */
    def createRandomFlashcards(Deck deck, Alphabet alphabet) {
        // Build a few flashcards
        // The idea here is: find all the unitMappings where unit1 is alphabet the given alphabet
        int f=0;
        UnitMapping.createCriteria().list(max: 5) {
            unit1 { eq('alphabet', alphabet) }
        }.each { unitMapping ->
            Pronunciation pronunciation = Pronunciation.findByUnit(unitMapping.unit1)
            if(pronunciation) {
                new Flashcard(primaryAlphabet: unitMapping.unit1.alphabet, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
                f++
            }
        }
        //ensure the queues are built for the deck owner
        flashcardInfoService.resetDeckFlashcardInfo(deck.owner, deck)
        
        log.info "Created ${f} flashcards for deck: ${deck.title}"
    }

	@Transactional
    void deleteFlashcard(Flashcard flashcardInstance) {
        
        // First delete customizations
        Customization.findAllByCard(flashcardInstance).each {
            it.audioAssoc.delete()
            it.imageAssoc.delete()
            it.delete()
        }

        // Delete all the stats
        FlashcardInfo.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        // Delete all card usages and rankings for this card
        CardUsage.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        CardRanking.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        // Now delete it
        flashcardInstance.delete()
    }
}
