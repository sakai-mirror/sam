/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/EditPartListener.java $
 * $Id: EditPartListener.java 9268 2006-05-10 21:27:24Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 Sakai Foundation
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



package org.sakaiproject.tool.assessment.ui.listener.author;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.facade.AssessmentFacade;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AssessmentSettingsBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.entity.api.Reference;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * @version $Id: EditPartListener.java 9268 2006-05-10 21:27:24Z daisyf@stanford.edu $
 */

public class SaveAssessmentAttachmentListener
    implements ActionListener
{
  private static Log log = LogFactory.getLog(SaveAssessmentAttachmentListener.class);

  public SaveAssessmentAttachmentListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException {
    AssessmentSettingsBean assessmentSettings = (AssessmentSettingsBean) ContextUtil.lookupBean("assessmentSettings");

    // attach item attachemnt to assessmentBean
    List attachmentList = prepareAssessmentAttachment(assessmentSettings);
    assessmentSettings.setAttachmentList(attachmentList);
  }

  private HashMap getResourceIdHash(Set attachmentSet){
    HashMap map = new HashMap();
    if (attachmentSet !=null ){
      Iterator iter = attachmentSet.iterator();
      while (iter.hasNext()){
        AssessmentAttachmentIfc attach = (AssessmentAttachmentIfc) iter.next();
        map.put(attach.getResourceId(), attach);
      }
    }
    return map;
  }

  private List prepareAssessmentAttachment(AssessmentSettingsBean bean){
    AssessmentFacade assessment = null;
    // assessment == null => assessment does not exist yet
    if (bean.getAssessment() != null){
	assessment = bean.getAssessment();
    }

    ToolSession session = SessionManager.getCurrentToolSession();
    if (session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) {
      
      Set attachmentSet = new HashSet();
      if (assessment!=null){
        attachmentSet = assessment.getAssessmentAttachmentSet();
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
            AssessmentAttachmentIfc newAttach = assessmentService.createAssessmentAttachment(
                                                (AssessmentIfc)assessment.getData(),
                                                ref.getId(), ref.getProperties().getProperty(
                                                ref.getProperties().getNamePropDisplayName()),
                                                protocol);
            newAttachmentList.add(newAttach);
	  }
          else{ 
            // attachment already exist, let's add it to new list and
	    // check it off from map
            newAttachmentList.add((AssessmentAttachmentIfc)map.get(resourceId));
            map.remove(resourceId);
	  }
        }
      }

      session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
      session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
      return newAttachmentList;
    }
    return new ArrayList();
  }

 }

