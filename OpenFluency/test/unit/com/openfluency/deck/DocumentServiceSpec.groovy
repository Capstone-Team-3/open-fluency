package com.openfluency.deck

import com.openfluency.language.Alphabet
import com.openfluency.language.Unit

import cscie99.team2.lingolearn.server.anki.AnkiFieldTypes;
import cscie99.team2.lingolearn.server.anki.AnkiFile
import cscie99.team2.lingolearn.shared.Deck
import cscie99.team2.lingolearn.shared.Card
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DocumentService)
class DocumentServiceSpec extends Specification {
	File file;
	AnkiFile anki;

    def setup() {
        // Test file in the same dir in test
        URL resource = getClass().getResource('Body_Parts_Pictures_to_English_and_Japanese.apkg') 
		file = new File(resource.toURI())
        String ff = file.getAbsolutePath()
		anki = new AnkiFile(ff);
    }

    def cleanup() {
    }

	// Test that the deck is correctly imported
    void "test Deck Size"() {
		System.out.println (anki.getTotalCards())
		expect:
			anki.getTotalCards() == 49
    }
	void "test Folder"() {
		expect:
			anki.getTmpDir() != null
	}
	void "test Field Mappings"() {
        expect:
            anki.fieldTypes.size() == 4
        and:
            anki.fieldNames.size() == 4
        and:
            this.anki.hashedFieldTypes["Picture"] == AnkiFieldTypes.Image
		assert(this.anki.hashedFieldTypes["Japanese"] == AnkiFieldTypes.Text);
		assert(anki.fieldNames.get(0) == "Picture");
		assert(anki.fieldNames.get(3) == "English");
		assert(anki.fieldTypes.get(0) == AnkiFieldTypes.Image);
		assert(anki.fieldTypes.get(3) == AnkiFieldTypes.Text);
		//documentService.createNewDeck(fullPath,lang);
    }

	void "test Deck"() {
		def decks = anki.getDeckIterator()
        expect:
            decks.hasNext()
        and:
            decks.next() != null
    }
	/**
	 * Check that all the cards are imported correctly with 4 fields
	 * and images are found
	 */
	void "test Cards"() {
		def decks = anki.getDeckIterator()
        Deck  deck = decks.next();
        def arrayList =  deck.getCardList() //deck.getCardSet()
        Card card = arrayList.get(0)
        expect:
            card.getImage().getImageUri()!= null
        and:
            card.fields.get(1) != null
	}

    void "test Service"() {
    }
}
