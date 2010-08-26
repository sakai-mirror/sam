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



package org.sakaiproject.tool.assessment.ui.bean.delivery;

import java.util.ArrayList;
import java.util.Iterator;


import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;


import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.application.FacesMessage;


/**
 * @author rgollub@stanford.edu
 * $Id$
 */
public class FibBean
{

  private ItemContentsBean parent;
  private AnswerIfc answer;
  private ItemGradingData data;
  private String response;
  private String text;
  private boolean isCorrect;
  private boolean hasInput;
  
  //gopalrc - added for EMI - Jan 2010
  //The MatchingBean that contains the set of FibBeans 
  //for each sub-question in choices list
  //private MatchingBean subQuestionContainer;
  


  public ItemContentsBean getItemContentsBean()
  {
    return parent;
  }

  public void setItemContentsBean(ItemContentsBean bean)
  {
    parent = bean;
  }

  public AnswerIfc getAnswer()
  {
    return answer;
  }

  public void setAnswer(AnswerIfc newanswer)
  {
    answer = newanswer;
  }

  public ItemGradingData getItemGradingData()
  {
    return data;
  }

  public void setItemGradingData(ItemGradingData newdata)
  {
    data = newdata;
  }

  public String getResponse()
  {
    if (response == null)
      return "";
    return response;
  }

  public void setResponse(String newresp)
  {
    
    //gopalrc - Jan 2010
	//gopalrc - Aug 2010 - Below is probably not needed for new EMI response structure
	// Will remove when refactor complete
	/*  
    if (parent.getItemData().getTypeId().equals(TypeIfc.EXTENDED_MATCHING_ITEMS)) {
   		response = newresp.toUpperCase();
    	
   		// If the Response is empty
        if (response==null || response.trim().equals("")) {
        	if (data != null) {
  	    	  ArrayList items = parent.getItemGradingDataArray();
  	    	  Iterator iter = items.iterator();
  	    	  while (iter.hasNext()) {
  	    		  ItemGradingData itemGrading = (ItemGradingData)iter.next();
  	    		  if (itemGrading.getItemGradingId().equals(data.getItemGradingId())) {
  	    			  itemGrading.setPublishedAnswerId(null);
  	    			  data = null;
    	      	      parent.setItemGradingDataArray(items); //must keep this line
  	    			  break;
  	    		  }
  	    	  }
        	}
        } // end if response is empty
        
        // The response is not empty and there is no saved ItemGradingData
        // i.e. it is a new response
        else if (data == null || data.getItemGradingId().equals(null))
	    {
          data = new ItemGradingData();
	      data.setPublishedItemId(parent.getItemData().getItemId());
	      Iterator iter = subQuestionContainer.getItemText().getAnswerSet().iterator();
	      while (iter.hasNext()) {
	    	  AnswerIfc selectedAnswer = (AnswerIfc) iter.next();
	    	  if (selectedAnswer.getLabel().equals(response)) {
	    		  answer = selectedAnswer;
	    	      data.setPublishedItemTextId(answer.getItemText().getId());
	    	      data.setPublishedAnswerId(answer.getId());
    	    	  ArrayList items = parent.getItemGradingDataArray();
    	    	  items.add(data);
	    	      parent.setItemGradingDataArray(items); //must keep this line
	    	      break;
	    	  }
	      }
	    } // end if data == null
        
        // The user changed the response
        else if (!data.getPublishedAnswer().getLabel().equals(response)) // changed response
	    {
  	      data.setPublishedItemId(parent.getItemData().getItemId());
	      Iterator iter = subQuestionContainer.getItemText().getAnswerSet().iterator();
	      boolean validAnswer = false;
	      while (iter.hasNext()) {
	    	  AnswerIfc selectedAnswer = (AnswerIfc) iter.next();
	    	  if (selectedAnswer.getLabel().equals(response)) {
	    		  answer = selectedAnswer;
	    	      data.setPublishedItemTextId(answer.getItemText().getId());
	    	      data.setPublishedAnswerId(answer.getId());
	    	      validAnswer = true;
	    	      break;
	    	  }
	      }
    	  ArrayList items = parent.getItemGradingDataArray();
    	  iter = items.iterator();
    	  while (iter.hasNext()) {
    		  ItemGradingData itemGrading = (ItemGradingData)iter.next();
    		  if (itemGrading.getItemGradingId().equals(data.getItemGradingId())) {
    			  if (validAnswer) {
	    			  itemGrading.setPublishedItemTextId(data.getPublishedItemTextId());
	    			  itemGrading.setPublishedAnswerId(data.getPublishedAnswerId());
    			  }
    			  else {
  	    			  itemGrading.setPublishedAnswerId(null);
  	    			  data = null;
    			  }
	      	      parent.setItemGradingDataArray(items); //must keep this line
    			  break;
    		  }
    	  }
	    }
        
	    //data.setAnswerText(newresp);
    } // end if EMI
    
    else { // other type
    */
        response = newresp;
	    if (data == null)
	    {
	      data = new ItemGradingData();
	      data.setPublishedItemId(parent.getItemData().getItemId());
	      data.setPublishedItemTextId(answer.getItemText().getId());
	      data.setPublishedAnswerId(answer.getId());
	      ArrayList items = parent.getItemGradingDataArray();
	      items.add(data);
	      parent.setItemGradingDataArray(items);
	    }
	    data.setAnswerText(newresp);
    //}
    
  }

