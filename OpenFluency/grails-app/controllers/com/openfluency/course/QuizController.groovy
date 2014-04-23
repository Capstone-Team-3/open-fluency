package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured

class QuizController {

	def springSecurityService
	def courseService
	def quizService

	@Secured(['ROLE_INSTRUCTOR'])
	def create(Course courseInstance) { 
		[courseInstance: courseInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def save() {
		Course courseInstance = Course.load(params["course.id"] as Long)

		// First check that it's the owner of the course who's creating it
        if(courseInstance.owner.id != springSecurityService.principal.id){
        	flash.message = "You're not authorized to create a quiz for a course you don't own!"
        	redirect action: "index", controller: "home"
        }

		// Build the quiz
		Quiz quizInstance = quizService.createQuiz(
			params.title, 
			params.liveTime,
			params.maxCardTime as Integer, 
			params.testElement as Integer,
			params.list('flashcardId'),
			courseInstance
			)

		if(!quizInstance) {
			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: params["course.id"]
		}

		if(quizInstance.hasErrors()) {
			flash.message = "Something went wrong, please try again"
		}

		redirect action: "show", id: quizInstance.id
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def show(Quiz quizInstance) {
		// Only the instructor that owns the course should be allowed access
		if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
			flash.message = "You're not allowed to view this course"
			redirect action: "index", controller: "home"
		}

		[quizInstance: quizInstance]
	}

	@Secured(['ROLE_STUDENT'])
	def take(Quiz quizInstance) {

		// Check that the quiz actually exists
		if(!quizInstance) {
			flash.message = "The test doesn't exist!"
			return
		}

		// Check that it's live
		if(quizInstance.liveTime != null && quizInstance.liveTime > new Date()) {
			flash.message = "The test is not live yet!"
			redirect action: "show", controller: "course", id: quizInstance.course.id
			return
		}

		if(quizService.startQuiz()) {

		}
	}

	def nextQuestion(Quiz quizInstance) {
		Answer answer = quizService.nextQuestion(quizInstance)

		// No more questions left
		if(!answer) {

		}

		render view: "quiz", model: [answerInstance: answer]
	}
}
