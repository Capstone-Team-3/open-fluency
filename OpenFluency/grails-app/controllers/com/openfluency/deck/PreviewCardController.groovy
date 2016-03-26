package com.openfluency.deck



import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PreviewCardController {
	
	def springSecurityService

    static allowedMethods = [ save: "POST", update: "PUT", delete: "DELETE"]

	/*
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PreviewCard.list(params), model:[previewCardInstanceCount: PreviewCard.count()]
    }
    */
	
	def index(PreviewDeck  deckid) {
		def max = 10
        params.max = Math.min(max ?: 10, 100)
		def previewCardInstanceList = PreviewCard.findAllByDeck(deckid, [max: max])
        respond previewCardInstanceList, model:[previewCardInstanceCount: previewCardInstanceList.count()]
	}

    def show(PreviewCard previewCardInstance) {
        respond previewCardInstance
    }

    def create() {
        respond new PreviewCard(params)
    }

    @Transactional
    def save(PreviewCard previewCardInstance) {
        if (previewCardInstance == null) {
            notFound()
            return
        }

        if (previewCardInstance.hasErrors()) {
            respond previewCardInstance.errors, view:'create'
            return
        }

        previewCardInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'previewCardInstance.label', default: 'PreviewCard'), previewCardInstance.id])
                redirect previewCardInstance
            }
            '*' { respond previewCardInstance, [status: CREATED] }
        }
    }

    def edit(PreviewCard previewCardInstance) {
        respond previewCardInstance
    }

    @Transactional
    def update(PreviewCard previewCardInstance) {
        if (previewCardInstance == null) {
            notFound()
            return
        }

        if (previewCardInstance.hasErrors()) {
            respond previewCardInstance.errors, view:'edit'
            return
        }

        previewCardInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'PreviewCard.label', default: 'PreviewCard'), previewCardInstance.id])
                redirect previewCardInstance
            }
            '*'{ respond previewCardInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(PreviewCard previewCardInstance) {

        if (previewCardInstance == null) {
            notFound()
            return
        }

        previewCardInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'PreviewCard.label', default: 'PreviewCard'), previewCardInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'previewCardInstance.label', default: 'PreviewCard'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
