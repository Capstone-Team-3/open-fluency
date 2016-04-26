package com.openfluency.course

import com.openfluency.auth.User 

/**
 *  Grade is a basic domain class used to capture user performance on quizzes
 */
class Grade {
	/** the quiz being graded */
	Quiz quiz
	/** the total number questions on the quiz */
	Integer numberOfQuestions
	/** the number of answers answered correctly on the quiz */
	Integer correctAnswers
	/** the User whos grade this is */
	User user

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
