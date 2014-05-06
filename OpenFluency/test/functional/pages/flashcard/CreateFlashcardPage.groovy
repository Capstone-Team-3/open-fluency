package pages.course

import pages.MainLayoutPage

class CreateFlashcardPage extends MainLayoutPage {

	static url = "course/show"

	static at = {
		$("#main").text() > 0
	}

	static content = {

	}
}