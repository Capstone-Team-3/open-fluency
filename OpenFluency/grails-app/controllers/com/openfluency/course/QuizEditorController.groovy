package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import au.com.bytecode.opencsv.CSVReader
import grails.converters.JSON

import com.openfluency.Constants

class QuizEditorController {

	def springSecurityService
	
	@Secured(['ROLE_INSTRUCTOR'])
	def create(Course courseInstance) {
		[courseInstance: courseInstance]
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
		
		// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
		String csv = params.questions
		
		try {
		
			Quiz quizInstance = new Quiz(
				course: courseInstance,
				title: title,
				enabled: true,
				liveTime: liveTime,
				maxCardTime: maxCardTime
				).save(failOnError: true)
			
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
			
			redirect controller: "quiz", action: "show", id: quizInstance.id
		}
		catch (Exception e) {
			
			log.error "Error: ${e.message}", e
			
			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: courseInstance.id
		} 
	}
}
