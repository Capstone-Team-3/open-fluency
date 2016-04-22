
package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import au.com.bytecode.opencsv.CSVReader
import grails.converters.JSON
import java.text.DateFormat
import java.text.SimpleDateFormat

import com.openfluency.Constants

class QuizEditorController {

	def springSecurityService
	
	@Secured(['ROLE_INSTRUCTOR'])
	def create(Course courseInstance) {
		[courseInstance: courseInstance]
	}
	
	@Secured(['ROLE_INSTRUCTOR'])
	def edit(Quiz quizInstance){
		// Only the instructor that owns the course should be allowed access
		if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
			flash.message = "You're not allowed to edit this quiz"
			redirect action: "index", controller: "home"
			return
		}

		Course courseInstance = quizInstance.course
		
		[quizInstance: quizInstance, courseInstance: courseInstance]
	}
	
	@Secured(['ROLE_INSTRUCTOR'])
	def save () {
		
		Course courseInstance = Course.load(params["course.id"] as Long)
		
		// First check that it's the owner of the course who's creating it
		if(courseInstance.owner.id != springSecurityService.principal.id){
			flash.message = "You're not authorized to create a quiz for a course you don't own!"
			redirect action: "index", controller: "home"
			return
		}
		
		String title = params.title
		Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0		
		Date liveTime = params.liveTime
		Date endTime = params.endTime
		
		// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
		String csv = params.questions
		
		try {
		
			Quiz quizInstance = new Quiz(
				course: courseInstance,
				title: title,
				enabled: true,
				liveTime: liveTime,
				endTime: endTime,
				maxCardTime: maxCardTime
				).save(failOnError: true)
			
			saveQuestions(quizInstance, csv)
			
			redirect controller: "quiz", action: "show", id: quizInstance.id
		}
		catch (Exception e) {
			
			log.error "Error: ${e.message}", e
			
			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: courseInstance.id
		} 
	}
	
	@Secured(['ROLE_INSTRUCTOR'])
	def update(Quiz quizInstance) {
		// First check that it's the owner of the course who's creating it
		if(quizInstance.course.owner.id != springSecurityService.principal.id){
			flash.message = "You're not authorized to edit a quiz for a course you don't own!"
			redirect action: "index", controller: "home"
			return
		}

		try {
			String title = params.title
			Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0
			Date liveTime = params.liveTime
			Date endTime = params.endTime
			
			// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
			String csv = params.questions
			
			quizInstance.title = title
			quizInstance.liveTime = liveTime
			quizInstance.endTime = endTime
			quizInstance.maxCardTime = maxCardTime
			quizInstance.save(failOnError: true)
			
			List<QuestionOption> oldOptions = new ArrayList<QuestionOption>()
			
			Question.findAllByQuiz(quizInstance).each {
				QuestionOption.findAllByQuestion(it).each {
					oldOptions.add(it)
				}
			}
			
			saveQuestions(quizInstance, csv)
			
			oldOptions.each {
				it.delete();
			}
			
			redirect controller: "quiz", action: "show", id: quizInstance.id
		}
		catch (Exception e) {
			
			log.error "Error: ${e.message}", e
			
			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: quizInstance.course.id
		}
	}
	
	private void saveQuestions(Quiz quizInstance, String csv) {
		CSVReader reader = new CSVReader(new StringReader(csv))
		
		String[] line;
		while ((line = reader.readNext()) != null) {
			int lineSize = line.size()
			if (lineSize > 3) {
				String questionType = line[0]
				String questionString = line[1]
				String correctAnswer = line[2]
				
				List<String> wrongAnswers = new ArrayList<String>()
				
				for (int i = 3; i < lineSize; i++) {
					wrongAnswers.add(line[i])
				}
					
				Question question = new Question(quiz: quizInstance, question: questionString, questionType: Constants.MANUAL).save(failOnError: true)
				
				new QuestionOption(question: question, option: correctAnswer, answerKey: 1).save(failOnError: true)
				
				wrongAnswers.each {
					new QuestionOption(question: question, option: it, answerKey: 0).save(failOnError: true)
				}
			}
		}
	}
}