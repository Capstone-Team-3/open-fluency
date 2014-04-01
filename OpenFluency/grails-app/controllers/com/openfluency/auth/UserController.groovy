package com.openfluency.auth


import com.openfluency.Constants
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.openfluency.language.*

@Transactional(readOnly = true)
class UserController {
    
    def userService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()]
    }

    def profile() {
        render view: "show", model: [userInstance: User.load(springSecurityService.principal.id)]
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {
        def user = new User(params)
        [userInstance: user, languages: Language.findAll(), authorities: Role.findAllByAuthorityNotEqual(Constants.ROLE_ADMIN)]
    }

    @Transactional
    def save() {
        
        User userInstance = userService.createUser(params.username, params.password, params.email, params.userType.id, params.list('language.id'), params.list('proficiency.id'))

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'create'
            return
        }

        redirect action: 'show', id: userInstance.id
    }

    def edit(User userInstance) {
        respond userInstance
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

        userInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*'{ respond userInstance, [status: OK] }
        }
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
