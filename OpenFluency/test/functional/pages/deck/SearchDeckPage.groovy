package pages.deck

import pages.MainLayoutPage

class SearchDeckPage extends MainLayoutPage {
	
	static url = "deck/search"

	static at = {
		$("#main").text() == "Deck Search"
	}

	static content = {
		decksTable(wait: true) { $(".decks-table") }
		languageFilter(wait: true) { $("#filter-lang") }
		keywordFilter(wait: true) { $("#search-text") }
		searchDeckButton(wait: true) { $("#run-search") }
	}
}