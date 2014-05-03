package com.openfluency.data

import com.openfluency.auth.User
import grails.converters.JSON

class DataAccessController {

	def exportService
	def grailsApplication

    def index() { 
    }

    def exportBioData()  {
    	params.each {k,v -> println "k: ${k}, v: ${v}"}
    	if (params?.extension && params?.extension == "csv"){
    		
    		response.contentType = grailsApplication.config.grails.mime.types[params.format]
    		response.setHeader("Content-disposition", "attachment; filename=BiographicalData.${params.extension}")

    		List fields = ["userId", "userType", "nativeLanguage", "languageProficiencies"]
    		Map labels = ["userId": "UserID", "userType": "UserType", "nativeLanguage": "NativeLanguage", "languageProficiencies": "OtherLanguages"]

    		def jsonify = {domain, value -> domain.getLanguageProficiencies().collect{it.toString()}.toString() }
    		def idify = {domain, value -> domain.id}

    		Map formatters = ["userId": idify ,"languageProficiencies": jsonify]
    		Map parameters = ["separator": "\t"]
    		
    		exportService.export("csv", response.outputStream, User.list(params), fields, labels, formatters, parameters)
    	}
    	println "rendering"
    	[userInstanceList: User.list(params)]
    }

    
}
