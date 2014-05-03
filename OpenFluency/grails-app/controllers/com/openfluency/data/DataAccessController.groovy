package com.openfluency.data

import grails.converters.JSON
import com.openfluency.auth.User
import com.openfluency.Constants
import com.openfluency.flashcard.*
import com.openfluency.media.*
import com.openfluency.course.*

class DataAccessController {

	def exportService
	def grailsApplication

    def index() {}

    //common usage formatting closures
    def idify = {domain, value -> domain.id}
    def valIdify = {domain, value -> (value != null) ? value.id : ""}
    def binify = {domain, value -> (value != null) ? 1 : 0}
    def elementify = {domain, value -> (value != null) ? Constants.CARD_ELEMENTS[(value as Integer)] : ""}
    def rankify = {domain, value -> (value != null) ? Constants.DIFFICULTIES[(value as Integer)] : ""}
    def stringify = {domain, value -> (value != null) ? value.toString() : ""}

    /**uses exporter plugin to export anonymous Biographical data on users to a tab separated text file
    */
    def exportBioData()  {
    	if (params?.extension && params?.extension == "csv"){    		
    		response.contentType = grailsApplication.config.grails.mime.types[params.format]
    		response.setHeader("Content-disposition", "attachment; filename=BiographicalData.${params.extension}")

    		List fields = ["userId", 
                           "userType", 
                           "nativeLanguage", 
                           "languageProficiencies", 
                           "decks", 
                           "courses"]

    		Map labels = ["userId": "UserID", 
                          "userType": "UserType", 
                          "nativeLanguage": "NativeLanguage", 
                          "languageProficiencies": "OtherLanguages",
                          "decks": "Decks",
                          "courses": "Courses"]

            //custom formatter
    		def lpify = {domain, value -> domain?.getLanguageProficiencies().collect{it.toString()}.toString() }
            def deckify = {domain, value -> domain.getDecks().collect{it.id}}
            def coursify = {domain, value -> domain.getCourses().collect{it.id}}
            Map formatters = ["userId": idify ,
                              "languageProficiencies": lpify,
                              "decks": deckify,
                              "courses": coursify]
    		
            Map parameters = ["separator": "\t"]
    		
    		exportService.export("csv", response.outputStream, User.list(params), fields, labels, formatters, parameters)
    	}
    	[userInstanceList: User.list(params)]
    }

    /**uses exporter plugin to export anonymous Flashcard Practice usage data in a tab separated text file
    */
    def exportPracticeData() {
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=CardUsageData.${params.extension}")
            //set fields
            List fields = ["userId", "flashcard", "rankingType", "ranking", "dateCreated", "endTime"]
            //map headings
            Map labels = ["userId": "UserID", 
                          "flashcard": "FlashcardID", 
                          "rankingType": "Element", 
                          "ranking": "Difficulty",
                          "dateCreated": "StartTime",
                          "endTime": "EndTime"]
            //custom formatters (others are in the class level formatters section)
            def userIdify = {domain, value -> domain.user.id}
            //map field data formatters
            Map formatters = ["userId": valIdify, 
                              "flashcard": idify, 
                              "rankingType": elementify, 
                              "ranking": rankify,
                              "dateCreated": stringify,
                              "endTime": stringify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, CardUsage.list(params), fields, labels, formatters, parameters)
        }
        
