package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.Constants

/**
* When the user views a flashcard, it selects the difficulty level. When this happens
* we create a new CardUsage object which records the ranking and the total time viewed
*/
class CardUsage {

	Flashcard flashcard
	User user

	String rankingType			// this can be meaning, pronunciation, etc depending on what the User is ranking for this card.
	Integer ranking 	// the difficulty level the user assigned to the flaschard (1,2,3)
	Date endTime

	Date dateCreated
	Date lastUpdated

	static constraints = {
		endTime nullable: true
		ranking nullable: true
	}

	static mapping = {
		ranking defaultValue: Constants.HARD
	}
}
