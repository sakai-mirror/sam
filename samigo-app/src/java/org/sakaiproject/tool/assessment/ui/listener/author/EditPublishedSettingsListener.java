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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentMetaDataIfc;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AuthorBean;
import org.sakaiproject.tool.assessment.ui.bean.author.PublishedAssessmentSettingsBean;
import org.sakaiproject.tool.assessment.ui.bean.authz.AuthorizationBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.util.FormattedText;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * <p>Copyright: Copyright (c) 2004 Sakai Project</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $Id$
 */

public class EditPublishedSettingsListener
    implements ActionListener
{
  //private static Log log = LogFactory.getLog(EditPublishedSettingsListener.class);

  public EditPublishedSettingsListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException
  {
    FacesContext context = FacesContext.getCurrentInstance();
    //log.info("**debugging ActionEvent: " + ae);
    //log.info("**debug requestParams: " + requestParams);
    //log.info("**debug reqMap: " + reqMap);

    PublishedAssessmentSettingsBean assessmentSettings = (PublishedAssessmentSettingsBean) ContextUtil.lookupBean(
                                          "publishedSettings");
    // #1a - load the assessment
    String assessmentId = (String) FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap().get("publishedAssessmentId");
    PublishedAssessmentService assessmentService = new PublishedAssessmentService();
    PublishedAssessmentFacade assessment = assessmentService.getSettingsOfPublishedAssessment(
        assessmentId);

    //## - permission checking before proceeding - daisyf
    AuthorBean author = (AuthorBean) ContextUtil.lookupBean("author");
    author.setOutcome("editPublishedAssessmentSettings");
    if (!passAuthz(context, assessment.getCreatedBy())){
      author.setOutcome("author");
      return;
    }
    assessmentSettings.setAssessment(assessment);
    assessmentSettings.setDisplayFormat(ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.GeneralMessages","output_data_picker_w_sec"));
    assessmentSettings.resetIsValidDate();
    assessmentSettings.resetOriginalDateString();
    
    // To unEscapeHtml for the fields that have been through ContextUtil.processFormattedText
    assessmentSettings.setTitle(ContextUtil.unEscapeHtml(assessment.getTitle()));
    assessmentSettings.setAuthors(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.AUTHORS)));
    assessmentSettings.setFinalPageUrl(ContextUtil.unEscapeHtml(assessment.getAssessmentAccessControl().getFinalPageUrl()));
    assessmentSettings.setBgColor(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.BGCOLOR)));
    assessmentSettings.setBgImage(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.BGIMAGE)));
    assessmentSettings.setKeywords(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.KEYWORDS)));
    assessmentSettings.setObjectives(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.OBJECTIVES)));
    assessmentSettings.setRubrics(ContextUtil.unEscapeHtml(assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.RUBRICS)));
    
  }

  public boolean passAuthz(FacesContext context, String ownerId){
    AuthorizationBean authzBean = (AuthorizationBean) ContextUtil.lookupBean("authorization");
    boolean hasPrivilege_any = authzBean.getPublishAnyAssessment();
    boolean hasPrivilege_own0 = authzBean.getPublishOwnAssessment();
    boolean hasPrivilege_own = (hasPrivilege_own0 && isOwner(ownerId));
    boolean hasPrivilege = (hasPrivilege_any || hasPrivilege_own);
    if (!hasPrivilege){
      String err=(String)ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages",
                                               "denied_edit_publish_assessment_settings_error");
      context.addMessage(null,new FacesMessage(err));
    }
    return hasPrivilege;
  }

  public boolean isOwner(String ownerId){
    boolean isOwner = false;
    String agentId = AgentFacade.getAgentString();
    isOwner = agentId.equals(ownerId);
    return isOwner;
  }

}
