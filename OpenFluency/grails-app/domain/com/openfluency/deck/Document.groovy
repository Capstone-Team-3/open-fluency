package com.openfluency.deck
import com.openfluency.language.Language
/**
 * The anki file document for upload, not saved
 * maps to a deck
 */
import com.openfluency.auth.User

class Document {
    User   owner
	String filename
	String fullPath
    Language language
	String description
	Date uploadDate = new Date()
	Date importedDate
	static constraints = {
		filename(blank:false,nullable:false)
		fullPath(blank:false,nullable:false)
        description(blank:true,nullable:true)
        importedDate nullable:true
		// Add unique name/owner
	}
	String toString() {return filename}
}
