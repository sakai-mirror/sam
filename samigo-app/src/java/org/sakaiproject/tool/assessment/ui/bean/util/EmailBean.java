/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/evaluation/TotalScoresBean.java $
 * $Id: EmailBean.java 18134 2006-11-14 18:59:25Z jholtzman@berkeley.edu $
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

package org.sakaiproject.tool.assessment.ui.bean.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.impl.ReferenceComponent;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.ServerOverloadException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.tool.assessment.data.dao.assessment.AttachmentData;
import org.sakaiproject.tool.assessment.data.dao.assessment.SectionAttachment;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionDataIfc;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.SectionBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.util.SamigoEmailService;
import org.sakaiproject.tool.cover.SessionManager;

public class EmailBean implements Serializable {
	private static Log log = LogFactory.getLog(EmailBean.class);

	private String fromName;

	private String fromEmailAddress;

	private String toName;

	private String toEmailAddress;

	private String toFirstName;

	private String assessmentName;

	private String subject;

	private String ccMe = "no";

	private String message;

	private List attachmentList;

	private boolean hasAttachment = false;
	
	private String outcome;
	
	private HashMap resourceHash = new HashMap();

	/**
	 * Creates a new TotalScoresBean object.
	 */
	public EmailBean() {
		log.debug("Creating a new EmailBean");
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}

	public String getToFirstName() {
		return toFirstName;
	}

	public void setToFirstName(String toFirstName) {
		this.toFirstName = toFirstName;
	}

	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCcMe() {
		return ccMe;
	}

	public void setCcMe(String ccMe) {
		this.ccMe = ccMe;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageTemplate() {
		StringBuffer sb = new StringBuffer(ContextUtil.getLocalizedString(
				"org.sakaiproject.tool.assessment.bundle.EvaluationMessages",
				"dear"));
		sb.append(" ");
		sb.append(toFirstName);
		sb.append(ContextUtil.getLocalizedString(
				"org.sakaiproject.tool.assessment.bundle.EvaluationMessages",
				"comma"));
		sb.append("<br/>");
		sb.append(ContextUtil.getLocalizedString(
				"org.sakaiproject.tool.assessment.bundle.EvaluationMessages",
				"regarding"));
		sb.append(" ");
		sb.append(assessmentName);
		this.message = sb.toString();
	}

	public List getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean getHasAttachment() {
		return this.hasAttachment;
	}

	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public void prepareAttachment() {
		ToolSession session = SessionManager.getCurrentToolSession();
		ArrayList newAttachmentList = new ArrayList();
		if (session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) {
			List refs = (List) session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
			Reference ref;
			if (refs.size() == 0) {
				hasAttachment = false;
			}
			else {
				HashMap map = getResourceIdHash(attachmentList);
				for (int i = 0; i < refs.size(); i++) {
					ref = (Reference) refs.get(i);
					String resourceId = ref.getId();
			        if (map.get(resourceId) == null) {
			        	AssessmentService assessmentService = new AssessmentService();
			        	AttachmentData attach = assessmentService.createEmailAttachment(
			        			resourceId, 
			        			ref.getProperties().getProperty(ref.getProperties().getNamePropDisplayName()),
			        			ContextUtil.getProtocol());
				
			        	newAttachmentList.add(attach);
			        	
			        }
			        else {
			        	newAttachmentList.add((AttachmentData)map.get(resourceId));
			            map.remove(resourceId);
			        }
			        
			        hasAttachment = true;
				}
			}
			session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
			session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
		}
		else {
			hasAttachment = false;
		}
		attachmentList = newAttachmentList;
	}


	private HashMap getResourceIdHash(List attachmentList) {
		HashMap map = new HashMap();
		if (attachmentList != null) {
			Iterator iter = attachmentList.iterator();
			while (iter.hasNext()) {
				AttachmentData attach = (AttachmentData) iter.next();
				map.put(attach.getResourceId(), attach);
			}
		}
		return map;
	}
	  
	public String addAttachmentsRedirect() {
	    try	{
	      List filePickerList = new ArrayList();
	      if (attachmentList != null){
	        filePickerList = prepareReferenceList(attachmentList);
	      }
	      log.debug("**filePicker list="+filePickerList.size());
	      ToolSession currentToolSession = SessionManager.getCurrentToolSession();
	      currentToolSession.setAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS, filePickerList);
	      ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	      context.redirect("sakai.filepicker.helper/tool");
	    }
	    catch(Exception e){
	      log.error("fail to redirect to attachment page: " + e.getMessage());
	    }
	    return "email";
	}
	
