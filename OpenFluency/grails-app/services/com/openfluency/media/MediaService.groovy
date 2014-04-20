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
        //get all the intances needed for building a customization
        Image imageInstance = createImage(imageLink, unitMappingId)
        Audio audioInstance = null;
        User userInstance = User.load(springSecurityService.principal.id)
        Flashcard flashcardInstance = Flashcard.load(flashcardId)

        //as audio is not required, only try to load audio if an id is given 
        if (audioId != "") { audioInstance = Audio?.load(audioId); }

        //if neither audio nor image is set, then we don't create a customization
        if ((!audioInstance) && (!imageInstance)){
            println "null customization - not saved"
            return null
        }

        //make sure to delete any old customizations the user had for this card - they would wind up unlinked
        def oldCustomizations = Customization?.findAllByOwnerAndCard(userInstance, flashcardInstance)
        oldCustomizations.each { it.delete(flush: true) }

        //create the customization
        def customizationInstance = new Customization(
            owner: userInstance,
            card: flashcardInstance,
            audioAssoc: audioInstance,
            imageAssoc: imageInstance
        ).save(flush: true, failOnError: true)

        println "Created Customization ${customizationInstance}"
        return customizationInstance
    }
}
