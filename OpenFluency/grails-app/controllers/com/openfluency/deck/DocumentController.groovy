package com.openfluency.deck
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional;

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
	@Transactional
	def upload()
	{
		def file = request.getFile('file')
		def lang = params['filter-lang']
		def ofdeck = params['filter-deck']
		def description = params['Description']
		def ankiDeck = null
		def previewDeck = null
		if(file.empty) {
			flash.message = "File cannot be empty"
		} else {
            User user = User.load(springSecurityService.principal.id)
			String filename = file.originalFilename
			String fullPath = grailsApplication.config.uploadFolder + "/" + filename
			Language l = Language.get(lang)
			Document documentInstance;
			try {
				documentInstance = documentService.createDocument(fullPath, filename, l, description);
			} catch (Exception ex) {
				flash.message = "Must login first"
				ex.printStackTrace()
				throw ex
			}
			try {
				new File(fullPath).mkdirs()
				file.transferTo(new File(fullPath))
				File ga = grailsApplication.getParentContext().getResource("card-media").file
				//String applicationPath = request.getSession().getServletContext().getRealPath("")
				String mediaDir= ga.getAbsolutePath()
				flash.message = "Loading "+ fullPath + " for User " + user
				ankiDeck = documentService.createPreviewDeck(fullPath,mediaDir,filename,description,l,documentInstance);
				documentInstance.status="Uploaded";
				documentInstance.save(flush:true)
				flash.message = "Loading "+ filename +" for User "+ user
				redirect(controller:'previewDeck',action:'show',params:[deck_id:ankiDeck.id])
			} catch (Exception e) {
				flash.message = "Cannot save document"+e.message;
				e.printStackTrace();
				redirect (action:'list', params: params)
			}
			//def alphabetInstanceList=Alphabet.findAllByLanguage(lang);
			//def fields = cardfields.getFlds()
		}
	}

	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
	def index() {
		redirect(action: "list", params: params)
	}

	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
	def list() {
        User user = User.load(springSecurityService?.principal?.id)
        //params.max = Math.min(max ?: 12, 100)
        //List<Document> docInstanceList = Document.findAllByOwner(user, params)
        List<Document> documentInstanceList = 
        Document.findAll("from Document where owner_id=? order by uploadDate",[ user.id ],[max: 10])
        //List<Document> deckInstanceList = Document.getAll()
		//[documentInstanceList:documentInstanceList, documentInstanceTotal: Document.countByOwner(user)]
        respond documentInstanceList , model:[documentInstanceTotal: Document.countByOwner(user)]
		//[documentInstanceList: Document.list(params), documentInstanceTotal: Document.count()]
	}

	// Do not use
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
	
	@Transactional
	def save(Document documentInstance) {
		User user = User.load(springSecurityService.principal.id)
		documentInstance.owner = user
		if (documentInstance == null) {
			notFound()
			return
		}

		if (documentInstance.hasErrors()) {
			respond documentInstance.errors, view:'create'
			return
		}

		documentInstance.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.created.message', args: [message(code: 'documentInstance.label', default: 'Document'), documentInstance.id])
				redirect documentInstance
			}
			'*' { respond documentInstance, [status: CREATED] }
		}
	}
}
