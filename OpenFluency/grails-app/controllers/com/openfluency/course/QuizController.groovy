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

        
        // Now the student can take the test
        // 1. start quiz
        // 2. create an empty answer for every question in the quiz for this student and save the session id

        // for every question in the quiz

        // - set the status of the Answer as viewed and send it to the user
        // - user selects answer
        // - update the selected option and set the status as answered
    }
}
