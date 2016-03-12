package cscie99.team2.lingolearn.server.anki;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import cscie99.team2.lingolearn.server.anki.error.AnkiException;
import cscie99.team2.lingolearn.server.anki.models.CardField;
import cscie99.team2.lingolearn.server.anki.models.CardModel;
import cscie99.team2.lingolearn.shared.Card;
import cscie99.team2.lingolearn.shared.Deck;
import cscie99.team2.lingolearn.shared.Image;
import cscie99.team2.lingolearn.shared.Sound;
import cscie599.openfluency2.*;

// Phoebe: Borrowed heavily from ankiAndroid to deal with media
// Handle multi media - see https://github.com/ankidroid/Anki-Android/blob/develop/AnkiDroid/src/main/java/com/ichi2/libanki/Media.java
public class AnkiFile {

	private File sqliteFile;
	private FileInputStream zipFileStream;
	private SqlJetDb db;
	//private static String TmpFolder = "TMP";
	private static String TmpFolder = System.getProperty("java.io.tmpdir");
	private String tmpFolder;
	private String ankiFilename = "";
	private HashMap<String, CardModel> models;
	// Field names and field types for all the cards in this deck
	private LinkedHashMap<String,AnkiFieldTypes> fieldTypes;
	private Boolean hasField=false;
	private int totalCards = 0;
	private int totalDeckModels = 0;
	private long cardId = 1;
	private HashMap<String, Deck> parsedDecks;
	private AnkiModelSqlJetTransaction modelTransaction;
	private AnkiCardSqlJetTransaction cardTransaction;
	private File mediaFile; // This holds all the image/sound files
	private static final Logger logger = 
			LoggerFactory.getLogger(AnkiCardSqlJetTransaction.class);
	private static final int FILE_COPY_BUFFER_SIZE = 1024;	

	public AnkiFile(String fileName) throws AnkiException, IOException  {
		this(fileName, TmpFolder);
	}

	public AnkiFile(String fileName, String tmpDir) throws AnkiException, IOException
	{
		File folder = File.createTempFile(fileName, null); // use for unpacking anki files
		folder.deleteOnExit();	// cleans up at exit
		folder.delete();		// if reuse a folder name - clean out folder first
		folder.mkdir();
		this.tmpFolder = folder.getAbsolutePath();
		try{
			this.modelTransaction = new AnkiModelSqlJetTransaction(this);
			this.cardTransaction = new AnkiCardSqlJetTransaction(this);
			this.zipFileStream = new FileInputStream(fileName);
			this.parsedDecks = new HashMap<String, Deck>();
			this.ankiFilename = fileName;
		    this.fieldTypes = new LinkedHashMap<String,AnkiFieldTypes>();	
            this.cardId = 1;  // All card id starts at 1 for each deck at import
			this.importData();
		}catch( SqlJetException sje ){
			throw new AnkiException(
					"An error occured parsing the Anki SqlLite databse", sje);
		}catch(FileNotFoundException fnf ){
			throw new AnkiException(
					"The specified Anki file was not found: " + fileName, fnf);
		}catch( IOException ioe ){
			throw new AnkiException(
					"An error occured parsing the Anki file.", ioe);
		}
	}
	
