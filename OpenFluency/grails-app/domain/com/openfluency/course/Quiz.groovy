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

	/**
	* Returns the test element to use during the test. If the test element is random, then a random test element is returned
	*/
	Integer getEffectiveTestElement() {
		if(testElement == Constants.RANDOM) {
			Random rand = new Random()
			int max = Constants.CARD_ELEMENTS.size()
			return rand.nextInt(max-1)
		} 
		else {
			return testElement
		}
	}

    static constraints = {
    	liveTime nullable: true
    }
}