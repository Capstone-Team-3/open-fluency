package com.openfluency.course

import com.openfluency.flashcard.*
import com.openfluency.auth.User
import grails.plugin.springsecurity.annotation.Secured

class ChapterController {

	def springSecurityService
	def courseService

    @Secured(['isAuthenticated()'])
	def create(Course courseInstance) {
		render view: "create", model: [courseInstance: courseInstance, userDecks: Deck.findAllByOwnerAndLanguage(User.load(springSecurityService.principal.id), courseInstance.language)]
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
		respond flashcards, model:[chapterInstance: chapterInstance, flashcardCount: Flashcard.countByDeck(chapterInstance.deck)]
    }

    def practice(Chapter chapterInstance, Integer max) {
        def flashcardInstance = Flashcard.findByDeck(chapterInstance.deck, params)
        [chapterInstance: chapterInstance, flashcardInstance: flashcardInstance, flashcardCount: Flashcard.countByDeck(chapterInstance.deck)]
    }
}
