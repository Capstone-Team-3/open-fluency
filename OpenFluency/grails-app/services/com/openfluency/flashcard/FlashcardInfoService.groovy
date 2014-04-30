package com.openfluency.flashcard

import grails.transaction.Transactional
import com.openfluency.auth.User
import com.openfluency.algorithm.CardServer
import com.openfluency.algorithm.AlgorithmService
import com.openfluency.course.Course
import com.openfluency.course.Chapter
import com.openfluency.Constants

@Transactional
class FlashcardInfoService {

	def algorithmService

    /**
    *  This method uses the Deck instance to look up the CardServer algorithm it is using and call its
    *  .buildNewFlashcardInfo() method to properly initialize new flashcards being added to the user's queues
    *  @Param User - the User the info is being built for
    *  @Param Deck - the deck the card is in
    *  @Param Flashcard - the flashcard instance the FlashcardInfo object is mapping to
    */
    void addNewFlashcardInfo(User theUser, Deck deckInstance, Flashcard flashcardInstance) {
    	for (int theQueue = 0; theQueue < Constants.CARD_ELEMENTS.size(); theQueue++){
    		double nextPriority = this.nextFlashcardInfo(theUser, deckInstance, theQueue)?.viewPriority ?: 1.0
    		CardServer csAlgo = algorithmService.cardServerByName(deckInstance.cardServerName)
    		FlashcardInfo newFCI = csAlgo.buildNewFlashcardInfo(theUser, deckInstance, theQueue, 
                                                                flashcardInstance, nextPriority)
    		newFCI.save(flush: true)
    	}
    }

    /**
    *  This method is called when a new card it added to a  deck - ensures all users of a deck will have it added to their queues
    *  @Param Deck - the deck the card belongs to
    *  @Param Flashcard - the flashcard being added
    */
    void addNewFlashcardInfoAllDeckUsers(Deck deckInstance, Flashcard flashcardInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { addNewFlashcardInfo(it.receiver, deckInstance, flashcardInstance) }
    	addNewFlashcardInfo(deckInstance.owner, deckInstance, flashcardInstance)
    }

    /**
    *  Removes the FlashcardInfo for a card from a user's queues.  Called if a card is deleted from a deck or user stops following a deck
    *  @Param User - the user of relevance
    *  @Param Deck - the deck the card is in
    *  @Param Flashcard - the flashcard being removed
    */
    void removeFlashcardInfo(User theUser, Deck deckInstance, Flashcard flashcardInstance){
    	def infos = FlashcardInfo.findAllByUserAndDeckAndFlashcard(theUser, deckInstance, flashcardInstance)
    	infos?.each { it.delete(flush: true) }
    }

    /**
    *  Removes a flashcard from all user queues for the deck - called when deck owner removes a card
    *  @Param Deck - the deck the card is in
    *  @Param Flashcard - the flashcard being removed
    */
    void removeFlashcardInfoAllDeckUsers(Deck deckInstance, Flashcard flashcardInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { removeFlashcardInfo(it.receiver, deckInstance, flashcardInstance) }
    	removeFlashcardInfo(deckInstance.owner, deckInstance, flashcardInstance)	
    }

    /**
    *  Removes all FlashcardInfo objects tied to a deck for a user- called if a user drops the Deck
    *  @Param User - the user of relevance
    *  @Param Deck - the deck being dropped - note the deck is not being removed for other users
    */
    void removeDeckFlashcardInfo(User theUser, Deck deckInstance){
    	def infos = FlashcardInfo.findAllByUserAndDeck(theUser, deckInstance)
    	infos?.each { it.delete(flush: true) }
    }

    /**
    *  Removes all FlashcardInfo objects for a deck - called when deck is deleted from the system
    *  @Param Deck - the deck to be removed from the system
    */
    void removeDeckFlashcardInfoAllUsers(Deck deckInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { removeDeckFlashcardInfo(it.receiver, deckInstance) }
    	removeDeckFlashcardInfo(deckInstance.owner, deckInstance)	
    }

    /**
    *  Resets the FlashcardInfo objects for a user of a specific deck
    *  @Param User - the user of relevance
    *  @Param Deck - the deck that has been reset or re-enrolled in
    */
    void resetDeckFlashcardInfo(User theUser, Deck deckInstance){
    	removeDeckFlashcardInfo(theUser, deckInstance)
    	deckInstance.getFlashcards()?.each { addNewFlashcardInfo(theUser, deckInstance, it) }
    }

    /**
    *  Resets the FlashcardInfo objects for a User for all chapters in a course - used on re-enrollment etc
    *  @Param User - the user of relevance
    *  @Param Course - the course that has been reset or re-enrolled in
    */
    void resetCourseFlashcardInfo(User theUser, Course theCourse){
    	def chapters = theCourse.getChapters()
    	chapters?.each { resetDeckFlashcardInfo(theUser, it.deck) }
    }

    /**
    *  This method is exceptionally important in the OpenFluency System - this method call gets the next / top
    *  FlashcardInfo in the relevant priority queue.  This is ultimately how flashcards are served in practice mode.
    *  @Param User - the relevant user
    *  @Param Deck - the deck we are looking for the next flashcard from
    *  @Param int - the queue from which we need the next card - queues map to Contants.CARD_ELEMENTS 
    */
    FlashcardInfo nextFlashcardInfo(User theUser, Deck deckInstance, int theQueue){
    	def infos = getFlashcardInfoQueue(theUser, deckInstance, theQueue)
    	return (infos?.size() > 0) ? infos[0] : null
    }

    List getFlashcardInfoQueue(User theUser, Deck deckInstance, int theQueue){
    	List infos = FlashcardInfo.findAllByUserAndDeckAndQueue(theUser, deckInstance, theQueue)?.sort{it.viewPriority}
        return infos
    }

    boolean updateViewedFlashcardInfo(Deck deckInstance, CardUsage cardUsageInstance){
    	def theInfo = nextFlashcardInfo(cardUsageInstance.user, deckInstance, cardUsageInstance.rankingType)
    	if (theInfo) {
    		CardServer csAlgo = algorithmService.cardServerByName(deckInstance.cardServerName)
            csAlgo.updateFlashcardInfo(theInfo, cardUsageInstance).save(flush: true)
    		return true
    	} else {
    		return false
    	}
    }


}
