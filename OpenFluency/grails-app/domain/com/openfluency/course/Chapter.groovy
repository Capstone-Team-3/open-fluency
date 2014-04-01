package com.openfluency.course

import com.openfluency.flashcard.Deck
import com.openfluency.auth.User

class Chapter {

	Deck deck 	// only one deck can be assigned to a Course
	Course course // Courses can have many chapters

	String title
	String description

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
