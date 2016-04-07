package pages.quiz

import pages.MainLayoutPage

class ImportQuizPage extends MainLayoutPage {
	
	static url = "quiz/quizImport"

	static at = {
		$(".create-create").size() > 0
	}
}