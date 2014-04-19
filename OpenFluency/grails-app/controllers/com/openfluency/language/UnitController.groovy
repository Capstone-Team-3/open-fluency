package com.openfluency.language

class UnitController {

	def languageService

	def show(Unit unit) {
		render view: 'show', model: [unitInstance: unit]
	}

	def list(Alphabet alphabet) {
		render view: 'list', model: [alphabetInstance: alphabet, unitInstanceList: Unit.findAllByAlphabet(alphabet)]
	}

	def search() {
		Long alphabetId = params['filter-alph'] as Long
		Long deck_id = (params['deck-id']) ? (params['deck-id'] as Long) : null
		println "Deck id -> " + deck_id
		String keyword = params['search-text']
		[keyword: keyword, alphabetId: alphabetId, unitInstanceList: languageService.searchUnits(alphabetId, keyword), deck_id: deck_id]
	}
}
