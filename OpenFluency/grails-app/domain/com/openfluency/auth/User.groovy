package com.openfluency.auth

import com.openfluency.language.*
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course

/**
 *  The user class is a core domain object in the OpenFluency system (see uml).
 *  User also plays a role in the SpringSecurity service.  The username field must be
 *  unique (enforced by the system).  Most of this class is defined by and in the
 *  SpringSecurity plug in documentation
 */

class User {  

	transient springSecurityService

	String username
	String password
	boolean enabled = false
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired 
	String email
	Role userType
	//the user's native language
	Language nativeLanguage

	/**
	 *  This function gets all the decks created by the user
	 *  @Return a List of Decks
	 */
	List<Deck> getDecks() {
		return Deck.findAllByOwner(this)
	}

	/**
	 *  This function gets all the courses created by the user
	 *  @Return a list of courses
	 */
	List<Deck> getCourses() {
		return Course.findAllByOwner(this)
	}

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		userType nullable: false
	}

	static mapping = {
		password column: '`password`'
	}

	/**  Function supporting Spring Security - gets all authorities/roles for a user
	 *   @Return a set of user Role
	 */
	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	/**
	 *  This function gets all the language proficiencies for a user
	 *  @Return a list of LanguageProficiency for the user
	 */
	List<LanguageProficiency> getLanguageProficiencies() {
		return LanguageProficiency.findAllByUser(this)
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

	String toString(){
		username
	}
}
