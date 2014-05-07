package com.openfluency.language

/**
* A Pronunciation specifies how a Unit in one Alphabet is pronounced in another Alphabet
*/
class Pronunciation {
    /** the language unit this pronunciation is being created for */
	Unit unit
    /** The Alphabet this pronunciation is written in */
	Alphabet alphabet 	
    /** the literal pronunciation as spelled out in the Alphabet defined */
	String literal

    static constraints = {
    }

    String toString(){
    	literal
    }
    /**
     *  This important convinience method looks up whether the specific alphabet needs decoding
     *  @Return a usable string of the pronunciation
     */
    String getPrint() {
        return alphabet.encodeEntities ? "&#x${literal};".decodeHTML() : literal
    }
}
