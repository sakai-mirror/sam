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
import org.sakaiproject.tool.assessment.data.dao.grading.AssessmentGradingData;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.services.GradingService;
import org.sakaiproject.tool.assessment.services.GradebookServiceException;
import org.sakaiproject.tool.assessment.ui.bean.delivery.DeliveryBean;
import org.sakaiproject.tool.assessment.ui.bean.delivery.ItemContentsBean;
import org.sakaiproject.tool.assessment.ui.bean.delivery.SectionContentsBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.StudentScoresBean;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.TotalScoresBean;
import org.sakaiproject.tool.assessment.ui.listener.evaluation.util.EvaluationListenerUtil;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.util.BeanSort;

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
  private static EvaluationListenerUtil util;
  private static BeanSort bs;
  private static ContextUtil cu;

  /**
   * Standard process action method.
   * @param ae ActionEvent
   * @throws AbortProcessingException
   */
  public void processAction(ActionEvent ae) throws
    AbortProcessingException
  {
    log.info("Student Score Update LISTENER.");
    StudentScoresBean bean = (StudentScoresBean) cu.lookupBean("studentScores");
    TotalScoresBean tbean = (TotalScoresBean) cu.lookupBean("totalScores");
    tbean.setAssessmentGradingHash(tbean.getPublishedAssessment().getPublishedAssessmentId());
    DeliveryBean delivery = (DeliveryBean) cu.lookupBean("delivery");
    log.info("Calling saveStudentScores.");
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
      while (iter.hasNext())
      {
        ArrayList items = ((SectionContentsBean) iter.next()).getItemContents();
        Iterator iter2 = items.iterator();
        while (iter2.hasNext())
        {
          ItemContentsBean question = (ItemContentsBean) iter2.next();
          ArrayList gradingarray = question.getItemGradingDataArray();
          log.info("****1. pub questionId = " + question.getItemData().getItemId());
          log.info("****2. Gradingarray length = " + gradingarray.size());
          // Create a new one if we need it.
          if (gradingarray.isEmpty() && (question.getExactPoints() > 0  ||
              (question.getGradingComment() != null &&
               !question.getGradingComment().trim().equals("")) ))
          {
            // this is another mystery, no idea why review is involved here - daiyf
            question.setReview(false); // This creates an itemgradingdata
            gradingarray = question.getItemGradingDataArray();
          }
          log.info("****3a Gradingarray length2 = " + gradingarray.size());
          log.info("****3b set points = " + question.getExactPoints() + ", comments to " + question.getGradingComment());
          Iterator iter3 = gradingarray.iterator();
          while (iter3.hasNext())
          {
            ItemGradingData data = (ItemGradingData) iter3.next();
            if (adata == null){
              adata = delegate.load(data.getAssessmentGradingId().toString());
	    }
            if (data.getAgentId() == null)
            { // it's a new data, fill it in
              data.setSubmittedDate(new Date());
              data.setAgentId(bean.getStudentId());
            }
            data.setAutoScore(new Float
              (new Float(question.getExactPoints()).floatValue()
               / (float) gradingarray.size()));
            data.setComments(question.getGradingComment());
            log.info("****4 itemGradingId="+data.getItemGradingId());
            log.info("****5 set points = " + data.getAutoScore() + ", comments to " + data.getComments());
            itemGradingSet.add(data);
          }
        }
        adata.setItemGradingSet(itemGradingSet);
      }

      if (adata == null)
        return true; // Nothing to save.

      adata.setComments(bean.getComments());
      //log.info("Got total comments: " + adata.getComments());

      // Some of the itemgradingdatas may be new.
      iter = itemGradingSet.iterator();
      while (iter.hasNext())
      {
        ItemGradingData data = (ItemGradingData) iter.next();
        data.setAssessmentGradingId(adata.getAssessmentGradingId());
      }

      delegate.updateAssessmentGradingScore(adata, tbean.getPublishedAssessment());

      log.info("Saved student scores.");

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
