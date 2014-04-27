package com.openfluency.algorithm

import com.openfluency.flashcard.*
import com.openfluency.auth.User

class LinearWithShuffle implements CardServer{

	final String name = "Linear-With-Shuffle"

	/**
	*  This is where the algo initiailizes new FlashcardInfo elements to be held in the priority queues. All cards are
	*  given the same priority to start.  If defining your own CardServiceAlgorithm, you can add any fields you need to the 
	*  FlashcardInfo.data data map.
	*  @param flashcardInstance - the Flashcard for which this FlashcardInfo will be tied
	*  @return FlashcardInfo - the new flashcardInfo instance
	*/
	FlashcardInfo buildNewFlashcardInfo(User theUser, Deck deckInstance, int theQueue,
										Flashcard flashcardInstance, double nextCardPriority) {
		def flashcardInfoInstance = new FlashcardInfo(flashcard: flashcardInstance,
													  user: theUser,
													  deck: deckInstance,
													  queue: theQueue,
													  algoName: deckInstance.cardServerName,
													  viewPriority: (nextCardPriority - 0.00001))
		return flashcardInfoInstance
	}

	/**
	*  This implementation of CardServiceAlgorithm simply inrements cards as viewed and adds a little random noise to shuffle the order
	*  Keep in mind that the LOWER .viewPriority is set, the SOONER the card will be seen
	*  @param fInfoInstance - FlashcardInfo on which to get and update values - including .viewPriority
	*  @param cUsageInstance - CardUsage from which you can access some user that may inform the algo
	*  @return FlashcardInfo - the updated instance
	*/
    FlashcardInfo updateFlashcardInfo(FlashcardInfo fInfoInstance, CardUsage cUsageInstance) {
    	java.util.Random rand = new java.util.Random()
    	//increment the priority by 1 and add a small bit of random noise to give a shuffling effect - note .viewPriority got bigger, so it moves back in the queue
    	fInfoInstance.viewPriority = fInfoInstance.viewPriority + 1.0 + ((rand.nextDouble() - 0.5) / 10.0) 
    	return fInfoInstance
    }

    String algoName(){
    	return name
    }

    String toString() {
    	return algoName()
    }
}