package com.openfluency.deck



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonSlurper
import com.openfluency.auth.User
import com.openfluency.language.Language;

@Secured(['isAuthenticated()'])
@Transactional(readOnly = true)
class PreviewDeckController {
	def springSecurityService
	def flashcardService
	def algorithmService
	def flashcardInfoService
	def previewDeckService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", importDeck:"POST"]

	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
    def index(Integer max) {
		//def user = springSecurityService.currentUser
		def user = User.load(springSecurityService.principal.id)
        params.max = Math.min(max ?: 10, 100)
       // respond PreviewDeck.list(params), model:[previewDeckInstanceCount: PreviewDeck.count()]
        def previewdecks = PreviewDeck.findAll("from PreviewDeck where owner_id= ? order by uploadDate desc",[user.id], params)
		respond previewdecks , model:[previewDeckInstanceCount: PreviewDeck.countByOwner(user)]
    }

	// Display a preview deck associated with a document
	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
    def display(Document document) {
		def user = User.load(springSecurityService.principal.id)
        PreviewDeck previewDeckInstance =  PreviewDeck.findByDocumentAndOwner(document,user)
		if (previewDeckInstance != null) {
			redirect action: "show", id: previewDeckInstance.id
			return
		}
		else {
			flash.message="Deck already imported or deleted"
			redirect controller: "Document", action: "list"
			return
		}
    }

	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
    def show(PreviewDeck previewDeckInstance) {
		if (previewDeckInstance == null) {
			notFound()
			return
		}
		def user = springSecurityService.currentUser // User.load(springSecurityService.principal.id)
        if (previewDeckInstance.ownerId != user.id) {
            flash.message = "You're not allowed to view this flashdeck "+previewDeckInstance.ownerId
            redirect(uri: request.getHeader('referer'))
            return
        } else {
			def max = 20
			params.max = Math.min(max ?: 10, 100)
			def previewCards= PreviewCard.findAllByDeck(previewDeckInstance, params)
        [ previewDeckInstance: previewDeckInstance, previewCardInstanceList: previewCards ]
        }
    }
	

	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT'])
	def map(PreviewDeck previewDeckInstance) {
		def mediaTmpDir= grailsApplication.config.tmpMediaFolder
		def mediaDir= grailsApplication.config.mediaFolder
		new File(mediaDir).mkdirs()
		def user = User.load(springSecurityService.principal.id)
		def mediaTmpDir= grailsApplication.config.tmpMediaFolder
		def mediaDir= grailsApplication.config.mediaFolder
		new File(mediaDir).mkdirs()
		if (previewDeckInstance.ownerId != user.id) {
			flash.message = "You're "+ user + " not allowed to view this flashdeck " + previewDeckInstance
			redirect(uri: request.getHeader('referer'))
			return
		} else {
			def max = 10
			params.max = Math.min(max ?: 10, 100)
			def previewCards= PreviewCard.findAllByDeck(previewDeckInstance, params) as JSON
		[ previewDeckInstance: previewDeckInstance, previewCardInstanceList: previewCards ]
		}
	}
	
	def unitMappingSubmit(PreviewDeck previewDeckInstance) {
		def jsonSlurper = new JsonSlurper();  
		// hack.. sending json string as text plain.. Json always null for some reason even with empty args.
		def payload = jsonSlurper.parseText(params.payload); 
		
		def fieldInd = payload.fieldIndices;
		def alphInd = payload.alphaIndices;
		def algorithm = payload.algorithm;
		def deckName = payload.name;
		def deckDescription = payload.description;
		
		int algoIndex = 0;  //sw2 is default
		if (algorithm.equals("lws"))
			algoIndex = 1;
				
		HashMap<String, Integer> fieldIndices = (HashMap<String, Integer>) fieldInd;
		HashMap<Integer, String> alphaIndices = (HashMap<Integer, String>) alphInd;
		
		if (!fieldIndices.containsKey("Literal") || !fieldIndices.containsKey("Pronunciation") || !fieldIndices.containsKey("Meaning")) {
			render (status: 400, text: 'Either "Literal", "Pronunciation", or "Meaning" missing')
			return;
		}
		else {
			def srcLang = previewDeckInstance.sourceLanguage;
			if (previewDeckInstance.sourceLanguage == null)
				srcLang = Language.findByName("English");
			
            previewDeckService.setDirs(grailsApplication.config.tmpMediaFolder,grailsApplication.config.mediaFolder)
			// should do exception checking here..
			
			try {
				previewDeckService.createOpenFluencyDeck(deckName, deckDescription, srcLang, previewDeckInstance, fieldIndices, alphaIndices, algorithmService.cardServerNames()[algoIndex]);
			} catch(Exception e) {
				render (status: 500, text: 'An error has occurred while creating the deck');
			}
			
            try { // Modify the download doc record
                def doc = Document.findById(previewDeckInstance.documentId)
                doc.status="Imported"
                doc.save()
            } catch(Exception e) {}
            
            //previewDeckInstance.delete flush:true
			render (status: 200, text: "success")
			//redirect controller:"Deck", action: "list"
		}		
	}

    @Transactional
    def save(PreviewDeck previewDeckInstance) {
		
        if (previewDeckInstance == null) {
            notFound()
            return
        }

        if (previewDeckInstance.hasErrors()) {
            respond previewDeckInstance.errors, view:'create'
            return
        }
		User user = User.load(springSecurityService.principal.id)
		previewDeckInstance.owner = user
        previewDeckInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'previewDeckInstance.label', default: 'PreviewDeck'), previewDeckInstance.id])
                redirect previewDeckInstance
            }
            '*' { respond previewDeckInstance, [status: CREATED] }
        }
    }

    def edit(PreviewDeck previewDeckInstance) {
		User user = User.load(springSecurityService.principal.id)
		if (previewDeckInstance.owner == user) {
			respond previewDeckInstance
		}
    }

	@Transactional
	def importDeck(PreviewDeck previewDeckInstance) {
		User user = User.load(springSecurityService.principal.id)
		def mediaTmpDir= grailsApplication.config.tmpMediaFolder
		def mediaDir= grailsApplication.config.mediaFolder
		new File(mediaDir).mkdirs()
		if (previewDeckInstance.ownerId == user.id) {
			previewDeckService.importDeck(previewDeckInstance,  mediaTmpDir, mediaDir)	
			def doc = Document.findById(previewDeckInstance.documentId)
			doc.status="Imported"
			doc.save()
			//previewDeckInstance.delete flush:true
			redirect controller:"Deck", action: "list"
			return
		}
		redirect action: "notowner"
	}

    @Transactional
    def update(PreviewDeck previewDeckInstance) {
		User user = User.load(springSecurityService.principal.id)
		if (previewDeckInstance.owner != user) {
			redirect action: "notowner"
			return
		}
        if (previewDeckInstance == null) {
            notFound()
            return
        }

        if (previewDeckInstance.hasErrors()) {
            respond previewDeckInstance.errors, view:'edit'
            return
        }

        previewDeckInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'PreviewDeck.label', default: 'PreviewDeck'), previewDeckInstance.id])
                redirect previewDeckInstance
            }
            '*'{ respond previewDeckInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(PreviewDeck previewDeckInstance) {

        if (previewDeckInstance == null) {
            notFound()
            return
        }

		def doc = Document.findById(previewDeckInstance.document.id)
		doc.status="Deleted"
		doc.save()
        previewDeckInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'PreviewDeck.label', default: 'PreviewDeck'), previewDeckInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notOwner() {
       flash.message = "You do not have permission to access this item"
    }
	
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'previewDeckInstance.label', default: 'PreviewDeck'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
