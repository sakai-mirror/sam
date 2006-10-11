
/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
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
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.tool.assessment.data.model.Tree;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionDataIfc;
import org.sakaiproject.tool.assessment.facade.SectionFacade;
import org.sakaiproject.tool.assessment.services.QuestionPoolService;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.ui.listener.author.SavePartListener;
import org.sakaiproject.tool.assessment.ui.listener.author.SavePartAttachmentListener;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.facade.QuestionPoolFacade;
import org.sakaiproject.tool.assessment.ui.bean.author.AssessmentBean;
import org.sakaiproject.tool.assessment.facade.AgentFacade;

import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.entity.impl.ReferenceComponent;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;

import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.tool.cover.SessionManager;

/**
 * Used to be org.navigoproject.ui.web.asi.author.section.SectionActionForm.java
 */

public class SectionBean implements Serializable
{
  private static Log log = LogFactory.getLog(SectionBean.class);

/** Use serialVersionUID for interoperability. */
private final static long serialVersionUID = 4216587136245498157L;
private String assessmentTitle;
private String assessmentId;
private String showMetadata;
private String sectionId;
private String noOfItems;
private String sectionTitle;
private String sectionDescription;
private ArrayList assessmentSectionIdents;
private ArrayList poolsAvailable;  // selectItems for pools
private ArrayList items;
private boolean random;
private String removeAllQuestions; // 1=Yes, 0=No
private SectionFacade section;
private AssessmentIfc assessment;
private String destSectionId; //destinated section where questions will be moved to

private String numberSelected;
private String selectedPool;  // pool id for the item to be added to

private String objective;
private String keyword;
private String rubric;
private String type;
private String questionOrdering;

private boolean hideRandom = false;
private boolean hideOneByOne= false;

private String outcome;
private Tree tree;

private List attachmentList;

  public void setSection(SectionFacade section) {
    try {
      this.section = section;
      this.assessment = section.getAssessment();
      this.assessmentId = assessment.getAssessmentId().toString();
      this.assessmentTitle = assessment.getTitle();
      this.sectionId = section.getSectionId().toString();
      this.sectionTitle = section.getTitle();
      this.sectionDescription = section.getDescription();
      this.attachmentList = section.getSectionAttachmentList();
      if (this.attachmentList != null && this.attachmentList.size() >0)
        this.hasAttachment = true;
    }
    catch (Exception ex) {
      log.warn(ex.getMessage());
    }
  }

  public SectionFacade getSection(){
    return section;
  }

  public boolean getHideRandom()
  {
    return hideRandom;
  }

  public void setHideRandom(boolean param)
  {
    hideRandom = param;
  }


  public boolean getHideOneByOne()
  {
    return hideOneByOne;
  }

  public void setHideOneByOne(boolean param)
  {
    hideOneByOne= param;
  }


  /**
   * @return
   */
  public String getAssessmentId()
  {
    return assessmentId;
  }

  /**
   * @return
   */
  public String getAssessmentTitle()
  {
    return assessmentTitle;
  }

  /**
   * @param string
   */
  public void setAssessmentId(String string)
  {
    assessmentId = string;
  }

  /**
   * @param string
   */
  public void setAssessmentTitle(String string)
  {
    assessmentTitle = string;
  }

  /**
   * @return
   */
  public String getShowMetadata()
  {
    return showMetadata;
  }

  /**
   * @param string
   */
  public void setShowMetadata(String string)
  {
    showMetadata = string;
  }

  /**
   * @return
   */
  public String getSectionId()
  {
    return sectionId;
  }

  public String getSectionIdent()
  {
    return getSectionId();
  }

  /**
   * @param string
   */
  public void setSectionId(String string)
  {
    sectionId = string;
  }

  public void setSectionIdent(String string)
  {
    setSectionId(string);
  }

  /**
   * @return
   */
  public ArrayList getAssessmentSectionIdents()
  {
    return assessmentSectionIdents;
  }

  /**
   * @param list
   */
  public void setAssessmentSectionIdents(ArrayList list)
  {
    assessmentSectionIdents = list;
  }

