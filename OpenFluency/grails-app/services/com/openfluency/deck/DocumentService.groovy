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
    		language: language,
			status: "Attempted"
    	).save(flush: true, failOnError: true)
    	return documentInstance
    }
	
	@Transactional
	def createPreviewDeck(String fullPath, String mediaDir, String name, String filename, String description, Language language, Document document){
		AnkiFile anki = new AnkiFile(fullPath,mediaDir)
		def nCards = anki.totalCards
		def folder = anki.getTmpDir()
		def ankiMediaDir = mediaDir + File.separator + anki.getMediaDir()
		def decks = anki.getDeckIterator()
		def cardfields = anki.getModels().values()
		// A place to hold all the cards
		def user= User.load(springSecurityService.principal.id)
		user.id = springSecurityService.principal.id
		def owner = springSecurityService.currentUser
        if (description==null || description.length()<1) description=filename // description must not be null in OF deck
		PreviewDeck previewDeckInstance = new PreviewDeck(owner: owner, filename: filename, name: name, description:description,language:language,document: document,mediaDir:ankiMediaDir);
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
}
