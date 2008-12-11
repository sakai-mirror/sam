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



package org.sakaiproject.tool.assessment.ui.listener.author;

import java.util.ArrayList;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.service.gradebook.shared.GradebookService;
import org.sakaiproject.spring.SpringBeanLocator;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedAssessmentData;
import org.sakaiproject.tool.assessment.facade.GradebookFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.integration.context.IntegrationContextFactory;
import org.sakaiproject.tool.assessment.integration.helper.ifc.GradebookServiceHelper;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AuthorBean;
import org.sakaiproject.tool.assessment.ui.bean.author.PublishedAssessmentBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * <p>Copyright: Copyright (c) 2004 Sakai Project</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $Id$
 */

public class RemovePublishedAssessmentListener
    implements ActionListener
{
  private static Log log = LogFactory.getLog(RemovePublishedAssessmentListener.class);
  private static final GradebookServiceHelper gbsHelper =
		IntegrationContextFactory.getInstance().getGradebookServiceHelper();
  private static final boolean integrated =
		IntegrationContextFactory.getInstance().isIntegrated();

  public RemovePublishedAssessmentListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException
  {
    PublishedAssessmentBean pulishedAssessment = (PublishedAssessmentBean) ContextUtil.lookupBean("publishedassessment");
    String assessmentId = pulishedAssessment.getAssessmentId();
    if (assessmentId != null)
    {
      log.debug("assessmentId = " + assessmentId); 	    
      PublishedAssessmentService assessmentService = new PublishedAssessmentService();
      assessmentService.removeAssessment(assessmentId, "remove");
      removeFromGradebook(assessmentId);
          
      AuthorBean author = (AuthorBean) ContextUtil.lookupBean("author");
      ArrayList publishedAssessmentList = author.getPublishedAssessments();
      ArrayList list = new ArrayList();
      for (int i=0; i<publishedAssessmentList.size();i++){
    	  PublishedAssessmentFacade pa = (PublishedAssessmentFacade) publishedAssessmentList.get(i);
        if (!(assessmentId).equals(pa.getPublishedAssessmentId().toString())) {
          list.add(pa);
        }
      }
      author.setPublishedAssessments(list);
      
      ArrayList inactivePublishedAssessmentList = author.getInactivePublishedAssessments();
      ArrayList inactiveList = new ArrayList();
      for (int i=0; i<inactivePublishedAssessmentList.size();i++){
    	  PublishedAssessmentFacade pa = (PublishedAssessmentFacade) inactivePublishedAssessmentList.get(i);
        if (!(assessmentId).equals(pa.getPublishedAssessmentId().toString())) {
        	inactiveList.add(pa);
        }
      }
      author.setInactivePublishedAssessments(inactiveList);
    }
    else {
    	log.warn("Could not remove published assessment - assessment id is null");
    }
  }
  
  private void removeFromGradebook(String assessmentId) {
	  GradebookService g = null;
	  if (integrated)
	  {
		  g = (GradebookService) SpringBeanLocator.getInstance().
		  getBean("org.sakaiproject.service.gradebook.GradebookService");
	  }
	  try {
		  log.debug("before gbsHelper.removeGradebook()");
		  gbsHelper.removeExternalAssessment(GradebookFacade.getGradebookUId(), assessmentId, g);
	  } catch (Exception e1) {
		  // Should be the external assessment doesn't exist in GB. So we quiet swallow the exception. Please check the log for the actual error.
		  log.info("Exception thrown in updateGB():" + e1.getMessage());
	  }
  }
}
