package com.openfluency.course

import com.openfluency.language.Alphabet
import com.openfluency.flashcard.Deck
import com.openfluency.auth.User
import grails.transaction.Transactional

@Transactional
class CourseService {

	def springSecurityService

    /**
    * Create a new course owned by the currently logged in user
    */
    Course createCourse(String title, String description, String alphabetId) {
    	Course course = new Course(title: title, description: description, alphabet: Alphabet.load(alphabetId), owner: User.load(springSecurityService.principal.id))
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

    List<Course> searchCourses(Long languageId, String keyword) {
        log.info "Searching Courses with languageId: $languageId and Keywords: $keyword"

        Course.withCriteria {

            // Apply alphabet criteria
            if(languageId) {
                alphabet {
                    language {
                        eq('id', languageId)
                    }
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
}
