package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured

class QuizController {

    def springSecurityService
	def courseService

	@Secured(['ROLE_INSTRUCTOR'])
    def create(Course courseInstance) { 
    	[courseInstance: courseInstance]
    }

    @Secured(['ROLE_INSTRUCTOR'])
    def save() {
    	
        Course courseInstance = Course.load(params["course.id"] as Long)

        log.info "Current Course title ${courseInstance.title}"

    	Quiz quizInstance = courseService.createQuiz(
    		params.title, 
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

        log.info "after Course title ${courseInstance.title}"

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
}