  /**
   * Get a numerical sequence for all parts.
   * Derived property.
   * @return String[] in format "1", "2", "3"... up to the number of parts
   */
  public ArrayList getSectionNumberList()
  {
    ArrayList list = new ArrayList();

    if (assessmentSectionIdents==null) return list;

    for (int i = 0; i < assessmentSectionIdents.toArray().length; i++) {
      SelectItem selection = new SelectItem();
      selection.setLabel("" + i);
      selection.setValue("" + i);
      list.add(selection);
    }

    return list;
  }


  public ArrayList getAuthorTypeList(){

    ArrayList list = new ArrayList();
    // cannot disable only one radio button in a list, so am generating the list again

    FacesContext context=FacesContext.getCurrentInstance();
    ResourceBundle rb=ResourceBundle.getBundle("org.sakaiproject.tool.assessment.bundle.AuthorMessages", context.getViewRoot().getLocale());


    if (hideRandom){
        SelectItem selection = new SelectItem();
        selection.setLabel((String)rb.getObject("type_onebyone"));
        selection.setValue("1");
        list.add(selection);
    }
    else {
        SelectItem selection = new SelectItem();
        selection.setLabel((String)rb.getObject("type_onebyone"));
        selection.setValue("1");
        list.add(selection);
        SelectItem selection1 = new SelectItem();
        selection1.setLabel((String)rb.getObject("random_draw_from_que"));
        selection1.setValue("2");
        list.add(selection1);
    }

    return list;
  }


  /**
   * Ordinal number of current section.
   * Derived property.
   * @return String the number as a String, e.g. "3"
   */
  public String getSelectedSection(){
    return "" + assessmentSectionIdents.indexOf(sectionId);
  }

  public int getTotalSections()
  {
    return assessmentSectionIdents.size();
  }

  /**List of available question pools for random draw. 
   * returns a list of pools that have not been used by other random drawn parts 
   * @return ArrayList of QuestionPoolFacade objects
   */
  public ArrayList getPoolsAvailable()
  {
  
    ArrayList resultPoolList= new ArrayList();
    AssessmentBean assessmentBean = (AssessmentBean) ContextUtil.lookupBean("assessmentBean");
    //ItemAuthorBean itemauthorBean = (ItemAuthorBean) ContextUtil.lookupBean("itemauthor");

    QuestionPoolService delegate = new QuestionPoolService();
    
     ArrayList allpoollist = delegate.getBasicInfoOfAllPools(AgentFacade.getAgentString());

    HashMap allPoolsMap= new HashMap();
    for (int i=0; i<allpoollist.size();i++){
      QuestionPoolFacade apool = (QuestionPoolFacade) allpoollist.get(i);
      allPoolsMap.put(apool.getQuestionPoolId().toString(), apool);
    }

    AssessmentService assessdelegate = new AssessmentService();
    List sectionList = assessmentBean.getSectionList();
    for (int i=0; i<sectionList.size();i++){
      SelectItem s = (SelectItem) sectionList.get(i);

      // need to remove the pools already used by random draw parts

      SectionDataIfc section= assessdelegate.getSection(s.getValue().toString());
      if( (section !=null) && (section.getSectionMetaDataByLabel(SectionDataIfc.AUTHOR_TYPE)!=null) &&
 (section.getSectionMetaDataByLabel(SectionDataIfc.AUTHOR_TYPE).equals(SectionDataIfc.RANDOM_DRAW_FROM_QUESTIONPOOL.toString()))) {
	String poolid = section.getSectionMetaDataByLabel(SectionDataIfc.POOLID_FOR_RANDOM_DRAW);
	if (allPoolsMap.containsKey(poolid) ) {
	  allPoolsMap.remove(poolid);
	}
      }
    }

    Iterator pooliter = allPoolsMap.keySet().iterator();
    while (pooliter.hasNext()) {
      QuestionPoolFacade pool = (QuestionPoolFacade) allPoolsMap.get(pooliter.next());
      //Huong's new
      ArrayList itemlist = delegate.getAllItems(pool.getQuestionPoolId() );
      if(itemlist.size()>0){
	  String resultListName= pool.getDisplayName()+"("+ itemlist.size()+")" ;	
	  resultPoolList.add(new SelectItem((pool.getQuestionPoolId().toString()),resultListName) );
      }
    }
    //  add pool which is currently used in current Part for modify part
    if (!("".equals(this.getSelectedPool())) && (this.getSelectedPool() !=null)){

    //now we need to get the poolid and displayName
     
	QuestionPoolFacade currPool= delegate.getPool(new Long(this.getSelectedPool()), AgentFacade.getAgentString());
    // now add the current pool used  to the list, so it's available in the pulldown 
        if (currPool!=null) {
          // if the pool still exists, it's possible that the pool has been deleted 
	  ArrayList currItemlist = delegate.getAllItems(currPool.getQuestionPoolId());
	  if(currItemlist.size()>0){
	    String currPoolName= currPool.getDisplayName()+"("+ currItemlist.size()+")" ;
	    resultPoolList.add(new SelectItem((currPool.getQuestionPoolId().toString()), currPoolName));
     
	  }
        }
        else {
          // the pool has been deleted, 
        } 
    }

    return resultPoolList;
  }



