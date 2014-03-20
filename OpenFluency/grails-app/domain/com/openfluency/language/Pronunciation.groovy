package com.openfluency.language

/**
* A Pronunciation specifies how a Unit is pronounced in a given Alphabet
*/
class Pronunciation {

	Unit unit
	Alphabet alphabet 	// The Alphabet to which this pronunciation belongs to
	String literal 		// How it's pronounced

    static constraints = {
    }
}
