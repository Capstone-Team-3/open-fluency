package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.language.Language

class DeckController {

	def springSecurityService
	def flashcardService

	def index() {
		redirect action: "list"
	}

	def list() {
		[deckInstanceList: Deck.findAllByOwner(User.load(springSecurityService.principal.id))]
	}

	def create() {
		[deckInstance: new Deck(params)]
	}

	def save() {
		def deckInstance = flashcardService.createDeck(params.title, params.description, params['language.id'])

    	// Check for errors
    	if (deckInstance.hasErrors()) {
    		render(view: "create", model: [deckInstance: deckInstance])
    		return
    	}

    	flash.message = "Well done! You succesfully created a new deck!"

    	redirect action: "show", id: deckInstance.id
    }

    def show(Deck deckInstance, Integer max) {
    	params.max = Math.min(max ?: 12, 100)
        def flashcards = Flashcard.findAllByDeck(deckInstance, params)
        respond flashcards, model:[deckInstance: deckInstance, flashcardCount: Flashcard.countByDeck(deckInstance)]
    }

    def practice(Deck deckInstance, Integer max) {
        params.max = max ?: 1
        def flashcardInstance = Flashcard.findAllByDeck(deckInstance, params)
        [deckInstance: deckInstance, flashcardInstance: flashcardInstance, flashcardCount: Flashcard.countByDeck(deckInstance)]
    }

    def search() {
        [deckInstanceList: Deck.findAll(), languageInstanceList: Language.list()]
    }
}
