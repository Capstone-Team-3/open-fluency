package com.openfluency.flashcard



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FlashcardInfoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond FlashcardInfo.list(params), model:[flashcardInfoInstanceCount: FlashcardInfo.count()]
    }

    def show(FlashcardInfo flashcardInfoInstance) {
        respond flashcardInfoInstance
    }

    def create() {
        respond new FlashcardInfo(params)
    }

    @Transactional
    def save(FlashcardInfo flashcardInfoInstance) {
        if (flashcardInfoInstance == null) {
            notFound()
            return
        }

        if (flashcardInfoInstance.hasErrors()) {
            respond flashcardInfoInstance.errors, view:'create'
            return
        }

        flashcardInfoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'flashcardInfoInstance.label', default: 'FlashcardInfo'), flashcardInfoInstance.id])
                redirect flashcardInfoInstance
            }
            '*' { respond flashcardInfoInstance, [status: CREATED] }
        }
    }

    def edit(FlashcardInfo flashcardInfoInstance) {
        respond flashcardInfoInstance
    }

    @Transactional
    def update(FlashcardInfo flashcardInfoInstance) {
        if (flashcardInfoInstance == null) {
            notFound()
            return
        }

        if (flashcardInfoInstance.hasErrors()) {
            respond flashcardInfoInstance.errors, view:'edit'
            return
        }

        flashcardInfoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'FlashcardInfo.label', default: 'FlashcardInfo'), flashcardInfoInstance.id])
                redirect flashcardInfoInstance
            }
            '*'{ respond flashcardInfoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(FlashcardInfo flashcardInfoInstance) {

        if (flashcardInfoInstance == null) {
            notFound()
            return
        }

        flashcardInfoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'FlashcardInfo.label', default: 'FlashcardInfo'), flashcardInfoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'flashcardInfoInstance.label', default: 'FlashcardInfo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
