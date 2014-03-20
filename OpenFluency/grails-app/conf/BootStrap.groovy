import com.openfluency.language.*

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

    	log.info "Booted!"

    }
    def destroy = {
    }
}
