package pages.deck

import pages.MainLayoutPage

class EditDeckPage extends MainLayoutPage {

	static url = "deck/edit"

	static at = {
		$("#main").text() == "Edit Deck"
	}

	static content = {

		deckTitle(wait: true) { $("input", name: 'title') }
		sourceLanguageSelect(wait: true) { $("select", name: 'sourceLanguage.id') }
		languageSelect(wait: true) { $("select", name: 'language.id') }
		cardServerAlgoSelect(wait: true) { $("select", name: 'cardServerAlgo') }
		deckDescription(wait: true) { $("textarea", name: 'description') }
		deckLanguage(wait: true) { $("select", name: 'language.id') }
		saveDeckButton(wait: true) { $("#create-deck") }
	}
}