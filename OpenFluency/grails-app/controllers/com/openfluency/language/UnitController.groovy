package com.openfluency.language

class UnitController {

    def show(Unit unit) {
    	render view: 'show', model: [unitInstance: unit]
    }

    def list(Alphabet alphabet) {
    	render view: 'list', model: [alphabetInstance: alphabet, unitInstanceList: Unit.findAllByAlphabet(alphabet)]
    }

    def index(Alphabet alphabet) {
        render view: 'index', model: [alphabetInstanceList: Alphabet.list(), alphabetInstance: alphabet, unitInstanceList: Unit.findAllByAlphabet(alphabet)]
    }
}
