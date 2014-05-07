package com.openfluency.language

/**
 *  Proficiency is a simple domain object that covers the various proficiency levels a user can report.
 *  When a new implementation of OpenFluency is deployed, objects for the 5 proficiency levels,
 *  ["Native","Fluent","Advanced","Intermediate","Beginner"], should be persisted
 */
class Proficiency {
	/** string representation of the proficiency level */
	String proficiency

    static constraints = {
    	proficiency nullable: false, unique: true, inList:["Native","Fluent","Advanced","Intermediate","Beginner"]
    }

    String toString(){
    	proficiency
    }
}
