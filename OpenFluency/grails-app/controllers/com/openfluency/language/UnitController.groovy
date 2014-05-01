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
		Long languageId = params.languageId as Long
		String keyword = params.keyword
		Long offset = params.offset ? params.offset as Long : 0

		// Run the search
		def result = languageService.searchUnits(languageId, keyword, offset)

		[keyword: keyword, 
		offset: offset, 
		languageId: languageId, 
		deckId: params.deckId, 
		unitCount: result.unitCount, 
		unitInstanceList: result.unitInstanceList]
	}
}
