package pages.deck

import pages.MainLayoutPage

class UploadNewAnkiDeckPage extends MainLayoutPage {
	
	static url = "document/create"

	static at = {
		$("h1").text() == "Upload New Anki Deck"
	}
}