package pages.quiz

import pages.MainLayoutPage

class CreateManualQuizPage extends MainLayoutPage {
	
	static url = "quizEditor/create"

	static at = {
		$(".create-create").size() > 0
	}
}