  /**List of available question pools.
   * @param list ArrayList of selectItems
   */
  public void setPoolsAvailable(ArrayList list)
  {
    poolsAvailable = list;
  }

  /**
   * @return
   */
  public String getNoOfItems()
  {
    return noOfItems;
  }

  /**
   * @param string
   */
  public void setNoOfItems(String string)
  {
    noOfItems = string;
  }

  /**
 * Get a numerical sequence for all questions.
 * Derived property.
 * @return String[] in format "1", "2", "3"... up to the number of questions
 */

  public ArrayList getItemNumberList(){
    ArrayList list = new ArrayList();

    for (int i = 0; i < items.toArray().length; i++) {
      SelectItem selection = new SelectItem();
      selection.setLabel("" + i);
      selection.setValue("" + i);
      list.add(selection);
    }

    return list;
  }

  /**
   * @return the title
   */
  public String getSectionTitle()
  {
    return this.sectionTitle;
  }

  /**
   * @param string the title
   */
  public void setSectionTitle(String string)
  {
    this.sectionTitle = string;
  }
  /**
   * @return the info
   */
  public String getSectionDescription()
  {
    return sectionDescription;
  }

  /**
   * @param string the info
   */
  public void setSectionDescription(String string)
  {
    sectionDescription = string;
  }
  /**
   * @return the number selected
   */
  public String getNumberSelected()
  {
    return numberSelected;
  }

  /**
   * @param string the number selected
   */
  public void setNumberSelected(String string)
  {
    numberSelected = string;
  }

  /**
   * randomize?
   * @return boolean
   */
  public boolean getRandom()
  {
    return random;
  }

  public ArrayList getItems()
  {
    return items;
  }

  /**
   * randomize?
   * @param bool boolean
   */
  public void setRandom(boolean bool)
  {
    random = bool;
  }

  public void setItems(ArrayList items)
  {
    this.items = items;
  }

  /**
   * If removing part, do questions go with it?
   * @return true if questions are deleted too.
   */
  public String getRemoveAllQuestions()
  {
    return removeAllQuestions;
  }

  /**
   * If removing part, do questions go with it?
   * @param removeAllQuestions
   */
  public void setRemoveAllQuestions(String removeAllQuestions)
  {
    this.removeAllQuestions = removeAllQuestions;
  }

  public String getDestSectionId()
  {
    return destSectionId;
  }

  /**
   * @param string the title
   */
  public void setDestSectionId(String destSectionId)
  {
    this.destSectionId = destSectionId;
  }


  /**
   * String value of selected pool id
   * @return String value of selected pool id
   */
  public String getSelectedPool() {
    return selectedPool;
  }

  /**
   * set the String value of selected pool id
   * @param selectedPool String value of selected pool id
   */
  public void setSelectedPool(String selectedPool) {
    this.selectedPool = selectedPool;
  }

