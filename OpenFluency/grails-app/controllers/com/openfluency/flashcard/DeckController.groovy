package com.openfluency.flashcard

import com.openfluency.auth.User
import com.openfluency.language.Language
import grails.plugin.springsecurity.annotation.Secured
import com.openfluency.media.Customization
import com.openfluency.algorithm.AlgorithmService

@Secured(['isAuthenticated()'])
class DeckController {

	def springSecurityService
    def deckService
    def flashcardService
    def algorithmService

    def index() {
        redirect action: "list"
    }

    def list() {
        User loggedUser = User.load(springSecurityService?.principal?.id)
        List<Deck> deckInstanceList = Deck.findAllByOwner(loggedUser)
        List<Deck> othersDeckInstanceList = Share.findAllByReceiver(loggedUser).collect {it.deck}
        
        // Get the progress for each deck
        deckInstanceList.each {
            it.metaClass.progress = deckService.getDeckProgress(it)
        }

        othersDeckInstanceList.each {
            it.metaClass.progress = deckService.getDeckProgress(it)
        }

        [deckInstanceList: deckInstanceList, othersDeckInstanceList: othersDeckInstanceList, userInstance: loggedUser]
    }

    def create() {
        [deckInstance: new Deck(params), cardServerAlgos: algorithmService.cardServerNames()]
    }

    def save() {
        def deckInstance = deckService.createDeck(params.title, params.description, params['language.id'], params['sourceLanguage.id'],params.cardServerAlgo)

    	// Check for errors
    	if (deckInstance.hasErrors()) {
    		render(view: "create", model: [deckInstance: deckInstance])
    		return
    	}

    	flash.message = "Well done! You succesfully created a new deck!"

    	redirect action: "show", id: deckInstance.id
    }

    def show(Deck deckInstance, Integer max) {
    	params.max = Math.min(max ?: 12, 100)
        List<Flashcard> flashcards = Flashcard.findAllByDeck(deckInstance, params)

        // Add the progress to the deck
        deckInstance.metaClass.progress = deckService.getDeckProgress(deckInstance)

        respond flashcards, model:[deckProgress: deckService.getDeckProgress(deckInstance), deckInstance: deckInstance, flashcardCount: Flashcard.countByDeck(deckInstance), isOwner: (springSecurityService.principal.id == deckInstance.owner.id)]
    }

    def practice(Deck deckInstance) {
        // Add the progress to the deck
        deckInstance.metaClass.progress = deckService.getDeckProgress(deckInstance)
        CardUsage cardUsageInstance = deckService.getNextFlashcard(deckInstance, params.cardUsageId, params.ranking as Integer, params.rankingType as Integer)
        
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

        println "Customization = ${customizationInstance?.id}, image = ${imageURL}, audio = ${audioSysId}"
        
        [cardRankingInstance: flashcardService.getLastRanking(cardUsageInstance.flashcard.id), 
        deckInstance: deckInstance, 
        cardUsageInstance: cardUsageInstance, 
        imageURL: imageURL, 
        audioSysId: audioSysId, 
        rankingType: params.rankingType]
    }

    def search(Integer max) {
        Long languageId = params['filter-lang'] as Long
        String keyword = params['search-text']
        [keyword: keyword, languageId: languageId, deckInstanceList: deckService.searchDecks(languageId, keyword), 
        languageInstanceList: Language.list(), userInstance: User.load(springSecurityService.principal.id)]
    }

    def add(Deck deckInstance) {
        Share shareInstance

        if(deckInstance) {
            shareInstance = deckService.addDeck(deckInstance)
        }

        if(shareInstance) {
            flash.message = "You succesfully added ${deckInstance.title} to your decks!"
        } 
        else {
            flash.message = "Could not add this deck!"
        }

        redirect action: "list"
    }

    def remove(Deck deckInstance) {
        if(deckInstance && deckService.removeDeck(deckInstance)) {
            flash.message = "You succesfully removed ${deckInstance.title} from your decks!"
        }
        else {
            flash.message = "Could not remove ${deckInstance.title} from your decks!"
        }

        redirect action: "list"
    }
}
