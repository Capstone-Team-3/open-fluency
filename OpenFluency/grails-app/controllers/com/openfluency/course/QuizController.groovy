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

		// Check if the student has completed this quiz
		Grade gradeInstance = quizService.getGrade(quizInstance)
		if(gradeInstance) {
			redirect action: "report", id: gradeInstance.id
			return
		}

		// Check that it's live
		if(quizInstance.liveTime != null && quizInstance.liveTime > new Date()) {
			flash.message = "The test is not live yet!"
			redirect action: "show", controller: "course", id: quizInstance.course.id
			return
		}

		// Validate the quiz can be started
		Answer firstAnswer = quizService.startQuiz(quizInstance, session.id)
		if(firstAnswer) {
			// Render view to answer first question
			render view: "quiz", model: [answerInstance: firstAnswer, quizInstance: quizInstance]
		}
		else {
			flash.message = "Quiz cannot be started"
			redirect action: "index", controller: "home"
		}
	}

	def nextQuestion(Answer answerInstance) {

		// Answer current question
		quizService.answerQuestion(answerInstance, params.option as Long, session.id)
		
		Answer answer = quizService.nextQuestion(answerInstance.question.quiz)

		// No more questions left - check if the user has completed the course
		if(!answer) {
			Grade gradeInstance = quizService.finalizeQuiz(answerInstance.question.quiz)
			if(gradeInstance) {
				redirect action: "report", id: gradeInstance.id
			}
		}

		render view: "quiz", model: [answerInstance: answer]
	}

	def report(Grade gradeInstance) {
		[answerInstanceList: quizService.getAnswersByLoggedUser(gradeInstance.quiz), gradeInstance: gradeInstance]
	}
}
