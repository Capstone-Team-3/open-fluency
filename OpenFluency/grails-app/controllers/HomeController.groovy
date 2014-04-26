import com.openfluency.Constants
import com.openfluency.auth.User
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course
import com.openfluency.course.Registration

class HomeController {

    def springSecurityService

    def index = {
        if (springSecurityService.getCurrentUser() == null || !springSecurityService.getCurrentUser()){
            return
        }

        [deckInstanceList: Deck.findAllByOwner(User.load(springSecurityService.principal.id)), 
            myCourses: Course.findAllByOwner(User.load(springSecurityService.principal.id)), 
                registrations: Registration.findAllByUserAndStatusNotEqual(User.load(springSecurityService.principal.id), Constants.REJECTED)]
    }
}