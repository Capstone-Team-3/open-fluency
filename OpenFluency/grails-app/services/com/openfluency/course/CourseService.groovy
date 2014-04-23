package com.openfluency.course

import com.openfluency.flashcard.Flashcard
import com.openfluency.language.Language
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import grails.transaction.Transactional

@Transactional
class CourseService {

	def springSecurityService
    def deckService

    /**
    * Create a new course owned by the currently logged in user
    */
    Course createCourse(String title, String description, String languageId) {
    	Course course = new Course(title: title, description: description, language: Language.load(languageId), owner: User.load(springSecurityService.principal.id))
    	course.save()
    	return course
    }

    Chapter createChapter(String title, String description, String deckId, String courseId) {
    	Chapter chapter = new Chapter(title: title, description: description, deck: Deck.load(deckId), course: Course.load(courseId))
    	chapter.save()
    	return chapter
    }

    Registration createRegistration(Course courseInstance){ 
        User loggedUser = User.load(springSecurityService.principal.id)

        // Create the registration but don't save it
        Registration registration = new Registration(course: courseInstance, user: User.load(springSecurityService.principal.id))

        // First check if the user is not registered already
        if(Registration.findByUserAndCourse(loggedUser, courseInstance)) {
            log.info "User ID: ${loggedUser.id} is already enrolled in Course ID: ${courseInstance.id}"
            registration.errors.reject('user.registered', 'You are already registered for ${courseInstance.title}!')
        }
        else {
            // No old registration found, save it
            log.info "User ID: ${loggedUser.id} enrolled in Course ID: ${courseInstance.id}"
            registration.save()
        }

        return registration
    }

    /**
    * Returns a list of all the courses that are enabled and have a live time that is either null or before the current date
    */
    List<Quiz> getLiveQuizes(Course courseInstance) {
        Quiz.withCriteria {
            eq('enabled', true)
            or {
                le('liveTime', new Date())
                isNull('liveTime')
            }
        }
    }

    /**
    * Search for courses of a given language and that contain the given keywords in the title or description
    */
    List<Course> searchCourses(Long languageId, String keyword) {
        log.info "Searching Courses with languageId: $languageId and Keywords: $keyword"

        Course.withCriteria {

            // Apply language criteria
            if(languageId) {
                language {
                    eq('id', languageId)
                }
            }

            // Search using keywords in the title or description
            if(keyword) {
                or {
                    ilike("title", "%${keyword}%")
                    ilike("description", "%${keyword}%")
                }
            }
        }
    }


    Quiz createQuiz(String title, Date liveTime, Integer maxCardTime, Integer testElement, List flashcardIds, Course courseInstance) {

        log.info "FlashcardIDs: ${flashcardIds}"

        // First check that it's the owner of the course who's creating it
        if(courseInstance.owner.id != springSecurityService.principal.id){
            return null    
        }

        User loggedUser = User.load(springSecurityService.principal.id)

        // Create the quiz
        Quiz quizInstance = new Quiz(course: courseInstance, title: title, testElement: testElement, enabled: true, liveTime: liveTime, maxCardTime: maxCardTime).save(failOnError: true)

        if(quizInstance.hasErrors()) {
            return quizInstance
        }

        // Now create the questions for each flashcard
        Random rand = new Random() // randomize the options for the questions
        
        flashcardIds.each {
            log.info "Creating question for flashcard ${it}"
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
}
