package com.openfluency.confuser;

import com.openfluency.language.Language;

public class ConfuserFactory {
	
	public boolean doesConfuserExist(Language language) {
		
		if (language.getCode().equals("JAP")) {
			return true;
		}
		
		return false;
	}
	
	public ConfuserInterface getConfuser(Language language) {
		
		if (language.getCode().equals("JAP")) {
			return new JapaneseConfuser();
		}
		
		return null;
	}
}
