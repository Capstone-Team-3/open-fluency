package com.openfluency.language

class UnitController {

    def show(Unit unit) {
    	render view: 'show', model: [unitInstance: unit]
    }

    def list(Alphabet alphabet) {
    	render view: 'list', model: [alphabetInstance: alphabet, unitInstanceList: Unit.findAllByAlphabet(alphabet)]
    }
}
