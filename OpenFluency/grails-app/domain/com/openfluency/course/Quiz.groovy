package com.openfluency.course

import com.openfluency.Constants

class Quiz {

	Course course
	String title

	Integer testElement 	// This indicates if in this test we're testing pronunciation, meaning, or symbol
	
	Date liveTime		// If the current time is less than the liveTime, the test will not be viewable
	boolean enabled 	// This allows control using a checkbox instead of time to be able to disable the test when needed
	Integer maxCardTime // If the user specifies a time greater than 0 here, there will be a counter in the flashcards and if the user 
						// submits the answer after the counter reaches 0 then it will be counted as incorrect

	List<Question> getQuestions() {
		Question.findAllByQuiz(this)
	}

	Integer countQuestions() {
		Question.countByQuiz(this)
	}

    static constraints = {
    	liveTime nullable: true
    }
}