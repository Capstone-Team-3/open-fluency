package com.openfluency.language

class Proficiency {

	String proficiency

    static constraints = {
    	proficiency nullable: false, unique: true, inList:["Native","Fluent","Advanced","Intermediate","Beginner"]
    }

    String toString(){
    	proficiency
    }
}
