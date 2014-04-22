package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured

class QuizController {

	def courseService

	@Secured(['ROLE_INSTRUCTOR'])
    def create(Course courseInstance) { 
    	[courseInstance: courseInstance]
    }

    @Secured(['ROLE_INSTRUCTOR'])
    def save(Course courseInstance) {
    	
    	Quiz quizInstance = courseService.createQuiz(
    		params.title, 
    		params.maxCardTime as Integer, 
    		params.testElement as Integer,
    		params.flashcardIds,
    		courseInstance
    		)

    	if(!quizInstance) {
    		flash.message = "Something went wrong, please try again"
    		redirect action: "create", id: courseInstance.id
    	}

    	if(quizInstance.hasErrors()) {
    		flash.message = "Something went wrong, please try again"
    		flash.message = "Something went wrong, please try again"	
    	}

    	redirect action: "show", id: quizInstance.id
    }

    def show(Quiz quizInstance) {
    	[quizInstance: quizInstance]
    }
}
