package com.openfluency.flashcard

import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet
import com.openfluency.media.Image
import com.openfluency.media.Audio
import com.openfluency.auth.User
import com.openfluency.language.Unit

class FlashcardController { 

	def springSecurityService
	def flashcardService

	/**
	* Render the create flashcard page for a particular unit
	*/
    def create() {
        Unit unit = Unit.load(params.unit)
    	[flashcardInstance: new Flashcard(params), unitInstance: unit, userDecks: Deck.findAllByOwnerAndAlphabet(User.load(springSecurityService.principal.id), unit.alphabet)]
    }

    /**
    * Save the flashcard for the selected unit
    */
    def save() {

        def flashcardInstance = flashcardService.createFlashcard(params.unit, params.unitMapping, params.pronunciation, params.image, params.audio, params.deck)

    	// Check for errors
        if (flashcardInstance.hasErrors()) {
            log.info "Unit has errors!"
            redirect action: "create", params: params
            return
        }

        redirect controller:"deck", action: "show", id: params.deck
    }

    /**
    * View a single flashcard by id - a sample call to this function would be
    * /OpenFluency/flashcard/show/1 --> this will display the flashcard with id = 1
    */
    def show(Flashcard flashcardInstance) {
        [flashcardInstance: flashcardInstance]
    }
}
