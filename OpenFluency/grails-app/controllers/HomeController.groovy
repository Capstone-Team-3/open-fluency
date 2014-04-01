import com.openfluency.auth.User
import com.openfluency.flashcard.Deck
import com.openfluency.course.Course
import com.openfluency.course.Registration

class HomeController {

    def springSecurityService

    def index = {
        [deckInstanceList: Deck.findAllByOwner(User.load(springSecurityService.principal.id)), 
            myCourses: Course.findAllByOwner(User.load(springSecurityService.principal.id)), 
                registrations: Registration.findAllByUser(User.load(springSecurityService.principal.id))]
    }
}