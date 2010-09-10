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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.util.ResourceLoader;

import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.cover.EntityManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;
import org.sakaiproject.tool.assessment.services.ItemService;
import org.sakaiproject.tool.assessment.services.PublishedItemService;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.cover.SessionManager;
 
import javax.faces.context.ExternalContext;
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
  
  //gopalrc Aug 2010 - Attachments for EMI questions
  private List attachmentList;

  //gopalrc - for navigation
  private String outcome;


private Float partialCredit = Float.valueOf(0);  //to incorporate partial credit
  
  private static Log log = LogFactory.getLog(AnswerBean.class);
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
	String optionLabel = null;
	String temp = "";
	// remove white space and delimiter characters
	for (int i=0; i<correctOptionLabels.length(); i++) {
		optionLabel = correctOptionLabels.substring(i, i+1);
		if (optionLabel.trim().equals("") || ItemDataIfc.ANSWER_OPTION_VALID_DELIMITERS.contains(optionLabel)) continue;
		temp += optionLabel;
	}
    this.correctOptionLabels = temp;
  }

  
  //gopalrc Jul 2010 - for EMI questions
  public int correctOptionsCount() {
	  if (correctOptionLabels==null || correctOptionLabels.trim().equals("")) return 0;
	  return correctOptionLabels.split(",").length;
  }

	// --mustansar for partial credit
	public Float getPartialCredit() {
		return partialCredit;
	}

	public void setPartialCredit(Float pCredit) {
		this.partialCredit = pCredit;
	}
	
	
	//gopalrc - added for EMI - Jan 2010
	/*
	public void validateCorrectOptionLabels(FacesContext context, 
            UIComponent toValidate,
            Object value) {
	*/
	//Now Called from ItemAddListener because of cross-field validation
	public boolean validateCorrectOptionLabels(FacesContext context){
		boolean isValid = true;
		String q = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","q");     
		
		if (correctOptionLabels == null || correctOptionLabels.trim().equals("")) {
			isValid = false;
			FacesMessage message = new FacesMessage("Please provide a set of correct option labels for sub-question: " + getSequence());
			context.addMessage(null, message);
		}
		
		String optionLabels = correctOptionLabels.trim().toUpperCase();
		String processed = "";
		
	    ItemAuthorBean itemauthorbean = (ItemAuthorBean) ContextUtil.lookupBean("itemauthor");
	    ItemBean itemBean = itemauthorbean.getCurrentItem();
	    
		for (int i=0; i<optionLabels.length(); i++) {
			String optionLabel = optionLabels.substring(i, i+1);
			if (optionLabel.equals("") || ItemDataIfc.ANSWER_OPTION_VALID_DELIMITERS.contains(optionLabel)) continue;

			if (!itemBean.isValidEmiAnswerOptionLabel(optionLabel)) {
				isValid=false;
				String invalid_response = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","invalid_response");     
				String please_select_from_available = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","please_select_from_available");     
				FacesMessage message = new FacesMessage(invalid_response + " '" + optionLabels + "' " + please_select_from_available + " - " + q + " " + itemauthorbean.getItemNo() + "(" + getSequence() + ")" );
				context.addMessage(null, message);
				break;
			}

			if (processed.contains(optionLabel)) {
				isValid=false;
				String duplicate_responses = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","duplicate_responses");     
				FacesMessage message = new FacesMessage(duplicate_responses + " '" + optionLabel + "' - " + q + " " + itemauthorbean.getItemNo() + "(" + getSequence() + ")" );
				context.addMessage(null, message);
				break;
			}
			
			processed += optionLabel;
			
		}
		return isValid;
	}


	//gopalrc - Jul 2010
	public String getRequiredOptionsCount() {
		return requiredOptionsCount;
	}
	
	public void setRequiredOptionsCount(String requiredOptionsCount) {
		this.requiredOptionsCount = requiredOptionsCount;
	}
	
 	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  public List getAttachmentList() {
	    return attachmentList;
	  }

 	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  public void setAttachmentList(List attachmentList)
	  {
	    this.attachmentList = attachmentList;
	  }

 	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  public boolean getHasAttachment(){
	    if (attachmentList != null && attachmentList.size() >0)
	      return true;
	    else
	      return false;    
	  }
	  
	  //gopalrc
	  public String getOutcome()
	  {
	    return outcome;
	  }

	  /**
	   * set the survey scale
	   * @param param
	   */
	  public void setOutcome(String param)
	  {
	    this.outcome= param;
	  }

 	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  public String addAttachmentsRedirect() {
  	      ItemAuthorBean itemAuthorBean = (ItemAuthorBean) ContextUtil.lookupBean("itemauthor");
  	      itemAuthorBean.setCurrentAnswer(this);
	      ToolSession currentToolSession = SessionManager.getCurrentToolSession();
	      
			// 1. load resources into session for resources mgmt page
			// then redirect to resources mgmt page
			try {
				List filePickerList = prepareAttachmentReferenceList(this.getAttachmentList());
				currentToolSession.setAttribute(
						FilePickerHelper.FILE_PICKER_ATTACHMENTS, filePickerList);
				ExternalContext context = FacesContext.getCurrentInstance()
						.getExternalContext();
				context.redirect("sakai.filepicker.helper/tool");
			} catch (Exception e) {
				log.error("fail to redirect to attachment page: " + e.getMessage());
			}
		  
 	      return getOutcome();
	  }


	  
	    //gopalrc - Aug 2010 - For EMI Item Attachments
	    /* called by SamigoJsfTool.java on exit from file picker */
	    public void setItemTextAttachment(){
	    	AuthorBean author = (AuthorBean) ContextUtil.lookupBean("author");
	    	boolean isEditPendingAssessmentFlow =  author.getIsEditPendingAssessmentFlow();
	    	ItemService service = null;
	    	if (isEditPendingAssessmentFlow) {
	    		service = new ItemService();
	    	}
	    	else {
	    		service = new PublishedItemService();
	    	}
	        ItemDataIfc itemData = null;
		    ItemAuthorBean itemauthorbean = (ItemAuthorBean) ContextUtil.lookupBean("itemauthor");
	        // itemId == null => new questiion
	        if (itemauthorbean.getItemId()!=null){
	          try{
	            itemData = service.getItem(itemauthorbean.getItemId());
	          }
	          catch(Exception e){
	            log.warn(e.getMessage());
	          }
	        }

	        ItemTextIfc itemText=itemData.getItemTextBySequence(getSequence());
	        
	    // list returns contains modified list of attachments, i.e. new 
	    // and old attachments. This list will be 
	    // persisted to DB if user hit Save on the Item Modifying page.
	    List list = prepareItemTextAttachment(itemText, isEditPendingAssessmentFlow);
	    setAttachmentList(list);
	  }

	  
	    //gopalrc - Aug 2010 - For EMI Item Attachments
	    private List prepareAttachmentReferenceList(List attachmentList){
	        List list = new ArrayList();
	        if (attachmentList == null){
	          return list;
	        }
	        for (int i=0; i<attachmentList.size(); i++){
	          ContentResource cr = null;
	          AttachmentIfc attach = (AttachmentIfc) attachmentList.get(i);
	          try{
	            log.debug("*** resourceId="+attach.getResourceId());
	            cr = AssessmentService.getContentHostingService().getResource(attach.getResourceId());
	          }
	          catch (PermissionException e) {
	        	  log.warn("ContentHostingService.getResource() throws PermissionException="+e.getMessage());
	          }
	          catch (IdUnusedException e) {
	        	  log.warn("ContentHostingService.getResource() throws IdUnusedException="+e.getMessage());
	              // <-- bad sign, some left over association of question and resource, 
	              // use case: user remove resource in file picker, then exit modification without
	              // proper cancellation by clicking at the left nav instead of "cancel".
	              // Also in this use case, any added resource would be left orphan.
	              AssessmentService assessmentService = new AssessmentService();
	              assessmentService.removeItemTextAttachment(attach.getAttachmentId().toString());
	          }
	          catch (TypeException e) {
	        	  log.warn("ContentHostingService.getResource() throws TypeException="+e.getMessage());
	          }
	          if (cr!=null){
	        	Reference ref = EntityManager.newReference(cr.getReference());
	            log.debug("*** ref="+ref);
	            if (ref !=null ) list.add(ref);
	          }
	        }
	        return list;
	      }

	    //gopalrc - Aug 2010 - For EMI Item Attachments
	      private List prepareItemTextAttachment(ItemTextIfc itemText, boolean isEditPendingAssessmentFlow){
	        ToolSession session = SessionManager.getCurrentToolSession();
	        if (session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) {

	          Set attachmentSet = new HashSet();
	          if (itemText != null){
	            attachmentSet = itemText.getItemTextAttachmentSet();
	          }
	          HashMap map = getResourceIdHash(attachmentSet);
	          ArrayList newAttachmentList = new ArrayList();
	          
	          AssessmentService assessmentService = new AssessmentService();
	          String protocol = ContextUtil.getProtocol();

	          List refs = (List)session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
	          if (refs!=null && refs.size() > 0){
	            Reference ref;

	            for(int i=0; i<refs.size(); i++) {
	              ref = (Reference) refs.get(i);
	              String resourceId = ref.getId();
	              if (map.get(resourceId) == null){
	                // new attachment, add 
	                log.debug("**** ref.Id="+ref.getId());
	                log.debug("**** ref.name="+ref.getProperties().getProperty(
	                           ref.getProperties().getNamePropDisplayName()));
	                ItemTextAttachmentIfc newAttach = assessmentService.createItemTextAttachment(
	                                              itemText,
	                                              ref.getId(), ref.getProperties().getProperty(
	                                                           ref.getProperties().getNamePropDisplayName()),
	                                            protocol, isEditPendingAssessmentFlow);
	                newAttachmentList.add(newAttach);
	              }
	              else{ 
	                // attachment already exist, let's add it to new list and
	    	    // check it off from map
	                newAttachmentList.add((ItemTextAttachmentIfc)map.get(resourceId));
	                map.remove(resourceId);
	              }
	            }
	          }

	          session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
	          session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
	          return newAttachmentList;
	        }
	        else if (itemText == null) {
	        	return new ArrayList();
	        }
	        else return itemText.getItemTextAttachmentList();
	      }


	    private HashMap getResourceIdHash(Set attachmentSet){
	    	    HashMap map = new HashMap();
	    	    if (attachmentSet !=null ){
	    	      Iterator iter = attachmentSet.iterator();
	    	      while (iter.hasNext()){
	    	        ItemTextAttachmentIfc attach = (ItemTextAttachmentIfc) iter.next();
	    	        map.put(attach.getResourceId(), attach);
	    	      }
	    	    }
	    	    return map;
	    }
	  
	  
 	  //gopalrc - Aug 2010 - for EMI - Attachments at Answer Level
	  private HashMap resourceHash = new HashMap();
	  public HashMap getResourceHash() {
	      return resourceHash;
	  }
	  public void setResourceHash(HashMap resourceHash)
	  {
	      this.resourceHash = resourceHash;
	  }
	
	
}