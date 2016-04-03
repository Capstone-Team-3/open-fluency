package com.openfluency.deck

import com.openfluency.language.Alphabet;
import com.openfluency.language.Language;
import com.openfluency.language.Pronunciation;
import com.openfluency.language.Unit;
import com.openfluency.language.Unit
import com.openfluency.language.UnitMapping
import com.openfluency.language.Pronunciation
import com.openfluency.media.*
import com.openfluency.flashcard.Share
import com.openfluency.language.Language
import com.openfluency.flashcard.Deck;
import static java.nio.file.StandardCopyOption.*
import java.nio.file.Files
import java.nio.file.Path
import org.apache.tools.ant.util.FileUtils;

import grails.transaction.Transactional

import java.io.File;
import java.util.regex.Pattern
import java.net.URLEncoder

import com.openfluency.auth.User
import com.openfluency.course.Chapter
import com.openfluency.Constants
import com.openfluency.algorithm.*

import cscie99.team2.lingolearn.server.anki.AnkiFile
import cscie99.team2.lingolearn.shared.Card
import cscie599.openfluency2.*
import cscie599.openfluency2.CharSetIdentifier.Charset

@Transactional
class PreviewDeckService {

	def springSecurityService
	def flashcardService
	def algorithmService
	def flashcardInfoService
	def mediaService
    def languageService
    def deckService
	def mediaTmpDir;
	def mediaDir;
	//import org.jsoup.Jsoup;

	// This is for guessing how a deck should import
	Pattern isId = Pattern.compile("id",Pattern.CASE_INSENSITIVE)
	Pattern english = Pattern.compile("english",Pattern.CASE_INSENSITIVE)
	Pattern japanese = Pattern.compile("japanese|kanji",Pattern.CASE_INSENSITIVE)
	Pattern katakana = Pattern.compile("katakana",Pattern.CASE_INSENSITIVE)
	Pattern pinyin = Pattern.compile("pinyin",Pattern.CASE_INSENSITIVE)
	Pattern kana = Pattern.compile("hiragana|kana",Pattern.CASE_INSENSITIVE)
	Pattern romaji = Pattern.compile("romaji|romanji|roumaji",Pattern.CASE_INSENSITIVE)
	Pattern picture = Pattern.compile("picture|image",Pattern.CASE_INSENSITIVE)
	Pattern sound = Pattern.compile("sound",Pattern.CASE_INSENSITIVE)
	Pattern expression = Pattern.compile("expression|front",Pattern.CASE_INSENSITIVE)
	Pattern meaning = Pattern.compile("meaning|back",Pattern.CASE_INSENSITIVE)
	Pattern reading = Pattern.compile("reading|transcript",Pattern.CASE_INSENSITIVE)
	def langMap=["English":english,"Japanese":japanese,
		"Katakana":katakana, "Hiragana":kana, "Romaji":romaji ]
	def fieldMap=["Meaning":[english,meaning],"Literal":[expression,japanese],
		"Pronunciation":[kana,katakana,romaji,reading,pinyin]]

