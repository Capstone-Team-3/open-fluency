package com.openfluency.dictionary

import com.openfluency.dictionary.SearchService;
import com.openfluency.dictionary.DictionaryEntry;

import java.util.Vector;

class DictionaryController {

	def searchService
	
    def index() { }
	
	def search = {
		def term = params.term as String;	// string to search
		def n = params.count as Integer;  	// number of results to get from dictionary

		if (term == null || n == null) return;

		// search in dictionary
		Vector<DictionaryEntry> result = searchService.getDictionaryEntries(term, n);
		
		if (result.elementCount == 0) return;
		
		[dictionarySearchResults:result]
	}
}
