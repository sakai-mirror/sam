/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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


package org.sakaiproject.tool.assessment.ui.bean.author;

import java.io.Serializable;
import org.sakaiproject.util.ResourceLoader;

import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
 
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.application.FacesMessage;



public class AnswerBean implements Serializable, Comparable{

  private static final long serialVersionUID = 7526471155622776147L;

  private String text;
  private Long sequence;
  private String label;
  private String feedback;
  private Boolean isCorrect;
  
  //gopalrc Jan 2010 - for EMI questions
  private String correctOptionLabels;
  //gopalrc Jul 2010 - for EMI questions
  private String requiredOptionsCount;
  
  



private Float partialCredit = Float.valueOf(0);  //to incorporate partial credit
  private static ResourceLoader rb = new ResourceLoader("org.sakaiproject.tool.assessment.bundle.AuthorMessages");

  public static final String choiceLabels = rb.getString("choice_labels"); 
  public AnswerBean() {}

  public AnswerBean(String ptext, Long pseq, String plabel, String pfdbk, Boolean pcorr, String pgrade , Float pscore) {
    this.text = ptext;
    this.sequence = pseq;
    this.label = plabel;
    this.feedback= pfdbk;
    this.isCorrect = pcorr;

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

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback= feedback;
  }

  public Boolean getIsCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(Boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  public static String[] getChoiceLabels() {
	  String[] lables = choiceLabels.split(":");
	  return lables;
  }
  
  // additional constroctor for partial credit
	public AnswerBean(String ptext, Long pseq, String plabel, String pfdbk,
			Boolean pcorr, String pgrade, Float pscore, Float pCredit) {
		this.text = ptext;
		this.sequence = pseq;
		this.label = plabel;
		this.feedback = pfdbk;
		this.isCorrect = pcorr;
		this.partialCredit = pCredit;
	}


  //gopalrc - Added Dec 22, 2009 
	public int compareTo(Object o) {
		if (o==null || getSequence()==null || ((AnswerBean)o).getSequence()==null) {
			return -1;
		}
		else {
			return getSequence().compareTo(((AnswerBean)o).getSequence());
		}
	}
	
  //gopalrc Jan 2010 - for EMI questions
  public String getCorrectOptionLabels() {
    return correctOptionLabels;
  }

  //gopalrc Jan 2010 - for EMI questions
  public void setCorrectOptionLabels(String correctOptionLabels) {
	if (correctOptionLabels != null) correctOptionLabels = correctOptionLabels.trim().toUpperCase();  
    this.correctOptionLabels = correctOptionLabels;
  }


	// --mustansar for partial credit
	public Float getPartialCredit() {
		return partialCredit;
	}

	public void setPartialCredit(Float pCredit) {
		this.partialCredit = pCredit;
	}
	
	
	//gopalrc - added for EMI - Jan 2010
	public void validateCorrectOptionLabels(FacesContext context, 
            UIComponent toValidate,
            Object value) {
		/*
		if (value == null || ((String) value).trim().equals("")) {
			((UIInput)toValidate).setValid(false);
			FacesMessage message = new FacesMessage("Please provide a set of correct option labels for sub-question: " + getSequence());
			context.addMessage(toValidate.getClientId(context), message);
			return;
		}
		*/
		String optionLabels = ((String) value).trim().toUpperCase();
		String[] optionLabelsArray = optionLabels.split(",");
		
	    ItemAuthorBean itemauthorbean = (ItemAuthorBean) ContextUtil.lookupBean("itemauthor");
	    ItemBean itemBean = itemauthorbean.getCurrentItem();
		for (int i=0; i<optionLabelsArray.length; i++) {
			String optionLabel = optionLabelsArray[i].trim();
			if (!itemBean.isValidEmiAnswerOptionLabel(optionLabel)) {
				((UIInput)toValidate).setValid(false);
				FacesMessage message = new FacesMessage("Invalid option: '" + optionLabel + "' in: '" + optionLabels + "' for sub-question: " + getSequence() + ". Please provide options from the available set of options only." );
				context.addMessage(toValidate.getClientId(context), message);
				return;
			}
		}
		
		for (int i=0; i<optionLabelsArray.length; i++) {
			String optionLabel1 = optionLabelsArray[i].trim();
			
			for (int j=0; j<optionLabelsArray.length; j++) {
				String optionLabel2 = optionLabelsArray[j].trim();
				if (i != j && optionLabel1.equals(optionLabel2)) {
					((UIInput)toValidate).setValid(false);
					FacesMessage message = new FacesMessage("Duplicate option: '" + optionLabel1 + "' in: '" + optionLabels + "' for sub-question: " + getSequence() + ". Please provide only unique options from the available set of options." );
					context.addMessage(toValidate.getClientId(context), message);
					return;
				}
			}
		}
	}


	//gopalrc - Jul 2010
	public String getRequiredOptionsCount() {
		return requiredOptionsCount;
	}
	
	public void setRequiredOptionsCount(String requiredOptionsCount) {
		this.requiredOptionsCount = requiredOptionsCount;
	}
	
}