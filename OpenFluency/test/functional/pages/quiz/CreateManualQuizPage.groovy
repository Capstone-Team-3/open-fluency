package pages.quiz

import pages.MainLayoutPage

class CreateManualQuizPage extends MainLayoutPage {
	
	static url = "quizEditor/create"

	static at = {
		$(".quiz-create").size() > 0
	}
	
	static content = {
		quizTitle(wait: true) { $("input", name: 'title') }
		maxCardTime(wait: true) { $("input", name:"maxCardTime") }
		createQuizButton(wait: true) { $("#create-quiz") }
	}
}