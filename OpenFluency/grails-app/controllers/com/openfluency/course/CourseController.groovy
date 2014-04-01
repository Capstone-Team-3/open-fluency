package com.openfluency.course

import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import com.openfluency.language.Language

class CourseController {

	def springSecurityService
	def courseService

	/**
	* A list of the courses that the person owns or is enrolled in
	*/
	def list() {
		def user = User.load(springSecurityService.principal.id)
		[myCourses: Course.findAllByOwner(user), registrations: Registration.findAllByUser(user)]
	}

	def create() {
		[courseInstance: new Course(params)]
	}

	def save() {

		def courseInstance = courseService.createCourse(params.title, params.description)

		// Check for errors
    	if (courseInstance.hasErrors()) {
    		render(view: "create", model: [courseInstance: courseInstance])
    		return
    	}

    	redirect action: "show", id: courseInstance.id
	}

	def show(Course courseInstance) {
		[courseInstance: courseInstance, isOwner: springSecurityService.principal.id == courseInstance.owner.id]
	}

	def search() {
		// This needs to be changed to courses that the student is not already enrolled in
		// And all the fancy search logic should go here
		[courseInstanceList: Course.list(), languageInstanceList: Language.list()]
	}

	def enroll(Course courseInstance) {
		Registration registrationInstance = courseService.createRegistration(courseInstance)

		// Check for errors
    	if (registrationInstance.hasErrors()) {
    		redirect action: "show", id: courseInstance.id
    		return
    	}

    	flash.message = "Well done! You're now registered in this course!"
    	render view: "show", model: [courseInstance: courseInstance]
	}
}
