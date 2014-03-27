import com.openfluency.language.*
import com.openfluency.auth.*

class BootStrap {

	def languageService

    def init = { servletContext ->

    	// Create languages
    	Language japanese = new Language(name: 'Japanese').save()
    	Language english = new Language(name: 'English').save()

    	log.info "Created ${Language.count()} languages"

    	// Create Alphabets
    	Alphabet kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: true).save()
    	Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on", encodeEntities: true).save()
    	Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun", encodeEntities: true).save()
    	Alphabet latin = new Alphabet(name: "Latin", language: english, code: "pinyin").save()

    	log.info "Created ${Alphabet.count()} alphabets"

    	// Load sample language
    	languageService.loadLanguage("https://s3.amazonaws.com/OpenFluency/resources/kanji_simple_short.xml", kanji, latin)

        //load user roles
        def studentRole = new Role(authority: 'Student').save(flush: true)
        def instructorRole = new Role(authority: 'Instructor').save(flush: true)
        def researcherRole = new Role(authority: 'Researcher').save(flush: true)
        def adminRole = new Role(authority: 'Admin').save(flush: true)

        //load language proficiency levels
        def nativeP = new Proficiency(proficiency: 'Native').save(flush: true)
        def fluentP = new Proficiency(proficiency: 'Fluent').save(flush: true)
        def advancedP = new Proficiency(proficiency: 'Advanced').save(flush: true)
        def intermediateP = new Proficiency(proficiency: 'Intermediate').save(flush: true)
        def beginnerP = new Proficiency(proficiency: 'Beginner').save(flush: true)

        //load a a language proficiency mapping
        def langProf = new LanguageProficiency(language: japanese, proficiency: nativeP).save(flush: true)

        //build an admin user
        def testUser = new User(username: 'admin', password: 'admin', userType: adminRole)
        testUser.addToLanguageProficiencies(langProf)
        testUser.save(flush: true)
        UserRole.create testUser, adminRole, true

    	log.info "Booted!"

    }
    def destroy = {
    }
}
