package com.openfluency.course

import com.openfluency.Constants
import com.openfluency.confuser.*;
import com.openfluency.flashcard.Flashcard
import com.openfluency.language.Language

/**
 *  The Question domain class represents a single question on a quiz
 */
class Question {
	/** the Quiz the question belongs to */
	Quiz quiz
	
	/** This indicates if in this test we're testing pronunciation, meaning, or symbol */
	Integer questionType
	
	String question;
	
	QuestionOption getCorrectOption() {
		def query = QuestionOption.where {
		    question == this && answerKey == 1
		}

		return query.find()
	}
	
	List<QuestionOption> getWrongOptions() {
		return QuestionOption.findAll {
			question == this && answerKey == 0
		}
	}

	/** Get all the other options that the user can choose from in addition to the correct answer
	 *  @Return a list of the QuestionOption available
	 */
	List<QuestionOption> getOptions() {
		return QuestionOption.findAllByQuestion(this)
	}

	/** @Return a randomly ordered list of flashcards that the user will have to choose from */
	List<QuestionOption> getSelections() {
		List<QuestionOption> selections = getOptions()
		Collections.shuffle(selections, new Random())
		return selections
	}
	
	/**
	 * Returns the test element to use during the test. If the test element is random, then a random test element is returned
	 */
	 Integer getEffectiveQuestionType() {
		 if(questionType == Constants.RANDOM) {
			 Random rand = new Random()
			 int max = Constants.CARD_ELEMENTS.size()
			 return rand.nextInt(max-1)
		 }
		 else {
			 return questionType
		 }
	 }
}
