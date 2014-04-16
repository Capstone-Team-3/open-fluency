package pages.deck

import pages.MainLayoutPage

class ListDeckPage extends MainLayoutPage {
	
	static url = "deck/list"

	static at = {
		$(".deck-list").size() > 0
	}

	static content = {
		
	}
}