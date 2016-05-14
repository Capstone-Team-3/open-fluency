package pages.quiz

import pages.MainLayoutPage

class ShowQuizPage extends MainLayoutPage {
	
	static url = "quiz/show"

	static at = {
		$(".quiz-show").size() > 0
	}
}