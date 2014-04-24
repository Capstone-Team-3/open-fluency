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
        //if there are cards in a deck add them to the queues
        deckInstance.flashcards.each { addNewFlashcardToQueues(it, deckInstance.algorithm) }
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
    		def aQueueInstance = entry.getValue()
            //get the priority of the next card to be served from the queue - if the queue isn't empty, else return 1.0
            double nextCardPriority = (aQueueInstance.size() > 0) ? this.nextFlashcard(Constants.CARD_ELEMENTS.findIndexOf(entry.getKey())).viewPriority : 1.0
    		aQueueInstance.add(algoInstance.buildNewFlashcardInfo(flashcardInstance, nextCardPriority))
    	}
        this.save(flush: true)
    }

    /** Simply removes the FlashcardInfos for a given Flashcard from the queues - if they held it
    */
    void removeFlashcardFromQueues(Flashcard flashcardInstance) {
        for (Entry<String, SortedSet<FlashcardInfo>> entry : this.queues.entrySet()){
            def aQueueInstance = entry.getValue()
            FlashcardInfo removalCard = null
            for (FlashcardInfo fi : aQueueInstance) { if (fi.flashcard == flashcardInstance) { removalCard = fi } }
            if (fi != null) { aQueueInstance.remove(fi) }
        }
    }

    /**  This method will return the FlashcardInfo with the LOWEST viewPriority value - important to note this! Cards to be seen sooner need LOWER priority values
    * @param integer elementQueueCode - the int corresponding to the appropriate Constant Rankable Card elements contained in the Constants.CARD_ELEMENTS list 
    * @return FlashcardInfo of the next priority card in the given queue
    */
    FlashcardInfo nextFlashcard(int elementQueueCode){
    	return this.queues.get(Constants.CARD_ELEMENTS[elementQueueCode]).first()
    }

    /**  This method uses a CardServiceAlgorithm.updateFlashcardInfo to update the info for an instance in the priority queue. 
    *    It leverages the 'nextFlashcard' function to get the card to update.  This is the only method that should be used to update card info
    * @param algoInstance an instantiated CardServiceAlgorithm instance
    * @param cUsageInstance the CardUsage for the current card
    */
    boolean updateViewedFlashcard(CardServiceAlgorithm algoInstance, CardUsage cUsageInstance){
    	//get the top priority flashcardInfo - its the one that was just viewed
    	int aQueueNum = (cUsageInstance.rankingType as int)
    	FlashcardInfo infoToUpdate = nextFlashcard(aQueueNum)

    	//the infoToUpdate should look at same flashcard as cUsageInstance
    	if (infoToUpdate.flashcard.id == cUsageInstance.flashcard.id){
	    	this.queues.get(Constants.CARD_ELEMENTS[aQueueNum]).remove(infoToUpdate)
	    	
            //update the flashcardInfo
            infoToUpdate = algoInstance.updateFlashcardInfo(infoToUpdate, cUsageInstance)
	    	
            this.queues.get(Constants.CARD_ELEMENTS[aQueueNum]).add(infoToUpdate)
            this.save(flush: true)
	    	return true
    	} else {
    		log.info "We have a problem with queue integrity - queue updates suspended"
    		return false
    	}
    }
}
