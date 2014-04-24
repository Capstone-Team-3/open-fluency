package com.openfluency.course

import com.openfluency.flashcard.Flashcard

class Question {

	Flashcard flashcard
	Quiz quiz

	// Get all the other options that the user can choose from in addition to the correct answer
	List<QuestionOption> getOptions() {
		return QuestionOption.findAllByQuestion(this)
	}

	// Returns a randomly ordered list of flashcards that the user will have to choose from
	List<Flashcard> getSelections() {
		List<Flashcard> selections = getOptions().collect { it.flashcard }
		selections << flashcard
		Collections.shuffle(selections, new Random())
		return selections
	}

    static constraints = {
    }
}
