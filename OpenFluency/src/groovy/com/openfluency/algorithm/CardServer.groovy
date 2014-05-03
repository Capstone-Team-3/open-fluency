package com.openfluency.algorithm

import com.openfluency.flashcard.*
import com.openfluency.auth.User

/**
*  CardServer is the interface that all spaced repetition, or any other type of card service and ordering algorithms, must implement.
*  The most important thing to understand is the role of FlashcardInfo.viewPriority.  The system treats FlashcardInfo objects
*  which are tied to a user and a deck, as the components of a Priority Queue, where the Flashcard linked to the FlashcardInfo
*  with the LOWEST .viewPriority is viewed / served next to the user.  Lower FlashcardInfo.viewPriority -> viewed sooner.
*  The updateFlashcardInfo(...) method in this class is where a user can get data needed for their Card Service algorithm
*  and where they must update, at a minimum, FlashcardInfo.viewPriority - if you don't do this, the user will never see a different card.
*  Please read the API docs for FlashcardInfoService, FlashcardInfo and CardUsage to ensure you understand their roles in the system.
*  Refer to the provided 'LinearWithSuffle.groovy' for a simple example CardServer implementation, and 'SM2SpacedRepetition.groovy'
*  for an example of a more complex CardServer implementation.
*  and docs for more usage details
*/
interface CardServer {

	/**  You must name your algorithm.  This will be used in the system, so make it unique, descriptive, presentable and readable.
	*/
	final String name;

	/**
	*  This is where the algo initiailizes new FlashcardInfo elements to be held in the priority queues.
	*  See FlashcardInfo to learn exactly what parameters are available to you.  FlashcardInfo does have string vars that could be used
	*  to store arbitrarily complex data (eg in JSON format) should need be.
	*  @param User - the user that the Flashcard info will map to the flashcard and deck***ALWAYS set FlashcardInfo.user to this
	*  @param Deck - the deck that the Flashcard belongs to***ALWAYS set FlashcardInfo.deck to this value
	*  @param int - representing the Queue the card belongs in - these map directly to the CARD_ELEMENTS constants***ALWAYS set FlashcardInfo.queue to this value
	*  @param flashcardInstance - the Flashcard for which this FlashcardInfo will be tied***ALWAYS set FlashcardInfo.flashcard to this value
	*  @param double - the .viewPriority of the top card on the queue, as it might be a useful reference for initialization
	*  @return FlashcardInfo - the new flashcardInfo instance
	*/
    FlashcardInfo buildNewFlashcardInfo(User theUser, Deck deckInstance, int theQueue, 
    									Flashcard flashcardInstance, double nextCardPriority);

    /**
	*  This is the function that updates a FlashcardInfo instance based on the basis of your
	*  algorithm and the CardUsage info that is available - this includes user provided ranking ([1,2,3]), 
	*  rankingType (which provides the Queue #) and time spent on card.
	*  IT IS IMPORTANT TO UPDATE THE FlashcardInfo.viewPriority OR THE PRESENTATION ORDER WILL NEVER CHANGE
	*  Keep in mind that the LOWER .viewPriority is set, the SOONER the card will be seen
	*  The system saves the updates after calling this method, so no need to invoke a save here.
	*  @param fInfoInstance - FlashcardInfo on which to get and update values - including .viewPriority
	*  @param cUsageInstance - CardUsage from which you can access some user that may inform the algo
	*  @return FlashcardInfo - the updated instance
	*/
	FlashcardInfo updateFlashcardInfo(FlashcardInfo fInfoInstance, CardUsage cUsageInstance);

	/**  Should return the name of the CardServer
	*/
    String algoName();

    /**  Please override toString to something useful.
    */
    String toString();
}