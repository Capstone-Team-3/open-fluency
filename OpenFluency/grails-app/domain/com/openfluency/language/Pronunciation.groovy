package com.openfluency.language

/**
* A Pronunciation specifies how a Unit in one Alphabet is pronounced in another Alphabet
*/
class Pronunciation {

	Unit unit
	Alphabet alphabet 	// The Alphabet to which this pronunciation belongs to
	String literal 		// How it's pronounced

    static constraints = {
    }

    String toString(){
    	literal
    }

    String getPrint() {
        return alphabet.encodeEntities ? "&#x${literal};".decodeHTML() : literal
    }
}
