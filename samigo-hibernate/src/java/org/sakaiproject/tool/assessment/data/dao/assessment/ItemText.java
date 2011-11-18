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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.sakaiproject.tool.assessment.data.dao.shared.TypeD;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;

public class ItemText
    implements Serializable, ItemTextIfc, Comparable<ItemTextIfc> {
  static Logger errorLogger = Logger.getLogger("errorLogger");

  private static final long serialVersionUID = 7526471155622776147L;

  private Long id;
  private ItemDataIfc item;
  private Long sequence;
  private String text;
  private Set<AnswerIfc> answerSet;

  //gopalrc - added Aug 2010
  private Set<ItemTextAttachmentIfc> itemTextAttachmentSet;
  private Integer requiredOptionsCount;
  

  
  public ItemText() {}

  public ItemText(ItemData item, Long sequence, String text, Set<AnswerIfc> answerSet) {
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

  public Set<AnswerIfc> getAnswerSet() {
    return answerSet;
  }

  public void setAnswerSet(Set<AnswerIfc> answerSet) {
    this.answerSet = answerSet;

    //gopalrc Added 27 Nov 2009
    //emiAnswerOptions = null;
    //emiQuestionAnswerCombinations = null;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
  }

  public List<AnswerIfc> getAnswerArray() {
    List<AnswerIfc> list = new ArrayList<AnswerIfc>();
    list.addAll(answerSet);
    return list;
  }

  public int compareTo(ItemTextIfc o) {
      return sequence.compareTo(o.getSequence());
  }

  public List<AnswerIfc> getAnswerArraySorted() {
    List<AnswerIfc> list = getAnswerArray();
    Collections.sort(list);
    return list;
  }
  
  
    //gopalrc - added Aug 2010
	public Set<ItemTextAttachmentIfc> getItemTextAttachmentSet() {
		return itemTextAttachmentSet;
	}

    //gopalrc - added Aug 2010
	public void setItemTextAttachmentSet(Set<ItemTextAttachmentIfc> itemTextAttachmentSet) {
		this.itemTextAttachmentSet = itemTextAttachmentSet;
	}

    //gopalrc - added Aug 2010
	public List<ItemTextAttachmentIfc> getItemTextAttachmentList() {
		ArrayList<ItemTextAttachmentIfc> list = new ArrayList<ItemTextAttachmentIfc>();
		if (itemTextAttachmentSet != null) {
			Iterator<ItemTextAttachmentIfc> iter = itemTextAttachmentSet.iterator();
			while (iter.hasNext()) {
				ItemTextAttachmentIfc a = iter.next();
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
		Iterator<AnswerIfc> iter = getAnswerArraySorted().iterator();
		while (iter.hasNext()) {
			AnswerIfc answer = iter.next();
			if (answer.getIsCorrect()) {
				correctOptionLabels += answer.getLabel();
			}
		}
		return correctOptionLabels;	
	}
	
	public String toString(){
		return getText();
	}
}
