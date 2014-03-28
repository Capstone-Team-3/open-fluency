package com.openfluency.algorithm

import grails.transaction.Transactional
import groovy.time.TimeCategory
import com.openfluency.flashcard.Flashcard

/**
 * An implementation of the Spaced Repetition algorithm.
 * The algorithm is based on the SuperMemo 2 algorithm:
 * http://www.supermemo.com/english/ol/sm2.htm
 *
 * SM2 uses the student performance on a flashcard to determine the next time to schedule that flashcard
 *
 *
 */
@Transactional
class SpacedRepetitionSM2Service 

{
	/**
	 * The formula:
	 * *****************************
	 * I(1):=1
	 * I(2):=6
	 * for n>2 I(n):=I(n-1)*EF
	 * *****************************
	 *
	 * Where:
	 * I(n) - Inter-repetition interval after the n-th repetition (in days)
	 * EF 	- Easiness factor reflecting the easiness of memorizing and retaining
	 * 		  a given item in memory (also called the E-Factor).
	 *
	 * ******************************
	 * EF':=f(EF,q)
	 * EF':=EF-0.8+0.28*q-0.02*q*q
	 * ******************************
	 * Where:
	 * EF' - new value of the E-Factor
	 * EF - old value of the E-Factor
	 * f - function used in calculating EF'.
	 * q - quality of the response
	 * 	   0-5 score scale:	5 - perfect response
	 * 						4 - correct response after a hesitation
	 * 						3 - correct response recalled with serious difficulty
	 * 						2 - incorrect response; where the correct one seemed easy to recall
	 * 						1 - incorrect response; the correct one remembered
	 * 						0 - complete blackout.
	 *
	 *
	 */
	static final MAX_QUALITY_RANKING = 5
	static final MIN_EFACTOR = 1.3f
	static final INIT_EF = 2.5f
	static final FIRST_INTERVAL = 1
	static final SECOND_INTERVAL = 6
	
	/**
	 * Initiate a Flashcard with default values
	 *
	 * @param flashcard
	 */
	void initFlashcard(Flashcard flashcard)
	{
		flashcard.setNumberOfRepetitions(0)
		flashcard.setEFactor(INIT_EF)
		flashcard.setInterval(FIRST_INTERVAL)
		use(TimeCategory) 
		{
			flashcard.setNextRepetitionDate((new Date()) + FIRST_INTERVAL.days)
		}

		
	}
	/**
	 * Rank a Flashcard  - calculates the new EFactor and Interval
	 * 					   sets accordingly the next date to review the card
	 *
	 * @param flashcard
	 * @param quality
	 */
	void rankFlashcard(Flashcard flashcard, int quality) {
		calcuateInterval(flashcard,quality) 
		
		use(TimeCategory) {
			flashcard.setNextRepetitionDate( (new Date())+ flashcard.getInterval().days)
		}
	}
	/**
	* Set the new E-Factor.
	*
	* @param flashcard
	* @param quality - the ranking (0-5)
	*/
	void calcuateEFactor(Flashcard flashcard, int quality) {
	  float newFactor = flashcard.getEFactor() - 0.8 + (0.28 * quality) - (0.02 * quality * quality)
	  //If EF is less than MIN_EF then let EF be MIN_EF
	  if (newFactor < MIN_EFACTOR){
		  newFactor = MIN_EFACTOR
	  }

	  flashcard.setEFactor(newFactor)
	}
	/**
	 * Calculate the interval - the next time (in days) that the flashcard will be presented
	 *
	 * @param flashcard
	 * @param quality - the ranking (0-5)
	 */
	void calcuateInterval(Flashcard flashcard, int quality ) {
		
		//if quality < 3 then start repetitions for the item from the beginning without changing the E-Factor
		// (i.e. use intervals I(1), I(2) etc. as if the item was memorized anew)
		if (quality < 3) {
			flashcard.setNumberOfRepetitions(1)
		}
		// numberOfRepetitions = the number of times the flashcard was viewed
		int numberOfRepetitions = flashcard.getNumberOfRepetitions()
		
		if (numberOfRepetitions == 1) {
			interval = FIRST_INTERVAL
		}
		
		if (numberOfRepetitions == 2) {
			interval = SECOND_INTERVAL
		}
		
		else if (numberOfRepetitions > 2) {
		   //If interval is a fraction, round it up to the nearest integer.
		   interval =  (int)Math.ceil(flashcard.getInterval() * flashcard.getEFactor())
		}
		flashcard.setInterval(interval)
	}
	
}
