package com.openfluency.algorithm

import com.openfluency.flashcard.*

/**
*  CardServiceAlgorithm serves as a wrapper class that all algorithm implementations must work in.
*  All spaced repetition algorithms, or any other algorithms for serving flashcards must be adapted to this interface.  Implementations
*  interact with FlashcardInfo, CardUsage and UserDeckQueues objects - please refer to their details and to the provided example implementaions
*  and docs for more usage details
*/
class CardServiceAlgorithm {

	def algorithmService
	
	//Trick to make sure dependancy injection for algorithmService works EVERY TIME 
	static AlgorithmService initializeService(){
		def grailsApplication = new CardServiceAlgorithm("NONE").domainClass.grailsApplication
		def ctx = grailsApplication.mainContext
		return ctx.algorithmService
	}

	String cardServerConstantName

	CardServiceAlgorithm(int cardServerConstant){
		this.cardServerConstantName = Constants.CARD_SERVERS[cardServerConstant]
		algorithmService = initializeService()
	}

	CardServiceAlgorithm(String cardServerName){
		this.cardServerConstantName = cardServerName
		if (cardServerName != "NONE") {
			algorithmService = initializeService()
		}
	}

	/**
	*  This is where the algo initiailizes new FlashcardInfo elements to be held in the priority queues.
	*  If defining your own CardServer Algorithm, you can add any fields you need to the FlashcardInfo.data data map.
	*  @param flashcardInstance - the Flashcard for which this FlashcardInfo will be tied
	*  @return FlashcardInfo - the new flashcardInfo instance
	*/
    FlashcardInfo buildNewFlashcardInfo(Flashcard flashcardInstance, double nextCardPriority){
    	return algorithmService.cardServerByName(cardServerConstantName).buildNewFlashcardInfo(flashcardInstance, nextCardPriority)
    }

    /**
	*  This is the function that updates a FlashcardInfo instance based on the basis of your
	*  algorithm and the CardUsage info that is available - this includes 'ranking [1,2,3]', rankingType and time spent on card.
	*  IT IS IMPORTANT TO UPDATE THE FlashcardInfo.viewPriority OR THE PRESENTATION ORDER WILL NEVER CHANGE
	*  Keep in mind that the LOWER .viewPriority is set, the SOONER the card will be seen
	*  It is also important to invoke .save() on the FlashcardInfo instance after the update
	*  @param fInfoInstance - FlashcardInfo on which to get and update values - including .viewPriority
	*  @param cUsageInstance - CardUsage from which you can access some user that may inform the algo
	*  @return FlashcardInfo - the updated instance
	*/
	FlashcardInfo updateFlashcardInfo(FlashcardInfo fInfoInstance, CardUsage cUsageInstance){
		return algorithmService.cardServerByName(cardServerConstantName).updateFlashcardInfo(fInfoInstance, cUsageInstance)
	}

    String algoName() { 
    	return algorithmService.cardServerByName(cardServerConstantName).algoName() 
    }

    String toString() {
    	return algorithmService.cardServerByName(cardServerConstantName).toString()
    }

    static constraints = {
    	cardServerConstantName nullable: false
    }
}
