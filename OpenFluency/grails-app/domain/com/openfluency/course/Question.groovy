package com.openfluency.course

import com.openfluency.flashcard.Flashcard

class Question {

	Flashcard flashcard
	Quiz quiz

	List<QuestionOption> getOptions() {
		return QuestionOption.findAllByQuestion(this)
	}

    static constraints = {
    }
}
