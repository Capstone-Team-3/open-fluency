package pages.deck

import pages.MainLayoutPage

class CreateDeckPage extends MainLayoutPage {
	
	static url = "deck/create"

	static at = {
		$("#main").text() == "Create New Deck"
	}

	static content = {
		
		deckTitle(wait: true) { $("input", name: 'title') }
		deckDescription(wait: true) { $("textarea", name: 'description') }
		deckLanguage(wait: true) { $("select", name: 'language.id') }
		createDeckButton(wait: true) { $("#create-deck") }
	}
}