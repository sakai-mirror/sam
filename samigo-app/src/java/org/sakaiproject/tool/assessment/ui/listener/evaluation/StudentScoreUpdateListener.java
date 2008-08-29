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
import java.util.HashSet;
import java.util.Iterator;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.event.cover.EventTrackingService;
import org.sakaiproject.tool.assessment.data.dao.grading.AssessmentGradingData;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.services.GradingService;
import org.sakaiproject.tool.assessment.services.GradebookServiceException;
import org.sakaiproject.tool.assessment.ui.bean.delivery.DeliveryBean;
import org.sakaiproject.tool.assessment.ui.bean.delivery.ItemContentsBean;
import org.sakaiproject.tool.assessment.ui.bean.delivery.SectionContentsBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.StudentScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.TotalScoresBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>
 * This handles the updating of the Student Score page.
 *  </p>
 * <p>Description: Action Listener Evaluation Updating Student Score page</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Rachel Gollub
 * @version $Id$
 */

public class StudentScoreUpdateListener
  implements ActionListener
{
  private static Log log = LogFactory.getLog(StudentScoreUpdateListener.class);
  private static ContextUtil cu;

  /**
   * Standard process action method.
   * @param ae ActionEvent
   * @throws AbortProcessingException
   */
  public void processAction(ActionEvent ae) throws
    AbortProcessingException
  {
    log.debug("Student Score Update LISTENER.");
    StudentScoresBean bean = (StudentScoresBean) cu.lookupBean("studentScores");
    TotalScoresBean tbean = (TotalScoresBean) cu.lookupBean("totalScores");
    tbean.setAssessmentGradingHash(tbean.getPublishedAssessment().getPublishedAssessmentId());
    DeliveryBean delivery = (DeliveryBean) cu.lookupBean("delivery");
    log.debug("Calling saveStudentScores.");
    try {
      if (!saveStudentScores(bean, tbean, delivery))
      {
        throw new RuntimeException("failed to call saveStudentScores.");
      }
    } catch (GradebookServiceException ge) {
       FacesContext context = FacesContext.getCurrentInstance();
       String err=(String)cu.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages", "gradebook_exception_error");
       context.addMessage(null, new FacesMessage(err));

    }

  }

  /**
   * Persist the results from the ActionForm in the student page.
   * @param bean StudentScoresBean bean
   * @return true if successful
   */
    public boolean saveStudentScores(StudentScoresBean bean, TotalScoresBean tbean,
                                   DeliveryBean delivery)
  {
    GradingService delegate = new GradingService();
    HashSet itemGradingSet = new HashSet();
    AssessmentGradingData adata = null;
    try
    {
      ArrayList parts = delivery.getPageContents().getPartsContents();
      Iterator iter = parts.iterator();
      boolean updateFlag = false;
      while (iter.hasNext())
      {
        ArrayList items = ((SectionContentsBean) iter.next()).getItemContents();
        Iterator iter2 = items.iterator();
        while (iter2.hasNext())
        {
          ItemContentsBean question = (ItemContentsBean) iter2.next();
          ArrayList gradingarray = question.getItemGradingDataArray();
          log.debug("****1. pub questionId = " + question.getItemData().getItemId());
          log.debug("****2. Gradingarray length = " + gradingarray.size());
          // Create a new one if we need it.
          if (gradingarray.isEmpty() && (question.getExactPoints() > 0  ||
              (question.getGradingComment() != null &&
               !question.getGradingComment().trim().equals("")) ))
          {
            // this is another mystery, no idea why review is involved here - daiyf
            question.setReview(false); // This creates an itemgradingdata
            gradingarray = question.getItemGradingDataArray();
          }

          log.debug("****3a Gradingarray length2 = " + gradingarray.size());
          log.debug("****3b set points = " + question.getExactPoints() + ", comments to " + question.getGradingComment());
          Iterator iter3 = gradingarray.iterator();
          while (iter3.hasNext())
          {
            ItemGradingData data = (ItemGradingData) iter3.next();
            if (adata == null && data.getAssessmentGradingId() != null){
              adata = delegate.load(data.getAssessmentGradingId().toString());
            }
            if (data.getAgentId() == null)
            { // this is a skipped question, set submittedDate=null
              data.setSubmittedDate(null);
              data.setAgentId(bean.getStudentId());
            }
            float newAutoScore = (new Float(question.getExactPoints()).floatValue() / (float) gradingarray.size());
            float oldAutoScore = 0;
            if (data.getAutoScore() !=null) {
              oldAutoScore=data.getAutoScore().floatValue();
            }
            String newComments = ContextUtil.processFormattedText(log, question.getGradingComment());
            if (newComments != null) {
      		  newComments = newComments.trim();
            }
            else {
              newComments = "";
            }
            String oldComments = data.getComments();
            if (oldComments != null) { 	
          	  oldComments = oldComments.trim();
            }
            else {
              oldComments = "";
            }
            boolean updateScore = newAutoScore != oldAutoScore;
            boolean updateComments = !newComments.equals(oldComments);
            StringBuffer logString = new StringBuffer();
            logString.append("gradedBy=");
            logString.append(AgentFacade.getAgentString());
            logString.append(", itemGradingId=");
            logString.append(data.getItemGradingId());
            
            if (updateScore) {
              data.setAutoScore(Float.valueOf(newAutoScore));
              logString.append(", newAutoScore=");
              logString.append(newAutoScore);
              logString.append(", oldAutoScore=");
              logString.append(oldAutoScore);
            }
            if (updateComments) {
              data.setComments(newComments);
              logString.append(", newComments=");
              logString.append(newComments);
              logString.append(", oldComments=");
              logString.append(oldComments);
            }
            if (updateScore || updateComments) {
              updateFlag = true;	
              data.setGradedBy(AgentFacade.getAgentString());
              data.setGradedDate(new Date());
              EventTrackingService.post(EventTrackingService.newEvent("sam.student.score.update", logString.toString(), true));
              log.debug("****4 itemGradingId="+data.getItemGradingId());
              log.debug("****5 set points = " + data.getAutoScore() + ", comments to " + data.getComments());
            }
            itemGradingSet.add(data);
          }
        }
        if (adata==null){
          // this is for cases when studnet submitted an assessment but skipped all teh questions
          // when we won't be able to get teh assessmentGrading based on itemGrdaing ('cos there is none).
          String assessmentGradingId = cu.lookupParam("gradingData");
          adata = delegate.load(assessmentGradingId);
        }
        adata.setItemGradingSet(itemGradingSet);
      }

      if (adata == null)
        return true; // Nothing to save.

      String newComments = ContextUtil.processFormattedText(log, bean.getComments());
      if (newComments != null) {
    	  newComments = newComments.trim();
      }
      else {
    	  newComments = "";
      }
      String oldComments = adata.getComments();
      if (oldComments != null) { 	
    	  oldComments = oldComments.trim();
      }
      else {
    	  oldComments = "";
      }

      if (!newComments.equals(oldComments)) {
    	  updateFlag = true;
    	  adata.setComments(newComments);
    	  adata.setGradedBy(AgentFacade.getAgentString());
    	  adata.setGradedDate(new Date());
    	  StringBuffer logString = new StringBuffer();
          logString.append("gradedBy=");
          logString.append(AgentFacade.getAgentString());
          logString.append(", assessmentGradingId=");
          logString.append(adata.getAssessmentGradingId());
          logString.append(", newComments=");
          logString.append(newComments);
          logString.append(", oldComments=");
          logString.append(oldComments);
    	  EventTrackingService.post(EventTrackingService.newEvent("sam.student.score.update", logString.toString(), true));
      }

      //log.debug("Got total comments: " + adata.getComments());

      // Some of the itemgradingdatas may be new - comment this out
      // I don't know how itemgradingdatas may be new
      /*
      iter = itemGradingSet.iterator();
      while (iter.hasNext())
      {
        ItemGradingData data = (ItemGradingData) iter.next();
        data.setAssessmentGradingId(adata.getAssessmentGradingId());
      }
	  */
      if (updateFlag) {
    	  delegate.updateAssessmentGradingScore(adata, tbean.getPublishedAssessment());
      }
      log.debug("Saved student scores.");

    } catch (GradebookServiceException ge) {
       FacesContext context = FacesContext.getCurrentInstance();
       String err=(String)cu.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages", "gradebook_exception_error");
       context.addMessage(null, new FacesMessage(err));

    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
