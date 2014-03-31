package com.openfluency.course

import com.openfluency.auth.User

class Course {

	User owner

	String title
	String description

	Date dateCreated
	Date lastUpdated

	List<Chapter> getChapters() {
		return Chapter.findAllByCourse(this)
	}

    static constraints = {
    }
}
