package com.openfluency.course

import com.openfluency.auth.User

/**
 *  The Registration domain object is used to facilitate user registration in courses
 */
class Registration {
	/** the Course being registered in */
	Course course
	/** the User registering */
	User user
	/** an integer flag representing the status of registration */
	Integer status

    static constraints = {
    }
}
