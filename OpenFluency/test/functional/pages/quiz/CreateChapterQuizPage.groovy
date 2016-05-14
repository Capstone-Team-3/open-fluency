package pages.quiz

import pages.MainLayoutPage

class CreateChapterQuizPage extends MainLayoutPage {
	
	static url = "quiz/create"

	static at = {
		$(".quiz-create").size() > 0
	}
	
	static content = {
		quizTitle(wait: true) { $("input", name: 'title') }
		maxCardTime(wait: true) { $("input", name:"maxCardTime") }
		quizType(wait: true) { $("select", name: 'testElement') }
		createQuizButton(wait: true) { $("#create-quiz") }
	}
}