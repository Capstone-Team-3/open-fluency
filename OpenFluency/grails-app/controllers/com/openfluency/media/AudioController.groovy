package com.openfluency.media

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.openfluency.auth.*
import com.openfluency.language.*
import grails.converters.JSON
import java.util.UUID


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

	def saveFile() {
		// Create new media file and any parent directories
		def p = params
		def audioName= null
		def name
		def file = params['audiofile']
		try {
			def pronunciation = params['pronunciation']
			def blob = params['blob']
			name = file?.getOriginalFilename()
			def mediaTopDir= grailsApplication.config.mediaFolder
			def dirname = UUID.randomUUID().toString()
			File ga = grailsApplication.getParentContext().getResource(mediaTopDir).file
			String mediaDir= ga.getAbsolutePath()
			audioName= mediaDir + File.separator + "audio" + File.separator + dirname + File.separator + name
			def audioUrl= "/OpenFluency/"+ mediaTopDir + "/audio/" + dirname +"/" + name
			audioUrl = audioUrl.replaceAll("//","/")
			def newupload = new File(audioName)
			newupload.mkdirs()
			file.transferTo(newupload)
			flash.message = "Loading "+ name
			//Audio audioInstance = mediaService.createAudio(audioUrl,blob,pronunciation)
			// return audio object
			def audioLink =["url": audioUrl]
			render audioLink as JSON
		} catch (Exception e) {
			flash.message = "Failed: Loading "+ name
		}
		// Create the audio instance
	}

	/* 
	 * This saves the media file as a static url
	 */
	def saveMediaFile() {
		// Create new media file and any parent directories
		def p = params
		def audioName= null
		def name
		def file = params['audiofile']
		try {
			def pronunciation = params['pronunciation']
			def blob = params['blob']
			name = file?.getOriginalFilename()
			def mediaTopDir= grailsApplication.config.mediaFolder
			def dirname = UUID.randomUUID().toString()
			File ga = grailsApplication.getParentContext().getResource(mediaTopDir).file
			String mediaDir= ga.getAbsolutePath()
			audioName= mediaDir + File.separator + "audio" + File.separator + dirname + File.separator + name
			def audioUrl= "/OpenFluency/"+ mediaTopDir + "/audio/" + dirname +"/" + name
			audioUrl = audioUrl.replaceAll("//","/")
			def newupload = new File(audioName)
			newupload.mkdirs()
			file.transferTo(newupload)
			flash.message = "Loading "+ audioName
			Audio audioInstance = mediaService.createAudio(audioUrl,blob,pronunciation)

			JSON.use('deep')
			// return audio object
			render audioInstance as JSON
		} catch (Exception e) {
			flash.message = "Failed: Loading "+ name
		}
		// Create the audio instance
	}
	
    @Transactional
    def save() {
        // Create the audio instance
        Audio audioInstance = mediaService.createAudio(params.url, params.blob?.getBytes(), params['pronunciation.id'])

        JSON.use('deep')
        render audioInstance as JSON            
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

    def removeAudioInstance(long audioInstanceId) {
        Audio audioInstance
        if (audioInstanceId){
            audioInstance = Audio.load(audioInstanceId);
        }
        delete(audioInstance);
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
