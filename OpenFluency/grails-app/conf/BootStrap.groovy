import com.openfluency.language.*
import com.openfluency.auth.*
import com.openfluency.Constants

class BootStrap {

	def languageService

    def init = { servletContext ->

    	// Create languages
    	Language japanese = new Language(name: 'Japanese').save(failOnError: true)
    	Language english = new Language(name: 'English').save(failOnError: true)

    	log.info "Created ${Language.count()} languages"

    	// Create Alphabets
    	Alphabet kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save(failOnError: true)
    	Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on", encodeEntities: true).save(failOnError: true)
    	Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun", encodeEntities: true).save(failOnError: true)
    	Alphabet latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save(failOnError: true)

    	log.info "Created ${Alphabet.count()} alphabets"

        // Load user roles
        def studentRole = new Role(authority: Constants.ROLE_STUDENT).save(flush: true, failOnError: true)
        def instructorRole = new Role(authority: Constants.ROLE_INSTRUCTOR).save(flush: true, failOnError: true)
        def researcherRole = new Role(authority: Constants.ROLE_RESEARCHER).save(flush: true, failOnError: true)
        def adminRole = new Role(authority: Constants.ROLE_ADMIN).save(flush: true)

        // Load language proficiency levels
        def nativeP = new Proficiency(proficiency: 'Native').save(flush: true, failOnError: true)
        def fluentP = new Proficiency(proficiency: 'Fluent').save(flush: true, failOnError: true)
        def advancedP = new Proficiency(proficiency: 'Advanced').save(flush: true, failOnError: true)
        def intermediateP = new Proficiency(proficiency: 'Intermediate').save(flush: true, failOnError: true)
        def beginnerP = new Proficiency(proficiency: 'Beginner').save(flush: true, failOnError: true)

        // Load a language proficiency mapping
        def langProf = new LanguageProficiency(language: japanese, proficiency: nativeP).save(flush: true, failOnError: true)

        // Build an admin user
        def testUser = new User(username: 'admin', password: 'admin', userType: adminRole)
        testUser.save(flush: true, failOnError: true)
        UserRole.create testUser, adminRole, true

        // Add language proficiency to admin user
        new UserLanguageProficiency(user: testUser, languageProficiency: langProf).save(failOnError: true)

    	// Load sample language
    	languageService.loadLanguage("https://s3.amazonaws.com/OpenFluency/resources/kanji_simple_short.xml", kanji, latin)

    	log.info "Booted!"

    }
    def destroy = {
    }
}
