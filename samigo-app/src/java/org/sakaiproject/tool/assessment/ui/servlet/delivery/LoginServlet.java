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



package org.sakaiproject.tool.assessment.ui.servlet.delivery;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.data.dao.authz.AuthorizationData;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.services.PersistenceService;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.delivery.DeliveryBean;
import org.sakaiproject.tool.assessment.ui.bean.shared.PersonBean;
import org.sakaiproject.tool.assessment.ui.listener.delivery.BeginDeliveryActionListener;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * <p>Copyright: Copyright (c) 2004 Sakai Project</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $Id$
 */

public class LoginServlet
    extends HttpServlet
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -5495078878170443939L;
private static Log log = LogFactory.getLog(LoginServlet.class);

  public LoginServlet()
  {
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException
  {
    doPost(req,res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException
  {
    HttpSession httpSession = req.getSession(true);
    httpSession.setMaxInactiveInterval(3600); // one hour
    PersonBean person = (PersonBean) ContextUtil.lookupBeanFromExternalServlet(
                        "person", req, res);
    // we are going to use the delivery bean to flag that this access is via url
    // this is the flag that we will use in deliverAssessment.jsp to decide what
    // button to display - daisyf
    DeliveryBean delivery = (DeliveryBean) ContextUtil.lookupBeanFromExternalServlet(
       "delivery", req, res);
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
    delivery.setContextPath(req.getContextPath());

    String alias = req.getParameter("id");
    // 1. get publishedAssessment and check if anonymous is allowed
    // 2. If so, goto welcome.faces
    // 3. If not, goto login.faces
    // both pages will set agentId and then direct user to BeginAssessment
    PublishedAssessmentService service = new PublishedAssessmentService();
    PublishedAssessmentFacade pub = service.getPublishedAssessmentIdByAlias(alias);

    delivery.setAssessmentId(pub.getPublishedAssessmentId().toString());
    delivery.setAssessmentTitle(pub.getTitle());
    delivery.setPublishedAssessment(pub);

    RequestDispatcher dispatcher = null;
    String path = "/jsf/delivery/invalidAssessment.faces";
    boolean relativePath = true;

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
	agentIdString = req.getRemoteUser();
        isAuthenticated = ( agentIdString!= null && !("").equals(agentIdString));
        if (isAuthenticated){
          isAuthorized = checkMembership(pub, req, res);
          // in 2.2, agentId is differnt from req.getRemoteUser()
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
          path = "/jsf/delivery/assessmentNotAvailable.faces";
        }
        else {
          // if assessment is available, set it in delivery bean for display in deliverAssessment.jsp
          BeginDeliveryActionListener listener = new BeginDeliveryActionListener();
          listener.processAction(null);
          path = "/jsf/delivery/beginTakingAssessment_viaurl.faces";
        }
      }
      else{ // notAuthorized
        if (!isAuthenticated){
          if (AgentFacade.isStandaloneEnvironment()) {
        	  delivery.setActionString(null);
        	  path = "/jsf/delivery/login.faces";
          }
          else{
            relativePath = false;
            delivery.setActionString(null);
            path = "/authn/login?url=" + URLEncoder.encode(req.getRequestURL().toString()+"?id="+alias, "UTF-8");
	  }
        }
        else { //isAuthenticated but not authorized
          path = "/jsf/delivery/accessDenied.faces";
        }
      }

    log.debug("***path"+path);
    if (relativePath){
      dispatcher = req.getRequestDispatcher(path);
      dispatcher.forward(req, res);
    }
    else{
      log.info("** servlet path="+req.getRequestURL().toString());
      String url = req.getRequestURL().toString();
      String context = req.getContextPath();
      String finalUrl = url.substring(0,url.lastIndexOf(context))+path;
      log.info("**** finalUrl = "+finalUrl);
      res.sendRedirect(finalUrl);
    }
  }

  private boolean checkMembership(PublishedAssessmentFacade pub,
       HttpServletRequest req, HttpServletResponse res){
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
}
