package com.openfluency.auth

import com.openfluency.language.*
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course

class User { 

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired 
	String email
	Role userType

	List<Deck> getDecks() {
		return Deck.findAllByOwner(this)
	}

	List<Deck> getCourses() {
		return Course.findAllByOwner(this)
	}

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email nullable: true
    	userType nullable: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
