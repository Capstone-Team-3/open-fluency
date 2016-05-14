package com.openfluency.dictionary

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DictionaryController)
class DictionaryControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test search"() {
        setup:
           javax.servlet.ServletContext servletContextMock = Mock();
           servletContextMock.getRealPath(_) >> new File("web-app").getAbsolutePath();
           controller.searchService = new com.openfluency.dictionary.SearchService();
           controller.searchService.servletContext = servletContextMock;

        when:
        params.term = "\u4EEE\u60F3\u4E16\u754C";
        params.count = 10;
        def model = controller.search()

        then:
        Vector<DictionaryEntry> results = model.dictionarySearchResults;
        results.size() == 1;
    }
}
