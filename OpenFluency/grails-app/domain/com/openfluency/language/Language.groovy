package com.openfluency.language

/**
 *  Language is a core domain class in the system
 */
class Language {
	/** the Language name as it will be seen in the system - typically the English version of the name */
	String name
	/** the code for the Language - allows flexibility to have the native name of the language */
	String code

	String toString() { 
		name 
	}
	/**
	 *  Get the list of all Alphabet belonging to a Language and known by the system
	 *  @Return a List of Alphabets
	 */
	List<Alphabet> getAlphabets() {
		return Alphabet.findAllByLanguage(this)
	}

	static constraints = {
	}
}
