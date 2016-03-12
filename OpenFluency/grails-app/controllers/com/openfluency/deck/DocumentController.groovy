package com.openfluency.deck
import grails.plugin.springsecurity.annotation.Secured
import com.openfluency.Constants
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import com.openfluency.language.Language
import cscie99.team2.lingolearn.server.anki.AnkiFile

@Secured(['isAuthenticated()'])
class DocumentController {
    def springSecurityService
    def deckService
    def documentService
	
    def create() {
        Long languageId = params['filter-lang'] as Long
        String keyword = null // params['search-text']
        [keyword: keyword, languageId: languageId, deckInstanceList: deckService.searchDecks(languageId, keyword), 
        languageInstanceList: Language.list(), userInstance: User.load(springSecurityService.principal.id)]
    }

	// Researchers will not be able to upload decks
	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
	def upload()
	{
		def file = request.getFile('file')
		def lang = params['filter-lang']
		def ofdeck = params['filter-deck']
		if(file.empty) {
			flash.message = "File cannot be empty"
		} else {
            User user = User.load(springSecurityService.principal.id)
			String filename = file.originalFilename
			String fullPath = grailsApplication.config.uploadFolder + "/" + filename
            try {
				/*
				Deck deckInstance = deckController.create(filename, filename, null, null, cardServerName)
				deckService.createDeck(filename, filename, params['language.id'], params['sourceLanguage.    id'], params.cardServerAlgo)
				cardServerAlgos: algorithmService.cardServerNames()
				*/
				flash.message = "Loading "+ fullPath + " for User " + user
				def documentInstance = documentService.createDocument(fullPath, filename);
				documentInstance.owner = user
				//documentInstance.save()
				flash.message = "Loading "+ file.originalFilename +" for User "+ user + " -> "+ grailsApplication.config
            } catch (Exception e) {
				flash.message = "Cannot save document"+e.message;	
				e.printStackTrace();
            }
			file.transferTo(new File(fullPath))
            AnkiFile anki = new AnkiFile(fullPath)
            def nCards = anki.totalCards
            def folder = anki.getTmpFolder()
            def decks = anki.getDeckIterator()
            def cardfields = anki.getModels().values()
			flash.message = "Imported "+ nCards +" cards";	
			while (decks.hasNext()) {
				def deck = decks.next();
                def arrayList =  deck.getCardList() //deck.getCardSet()
                for (card in arrayList) {
                    // matches anki.fieldTypes (Linked HashMap)
                    // matches anki.models.values.get(0).flds[n]
                    // returns CardField array
                    for (field in card.fields) {
						// display 5 flashcards
                    }
                }
			}
			//def fields = cardfields.getFlds()
		}
		redirect (action:'list')
	}
	def index() {
		redirect(action: "list", params: params)
	}
	def list() {
        User user = User.load(springSecurityService?.principal?.id)
		params.max = 10
		//params.owner = user
        List<Document> deckInstanceList = Document.findAllByOwner(user)
		//[documentInstanceList: Document.list(params), documentInstanceTotal: Document.countByUser(user)]
		[documentInstanceList: Document.list(params), documentInstanceTotal: Document.count()]
	}

	def download(long id) {
		Document documentInstance = Document.get(id)
		if ( documentInstance == null) {
			flash.message = "Document not found."
			redirect (action:'list')
		} else {
			response.setContentType("APPLICATION/OCTET-STREAM")
			response.setHeader("Content-Disposition", "Attachment;Filename=\"${documentInstance.filename}\"")

			def file = new File(documentInstance.fullPath)
			def fileInputStream = new FileInputStream(file)
			def outputStream = response.getOutputStream()

			byte[] buffer = new byte[4096];
			int len;
			while ((len = fileInputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}

			outputStream.flush()
			outputStream.close()
			fileInputStream.close()
		}
	}
}
