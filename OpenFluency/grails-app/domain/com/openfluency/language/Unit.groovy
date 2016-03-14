package com.openfluency.language

import com.openfluency.flashcard.Flashcard

/**
 *  Unit objects are key in our domain model (see uml and user guides).  Units can repesent any element or composition
 *  of elements in a language (letter, word, phrase)
 */
class Unit {
    /** the alphabet in which the unit is defined - eg Kanji, Katakana, etc */
	Alphabet alphabet 	
	UnitType type;
	Flashcard flashcard;
	/** 
     *  This is the literal character, word or phrase in the alphabet. For alphabets that require 
     *  unicode encoding, this will be the unicodes (4e9c, 5516, etc)
     */
	String literal 		
    /** What grade this unit is typically taught in */
	Integer grade 		
    /** Number of strokes required to write this Unit */
	Integer strokeCount 
	/** How frequently this Unit is used */
    Integer frequency 	

    static constraints = {
        literal unique: true
    	frequency nullable: true
    	strokeCount nullable: true
    	grade nullable: true
		type  nullable: true // For now, but should have a meaning
		flashcard nullable: true // For now
    }

    /**
	* This allows getting the language quicker -> unit.language
    *  @Return the Language
    */
    Language getLanguage() {
    	return this.alphabet.language
    }

    /**
    * @return a list of all the UnitMappings where one of the units is this
    */
    List<UnitMapping> getUnitMappings() {
        return UnitMapping.findAllByUnit1OrUnit2(this, this)
    }

    /** 
    * @return a list of all the Units mapped to this Unit
    */
    List<Unit> getMappings() {
        // The idea here is: first find all the UnitMappings where either Unit in the mapping is this Unit
        // After that, get the Unit from the mapping that is not this Unit
        return this.unitMappings.collect { 
            it.unit1 == this  ? it.unit2 : it.unit1 
        }
    }

    /** @Return a list of all the pronunciations tied to this unit in the system */ 
    List<Pronunciation> getPronunciations() {
        return Pronunciation.findAllByUnit(this)
    }

    /**
     *  This function conviniently detects whether decoding is needed for the unit and provides a useable text String
     *  @Return a string of the Unit
     */
    String getPrint() {
        return alphabet.encodeEntities ? "&#x${literal};".decodeHTML() : literal
    }
}
