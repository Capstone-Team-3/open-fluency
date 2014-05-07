package com.openfluency.course

import com.openfluency.flashcard.Deck
import com.openfluency.auth.User

/**
 *  The chapter domain class acts as a thin wrapper around a deck which
 *  allows decks to be added to courses
 */
class Chapter {
	/** only one deck can be assigned to a Chapter */
	Deck deck 	
	/** Courses the chapter belongs in */
	Course course 

	String title
	String description

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }
}
