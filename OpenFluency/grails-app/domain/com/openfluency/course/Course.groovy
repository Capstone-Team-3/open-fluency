package com.openfluency.course

import com.openfluency.auth.User
import com.openfluency.language.Language

class Course {

	User owner

	String title
	String description

	Language language

	Date startDate
	Date endDate

	Date dateCreated
	Date lastUpdated

	String getCourseNumber() {
		return "${language.code}-${this.id}"
	}

	List<Chapter> getChapters() {
		return Chapter.findAllByCourse(this)
	}

    static constraints = {
    	startDate nullable: true
    	endDate nullable: true
    }
}
