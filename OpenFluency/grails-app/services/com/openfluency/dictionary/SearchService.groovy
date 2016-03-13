package com.openfluency.dictionary

import grails.transaction.Transactional

class OSDetails{
	String arch
	String exeExt
}

/**
 * A class to determine the OS of the machine
 *
 */
class GetOS {

	private static String OS = System.getProperty("os.name").toLowerCase();
	private static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
		
	private static boolean isMac() {	
		return (OS.indexOf("mac") >= 0);
	}
	
	private static boolean isUnix() {	
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );		
	}
	
	public static OSDetails details() {	
		if(isWindows()){
			return new OSDetails(arch: "win", exeExt : ".exe")
		}
		else if(isUnix()) {
			return new OSDetails(arch: "linux", exeExt : "")
		} 
		else if(isMac()) {
		   return new OSDetails(arch: "mac", exeExt : "")
		}			
	}
}

/**
 * A class that holds entries from a Dictionary
 *
 */
class DictionaryEntry {
	String concept
	String pronounciation
	String meaning
}

/**
 * A Japanese Dictionary Search service to determine other concepts, pronunciation
 * and meaning where a term is used.
 */
class SearchService {
     //
     ///We do not need transactional support
     static transactional = false
	 def init = false
	 String indexSearchToolPath;
	 String indexPath;
	 String commandBase
	 def    entryRegexp
	 
	 def grailsApplication
     def servletContext
	 
	 /**
	 * An init function to initiaze the class parameters
	 */
	private void init() {
		 if(!init){
			 OSDetails details = GetOS.details()
			 indexSearchToolPath = new java.io.File(
				 servletContext.getRealPath(
					 '/../resources/dictionaries/index_search_tools/' + 
					    details.arch + 
						'/csearch' + 
						details.exeExt +
						' '
				 )).getCanonicalPath()
			 indexPath = new java.io.File(
				 servletContext.getRealPath(
					 '/../resources/dictionaries/index/jp-en.index '
				 )).getCanonicalPath()
				 
			 commandBase = indexSearchToolPath + " -indexpath " +  indexPath;
			 
			 entryRegexp = /^(:?.*)?:(.*)?\[(.*)?\](?:[^\/]*)?\/([^\/]*)?\/.*/
			 init = true;
		 }
	 }
	 
	 /**
	 * A helper function that does an index search into the dictionary 
	 * @param termRegexp Regular expression to seach for
	 * @param count Number of results we want
	 * @return A vector of raw dictionary entries
	 */
	private Vector<String> getDictionaryEntriesImpl(String termRegexp, int count) {
		String command = commandBase + " -m " + count + " " + termRegexp;
		Vector<String> ret = new Vector<String>();
		
		def proc = command.execute()
		proc.waitFor()                               // Wait for the command to finish
		
		// Obtain status and output
		def exitCode = proc.exitValue()
		if(exitCode == 0) {
			proc.in.eachLine { line ->
				ret.add(line)
			}
		}
//		def outPut = proc.in.text;
		def errorPut = proc.err.text;
		if(!errorPut.empty){
			System.err.println("Unable to Search the Dictionary. Error Is:")
			System.err.println(errorPut)
		}	
        return ret
    }
	 
	/**
	 * A helper function that parses raw results from the dictionary and creates
	 * DictionaryEntry objects from them
	 * @param entries
	 * @return
	 */
	private Vector<DictionaryEntry> parseDictionaryEntries(Vector<String> entries) {
		Vector<DictionaryEntry> ret = new Vector<DictionaryEntry>()
		for(String entry : entries) {
			def matcher = (entry =~ entryRegexp)
			if(matcher.getCount()) {
				DictionaryEntry match = new DictionaryEntry(
					            concept : matcher[0][2], 
								pronounciation : matcher[0][3], 
								meaning : matcher[0][4] )
				ret.add(match)
			}
		}
		
		return ret;
	}
	 
	/**
	 * Return entries from the dictionary for the given search term
	 * @param term Concept to search for
	 * @param count Number of entries we want to return
	 * @return A vector of DictionaryEntries.
	 */
	public Vector<DictionaryEntry> getDictionaryEntries(String term, int count) {
		init();
		String termRegexp = '^.*' + term + '.*\\['
		Vector<String> entries = getDictionaryEntriesImpl(termRegexp, count)
		Vector<DictionaryEntry> ret = parseDictionaryEntries(entries)
		return ret;
	}
}
