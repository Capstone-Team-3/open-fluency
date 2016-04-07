package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import java.text.DateFormat
import java.text.SimpleDateFormat

class QuizController {

	def springSecurityService
		def courseService
		def quizService
		def exportService 
		def grailsApplication 

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
						return
				}

			println "TEST ELEMENT ${params.testElement}"

				// Build the quiz
				Quiz quizInstance = quizService.createQuiz(
						params.title, 
						params.liveTime,
						params.endTime,
						params.maxCardTime ? params.maxCardTime as Integer : 0, 
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
				flash.message = "You're not allowed to view this quiz"
					redirect action: "index", controller: "home"
					return
			}

			[quizInstance: quizInstance,
				isOwner: (springSecurityService.principal.id == quizInstance.course.owner.id)
					]
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def edit(Quiz quizInstance){
			// Only the instructor that owns the course should be allowed access
			if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
				flash.message = "You're not allowed to edit this quiz"
					redirect action: "index", controller: "home"
					return
			}

			[quizInstance: quizInstance,
				isOwner: (springSecurityService.principal.id == quizInstance.course.owner.id)
					]
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def update(Quiz quizInstance) {
			// First check that it's the owner of the course who's creating it
			if(quizInstance.course.owner.id != springSecurityService.principal.id){
				flash.message = "You're not authorized to edit a quiz for a course you don't own!"
					redirect action: "index", controller: "home"
					return
			}

			// Build the quiz
			quizService.updateQuiz(
					quizInstance,
					params.title, 
					params.liveTime,
					params.endTime,
					params.maxCardTime as Integer, 
					params.testElement as Integer,
					params.list('flashcardId')
					)

				if(quizInstance.hasErrors()) {
					flash.message = "Something went wrong, please try again"
				}

			redirect action: "show", id: quizInstance.id
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def delete(Quiz quizInstance) {
			// Only allow editing for owner
			if(springSecurityService.principal.id != quizInstance.course?.owner?.id) {
				flash.message = "You don't have permissions to delete this quiz"
					redirect action: "index", controller: "home"
					return
			}

			// Get course id to redirect afterwards
			Long courseId = quizInstance.course.id

				// Delete it
				courseService.deleteQuiz(quizInstance)

				redirect action: "show", controller: "course", id: courseId
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def deleteQuestion(Question questionInstance) {
			// Only allow editing for owner
			if(springSecurityService.principal.id != questionInstance.quiz.course?.owner?.id) {
				flash.message = "You don't have permissions to delete this question"
					redirect action: "index", controller: "home"
					return
			}

			// Get course id to redirect afterwards
			Long quizId = questionInstance.quiz.id

				// Delete it
				courseService.deleteQuestion(questionInstance)

				redirect action: "show", controller: "quiz", id: quizId
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
						redirect action: "show", controller: "course", id: quizInstance.course.id
						return
				}

		}

	def nextQuestion(Answer answerInstance) {
		Quiz quizInstance = Quiz.load(params.quiz)

			// Answer current question
			quizService.answerQuestion(answerInstance, params.option as Long, session.id)

			Answer answer = quizService.nextQuestion(answerInstance.question.quiz)

			// No more questions left - check if the user has completed the course
			if(!answer) {
				Grade gradeInstance = quizService.finalizeQuiz(answerInstance.question.quiz)
					if(gradeInstance) {
						redirect action: "report", id: gradeInstance.id
							return
					}
			}

		render view: "quiz", model: [answerInstance: answer, quizInstance: quizInstance]
	}

	def report(Grade gradeInstance) {
		[answerInstanceList: quizService.getAnswersByLoggedUser(gradeInstance.quiz), gradeInstance: gradeInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
		def quizImport(Course courseInstance) {
			[courseInstance: courseInstance]
		}


@Secured(['ROLE_INSTRUCTOR'])
def export(Quiz quizInstance) {
		def quizTitle = quizInstance.title

		response.contentType = grailsApplication.config.grails.mime.types[params.format]
		response.setHeader("Content-disposition", "attachment; filename=${quizTitle}.csv")

		List llist = quizInstance ? Question.findAllByQuiz(quizInstance) : []	
		String quesiton_type = "multiple_choice"
		// need to revise.
		String language = "Japanese"
		String default_image = "";
		String default_audio = "";	
		int i = 0

		List fields = ["question_number",
					   "language:alphabet",
					   "question_type",
					   "question",	  
					   "correctAnswer",
					   "wrongAnswers",
					   "image_link",
					   "audio_link"]
					   
		Map labels = ["question_number":"question_number",
					   "language:alphabet":"language:alphabet",
					   "question_type":"question_type",
					   "question":"Question",		   
					   "correctAnswer":"correct_answer",
					   "wrongAnswers":"wrongAnswers",
					   "image_link":"image_link",
					   "audio_link":"audio_link"]
					  
					 
		Map formatters = ["question_number": {domain, value -> return ++i},
		                "language:alphabet": {domain, value -> return language},
		                "question_type": {domain, value -> return quesiton_type},
						"question": {domain, value -> return domain.question},
						"correctAnswer": {domain, value -> return domain.getOptions().findAll{it.answerKey==1}.option},
						"wrongAnswers": {domain, value -> return domain.getOptions().findAll{it.answerKey==0}.option},
						"image_link": {domain, value -> return default_image},
						"audio_link": {domain, value -> return default_audio}]

		Map parameters = ["separator": ","]
		
		exportService.export("csv", response.outputStream, llist, fields, labels, formatters, parameters)
		
		 [questionInstanceList: Question.list(params)]
		 
	}



	/**
	 * Upload Quiz from a CSV file in the format
	 *   Symbol,Meaning,Pronunciation,ImageURL
	 */

	def loadQuizFromCSV(){

		//log.info "This hiddenField should display the date Rendered ${params.course.id}" 

		 Course courseInstance = Course.load(params["course.id"] as Long)

		 //First check that it's the owner of the course who's creating it
			 if(courseInstance.owner.id != springSecurityService.principal.id){
				 flash.message = "You're not authorized to create a quiz for a course you don't own!"
				 redirect action: "index", controller: "home"
				 return
			 }
			String title = params.title
			// convert string formatted tate to date objects
			String lTime = params.liveTime;
			String eTime = params.endTime;
			Date liveTime = null;
			log.info "the value for liveTime ${lTime}"
			if (lTime==null || lTime==""){
				//flash.message = "You failed to enter a date for when the quiz will go live.  The quiz has been set to immediately go live."
				//redirect(action: "show", controller: "course", id: courseInstance.id)
				liveTime = new Date();
			} else {
			log.info lTime
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			liveTime = format.parse(lTime);
			log.info "The date is now bogus2: ${liveTime}"
			}
			Date endTime = null;
			if (eTime==null || eTime==""){
				endTime = null;
			} else {
			log.info eTime
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			endTime = format.parse(eTime);
			}

			Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0

			request.getMultiFileMap().csvData.eachWithIndex { f, i ->
				List result = quizService.loadQuizFromCSV(title, liveTime, endTime, maxCardTime, courseInstance, f)
					if(result.isEmpty()) {
						flash.message = "You succesfully uploaded your Quiz!"
					}
					else {av
						flash.message = result.join(",\n")
					}
			}

		redirect(action: "show", controller: "course", id: courseInstance.id)
	}

}
