package com.openfluency.flashcard

import com.openfluency.language.Language
import com.openfluency.auth.User

class Deck {

	String title
	String description
	User owner

	Language language

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
