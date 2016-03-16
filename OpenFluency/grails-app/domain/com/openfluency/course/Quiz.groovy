package com.openfluency.course

import com.openfluency.Constants

/** Quiz objects are a major domain component of Courses.  
 *  Quizzes support multiple choice tests
 */
class Quiz {
	/** the Course a Quiz belongs to */
	Course course
	String title
	/** This indicates if in this test we're testing pronunciation, meaning, or symbol */
	Integer testElement // TODO remove this - question type should be determined by the question, not the quiz
	/** If the current time is less than the liveTime, the test will not be viewable */
	Date liveTime		
	/** This allows control using a checkbox instead of time to be able to disable the test when needed */
	boolean enabled 	
	/** Used to enable time limits on quiz questions
	 *  If the user specifies a time greater than 0 here, there will be a counter in the flashcards and if the user 
	 *  submits the answer after the counter reaches 0 then it will be counted as incorrect
	 */
	Integer maxCardTime 
	/**
	 *  @Return a list of all the question in a Quiz
	 */
	List<Question> getQuestions() {
		Question.findAllByQuiz(this)
	}
	/** @Return the number of questions in a quiz */
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