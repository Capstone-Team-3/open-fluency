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

		def courseInstance = courseService.createCourse(params.title, params.description, params.language.id)

		// Check for errors
    	if (courseInstance.hasErrors()) {
    		render(view: "create", model: [courseInstance: courseInstance])
    		return
    	}

    	redirect action: "show", id: courseInstance.id
	}

	def show(Course courseInstance) {
		[courseInstance: courseInstance, isOwner: springSecurityService.principal.id == courseInstance.owner.id, userInstance: User.load(springSecurityService.principal.id)]
	}

	def search() {
		Long languageId = params['filter-lang'] as Long
		String keyword = params['search-text']
		[keyword: keyword, languageId: languageId, courseInstanceList: courseService.searchCourses(languageId, keyword), 
            languageInstanceList: Language.list(), userInstance: User.load(springSecurityService.principal.id)]
	}

	def enroll(Course courseInstance) {
		Registration registrationInstance = courseService.createRegistration(courseInstance)

		// Check for errors
    	if (registrationInstance.hasErrors()) {
    		redirect action: "show", id: courseInstance.id
    		return
    	}

    	flash.message = "Well done! You're now registered in this course!"
    	render view: "show", model: [courseInstance: courseInstance, isOwner: springSecurityService.principal.id == courseInstance.owner.id, userInstance: User.load(springSecurityService.principal.id)]
	}
}
