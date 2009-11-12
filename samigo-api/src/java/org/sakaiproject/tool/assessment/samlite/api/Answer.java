package org.sakaiproject.tool.assessment.samlite.api;

import java.util.regex.Pattern;

public class Answer {
	private String id;
	private String text;
	private boolean isCorrect;
	
	//gopalrc added 12 Nov 2009
	private Pattern extendedMatchingCorrectAnswersPattern 
		= Pattern.compile("^(\\d+\\.+ ).*\\[[a-z[ ,]]*\\].*", Pattern.CASE_INSENSITIVE);
	private boolean isEMI;
	
	
	public Answer(String id, String text, boolean isCorrect) {
		this.id = id;
		this.text = text;
		this.isCorrect = isCorrect;
		
		//gopalrc - added 12 November 2009
		if (extendedMatchingCorrectAnswersPattern.matcher(id + ". " + text).find()) {
			this.isEMI = true;
		}
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
	
	public String getEmiQuestionPart() {
		if (isEMI) {
			return text.substring(0, text.indexOf("[")).trim();
		}
		else {
			return null;
		}
	}
	
	public String getEmiAnswerPart() {
		if (isEMI) {
			return text.substring(text.indexOf("[")+1, text.indexOf("]")).trim();
		}
		else {
			return null;
		}
	}
	
}
