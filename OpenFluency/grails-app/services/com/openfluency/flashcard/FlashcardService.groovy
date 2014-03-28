package com.openfluency.flashcard

import com.openfluency.auth.User
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
    Deck createDeck(String title, String description) {
    	Deck deck = new Deck(title: title, description: description, owner: User.load(springSecurityService.principal.id))
    	deck.save()
    	return deck
    }
}
