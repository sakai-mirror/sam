package org.sakaiproject.tool.assessment.samlite.api;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class Answer {
	private String id;
	private String text;
	private boolean isCorrect;
	
	
	
	public Answer(String id, String text, boolean isCorrect) {
		this.id = id;
		this.text = text;
		this.isCorrect = isCorrect;
		
	}
	
	public String getId() {
		return id;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public String getText() {
		return text;
	}
	
	//gopalrc added 16 Nov 2009
	public void postProcessing(int questionType) {
		if (questionType == Question.EXTENDED_MATCHING_ITEMS_QUESTION) {
			text = text.substring(text.lastIndexOf("[")+1, text.lastIndexOf("]")).trim();
		}
	}
	
	//gopalrc added 3 Dec 2009
	/*
	public String getCorrectEmiOptions() {
		return text.substring(text.lastIndexOf("[")+1, text.lastIndexOf("]")).trim();
	}
	*/


	
}
