package com.openfluency.deck

import com.openfluency.flashcard.Deck;
import java.util.regex.Pattern;
import com.openfluency.flashcard.Flashcard;
import com.openfluency.media.Audio
import com.openfluency.media.Image
import cscie599.openfluency2.CharSetIdentifier
import cscie599.openfluency2.CharSetIdentifier.Charset
import cscie99.team2.lingolearn.server.anki.AnkiFile

import com.openfluency.language.Language
import com.openfluency.language.Alphabet
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.Constants
import com.openfluency.deck.Document
import com.openfluency.deck.PreviewCard
import com.openfluency.deck.PreviewDeck
import cscie99.team2.lingolearn.server.anki.AnkiConverter
import cscie99.team2.lingolearn.shared.Card

import grails.transaction.Transactional
import com.openfluency.auth.User

@Transactional
class DocumentService {
    def springSecurityService
    def algorithmService
	def flashcardService
	def flashcardInfoService
	def languageService
/*
    Add an imported document - anki file or csv file
*/
    def createDocument(String fullPath,String filename,Language language,String description)  {
		//def owner= User.load(springSecurityService.principal.id)
		def owner = springSecurityService.currentUser
    	def documentInstance = new Document(
			owner: owner,
    		fullPath: fullPath,
    		filename: filename,
    		description: description,
    		language: language
    	).save(flush: true, failOnError: true)
    	return documentInstance
    }
	
	def createNewDeck(fullPath,description,language,mediaDir) {
		AnkiFile anki = new AnkiFile(fullPath,mediaDir)
		def nCards = anki.totalCards
		def folder = anki.getTmpDir()
		def decks = anki.getDeckIterator()
		def alphabets = Alphabet.findAllByLanguage(language);
		def cardfields = anki.getModels().values()
		// A place to hold all the cards
		def ArrayList<ArrayList> flashDeck = new ArrayList<ArrayList>();
		
		while (decks.hasNext()) {
			def deck = decks.next();
			ArrayList cardList =  deck.getCardList() //deck.getCardSet()
			ArrayList alphaList = new ArrayList<Alphabet>();
			def typeList;
			def ncards = cardList.size();
			ArrayList<CharSetIdentifier>ci = new ArrayList<CharSetIdentifier>(ncards);
			for (int i=0;i<ncards;i++){
				def field = anki.fieldNames.get(1)
				Charset b = CharSetIdentifier.testField(field);
				for (Card card : cardList) {
					int j=0;
					for (cardfield in card.fields) {
						ci.get(i).addText(cardfield.get(j))
						j++;
					}
				}
			} 
			//Charset.Kanji Charset.Hiragana Charset.English Charset.Hiragana
			for (Card card in cardList) {
				/*
				Flashcard fc = new Flashcard()
				fc.image = new Image()
				fc.image.url= card.getImage().getImageUri()
				fc.audio = new Audio()
				fc.image.url= card.getSound().getSoundUri()
				*/
				def fieldTypes = anki.fieldTypes;
				def fieldNames = anki.fieldNames;
				ArrayList<Unit> units = createUnits(anki.fieldTypes,card, alphaList)
				//PreviewCard precard = new PreviewCard(PreviewDeck deckId, units: units, fields: fieldNames,types: fieldTypes );
				//def um = [units: units.get(0),unit2:units.get(1)]
				flashDeck.add(units);
			}
		}
		return flashDeck;
	}
	
	// create open fluency deck
	def createDeck(String title, String description, String languageId){
		def cardServerName = algorithmService.getDefault();
		deckService.createDeck(title, description,languageId, cardServerName);
		return deck
	}
	
	@Transactional
	def createPreviewDeck(String fullPath, String mediaDir, String filename, String description, Language language){
		AnkiFile anki = new AnkiFile(fullPath,mediaDir)
		def nCards = anki.totalCards
		def folder = anki.getTmpDir()
		def decks = anki.getDeckIterator()
		def cardfields = anki.getModels().values()
		// A place to hold all the cards
		def user= User.load(springSecurityService.principal.id)
		user.id = springSecurityService.principal.id
		def owner = springSecurityService.currentUser
		PreviewDeck previewDeckInstance = new PreviewDeck(owner: owner, filename: filename, name: filename, description:description,language:language);
		previewDeckInstance.save(flush:true)
		def nfields = anki.fieldNames.size()
		while (decks.hasNext()) {
			def deck = decks.next();
			ArrayList cardList =  deck.getCardList()
			// Save each card
			def ncards = cardList.size();
			for (int i=0;i<ncards;i++) {
				Card card = cardList.get(i)
				def fieldTypes = anki.fieldTypes;
				def fieldNames = anki.fieldNames;
				PreviewCard pc=new PreviewCard(deck: previewDeckInstance)
				for (String cardfield in card.fields) {
					pc.addToUnits(cardfield.take(255)) }
				for (String fieldtype in fieldTypes) {
					 pc.addToTypes(fieldtype) }
				for (String fieldname in fieldNames) {
					 pc.addToFields(fieldname) }
				pc.save flush:true
			}
		}
		return previewDeckInstance;
	}

	/**
	 * Creates an arraylist of all the fields imported
	 * @param fieldList
	 * @param card - contains all the fields in the card
	 * @param alphabet
	 * @return
	 */
	def createPreviewUnits(fieldList,card){
		ArrayList<Unit> units=new ArrayList<Unit>()
		int i=0;
		for (field in card.fields) {
			def unit = new Unit(alphabet: alphabetList.get(i), literal: field);
			units.add(unit);
			i++;
		}
		return units;
	}
}
