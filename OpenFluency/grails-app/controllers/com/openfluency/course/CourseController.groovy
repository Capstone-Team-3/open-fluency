package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import com.openfluency.language.Language

import grails.plugin.springsecurity.annotation.Secured

class CourseController {

	def springSecurityService
	def courseService
	def deckService
	def quizService	

	// Researchers will not be able to enroll or create courses
	@Secured(['ROLE_INSTRUCTOR', 'ROLE_STUDENT', 'ROLE_ADMIN'])
	def list() {
		def user = User.load(springSecurityService.principal.id)
		[myCourses: Course.findAllByOwner(user), registrations: Registration.findAllByUser(user)]
	}

	// Only instructors are able to create courses
	@Secured(['ROLE_INSTRUCTOR'])
	def create() {
		[courseInstance: new Course(params)]
	}

	// Only instructors are able to create or edit courses
	@Secured(['ROLE_INSTRUCTOR'])
	def save() {

		def courseInstance = courseService.createCourse(params.title, params.description, params.language.id, params.visible == "true", params.open == "true")

		// Check for errors
		if (courseInstance.hasErrors()) {
			render(view: "create", model: [courseInstance: courseInstance])
			return
		}

		redirect action: "show", id: courseInstance.id
	}

	@Secured(['isAuthenticated()'])
	def show(Course courseInstance) {
		
		// Add progress  to chapters
		courseInstance.chapters.each {
			it.metaClass.progress = deckService.getDeckProgress(it.deck)
		}

		// Get quizes
		List<Quiz> quizesInstanceList = courseService.getLiveQuizes(courseInstance)

		// Get student grades for each quiz
		quizesInstanceList.each {
			it.metaClass.finalGrade = quizService.getPercentageGrade(it)
		}

		[quizesInstanceList: quizesInstanceList, 
		courseInstance: courseInstance, 
		isOwner: springSecurityService?.principal?.id == courseInstance.owner.id, 
		userInstance: User.load(springSecurityService.principal.id),
		students: Registration.findAllByCourse(courseInstance)]
	}

	@Secured(['isAuthenticated()'])
	def search(Integer max) {
		Long languageId = params['filter-lang'] as Long
		String keyword = params['search-text']
		[keyword: keyword, languageId: languageId, courseInstanceList: courseService.searchCourses(languageId, keyword), 
		languageInstanceList: Language.list(), userInstance: User.load(springSecurityService.principal.id)]
	}

	// Only students can enroll in courses
	@Secured(['ROLE_STUDENT'])
	def enroll(Course courseInstance) {
		Registration registrationInstance = courseService.createRegistration(courseInstance)

		// Check for errors
		if (registrationInstance.hasErrors()) {
			flash.message = "Could not complete registration"
			redirect action: "show", id: courseInstance.id
			return
		}

		if(registrationInstance.status == Constants.APPROVED) {
			flash.message = "Well done! You're now registered in this course!"	
		}
		else {
			flash.message = "Well done! Your registration is pending approval!"
		}
		
		redirect action: "show", id: courseInstance.id
	}

	@Secured(['ROLE_STUDENT'])
	def drop(Course courseInstance) {

		// First find if the logged user is registered
		Registration registrationInstance = courseService.findRegistration(courseInstance)

		if(!registrationInstance) {
			flash.message = "You're not registered for this course"
			redirect(uri: request.getHeader('referer'))
			return
		}

		// Now drop the registration
		courseService.dropRegistration(registrationInstance)
		flash.message = "You just dropped the course ${courseInstance.title}."
		redirect(uri: request.getHeader('referer'))
	}

	// Only instructors can see enrolled students
	@Secured(['ROLE_INSTRUCTOR'])
	def students(Course courseInstance) {
		
		// Only allow editing for owner
		if(springSecurityService.principal.id != courseInstance?.owner?.id) {
			flash.message = "You don't have permissions to view this page"
			redirect action: "index", controller: "home"
			return
		}

		// Now need to get the stats for each student
		List<Registration> studentStats = courseService.getStudentStats(courseInstance)

		render view: "students", model: [courseInstance: courseInstance, enrolledStudents: studentStats]
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def edit(Course courseInstance) {

		// Only allow editing for owner
		if(springSecurityService.principal.id != courseInstance?.owner?.id) {
			flash.message = "You don't have permissions to edit this course"
			redirect action: "index", controller: "home"
			return
		}

		[courseInstance: courseInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def update(Course courseInstance) {

		// Only allow editing for owner
		if(springSecurityService.principal.id != courseInstance?.owner?.id) {
			flash.message = "You don't have permissions to edit this course"
			redirect action: "index", controller: "home"
			return
		}

		// Update it
		courseInstance = courseService.updateCourse(courseInstance, params.title, params.description, params.language.id, params.visible == "true", params.open == "true")

		// Check for errors
		if (courseInstance.hasErrors()) {
			render(view: "edit", model: [courseInstance: courseInstance])
			return
		}

		redirect action: "show", id: courseInstance.id
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def delete(Course courseInstance) {

		// Only allow deleting for owner
		if(springSecurityService.principal.id != courseInstance?.owner?.id) {
			flash.message = "You don't have permissions to delete this course"
			redirect action: "index", controller: "home"
			return
		}

		flash.message = "You just deleted ${courseInstance.title}"
		courseService.deleteCourse(courseInstance)
		redirect action: "list", controller: "course"
	}
}
