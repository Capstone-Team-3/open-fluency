package com.openfluency.course

import com.openfluency.flashcard.Deck
import com.openfluency.auth.User

class ChapterController {

	def springSecurityService
	def courseService

    /**
    * Create a chapter for a particular course
    */
	def create(Course courseInstance) {
		render view: "create", model: [courseInstance: courseInstance, userDecks: Deck.findAllByOwnerAndAlphabet(User.load(springSecurityService.principal.id), courseInstance.alphabet)]
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
    	[chapterInstance: chapterInstance]
    }
}
