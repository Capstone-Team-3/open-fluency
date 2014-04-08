package com.openfluency.media

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


@Transactional(readOnly = true)
class AudioController { 

    def mediaService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Audio.list(params), model:[audioInstanceCount: Audio.count()]
    }

    def show(Audio audioInstance) {
        respond audioInstance
    }

    def create() {
        respond new Audio(params)
    }

    @Transactional
    def save() {

        Byte[] blob = (Byte[]) params.audioWAV
        Audio audioInstance = mediaService.createAudio(params.url, blob, params['pronunciation.id'])

        if (audioInstance == null) {
            notFound()
            return
        }

        if (audioInstance.hasErrors()) {
            respond audioInstance.errors, view:'create'
            return
        }

        audioInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'audioInstance.label', default: 'Audio'), audioInstance.id])
                redirect audioInstance
            }
            '*' { respond audioInstance, [status: CREATED] }
        }
    }

    def edit(Audio audioInstance) {
        respond audioInstance
    }

    @Transactional
    def update(Audio audioInstance) {
        if (audioInstance == null) {
            notFound()
            return
        }

        if (audioInstance.hasErrors()) {
            respond audioInstance.errors, view:'edit'
            return
        }

        audioInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Audio.label', default: 'Audio'), audioInstance.id])
                redirect audioInstance
            }
            '*'{ respond audioInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Audio audioInstance) {

        if (audioInstance == null) {
            notFound()
            return
        }

        audioInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Audio.label', default: 'Audio'), audioInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'audioInstance.label', default: 'Audio'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
