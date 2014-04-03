import com.openfluency.language.*
import com.openfluency.auth.*
import com.openfluency.flashcard.*
import com.openfluency.course.*
import com.openfluency.Constants

class BootStrap {

	def languageService
    def flashcardService
    def grailsApplication

    def init = { servletContext ->

    	// Create languages
    	Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
    	Language english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)

    	log.info "Created ${Language.count()} languages"

    	// Create Alphabets
    	Alphabet kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
    	Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on", encodeEntities: true).save(failOnError: true)
    	Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun", encodeEntities: true).save(failOnError: true)
    	Alphabet latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)

    	log.info "Created ${Alphabet.count()} alphabets"

        // Load user roles
        def studentRole = new Role(name: "Student", authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
        def instructorRole = new Role(name: "Instructor", authority: Constants.ROLE_INSTRUCTOR).save(flush: true, failOnError: true)
        def researcherRole = new Role(name: "Researcher", authority: Constants.ROLE_RESEARCHER).save(flush: true, failOnError: true)
        def adminRole = new Role(name: "Administrator", authority: Constants.ROLE_ADMIN).save(flush: true)

        // Load language proficiency levels
        def nativeP = new Proficiency(proficiency: 'Native').save(flush: true, failOnError: true)
        def fluentP = new Proficiency(proficiency: 'Fluent').save(flush: true, failOnError: true)
        def advancedP = new Proficiency(proficiency: 'Advanced').save(flush: true, failOnError: true)
        def intermediateP = new Proficiency(proficiency: 'Intermediate').save(flush: true, failOnError: true)
        def beginnerP = new Proficiency(proficiency: 'Beginner').save(flush: true, failOnError: true)

        // Build some users
        def admin = new User(username: 'admin', password: 'admin', userType: adminRole, nativeLanguage: english)
        admin.save(flush: true, failOnError: true)
        UserRole.create admin, adminRole, true

        def student = new User(username: 'student', password: 'test', userType: studentRole, nativeLanguage: english)
        student.save(flush: true, failOnError: true)
        UserRole.create student, studentRole, true

        def instructor = new User(username: 'instructor', password: 'test', userType: instructorRole, nativeLanguage: english)
        instructor.save(flush: true, failOnError: true)
        UserRole.create instructor, instructorRole, true

        def researcher = new User(username: 'researcher', password: 'test', userType: researcherRole, nativeLanguage: english)
        researcher.save(flush: true, failOnError: true)
        UserRole.create researcher, researcherRole, true

        // Add language proficiency to student user
        new LanguageProficiency(user: student, proficiency: nativeP, language: japanese).save(failOnError: true)

    	// Load sample language - the configuration is now externalized. 
        // The settings are in: conf/open-fluency-config.properties
        boolean local = grailsApplication.config.localDictionary == 'true'
        if(local) {
            log.info "Loading local dictionary from ${grailsApplication.config.kanjiDictinoaryLocal}"
            languageService.loadLanguage(grailsApplication.config.kanjiDictinoaryLocal, kanji, latin, local)
        } 
        else {
            log.info "Loading remote dictionary from ${grailsApplication.config.kanjiDictionaryURL}"
            languageService.loadLanguage(grailsApplication.config.kanjiDictionaryURL, kanji, latin, local)
        }

        // Build a bunch of sample decks
        Deck restaurant = new Deck(alphabet: kanji, title: "Restaurant", description: "Words that I would use in a restaurant context", owner: student).save(failOnError: true)
        Deck business = new Deck(alphabet: kanji, title: "Business", description: "Words that I would use in a business context", owner: student).save(failOnError: true)
        Deck sports = new Deck(alphabet: kanji, title: "Sports", description: "Words that I would use in a sports context", owner: student).save(failOnError: true)
        
        // Build a few flashcards for the business deck
        flashcardService.createRandomFlashcards(business, kanji)        

        // Build a few decks to be used in a course and a bunch of flashcards in each
        Deck chapterDeck1 = new Deck(alphabet: kanji, title: "Kanji for Dummies 1", description: "Simple phrases 1", owner: instructor).save(failOnError: true)
        Deck chapterDeck2 = new Deck(alphabet: kanji, title: "Kanji for Dummies 2", description: "Simple phrases 2", owner: instructor).save(failOnError: true)        
        flashcardService.createRandomFlashcards(chapterDeck1, kanji)
        flashcardService.createRandomFlashcards(chapterDeck2, kanji)

        // Create a course
        Course kanji1 = new Course(alphabet: kanji, title: "Kanji for Dummies", description: "Start here if you have no idea what you're doing", owner: instructor).save(failOnError: true)
        // Create two chapters for this course
        new Chapter(title: "Chapter 1: The basics", description: "If you get lost in Japan, at least you need to know these words", deck: chapterDeck1, course: kanji1).save(failOnError: true)
        new Chapter(title: "Chapter 2: A bit more into it", description: "Now that you can get to the bathroom, learn how to ask for a beer and other important phrases", deck: chapterDeck2, course: kanji1).save(failOnError: true)

        log.info "Booted!"
    }
    def destroy = {
    }
}
