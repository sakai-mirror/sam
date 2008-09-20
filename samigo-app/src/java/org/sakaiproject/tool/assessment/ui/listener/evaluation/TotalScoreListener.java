/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008 Sakai Foundation
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



package org.sakaiproject.tool.assessment.ui.listener.evaluation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.business.entity.RecordingData;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedAccessControl;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedAssessmentData;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedItemData;
import org.sakaiproject.tool.assessment.data.dao.grading.AssessmentGradingData;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.EvaluationModelIfc;
import org.sakaiproject.tool.assessment.data.ifc.grading.AssessmentGradingIfc;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.integration.context.IntegrationContextFactory;
import org.sakaiproject.tool.assessment.integration.helper.ifc.AgentHelper;
import org.sakaiproject.tool.assessment.services.GradingService;
import org.sakaiproject.tool.assessment.services.PersistenceService;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AuthorBean;
import org.sakaiproject.tool.assessment.ui.bean.authz.AuthorizationBean;
import org.sakaiproject.tool.assessment.ui.bean.delivery.DeliveryBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.AgentResults;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.HistogramScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.QuestionScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.SubmissionStatusBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.TotalScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.util.EmailBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.util.BeanSort;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAccessControlIfc;
import org.sakaiproject.util.FormattedText;

/**
 * <p>
 * This handles the selection of the Total Score entry page.
 *  </p>
 * <p>Description: Action Listener for Evaluation Total Score front door</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $Id$
 */