	private List prepareReferenceList(List attachmentList) {
		List list = new ArrayList();
		for (int i = 0; i < attachmentList.size(); i++) {
			ContentResource cr = null;
			AttachmentIfc attach = (AttachmentIfc) attachmentList.get(i);
			try {
				cr = ContentHostingService.getResource(attach.getResourceId());
			} catch (PermissionException e) {
				log.warn("PermissionException from ContentHostingService:"
						+ e.getMessage());
			} catch (IdUnusedException e) {
				log.warn("IdUnusedException from ContentHostingService:"
						+ e.getMessage());
				// <-- bad sign, some left over association of part and
				// resource,
				// use case: user remove resource in file picker, then exit
				// modification without
				// proper cancellation by clicking at the left nav instead of
				// "cancel".
				// Also in this use case, any added resource would be left
				// orphan.
				//AssessmentService assessmentService = new AssessmentService();
				//assessmentService.removeSectionAttachment(attach.getAttachmentId().toString());
			} catch (TypeException e) {
				log.warn("TypeException from ContentHostingService:"
						+ e.getMessage());
			}
			if (cr != null) {
				if (this.resourceHash == null) {
					this.resourceHash = new HashMap();
				}
				this.resourceHash.put(attach.getResourceId(), cr);
				ReferenceComponent ref = new ReferenceComponent(cr.getReference());
				if (ref != null) {
					list.add(ref);
				}
			}
		}
		return list;
	}
	
	public HashMap getResourceHash() {
		return resourceHash;
	}

	public void setResourceHash(HashMap resourceHash)
	{
		this.resourceHash = resourceHash;
	}
	  
	public String send() {
		log.debug("send()");
		log.debug("fromName: " + fromName);
		log.debug("fromEmailAddress: " + fromEmailAddress);
		log.debug("toName: " + toName);
		log.debug("toEmailAddress: " + toEmailAddress);
		log.debug("subject: " + subject);
		log.debug("ccMe: " + ccMe);
		log.debug("message: " + message);

		SamigoEmailService samigoEmailService = new SamigoEmailService(
				fromName, fromEmailAddress, toName, toEmailAddress, ccMe,
				subject, message);
		
		String result = "error";
		try {
			result = samigoEmailService.send();
		} catch (UnsupportedEncodingException e) {
			log.error("Exception throws from send()" + e);
		} catch (MessagingException e) {
			log.error("Exception throws from send()" + e);
		} catch (ServerOverloadException e) {
			log.error("Exception throws from send()" + e);
		} catch (PermissionException e) {
			log.error("Exception throws from send()" + e);
		} catch (IdUnusedException e) {
			log.error("Exception throws from send()" + e);
		} catch (TypeException e) {
			log.error("Exception throws from send()" + e);
		} catch (IOException e) {
			log.error("Exception throws from send()" + e);
		}
		
		if (result.equals("send")) {
			String msgSent = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages","sent_email_confirmation");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(msgSent));
			
			return "confirmEmailSent";
		}
		else {
			String msgError = ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages","sent_email_error");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(msgError));
			
			return "email";
		}
	}

	public void cancel() {
		log.debug("cancel");
		// num_files=0;
		// attachedFiles.clear();
	}
	
}
