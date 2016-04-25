package com.openfluency.flashcard

import static org.springframework.http.HttpStatus.*

import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet
import com.openfluency.media.MediaService
import com.openfluency.auth.User
import com.openfluency.language.Unit
import com.openfluency.language.Language
import com.openfluency.course.Course
import com.openfluency.course.Grade
import com.openfluency.course.Registration
import grails.plugin.springsecurity.annotation.Secured
import java.util.regex.Matcher
import java.util.regex.Pattern

@Secured(['isAuthenticated()'])
class CardRankingController {

	def springSecurityService
	def cardRankingService
	final Pattern patt = Pattern.compile("/deck/show/(\\d+)")

	/**
	 * Render all data for a user
	 */

	def index(Integer max) {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def referer = request.getHeader('referer')
		Matcher matcher = patt.matcher(referer);
		boolean matches = matcher.matches();
		if (matches) {
			params['deck_id']= matches.group(1)
			log.info ("matches "+ matches.group(0) + " "+ matches.group(1))
		}
		list(params)
	}

	def list = {
		User user = User.load(springSecurityService.principal.id)
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		params.offset = Math.min(params.offset ? params.int('offset') : 0, 0)
		def course = params.course ? Course.load(params.long('course')) : null
		def ranking
		def quizzes = null
		def total
		if (course) {   // Getting all the stats for a course only
            def q4 = (params.sort)? ("order by "+ params.sort +" "+ params.order): ""
            def q3="select deck from Chapter where course_id =:course"
            def q2 ="select id from Flashcard where deck.id in (" + q3 +")"
            def query = "from CardRanking where user.id=:user and flashcard.id in (" + q2 + ")" + q4
            ranking = CardRanking.executeQuery(query,
                [user:user.id, course:course.id],
                [max: params.max, offset: params.offset])
			total = getCardRankingCountByCourse(course, user )
            quizzes = getQuizGradeByCourse(course, user)
		}
		else {
			ranking = CardRanking.findAllByUser(user,
				[max: params.max, offset: params.offset, sort:params.sort, order:params.order])
			total = CardRanking.where { user == user }.count()
		}

		def meaning =  getStudyStatsDisplay("meaningRanking",user,course)
		def symbol =  getStudyStatsDisplay("symbolRanking",user,course)
		def pronunciation =  getStudyStatsDisplay("pronunciationRanking",user,course)
		def summaries = ["Meaning": meaning, "Symbol": symbol, "Pronunciation": pronunciation ]
		def courses = getCourses(user)
		if (request.xhr) {
			// ajax request
			def model=[cardRankingInstanceList: ranking,cardRankingInstanceTotal:total, summaries: summaries,
				meaningSummary: meaning, pronunciationSummary: pronunciation, symbolSummary: symbol]
			render(template: "table", model: model)
		}
		else {
			respond ranking, model:[cardRankingInstanceTotal:total, meaningSummary:meaning,
				pronunciationSummary: pronunciation, symbolSummary: symbol, courses: courses, quizzes: quizzes]
		}
	}


	def getStudyStatsDisplay(String statName, User user, Course course) {
		def stats;
		if (course)
		 	stats = getStudyStatsByCourse(statName, course, user)
		else
			stats =  getStudyStats(statName,user)
		def total = 0
		def slist = [0,0,0,0]
		for (it in stats) {
			total += it[1]
			if (it[0] !=null ) {
				slist[it[0]] = it[1]
			} else {
				slist[0] = it[1]
			}
		}
		def stat=[]
		for (it in slist) {
			if (total > 0)
				stat.add((it * 100)/total)
			else
				stat.add(0)
		}
		return stat 
	}

	// code to do a select stat, count(*) group by stat 
	// stat options are meaningRanking, symbolRanking, pronunciationRanking
	def getStudyStats(String stat, User user) {
		def c = CardRanking.createCriteria()
		def pl = c.list {
			eq ('user', user )
			projections {
				groupProperty stat
				countDistinct 'id', 'total'
			}
			order (stat, 'desc')
		}
	}
	
	def getCourses(User user) {
        def courses = Registration.findAllByUser(user,[sort: "course.title", order: "desc"])
    }

	def getCourseMenu(User user) {
        def courses = Registration.findAllByUser(user,[sort: "course.title", order: "desc"])
        List options=[]
          options.add([id:"", title:"All decks",selected:false]) 
		for (it in courses) {
          options.add([id: it.course.id, title:it.course.title, selected: it.course.id == params.course]) 
        }
        return options
    }


	def getStudyStatsByCourse(String stat, Course course, User user) {
		def q4=" group by "+stat;
		def q3="select deck from Chapter where course_id = :course"
		def q2 ="select id from Flashcard where deck.id in ("+q3 +")"
		def query = "from CardRanking where user.id= :user and flashcard.id in ("+ q2 + ")"
		def c = CardRanking.executeQuery("SELECT "+stat+", count(id) as total "+query + q4,
            [course:course.id, user: user.id])
		return c
   }

	def getFlashcardsByCourse(String stat, Course course, User user) {
		def q1="select deck from Chapter where course_id = :course"
		def query ="select id from Flashcard where deck.id in ("+q3 +")"
		def c= Flashcard.executeQuery(query,  [course:course.id])
        return c
    }

	def getCardRankingCountByCourse(Course course, User user) {
		def q3="select deck from Chapter where course_id = :course"
		def q2 ="select id from Flashcard where deck.id in ("+q3 +")"
		def query = "select count(*) from CardRanking where user.id= :user and flashcard.id in ("+ q2 + ")"
		def c= CardRanking.executeQuery(query,
            [course:course.id, user: user.id])
        return c
	}
	
	def getQuizGradeByCourse(Course course, User user) {
		def q2="select id from Quiz where course_id = :course"
		def query = "from Grade where user.id= :user and quiz.id in ("+ q2 + ")"
		def c= Grade.executeQuery(query,
				[course:course.id, user: user.id]
			 )
		return c
	}
}
