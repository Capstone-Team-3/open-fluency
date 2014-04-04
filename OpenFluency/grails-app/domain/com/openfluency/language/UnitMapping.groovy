package com.openfluency.language

/**
* This class maps a Unit in one language to a Unit in another language. It's specifies the meaning of a Unit in one language in another language.
*/
class UnitMapping {

	Unit unit1
	Unit unit2

    static constraints = {
    }

    String toString(){
    	"${unit1.getPrint()} -> ${unit2.getPrint()}"
    }
}
