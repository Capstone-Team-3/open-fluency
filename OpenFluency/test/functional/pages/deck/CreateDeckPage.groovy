package pages.deck

import pages.MainLayoutPage

class CreateDeckPage extends MainLayoutPage {
	
	static url = "deck/create"

	static at = {
		$("#main").text() == "Create New Deck"
	}

	static content = {
		
		deckTitle(wait: true) { $("input", name: 'title') }
		sourceLanguageSelect(wait: true) { $("select", name: 'sourceLanguage.id') }
		languageSelect(wait: true) { $("select", name: 'language.id') }
		cardServerAlgoSelect(wait: true) { $("select", name: 'cardServerAlgo') }
		deckDescription(wait: true) { $("textarea", name: 'description') }
		deckLanguage(wait: true) { $("select", name: 'language.id') }
		createDeckButton(wait: true) { $("#create-deck") }
	}
}