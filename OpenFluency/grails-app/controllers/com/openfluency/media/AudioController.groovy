package com.openfluency.media

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.openfluency.auth.*
import com.openfluency.language.*


@Transactional(readOnly = true)
class AudioController { 

    def mediaService
    def springSecurityService

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

    /**
    *   This function is designed to serve audio clips to the Audio and Flashcard views.
    *   I am doing something wrong - either here or in the audio>show view.  Probably sourcing
    *   this incorrectly their.
    */
    def sourceAudio(long id){
        Audio audioInstance = Audio.get(id)
        if (audioInstance == null){
            flash.message = "Audio not found"
        } else {
            response.setContentType('audio/wav')
            response.setContentLength(audioInstance.audioWAV.length)
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${audioInstance.pronunciation.toString()}\"")
            
            def outputStream = response.getOutputStream()
            outputStream << audioInstance.audioWAV
            outputStream.flush()
            outputStream.close()
        }
    }

    @Transactional
    def save() {

        Audio audioInstance = mediaService.createAudio(params.url, params.audioWAV.getBytes(), params['pronunciation.id'])
        println "ID: ${audioInstance.id}, Content: ${audioInstance.audioWAV}"

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
