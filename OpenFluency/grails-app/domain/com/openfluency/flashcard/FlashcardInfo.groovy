package com.openfluency.flashcard

import groovy.transform.EqualsAndHashCode
import com.openfluency.auth.User

/**
*  This class is used inside the priority queues of UserDeckQueues.  It maps a Flashcard to a ranking
*  called 'viewPriority' - this is used to determine the order in which Flashcards are served.
*  The LOWER the .viewPriority, the SOONER the card will be chosen -> this is to match the natural language
*  on spaced repetition algorithms that tend to set offsets such as 'view this card again in +X time periods'
*
*  This class has a 'data' parameter which is a map that can be used in whatever manner is needed
*  by a CardServiceAlgorithm implementation.  The only requirement for proper card service is that
*  the CardServiceAlgorithm updates the FlashcardInfo.viewPriority after a user views the corresponding
*  flashcard or the user will never see a new flashcard -> this happens in using CardServiceAlgorithm.updateFlashcardInfo
*  in the UserDeckQueues.updateCurrentFlashcardInfo method.  Please refer to the other classes, examples and docs for more detail 
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
