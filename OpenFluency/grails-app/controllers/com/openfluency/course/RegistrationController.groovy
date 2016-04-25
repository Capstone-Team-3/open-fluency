package com.openfluency.course

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.openfluency.Constants
import com.openfluency.auth.User

class RegistrationController {

	def courseService, springSecurityService

	@Secured(['ROLE_INSTRUCTOR'])
	def approve(Registration registrationInstance) { 
		if(!courseService.approveRegistration(registrationInstance)){
			flash.message = "You're not allowed to update registrations in courses you don't own"
            redirect action: "index", controller: "home"
            return
		}

		redirect controller: "course", action: "students", id: registrationInstance.course.id
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def reject(Registration registrationInstance) { 
		if(!courseService.rejectRegistration(registrationInstance)){
			flash.message = "You're not allowed to update registrations in courses you don't own"
            redirect action: "index", controller: "home"
            return
		}
		
		redirect controller: "course", action: "students", id: registrationInstance.course.id
	}

	@Secured(['isAuthenticated()'])
	def enrolledCourses() {
		User user = User.load(springSecurityService.principal.id)
		def reg=Registration.findAllByUser(user)
		render reg?.course as JSON
	}
}
