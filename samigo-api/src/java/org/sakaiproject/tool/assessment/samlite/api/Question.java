package org.sakaiproject.tool.assessment.samlite.api;

import java.util.ArrayList;
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
    //gopalrc - added 3 Dec 2009 - for EMI question
    private String themeText;
    private String leadInText;
    private ArrayList emiAnswerOptions;  // store List of possible options for an EMI question's anwers
    private ArrayList emiQuestionAnswerCombinations;  // store List of possible options for an EMI question's anwers
	//gopalrc - added 16 Nov 2009
	private String availableOptions;
    
    
	
	private int questionNumber;
	private String questionPoints;
	private List questionLines;
	private int questionType;
	private String correctAnswer;
	private List answers;
	private boolean hasPoints;
	private String questionTypeAsString;
	

	
	public Question() {
		this.questionNumber = 0;
		this.questionPoints = "";
		this.questionLines = new LinkedList();
		this.questionType = UNDEFINED_QUESTION;
		this.correctAnswer = "";
		this.answers = new LinkedList();
		this.hasPoints = false;
		this.questionTypeAsString = "";
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

/*	
 * gopalrg
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
*/	
	
	
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
			int optionLine = 2;
			questionLines.set(themeLineIndex, questionLines.get(themeLineIndex).toString() + "<br /><br />");
			Iterator answerLines = answers.iterator();
			String textToAdd = "Options: ";
			questionLines.add(optionLine++, textToAdd + "<br />");
			while (answerLines.hasNext()) {
				Answer answer = (Answer) answerLines.next();
				textToAdd = answer.getId() + ". " + answer.getText();
				if (!answer.isCorrect()) {
					// add at next options position
					availableOptions += answer.getId();
					questionLines.add(optionLine++, textToAdd + "<br />");
				}
				else {
					// add at end
					//textToAdd = textToAdd.substring(0, textToAdd.indexOf("[")).trim() + "<br />";
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

	
	public String getQuestionTypeAsString() {
		return questionTypeAsString;
	}

	public void setQuestionTypeAsString(String questionTypeAsString) {
		this.questionTypeAsString = questionTypeAsString;
	}

	
//************ Theme and Lead-In Text ******************

  //gopalrc - added 3 Dec 2009
  public String getLeadInText() {
	if (leadInText == null) {
		setThemeAndLeadInText();
	}
	return leadInText;
  }


  //gopalrc - added 3 Dec 2009
  public String getThemeText() {
	if (themeText == null) {
		setThemeAndLeadInText();
	}
	return themeText;
  }

  //gopalrc - added 3 Dec 2009
  public void setThemeAndLeadInText() {
	//String text = (String)questionLines.get(1) + (String)questionLines.get(2);  
	/*
	if (getQuestionType() == EXTENDED_MATCHING_ITEMS_QUESTION &&
			text.indexOf(LEAD_IN_STATEMENT_DEMARCATOR) > -1) {
		String[] itemTextElements = text.split(LEAD_IN_STATEMENT_DEMARCATOR);
		themeText = itemTextElements[0];
		leadInText = itemTextElements[1];
	}
	*/
	themeText = (String)questionLines.get(1);
	leadInText = (String)questionLines.get(2);

  }
	  
	  	
  //************ EMI Answer Options and Q-A combinations******************
  
  
  //gopalrc - added 3 Dec 2009
  public ArrayList getEmiAnswerOptions() {
  	if (emiAnswerOptions==null) {
  		setEmiOptionsAndQACombinations();
    }
  	return emiAnswerOptions;
  }
  
  //gopalrc - added 3 Dec 2009
  public ArrayList getEmiQuestionAnswerCombinations() {
  	if (emiQuestionAnswerCombinations==null) {
  		setEmiOptionsAndQACombinations();
    }
  	return emiQuestionAnswerCombinations;
  }
 
  //gopalrc - added 3 Dec 2009
  private void setEmiOptionsAndQACombinations() {
	emiAnswerOptions = new ArrayList();
	emiQuestionAnswerCombinations = new ArrayList();
	Iterator iter = answers.iterator();
	while (iter.hasNext()) {
		Answer answer = (Answer)iter.next();
		if (answer.getId().matches("[0-9]+")) {
			emiQuestionAnswerCombinations.add(answer);
		}
		else {
			emiAnswerOptions.add(answer);
		}
	}
  }
  
  
	
}
