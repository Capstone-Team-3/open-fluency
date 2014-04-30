package com.openfluency.auth


import com.openfluency.Constants
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.openfluency.language.*
import grails.plugin.springsecurity.annotation.Secured

@Transactional(readOnly = true)
class UserController {

    def userService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()]
    }

    @Secured(['isAuthenticated()'])
    def profile() {
        render view: "show", model: [userInstance: User.load(springSecurityService.principal.id)]
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {
        // If logged in we shouldn't be creating users
        if(springSecurityService.currentUser) {
            redirect action: "index", controller: "home"
            return
        }

        def user = new User(params)
        [userInstance: user, languages: Language.findAll(), authorities: Role.findAllByAuthorityNotEqual(Constants.ROLE_ADMIN)]
    }

    @Transactional
    def save() {
        // If logged in we shouldn't be creating users
        if(springSecurityService.currentUser) {
            redirect action: "index", controller: "home"
            return
        }

        //create user instance via service
        User userInstance = userService.createUser(params.username, params.password, params.email, params.userType.id, params['nativeLanguage.id'], params.list('language.id'), params.list('proficiency.id'))

        if (userInstance.hasErrors()) {
            render view: "create", model: [userInstance: userInstance, languages: Language.findAll(), authorities: Role.findAllByAuthorityNotEqual(Constants.ROLE_ADMIN)]
            return
        }

        //log new user in
        if(userInstance.enabled) {
            springSecurityService.reauthenticate userInstance.username
            flash.message = "${userInstance.username}, welcome to OpenFluency!"
        } 
        else {
            flash.message = "${userInstance.username}, your account is pending approval!"
        }
        redirect(uri: '/')
        //redirect action: 'show', id: userInstance.id
    }

    @Secured(['isAuthenticated()'])
    def edit() {
        [disabled: "disabled", userInstance: User.load(springSecurityService.principal.id), languages: Language.findAll(), authorities: Role.findAllByAuthorityNotEqual(Constants.ROLE_ADMIN)]
    }

    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'edit'
            return
        }

        userService.editUser(userInstance, params.list('language.id'), params.list('proficiency.id'), params.password, params.email)
        flash.message = "${userInstance.username}, your profile has been updated."
        redirect(uri:'/')
    }

    @Transactional
    def delete(User userInstance) {

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    // Reset User password
    def reset(){
        if(params.email){
            def user = userService.resetUserPassword(params.email)

            if(!user){
                flash.message = "Could not find user with email: $params.email"
            }
            else {
                flash.message = "New password sent to $params.email"   
            }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def enable(User userInstance) {
        userService.updateUser(userInstance, true)
        redirect action: "index"
    }

    @Secured(['ROLE_ADMIN'])
    def disable(User userInstance) {
        userService.updateUser(userInstance, false)
        redirect action: "index"
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userInstance.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
