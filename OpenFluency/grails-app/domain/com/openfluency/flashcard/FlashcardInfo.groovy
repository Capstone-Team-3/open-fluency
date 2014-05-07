package com.openfluency.flashcard

import groovy.transform.EqualsAndHashCode
import com.openfluency.auth.User

/**
*  This class is used to facilitate priority queues for users when they are learning a Deck of Flashcards.
*  There will essentially be 3 queues per user, per deck - 1 for each Constans.CARD_ELEMENT that a user might study on a flashcard.
*  .flashcard, .user, .deck and .queue must all be set for proper information tracking in the system.
*  *** .viewPriority is VERY IMPORTANT - this is used to determine the order in which Flashcards are served (ie order the queue). ***
*  *** The LOWER the .viewPriority, the SOONER the card will be chosen *** -> this is to match the natural language
*  on spaced repetition algorithms that tend to set offsets such as 'view this card again in +X time periods'
*
*  This class has a number of fields that match data numerous spaced repetition algorithms need.
*  It also provides 3 String 'data' fields to provide for flexability - a user could map arbitrarily complex data into the strings
*  in JSON or some other text format as needed.
*  
*  Due to the importance in the system, we reiterate -> the FlashcardInfo.viewPriority needs to be updated in a CardServer.updateFlashcardInfo() call.
*  If this is not done, the user will never see a new flashcard.  Please see CardServer and the provided implementations for details. 
*/
@EqualsAndHashCode
class FlashcardInfo implements Comparable<FlashcardInfo> {
    /** the Flashcard this info object is for */
	Flashcard flashcard
    /** the User interacting with the card and deck */
	User user
    /** the Deck that the flashcard belongs to */
    Deck deck
    /** the CardServer algorithm being used - identified by unique string name */
    String algoName
    /** the queue pertaining to the Flashcard Element being studied - Corresponds to learning mode in decks, Constants.CARD_ELEMENT */
    int queue
    /** the key field for ordering the priority queue - the lower the view priority, the sooner the card is seen - must be updated by SR algos */
    double viewPriority
    /** field available to store number of times the user has practiced the card */
    int numberOfRepetitions
    /** optional field available for calculating a difficulty / easiness ranking for the card based on user feedback */
    double easinessFactor
    /** optional - this field maps directly to a parameter needed by our out of the box SR algorithm */
    double interval
    /** three String data fields are provided to allow users flexible data storage in implementations of CardServer algorithms */
	String data1
    String data2
    String data3

    /** provides a detailed map of which fields are required or nullable */
    static constraints = {
    	flashcard nullable: false
        user nullable: false
        deck nullable: false
        algoName nullable: false
        queue nullable: false
    	viewPriority nullable: false
        numberOfRepetitions nullable: true
        easinessFactor nullable: true
        interval nullable: true
        data1 nullable: true
        data2 nullable: true
        data3 nullable: true
    }
    /** provides an override of the compareTo method for the Comparable interface - orders based on viewPriority */
    @Override
    int compareTo(FlashcardInfo that){
    	return Double.compare(this.viewPriority, that.viewPriority)
    }

    String toString(){
        return "FlashcardInfo : ${user} : ${deck} : ${queue} : ${viewPriority}"
    }
}
