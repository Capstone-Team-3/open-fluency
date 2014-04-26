package com.openfluency.algorithm

import grails.transaction.Transactional
import java.util.*

class AlgorithmService {

    def springSecurityService
    def deckService

	//a static map to hold all instantiated card servers
    static Map<String,CardServer> cardServers = new HashMap<String,CardServer>()

    /**  Look up a CardServer by name - null if it isn't found
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


    List cardServerNames() {
    	def csns = []
    	cardServers.each { csns << it.key }
    	return csns
    }
}
