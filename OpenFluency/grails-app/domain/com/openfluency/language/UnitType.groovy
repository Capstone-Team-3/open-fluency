package com.openfluency.language

/**
 *  Unit type is a domain class in the flashcard structure.
 *  Cards can have multiple units and not all of them are of one alphabet
 *  Unit types can be sample sentence, grammar hints, etc 
 */
class UnitType {
	/** a short hand or descriptive name */
	String code						
	/** a decription for the field */
	String name 					
	/** Defines if the Units of this alphabet should be encoded into HTML entities */
    String toString() { name }

    static constraints = {
    }
}
