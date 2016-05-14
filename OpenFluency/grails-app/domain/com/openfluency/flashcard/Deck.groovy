package com.openfluency.flashcard

import com.openfluency.language.Language
import com.openfluency.auth.User

/**
 *  Deck is a major domain object in this system.  Decks hold collections of flashcards
 *  and are a key component in the operation of the system
 */
class Deck {

	String title
	String description
	/** the User who created and owns the deck */
	User owner
	/** the CardServer, by string name, that the deck uses to server cards
     *  All CardServers provide access to their name, and can be looked up by name through AlgorithmService
	 */
	String cardServerName
	static constraints = {
    	cardServerName nullable: false
    }
	/** the language the user is trying to learn */
	Language language 
	/** the language the user already knows */
	Language sourceLanguage 

	Date dateCreated
	Date lastUpdated
	
	Boolean privateDeck = false;

	/**
	* Get a list of all the flashcards in this deck
	*  @Return a list of Flashcards
	*/
	List<Flashcard> getFlashcards() {
		Flashcard.findAllByDeck(this)
	}

	/**
	*  @Return a count of how many cards are in this deck
	*/
	Integer getFlashcardCount() {
		Flashcard.countByDeck(this)	
	}

	String toString() {return title}
}