   /**
   * get keyword metadata
   */
  public String getKeyword()
  {
    return keyword;
  }

  /**
   * set metadata
   * @param param
   */
  public void setKeyword(String param)
  {
    this.keyword= param;
  }

   /**
   * get objective metadata
   */
  public String getObjective()
  {
    return objective;
  }

  /**
   * set metadata
   * @param param
   */
  public void setObjective(String param)
  {
    this.objective= param;
  }


   /**
   * get rubric metadata
   */
  public String getRubric()
  {
    return rubric;
  }

  /**
   * set metadata
   * @param param
   */
  public void setRubric(String param)
  {
    this.rubric= param;
  }


   /**
   * get type
   */
  public String getType()
  {
    return type;
  }

  /**
   * set type
   * @param param
   */
  public void setType(String param)
  {
    this.type= param;
  }


   /**
   * get questionOrdering
   */
  public String getQuestionOrdering()
  {
    return questionOrdering;
  }

  /**
   * set questionOrdering
   * @param param
   */
  public void setQuestionOrdering(String param)
  {
    this.questionOrdering= param;
  }

  public void toggleAuthorType(ValueChangeEvent event) {

// need to update metadata in db.
        //FacesContext context = FacesContext.getCurrentInstance();
        String type = (String) event.getNewValue();


        if ((type == null) || type.equals(SectionDataIfc.QUESTIONS_AUTHORED_ONE_BY_ONE.toString())) {
          setType(SectionDataIfc.QUESTIONS_AUTHORED_ONE_BY_ONE.toString());
        }
        else if ((type != null) || type.equals(SectionDataIfc.RANDOM_DRAW_FROM_QUESTIONPOOL.toString())) {
          setType(SectionDataIfc.RANDOM_DRAW_FROM_QUESTIONPOOL.toString());
        }
        else {
	  // shouldn't go here.
        }

  }


   /**
   * get outcome
   */
  public String getOutcome()
  {
    return outcome;
  }

  /**
   * set outcome
   * @param param
   */
  public void setOutcome(String param)
  {
    this.outcome= param;
  }

  public List getAttachmentList() {
    return attachmentList;
  }

  public void setAttachmentList(List attachmentList)
  {
    this.attachmentList = attachmentList;
  }

  private boolean hasAttachment = false;
  public boolean getHasAttachment(){
    return this.hasAttachment;
  }

  public void setHasAttachment(boolean hasAttachment){
    this.hasAttachment = hasAttachment;
  }

  public String addAttachmentsRedirect() {
    // 1. first save any part description and stuff
    savePart();

    // 2. then redirect to add attachment
    try	{
      List filePickerList = prepareReferenceList(attachmentList);
      ToolSession currentToolSession = SessionManager.getCurrentToolSession();
      currentToolSession.setAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS, filePickerList);
      ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
      context.redirect("sakai.filepicker.helper/tool");
    }
    catch(Exception e){
      log.error("fail to redirect to attachment page: " + e.getMessage());
    }
    return getOutcome();
  }

  private void savePart(){
    SavePartListener lis = new SavePartListener();
    lis.processAction(null);
  }

  public void savePartAttachment(){
    SavePartAttachmentListener lis = new SavePartAttachmentListener();
    lis.processAction(null);
  }

  private List prepareReferenceList(List attachmentList){
    List list = new ArrayList();
    for (int i=0; i<attachmentList.size(); i++){
      AttachmentIfc attach = (AttachmentIfc) attachmentList.get(i);
      try{
        ContentResource cr = ContentHostingService.getResource(attach.getResourceId());
        if (cr!=null){
          ReferenceComponent ref = new ReferenceComponent(cr.getReference());
          if (ref !=null ) list.add(ref);
        }
      }
      catch (PermissionException e) {
    	  log.warn(e.getMessage());
      }
      catch (IdUnusedException e) {
    	  log.warn(e.getMessage());
      }
      catch (TypeException e) {
    	  log.warn(e.getMessage());
      }
    }
    return list;
  }

}