	/*
	 * Helper method to import the Card and model data contained
	 * in the AnkiDeck
	 */
	private void importData() throws SqlJetException, IOException {
		
		ZipInputStream zis = new ZipInputStream(zipFileStream);
    	
	    ZipEntry ze = zis.getNextEntry();	 
	    while (ze!=null) {
	    	if (ze.getName().contains(".anki2")) {
	    		this.sqliteFile = new File(this.tmpFolder + File.separator + ze.getName());
	    
	    		//Create parent folder
	    		new File(this.sqliteFile.getParent()).mkdirs();
	    		 
	            FileOutputStream fos = new FileOutputStream(this.sqliteFile);   
	            byte[] buffer = new byte[FILE_COPY_BUFFER_SIZE];
	 
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	fos.write(buffer, 0, len);
	            }
	            fos.close();   
	    	}
	    	else if (ze.getName().equals("media")) {
	    		this.mediaFile = new File(this.tmpFolder + File.separator + ze.getName());
	    		//Create parent folder, ok if already exists
	    		new File(this.mediaFile.getParent()).mkdirs();
	    		 
	            FileOutputStream fos = new FileOutputStream(this.mediaFile);   
	            byte[] buffer = new byte[FILE_COPY_BUFFER_SIZE];
	 
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	fos.write(buffer, 0, len);
	            }
	            fos.close();   
	    	}
            ze = zis.getNextEntry();
	    }
	    zis.closeEntry();
    	zis.close();
	    
    	MediaFileMap mediaMapper = new MediaFileMap(
    			this.ankiFilename,
				this.mediaFile,		// "media" file
				this.tmpFolder		// All the files will be unpacked here
			);
    	mediaMapper.parse(); // This will unpack all the media into mediaFolder
		SqlJetDb db = SqlJetDb.open(this.sqliteFile, false);
        
		db.runReadTransaction( this.modelTransaction );
		db.runReadTransaction( this.cardTransaction );
		db.close();
	}
	
    public String getTmpFolder(){
        return (this.tmpFolder);
    }

	/**
	 * Add a Card to the collection of parsed cards and decks
	 * @param deckName the name of the deck as parsed from the anki file.  If 
	 * no "name" exists this will be the model id.
	 * @param card the card object as parsed from the anki file.
	 */
	public void addCard( String deckName, Card card ){
		Deck deck = parsedDecks.get(deckName);
		if( deck == null ){
			deck = new Deck();
			totalDeckModels++;
			deck.setName( getNextDeckName() );
		}
        card.setId(this.cardId);
        this.cardId++;
		deck.add(card);
		
		parsedDecks.put( deckName, deck );
		totalCards++;
	
	}
	
	/**
	 * Parse a Card object based on the fields and model read from the 
	 * Anki file.
	 * @param cardFields an array of fields read in from the Anki file that represent
	 * an individual card.
	 * @param modelId the model id for this card as read in from the anki file.
	 * @return a Card object parsed from the specified fields and model.
	 */
	public Card buildCard( String cardFields[], String modelId ){
		
		CardModel model = models.get(modelId);
		//TODO more robust error handling here
		if( model == null )
			return null;
		
		Card card = new Card();
		String cardFieldValue;
		CardField modelFields[] = model.getFlds();
		int allFields=modelFields.length;
		for( int i = 0; i < modelFields.length; i++ ){
			AnkiFieldTypes fieldType;
			CardField modelField;
			try { // Array out of bound in some of them, how?
				modelField = modelFields[i];
				String p[] = modelField.getMedia();
			} catch (Exception e) {
				modelField = null;
			}
			try {
				cardFieldValue = cardFields[i];
			} catch (Exception e) {
				cardFieldValue = "";
				logger.error("Card field "+ i +" is missing "+ ((modelField != null)?modelField.getName():"unknown"), e);
			}
			// If mapped, use it
			if (!hasField) {
				if (this.fieldTypes.containsKey(modelField.getName())
				&& this.fieldTypes.get(modelField.getName())!= AnkiFieldTypes.Unknown ) {
					fieldType=this.fieldTypes.get(modelField.getName());
					--allFields;
				} else {  // See if it is image, text or otherwise
					fieldType = getModelFieldMap(cardFieldValue);
					this.fieldTypes.put(modelField.getName(), fieldType);
				}
			}
			else
				if (modelField != null)
					fieldType=this.fieldTypes.get(modelField.getName());
				else // out of bound for 1 large file
					fieldType = getModelFieldMap(cardFieldValue);
			card.addField(cardFieldValue);
            // Most cards don't have front back, keep it here for giggles
			switch( fieldType ){
			// Front and Back are 
				case Front:
					// Determine if value is Hiragana or other alphabet
					card.setHiragana( cardFieldValue );
					break;
				
				case Back:
					String cardBack = cardFieldValue;
					// Use RegEx to remove chars between [ ].  Some anki files
					// this notation to indicate embedded media seperate from
					// the translation.
					cardBack = cardBack.replaceAll("(.*)(\\[.*\\])", "$1");
					card.setTranslation( cardBack );
					break;
				
				case Image:
					//TODO: setImage Handler!
					String image = Media.isImage(cardFields[i]);
					Image im = new Image();
					im.setImageUri(image);
					card.setImage(im);
					break;
					
				case Sound:
					//TODO: set Sound Handler!
					String audio = Media.isSound(cardFields[i]);
					Sound sound = new Sound();
					sound.setSoundUri(audio);
					card.setSound(sound);
					break;
				default:
					continue;
			}
		}
		if (allFields == 0)	// All fields are mapped
			this.hasField = true;
		return card;
	}
	
	/*
	 * Generate a name for the next parsed deck.
	 * These names are based on the original Anki filename.
	 */
	private String getNextDeckName(){
		File ankiFile = new File(this.ankiFilename);
		String filename = ankiFile.getName();
		String fileTokens[] = filename.split("\\.(?=[^\\.]+$)");
		if( fileTokens.length < 1 )
			return filename;
		
		String basename = fileTokens[0];
		
		String deckName = basename;
		if( totalDeckModels > 1 )
			deckName += totalDeckModels;
		
		return deckName;
	}
	
	/*
	 * Return the AnkiFieldTypes enum represented in the CardField
	 * passed.
	 */
	private AnkiFieldTypes getModelFieldType( CardField field ){
		AnkiFieldTypes types[] = AnkiFieldTypes.values();
		String fieldName = field.getName();
		for( int i = 0; i < types.length; i++ ){
			AnkiFieldTypes type = types[i];
			if( type.toString().equalsIgnoreCase(fieldName))
				return type;
		}
		logger.info("Unknown Card Type ", field);
		return null;
	}
	
	/***
	  Phoebe: Parses the content file to get content type,
	  what if the first card is bad - need to parse cards until
	  all the fields are mapped to image,sound or text
	  What about markups?
	*/
	private AnkiFieldTypes getModelFieldMap( String cardFieldValue ){
		//AnkiFieldTypes types[] = AnkiFieldTypes.values();
		String path = Media.isSound(cardFieldValue);
		if (path!=null) return AnkiFieldTypes.Sound;
		path = Media.isImage(cardFieldValue);
		if (path!=null) return AnkiFieldTypes.Image;
		if (cardFieldValue.length() > 1) return AnkiFieldTypes.Text;
		else return AnkiFieldTypes.Unknown;
	}	
	/**
	 * Return a Map of the Card Model structures that are represented
	 * in the Anki file.  This is performed by parsing the JSON that
	 * is contained in the models column of the cols table in the
	 * SqlLite databse.
	 * @param modelMap the value of the models column in the cols table of the AnkiFile's
	 * SqlLite database.
	 * @return a Map collection that keys the String modelId to the Model datastructure
	 * itself.
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static HashMap<String, CardModel> getModels(String modelMap)
	throws JsonMappingException, JsonParseException, IOException
	{
		
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, CardModel> models = mapper.readValue( 
				modelMap, new TypeReference<HashMap<String, CardModel>>(){});
				
		Set<String> keys = models.keySet();
		for( String mid : keys ){
			CardModel model = models.get(mid);
			System.out.println(model.getId());
		}
		
		return models;
	}
	
	public HashMap<String, Deck> getParsedDecks() {
		return parsedDecks;
	}

	public Iterator getDeckIterator() {
        if (parsedDecks != null) {
            Collection c = parsedDecks.values();
            System.out.println("parsedDecks "+ parsedDecks );
            return c.iterator();
        }
        return null;
	}

	public HashMap<String, CardModel> getModels() {
		return models;
	}

	public void setModels(HashMap<String, CardModel> models) {
		this.models = models;
	}

	public int getTotalCards() {
		return totalCards;
	}

	public void setTotalCards(int totalCards) {
		this.totalCards = totalCards;
	}

	public int getTotalDeckModels() {
		return totalDeckModels;
	}

	public void setTotalDeckModels(int totalDeckModels) {
		this.totalDeckModels = totalDeckModels;
	}
	
	
}
