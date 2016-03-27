package com.openfluency.confuser;

import com.openfluency.language.Alphabet;

/**
 * This interface encapsulates a means of getting characters that are similar to a
 * given example for the purposes of confusing humans.
 */
interface ConfuserInterface {
	
	/**
	 * Get a random list of confusers of given type limited to the count 
	 * provided, these results are checked against the black list to 
	 * ensure that nothing inappropriate is returned. 
	 * 
	 * @param word The word (in the foreign language) that you want to generate confusers for
	 * @param alphabet The alphabet that the word is encoded in
	 * @param count The number that should be returned, if -1 is provided then
	 * all results are returned without processing.
	 * @return List of strings containing the confuser results, The length of the list will be between 0 and the requested count.
	 */
	List<String> getConfusers(String word, Alphabet alphabet, int count);
	
}
