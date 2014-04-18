package com.openfluency.flashcard

import com.openfluency.Constants
import com.openfluency.auth.User

/**
* When the user views a flashcard, it selects the difficulty level. When this happens
* we create a new CardUsage object which records the ranking and the total time viewed.
* CardRankingCurrent holds the last ranking the user gave to the flashcard
*/
class CardRankingCurrent {

	Flashcard flashcard
	User user

	Integer ranking 	// the difficulty level the user assigned to the flaschard

	Date dateCreated
	Date lastUpdated

    static constraints = {
    }

    static mapping = {
    	ranking defaultValue: Constants.UNRANKED
    }
}
