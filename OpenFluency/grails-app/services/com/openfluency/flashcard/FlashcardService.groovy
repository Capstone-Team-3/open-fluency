package com.openfluency.flashcard

import com.openfluency.course.Question
import com.openfluency.course.QuestionOption
import com.openfluency.auth.User
import com.openfluency.flashcard.Share
import com.openfluency.media.Customization
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
    def deckService
    def flashcardInfoService

	/**
	* Creates a new flashcard - the alternative is to pass a Map as an argument 
    * does not currently support audio file storage
	*/
    Flashcard createFlashcard(String unitId, String unitMappingId, String pronunciationId, String imageLink, String audioId, String deckId) {

        Unit unitInstance = Unit.load(unitId)
        println "....${unitInstance.literal}...."
        Image imageInstance = mediaService.createImage(imageLink, unitMappingId)
        Audio audioInstance = null;
        if (audioId != "") { audioInstance = Audio.load(audioId); }

        //build info to add flashcard to queueing system
        Deck deckInstance = Deck.load(deckId)
        
        def flashcardInstance = new Flashcard(
            primaryAlphabet: unitInstance?.alphabet, 
            unitMapping: UnitMapping.load(unitMappingId), 
            pronunciation: Pronunciation.load(pronunciationId), 
            image: imageInstance, 
            audio: audioInstance, 
            deck: deckInstance).save(flush: true, failOnError: true)

        //build the flashcardInfo objs for all users following this deck
        flashcardInfoService.addNewFlashcardInfoAllDeckUsers(deckInstance, flashcardInstance)

        println "Created flashcard $flashcardInstance"
        return flashcardInstance
    }

    /**
    * Get the last ranking that the user gave this flashcard
    */
    CardRanking getLastRanking(Long flashcardId) {
        return CardRanking.findByFlashcardAndUser(Flashcard.load(flashcardId), User.load(springSecurityService.principal.id))
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
        //ensure the queues are built for the deck owner
        flashcardInfoService.resetDeckFlashcardInfo(deck.owner, deck)
        
        log.info "Created ${f} flashcards for deck: ${deck.title}"
    }

    void deleteFlashcard(Flashcard flashcardInstance) {
        
        // First delete customizations
        Customization.findAllByCard(flashcardInstance).each {
            it.audioAssoc.delete()
            it.imageAssoc.delete()
            it.delete()
        }

        // Delete all the stats
        FlashcardInfo.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        // Delete all the questions and options that use this card
        QuestionOption.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        Question.findAllByFlashcard(flashcardInstance).each {
            QuestionOption.findAllByQuestion(it).each {
                it.delete()
            }

            it.delete()
        }

        // Delete all card usages and rankings for this card
        CardUsage.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        CardRanking.findAllByFlashcard(flashcardInstance).each {
            it.delete()
        }

        // Now delete it
        flashcardInstance.delete()
    }
}
