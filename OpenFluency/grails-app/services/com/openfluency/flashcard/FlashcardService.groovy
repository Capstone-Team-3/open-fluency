package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.flashcard.Share
import com.openfluency.language.Language
import com.openfluency.language.Alphabet
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.media.Image
import com.openfluency.media.Audio
import grails.transaction.Transactional

@Transactional
class FlashcardService {

	def springSecurityService 
    def mediaService

	/**
	* Creates a new flashcard - the alternative is to pass a Map as an argument 
    * does not currently support audio file storage
	*/
    def createFlashcard(String unitId, String unitMappingId, String pronunciationId, String imageLink, String audioId, String deckId) {

        Unit unitInstance = Unit.load(unitId)

        Image imageInstance = mediaService.createImage(imageLink, unitMappingId)
        
        def flashcardInstance = new Flashcard(
            primaryAlphabet: unitInstance?.alphabet, 
            unitMapping: UnitMapping.load(unitMappingId), 
            pronunciation: Pronunciation.load(pronunciationId), 
            image: imageInstance, 
            audio: Audio?.load(audioId), 
            deck: Deck.load(deckId)).save(flush: true, failOnError: true)

        println "Created flashcard $flashcardInstance"
        return flashcardInstance
    }

    /**
    * For development purposes
    */
    def createRandomFlashcards(Deck deck, Alphabet alphabet) {
        // Build a few flashcards
        // The idea here is: find all the unitMappings where unit1 is alphabet the given alphabet
        int f=0;
        UnitMapping.createCriteria().list(max: 5) {
            unit1 { eq('alphabet', alphabet) }
        }.each { unitMapping ->
            Pronunciation pronunciation = Pronunciation.findByUnit(unitMapping.unit1)
            if(pronunciation) {
                new Flashcard(primaryAlphabet: unitMapping.unit1.alphabet, unitMapping: unitMapping, pronunciation: pronunciation, deck: deck).save(failOnError: true)
                f++
            }
        }

        log.info "Created ${f} flashcards for deck: ${deck.title}"
    }
}
