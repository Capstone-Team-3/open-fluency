package pages.course

import pages.MainLayoutPage

class SearchUnitPage extends MainLayoutPage {
	
	static url = "unit/search"

	static at = {
		$("#main").text() == "Flashcard Search"
	}

	static content = {

	}
}