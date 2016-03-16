package com.openfluency.confuser;

import com.openfluency.language.Language;

/**
 * Factory class for instantiating instances of the ConfuserInterface
 */
public class ConfuserFactory {
	
	/**
	 * Returns true if a confuser implementation exists in the system for the specified language
	 */
	public boolean doesConfuserExist(Language language) {
		
		if (language.getCode().equals("JAP")) {
			return true;
		}
		else if (language.getCode().equals("CHN")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a ConfuserInterface implementation for the specified language if one exists, returns null otherwise.
	 * It is recommended to call doesConfuserExist to verify that a confuser exists for the specified language exists before calling this method.
	 */
	public ConfuserInterface getConfuser(Language language) {
		
		if (language.getCode().equals("JAP")) {
			return new JapaneseConfuser();
		}
		else if (language.getCode().equals("CHN")) {
			return new ChineseConfuser();
		}
		
		return null;
	}
}
