/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.tool.assessment.data.dao.assessment;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerFeedbackIfc;
import org.apache.log4j.*;
import java.io.Serializable;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

public class PublishedAnswer
    implements Serializable, AnswerIfc, Comparable<AnswerIfc>, Cloneable { //gopalrc - added cloneable 27 Nov 2009
  static Category errorLogger = Category.getInstance("errorLogger");

  private static final long serialVersionUID = 7526471155622776147L;

  private Long id;
  private ItemTextIfc itemText;
  private ItemDataIfc item;
  private String text;
  private Long sequence;
  private String label;
  private Boolean isCorrect;
  private String grade;
  private Float score;
  private Float discount;
  private Float  partialCredit; //partial credit
  private Set answerFeedbackSet;
  private HashMap answerFeedbackMap;
  private PublishedItemData publishedItemData = new PublishedItemData();
  
  //gopalrc Jan 2010 - for EMI questions
//  private String correctOptionLabels;
  
  //gopalrc - added 27 Nov 2009
  //set of possible EMI selection options indicating correct and incorrect options
//  private ArrayList emiSelectionOptions;
  

  public PublishedAnswer() {}

  public PublishedAnswer(ItemTextIfc itemText, String text, Long sequence, String label,
          Boolean isCorrect, String grade, Float score, Float partialCredit, Float discount, 
          //String correctOptionLabels,
          Set answerFeedbackSet) {
	this.itemText = itemText;
	this.item = itemText.getItem();
	this.text = text;
	this.sequence = sequence;
	this.label = label;
	this.isCorrect = isCorrect;
	this.grade = grade;
	this.score = score;
	this.discount=discount;
//	this.correctOptionLabels = correctOptionLabels;
	this.answerFeedbackSet = answerFeedbackSet;
	this.partialCredit=partialCredit;
  }
  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ItemTextIfc getItemText() {
    return itemText;
  }

  public void setItemText(ItemTextIfc itemText) {
    this.itemText = itemText;
  }

  public ItemDataIfc getItem() {
    return item;
  }

  public void setItem(ItemDataIfc item) {
    this.item = item;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getSequence() {
    return sequence;
  }

  public void setSequence(Long sequence) {
    this.sequence = sequence;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Boolean getIsCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(Boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public Float getDiscount() {
	  if (this.discount==null){
		  this.discount= Float.valueOf(0);
	  }
	  return this.discount;
  }

  public void setDiscount(Float discount) {
	  if (discount==null){
		  discount= Float.valueOf(0);
	  }
	  this.discount = discount;
  }

  public Set getAnswerFeedbackSet() {
    return answerFeedbackSet;
  }

  public ArrayList getAnswerFeedbackArray() {
    ArrayList list = new ArrayList();
    Iterator iter = answerFeedbackSet.iterator();
    while (iter.hasNext()){
      list.add(iter.next());
    }
    return list;
  }

  public void setAnswerFeedbackSet(Set answerFeedbackSet) {
    this.answerFeedbackSet = answerFeedbackSet;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
  }

  public String getAnswerFeedback(String typeId) {
    if (this.answerFeedbackMap == null)
      this.answerFeedbackMap = getAnswerFeedbackMap();
    return (String)this.answerFeedbackMap.get(typeId);
  }

  public HashMap getAnswerFeedbackMap() {
    HashMap answerFeedbackMap = new HashMap();
    if (this.answerFeedbackSet != null){
      for (Iterator i = this.answerFeedbackSet.iterator(); i.hasNext(); ) {
        Object obj = i.next();
        AnswerFeedbackIfc answerFeedback = (AnswerFeedbackIfc) obj;
        answerFeedbackMap.put(answerFeedback.getTypeId(), answerFeedback.getText());
      }
    }
    return answerFeedbackMap;
  }

  public String getCorrectAnswerFeedback() {
    return getAnswerFeedback(AnswerFeedbackIfc.CORRECT_FEEDBACK);
  }

  public String getInCorrectAnswerFeedback() {
    return getAnswerFeedback(AnswerFeedbackIfc.INCORRECT_FEEDBACK);
  }

  public String getGeneralAnswerFeedback() {
    return getAnswerFeedback(AnswerFeedbackIfc.GENERAL_FEEDBACK);
  }

  public String getTheAnswerFeedback() {
    return getAnswerFeedback(AnswerFeedbackIfc.ANSWER_FEEDBACK);
  }

  public int compareTo(AnswerIfc o) {
      return sequence.compareTo(o.getSequence());
  }

  	public boolean getGeneralAnswerFbIsNotEmpty() {
		return publishedItemData.isNotEmpty(getGeneralAnswerFeedback());
	}

	public boolean getCorrectAnswerFbIsNotEmpty() {
		return publishedItemData.isNotEmpty(getCorrectAnswerFeedback());
	}

	public boolean getIncorrectAnswerFbIsNotEmpty() {
		return publishedItemData.isNotEmpty(getInCorrectAnswerFeedback());
	}

	public boolean getTextIsNotEmpty() {
		return publishedItemData.isNotEmpty(getText());
	}
	
/*	
	 
	//gopalrc added 30 Nov 2009
	public String getEmiCorrectOptionLabelsAsString() {
		String optionString = text.substring(text.lastIndexOf("[")+1, text.lastIndexOf("]")).trim();
		if (optionString == null) {
			return "";
		}
		return optionString;
	}

	//gopalrc - added 30 Nov 2009
	public int getNumberOfCorrectOptions() {
		int count = 0;
		String emiCorrectOptionsString = getEmiCorrectOptionLabelsAsString().trim();
		if (emiCorrectOptionsString == null || emiCorrectOptionsString.equals("")) {
			count = 0;
		}
		else if (emiCorrectOptionsString.indexOf(",") == -1) {
		}
		else {
			String[] correctOptionLabels = emiCorrectOptionsString.split(",");
			count = correctOptionLabels.length;
		}
		return count;
	}
*/	
	
	//***********************
	

	//gopalrc added 16 Nov 2009
/*	
	public String getEmiCorrectOptionLabelsAsString() {
		String optionString = text.substring(text.lastIndexOf("[")+1, text.lastIndexOf("]"));
		if (optionString == null) {
			return "";
		}
		return optionString.trim().toUpperCase();
	}
*/
	
	//gopalrc added 4 Dec 2009
/*	
	public String getEmiTextWithoutCorrectOptionLabels() {
		String textWithoutLabels = text.substring(0, text.lastIndexOf("[")).trim();
		if (textWithoutLabels == null) {
			return "";
		}
		return textWithoutLabels;
	}
*/
	
	
	//gopalrc added 16 Nov 2009
/*	
	public boolean isEmiOptionCorrect(String optionLabel) {
		optionLabel = optionLabel.trim().toUpperCase();
		String correctOptionLabels = getCorrectOptionLabels();
		if (correctOptionLabels.indexOf(optionLabel) > -1) {
			return true;
		}
		else {
			return false;
		}
	}
*/	
	
	//gopalrc added 27 Nov 2009
/*	
	public ArrayList getEmiSelectionOptions() {
		return emiSelectionOptions;
	}
*/
	
	//gopalrc added 27 Nov 2009
/*	
	public void setEmiSelectionOptions(ArrayList emiSelectionOptions) {
		this.emiSelectionOptions = emiSelectionOptions;
	}
*/	
	
	//gopalrc added 27 Nov 2009
	protected PublishedAnswer clone() throws CloneNotSupportedException {
		return (PublishedAnswer)super.clone();
	}
	

	//gopalrc - added 30 Nov 2009
/*	
	public int getNumberOfCorrectEmiOptions() {
		int count = 0;
		Iterator iter = emiSelectionOptions.iterator();
		while (iter.hasNext()) {
			PublishedAnswer answer = (PublishedAnswer) iter.next();
			if (answer.getIsCorrect()) {
				count++;
			}
		}
		return count;
	}
*/		
	
	  //gopalrc Jan 2010 - for EMI questions
/*	
	  public String getCorrectOptionLabels() {
	    return correctOptionLabels;
	  }
*/
	
	  //gopalrc Jan 2010 - for EMI questions
/*	  
	  public void setCorrectOptionLabels(String correctOptionLabels) {
		if (correctOptionLabels != null) correctOptionLabels = correctOptionLabels.trim().toUpperCase();  
	    this.correctOptionLabels = correctOptionLabels;
	  }
*/
	
	
	//--mustansar for partial credit
	public Float getPartialCredit(){
		return partialCredit;
	}
	
	public void setPartialCredit(Float pCredit ){
		this.partialCredit=pCredit;
	} 
}