	// This is a desperate guessing function - Should be a test function instead
	def importDeck(PreviewDeck previewDeckInstance, String mediaTmpDir, String mediaDir) {
		this.mediaTmpDir= mediaTmpDir
		this.mediaDir= mediaDir
		PreviewCard card= PreviewCard.findByDeck(previewDeckInstance,[max:1])
		HashMap<String,Integer> fieldIndices = new HashMap<String,Integer>();
		HashMap<Integer,String> alphaIndices = new HashMap<Integer,String>();
		int nfields = card.types.size()
		for (int i=0; i < nfields; i++) {
			def field = card.fields.get(i)
			def type = card.types.get(i)
			if (isId.matcher(field).matches()) continue;
			if ("Text".equals (type)) {
				langMap.each  {
					if (it.value.matcher( field ).matches()) {
						alphaIndices.put(i,it.key)
					}
				}
				fieldMap.each  {
					for (obj in it.value) {
						if (obj.matcher( field ).matches()) {
							fieldIndices.put(it.key,i)
						}
					}
				}
			} else if ("Sound".equals(type)) fieldIndices.put("Sound",i)
			else if ("Image".equals(type)) fieldIndices.put("Image",i)
		}
		if ("Kana" in fieldMap.keySet()){ // preferred
			fieldIndices.put("Pronunciation", fieldIndices.get("Kana"))
			alphaIndices.put(fieldIndices.get("Kana"), "Hiragana")
		} else if ("Katakana" in fieldMap.keySet()){
			fieldIndices.put("Pronunciation", fieldIndices.get("Katakana"))
			alphaIndices.put(fieldIndices.get("Kana"), "Katakana")
		}
		if (!fieldIndices.containsKey("Literal") && "Pronunciation" in fieldIndices.keySet())
			fieldIndices.put("Literal",fieldIndices.get("Pronunciation"))
		if (!fieldIndices.containsKey("Literal") || !fieldIndices.containsKey("Meaning")){
			println("There are no front and back assignments (such as expression,meaning,etc) for the cards")
			return null
		}
		return createOpenFluencyDeck(Language.findByName("English"), previewDeckInstance,
				fieldIndices, alphaIndices, algorithmService.cardServerNames()[0]) //getDefault())
	}


	// This should copy the file to a new directory and return the URL to the new path
	String remapMedia(String media) {
        if (media == null) return null
        try {
            String prefix = "web-app" + File.separator
            MediaFileMap.remapMedia(media, prefix + this.mediaTmpDir, prefix + this.mediaDir )
            return "OpenFluency/" + this.mediaDir + "/" + media.replaceAll("\\\\","/") // if windows
        } catch (Exception e) {
            println("remap media "+ media + " "+ e )
        }
        return null
	}

