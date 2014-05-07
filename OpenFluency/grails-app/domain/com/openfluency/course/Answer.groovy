package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.flashcard.Flashcard
import com.openfluency.auth.User

/**
 *  This domain class is used to track answers to quizzes.
 *
 *
 */
class Answer {
	/** the user answering */
	User user 				
	/** the question being answered */
	Question question 		
	/** the option that the user chose between the questions options */
	Flashcard selection 	
	/** Status of the answer */
	Integer status 		// Status of the answer for a given user:
						// NOT_ANSWERED: 	As soon as the user starts a quiz, we create an answer 
						//					for every question in it with this status
						// VIEWED: 			When the user requests a question, we change the status 
						// 					to VIEWED. The user will only be able to answer a question if the status 
						// 					is VIEWED and the session matches the session of the Answer
						// ANSWERED: 		After the user answers the question, we change the status to this
	/** The session that the user was in when the answer was created */
	String sessionId 	
	/** when the question was started */
	Date dateCreated
	/** when the question was answered */
	Date lastUpdated

    static constraints = {
    	selection nullable: true
    	status defaultValue: Constants.NOT_ANSWERED
    }
    /**
     *  @Return a boolean indicating if the answer was correct
     */
    Boolean isCorrect() {
    	return this.selection.id == this.question.flashcard.id
    }
}
