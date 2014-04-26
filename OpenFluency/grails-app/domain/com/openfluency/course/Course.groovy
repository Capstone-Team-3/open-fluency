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

	Boolean visible 	// this defines if the course appears on search results
	Boolean open 	// if the course is open, anyone can register. Otherwise the instructor will have to approve the registration

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
