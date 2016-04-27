package pages

import pages.MainLayoutPage

class HelpPage extends MainLayoutPage {
	
	static url = "help"

	static at = {
		$("h1").text() == "User Guide"
	}
}