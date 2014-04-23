package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.flashcard.Flashcard
import com.openfluency.auth.User

class Answer {

	User user 				// the user answering
	
	Question question 		// the question being answered
	
	Flashcard selection 	// the option that the user chose between the questions options
	
	Integer status 		// Status of the answer for a given user:
						// NOT_ANSWERED: 	As soon as the user starts a quiz, we create an answer 
						//					for every question in it with this status
						// VIEWED: 			When the user requests a question, we change the status 
						// 					to VIEWED. The user will only be able to answer a question if the status 
						// 					is VIEWED and the session matches the session of the Answer
						// ANSWERED: 		After the user answers the question, we change the status to this

	String sessionId 	// The session that the user was in when the answer was created

    static constraints = {
    	selection nullable: true
    	status defaultValue: Constants.NOT_ANSWERED
    }
}
