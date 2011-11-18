/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
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
import org.sakaiproject.tool.assessment.data.dao.shared.TypeD;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

public class PublishedItemText
    implements Serializable, ItemTextIfc, Comparable<ItemTextIfc> {
  static Category errorLogger = Category.getInstance("errorLogger");

  private static final long serialVersionUID = 7526471155622776147L;

  private Long id;
  private ItemDataIfc item;
  private Long sequence;
  private String text;
  private Set answerSet;

  
  
  //gopalrc - added Aug 2010
  private Set itemTextAttachmentSet;
  private Integer requiredOptionsCount;

  
  public PublishedItemText() {}

  public PublishedItemText(PublishedItemData item, Long sequence, String text, Set answerSet) {
    this.item = item;
    this.sequence = sequence;
    this.text = text;
    this.answerSet = answerSet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ItemDataIfc getItem() {
    return item;
  }

  public void setItem(ItemDataIfc item) {
    this.item = item;
  }

  public Long getSequence() {
    return sequence;
  }

  public void setSequence(Long sequence) {
    this.sequence = sequence;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set getAnswerSet() {
    return answerSet;
  }

  public void setAnswerSet(Set answerSet) {
    this.answerSet = answerSet;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
  }

  public ArrayList getAnswerArray() {
    ArrayList list = new ArrayList();
    Iterator iter = answerSet.iterator();
    while (iter.hasNext()){
      list.add(iter.next());
    }
    return list;
  }

  public ArrayList getAnswerArraySorted() {
    ArrayList list = getAnswerArray();
    Collections.sort(list);
    return list;
  }

  public int compareTo(ItemTextIfc o) {
      return sequence.compareTo(o.getSequence());
  }
  
  
  
  //gopalrc - added 26 Nov 2009
  //TODO - For elegance this should probably be moved up to [Published]ItemData
  // as it applies only to the first (seq=0) ItemText
/*  
  public ArrayList getEmiAnswerOptions() {
	  if (emiAnswerOptions != null) {
		  return emiAnswerOptions;
	  }
	  else { // use lazy initialization
	    ArrayList list = getAnswerArray();
	    emiAnswerOptions = new ArrayList();
	    if (list == null) {
	    	return emiAnswerOptions;
	    }
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
	    	PublishedAnswer answer = (PublishedAnswer) iter.next();
	    	if (answer.getLabel() != null && answer.getLabel().matches("[A-Za-z]")) {
	    		emiAnswerOptions.add(answer);
	    	}
	    }
	    Collections.sort(emiAnswerOptions);
	    return emiAnswerOptions;
	  }
  }
*/  
  
  //gopalrc - added 26 Nov 2009
  //TODO - For elegance this should probably be moved up to [Published]ItemData
  // as it applies only to the first (seq=0) ItemText
/*  
  public ArrayList getEmiQuestionAnswerCombinations() {
	  if (emiQuestionAnswerCombinations != null) {
		  return emiQuestionAnswerCombinations;
	  }
	  else { // use lazy initialization
	    ArrayList list = getAnswerArray();
	    emiQuestionAnswerCombinations = new ArrayList();
	    if (list == null) {
	    	return emiQuestionAnswerCombinations;
	    }
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
	    	PublishedAnswer answer = (PublishedAnswer) iter.next();
	    	if (answer.getLabel() != null && answer.getLabel().matches("[0-9]+")) {
	    		emiQuestionAnswerCombinations.add(answer);
	    		ArrayList answerOptions = this.getEmiAnswerOptions();
	    		//set of possible selection options indicating correct and incorrect options
	    		ArrayList selections = new ArrayList();
	    		Iterator optionsIter = answerOptions.iterator();
	    		while (optionsIter.hasNext()) {
	    			PublishedAnswer option = (PublishedAnswer)optionsIter.next();
	    			PublishedAnswer selection = null;
	    			try {
						selection = option.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (answer.isEmiOptionCorrect(option.getLabel())) {
						selection.setIsCorrect(Boolean.TRUE);
					}
					else {
						selection.setIsCorrect(Boolean.FALSE);
					}
					selections.add(selection);
	    		}
	    		answer.setEmiSelectionOptions(selections);
	    	}
	    }
	    Collections.sort(emiQuestionAnswerCombinations);
	    return emiQuestionAnswerCombinations;
	  }
  }
*/  
  
  //gopalrc - added Aug 2010
	public Set getItemTextAttachmentSet() {
		return itemTextAttachmentSet;
	}

  //gopalrc - added Aug 2010
	public void setItemTextAttachmentSet(Set itemTextAttachmentSet) {
		this.itemTextAttachmentSet = itemTextAttachmentSet;
	}

  //gopalrc - added Aug 2010
	public List getItemTextAttachmentList() {
		ArrayList list = new ArrayList();
		if (itemTextAttachmentSet != null) {
			Iterator iter = itemTextAttachmentSet.iterator();
			while (iter.hasNext()) {
				ItemTextAttachmentIfc a = (ItemTextAttachmentIfc) iter.next();
				list.add(a);
			}
		}
		return list;
	}
	
	
	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  public boolean getHasAttachment(){
	    if (itemTextAttachmentSet != null && itemTextAttachmentSet.size() >0)
	      return true;
	    else
	      return false;    
	  }
	
	
	  //gopalrc - added Aug 2010
	  //This is an actual EMI Question Item 
	  //(i.e. not Theme or Lead In Text or the complete Answer Options list) 
	  public boolean isEmiQuestionItemText() {
		  return getSequence() > 0;
	  }

		// gopalrc - added Aug 2010
		public Integer getRequiredOptionsCount() {
			return requiredOptionsCount;
		}

		// gopalrc - added Aug 2010
		public void setRequiredOptionsCount(Integer requiredOptionsCount) {
			this.requiredOptionsCount = requiredOptionsCount;
		}

		// gopalrc - added Aug 2010
		public String getEmiCorrectOptionLabels() {
			if (!item.getTypeId().equals(TypeD.EXTENDED_MATCHING_ITEMS)) return null;
			if (!this.isEmiQuestionItemText()) return null;
			if (answerSet==null) return null;
			String correctOptionLabels = "";
			Iterator iter = getAnswerArraySorted().iterator();
			while (iter.hasNext()) {
				AnswerIfc answer = (AnswerIfc)iter.next();
				if (answer.getIsCorrect()) {
					correctOptionLabels += answer.getLabel();
				}
			}
			return correctOptionLabels;	
		}


}
