package com.openfluency.language

/**
 *  Alphabet is a core domain class in the system's language structure.  It is used to handle the fact
 *  that some languages have more than one alphabet, and there are Alphabet's, such as the 
 *  international phonetic alphabet, that could be useful in the system.  Alphabets are used in pronunciations
 */
class Alphabet {
	/** the Language to which this Alphabet belongs */
	Language language 				
	/** a short hand or descriptive name of an alphabet - eg pinyin, ja_kun, etc */
	String code						
	/** a formal name for the alphabet - egg Kanji, Katakana, etc */
	String name 					
	/** Defines if the Units of this alphabet should be encoded into HTML entities */
	Boolean encodeEntities = false	

    String toString() { name }

    static constraints = {
    }
}
