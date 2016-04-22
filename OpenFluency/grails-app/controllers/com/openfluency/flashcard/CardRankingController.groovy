package com.openfluency.flashcard

import static org.springframework.http.HttpStatus.*

import com.openfluency.language.Pronunciation
import com.openfluency.language.Alphabet
import com.openfluency.media.MediaService
import com.openfluency.auth.User
import com.openfluency.language.Unit

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
			System.out.println ("matches "+ matches.group(0) + " "+ matches.group(1))
		}
		list(params)
	}

	def list = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		User user = User.load(springSecurityService.principal.id)
		def ranking = CardRanking.findAllByUser(user,
				[max: params.max, offset: params.offset, sort:params.sort, order:params.order])
		def total = CardRanking.where { user == user }.count()

		def meaning =  getStudyStatsDisplay("meaningRanking",user)
		def symbol =  getStudyStatsDisplay("symbolRanking",user)
		def pronunciation =  getStudyStatsDisplay("pronunciationRanking",user)
		def summaries = ["Meaning": meaning, "Symbol": symbol, "Pronunciation": pronunciation ]

		if (request.xhr) {
			// ajax request
			def model=[cardRankingInstanceList: ranking,cardRankingInstanceTotal:total, summaries: summaries,
				meaningSummary: meaning, pronunciationSummary: pronunciation, symbolSummary: symbol]
			render(template: "table", model: model)
		}
		else {
			respond ranking, model:[cardRankingInstanceTotal:total, meaningSummary:meaning,
				pronunciationSummary: pronunciation, symbolSummary: symbol]
		}
	}

	def getStudyStatsDisplay(String statName, User user) {
		def stats =  getStudyStats(statName,user)
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
}
