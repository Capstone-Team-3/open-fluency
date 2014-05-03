package com.openfluency.algorithm

import com.openfluency.flashcard.*
import com.openfluency.auth.User

/**
*  This class is an example of an implementation of the CardServer interface as required for use in the OpenFluency system.
*  This implementation is basic - it simply adds 1 + some random noise to a card's FlashcardInfo.viewPriority to move it back 
*  in the priority queue with a little bit of 'randomized shuffling.'  This class shows a minimal implementation of CardServer
*  and is a good starting point for anyone looking to build their own ardServer Implementation.
*/

class LinearWithShuffle implements CardServer{

	final String name = "Linear-With-Shuffle"

	/**
	*  This is where the algo initiailizes new FlashcardInfo elements to be held in the priority queues. New cards are
	*  given a slightly lower .viewPriority than the current 'next card' - this has the effect of adding it to the top of
	*  the deck's queues.  Notice that the FlashcardInfo . flashcard, .user, .deck, .queue and .algoName are mapped exactly as required.
	*  @param User - the user that the Flashcard info will map to the flashcard and deck***ALWAYS set FlashcardInfo.user to this
	*  @param Deck - the deck that the Flashcard belongs to***ALWAYS set FlashcardInfo.deck to this value
	*  @param int - representing the Queue the card belongs in - these map directly to the CARD_ELEMENTS constants***ALWAYS set FlashcardInfo.queue to this value
	*  @param flashcardInstance - the Flashcard for which this FlashcardInfo will be tied***ALWAYS set FlashcardInfo.flashcard to this value
	*  @param double - the .viewPriority of the top card on the queue, as it might be a useful reference for initialization
	*  @return FlashcardInfo - the new flashcardInfo instance
	*/
	FlashcardInfo buildNewFlashcardInfo(User theUser, Deck deckInstance, int theQueue,
										Flashcard flashcardInstance, double nextCardPriority) {
		def flashcardInfoInstance = new FlashcardInfo(flashcard: flashcardInstance,
													  user: theUser,
													  deck: deckInstance,
													  queue: theQueue,
													  algoName: this.name,
													  viewPriority: (nextCardPriority - 0.00001))
		return flashcardInfoInstance
	}

	/**
	*  This implementation of CardServer simply inrements cards as viewed and adds a little random noise to shuffle the order
	*  Keep in mind that the LOWER .viewPriority is set, the SOONER the card will be seen
	*  @param fInfoInstance - FlashcardInfo on which to get and update values - including .viewPriority
	*  @param cUsageInstance - CardUsage from which you can access some user that may inform the algo
	*  @return FlashcardInfo - the updated instance
	*/
    FlashcardInfo updateFlashcardInfo(FlashcardInfo fInfoInstance, CardUsage cUsageInstance) {
    	java.util.Random rand = new java.util.Random()
    	//increment the priority by 1 and add a small bit of random noise to give a shuffling effect - note .viewPriority got bigger, so it moves back in the queue
    	fInfoInstance.viewPriority = fInfoInstance.viewPriority + 1.0 + ((rand.nextDouble() - 0.5) / 10.0)
    	fInfoInstance.numberOfRepetitions++ 
    	return fInfoInstance
    }

    String algoName(){
    	return name
    }

    String toString() {
    	return algoName()
    }
}