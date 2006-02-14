/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004-2005 The Regents of the University of Michigan, Trustees of Indiana University,
 *                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
 *
   * Licensed under the Educational Community License Version 1.0 (the "License");
 * By obtaining, using and/or copying this Original Work, you agree that you have read,
 * understand, and will comply with the terms and conditions of the Educational Community License.
 * You may obtain a copy of the License at:
 *
 *      http://cvs.sakaiproject.org/licenses/license_1_0.html
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 **********************************************************************************/
package org.sakaiproject.tool.assessment.integration.helper.standalone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.service.gradebook.shared.GradebookService;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedAssessmentData;
import org.sakaiproject.tool.assessment.data.ifc.grading.AssessmentGradingIfc;
import org.sakaiproject.tool.assessment.integration.helper.ifc.GradebookServiceHelper;

/**
 *
 * <p>Description:
 * This is a stub standalone context implementation helper delegate class for
 * the GradebookService class.  The helper methods are stubs because in
 * standalone there isn't gradebook integration.  "Standalone" means that
   * Samigo (Tests and Quizzes) is running without the context of the Sakai portal
 * and authentication mechanisms, and therefore we use stub methods.</p>
 * <p>Note: To customize behavior you can add your own helper class to the
 * Spring injection via the integrationContext.xml for your context.
 * The particular integrationContext.xml to be used is selected by the
 * build process.
 * </p>
 * <p>Sakai Project Copyright (c) 2005</p>
 * <p> </p>
 * @author Ed Smiley <esmiley@stanford.edu>
 *
 */

public class GradebookServiceHelperImpl implements GradebookServiceHelper
{
  private static Log log = LogFactory.getLog(GradebookServiceHelperImpl.class);

  public boolean isAssignmentDefined(String assessmentTitle,
                                GradebookService g)
  {
    return false;
  }

  /**
   * STUB.  NO-OP.
   * @param publishedAssessment the published assessment
   * @param g  the Gradebook Service
   * @return false: cannot add to gradebook
   * @throws java.lang.Exception
   */
  public boolean addToGradebook(PublishedAssessmentData publishedAssessment,
                                GradebookService g)
  {
    return false;
  }

  /**
   * STUB.  NO-OP.
   * @param gradebookUId the gradebook id
   * @param publishedAssessmentId the id of the published assessment
   * @param g  the Gradebook Service
   * @throws java.lang.Exception
   */
  public void removeExternalAssessment(String gradebookUId,
                                       String publishedAssessmentId,
                                       GradebookService g) throws Exception
  {
  }

  /**
   * Always returns false, because standalone.
   * @param gradebookUId the gradebook id
   * @param g  the Gradebook Service
   * @return false, no gradebook integration
   */
  public boolean gradebookExists(String gradebookUId, GradebookService g)
  {
    return false;
  }

  /**
   * STUB.  NO-OP.
   * @param ag the assessment grading
   * @param g  the Gradebook Service
   * @throws java.lang.Exception
   */
  public void updateExternalAssessmentScore(AssessmentGradingIfc ag,
                                            GradebookService g) throws
    Exception
  {
  }

}
