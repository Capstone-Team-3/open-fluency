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

    void addNewFlashcardInfo(User theUser, Deck deckInstance, Flashcard flashcardInstance) {
    	for (int theQueue = 0; theQueue < Constants.CARD_ELEMENTS.size(); theQueue++){
    		double nextPriority = this.nextFlashcardInfo(theUser, deckInstance, theQueue)?.viewPriority ?: 1.0
    		CardServer csAlgo = algorithmService.cardServerByName(deckInstance.cardServerName)
    		FlashcardInfo newFCI = csAlgo.buildNewFlashcardInfo(theUser, deckInstance, theQueue, 
                                                                flashcardInstance, nextPriority)
    		newFCI.save(flush: true)
    	}
    }

    void addNewFlashcardInfoAllDeckUsers(Deck deckInstance, Flashcard flashcardInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { addNewFlashcardInfo(it.receiver, deckInstance, flashcardInstance) }
    	addNewFlashcardInfo(deckInstance.owner, deckInstance, flashcardInstance)
    }

    void removeFlashcardInfo(User theUser, Deck deckInstance, Flashcard flashcardInstance){
    	def infos = FlashcardInfo.findAllByUserAndDeckAndFlashcard(theUser, deckInstance, flashcardInstance)
    	infos?.each { it.delete(flush: true) }
    }

    void removeFlashcardInfoAllDeckUsers(Deck deckInstance, Flashcard flashcardInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { removeFlashcardInfo(it.receiver, deckInstance, flashcardInstance) }
    	removeFlashcardInfo(deckInstance.owner, deckInstance, flashcardInstance)	
    }

    void removeDeckFlashcardInfo(User theUser, Deck deckInstance){
    	def infos = FlashcardInfo.findAllByUserAndDeck(theUser, deckInstance)
    	infos?.each { it.delete(flush: true) }
    }

    void removeDeckFlashcardInfoAllUsers(Deck deckInstance){
    	def shares = Share.findAllByDeck(deckInstance)
    	shares?.each { removeDeckFlashcardInfo(it.receiver, deckInstance) }
    	removeDeckFlashcardInfo(deckInstance.owner, deckInstance)	
    }

    void resetDeckFlashcardInfo(User theUser, Deck deckInstance){
    	removeDeckFlashcardInfo(theUser, deckInstance)
    	deckInstance.getFlashcards()?.each { addNewFlashcardInfo(theUser, deckInstance, it) }
    }

    void resetCourseFlashcardInfo(User theUser, Course theCourse){
    	def chapters = theCourse.getChapters()
    	chapters?.each { resetDeckFlashcardInfo(theUser, it.deck) }
    }

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
