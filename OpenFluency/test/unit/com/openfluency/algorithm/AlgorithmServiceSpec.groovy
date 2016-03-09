package com.openfluency.algorithm

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AlgorithmService)
class AlgorithmServiceSpec extends Specification {

	def algorithmService

	/*
	void "addCardServer(cardServerName, cardServer) should add a cardServer to the algorithmService "(){
    	given: "a cardServer instance"
    		CardServer linearWithShuffle = new LinearWithShuffle()
    	when: "we add the cardServer"
    		algorithmService.addCardServer(linearWithShuffle.name, linearWithShuffle)
    	then: "the algorithmService.cardServers should not be empty"
    		algorithmService.cardServers.size() > 0
    }
	*/
	
	/*
    void "cardServerByName() should return the Linear-With-Shuffle server or null for other requests"() {
    	given: "we define a card server and add it to the algorithmService"
    		CardServer linearWithShuffle = new LinearWithShuffle()
        	algorithmService.addCardServer(linearWithShuffle.name, linearWithShuffle)
        and: "we attempt to get the added card server by name, and get a server using a non registered name"
        	def linearAlgo = algorithmService.cardServerByName("Linear-With-Shuffle")
        	def noAlgo = algorithmService.cardServerByName("nothing")
        expect: "that we get back the Linear-With-Shuffle cardServer as expected, and a null instance where the name wasn't registered"
        	linearAlgo.name == "Linear-With-Shuffle"
        	noAlgo == null
    }
    */
}
