package com.openfluency.course

import com.openfluency.flashcard.Flashcard

/**
 *  The Question domain class represents a single question on a quiz
 */
class Question {
	/** the flashcard the question is based on */
	Flashcard flashcard
	/** the Quiz the question belongs to */
	Quiz quiz

	/** Get all the other options that the user can choose from in addition to the correct answer
	 *  @Return a list of the QuestionOption available
	 */
	List<QuestionOption> getOptions() {
		return QuestionOption.findAllByQuestion(this)
	}

	/** @Return a randomly ordered list of flashcards that the user will have to choose from */
	List<Flashcard> getSelections() {
		List<Flashcard> selections = getOptions().collect { it.flashcard }
		selections << flashcard
		Collections.shuffle(selections, new Random())
		return selections
	}

    static constraints = {
    }
}
