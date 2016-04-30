package com.openfluency.flashcard 

import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet
import com.openfluency.media.Image
import com.openfluency.media.Audio
import com.openfluency.media.MediaService
import com.openfluency.auth.User
import com.openfluency.language.Unit
import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class FlashcardController { 

	def springSecurityService 
	def flashcardService
    def mediaService

	/**
	* Render the create flashcard page for a particular unit
	*/
    def create() {
        Unit unit = Unit.load(params.unit)
        def deckId = params.deckId
    	[flashcardInstance: new Flashcard(params), unitInstance: unit, userDecks: Deck.findAllByOwnerAndLanguage(User.load(springSecurityService.principal.id), unit.alphabet.language), deckId: deckId]
    }

    def createFromDictionary() {
        User loggedUser = User.load(springSecurityService?.principal?.id)
        List<Deck> deckInstanceList = Deck.findAllByOwner(loggedUser)
        def deckDefault = null;
        if(params.deckId){
            deckDefault = deckInstanceList.find{ it.id.toString() == params.deckId };
        }
        render(view: "createFromDictionary", model : [deckInstanceList : deckInstanceList, deckDefault : deckDefault])
    }

    def createTest(){
        def primaryString = params.concept.trim();
        def otherString = params.meaning.trim();
        def pronunciationString = params.pronunciation.trim(); 
        def deckId = params.deckId.trim();
        def imageLink = params.imageLink.trim();
        def audioLink = "";

        flashcardService.createFlashcardUsingDictionaryInfo(primaryString, otherString, pronunciationString, deckId.toInteger(), imageLink, audioLink );
        redirect(action: "createFromDictionary", params: [deckId : params.deckId]) 
    }

    /**
    * Save the flashcard for the selected unit
    */
    def save() {
        def flashcardInstance = flashcardService.createFlashcard(params.unit, params.unitMapping, params.pronunciation, params.imageLink, params.audio_id, params.deck)
        
    	// Check for errors
        if (flashcardInstance.hasErrors()) {
            log.info "Unit has errors!"
            redirect action: "create", params: params
            return
        }

        redirect controller:"deck", action: "show", id: params.deck
    }

    /**
    * View a single flashcard by id - a sample call to this function would be
    * /OpenFluency/flashcard/show/1 --> this will display the flashcard with id = 1
    */
    def show(Flashcard flashcardInstance) {
        [flashcardInstance: flashcardInstance]
    }

    def delete(Flashcard flashcardInstance) {
        if(springSecurityService.principal.id != flashcardInstance.deck.owner.id) {
            flash.message = "You're not allowed to delete this flashcard"
            redirect(uri: request.getHeader('referer'))
            return
        }


        flashcardService.deleteFlashcard(flashcardInstance)
        redirect(uri: request.getHeader('referer'))
    }
	
	//Param String: ?flashcard_id=125&flashcard_id=126&deckdest_id=18
	def reassign() {
		def flashcard_ids = params.flashcard_id
		def deck_dest_id  = params.deckdest_id
		
		def flashcard_ids_norm = [];
		flashcard_ids_norm.addAll(flashcard_ids)
		
		def dest = Deck.findById(deck_dest_id)
		
		
		flashcard_ids_norm.each {
			def flashcardInstance = Flashcard.findById(it)
			
			if(springSecurityService.principal.id != flashcardInstance.deck.owner.id) {
				flash.message = "You're not allowed to reassign this flashcard"
				redirect(uri: request.getHeader('referer'))
				return
			}
	
			flashcardService.reassignFlashcard(flashcardInstance, dest)
		}
		
		redirect(uri: request.getHeader('referer'))
	}
	
}
