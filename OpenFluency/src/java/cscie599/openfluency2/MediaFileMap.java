package cscie599.openfluency2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cscie99.team2.lingolearn.server.anki.AnkiCardSqlJetTransaction;
/***
 * Copied much logic from Anki-Android
 * @author phoebemiller
 *
 */
public class MediaFileMap {
	private File mediaFile;
	private File tmpDir;
	private String ankiFile;
    private Map<String, String> numToName;
    private Map<String,String> mNameToNum;
	static final int FILE_COPY_BUFFER_SIZE = 512;
	private static final Logger logger = 
			LoggerFactory.getLogger(MediaFileMap.class);

	public MediaFileMap(String ankiFile, File mediaFile, String tmpDir){
		this.ankiFile = ankiFile;
		this.mediaFile = mediaFile;
		this.tmpDir = new File(tmpDir);
        this.numToName = new HashMap<>();
        this.mNameToNum = new HashMap<>();
		// if the directory does not exist, create it
	}

	public void parse() throws IOException{
        // We need the opposite mapping, extraction method requires it.
        try {
        	JsonFactory jfactory = new JsonFactory();
        	/*** read media mapping from file ***/
        	JsonParser jParser = jfactory.createJsonParser(mediaFile);
        	while(!jParser.isClosed()){
        	    JsonToken jsonToken = jParser.nextToken();
        	    if(JsonToken.FIELD_NAME.equals(jsonToken)){
					String num = jParser.getCurrentName();
					String name = jParser.nextTextValue(); //.getText();
					mNameToNum.put(name, num);
					numToName.put(num, name);
        	    }
        	}
        	jParser.close();
        } catch (JsonGenerationException e) {
            logger.trace("Json generator error",e);
            throw new IOException("json generator failed");
        } catch (JsonMappingException e) {
            logger.trace("Json mapping error",e);
            throw new IOException("json mapper failed");
        } catch (IOException e) {
            logger.trace("Malformed media dict. Media import will be incomplete.");
            throw e;
        }   

        // Unpack all the media files into tmp dir - all this just do a an unzip of the .apkg file
        ZipFile zipfile = new ZipFile(ankiFile);
        for (Map.Entry<String, String> entry : mNameToNum.entrySet()) {
            String file = entry.getKey();
            String c = entry.getValue();
            if (file.startsWith("_") || file.startsWith("latex-")) {
                continue;
            }   
            File path = new File(tmpDir, nfcNormalized(file));
            if (!path.exists()) {
                try {
                    unzipFiles(zipfile, tmpDir.getAbsolutePath(),new String[]{c}, numToName);
                } catch (IOException e) {
                    logger.error("Failed to extract static media file. Ignoring.");
                }   
            }   
        }   
	}
	
    String getNumToName(String name) {
    	return numToName.get(name);
    }

    String getNameToNum(String num) {
		return mNameToNum.get(num);
    }

	 /*   
     *  Return the input string in the Unicode normalized form. This helps with text comparisons, for example a ü
     *  stored as u plus the dots but typed as a single character compare as the same.
     *
     * @param txt Text to be normalized
     * @return The input text in its NFC normalized form form.
    */
    public static String nfcNormalized(String txt) {
        if (!Normalizer.isNormalized(txt, Normalizer.Form.NFC)) {
            return Normalizer.normalize(txt, Normalizer.Form.NFC);
        }    
        return txt; 
    }    

	
    /***
     * unzips apkg files into target dir
     * @param zipFile
     * @param targetDirectory
     * @param zipEntries
     * @param zipEntryToFilenameMap
     * @throws IOException
     */
    public static void unzipFiles(ZipFile zipFile, String targetDirectory, String[] zipEntries,
    		Map<String, String> zipEntryToFilenameMap) throws IOException {
    	byte[] buf = new byte[FILE_COPY_BUFFER_SIZE];
    	File dir = new File(targetDirectory);
    	if (!dir.exists() && !dir.mkdirs()) {
    		throw new IOException("Failed to create target directory: " + targetDirectory);
    	}    
    	if (zipEntryToFilenameMap == null) {
    		zipEntryToFilenameMap = new HashMap<>();
    	}    
    	BufferedInputStream zis = null;
    	BufferedOutputStream bos = null;
    	try {
    		for (String requestedEntry : zipEntries) {
    			ZipEntry ze = zipFile.getEntry(requestedEntry);
    			if (ze != null) {
    				String name = ze.getName();
    				if (zipEntryToFilenameMap.containsKey(name)) {
    					name = zipEntryToFilenameMap.get(name);
    				}    
    				File destFile = new File(dir, name);
    				if (!ze.isDirectory()) {
    					zis = new BufferedInputStream(zipFile.getInputStream(ze));
    					bos = new BufferedOutputStream(new FileOutputStream(destFile), FILE_COPY_BUFFER_SIZE);
    					int n;
    					while ((n = zis.read(buf, 0, FILE_COPY_BUFFER_SIZE)) != -1) {
    						bos.write(buf, 0, n);
    					}    
    					bos.flush();
    					bos.close();
    					zis.close();
    				}    
    			}    
    		}    
    	} finally {
    		if (bos != null) {
    			bos.close();
    		}    
    		if (zis != null) {
    			zis.close();
    		}    
    	}    
    }    

    /**
     * Simply copy a file to another location
     * @param sourceFile The source file
     * @param destFile The destination file, doesn't need to exist yet.
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
