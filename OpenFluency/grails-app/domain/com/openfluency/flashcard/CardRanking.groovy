package com.openfluency.flashcard

import com.openfluency.Constants
import com.openfluency.auth.User

/**
* When the user views a flashcard, it selects the difficulty level. When this happens
* we create a new CardUsage object which records the ranking and the total time viewed.
* CardRanking holds the last ranking the user gave to the flashcard
*/
class CardRanking {
    /** the Flashcard being ranked */
	Flashcard flashcard
    /** the User ranking the flashcard */
	User user
    /** the difficulty level the user assigned to the symbol on the flashcard */
    Integer symbolRanking           
	/** the difficulty level the user assigned to the meaning of a flaschard */
    Integer meaningRanking 			 
	/** the difficulty level the user assigned to the pronunciation of a flaschard */
    Integer pronunciationRanking 	 

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	meaningRanking nullable: true
    	pronunciationRanking nullable: true
        symbolRanking nullable: true
    }

    static mapping = {
    	ranking defaultValue: Constants.UNRANKED
    }
}
