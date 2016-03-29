package com.openfluency.media



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.openfluency.media.MediaService
import com.openfluency.auth.User
import com.openfluency.flashcard.Flashcard

@Transactional(readOnly = true)
class CustomizationController {

    def MediaService
    def SpringSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", remove: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Customization.list(params), model:[customizationInstanceCount: Customization.count()]
    }

    def show(Customization customizationInstance) {
        respond customizationInstance
    }

    def create() {
        respond new Customization(params)
    }

    def remove() { 
        User userInstance = User.load(springSecurityService.principal.id)
        Customization customizationInstance

        if (params?.flashcardId) {
            Flashcard flashcardInstance = Flashcard.load(params.flashcardId)
            customizationInstance = Customization?.findByOwnerAndCard(userInstance, flashcardInstance)
        }
        delete(customizationInstance)
    }

    @Transactional
    def save() {
        
        def customizationInstance = mediaService.createCustomization(params.flashcardId, params.unitMappingId, params.imageLink, params.audioId)
        

        if (customizationInstance == null) { 
            notFound()
            return
        }
		
		customizationInstance.save(flush:true)

        if (customizationInstance.hasErrors()) {
            respond customizationInstance.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'customizationInstance.label', default: 'Customization'), customizationInstance.id])
                redirect customizationInstance
            }
            '*' { respond customizationInstance, [status: CREATED] }
        }
    }

    def edit(Customization customizationInstance) {
        respond customizationInstance
    }

    @Transactional
    def update(Customization customizationInstance) {
        if (customizationInstance == null) {
            notFound()
            return
        }

        if (customizationInstance.hasErrors()) {
            respond customizationInstance.errors, view:'edit'
            return
        }

        customizationInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Customization.label', default: 'Customization'), customizationInstance.id])
                redirect customizationInstance
            }
            '*'{ respond customizationInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Customization customizationInstance) {

        if (customizationInstance == null) {
            notFound()
            return
        }

        customizationInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Customization.label', default: 'Customization'), customizationInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'customizationInstance.label', default: 'Customization'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
