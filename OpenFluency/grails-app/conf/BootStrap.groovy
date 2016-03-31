import com.openfluency.language.*
import com.openfluency.auth.*
import com.openfluency.flashcard.*
import com.openfluency.course.*
import com.openfluency.algorithm.*
import com.openfluency.Constants
import grails.util.Environment

class BootStrap {

	def languageService
    def flashcardService
    def grailsApplication
    def quizService
    def algorithmService
    def deckService
    def flashcardInfoService
    def userService

    def init = { servletContext ->

        Environment.executeForCurrentEnvironment {
            test {
                // Create languages
                Language japanese = new Language(name: 'Japanese', code: 'JAP').save(failOnError: true)
                Language english = new Language(name: 'English', code: 'ENG-US').save(failOnError: true)
				Language chinese = new Language(name: 'Chinese', code: 'CHN').save(failOnError: true)
				Language korean = new Language(name: 'Korean', code: 'KOR').save(failOnError: true)

                log.info "Created ${Language.count()} languages"

                // Create Alphabets
                Alphabet kanji = new Alphabet(name: 'Kanji', language: japanese, code: "kanji", encodeEntities: false).save(failOnError: true)
                Alphabet katakana = new Alphabet(name: 'Katakana', language: japanese, code: "ja_on", encodeEntities: false).save(failOnError: true)
                Alphabet hiragana = new Alphabet(name: 'Hiragana', language: japanese, code: "ja_kun", encodeEntities: false).save(failOnError: true)
				Alphabet hanzi = new Alphabet(name: 'Hanzi', language: chinese, code: "hanzi", encodeEntities: false).save(failOnError: true)
                Alphabet romaji = new Alphabet(name: "Romaji", language: japanese, code: "romaji").save(failOnError: true)

                Alphabet hangul = new Alphabet(name: "Hangul", language: korean, code: "hangul").save(failOnError: true)
                Alphabet hanja = new Alphabet(name: "Hanja", language: korean, code: "hanja").save(failOnError: true)
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
                def admin = userService.createUser("admin", "admin", "admin@openfluency.com", adminRole.id.toString(), english.id.toString(), [], [])
                def student = userService.createUser("student", "test", "student@openfluency.com", studentRole.id.toString(), english.id.toString(), [], [])
                def instructor = userService.createUser("instructor", "test", "instructor@openfluency.com", instructorRole.id.toString(), english.id.toString(), [], [])
                def researcher = userService.createUser("researcher", "test", "researcher@openfluency.com", researcherRole.id.toString(), english.id.toString(), [], [])

                // Enable all by default
                User.list().each {
                    it.enabled = true
                    it.save()
                }

                // Add language proficiency to student user
                new LanguageProficiency(user: student, proficiency: nativeP, language: japanese).save(failOnError: true)

                // Load sample language - the configuration is now externalized. 
                // The settings are in: conf/open-fluency-config.properties
                languageService.loadLanguage(grailsApplication.config.kanjiDictionaryURL, kanji, latin)

                // Register some CardServiceAlgorithms
                CardServer linearWithShuffle = new LinearWithShuffle()
                algorithmService.addCardServer(linearWithShuffle.name, linearWithShuffle)
                CardServer sm2SpacedRep = new SM2SpacedRepetition()
                algorithmService.addCardServer(sm2SpacedRep.name, sm2SpacedRep)

                log.info "Added the ${linearWithShuffle} algo"

                // Build a bunch of sample decks
                Deck restaurant = new Deck(sourceLanguage: english, language: japanese, title: "Restaurant", description: "Words that I would use in a restaurant context", owner: student, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
                Deck business = new Deck(sourceLanguage: english, language: japanese, title: "Business", description: "Words that I would use in a business context", owner: student, cardServerName: "SM2-Spaced-Repetition").save(failOnError: true)
                Deck sports = new Deck(sourceLanguage: english, language: japanese, title: "Sports", description: "Words that I would use in a sports context", owner: student, cardServerName: "Linear-With-Shuffle").save(failOnError: true)

                log.info "Built the 3 decks"

                // Build a few flashcards for the business deck
                flashcardService.createRandomFlashcards(business, kanji)  

                log.info "Built the business flashcards and queues"      

                // Build a few decks to be used in a course and a bunch of flashcards in each
                Deck chapterDeck1_1 = new Deck(sourceLanguage: english, language: japanese, title: "Kanji for Dummies 1", description: "Simple phrases 1", owner: instructor, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
                Deck chapterDeck1_2 = new Deck(sourceLanguage: english, language: japanese, title: "Kanji for Dummies 2", description: "Simple phrases 2", owner: instructor, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
                Deck chapterDeck2_1 = new Deck(sourceLanguage: english, language: japanese, title: "Kanji for Dummies 1", description: "Simple phrases 1", owner: instructor, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
                Deck chapterDeck2_2 = new Deck(sourceLanguage: english, language: japanese, title: "Kanji for Dummies 2", description: "Simple phrases 2", owner: instructor, cardServerName: "Linear-With-Shuffle").save(failOnError: true)
                flashcardService.createRandomFlashcards(chapterDeck1_1, kanji)
                flashcardService.createRandomFlashcards(chapterDeck1_2, kanji)
                flashcardService.createRandomFlashcards(chapterDeck2_1, kanji)
                flashcardService.createRandomFlashcards(chapterDeck2_2, kanji)

                // Create 2 Japanese courses
                Course kanji1 = new Course(visible: true, open: true, language: japanese, title: "Kanji for Dummies", description: "Start here if you have no idea what you're doing", owner: instructor).save(failOnError: true)
                // Create two chapters for this course
                Chapter chapter1_1 = new Chapter(title: "Chapter 1: The basics", description: "If you get lost in Japan, at least you need to know these words", deck: chapterDeck1_1, course: kanji1).save(failOnError: true)
                Chapter chapter1_2 = new Chapter(title: "Chapter 2: A bit more into it", description: "Now that you can get to the bathroom, learn how to ask for a beer and other important phrases", deck: chapterDeck1_2, course: kanji1).save(failOnError: true)

                Course kanji2 = new Course(visible: true, open: false, language: japanese, title: "Kanji for More Advanced Dummies", description: "A sequel of the acclaimed, award-winning course Kanji for Dummies", owner: instructor).save(failOnError: true)
                // Create two chapters for this course
                Chapter chapter2_1 = new Chapter(title: "Chapter 1: Welcome back!", description: "Continuing to learn more Japanese", deck: chapterDeck2_1, course: kanji2).save(failOnError: true)
                Chapter chapter2_2 =new Chapter(title: "Chapter 2: Still a dummy? Don't think so!", description: "Now that's what I call a Japanese-speaking dummy", deck: chapterDeck2_2, course: kanji2).save(failOnError: true)
				
				// Create a Chinese course
				Course chineseCourse = new Course(visible: true, open: true, language: chinese, title: "Chinese for Dummies", description: "Start here if you have no idea what you're doing", owner: instructor).save(failOnError: true)

                // Create a test for the course
                quizService.createQuiz("Chapter 1 Quiz", new Date(), 20, Constants.MEANING, chapter1_1.deck.flashcards.collect {it.id}, chapter1_1.course)
                quizService.createQuiz("Chapter 2 Quiz", new Date(), 0, Constants.PRONUNCIATION, chapter1_2.deck.flashcards.collect {it.id}, chapter1_2.course)
				
				// Create a quiz manually, just to see if it works
				Quiz quizInstance = new Quiz(
					course: kanji1,
					title: "Fill in the Blank Quiz",
					enabled: true,
					liveTime: new Date(),
					maxCardTime: 20
					).save(failOnError: true)
					
				Question question = new Question(quiz: quizInstance, question: "私は日本語を ______ ます", questionType: Constants.FILL_IN_BLANK).save(failOnError: true)
				
				new QuestionOption(question: question, option: "話し", answerKey: 1).save(failOnError: true)
				new QuestionOption(question: question, option: "食べ", answerKey: 0).save(failOnError: true)
				new QuestionOption(question: question, option: "飲み", answerKey: 0).save(failOnError: true)
				
				// Create a confuser quiz
				Quiz confuserQuiz = new Quiz(
					course: kanji1,
					title: "Japanese Confuser Quiz",
					enabled: true,
					liveTime: new Date(),
					maxCardTime: 20
					).save(failOnError: true)
					
				quizService.createConfuserQuestion(confuserQuiz, "Thank you", "ありがとう", japanese, hiragana)
				quizService.createConfuserQuestion(confuserQuiz, "Hello", "こんにちは", japanese, hiragana)
				quizService.createConfuserQuestion(confuserQuiz, "Certificates", "卷", japanese, kanji)
				quizService.createConfuserQuestion(confuserQuiz, "End", "末", japanese, kanji)
				
				// Create a confuser quiz
				Quiz chineseConfuserQuiz = new Quiz(
					course: chineseCourse,
					title: "Chinese Confuser Quiz",
					enabled: true,
					liveTime: new Date(),
					maxCardTime: 20
					).save(failOnError: true)
					
				quizService.createConfuserQuestion(chineseConfuserQuiz, "Big", "大", chinese, hanzi)
				quizService.createConfuserQuestion(chineseConfuserQuiz, "Arrow", "矢", chinese, hanzi)
				quizService.createConfuserQuestion(chineseConfuserQuiz, "Outlaw", "无法无天", chinese, hanzi)
				quizService.createConfuserQuestion(chineseConfuserQuiz, "Field", "田", chinese, hanzi)
				
                // Sign up the student for course 1
                new Registration(user: student, course: kanji1).save()
                flashcardInfoService.resetCourseFlashcardInfo(student, kanji1)
                
                log.info "Booted!"
            }

            development {
                // Register some CardServiceAlgorithms
                CardServer linearWithShuffle = new LinearWithShuffle()
                algorithmService.addCardServer(linearWithShuffle.name, linearWithShuffle)
                CardServer sm2SpacedRep = new SM2SpacedRepetition()
                algorithmService.addCardServer(sm2SpacedRep.name, sm2SpacedRep)
                log.info "Added the ${linearWithShuffle} algo"
            }
        }
        def destroy = {
        }
    }
}
