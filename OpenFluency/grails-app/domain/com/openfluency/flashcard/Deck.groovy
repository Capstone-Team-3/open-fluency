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

	/**
	* Get a list of all the flashcards in this deck
	*/
	List<Flashcard> getFlashcards() {
		Flashcard.findAllByDeck(this)
	}

	/**
	* Returns how many cards are in this deck
	*/
	Integer getFlashcardCount() {
		Flashcard.countByDeck(this)	
	}
}
