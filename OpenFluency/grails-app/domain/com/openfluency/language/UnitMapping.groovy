package com.openfluency.language

/**
* This class maps a Unit in one language to a Unit in another language. It specifies the meaning of a Unit in one language 
*  It should be noted that this is a bi-directional mapping - it is utalized by flashcards
*/
class UnitMapping {
	/** a language unit */
	Unit unit1
	/** a language unit */
	Unit unit2

    static constraints = {
    }

    String toString(){
    	"${unit1.getPrint()} <-> ${unit2.getPrint()}"
    }
}
