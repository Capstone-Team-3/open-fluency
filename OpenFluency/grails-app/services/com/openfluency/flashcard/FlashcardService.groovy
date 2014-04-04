package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.language.Language
import com.openfluency.language.Alphabet
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.media.Image
import com.openfluency.media.Audio
import grails.transaction.Transactional

@Transactional
class FlashcardService {

	def springSecurityService

	/**
	* Creates a new flashcard - the alternative is to pass a Map as an argument
	*/
    def createFlashcard(String unitId, String unitMappingId, String pronunciationId, String imageId, String audioId, String deckId) {

        Unit unitInstance = Unit.load(unitId)

        def flashcardInstance = new Flashcard(
            primaryAlphabet: unitInstance?.alphabet, 
            unitMapping: UnitMapping.load(unitMappingId), 
            pronunciation: Pronunciation.load(pronunciationId), 
            image: Image.load(imageId), 
            audio: Audio.load(audioId), 
            deck: Deck.load(deckId)).save(flush: true, failOnError: true)

        println "Created flashcard $flashcardInstance"
        return flashcardInstance
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
    * For development purposes
    */
    def createRandomFlashcards(Deck deck, Alphabet alphabet) {
        // Build a few flashcards
        // The idea here is: find all the unitMappings where unit1 is alphabet the given alphabet
        int f=0;
        UnitMapping.withCriteria(max: 50) {
            unit1 { eq('alphabet', alphabet) }
        }.each { unitMapping ->
            Pronunciation pronunciation = Pronunciation.findByUnit(unitMapping.unit1)
            if(pronunciation) {
                new Flashcard(primaryAlphabet: unitMapping.unit1.alphabet, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
                f++
            }
        }

        log.info "Created ${f} flashcards for deck: ${deck.title}"
    }
}
