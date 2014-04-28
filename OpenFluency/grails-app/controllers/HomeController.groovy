import com.openfluency.Constants
import com.openfluency.auth.User
import com.openfluency.auth.UserRole
import com.openfluency.auth.Role
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course
import com.openfluency.course.Registration

class HomeController {

	def springSecurityService

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
        List<Deck> decks = Deck.withCriteria {
        	owner {
        		eq('id', springSecurityService.principal.id)
        	}
        	maxResults(4)
        	order("lastUpdated", "desc")
        }

        List<Registration> registrations = Registration.findAllByUserAndStatusNotEqual(User.load(springSecurityService.principal.id), Constants.REJECTED)

        [deckInstanceList: decks, myCourses: courses, registrations: registrations]
    }
}