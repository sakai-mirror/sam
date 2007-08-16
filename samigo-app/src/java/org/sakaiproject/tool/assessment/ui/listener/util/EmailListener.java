/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/evaluation/TotalScoreListener.java $
 * $Id: EmailListener.java 17372 2006-10-25 02:27:49Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006 The Sakai Foundation.
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

package org.sakaiproject.tool.assessment.ui.listener.util;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.TotalScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.util.EmailBean;

public class EmailListener implements ActionListener {
	private static Log log = LogFactory.getLog(EmailListener.class);

	/**
	 * Standard process action method.
	 * 
	 * @param ae
	 *            ActionEvent
	 * @throws AbortProcessingException
	 */
	public void processAction(ActionEvent ae) throws AbortProcessingException {
		log.debug("Email Action Listener.");
		EmailBean emailBean = (EmailBean) ContextUtil.lookupBean("email");
		TotalScoresBean totalScoreBean = (TotalScoresBean) ContextUtil.lookupBean("totalScores");
			
		emailBean.setAttachmentList(null);
		emailBean.setHasAttachment(false);
			
		// From Name
		AgentFacade agent = new AgentFacade();
		StringBuffer sb = new StringBuffer(agent.getFirstName());
		sb.append(" ");
		sb.append(agent.getLastName());
		emailBean.setFromName(sb.toString());
		
		// To
		String toUserId = ContextUtil.lookupParam("toUserId");
		agent = new AgentFacade(toUserId);
		String toFirstName = agent.getFirstName();
		String toName = toFirstName + " " + agent.getLastName();
		String toEmailAddress = agent.getEmail();
		emailBean.setToFirstName(toFirstName);
		emailBean.setToName(toName);
		emailBean.setToEmailAddress(toEmailAddress);
			
		// Cc
		emailBean.setCcMe("no");
			
		// AssessmentName
		emailBean.setAssessmentName(totalScoreBean.getAssessmentName());
			
		// Subject
		sb = new StringBuffer(totalScoreBean.getAssessmentName());
		sb.append(" ");
		sb.append(ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "feedback"));
		emailBean.setSubject(sb.toString());
			
		// Message
		emailBean.setMessageTemplate();
	}
}
