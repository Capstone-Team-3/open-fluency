package com.openfluency.course

import com.openfluency.flashcard.Flashcard

/**
 *  QuestionOption is a basic domain class that maps flashcards to multiple choice options on a Question
 */
class QuestionOption {
	/** The card used to confuse the user */
	Flashcard flashcard 	
	/** The question that this option belongs to */
	Question question 		

    static constraints = {
    }
}