public class TotalScoreListener
  implements ActionListener, ValueChangeListener
{
  private static Log log = LogFactory.getLog(TotalScoreListener.class);
  private static BeanSort bs;

  //private SectionAwareness sectionAwareness;
  // private List availableSections;

  /**
   * Standard process action method.
   * @param ae ActionEvent
   * @throws AbortProcessingException
   */
  public void processAction(ActionEvent ae) throws
    AbortProcessingException
  {

    // this is called when you click on 'scores' or 'total scores' link from other pages.
    log.debug("TotalScore Action Listener.");
    DeliveryBean delivery = (DeliveryBean) ContextUtil.lookupBean("delivery");
    TotalScoresBean bean = (TotalScoresBean) ContextUtil.lookupBean("totalScores");
    QuestionScoresBean questionbean = (QuestionScoresBean) ContextUtil.lookupBean("questionScores");
    HistogramScoresBean histobean = (HistogramScoresBean) ContextUtil.lookupBean("histogramScores");
    SubmissionStatusBean submissionbean = (SubmissionStatusBean) ContextUtil.lookupBean("submissionStatus");
    EmailBean emailBean = (EmailBean) ContextUtil.lookupBean("email");
    
    // we probably want to change the poster to be consistent
    String publishedId = ContextUtil.lookupParam("publishedId");
    //log.info("Got publishedId " + publishedId);
    PublishedAssessmentService pubAssessmentService = new PublishedAssessmentService();
    PublishedAssessmentFacade pubAssessment = pubAssessmentService.
                                              getPublishedAssessment(publishedId);
    
    // reset scoringType based on evaluationModel,scoringType if coming from authorIndex
    if (ae != null && ae.getComponent().getId().startsWith("authorIndexToScore")) {
    	log.debug("coming from authorIndex");
        log.debug("ae.getComponent().getId() = " + ae.getComponent().getId());
    	EvaluationModelIfc model = pubAssessment.getEvaluationModel();
    	if (model != null && model.getScoringType()!=null){
    		String allSubmissions = model.getScoringType().toString();
    		bean.setAllSubmissions(allSubmissions);
    		questionbean.setAllSubmissions(allSubmissions);
    		histobean.setAllSubmissions(allSubmissions);
    	}
    	else {
    		bean.setAllSubmissions(TotalScoresBean.LAST_SUBMISSION); 
    		questionbean.setAllSubmissions(TotalScoresBean.LAST_SUBMISSION);
    		histobean.setAllSubmissions(TotalScoresBean.LAST_SUBMISSION);
    	}
    }
    
    // Set grader info (for email feature)
    AgentFacade agent = new AgentFacade();
	StringBuilder sb = new StringBuilder(agent.getFirstName());
	sb.append(" ");
	sb.append(agent.getLastName());
	bean.setGraderName(sb.toString());
	emailBean.setFromName(agent.getEmail());
    // Set from email here. We need it to decide if the Email link should be displayed or not.
    // (if from email is null, we don't display the Email link)
    // as well.
    bean.setGraderEmailInfo(agent.getEmail());
    emailBean.setFromEmailAddress(agent.getEmail());
   
    // checking for permission first
    FacesContext context = FacesContext.getCurrentInstance();
    AuthorBean author = (AuthorBean) ContextUtil.lookupBean("author");
    author.setOutcome("totalScores");
    if (!passAuthz(context, pubAssessment.getCreatedBy())){
      author.setOutcome("author");
      return;
    }

    // set action mode
    delivery.setActionString("gradeAssessment");

    // Reset the search field
    String defaultSearchString = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "search_default_student_search_string");
    bean.setSearchString(defaultSearchString);
    
    // reset question score page content 
    questionbean.setSections(new ArrayList());
    questionbean.setTypeId("0");   // if setting "", QuestionScoreBean.getTypeId will default to 1. Thus setting it to 0. 
    questionbean.setMaxScore("");
    questionbean.setDeliveryItem(new ArrayList());
    questionbean.setSelectedSARationaleView(QuestionScoresBean.SHOW_SA_RATIONALE_RESPONSES_POPUP);
    
    // if comes from scores link in auther index (means to view the score of a different assessment)
    // we reset the following values for paging (for audio, displays 5 records; for others, display all)
    if (ae.getComponent().getId().indexOf("authorIndexToScore") > -1) {
    	submissionbean.setMaxDisplayedRows(0);
    	bean.setMaxDisplayedRows(0);
    	questionbean.setHasAudioMaxDisplayedScoreRowsChanged(false);
    	questionbean.setMaxDisplayedRows(0);
    	questionbean.setOtherMaxDisplayedScoreRows(0);
    	questionbean.setAudioMaxDisplayedScoreRows(5);
    }
    
    if (!totalScores(pubAssessment, bean, false))
    {
      throw new RuntimeException("failed to call totalScores.");
    }

  }

  /**
   * Process a value change.
   */
  public void processValueChange(ValueChangeEvent event)
  {
    // this is called when you change the section or submission pulldown. 
    // need reset assessmentGrading list
    ResetTotalScoreListener reset = new ResetTotalScoreListener();
    reset.processAction(null);

    //log.info("TotalScore CHANGE LISTENER.");
    TotalScoresBean bean = (TotalScoresBean) ContextUtil.lookupBean("totalScores");
    QuestionScoresBean questionbean = (QuestionScoresBean) ContextUtil.lookupBean("questionScores");
    HistogramScoresBean histobean = (HistogramScoresBean) ContextUtil.lookupBean("histogramScores");

    // we probably want to change the poster to be consistent
    String publishedId = ContextUtil.lookupParam("publishedId");
    PublishedAssessmentService pubAssessmentService = new PublishedAssessmentService();
    PublishedAssessmentFacade pubAssessment = pubAssessmentService.
                                              getPublishedAssessment(publishedId);

    String selectedvalue= (String) event.getNewValue();
    if ((selectedvalue!=null) && (!selectedvalue.equals("")) ){
      if (event.getComponent().getId().indexOf("sectionpicker") >-1 ) 
      {
        log.debug("changed section picker");
        bean.setSelectedSectionFilterValue(selectedvalue);   // changed section pulldown
        questionbean.setSelectedSectionFilterValue(selectedvalue);
      }
      else 
      {
        log.debug("changed submission pulldown ");
        bean.setAllSubmissions(selectedvalue);    // changed for total score bean
        histobean.setAllSubmissions(selectedvalue);    // changed for histogram score bean
        questionbean.setAllSubmissions(selectedvalue); // changed for Question score bean
      }
    }

    //log.info("Calling totalScores.");
    if (!totalScores(pubAssessment, bean, true))
    {
      throw new RuntimeException("failed to call totalScores.");
    }
  }

  /**
   * This will populate the TotalScoresBean with the data associated with the
   * particular versioned assessment based on the publishedId.
   *
   * @todo Some of this code will change when we move this to Hibernate persistence.
   * @param publishedId String
   * @param bean TotalScoresBean
   * @return boolean
   */
  public boolean totalScores(
    PublishedAssessmentFacade pubAssessment, TotalScoresBean bean, boolean isValueChange)
  {
	log.debug("TotalScoreListener: totalScores() starts");
    if (ContextUtil.lookupParam("sortBy") != null &&
	!ContextUtil.lookupParam("sortBy").trim().equals("")){
      bean.setSortType(ContextUtil.lookupParam("sortBy"));
      log.debug("TotalScoreListener: totalScores() :: sortBy = " + ContextUtil.lookupParam("sortBy"));
    }
    boolean sortAscending = true;
    if (ContextUtil.lookupParam("sortAscending") != null &&
    		!ContextUtil.lookupParam("sortAscending").trim().equals("")){
    	sortAscending = Boolean.valueOf(ContextUtil.lookupParam("sortAscending")).booleanValue();
    	bean.setSortAscending(sortAscending);
    	log.debug("TotalScoreListener: totalScores() :: sortAscending = " + sortAscending);
    }
    
    log.debug("totalScores()");
    try
    {
      // when will this happen? 
      boolean firstTime = true;
      PublishedAssessmentData p = (PublishedAssessmentData)pubAssessment.getData();

      // check if this is the first visit to total Scores page, if not, then firstTime is set to false, 
      // for example, if you click on 'scores' from authorIndex page, firstTime is true.  then you click
      // 'question scores' page. then if you click on 'totalscores' page again from 'question scores' 
      // page, this firstTime = false;

      if ((bean.getPublishedId()).equals(p.getPublishedAssessmentId().toString())){
        firstTime = false;
      }

      // this line below also call bean.setPublishedId() so that the previous if.. will return true for 
      // any subsequent click on 'totalscores' link.
      bean.setPublishedAssessment(p);

      PublishedAccessControl ac = (PublishedAccessControl) p.getAssessmentAccessControl();
      if (ac.getTimeLimit().equals(Integer.valueOf(0))) {
    	  bean.setIsTimedAssessment(false);
      }
      else {
    	  bean.setIsTimedAssessment(true);
      }
      
      if (ac.getLateHandling().equals(AssessmentAccessControlIfc.ACCEPT_LATE_SUBMISSION)) {
    	  bean.setAcceptLateSubmission(true);
      }
      else {
    	  bean.setAcceptLateSubmission(false);
      }
      
      //#1 - prepareAgentResultList prepare a list of AssesmentGradingData and set it as
      // bean.agents later in step #4
      // scores is a filtered list contains last AssessmentGradingData submitted for grade
      ArrayList scores = new ArrayList();  
      ArrayList students_not_submitted= new ArrayList();  
      
      // if for anonymous, reset totalscorebean.getselectedsectionfiltervalue = ALL_SECTIONS_SELECT_VALUE 
    if ("true".equalsIgnoreCase(bean.getAnonymous())){
      //reset sectionaware pulldown to -1 all sections
      //bean.setSelectedSectionFilterValue(TotalScoresBean.ALL_SECTIONS_SELECT_VALUE);
      
      // changed from above by gopalrc - Jan 2008
    	//PublishedAssessmentService publishedAssessmentService = new PublishedAssessmentService();
	    //  boolean groupRelease = publishedAssessmentService.isReleasedToGroups(bean.getPublishedId());
    	boolean groupRelease = p.getAssessmentAccessControl().getReleaseTo().equals(AssessmentAccessControlIfc.RELEASE_TO_SELECTED_GROUPS);
	  	if (groupRelease) {
	  		bean.setSelectedSectionFilterValue(TotalScoresBean.RELEASED_SECTIONS_GROUPS_SELECT_VALUE);
	  	}
	  	else {
	  		bean.setSelectedSectionFilterValue(TotalScoresBean.ALL_SECTIONS_SELECT_VALUE);
	  	}
      
    }

      Map useridMap= bean.getUserIdMap(TotalScoresBean.CALLED_FROM_TOTAL_SCORE_LISTENER); 
      ArrayList agents = new ArrayList();
      prepareAgentResultList(bean, p, scores, students_not_submitted, useridMap);
      if ((scores.size()==0) && (students_not_submitted.size()==0)) 
      // no submission and no not_submitted students, return
      {
        bean.setAgents(agents);
        bean.setAllAgents(agents);
        return true;
      }

      // pass #1, proceed forward to prepare properties that set the link "Statistics"
      //#2 - the following methods are used to determine if the link "Statistics"
      // and "Questions" should be displayed in totalScore.jsp. Once set, they 
      // need not be executed everytime
      if (firstTime){
        // if section set is null, initialize it - daisyf , 01/31/05
        HashSet sectionSet = PersistenceService.getInstance().
                     getPublishedAssessmentFacadeQueries().getSectionSetForAssessment(p);
        p.setSectionSet(sectionSet);
        bean.setFirstItem(getFirstItem(p));
log.debug("totallistener: firstItem = " + bean.getFirstItem());
        bean.setHasRandomDrawPart(hasRandomPart(p));
      }
      if (firstTime || (isValueChange)){
        bean.setAnsweredItems(getAnsweredItems(scores, p)); // Save for QuestionScores
      }
      log.debug("**firstTime="+firstTime);
      log.debug("**isValueChange="+isValueChange);

      //#3 - Collect a list of all the users in the scores list
      ArrayList agentUserIds = getAgentIds(useridMap);
      AgentHelper helper = IntegrationContextFactory.getInstance().getAgentHelper();
      Map userRoles = helper.getUserRolesFromContextRealm(agentUserIds);
      //#4 - prepare agentResult list
      prepareAgentResult(p, scores.iterator(), agents, userRoles);
      prepareNotSubmittedAgentResult(students_not_submitted.iterator(), agents, userRoles);
      bean.setAgents(agents);
      bean.setAllAgents(agents);
      bean.setTotalPeople(Integer.toString(bean.getAgents().size()));

      //#5 - set role & sort selection
      setRoleAndSortSelection(bean, agents, sortAscending);

      //#6 - this is for audio questions?
      //setRecordingData(bean);

    }

    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }

    return true;
  }


 public boolean passAuthz(FacesContext context, String ownerId){
    AuthorizationBean authzBean = (AuthorizationBean) ContextUtil.lookupBean("authorization");
    boolean hasPrivilege_any = authzBean.getGradeAnyAssessment();
    boolean hasPrivilege_own0 = authzBean.getGradeOwnAssessment();
    boolean hasPrivilege_own = (hasPrivilege_own0 && isOwner(ownerId));
    boolean hasPrivilege = (hasPrivilege_any || hasPrivilege_own);
    if (!hasPrivilege){
       String err=(String)ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages", "denied_grade_assessment_error");
       context.addMessage("authorIndexForm:grade_assessment_denied" ,new FacesMessage(err));
    }
    return hasPrivilege;
  }

  public boolean isOwner(String ownerId){
    boolean isOwner = false;
    String agentId = AgentFacade.getAgentString();
    isOwner = agentId.equals(ownerId);
    log.debug("***isOwner="+isOwner);
    return isOwner;
  }

  public Integer getScoringType(PublishedAssessmentData pub){
    Integer scoringType = EvaluationModelIfc.HIGHEST_SCORE;
    EvaluationModelIfc e = pub.getEvaluationModel();
    if ( e!=null ){
      scoringType = e.getScoringType();
    }
    return scoringType;
  }

  // Set first item for question scores.  This can be complicated.
  // It's important because it simplifies Question Scores to do this
  // once and keep track of it -- the data is available here, and
  // not there.  If firstItem is "", there are no items with
  // answers, and the QuestionScores and Histograms pages don't
  // show.  This is a very weird case, but has to be handled.
  /* daisy's  comment: Really? I don't really understand why but 
     I have rewritten this method so it is more efficient. The old method
     has trouble dealing with large class with large question set. */

  public HashMap getAnsweredItems(ArrayList scores, PublishedAssessmentData pub){
    log.debug("*** in getAnsweredItems.  scores.size = " + scores.size());
    HashMap answeredItems = new HashMap();
    HashMap h = new HashMap();

    // 0. build a Hashmap containing all the assessmentGradingId in the filtered list 
    for (int m=0; m<scores.size(); m++){
      AssessmentGradingData a = (AssessmentGradingData)scores.get(m);
      h.put(a.getAssessmentGradingId(),"");
    }

        log.debug("****h.size "+h.size());
    // 1. get list of publishedItemId
    List list =PersistenceService.getInstance().
      getPublishedAssessmentFacadeQueries().getPublishedItemIds(pub.getPublishedAssessmentId());

        log.debug("***list .size "+list.size());
    // 2. build a HashMap (Long publishedItemId, ArrayList assessmentGradingIds)
    HashMap itemIdHash = getPublishedItemIdHash(pub);
        log.debug("***temIdHash.size "+itemIdHash.size());

    // 3. go through each publishedItemId and get all the submission of 
    // assessmentGradingId for the item
    for (int i=0; i<list.size(); i++){
      Long itemId = (Long)list.get(i);
      log.debug("****publishedItemId"+itemId);
      ArrayList l = new ArrayList();
      Object o = itemIdHash.get(itemId);
      if (o != null) l = (ArrayList) o;
      // check if the assessmentGradingId submitted is among the filtered list
      for (int j=0; j<l.size(); j++){
        Long assessmentGradingId = (Long) l.get(j);
        log.debug("****assessmentGradingId"+assessmentGradingId);
        if (h.get(assessmentGradingId) != null){
      log.debug("****putting itemid into answeredItems: " + itemId);
          answeredItems.put(itemId, "true");
          break;    
	}
      } 
    }

        log.debug("***answeritems.size "+answeredItems.size());
    return answeredItems;
  }

  public boolean hasRandomPart(PublishedAssessmentData pub){
    return PersistenceService.getInstance().
          getPublishedAssessmentFacadeQueries().hasRandomPart(pub.getPublishedAssessmentId());
  }

  public String getFirstItem(PublishedAssessmentData pub){
    PublishedItemData item = PersistenceService.getInstance().
          getPublishedAssessmentFacadeQueries().getFirstPublishedItem(pub.getPublishedAssessmentId());
    if (item!=null)
      return item.getItemId().toString();
    else
      return "";
  }

  public void getFilteredList(TotalScoresBean bean, ArrayList allscores,ArrayList scores, 
                              ArrayList students_not_submitted, Map useridMap){
    // only do section filter if it's published to authenticated users
    if (bean.getReleaseToAnonymous()){
    // skip section filter if it's published to anonymous users
      scores.addAll(allscores);
    }
/*
    else if ("true".equalsIgnoreCase(bean.getAnonymous())){
    // skip section filter if it's anonymous grading, SAK-4395 
      scores.addAll(allscores);
    }
*/
    else {
      // now we need filter by sections selected 
      ArrayList students_submitted= new ArrayList();  // arraylist of students submitted test
      Iterator allscores_iter = allscores.iterator();
      while (allscores_iter.hasNext())
      {
        AssessmentGradingData data = (AssessmentGradingData) allscores_iter.next();
        String agentid =  data.getAgentId();
        
        // get the Map of all users(keyed on userid) belong to the selected sections 
        // now we only include scores of users belong to the selected sections
        if (useridMap.containsKey(agentid)) {
          scores.add(data); // daisyf: #1b - what is the min set of info needed for data?
          students_submitted.add(agentid);
        }
      }

      // now get the list of students that have not submitted for grades 
      Iterator useridIterator = useridMap.keySet().iterator(); 
      while (useridIterator.hasNext()) {
        String userid = (String) useridIterator.next(); 	
        if (!students_submitted.contains(userid)) {
          students_not_submitted.add(userid);
        }
      }
    }
  }

  // This method also store all the submitted assessment grading for a given published
  // assessment in TotalScoresBean
  public void prepareAgentResultList(TotalScoresBean bean, PublishedAssessmentData p,
                 ArrayList scores, ArrayList students_not_submitted, Map useridMap){ 

    // get available sections 
    //String pulldownid = bean.getSelectedSectionFilterValue();

    // daisyf: #1a - place for optimization. all score contains full object of
    // AssessmentGradingData, do we need full?
    GradingService delegate = new GradingService();
    ArrayList allscores = bean.getAssessmentGradingList();
    if (allscores == null || allscores.size()==0){
      PublishedAccessControl ac = (PublishedAccessControl) p.getAssessmentAccessControl();
      if (ac.getUnlimitedSubmissions()!=null && !ac.getUnlimitedSubmissions().booleanValue()){
        if (ac.getSubmissionsAllowed().intValue() == 1) {
        	bean.setAllSubmissions(TotalScoresBean.LAST_SUBMISSION);
        	((QuestionScoresBean) ContextUtil.lookupBean("questionScores")).setAllSubmissions(TotalScoresBean.LAST_SUBMISSION);
            ((HistogramScoresBean) ContextUtil.lookupBean("histogramScores")).setAllSubmissions(TotalScoresBean.LAST_SUBMISSION);
        }
      }
      allscores = delegate.getTotalScores(p.getPublishedAssessmentId().toString(), bean.getAllSubmissions());
      bean.setAssessmentGradingList(allscores);
    }
    getFilteredList(bean, allscores, scores, students_not_submitted, useridMap);
    bean.setTotalPeople(scores.size()+"");
  }

  /* Dump the grading and agent information into AgentResults */
  public void prepareAgentResult(PublishedAssessmentData p, Iterator iter, ArrayList agents, Map userRoles){
	GradingService gradingService = new GradingService();
    while (iter.hasNext())
    {
      AgentResults results = new AgentResults();
      AssessmentGradingData gdata = (AssessmentGradingData) iter.next();
      
      // no need to initialize itemSet 'cos we don't need to use it in totalScoresPage. So I am
      // stuffing it with an empty HashSet - daisyf
      gdata.setItemGradingSet(gradingService.getItemGradingSet(gdata.getAssessmentGradingId().toString()));
      //gdata.setItemGradingSet(new HashSet());
      try{
        BeanUtils.copyProperties(results, gdata);
      }
      catch(Exception e){
        log.warn(e.getMessage());
      }

      results.setAssessmentGradingId(gdata.getAssessmentGradingId());
      if(gdata.getTotalAutoScore() != null)
        results.setTotalAutoScore(gdata.getTotalAutoScore().toString());
      else
        results.setTotalAutoScore("0.0");

      if(gdata.getTotalOverrideScore() != null)
        results.setTotalOverrideScore(gdata.getTotalOverrideScore().toString());
      else
        results.setTotalOverrideScore("0.0");

      if(gdata.getFinalScore() != null)
        results.setFinalScore(gdata.getFinalScore().toString());
      else
        results.setFinalScore("0.0");
      
      results.setComments(FormattedText.unEscapeHtml(gdata.getComments()));

      Iterator i3 = gdata.getItemGradingSet().iterator();
      Long typeId = new Long(-1);
      boolean autoGrade = true;
      // Go through all itemGrading data, if there is at least one cannot be auto graded,
      // ie, file upload, short answer/essay, or audio question,
      // we set the autoGrade to false
      // That is, this assessment grading record cannot be auto grade.
      // We update the status to 3.
      while (i3.hasNext())
      {
        ItemGradingData igd = (ItemGradingData) i3.next();
        typeId = gradingService.getTypeId(igd.getItemGradingId());
        if (typeId.equals(TypeIfc.FILE_UPLOAD) 
        		|| typeId.equals(TypeIfc.ESSAY_QUESTION) 
        		|| typeId.equals(TypeIfc.AUDIO_RECORDING)) {
        	autoGrade = false;
        	break;
        }        
      }
      
      // Update the status to AUTO_GRADED or NEED_HUMAN_ATTENTION only when the 
      // assessment has been submitted.
      // If the assessment has not yet been submitted but only updated by instructor, 
      // we leave the status as NO_SUBMISSION.
      // Note: I think "if(results.getForGrade())" is enough here for now. But I still 
      // put the second condition "!AssessmentGradingIfc.NO_SUBMISSION.equals(results.getStatus())"
      // to avoid breaking by new statuses created later.
      if (results.getForGrade() && !AssessmentGradingIfc.NO_SUBMISSION.equals(results.getStatus())) {
    	  if (autoGrade) {
    		  results.setStatus(AssessmentGradingIfc.AUTO_GRADED);
    	  }
    	  else {
    		  results.setStatus(AssessmentGradingIfc.NEED_HUMAN_ATTENTION);
    	  }
      }
      Date dueDate = null;
      PublishedAccessControl ac = (PublishedAccessControl) p.getAssessmentAccessControl();
      if (ac!=null)
        dueDate = ac.getDueDate();
      if (dueDate == null || gdata.getSubmittedDate() == null || gdata.getSubmittedDate().before(dueDate)) {   // SAK-5504
      //if (dueDate == null || gdata.getSubmittedDate().before(dueDate)) {
        results.setIsLate(Boolean.FALSE);
      }
      else {
        results.setIsLate(Boolean.TRUE);
        // The mock up has been updated. For a late submission, the "LATE" will be displayed
        // under Submission Date column instead of Status column. Therefore, we will not treat
        // LATE_SUBMISSION as a status. Comment out the following line for this reason.
        //results.setStatus(AssessmentGradingIfc.LATE_SUBMISSION);
      }
      AgentFacade agent = new AgentFacade(gdata.getAgentId());
      results.setLastName(agent.getLastName());
      results.setFirstName(agent.getFirstName());
      results.setEmail(agent.getEmail());
      if (results.getLastName() != null &&
        results.getLastName().length() > 0)
        results.setLastInitial(results.getLastName().substring(0,1));
      else if (results.getFirstName() != null &&
               results.getFirstName().length() > 0)
             results.setLastInitial(results.getFirstName().substring(0,1));
      else
        results.setLastInitial("Anonymous");
        results.setIdString(agent.getIdString());
        results.setAgentEid(agent.getEidString());
log.debug("testing agent getEid agent.getFirstname= " + agent.getFirstName());
log.debug("testing agent getEid agent.getid= " + agent.getIdString());
log.debug("testing agent getEid agent.geteid = " + agent.getEidString());
      results.setRole((String)userRoles.get(gdata.getAgentId()));
      agents.add(results);
    }
  }

  public ArrayList getAgentIds(Map useridMap){
    ArrayList agentUserIds = new ArrayList();
    Iterator iter = useridMap.keySet().iterator();
    while (iter.hasNext())
    {
      String userid = (String)iter.next();
      agentUserIds.add(userid);
    }
    return agentUserIds;
  }


  public void setRoleAndSortSelection(TotalScoresBean bean, ArrayList agents, boolean sortAscending){
    log.debug("TotalScoreListener: setRoleAndSortSection() starts");
	  if (ContextUtil.lookupParam("roleSelection") != null)
    {
      bean.setRoleSelection(ContextUtil.lookupParam("roleSelection"));
    }

    if (bean.getSortType() == null)
    {
      if (bean.getAnonymous().equals("true"))
      {
        bean.setSortType("totalAutoScore");
      }
      else
      {
        bean.setSortType("lastName");
      }
    }
 
    String sortProperty = bean.getSortType();
    //System.out.println("****Sort type is " + sortProperty);
    log.debug("TotalScoreListener: setRoleAndSortSection() :: sortProperty = " + sortProperty);
    
    bs = new BeanSort(agents, sortProperty);

    if ((sortProperty).equals("lastName")) bs.toStringSort();
    if ((sortProperty).equals("idString")) bs.toStringSort();
    if ((sortProperty).equals("agentEid")) bs.toStringSort();
    if ((sortProperty).equals("role")) bs.toStringSort();
    if ((sortProperty).equals("comments")) bs.toStringSort();
    if ((sortProperty).equals("submittedDate")) bs.toDateSort();
    if ((sortProperty).equals("assessmentGradingId")) bs.toNumericSort();
    if ((sortProperty).equals("status")) bs.toNumericSort();
    if ((sortProperty).equals("totalAutoScore")) bs.toNumericSort();
    if ((sortProperty).equals("totalOverrideScore")) bs.toNumericSort();
    if ((sortProperty).equals("finalScore")) bs.toNumericSort();

    if (sortAscending) {
    	log.debug("TotalScoreListener: setRoleAndSortSection() :: sortAscending");
    	agents = (ArrayList)bs.sort();
    }
    else {
    	log.debug("TotalScoreListener: setRoleAndSortSection() :: !sortAscending");
    	agents = (ArrayList)bs.sortDesc();
    }
  }

    public void setRecordingData(TotalScoresBean bean){
      // recordingData encapsulates the inbeanation needed for recording.
      // set recording agent, agent assessmentId,
      // set course_assignment_context value
      // set max tries (0=unlimited), and 30 seconds max length
      String courseContext = bean.getAssessmentName() + " total ";
      // Note this is HTTP-centric right now, we can't use in Faces
      //      AuthoringHelper authoringHelper = new AuthoringHelper();
      //      authoringHelper.getRemoteUserID() needs servlet stuff
      //      authoringHelper.getRemoteUserName() needs servlet stuff

      String userId = "";
      String userName = "";
      RecordingData recordingData =
        new RecordingData( userId, userName,
        courseContext, "0", "30");
      // set this value in the requestMap for sound recorder
      bean.setRecordingData(recordingData);

    }

  //add those students that have not submitted scores, need to display them 
  // in the UI as well SAK-2234
  // students_not_submitted
  public void prepareNotSubmittedAgentResult(Iterator notsubmitted_iter,
                                             ArrayList agents, Map userRoles){
	log.debug("TotalScoreListener: prepareNotSubmittedAgentResult starts");
    while (notsubmitted_iter.hasNext()){
      String studentid = (String) notsubmitted_iter.next();
      AgentResults results = new AgentResults();
      AgentFacade agent = new AgentFacade(studentid);
      results.setLastName(agent.getLastName());
      results.setFirstName(agent.getFirstName());
      results.setEmail(agent.getEmail());
      if (results.getLastName() != null &&
        results.getLastName().length() > 0)
      {
        results.setLastInitial(results.getLastName().substring(0,1));
      }
      else if (results.getFirstName() != null &&
               results.getFirstName().length() > 0)
      {
        results.setLastInitial(results.getFirstName().substring(0,1));
      }
      else
      {
        results.setLastInitial("Anonymous");
      }
      //results.setIdString(agent.getEidString());
      results.setIdString(agent.getIdString());
      results.setAgentEid(agent.getEidString());
      results.setRole((String)userRoles.get(studentid));
      // use -1 to indicate this is an unsubmitted agent
      results.setAssessmentGradingId(new Long(-1));
      results.setTotalAutoScore("0");
      results.setTotalOverrideScore("0");
      results.setSubmittedDate(null);
      results.setFinalScore("0");
      results.setComments("");
      results.setStatus(AssessmentGradingIfc.NO_SUBMISSION);  //  no submission
      agents.add(results);
    }
  }

  // build a Hashmap (Long itemId, ArrayList assessmentGradingIds)
  // containing the last/highest item submission 
  // (regardless of users who submitted it) of a given published assessment
  public HashMap getPublishedItemIdHash(PublishedAssessmentData pub){
    HashMap publishedItemIdHash;
    Integer scoringType = getScoringType(pub);
    if ((scoringType).equals(EvaluationModelIfc.HIGHEST_SCORE)){
	publishedItemIdHash = PersistenceService.getInstance().
            getAssessmentGradingFacadeQueries().
            getHighestAssessmentGradingByPublishedItem(
            pub.getPublishedAssessmentId());
    }
    else{
	publishedItemIdHash = PersistenceService.getInstance().
            getAssessmentGradingFacadeQueries().
            getLastAssessmentGradingByPublishedItem(
            pub.getPublishedAssessmentId());
    }
    return publishedItemIdHash;
  }
}
