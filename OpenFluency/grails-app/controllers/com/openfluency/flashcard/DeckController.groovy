package com.openfluency.flashcard

import com.openfluency.auth.User

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
		def deckInstance = flashcardService.createDeck(params.title, params.description, params['alphabet.id'])

    	// Check for errors
    	if (deckInstance.hasErrors()) {
    		render(view: "create", model: [deckInstance: deckInstance])
    		return
    	}

    	redirect action: "show", id: deckInstance.id
    }

    def show(Deck deckInstance) {
    	[deckInstance: deckInstance]
    }
}
