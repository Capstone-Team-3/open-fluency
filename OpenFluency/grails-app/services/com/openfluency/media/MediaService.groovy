package com.openfluency.media

import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*
import com.openfluency.auth.User
import com.openfluency.language.*
import com.openfluency.flashcard.*


@Transactional
class MediaService {

	def springSecurityService

    def createImage(String imageLink, String unitmappingId) {
    	
    	if (!imageLink){ return null }

    	def imageInstance = new Image(
    		owner: User.load(springSecurityService.principal.id),
    		url: imageLink,
    		unitMapping: UnitMapping.load(unitmappingId),
    		dateCreated: new Date(),
    		lastUpdated: new Date()
    	).save(flush: true, failOnError: true)
    	
    	println "Associated Image ${imageInstance}"
    	return imageInstance
    }

    def createAudio(String audioLink, byte[] audioFile, String pronunciationId){ 

        if ((!audioLink) && (!audioFile)){ 
            return null 
        }

    	def audioInstance = new Audio(
    		owner: User.load(springSecurityService.principal.id),
    		url: audioLink,
            audioWAV: audioFile,
            pronunciation: Pronunciation.load(pronunciationId),
    		dateCreated: new Date(),
    		lastUpdated: new Date()
    	).save(flush: true, failOnError: true)

    	println "Associated Audio ${audioInstance}"
    	return audioInstance
    }

    def createCustomization(String flashcardId, String unitMappingId, String imageLink, String audioId){
        
        Image imageInstance = createImage(imageLink, unitMappingId)
        Audio audioInstance = null;
        if (audioId != "") { audioInstance = Audio.load(audioId); }

        if ((!audioInstance) && (!imageInstance)){
            println "null customization - not saved"
            return null
        }

        def customizationInstance = new Customization(
            owner: User.load(springSecurityService.principal.id),
            card: Flashcard.load(flashcardId),
            audioAssoc: audioInstance,
            imageAssoc: imageInstance
        ).save(flush: true, failOnError: true)

        println "Created Customization ${customizationInstance}"
        return customizationInstance
    }
}
