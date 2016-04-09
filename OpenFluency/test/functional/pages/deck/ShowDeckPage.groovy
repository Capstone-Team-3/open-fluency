package pages.deck

import pages.MainLayoutPage

class ShowDeckPage extends MainLayoutPage {
	
	static url = "deck/show"

	static at = {
		$(".deck-title").size() > 0
	}

	static content = {
		deckTitle(wait: true) { $(".deck-title") }
		deckDescription(wait: true) { $(".deck-description") }
		addFlashcardsButton(wait: true) { $(".add-flashcards") }
		editDeckButton(wait: true) { $("#edit-deck") }
		flashcardResult(wait: true) { $(".flashcard-result") }
	}
}