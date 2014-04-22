package com.openfluency.course

import com.openfluency.flashcard.*
import com.openfluency.auth.User
import grails.plugin.springsecurity.annotation.Secured
import com.openfluency.media.Customization

class ChapterController {

	def springSecurityService
	def courseService
    def deckService
    def flashcardService

    @Secured(['isAuthenticated()'])
	def create(Course courseInstance) {
		render view: "create", model: [courseInstance: courseInstance, userDecks: Deck.findAllByOwner(User.load(springSecurityService.principal.id))]
		
	}

    @Secured(['isAuthenticated()'])
	def save() {
		def chapterInstance = courseService.createChapter(params.title, params.description, params.deckId, params.courseId)

    	// Check for errors
    	if (chapterInstance.hasErrors()) {
    		render(view: "create", model: [chapterInstance: chapterInstance, courseInstance: chapterInstance.course], userDecks: Deck.findAllByOwner(User.load(springSecurityService.principal.id)))
    		return
    	}

    	redirect action: "show", controller: "course", id: "${params.courseId}"
    }

    def show(Chapter chapterInstance, Integer max) {
		params.max = Math.min(max ?: 12, 100)
		List<Flashcard> flashcards = Flashcard.findAllByDeck(chapterInstance.deck, params)
		respond flashcards, model:[chapterInstance: chapterInstance, flashcardCount: Flashcard.countByDeck(chapterInstance.deck),isOwner: (springSecurityService.principal.id == chapterInstance.deck.owner.id),
			userInstance: User.load(springSecurityService.principal.id)]
    }

    def practice(Chapter chapterInstance, Integer max) {
        // Add the progress to the deck
        log.info "Deck ${chapterInstance.deck.id}"
        chapterInstance.deck.metaClass.progress = deckService.getDeckProgress(chapterInstance.deck)
        CardUsage cardUsageInstance = deckService.getNextFlashcard(chapterInstance.deck, params.cardUsageId, params.ranking as Integer, params.rankingType as Integer)
        
        User userInstance = User.load(springSecurityService.principal.id)
        Customization customizationInstance = Customization?.findByOwnerAndCard(userInstance, cardUsageInstance.flashcard)
        //get the right image - take the customization if made, else the flashcard provided image if made, else null
        String imageURL = null
        if (customizationInstance?.imageAssoc){
            imageURL = customizationInstance.imageAssoc?.url
        } else if (cardUsageInstance.flashcard?.image){
            imageURL = cardUsageInstance.flashcard.image.url
        }
        //get the right audio - take the customization if made, else the flahscard proviced audio if made, else null
        Long audioSysId = null
        if (customizationInstance?.audioAssoc){
            audioSysId = customizationInstance.audioAssoc?.id
        } else if (cardUsageInstance.flashcard?.audio){
            audioSysId = cardUsageInstance.flashcard.audio.id
        }

        [cardRankingInstance: flashcardService.getLastRanking(cardUsageInstance.flashcard.id), 
        chapterInstance: chapterInstance, 
        cardUsageInstance: cardUsageInstance,
        imageURL: imageURL, 
        audioSysId: audioSysId, 
        rankingType: params.rankingType]
    }

        /**
    * Returns the flaskcard for a deck
    */
    def flashcardSelect(Chapter chapterInstance) {
        render template: "selectFlashcards", bean: chapterInstance, var: "chapterInstance"
    }
}
