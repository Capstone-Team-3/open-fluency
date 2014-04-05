package com.openfluency.course

import com.openfluency.flashcard.*
import com.openfluency.auth.User

class ChapterController {

	def springSecurityService
	def courseService

    /**
    * Create a chapter for a particular course
    */
	def create(Course courseInstance) {
		render view: "create", model: [courseInstance: courseInstance, userDecks: Deck.findAllByOwnerAndLanguage(User.load(springSecurityService.principal.id), courseInstance.language)]
	}

	def save() {
		def chapterInstance = courseService.createChapter(params.title, params.description, params.deckId, params.courseId)

    	// Check for errors
    	if (chapterInstance.hasErrors()) {
    		render(view: "create", model: [chapterInstance: chapterInstance, courseInstance: chapterInstance.course], userDecks: Deck.findAllByOwner(User.load(springSecurityService.principal.id)))
    		return
    	}

    	redirect action: "show", controller: "course", id: "${params.courseId}"
    }

    def show(Chapter chapterInstance) {
    	[chapterInstance: chapterInstance, flashcardCount: Flashcard.countByDeck(chapterInstance.deck)]
    }

    def practice(Chapter chapterInstance, Integer max) {
        params.max = max ?: 1
        def flashcardInstance = Flashcard.findAllByDeck(chapterInstance.deck, params)
        [chapterInstance: chapterInstance, flashcardInstance: flashcardInstance, flashcardCount: Flashcard.countByDeck(chapterInstance.deck)]
    }
}
