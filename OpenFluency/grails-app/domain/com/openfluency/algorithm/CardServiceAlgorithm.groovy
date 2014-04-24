package com.openfluency.algorithm

import com.openfluency.flashcard.*

/**
*  CardServiceAlgorithm serves as an abstract template class that all algorithm implementations to be used in the system must extend.
*  All spaced repetition algorithms, or any other algorithms for serving flashcards must be adapted to this interface.  Implementations
*  interact with FlashcardInfo, CardUsage and UserDeckQueues objects - please refer to their details and to the provided example implementaions
*  and docs for more usage details
*/
abstract class CardServiceAlgorithm {

    abstract FlashcardInfo buildNewFlashcardInfo(Flashcard flashcardInstance);

    abstract FlashcardInfo updateFlashcardInfo(FlashcardInfo fInfoInstance, CardUsage cUsageInstance);

    abstract String algoName();

    String toString() {
    	return algoName()
    }

    static constraints = {
    }
}
