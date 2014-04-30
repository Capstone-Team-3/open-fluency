package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.flashcard.Flashcard
import com.openfluency.language.Language
import com.openfluency.flashcard.Deck
import com.openfluency.flashcard.FlashcardInfoService
import com.openfluency.auth.User
import grails.transaction.Transactional

@Transactional
class CourseService {

	def springSecurityService
    def deckService
    def flashcardInfoService

    /**
    * Create a new course owned by the currently logged in user
    */
    Course createCourse(String title, String description, String languageId, Boolean visible=true, Boolean open=true) {
    	Course course = new Course(visible: visible, open: open, title: title, description: description, language: Language.load(languageId), owner: User.load(springSecurityService.principal.id))
    	course.save()
    	return course
    }

    /**
    * Updates an existing course
    */
    Course updateCourse(Course courseInstance, String title, String description, String languageId, Boolean visible, Boolean open) {

        // Update properties
        courseInstance.visible = visible
        courseInstance.open = open
        courseInstance.title = title
        courseInstance.description = description
        courseInstance.language = Language.load(languageId)
        courseInstance.owner = User.load(springSecurityService.principal.id)
        courseInstance.save()

        return courseInstance
    }

    Chapter createChapter(String title, String description, String deckId, String courseId) {
    	Deck deckInstance = Deck.load(deckId)
        Course courseInstance = Course.load(courseId)
        Chapter chapter = new Chapter(title: title, description: description, deck: deckInstance, course: courseInstance)
        chapter.save()
        //make sure the chapter maker has a fresh set of flashcardInfos for the deck
        flashcardInfoService.resetDeckFlashcardInfo(courseInstance.owner, deckInstance)
        return chapter
    }

    /**
    * Remove a chapter from a course
    */
    void deleteChapter(Chapter chapterInstance) {
        chapterInstance.delete()
    }

    /**
    * Remove a quiz from a course
    */
    void deleteQuiz(Quiz quizInstance) {
        // Remove all questions before
        Question.findAllByQuiz(quizInstance).each {
            deleteQuestion(it)
        }

        // Remove all grades
        Grade.findAllByQuiz(quizInstance).each {
            it.delete()
        }

        quizInstance.delete()
    }

    /**
    * Remove a question from a quiz
    */
    void deleteQuestion(Question questionInstance) {

        // First delete all options
        QuestionOption.findAllByQuestion(questionInstance).each {
            it.delete()
        }

        questionInstance.delete()
    }

    /**
    * Delete an entire course
    */
    void deleteCourse(Course courseInstance) {
        // First delete all quizes
        Quiz.findAllByCourse(courseInstance).each {
            deleteQuiz(it)
        }

        // Now delete all chapteres
        Chapter.findAllByCourse(courseInstance).each {
            deleteChapter(it)
        }

        // Delete all registrations
        Registration.findAllByCourse(courseInstance).each {
            dropRegistration(it)
        }
        
        // Now delete it
        courseInstance.delete()
    }

    /**
    * Update a given chapter with new properties
    */
    Chapter updateChapter(Chapter chapterInstance, String title, String description, String deckId, String courseId) {

        Deck deckInstance = Deck.load(deckId)
        Course courseInstance = Course.load(courseId)
        chapterInstance.title = title
        chapterInstance.description = description
        chapterInstance.deck = deckInstance
        chapterInstance.course = courseInstance
        chapterInstance.save()

        return chapterInstance
    }

    /**
    * Register the logged user for the given course
    */
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
            // Check if the course is open enrollment or not
            if(courseInstance.open) {
                log.info "User ID: ${loggedUser.id} enrolled in Course ID: ${courseInstance.id}"
                registration.status = Constants.APPROVED
            }
            else {
                log.info "User ID: ${loggedUser.id} enrolled in Course ID: ${courseInstance.id} - pending approval"
                registration.status = Constants.PENDING_APPROVAL
            }

            registration.save()
            //make sure the registrant has a fresh set of FlashcardInfos for the chapters of the course
            flashcardInfoService.resetCourseFlashcardInfo(loggedUser, courseInstance)
        }

        return registration
    }

    /**
    * Allows a user to drop a course
    */
    Registration findRegistration(Course courseInstance) {
        return Registration.findByUserAndCourse(User.load(springSecurityService.principal.id), courseInstance)
    }

    void dropRegistration(Registration registrationInstance) {
        registrationInstance.delete()
    }

    /**
    * Returns a list of all the courses that are enabled and have a live time that is either null or before the current date
    */
    List<Quiz> getLiveQuizes(Course courseInstance) {
        Quiz.withCriteria {
            eq('course', courseInstance)
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

            eq('visible', true) // only display courses that are visible

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

    Boolean approveRegistration(Registration registrationInstance) {
        return updateRegistration(registrationInstance, Constants.APPROVED)
    }

    Boolean rejectRegistration(Registration registrationInstance) {
        return updateRegistration(registrationInstance, Constants.REJECTED)
    }

    /**
    * Update a given registration to a given status
    */
    Boolean updateRegistration(Registration registrationInstance, Integer status) {

        // Only allow the instructor of the course to approve registrations
        if(registrationInstance.course.owner.id != springSecurityService.principal.id) {
            return false
        }

        // Update the status and return true
        registrationInstance.status = status
        registrationInstance.save(flush: true)

        return true
    }
}