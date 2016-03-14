package com.openfluency.deck
import grails.plugin.springsecurity.annotation.Secured
import com.openfluency.Constants
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import com.openfluency.language.Language
import com.openfluency.deck.DocumentService
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
		def ankiDeck = null
		if(file.empty) {
			flash.message = "File cannot be empty"
		} else {
            User user = User.load(springSecurityService.principal.id)
			String filename = file.originalFilename
			String fullPath = grailsApplication.config.uploadFolder + "/" + filename
			try {
				file.transferTo(new File(fullPath))
			File ga = grailsApplication.getParentContext().getResource("card-media").file
			String applicationPath = request.getSession().getServletContext().getRealPath("")
			String mediaDir= ga.getAbsolutePath()
				flash.message = "Loading "+ fullPath + " for User " + user
				Language l = Language.load(lang)
				Document documentInstance = documentService.createDocument(fullPath, filename, l);
				documentInstance.owner = user
				// Doesn't work - owner always=null
				//documentInstance.save()
				flash.message = "Loading "+ file.originalFilename +" for User "+ user 
            } catch (Exception e) {
				flash.message = "Cannot save document"+e.message;	
				e.printStackTrace();
            }
			ankiDeck = documentService.createNewDeck(fullPath,lang);
			//def alphabetInstanceList=Alphabet.findAllByLanguage(lang);
			//def fields = cardfields.getFlds()
		}
		redirect (action:'list', params: ankiDeck)
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
