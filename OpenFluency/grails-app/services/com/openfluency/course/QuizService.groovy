package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.flashcard.Flashcard
import com.openfluency.language.Language
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import grails.transaction.Transactional

@Transactional
class QuizService {

	def deckService
	def springSecurityService

	Quiz createQuiz(String title, Date liveTime, Integer maxCardTime, Integer testElement, List flashcardIds, Course courseInstance) {

		// Create the quiz
        Quiz quizInstance = new Quiz(course: courseInstance, title: title, testElement: testElement, enabled: true, liveTime: liveTime, maxCardTime: maxCardTime).save(failOnError: true)

        if(quizInstance.hasErrors()) {
        	return quizInstance
        }

        // Now create the questions for each flashcard
        Random rand = new Random() // randomize the options for the questions
        
        flashcardIds.each {
        	Flashcard flashcardInstance = Flashcard.get(it)

            // First create the question itself
            Question question = new Question(quiz: quizInstance, flashcard: flashcardInstance).save()
            
            // Now create a number of options - right now it's hard coded to 3 but it can be easily user defined
            int maxOptions = 3
            (1..maxOptions).each {
            	new QuestionOption(question: question, flashcard: deckService.getRandomFlashcard(flashcardInstance)).save()
            }
        }

        return quizInstance
    }

    /**
    * Initialize the quiz: create an answer for every question in the quiz for the logged student
    * @return true on success - for now this cannot fail
    */
    Boolean startQuiz(Quiz quizInstance) {

    	User loggedUser = User.load(springSecurityService.principal.id)

    	// Create all the answers
    	quizInstance.questions.each {
    		new Answer(
    			user: loggedUser,
    			question: it,
    			selection: null, // not answered yet
    			status: Constants.NOT_ANSWERED,
    			sessionId: session.id
    			).save()
    	}

    	return true
    }

    /**
	* Get the next question in the queue that has not been answered already
    */
    Answer nextQuestion(Quiz quizInstance) {
    	// Find the next answer in the quiz that has not yet been answered
		Answer answer = Answer.createCriteria().list(max: 1) {
			user {
				eq('id', springSecurityService.principal.id)
			}
			question {
				eq('quiz', quizInstance)
			}
			eq('status', Constants.NOT_ANSWERED)
		}[0]

		if(answer) {
			// Change the status of this answer to viewed
			answer.status = Constants.VIEWED
		}

		return answer
    }
}