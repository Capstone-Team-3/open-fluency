package com.openfluency.media

import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*
import com.openfluency.auth.User
import com.openfluency.language.*


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

    def createAudio(String audioLink, Byte[] audioFile, String pronunciationId){ 

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
}
