package com.openfluency.language

class Alphabet {

	Language language 				// the Language to which this Alphabet belongs
	String code						// pinyin, ja_kun, etc
	String name 					// Kanji, Katakana, etc
	Boolean encodeEntities = false	// Defines if the Units of this alphabet should be encoded into HTML entities

    String toString() { name }

    static constraints = {
    }
}
