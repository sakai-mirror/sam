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



package org.sakaiproject.tool.assessment.data.ifc.grading;
import java.util.Date;
import java.util.Set;

public interface AssessmentGradingIfc
    extends java.io.Serializable{

	public static final Integer AUTO_GRADED = new Integer(2);

	public static final Integer NEED_HUMAN_ATTENTION = new Integer(3);

	public static final Integer LATE_SUBMISSION = new Integer(4);

	public static final Integer NO_SUBMISSION = new Integer(5);


  Long getAssessmentGradingId();

  void setAssessmentGradingId(Long assessmentGradingId);

  Long getPublishedAssessmentId();

  void setPublishedAssessmentId(Long publishedAssessmentId);

  String getAgentId();

  void setAgentId(String agentId);

  //AgentIfc getAgent();

  Date getSubmittedDate();

  void setSubmittedDate(Date submittedDate);

  // Is isLate determined by comparing the submitted date with the duedate
  // of published assessment or core assessment?
  // if the former, then we need to store the duedate info in DB
  // if latter, isLate is determined on the fly -
  // 'cos core assessment due date can be changed.
  Boolean getIsLate();

  void setIsLate(Boolean isLate);

  Boolean getForGrade();

  void setForGrade(Boolean forGrade);

  // sum of item score through auto scoring
  Float getTotalAutoScore();

  void setTotalAutoScore(Float totalAutoScore);

  // sum of item score through instructor grading
  Float getTotalOverrideScore();

  void setTotalOverrideScore(Float totalOverrideScore);

  // grader can override the total score with a final score
  Float getFinalScore();

  void setFinalScore(Float finalScore);

  String getComments();

  void setComments(String comments);

  String getGradedBy();

  void setGradedBy(String GradedBy);

  Date getGradedDate();

  void setGradedDate(Date GradedDate);

  Integer getStatus();

  void setStatus(Integer status);

  Set getItemGradingSet();

  void setItemGradingSet(Set itemGradingSet);

  Date getAttemptDate();

  void setAttemptDate(Date attemptDate);

  Integer getTimeElapsed();

  void setTimeElapsed(Integer timeElapsed);

}
