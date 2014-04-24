package com.openfluency.algorithm

import grails.transaction.Transactional
import java.util.*

class AlgorithmService {

	static Map<String,CardServer> cardServers = new HashMap<String,CardServer>()

    CardServer cardServerByName(String cardServerConstantName) {
    	if (cardServers.containsKey(cardServerConstantName)) {
    		return cardServers.get(cardServerConstantName)
    	} else {
    		log.info "No Algo By Name ${cardServerConstantName}"
    		return null
    	}
    }

    /**  Add new CardServer to the service - ensure cardServerConstantName is in the Constants list
    */
    void addCardServer(String cardServerConstantName, CardServer cardServer) {
    	cardServers.put(cardServerConstantName, cardServer)
    }


    List cardServerNames() {
    	def csns = []
    	cardServers.each { csns << it.key }
    	return csns
    }
}
