package com.openfluency.course

import com.openfluency.auth.User 

class Grade {

	Quiz quiz
	Integer correctAnswers
	User user

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
