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

	Flashcard flashcard
	User user
    Deck deck
    String algoName
    int queue

    double viewPriority

    int numberOfRepetitions
    double easinessFactor
    double interval

	String data1
    String data2
    String data3

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

    @Override
    int compareTo(FlashcardInfo that){
    	return Double.compare(this.viewPriority, that.viewPriority)
    }

    String toString(){
        return "FlashcardInfo : ${user} : ${deck} : ${queue} : ${viewPriority}"
    }
}
