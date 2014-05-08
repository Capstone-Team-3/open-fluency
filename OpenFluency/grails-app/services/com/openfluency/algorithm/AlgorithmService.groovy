package com.openfluency.algorithm

import grails.transaction.Transactional
import java.util.*

/**
 *  AlgorithmService provides functionality to statically store and serve instances of
 *  CardServer - algorithms for ordering and serving flashcards in decks.  
 */
class AlgorithmService {

    def springSecurityService
    def deckService

	/** a static map to hold all instantiated card servers in - provides look up by their defined name */
    static Map<String,CardServer> cardServers = new HashMap<String,CardServer>()

    /**  Look up a CardServer by name - null if it isn't found
     *  @Return a CardServer implementation
    */
    CardServer cardServerByName(String cardServerName) {
    	if (cardServers.containsKey(cardServerName)) {
    		return cardServers.get(cardServerName)
    	} else {
    		log.info "No Algo By Name ${cardServerName}"
    		return null
    	}
    }

    /**  Add new CardServer to the service - ensure cardServerConstantName is in the Constants list
    */
    void addCardServer(String cardServerName, CardServer cardServer) {
    	cardServers.put(cardServerName, cardServer)
    }

    /**
     * Get a list of the names of all the CardServer implementations currently loaded in the system
     * @Return a List of String names of available CardServers
     */
    List cardServerNames() {
    	def csns = []
    	cardServers.each { csns << it.key }
    	return csns
    }
}
