package org.sakaiproject.tool.assessment.samlite.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Question {
	public static final int UNDEFINED_QUESTION = 0;
	public static final int MULTIPLE_CHOICE_QUESTION = 10;
	public static final int MULTIPLE_CHOICE_MULTIPLE_ANSWER_QUESTION = 15;
	public static final int FILL_IN_THE_BLANK_QUESTION = 20;
	public static final int TRUE_FALSE_QUESTION = 30;
	public static final int SHORT_ESSAY_QUESTION = 40;
	
    //gopalrc - added 11 Nov 2009
    public static final int EXTENDED_MATCHING_ITEMS_QUESTION = 50;
	
	private int questionNumber;
	private String questionPoints;
	private List questionLines;
	private int questionType;
	private String correctAnswer;
	private List answers;
	private boolean hasPoints;
	
	//gopalrc - added 16 Nov 2009
	private String availableOptions;

	
	public Question() {
		this.questionNumber = 0;
		this.questionPoints = "";
		this.questionLines = new LinkedList();
		this.questionType = UNDEFINED_QUESTION;
		this.correctAnswer = "";
		this.answers = new LinkedList();
		this.hasPoints = false;
	}


	
	public void addAnswer(String id, String text, boolean isCorrect) {
		this.answers.add(new Answer(id, text, isCorrect));
	}

	public List getAnswers() {
		return answers;
	}


	public void setAnswers(List answers) {
		this.answers = answers;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}


	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	public String getQuestion() {
		StringBuilder buffer = new StringBuilder();
		
		for (Iterator it = questionLines.iterator();it.hasNext();) {
			String line = (String)it.next();
			if (null != line && !"".equals(line)) 	
				buffer.append(line.trim()).append(" ");
		}
		
		return buffer.toString();
	}

	public void append(String questionSegment) {
		this.questionLines.add(questionSegment);
	}
	
	public int getQuestionNumber() {
		return questionNumber;
	}


	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}


	public String getQuestionPoints() {
		return questionPoints;
	}


	public void setQuestionPoints(String questionPoints) {
		if (null != questionPoints && !"".equals(questionPoints))
			this.hasPoints = true;
		this.questionPoints = questionPoints;
	}
	
	public boolean hasPoints() {
		return hasPoints;
	}

	public String getQuestionTypeAsString() {
		switch (questionType) {
		case MULTIPLE_CHOICE_QUESTION:
			return "Multiple Choice";
		case MULTIPLE_CHOICE_MULTIPLE_ANSWER_QUESTION:
			return "Multiple Correct Choices";
		case FILL_IN_THE_BLANK_QUESTION:
			return "Fill in the Blank";
		case TRUE_FALSE_QUESTION:
			return "True/False";
		case SHORT_ESSAY_QUESTION:
			return "Short Essay";
			
		//gopalrc - added 11 Nov 2009	
		case EXTENDED_MATCHING_ITEMS_QUESTION:
			return "Extended Matching Items";
		};
		return "Unrecognized Type";
	}
	
	public int getQuestionType() {
		return questionType;
	}


	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	
	//gopalrc added 12 Nov 2009
	public void postProcessing() {
		if (getQuestionType() == EXTENDED_MATCHING_ITEMS_QUESTION) {
			int themeLineIndex = 1;
			questionLines.set(themeLineIndex, questionLines.get(themeLineIndex).toString() + "<br />");
			Iterator answerLines = answers.iterator();
			int optionLine = 2;
			while (answerLines.hasNext()) {
				Answer answer = (Answer) answerLines.next();
				String textToAdd = answer.getId() + ". " + answer.getText();
				if (!answer.isCorrect()) {
					// add at next options position
					availableOptions += answer.getId();
					questionLines.add(optionLine++, textToAdd + "<br />");
				}
				else {
					// add at end
					textToAdd = textToAdd.substring(0, textToAdd.indexOf("[")).trim() + "<br />";
					questionLines.add(textToAdd);
				}
			}
			answerLines = answers.iterator();
			while (answerLines.hasNext()) {
				Answer answer = (Answer)answerLines.next();
				if (!answer.isCorrect()) {
					answerLines.remove();
				}
				else {
					answer.postProcessing(questionType);
				}
			}
		}

		
	}
	
	
	
}
