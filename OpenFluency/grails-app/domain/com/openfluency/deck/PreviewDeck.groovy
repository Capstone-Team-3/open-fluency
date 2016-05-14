package com.openfluency.deck

import java.util.ArrayList;
import java.util.Date;

import com.openfluency.algorithm.CardServer
import com.openfluency.auth.User;
import com.openfluency.language.Language

class PreviewDeck {
	User owner
	String name
	String filename
	String description
	Language language
	Language sourceLanguage
	Document	document
	String mediaDir	//Location 
	Date uploadDate = new Date()
	static hasMany = [cards: PreviewCard]

	static constraints = {
		description (blank:true, nullable: true)
		filename(blank:false, nullable:false)
		name(blank:false, nullable:false)
		language nullable: true
		sourceLanguage nullable: true
		owner nullable: true
		document nullable: true
		mediaDir nullable: true
	}
}
