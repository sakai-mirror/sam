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
import org.sakaiproject.tool.assessment.facade.AssessmentFacadeQueries;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacadeQueries;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AuthorBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>Description: Listener for the Assessment page</p>
 */

public class AssessmentListener 
    implements ActionListener
{
  private static Log log = LogFactory.getLog(TemplateListener.class);
  private static ContextUtil cu;

  public AssessmentListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException
  {
	  AuthorBean author = (AuthorBean) cu.lookupBean("author");

	  // prepare core assessment list
	  AssessmentService assessmentService = new AssessmentService();
	  ArrayList assessmentList = assessmentService.getBasicInfoOfAllActiveAssessments(AssessmentFacadeQueries.TITLE, author.isCoreAscending());
	  author.setAssessments(assessmentList);
	  log.debug("core assessmen list size = " + assessmentList.size());
        
	  // prepare published assessment list
	  PublishedAssessmentService publishedAssessmentService = new PublishedAssessmentService();
	  ArrayList publishedList = publishedAssessmentService.getBasicInfoOfAllActivePublishedAssessments(PublishedAssessmentFacadeQueries.TITLE,true);
	  author.setPublishedAssessments(publishedList);
	  log.debug("published assessment list size = " + publishedList.size());
	  
	  // prepare published inactive assessment list
	  ArrayList inactivePublishedList = publishedAssessmentService.getBasicInfoOfAllInActivePublishedAssessments(PublishedAssessmentFacadeQueries.TITLE,true);
	  author.setInactivePublishedAssessments(inactivePublishedList);
	  log.debug("published inactive assessment list size = " + inactivePublishedList.size());
  }
}
