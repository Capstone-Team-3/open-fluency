import com.openfluency.Constants
import com.openfluency.auth.User
import com.openfluency.auth.UserRole
import com.openfluency.auth.Role
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course
import com.openfluency.course.Registration

class HomeController {

	def deckService
	def springSecurityService
    def exportService

	def index = {
		if (!springSecurityService.currentUser){
			return
		}

        // Get courses for this user
        List<Course> courses = Course.withCriteria {
        	owner {
        		eq('id', springSecurityService.principal.id)
        	}
        	maxResults(4)
        	order("lastUpdated", "desc")
        }

        // Get decks for this user
        List<Deck> deckInstanceList = Deck.withCriteria {
        	owner {
        		eq('id', springSecurityService.principal.id)
        	}
        	maxResults(4)
        	order("lastUpdated", "desc")
        }

        // Get the user's progress for the decks
        deckInstanceList.each {
            it.metaClass.progress = deckService.getDeckProgress(it)
        }

        List<Registration> registrations = Registration.findAllByUserAndStatusNotEqual(User.load(springSecurityService.principal.id), Constants.REJECTED)

        [deckInstanceList: deckInstanceList, myCourses: courses, registrations: registrations]
    }
}