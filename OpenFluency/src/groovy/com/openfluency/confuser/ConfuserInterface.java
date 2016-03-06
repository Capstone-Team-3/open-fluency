package com.openfluency.confuser;

import java.util.List;

import com.openfluency.language.Alphabet;

import cscie99.team2.lingolearn.shared.error.ConfuserException;

public interface ConfuserInterface {
	
	public List<String> getConfusers(String word, Alphabet alphabet, int count) throws ConfuserException;
	
}
