package com.openfluency.flashcard

import groovy.transform.EqualsAndHashCode

/**
*  This class is used inside the priority queues of UserDeckQueues.  It maps a Flashcard to a ranking
*  called 'viewPriority' - this is used to determine the order in which Flashcards are served.
*  This class has a 'data' parameter which is a map that can be used in whatever manner is needed
*  by a CardServiceAlgorithm implementation.  The only requirement for proper card service is that
*  the CardServiceAlgorithm updates the FlashcardInfo.viewPriority after a user views the corresponding
*  flashcard or the user will never see a new flashcard -> this happens in using CardServiceAlgorithm.updateFlashcardInfo
*  in the UserDeckQueues.updateCurrentFlashcardInfo method.  Please refer to the other classes, examples and docs for more detail 
*/
@EqualsAndHashCode
class FlashcardInfo implements Comparable<FlashcardInfo> {

	Flashcard flashcard
	double viewPriority
	def data = [:]

    static constraints = {
    	flashcard nullable: false
    	viewPriority nullable: false
    }

    @Override
    int compareTo(FlashcardInfo that){
    	return Double.compare(this.viewPriority, that.viewPriority)
    }
}
