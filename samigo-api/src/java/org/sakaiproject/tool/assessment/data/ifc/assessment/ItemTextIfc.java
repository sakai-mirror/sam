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



package org.sakaiproject.tool.assessment.data.ifc.assessment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ItemTextIfc
    extends java.io.Serializable
{
	
	
  public static Long EMI_THEME_TEXT_AND_ANSWER_OPTIONS_SEQUENCE = Long.valueOf(0);
  public static Long EMI_LEAD_IN_TEXT_SEQUENCE = Long.valueOf(-1);
  
  /*
  public static Long EMI_THEME_TEXT_SEQUENCE = Long.valueOf(-1);
  public static Long EMI_ANSWER_OPTIONS_SEQUENCE = Long.valueOf(-2);
  public static Long EMI_LEAD_IN_TEXT_SEQUENCE = Long.valueOf(-3);
   */
	  
	
  Long getId();

  void setId(Long id);

  ItemDataIfc getItem();

  void setItem(ItemDataIfc item);

  Long getSequence();

  void setSequence(Long sequence);

  String getText();

  void setText(String text);

  Set getAnswerSet();

  void setAnswerSet(Set answerSet);

  ArrayList getAnswerArray();

  ArrayList getAnswerArraySorted();
  
  Set getItemTextAttachmentSet();

  void setItemTextAttachmentSet(Set itemTextAttachmentSet);

  List getItemTextAttachmentList();

  
  //gopalrc - added 26 Nov 2009
  //TODO - For elegance these methods should probably be moved up to [Published]ItemData
  // as it applies only to the first (seq=0) ItemText
  public ArrayList getEmiAnswerOptions();
  public ArrayList getEmiQuestionAnswerCombinations();
  
}
