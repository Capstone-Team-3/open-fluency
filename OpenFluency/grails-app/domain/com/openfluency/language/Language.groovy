package com.openfluency.language

class Language {

	String name
	String code

	String toString() { 
		name 
	}

	List<Alphabet> getAlphabets() {
		return Alphabet.findAllByLanguage(this)
	}

	static constraints = {
	}
}
