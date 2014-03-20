package com.openfluency.language

class Unit {

	Alphabet alphabet 	// Kanji, Katakana, etc
	
	String literal 		// This is the literal character, word or phrase in the alphabet. For alphabets that require 
                        // unicode encoding, this will be the unicodes (4e9c, 5516, etc)

	Integer grade 		// What grade this unit is taught in
	Integer strokeCount // Number of strokes required to write this Unit
	Integer frequency 	// How frequently this Unit is used

    static constraints = {
        literal unique: true
    	frequency nullable: true
    	strokeCount nullable: true
    	grade nullable: true
    }

    /**
	* This allows getting the language quicker -> unit.language
    */
    Language getLanguage() {
    	return this.alphabet.language
    }

    /** 
    * @return a list of all the Units mapped to this Unit
    */
    List getMappings() {
        // The idea here is: first find all the UnitMappings where either Unit in the mapping is this Unit
        // After that, get the Unit from the mapping that is not this Unit
        return UnitMapping.findAllByUnit1OrUnit2(this, this).collect { 
            it.unit1 == this  ? it.unit2 : it.unit1 
        }
    }

    List getPronunciations() {
        return Pronunciation.findAllByUnit(this)
    }

    String getPrint() {
        return alphabet.encodeEntities ? "&#x${literal};".decodeHTML() : literal
    }
}
