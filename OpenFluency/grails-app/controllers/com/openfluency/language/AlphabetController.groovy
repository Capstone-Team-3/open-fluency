package com.openfluency.language

class AlphabetController {

    def list() { 
    	[alphabetInstanceList: Alphabet.list()]
    }
}
