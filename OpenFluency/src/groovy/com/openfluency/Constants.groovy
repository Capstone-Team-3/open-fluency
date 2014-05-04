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

	// The different things that a User can rank a flashcard on - card elements must stay coordinated with the integer values
	public static final int MEANING = 0
	public static final int PRONUNCIATION = 1
	public static final int SYMBOL = 2
	public static final List CARD_ELEMENTS = ["Meaning", "Pronunciation", "Symbol"]

	// Answer statuses
	public static final int NOT_ANSWERED = 0
	public static final int VIEWED = 1
	public static final int ANSWERED = 2

	// Quiz statuses
	public static final int NOT_STARTED = 0
	public static final int IN_PROGRESS = 1
	public static final int COMPLETED = 2

	// Registration Status
	public static final int PENDING_APPROVAL = 0
	public static final int APPROVED = 1
	public static final int REJECTED = 2
	public static List REGISTRATION_STATUS = ["Pending Approval", "Approved", "Rejected"]

	// CSV file validation
	public static final String VALIDATION_OK = "Validation OK"
}