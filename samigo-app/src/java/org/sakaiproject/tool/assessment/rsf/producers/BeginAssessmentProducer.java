/******************************************************************************
 * BeginAssessmentProducer.java
 * 
 * Copyright (c) 2006 Sakai Project/Sakai Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.tool.assessment.rsf.producers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.tool.assessment.data.dao.authz.AuthorizationData;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.rsf.params.BeginAssessmentViewParameters;
import org.sakaiproject.tool.assessment.services.PersistenceService;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.delivery.DeliveryBean;
import org.sakaiproject.tool.assessment.ui.bean.shared.PersonBean;
import org.sakaiproject.tool.assessment.ui.listener.delivery.BeginDeliveryActionListener;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.DynamicNavigationCaseReporter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * @author Joshua Ryan  josh@asu.edu   alt^I
 */
public class BeginAssessmentProducer implements ViewComponentProducer, DynamicNavigationCaseReporter, DefaultView, ViewParamsReporter {

  public HttpServletRequest httpServletRequest;
  public HttpServletResponse httpServletResponse;
  
  private static Log log = LogFactory.getLog(BeginAssessmentProducer.class);
  
	public static final String VIEW_ID = "BeginTakingAssessment";
	public String getViewID() {
		return VIEW_ID;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
    
    BeginAssessmentViewParameters params = null;
    if (viewparams != null)
      params = (BeginAssessmentViewParameters) viewparams;
    else
      System.out.println("Something bad... we have no viewparams");
    
    String alias = params.pubId;
    
    //Begin cut and past (with small deviations) from existing LoginServlet that currently does the job of url aliased assessment delivery in Samigo.
    //Much of this could/should be changed as there are easier/cleaner ways of doing some of this in RSF
    
    HttpSession httpSession = httpServletRequest.getSession(true);
    httpSession.setMaxInactiveInterval(3600); // one hour
    PersonBean person = (PersonBean) ContextUtil.lookupBeanFromExternalServlet("person", httpServletRequest, httpServletResponse);
    // we are going to use the delivery bean to flag that this access is via url
    // this is the flag that we will use in deliverAssessment.jsp to decide what
    // button to display - daisyf
    DeliveryBean delivery = (DeliveryBean) ContextUtil.lookupBeanFromExternalServlet("delivery", httpServletRequest, httpServletResponse);
    // For SAK-7132. 
    // As this class is only used for taking assessment via URL, 
    // there should not be any assessment grading data at this point
    delivery.setAssessmentGrading(null);
    delivery.setActionString("takeAssessmentViaUrl");

    // reset timer in case this is a timed assessment
    delivery.setTimeElapse("0");
    delivery.setLastTimer(0);
    delivery.setTimeLimit("0");
    delivery.setBeginAssessment(true);

    // set path
    delivery.setContextPath(httpServletRequest.getContextPath());

    // 1. get publishedAssessment and check if anonymous is allowed
    // 2. If so, goto welcome.faces
    // 3. If not, goto login.faces
    // both pages will set agentId and then direct user to BeginAssessment
    PublishedAssessmentService service = new PublishedAssessmentService();
    PublishedAssessmentFacade pub = service.getPublishedAssessmentIdByAlias(alias);

    delivery.setAssessmentId(pub.getPublishedAssessmentId().toString());
    delivery.setAssessmentTitle(pub.getTitle());
    delivery.setPublishedAssessment(pub);

    String path = null;

    String agentIdString = "";
    boolean isAuthorized = false;
    boolean isAuthenticated = false;

    // Determine if assessment accept Anonymous Users. If so, starting in version 2.0.1
    // all users will be authenticated as anonymous for the assessment in this case.
    //boolean anonymousAllowed = false;
    String releaseTo = pub.getAssessmentAccessControl().getReleaseTo();
    if (releaseTo != null && releaseTo.indexOf("Anonymous Users")> -1){
      //anonymousAllowed = true;
      agentIdString = AgentFacade.createAnonymous();
      isAuthenticated = true;
      isAuthorized = true;
      delivery.setAnonymousLogin(true);
      person.setAnonymousId(agentIdString);
    }
    else { // check membership
      agentIdString = httpServletRequest.getRemoteUser();
      isAuthenticated = ( agentIdString!= null && !("").equals(agentIdString));
      if (isAuthenticated){
        isAuthorized = checkMembership(pub);
        // in 2.2, agentId is differnt from httpServletRequest.getRemoteUser()
        agentIdString = AgentFacade.getAgentString();
      }
    }

    log.debug("*** agentIdString: "+agentIdString);

    // check if assessment is available
    // We are getting the total no. of submission (for grade) per assessment
    // by the given agent at the same time
    boolean assessmentIsAvailable = assessmentIsAvailable(service, agentIdString, pub,
                                                          delivery);
    if (isAuthorized){
      if (!assessmentIsAvailable) {
        UIBranchContainer assessmentNotAvailable = UIBranchContainer.make(tofill, "assessmentNotAvailable:");
        // TODO added info on why
      }
      else {
        // if assessment is available, set it in delivery bean for display in deliverAssessment.jsp
        BeginDeliveryActionListener listener = new BeginDeliveryActionListener();
        listener.processAction(null);

        UIForm form = UIForm.make(tofill, "takeAssessmentForm:");
        //UIOutput.make(form, "assessmentTitle", delivery.getAssessmentTitle());
        UIOutput.make(form, "courseName", delivery.getCourseName());
        UIOutput.make(form, "creatorName", delivery.getCreatorName());
        UIOutput.make(form, "assessmentTitle", delivery.getAssessmentTitle());
        //UIOutput.make(form, "maxAttempts", delivery.getSettings().getMaxAttempts() + "");
        UIOutput.make(form, "dueDate", delivery.getDueDateString());
        UIOutput.make(form, "feedback", delivery.getFeedback());
        
        UIBranchContainer timeLimit = UIBranchContainer.make(form, "timeLimit:");
        UIOutput.make(timeLimit, "timeLimit", delivery.getTimeLimit());
        UICommand.make(form, "beginAssessment", "#{beginAssessmentDeliveryBean.startAssessment}");

      }
    }
    else{ // notAuthorized
      if (!isAuthenticated){
        if (AgentFacade.isStandaloneEnvironment()) {
          delivery.setActionString(null);
          path = delivery.getPortal();
        }
        else{
          delivery.setActionString(null);
          try {
            path = "/authn/login?url=" + URLEncoder.encode(httpServletRequest.getRequestURL().toString()+"?pubId="+alias, "UTF-8");          
          }
          catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        log.info("** servlet path="+httpServletRequest.getRequestURL().toString());
        String url = httpServletRequest.getRequestURL().toString();
        String context = httpServletRequest.getContextPath();
        String finalUrl = url.substring(0,url.lastIndexOf(context))+path;
        log.info("**** finalUrl = "+finalUrl);
        try {
          httpServletResponse.sendRedirect(finalUrl);
        }
        catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      else { //isAuthenticated but not authorized
        UIBranchContainer assessmentNotAvailable = UIBranchContainer.make(tofill, "accessDenied:");
        // TODO added info on why
      }
    }
 
    //End cut and paste from LoginServlet (with small deviations)

	}

    private boolean checkMembership(PublishedAssessmentFacade pub){
      boolean isMember=false;
      // get list of site that this published assessment has been released to
      List l =PersistenceService.getInstance().getAuthzQueriesFacade().
      getAuthorizationByFunctionAndQualifier("VIEW_PUBLISHED_ASSESSMENT",
                                             pub.getPublishedAssessmentId().toString());
      for (int i=0;i<l.size();i++){
        String siteId = ((AuthorizationData)l.get(i)).getAgentIdString();
        isMember = PersistenceService.getInstance().getAuthzQueriesFacade().
        checkMembership(siteId);
        if (isMember)
          break;
      }
      return isMember;
    }

    // check if assessment is available based on criteria like
    // dueDate
    public boolean assessmentIsAvailable(PublishedAssessmentService service,
                                         String agentIdString, PublishedAssessmentFacade pub,
                                         DeliveryBean delivery){
      boolean assessmentIsAvailable = false;
      String nextAction = delivery.checkBeforeProceed();
      log.debug("nextAction="+nextAction);
      if (("safeToProceed").equals(nextAction)){
        assessmentIsAvailable = true;
      }
      return assessmentIsAvailable;
    }

    public ViewParameters getViewParameters() {
      return new BeginAssessmentViewParameters();
    }

    public List reportNavigationCases() {
      List togo = new ArrayList();
      togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
      togo.add(new NavigationCase("takeAssessment", new RawViewParameters("/samigo/jsf/delivery/deliverAssessment.faces")));
      
      return togo;      
      
    }
}
