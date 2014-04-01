package com.openfluency.auth

class Role {

	String authority
	String name

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true, inList:["ROLE_ADMIN", "ROLE_STUDENT", "ROLE_INSTRUCTOR", "ROLE_RESEARCHER"]
	}

	String toString(){
		authority
	}
}
