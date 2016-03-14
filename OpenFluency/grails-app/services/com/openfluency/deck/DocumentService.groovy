package com.openfluency.deck
import com.openfluency.flashcard.Flashcard;
import com.openfluency.media.Image
import cscie99.team2.lingolearn.server.anki.AnkiFile

import com.openfluency.auth.User
import com.openfluency.language.Language
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import grails.transaction.Transactional
import com.openfluency.Constants
import com.openfluency.deck.Document

import grails.transaction.Transactional
import cscie99.team2.lingolearn.server.anki.AnkiConverter

@Transactional
class DocumentService {
    def springSecurityService
/*
    Add an imported document - anki file or csv file
*/
    def createDocument(String fullPath,String filename,Language language) {
		def owner= User.load(springSecurityService.principal.id)
    	def documentInstance = new Document(
    		owner: owner, //User.load(springSecurityService.principal.id),
    		fullPath: fullPath,
    		filename: filename,
    		language: language
    	)//.save(flush: true, failOnError: true)
    	//documentInstance.save(flush: true, failOnError: true)
    	//println "Associated Document ${documentInstance}"
    	return documentInstance
    }
	
	def createNewDeck(fullPath, alphabet) {
		AnkiFile anki = new AnkiFile(fullPath)
		def nCards = anki.totalCards
		def folder = anki.getTmpDir()
		def decks = anki.getDeckIterator()
		def cardfields = anki.getModels().values()
		def ArrayList<ArrayList> flashDeck = new ArrayList<ArrayList>();
		while (decks.hasNext()) {
			def deck = decks.next();
			def arrayList =  deck.getCardList() //deck.getCardSet()
			def typeList;
			for (card in arrayList) {
				// matches anki.fieldTypes (Linked HashMap)
				// matches anki.models.values.get(0).flds[n]
				// returns CardField array
				Flashcard fc = new Flashcard()
				fc.image = new Image()
				fc.image.url= card.getImage().getImageUri()
				//def fieldTypes = anki.fieldTypes;
				//def fieldNames = anki.fieldNames;
				ArrayList<Unit> units = createUnits(anki.fieldTypes,card, alphabet)
				//def um = [units: units.get(0),unit2:units.get(1)]
				flashDeck.add(units);
			}
		}
		return flashDeck;
	}
	
	/**
	 * Creates an arraylist of all the fields imported
	 * @param fieldList
	 * @param card
	 * @param alphabet
	 * @return
	 */
	def createUnits(fieldList,card,alphabet){
		ArrayList<Unit> units=new ArrayList<Unit>()
		for (field in card.fields) {
			def unit = new Unit(alphabet: alphabet, literal: field);
			units.add(unit);
		}
		return units;
	}

    /*
        maps the field index to a flashcard field
    def createDocumentCardFieldMapping(Map mapping) {
        User user = User.load(springSecurityService.principal.id)
        def oldCustomizations = Customization?.findAllByOwnerAndCard(userInstance, flashcardInstance)
        oldCustomizations.each { 
            println "Removing Customization ${it.id}"
            it.delete(flush: true) 
        }
    }
    */
}
