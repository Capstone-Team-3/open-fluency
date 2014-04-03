package com.openfluency.flashcard

import com.openfluency.language.Alphabet
import com.openfluency.auth.User

class Deck {

	String title
	String description
	User owner

	Alphabet alphabet

	Date dateCreated
	Date lastUpdated

	List<Flashcard> getFlashcards() {
		Flashcard.findAllByDeck(this)
	}

	Integer getFlashcardCount() {
		Flashcard.countByDeck(this)	
	}

    static constraints = {
    }
}