  public String getText()
  {
    return text;
  }

  public void setText(String newtext)
  {
    text = newtext;
  }

  public boolean getIsCorrect()
  {
    return isCorrect;
  }

  public void setIsCorrect(boolean newb)
  {
    isCorrect = newb;
  }

  public boolean getHasInput()
  {
    return hasInput;
  }

  public void setHasInput(boolean newin)
  {
    hasInput = newin;
  }
  
/*  
  //gopalrc - added for EMI - Jan 2010
  public MatchingBean getSubQuestionContainer() {
	return subQuestionContainer;
  }

  //gopalrc - added for EMI - Jan 2010
	public void setSubQuestionContainer(MatchingBean subQuestionContainer) {
		this.subQuestionContainer = subQuestionContainer;
	}
  
	//gopalrc - added for EMI - Jan 2010
	public void validateEmiResponse(FacesContext context, 
            UIComponent toValidate,
            Object value) {
		
		String response = ((String) value).trim().toUpperCase();

		if (response.length() > 1 || (response.length() != 0 && !parent.getItemData().isValidEmiAnswerOptionLabel(response)) ) {
			((UIInput)toValidate).setValid(false);
			String invalid_response = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.DeliveryMessages","invalid_response");     
			String please_select_from_available = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.DeliveryMessages","please_select_from_available");     
			FacesMessage message = new FacesMessage(invalid_response + " '" + response + "' " + please_select_from_available);
			context.addMessage(toValidate.getClientId(context), message);
		}
		else {
		      Iterator iter = subQuestionContainer.getChoices().iterator();
		      while (iter.hasNext()) {
		    	  FibBean fibBean = (FibBean) iter.next();
		    	  if (fibBean.getResponse()!=null && fibBean.getResponse().equals(response) && 
		    			  !fibBean.equals(this)) 
		    	  	{
						((UIInput)toValidate).setValid(false);
						String duplicate_responses = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.DeliveryMessages","duplicate_responses");     
						String for_sub_question = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.DeliveryMessages","for_sub_question");     
						FacesMessage message = new FacesMessage(duplicate_responses + " '" + response + "'  " + for_sub_question + " " + subQuestionContainer.getItemText().getSequence() );
						context.addMessage(toValidate.getClientId(context), message);
		    	  	}
		      }
		}
	}
*/
  
}
