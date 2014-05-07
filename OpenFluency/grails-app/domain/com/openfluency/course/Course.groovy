package com.openfluency.course

import com.openfluency.auth.User
import com.openfluency.language.Language

/**
 *  The course domain class does what one expects - it exposes course management related
 *  fields and methods to get all the chapters in a course
 *
 */
class Course {
	/** the User who created the course */
	User owner

	String title
	String description
	/** the focal language of the course */
	Language language

	Date startDate
	Date endDate

	Date dateCreated
	Date lastUpdated
	/** defines whether the course is currently available in search results */
	Boolean visible
	/** defines 'openness' - anyone can register in an open course, instructors must approve registration in private ones */
	Boolean open 
	/** @Return a string representing the course number - a unique combo in the system */
	String getCourseNumber() {
		return "${language.code}-${this.id}"
	}
	/** @Return a list of the chapters in the course */
	List<Chapter> getChapters() {
		return Chapter.findAllByCourse(this)
	}

    static constraints = {
    	startDate nullable: true
    	endDate nullable: true
    }
}
