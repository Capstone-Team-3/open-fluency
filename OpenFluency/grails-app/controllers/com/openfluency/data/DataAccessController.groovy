package com.openfluency.data

import grails.converters.JSON
import com.openfluency.auth.User
import com.openfluency.Constants
import com.openfluency.flashcard.*
import com.openfluency.media.*
import com.openfluency.course.*
import com.openfluency.language.*

class DataAccessController {

	def exportService
	def grailsApplication

    //def index() {}

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
                           "languageProficiencies", 
                           "decks", 
                           "courses"]

    		Map labels = ["userId": "UserID", 
                          "userType": "UserType", 
                          "languageProficiencies": "LanguageIDsAndProficiencies",
                          "decks": "Decks",
                          "courses": "Courses"]

            //custom formatter
    		def lpify = {domain, value -> def lps = domain.getLanguageProficiencies().collect{it.getLanguageProficiencyMap()};
                                                    lps.add("${domain.nativeLanguage.id}:Native");
                                                    lps.toString()}
            def deckify = {domain, value -> domain.getDecks().collect{it.id}}
            def coursify = {domain, value -> domain.getCourses().collect{it.id}}
            Map formatters = ["userId": idify,
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
            Map formatters = ["userId": userIdify,
                              "flashcard": valIdify,  
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
    /**uses the exporter plugin to export anonymous data on quizzes
    */
    def exportQuizData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=QuizData.${params.extension}")
            //set fields
            List fields = ["quiz", 
                           "course", 
                           "testElement",
                           "maxCardTime", 
                           "numQuestions",
                           "questions",
                           "grades"]
            //map headings
            Map labels = ["quiz": "QuizID", 
                          "course": "CourseID", 
                          "testElement": "TestedElement",
                          "maxCardTime": "QuestionTimeLimit",
                          "numQuestions": "NumberOfQuestions",
                          "questions": "QuestionToFlashcardIDs",
                          "grades": "UserGrades"]
            //map field data formatters
            Map formatters = ["quiz": idify, 
                              "course": valIdify,
                              "numQuestions": {domain, value -> domain.countQuestions()}, 
                              "questions": {domain, value -> domain.getQuestions().collect{"${it.id}:${it.flashcard.id}"}.toString()},
                              "grades": {domain, value -> Grade.findAllByQuiz(domain).collect{"${it.user.id}:${it.correctAnswers}"}.toString()}]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Quiz.list(params), fields, labels, formatters, parameters)
        }
        
        [quizInstanceList: Quiz.list(params)]
    }

    /**uses the exporter plugin to export anonymous data on answers to quiz questions
    */
    def exportAnswerData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=AnswerData.${params.extension}")
            //set fields
            List fields = ["answer", 
                           "user",
                           "course",
                           "quiz",
                           "question",
                           "testElement", 
                           "selection",
                           "correctAnswer", 
                           "correct",
                           "status"]
            //map headings
            Map labels = ["answer": "AnswerID", 
                          "user": "UserID",
                          "course": "CourseID",
                          "quiz": "QuizID",
                          "question": "QuestionID",
                          "testElement": "TestedElement", 
                          "selection": "UserAnswerID",
                          "correctAnswer": "CorrectAnswerID", 
                          "correct": "AnsweredCorrectly",
                          "status": "AnswerStatus"]
            //map field data formatters
            Map formatters = ["answer": idify, 
                              "user": valIdify,
                              "course": {domain, value -> domain.question.quiz.course.id},
                              "quiz": {domain, value -> domain.question.quiz.id},
                              "question": valIdify,
                              "testElement": {domain, value -> domain.question.quiz.testElement}, 
                              "selection": valIdify,
                              "correctAnswer": {domain, value -> domain.question.flashcard.id}, 
                              "correct": {domain, value -> (value) ? 1 : 0}]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Answer.list(params), fields, labels, formatters, parameters)
        }
        
        [answerInstanceList: Answer.list(params)]
    }

    /**uses the exporter plugin to export anonymous data on flashcards
    */
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
                           "primaryAlphabet": "FocalAlphabetID",
                           "primaryElement": "FocalPhrase", 
                           "unitMapping": "BaseAlphabetID",
                           "secondaryElement": "BasePhrase",
                           "pronunciationAlphabet": "PronunciationAlphabetID",
                           "pronunciation": "Pronunciation",
                           "image": "Image",
                           "imageURI": "ImageURI",
                           "audio": "Audio"]
            //custom formatters (general formatters defined at class level)
            def primeAlpha = {domain, value -> domain.getPrimaryUnit().alphabet.id}
            def primeEle = {domain, value -> domain.getPrimaryUnit().getPrint()}
            def secAlpha = {domain, value -> domain.getSecondaryUnit().alphabet.id}
            def secEle = {domain, value -> domain.getSecondaryUnit().getPrint()}
            def proAlpha = {domain, value -> domain?.pronunciation.alphabet.id}
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
    /**uses the exporter plugin to export anonymous data on decks
    */
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
                          "language": "FocalLanguageID",
                          "sourceLanguage": "BaseLanguage"]
            //map field data formatters
            Map formatters = ["deck": idify, 
                              "owner": valIdify,
                              "language": valIdify,
                              "sourceLanguage": stringify]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Deck.list(params), fields, labels, formatters, parameters)
        }
        
        [deckInstanceList: Deck.list(params)]   
    }
    /**uses the exporter plugin to export anonymous data on courses
    */
    def exportCourseData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=CourseData.${params.extension}")
            //set fields
            List fields = ["course", 
                           "owner",
                           "title",
                           "description",
                           "language",
                           "startDate",
                           "endDate",
                           "courseNumber",
                           "chapters",
                           "quizzes"]
            //map headings
            Map labels = ["course": "CourseID", 
                          "owner": "UserID",
                          "title": "Title",
                          "description": "Description",
                          "language": "FocalLanguageID",
                          "startDate": "StartDate",
                          "endDate": "EndDate",
                          "courseNumber": "CourseNumber",
                          "chapters": "ChapterDeckIDs",
                          "quizzes": "QuizIDs"]
            //map field data formatters
            Map formatters = ["course": idify, 
                              "owner": valIdify,
                              "language": valIdify,
                              "startDate": stringify,
                              "endDate": stringify,
                              "chapters": {domain, value -> domain.getChapters().collect{it.deck.id}.toString()},
                              "quizzes": {domain, value -> Quiz.findAllByCourse(domain).collect{it.id}.toString()}]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Course.list(params), fields, labels, formatters, parameters)
        }
        
        [courseInstanceList: Course.list(params)]   
    }
    /**uses the exporter plugin to export anonymous data on Language and Alphabets in the system
    */
    def exportLanguageData(){
        if (params?.extension && params?.extension == "csv"){           
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=LanguageData.${params.extension}")
            //set fields
            List fields = ["languageId",
                           "language", 
                           "alphabetId",
                           "name"]
            //map headings
            Map labels = ["languageId": "LanguageID",
                          "language": "Language",
                          "alphabetId": "AlphabetID",
                          "name": "Alphabet"]
            //map field data formatters
            Map formatters = ["languageId": {domain, value -> domain.language.id},
                              "language": {domain, value -> value.name},
                              "alphabetId": {domain, value -> domain.id}]
            //map additional parameters
            Map parameters = ["separator": "\t"]
            
            exportService.export("csv", response.outputStream, Alphabet.list(params), fields, labels, formatters, parameters)
        }
        
        [alphabetInstanceList: Alphabet.list(params)]
    }
}