        [cardUsageInstanceList: CardUsage.list(params)]
    }

    /**uses the exporter plugin to export anonymous data on user customization of flashcard images and audio
    */
    def exportCustomizationData() {
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=CustomizationData.${params.extension}")
            //set fields
            List fields = ["owner", "card", "audioAssoc", "imageAssoc", "imageURI"]
            //map headings
            Map labels = ["owner": "UserID", 
                          "card": "FlashcardID", 
                          "audioAssoc": "Audio", 
                          "imageAssoc": "Image",
                          "imageURI": "ImageURI"]
            //custom formatters (general formatters defined at class level)
            def imageURIify = {domain, value -> (domain?.imageAssoc?.url) ?: ""}
            //map field data formatters
            Map formatters = ["owner": valIdify, 
                              "card": valIdify, 
                              "audioAssoc": binify, 
                              "imageAssoc": binify,
                              "imageURI": imageURIify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Customization.list(params), fields, labels, formatters, parameters)
        }
        
        [customizationInstanceList: Customization.list(params)]
    }

    def exportFlashcardData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=FlashcardData.${params.extension}")
            //set fields
            List fields = ["flashcard",
                           "deck",
                           "primaryAlphabet",
                           "primaryElement", 
                           "unitMapping",
                           "secondaryElement",
                           "pronunciationAlphabet",
                           "pronunciation",
                           "image",
                           "imageURI",
                           "audio"]
            //map headings
            Map labels = ["flashcard": "FlashcardID",
                           "deck": "DeckID",
                           "primaryAlphabet": "FocalAlphabet",
                           "primaryElement": "FocalPhrase", 
                           "unitMapping": "BaseAlphabet",
                           "secondaryElement": "BasePhrase",
                           "pronunciationAlphabet": "PronunciationAlphabet",
                           "pronunciation": "Pronunciation",
                           "image": "Image",
                           "imageURI": "ImageURI",
                           "audio": "Audio"]
            //custom formatters (general formatters defined at class level)
            def primeAlpha = {domain, value -> domain.getPrimaryUnit().alphabet.toString()}
            def primeEle = {domain, value -> domain.getPrimaryUnit().getPrint()}
            def secAlpha = {domain, value -> domain.getSecondaryUnit().alphabet.toString()}
            def secEle = {domain, value -> domain.getSecondaryUnit().getPrint()}
            def proAlpha = {domain, value -> domain?.pronunciation.alphabet.toString()}
            def proEle = {domain, value -> domain?.pronunciation.getPrint()}
            def imageURIify = {domain, value -> (domain?.image?.url) ?: ""}
            //map field data formatters
            Map formatters = ["flashcard": idify,
                              "deck": valIdify,
                              "primaryAlphabet": primeAlpha,
                              "primaryElement": primeEle,
                              "unitMapping": secAlpha,
                              "secondaryElement": secEle,
                              "pronunciationAlphabet": proAlpha,
                              "pronunciation": proEle,
                              "image": binify,
                              "imageURI": imageURIify,
                              "audio": binify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Flashcard.list(params), fields, labels, formatters, parameters)
        }
        
        [flashcardInstanceList: Flashcard.list(params)]    
    }

    def exportDeckData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=DeckData.${params.extension}")
            //set fields
            List fields = ["deck", 
                           "owner",
                           "title",
                           "description",
                           "cardServerName",
                           "language",
                           "sourceLanguage"]
            //map headings
            Map labels = ["deck": "DeckID", 
                          "owner": "UserID",
                          "title": "Title",
                          "description": "Description",
                          "cardServerName": "Algorithm",
                          "language": "FocalLanguage",
                          "sourceLanguage": "BaseLanguage"]
            //map field data formatters
            Map formatters = ["deck": idify, 
                              "owner": valIdify,
                              "language": stringify,
                              "sourceLanguage": stringify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Deck.list(params), fields, labels, formatters, parameters)
        }
        
        [deckInstanceList: Deck.list(params)]   
    }

    def exportCourseData(){
        /*if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=CourseData.${params.extension}")
            //set fields
            List fields = ["deck", 
                           "owner",
                           "title",
                           "description",
                           "cardServerName",
                           "language",
                           "sourceLanguage"]
            //map headings
            Map labels = ["deck": "DeckID", 
                          "owner": "UserID",
                          "title": "Title",
                          "description": "Description",
                          "cardServerName": "Algorithm",
                          "language": "FocalLanguage",
                          "sourceLanguage": "BaseLanguage"]
            //map field data formatters
            Map formatters = ["deck": idify, 
                              "owner": valIdify,
                              "language": stringify,
                              "sourceLanguage": stringify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Course.list(params), fields, labels, formatters, parameters)
        }
        
        [courseInstanceList: Course.list(params)]
        */   
    }
}
