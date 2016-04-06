package com.openfluency.course

import com.openfluency.flashcard.Flashcard

/**
 *  QuestionOption is a basic domain class that maps flashcards to multiple choice options on a Question
 */
class QuestionOption {
	/** The question that this option belongs to */
	Question question 	
	
	String option;
	int answerKey;	

    static constraints = {
		option blank: true
    }
}