	// create open fluency deck from PreviewDeck with
	@Transactional
	def createOpenFluencyDeck(Language sourceLanguage, PreviewDeck previewDeckInstance,
		HashMap<String,Integer> fieldIndices, HashMap<String,Integer> alphaIndices, String cardServerName){
		Deck deckInstance = deckService.createDeck(previewDeckInstance.name,
			previewDeckInstance.description, previewDeckInstance.language.id.toString(),sourceLanguage.id.toString(),cardServerName);
			
		def previewCardInstances= PreviewCard.findAllByDeck(previewDeckInstance)
		Language lang = previewDeckInstance.language
		Language lang2 = sourceLanguage
		for ( card in previewCardInstances) {
			String symbolString = card.units.get( fieldIndices.get("Literal"));
			String meaningString = card.units.get( fieldIndices.get("Meaning"));
			String pronunciationString = null
			String audioInstanceId=null
			String imageURL = null
			String audioURL = null
			String alphabet1 = null
			String alphabet2 = null
			Alphabet alphabetp = null

			if (symbolString == null || symbolString.length() < 1 || meaningString == null || meaningString.length() < 1)
				continue // Skip if missing data
			symbolString = removeHtml(symbolString)
			meaningString = removeHtml(meaningString)
			if (symbolString.length() < 1 || meaningString.length() < 1)
				continue // Skip if data is blank
            try {
                imageURL = remapMedia(card.units.get( fieldIndices.get("Image")))
            } catch (Exception e){}
            try {
                audioURL = remapMedia(card.units.get( fieldIndices.get("Sound")))
            } catch (Exception e){}
			try {
                  String alpha = getCharSet(symbolString)
                  alphabet1 = alphaIndices.get(fieldIndices.get("Literal"));
                  if (!"Unknown".equals(alpha)) alphabet1= alpha;
                  println("Literal "+ alpha1)
            } catch (Exception e){}
			try { alphabet2 = alphaIndices.get(fieldIndices.get("Meaning"));
            } catch (Exception e){}
			try { pronunciationString = card.units.get( fieldIndices.get("Pronunciation"));
            } catch (Exception e){}
                // if pronunication is missing, perhaps the literal string can be used
            if ((pronunciationString == null || pronunciationString.length() < 1 ) 
                && ("Hiragana".equals(alphabet1) || "Katakana".equals(alphabet1))) {
                pronunciationString = symbolString
            }

			// Objects to build flashcard
            // TODO:  Literal can be hiragana instead of Kanji
			Unit symbol = languageService.getUnit(symbolString, lang )
			Unit meaning = languageService.getUnit(meaningString, deckInstance.sourceLanguage)

			pronunciationString = removeHtml(pronunciationString)
			if (pronunciationString != null && !pronunciationString.isEmpty()) {
				Pronunciation pronunciation
                String alpha = getCharSet(pronunciationString)
                // Parsed charset takes precedence
                def al = ("Unknown".equals(alpha))? alphaIndices.get(fieldIndices.get("Pronunciation")) : alpha;
                alphabetp = Alphabet.findByName(al)
                if (alphabetp == null)
                    alphabetp = Alphabet.findByLanguage(lang)
                //println("Pronunciation al " + al + " = " + " alpha " + alphabetp)
				try {
					if (alphabetp != null)
						pronunciation =  languageService.getPronunciationAlphabet(pronunciationString, symbol, alphabetp)
					else
						pronunciation = languageService.getPronunciation(pronunciationString, symbol, deckInstance.language)
                    println("Pronunciation "+ pronunciation + alphabetp)
					pronunciationString = pronunciation.id.toString()
					if (audioURL != null) {
						def audioInstance = mediaService.createAudio(audioURL,null, pronunciation.id.toString())
						audioInstanceId= audioInstance.id.toString()
					}
				} catch (Exception e) {
					println("Problem with audio, skip the audio "+e)
					pronunciationString = null
				}
			} else {
				pronunciationString = null
			}
			UnitMapping unitMapping = languageService.getUnitMapping(symbol, meaning)
			flashcardService.createFlashcard(symbol.id.toString(), unitMapping.id.toString(), pronunciationString, imageURL, audioInstanceId, deckInstance.id.toString())
		}
        println("deckInstance ="+ deckInstance)
		return deckInstance
	}
	
	@Transactional
	def createPreviewDeck(String fullPath, String mediaDir, String filename, String description, Language language, Document document){
		AnkiFile anki = new AnkiFile(fullPath,mediaDir)
		def nCards = anki.totalCards
		def folder = anki.getTmpDir()
		def decks = anki.getDeckIterator()
		def cardfields = anki.getModels().values()
		// A place to hold all the cards
		def user= User.load(springSecurityService.principal.id)
		user.id = springSecurityService.principal.id
		//def owner = springSecurityService.currentUser
		PreviewDeck previewDeckInstance = new PreviewDeck(owner: user, filename: filename, name: filename, description:description,language:language,document: document,mediaDir: mediaDir);
		previewDeckInstance.save(flush:true)
		def nfields = anki.fieldNames.size()
		while (decks.hasNext()) {
			def deck = decks.next();
			ArrayList cardList =  deck.getCardList()
			// Save each card
			def ncards = cardList.size();
			for (int i=0;i<ncards;i++) {
				Card card = cardList.get(i)
				def fieldTypes = anki.fieldTypes;
				def fieldNames = anki.fieldNames;
				PreviewCard pc=new PreviewCard(deck: previewDeckInstance)
				for (String cardfield in card.fields) {
					pc.addToUnits(cardfield.take(255)) }
				for (String fieldtype in fieldTypes) {
					 pc.addToTypes(fieldtype) }
				for (String fieldname in fieldNames) {
					 pc.addToFields(fieldname) }
				pc.save flush:true
			}
		}
		return previewDeckInstance;
	}

