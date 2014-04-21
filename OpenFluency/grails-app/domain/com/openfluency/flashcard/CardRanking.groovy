package com.openfluency.flashcard

import com.openfluency.Constants
import com.openfluency.auth.User

/**
* When the user views a flashcard, it selects the difficulty level. When this happens
* we create a new CardUsage object which records the ranking and the total time viewed.
* CardRanking holds the last ranking the user gave to the flashcard
*/
class CardRanking {

	Flashcard flashcard
	User user

	Integer meaningRanking 			// the difficulty level the user assigned to the meaning of a flaschard 
	Integer pronunciationRanking 	// the difficulty level the user assigned to the pronunciation of a flaschard 

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	meaningRanking nullable: true
    	pronunciationRanking nullable: true
    }

    static mapping = {
    	ranking defaultValue: Constants.UNRANKED
    }
}
