package com.openfluency.flashcard

import com.openfluency.algorithm.CardServiceAlgorithm
import com.openfluency.auth.User
import com.openfluency.Constants
import java.util.*
import java.util.Map.Entry

/**
*  This class provides the required queues for serving flashcards via various spaced repetition algorithms during practice
*  This works in concert with CardServiceAlgorithm instantiations, FlashcardInfo and CardUsage.  Please see docs for more info.
*/

class UserDeckQueues {

	User user
	Deck deck 
	private Map<String, SortedSet<FlashcardInfo>> queues

	UserDeckQueues (User userInstance, Deck deckInstance){
		this.user = userInstance
		this.deck = deckInstance
		this.queues = new TreeMap<String, SortedSet<FlashcardInfo>>()
		//add a queue for every testable/learnable card element
		Constants.CARD_ELEMENTS.each { this.queues.put(it,new TreeSet<FlashcardInfo>()); }
	}

    static constraints = {
    	user nullable: false
    	deck nullable: false
    }

    /** This method is to be invoked whenever an new flashcard is added to a deck - all UserDeckQueues for the Deck must be updated
    * @param flashcardInstance - the new flashcard added to the deck
    * @param algoInstance - the CardServiceAlgorithm being used by the deck
    */
    void addNewFlashcardToQueues(Flashcard flashcardInstance, CardServiceAlgorithm algoInstance){
    	//add an algo built and initialized FlashcardInfo for the Flashcard instance to each queue
    	for (Entry<String, SortedSet<FlashcardInfo>> entry : this.queues.entrySet()){
    		def aQueue = entry.getValue()
    		aQueue.add(algoInstance.buildNewFlashcardInfo(flashcardInstance))
    	}
    }

    /**  This method will return the FlashcardInfo with the HIGHEST viewPriority value - important to note this! Cards to be seen sooner need Higher priority values
    * @param integer elementQueueCode - the int corresponding to the appropriate Constant Rankable Card elements contained in the Constants.CARD_ELEMENTS list 
    * @return FlashcardInfo of the highest priority card in the given queue
    */
    FlashcardInfo nextFlashcard(int elementQueueCode){
    	return this.queues.get(Constants.CARD_ELEMENTS[elementQueueCode]).last()
    }

    boolean updateViewedFlashcard(CardServiceAlgorithm algoInstance, CardUsage cUsageInstance){
    	//get the top priority flashcardInfo - its the one that was just viewed
    	int aQueue = (cUsageInstance.rankingType as int)
    	FlashcardInfo infoToUpdate = nextFlashcard(aQueue)
    	//the infoToUpdate should look at same flashcard as cUsageInstance
    	if (infoToUpdate.flashcard.id == cUsageInstance.flashcard.id){
	    	this.queues.get(Constants.CARD_ELEMENTS[aQueue]).remove(infoToUpdate)
	    	infoToUpdate = algoInstance.updateFlashcardInfo(infoToUpdate, cUsageInstance)
	    	this.queues.get(Constants.CARD_ELEMENTS[aQueue]).add(infoToUpdate)
	    	return true
    	} else {
    		log.info "We have a problem with queue integrity - queue updates suspended"
    		return false
    	}
    }
}