    def getCharSet(str) {
        CharSetIdentifier cs=new CharSetIdentifier();
        cs.addText(str)
        String result = cs.getCharSet()
        if (result != "Mixed")  // Can't deal with mixed
            return result.toString()
        return "Unknown"
    }   

	/**
	 * Creates an arraylist of all the fields imported
	 * @param fieldList
	 * @param card - contains all the fields in the card
	 * @param alphabet
	 * @return
	 */
	def createPreviewUnits(fieldList,card){
		ArrayList<Unit> units=new ArrayList<Unit>()
		int i=0;
		for (field in card.fields) {
			def unit = new Unit(alphabet: alphabetList.get(i), literal: field);
			units.add(unit);
			i++;
		}
		return units;
	}
	/**
	* Retrieves the next flashcard that the user should preview 
	* @param cardUsage: the usage for the flashcard being ranked
	* @param ranking that the user gave to this flashcard
	* @return the new usage for the next flashcard
	*/
	PreviewCard getNextFlashcard(PreviewDeck deckInstance, int cardId,int ccount) {
		def cards =
		PreviewCard.findAll("from PreviewCard as pc where " +
					 "deck = :deck " +
					 "order by pc.id asc",
					 [deck: deckInstance, max: ccount, offset: cardId])
		return cards;	
	}

	/**
    * Create a new deck owned by the currently logged in user
    */
    PreviewDeck createDeck(String title, String description, String languageId) {
    	User theUser = User.load(springSecurityService.principal.id)
    	PreviewDeck deck = new PreviewDeck(title: title, 
    		description: description, 
    		owner: theUser, 
    		language: Language.load(languageId))
    	deck.save()
    	return deck
    }

    /** 
    * Update an existing deck
    */
    void updateDeck(PreviewDeck deckInstance, String name, String description, String languageId, String cardServerName) {
    	deckInstance.name = name
    	deckInstance.description = description
    	deckInstance.language = Language.load(languageId)
    	deckInstance.save()
    }   	

    Boolean removeDeck(PreviewDeck deck) {
    	User theUser = User.load(springSecurityService.principal.id)
		
    }

    /**
    * Search for Decks
    */
    List<PreviewDeck> searchDecks(Long languageId, String keyword) {
    	log.info "Searching Decks with languageId: $languageId and Keywords: $keyword"

    	Deck.withCriteria {

            // Apply language criteria
            if(languageId) {
            	language {
            		eq('id', languageId)
            	}
            }

            // Search using keywords in the title or description
            if(keyword) {
            	or {
            		ilike("name", "%${keyword}%")
            		ilike("description", "%${keyword}%")
            	}
            }
        }
    }

    /**
    * Delete deck
    */
    void deleteDeck(PreviewDeck deckInstance) {
    	// First delete all flashcards
    	PreviewCard.findAllByDeck(deckInstance).each {
    		it.delete(it)
    	}
		// Delete all associated media
		try {
			File mediaDir= new File(deckInstance.mediaDir)
			FileUtils.delete(mediaDir)
		} catch (IOException) {
			println("Cannot delete media")
		}
    	// Now delete it
    	deckInstance.delete()
    }

    
    PreviewCard getRandomFlashcard(PreviewCard flashcardInstance) {
    	PreviewCard.executeQuery('FROM PreviewCard WHERE deck = ? AND id <> ? ORDER BY rand()', [flashcardInstance.deck, flashcardInstance.id], [max: 1])[0]
    }

    /**
    * Get a random flashcard from a deck where the given flashcard lives but is not any of the given flashcards
    */
    PreviewCard getRandomFlashcard(PreviewCard flashcardInstance, def flashcardIds) {
        def query = "FROM PreviewCard WHERE deck.id = " + flashcardInstance.deck.id + " AND id NOT IN (" + flashcardIds.join(",") + ") ORDER BY rand()"
        PreviewCard.executeQuery(query, [max: 1])[0]
    }

	String removeHtml(String str) {
        if (str == null)   return null
		return str.replaceAll("\\<.*?>","");
	}
}
