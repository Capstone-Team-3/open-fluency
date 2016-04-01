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
    def flashcardInfoService

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

    def listPreview() {

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
        def deckInstance = deckService.createDeck(params.title, params.description, params['language.id'], params['sourceLanguage.id'], params.cardServerAlgo)

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

        User loggedUser = User.load(springSecurityService?.principal?.id)
        List<Deck> deckInstanceList = Deck.findAllByOwner(loggedUser)

        // Add the progress to the deck
        deckInstance.metaClass.progress = deckService.getDeckProgress(deckInstance)

        respond flashcards, model:[deckProgress: deckService.getDeckProgress(deckInstance), deckInstance: deckInstance, flashcardCount: Flashcard.countByDeck(deckInstance), isOwner: (springSecurityService.principal.id == deckInstance.owner.id), deckInstanceList:deckInstanceList]
    }

    def edit(Deck deckInstance) {
        
        // Check for permissions
        if(springSecurityService.principal.id != deckInstance.owner.id) {
            flash.message = "You don't have permissions to edit this deck!"
            redirect(uri: request.getHeader('referer'))
            return
        }

        [deckInstance: deckInstance, cardServerAlgos: algorithmService.cardServerNames()]
    }

    def delete(Deck deckInstance) {
        // Check for permissions
        if(springSecurityService.principal.id != deckInstance.owner.id) {
            flash.message = "You don't have permissions to edit this deck!"
            redirect(uri: request.getHeader('referer'))
            return
        }

        flash.message = "You succesfully deleted ${deckInstance.title}"
        deckService.deleteDeck(deckInstance)
        redirect action: "list"     
    }

    def update(Deck deckInstance) {
        
        // Check for permissions
        if(springSecurityService.principal.id != deckInstance.owner.id) {
            flash.message = "You don't have permissions to edit this deck!"
            redirect(uri: request.getHeader('referer'))
            return
        }

        // do we need to reset flashcard info?
        deckService.updateDeck(deckInstance, params.title, params.description, params['language.id'], params['sourceLanguage.id'], params.cardServerAlgo)

        // Check for errors
        if (deckInstance.hasErrors()) {
            render(view: "create", model: [deckInstance: deckInstance])
            return
        }

        redirect action: "show", id: deckInstance.id
    }

    def practice(Deck deckInstance) {
        User userInstance = User.load(springSecurityService.principal.id)
        
        //make sure user is registered - add deck if not
        if(!flashcardInfoService.hasFlashcardInfos(userInstance, deckInstance)) {
            deckService.addDeck(deckInstance)
        }
        // Add the progress to the deck
        deckInstance.metaClass.progress = deckService.getDeckProgress(deckInstance)
        CardUsage cardUsageInstance = deckService.getNextFlashcard(deckInstance, params.cardUsageId, params.ranking as Integer, params.rankingType as Integer)
        
        Customization customizationInstance = Customization?.findByOwnerAndCard(userInstance, cardUsageInstance.flashcard)
        
        //get the right image - take the customization if made, else the flashcard provided image if made, else null
        String imageURL = null
        if (customizationInstance?.imageAssoc){
            imageURL = customizationInstance.imageAssoc?.url
        } else if (cardUsageInstance.flashcard?.image){
            imageURL = cardUsageInstance.flashcard.image.url
        }
        //get the right audio - take the customization if made, else the flahscard provided audio if made, else null
        Long audioSysId = null
        if (customizationInstance?.audioAssoc){
            audioSysId = customizationInstance.audioAssoc?.id
        } else if (cardUsageInstance.flashcard?.audio){
            audioSysId = cardUsageInstance.flashcard.audio.id
        }

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

    /**
    * Remove a shared deck from list of logged user's decks
    */
    def remove(Deck deckInstance) {
        if(deckInstance && deckService.removeDeck(deckInstance)) {
            flash.message = "You succesfully removed ${deckInstance.title} from your decks!"
        }
        else {
            flash.message = "Could not remove ${deckInstance.title} from your decks!"
        }

        redirect action: "list"
    }

    /**
    * Upload flashcards from a CSV file in the format
    *   Symbol,Meaning,Pronunciation,ImageURL
    */
    def loadFlashcardsFromCSV(Deck deckInstance){
        
        request.getMultiFileMap().csvData.eachWithIndex { f, i ->
            List result = deckService.loadFlashcardsFromCSV(deckInstance, f)
            if(result.isEmpty()) {
                flash.message = "You succesfully uploaded your CSV!"
            }
            else {
                flash.message = result.join(",\n")
            }
        }

        redirect(action: "show", id: deckInstance.id)
    }
}
