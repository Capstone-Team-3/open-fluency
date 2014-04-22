package com.openfluency

public class Constants {
	public static final String ROLE_ADMIN = "ROLE_ADMIN"
	public static final String ROLE_STUDENT = "ROLE_STUDENT"
	public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR"
	public static final String ROLE_RESEARCHER = "ROLE_RESEARCHER"

	public static final int EASY = 3
	public static final int MEDIUM = 2
	public static final int HARD = 1
	public static final int UNRANKED = 0
	public static final List DIFFICULTIES = ["Unranked", "Hard", "Medium", "Easy"]

	// The different things that a User can rank a flashcard on
	public static final int MEANING = 0
	public static final int PRONUNCIATION = 1
	public static final int SYMBOL = 2
	public static final List CARD_ELEMENTS = ["Meaning", "Pronunciation", "Symbol"]
}