/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/facade/PublishedAssessmentFacade.java $
 * $Id: PublishedAssessmentFacade.java 9273 2006-05-10 22:34:28Z daisyf@stanford.edu $
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

package org.sakaiproject.tool.assessment.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.data.dao.assessment.AssessmentData;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedMetaData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAccessControlIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentFeedbackIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.EvaluationModelIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.PublishedAssessmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;

public class PublishedAssessmentFacade
    implements java.io.Serializable, PublishedAssessmentIfc, Cloneable
{
  private static final long serialVersionUID = 7526471155622776147L;
  private static Log log = LogFactory.getLog(PublishedAssessmentFacade.class);
  public static final Integer ACTIVE_STATUS = new Integer(1);
  public static final Integer INACTIVE_STATUS = new Integer(0);
  public static final Integer ANY_STATUS = new Integer(2);
  private PublishedAssessmentIfc data;
  private AssessmentFacade assessment;
  private Long publishedAssessmentId;
  private Long assessmentId;
  private Boolean isTemplate = new Boolean("false");
  private Long parentId;
  private String title;
  private String description;
  private String comments;
  private Long typeId;
  private Integer instructorNotification;
  private Integer testeeNotification;
  private Integer multipartAllowed;
  private Integer status;
  private String createdBy;
  private Date createdDate;
  private String lastModifiedBy;
  private Date lastModifiedDate;
  private AssessmentAccessControlIfc publishedAccessControl;
  private EvaluationModelIfc publishedEvaluationModel;
  private AssessmentFeedbackIfc publishedFeedback;
  private Set publishedMetaDataSet;
  private HashMap publishedMetaDataMap = new HashMap();
  private Set publishedSectionSet;
  private Set publishedSecuredIPAddressSet;
  // the following properties is added for the "Convenient Constructor"
  private String releaseTo;
  private Date startDate;
  private Date dueDate;
  private Date retractDate;
  private int submissionSize;
  private Integer lateHandling;
  private Boolean unlimitedSubmissions;
  private Integer submissionsAllowed;
  private Integer feedbackDelivery;
  private Integer feedbackAuthoring;
  private Date feedbackDate;
  private String ownerSiteName;

  public PublishedAssessmentFacade() {
  }

  // constructor that whole min. info, used for listing
  public PublishedAssessmentFacade(Long id, String title, String releaseTo,
                                 Date startDate, Date dueDate){
    this.publishedAssessmentId = id;
    this.title = title;
    this.releaseTo = releaseTo;
    this.startDate = startDate;
    this.dueDate = dueDate;
  }

  // constructor that whole min. info, used for listing
  public PublishedAssessmentFacade(Long id, String title, String releaseTo,
                                 Date startDate, Date dueDate, Date retractDate,
                                 Date feedbackDate, Integer feedbackDelivery, Integer feedbackAuthoring,
                                 Integer lateHandling, Boolean unlimitedSubmissions,
                                 Integer submissionsAllowed){
    this.publishedAssessmentId = id;
    this.title = title;
    this.releaseTo = releaseTo;
    this.startDate = startDate;
    this.dueDate = dueDate;
    this.retractDate = retractDate;
    this.feedbackDelivery = feedbackDelivery; //=publishedFeedback.feedbackDelivery
 this.feedbackAuthoring = feedbackAuthoring; //=publishedFeedback.feedbackAuthoring
    this.feedbackDate = feedbackDate;
    this.lateHandling = lateHandling;
    if (unlimitedSubmissions != null)
      this.unlimitedSubmissions = unlimitedSubmissions;
    else
      this.unlimitedSubmissions = Boolean.TRUE;
    if (submissionsAllowed == null)
      this.submissionsAllowed = new Integer(0);
    else
      this.submissionsAllowed = submissionsAllowed;
  }

  public PublishedAssessmentFacade(Long id, String title, AssessmentAccessControlIfc publishedAccessControl){
    this.publishedAssessmentId = id;
    this.title = title;
    this.publishedAccessControl = publishedAccessControl;
  }

  public PublishedAssessmentFacade(PublishedAssessmentIfc data, Boolean loadSection) {
    setProperties(data);
    if (loadSection.equals(Boolean.TRUE))
      this.publishedSectionSet = data.getSectionSet();
  }

  public PublishedAssessmentFacade(PublishedAssessmentIfc data) {
    setProperties(data);
    this.publishedSectionSet = data.getSectionSet();
  }

  private void setProperties(PublishedAssessmentIfc data){
    this.data = data;
    this.publishedAssessmentId = data.getPublishedAssessmentId();
    this.assessmentId= data.getAssessmentId();
    this.title = data.getTitle();
    this.description = data.getDescription();
    this.comments = data.getComments();
    this.publishedMetaDataSet = data.getAssessmentMetaDataSet();
    this.instructorNotification = data.getInstructorNotification();
    this.testeeNotification = data.getTesteeNotification();
    this.multipartAllowed = data.getMultipartAllowed();
    this.status = data.getStatus();
    this.createdBy = data.getCreatedBy();
    this.createdDate = data.getCreatedDate();
    this.lastModifiedBy = data.getLastModifiedBy();
    this.lastModifiedDate = data.getLastModifiedDate();
    this.publishedAccessControl = data.getAssessmentAccessControl();
    this.publishedFeedback = data.getAssessmentFeedback();
    this.publishedEvaluationModel = data.getEvaluationModel();
    this.publishedMetaDataMap = data.getAssessmentMetaDataMap(
        this.publishedMetaDataSet);
    this.publishedSecuredIPAddressSet = data.getSecuredIPAddressSet();
  }

  public Long getPublishedAssessmentId(){
    return publishedAssessmentId;
  }

  public void setPublishedAssessmentId(Long publishedAssessmentId) {
    this.publishedAssessmentId = publishedAssessmentId;
    this.data.setAssessmentBaseId(publishedAssessmentId);
  }

  // assessment returns is AssessmentFacade
  public AssessmentIfc getAssessment(){
    return assessment;
  }

  // assessment here is AssessmentFacade not AssessmentData
  public void setAssessment(AssessmentIfc assessment) {
    this.assessment = (AssessmentFacade) assessment;
    AssessmentData d = (AssessmentData) this.assessment.getData();
    this.data.setAssessmentId(d.getAssessmentBaseId());
    //this.data.setAssessment(d);
  }

  // override the following method from AssessmentData
  public Long getAssessmentId(){
    return assessmentId;
  }

  public void setAssessmentId(Long assessmentId) {
    setAssessmentBaseId(assessmentId);
  }

  public Long getAssessmentBaseId() {
    return assessmentId;
  }

  public void setAssessmentBaseId(Long id) {
    this.assessmentId = id;
    this.data.setAssessmentBaseId(id);
  }

  // don't have isTemplate
  public Boolean getIsTemplate() {
    return isTemplate;
  }

  public void setIsTemplate(Boolean isTemplate) {
  }

  // published assessment don't have parent
  public Long getParentId() {
    return null;
  }

  public void setParentId(Long parentId) {
  }

  // published assessment don't have a template
  public Long getAssessmentTemplateId() {
    return null;
  }

  public void setAssessmentTemplateId(Long assessmentTemplateId) {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
    this.data.setTitle(title);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
    this.data.setDescription(description);
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
    this.data.setComments(comments);
  }

  public Integer getInstructorNotification() {
    return instructorNotification;
  }

  public void setInstructorNotification(Integer instructorNotification) {
    this.instructorNotification = instructorNotification;
    this.data.setInstructorNotification(instructorNotification);
  }

  public Integer getTesteeNotification() {
    return testeeNotification;
  }

  public void setTesteeNotification(Integer testeeNotification) {
    this.testeeNotification = testeeNotification;
    this.data.setTesteeNotification(testeeNotification);
  }

  public Integer getMultipartAllowed() {
    return multipartAllowed;
  }

  public void setMultipartAllowed(Integer multipartAllowed) {
    this.multipartAllowed = multipartAllowed;
    this.data.setMultipartAllowed(multipartAllowed);
  }

  public Long getTypeId() {
    return typeId;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
    this.data.setTypeId(typeId);
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
    this.data.setStatus(status);
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
    this.data.setCreatedBy(createdBy);
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    this.data.setCreatedDate(createdDate);
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy =lastModifiedBy;
    this.data.setLastModifiedBy(lastModifiedBy);
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    this.data.setLastModifiedDate(lastModifiedDate);
  }

  public AssessmentAccessControlIfc getAssessmentAccessControl() {
    return publishedAccessControl;
  }

  public void setAssessmentAccessControl(AssessmentAccessControlIfc
                                  publishedAccessControl) {
    this.publishedAccessControl = publishedAccessControl;
    this.data.setAssessmentAccessControl(publishedAccessControl);
  }

  public EvaluationModelIfc getEvaluationModel() {
    return publishedEvaluationModel;
  }

  public void setEvaluationModel(EvaluationModelIfc publishedEvaluationModel) {
    this.publishedEvaluationModel = publishedEvaluationModel;
    this.data.setEvaluationModel(publishedEvaluationModel);
  }

  public AssessmentFeedbackIfc getAssessmentFeedback() {
    return publishedFeedback;
  }

  public void setAssessmentFeedback(AssessmentFeedbackIfc assessmentFeedback) {
    this.publishedFeedback = assessmentFeedback;
  }

  public Set getAssessmentMetaDataSet() {
    return publishedMetaDataSet;
  }

  public void setAssessmentMetaDataSet(Set publishedMetaDataSet) {
    this.publishedMetaDataSet = publishedMetaDataSet;
    this.data.setAssessmentMetaDataSet(publishedMetaDataSet);
  }

  public HashMap getAssessmentMetaDataMap(Set publishedMetaDataSet) {
    HashMap publishedMetaDataMap = new HashMap();
    if (publishedMetaDataSet !=null){
      for (Iterator i = publishedMetaDataSet.iterator(); i.hasNext(); ) {
        PublishedMetaData publishedMetaData = (PublishedMetaData) i.next();
        publishedMetaDataMap.put(publishedMetaData.getLabel(), publishedMetaData.getEntry());
      }
    }
    return publishedMetaDataMap;
  }

  public HashMap getAssessmentMetaDataMap() {
    HashMap publishedMetaDataMap = new HashMap();
    if (this.publishedMetaDataSet !=null){
      for (Iterator i = this.publishedMetaDataSet.iterator(); i.hasNext(); ) {
        PublishedMetaData publishedMetaData = (PublishedMetaData) i.next();
        publishedMetaDataMap.put(publishedMetaData.getLabel(), publishedMetaData.getEntry());
      }
    }
    return publishedMetaDataMap;
  }

  public String getAssessmentMetaDataByLabel(String label) {
    return (String)publishedMetaDataMap.get(label);
  }

  public void addAssessmentMetaData(String label, String entry) {
    if (this.publishedMetaDataMap.get(label)!=null){
      // just update
      Iterator iter = this.publishedMetaDataSet.iterator();
      while (iter.hasNext()){
        PublishedMetaData metadata = (PublishedMetaData) iter.next();
        if (metadata.getLabel().equals(label))
          metadata.setEntry(entry);
      }
    }
    else{ // add
      PublishedMetaData metadata = null;
      if (!("").equals(entry.trim())){
        metadata = new PublishedMetaData(this.data, label, entry);
        this.publishedMetaDataSet.add(metadata);
      }
      setAssessmentMetaDataSet(this.publishedMetaDataSet);
    }
  }

  public void updateAssessmentMetaData(String label, String entry) {
    addAssessmentMetaData(label, entry);
  }

/** not tested this method -daisy 11/16/04
  public void removeAssessmentMetaDataByLabel(String label) {
    HashSet set = new HashSet();
    Iterator iter = this.publishedMetaDataSet.iterator();
    while (iter.hasNext()){
      PublishedMetaData metadata = (PublishedMetaData) iter.next();
      if (!metadata.getLabel().equals(label))
        set.add(metadata);
    }
    setAssessmentMetaDataSet(set);
  }
*/
  public Set getSectionSet() {
    return publishedSectionSet;
  }

  public void setSectionSet(Set sectionSet) {
    this.publishedSectionSet = sectionSet;
    this.data.setSectionSet(sectionSet);
  }

  public Set getSecuredIPAddressSet() {
    return publishedSecuredIPAddressSet;
  }

  public void setSecuredIPAddressSet(Set publishedSecuredIPAddressSet) {
    this.publishedSecuredIPAddressSet =  publishedSecuredIPAddressSet;
    this.data.setSecuredIPAddressSet(publishedSecuredIPAddressSet);
  }

  public PublishedAssessmentIfc getData() {
    return data;
  }

  public TypeIfc getType() {
    return null;
  }

  public ArrayList getSectionArray() {
    ArrayList list = new ArrayList();
    Iterator iter = publishedSectionSet.iterator();
    while (iter.hasNext()){
      list.add(iter.next());
    }
    return list;
  }

  public ArrayList getSectionArraySorted() {
    ArrayList list = getSectionArray();
    Collections.sort(list);
    return list;
  }

  public String getReleaseTo() {
    return this.releaseTo;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public Date getDueDate() {
    return this.dueDate;
  }

  public int getSubmissionSize() {
    return this.submissionSize;
  }

  public void setSubmissionSize(int size) {
    this.submissionSize = size;
  }

  public SectionDataIfc getSection(Long sequence){
    ArrayList list = getSectionArraySorted();
    if (list == null)
      return null;
    else
      return (SectionDataIfc) list.get(sequence.intValue()-1);
  }

  public SectionDataIfc getDefaultSection(){
    ArrayList list = getSectionArraySorted();
    if (list == null)
      return null;
    else
      return (SectionDataIfc) list.get(0);
  }

  public Integer getLateHandling() {
    return lateHandling;
  }

  public Boolean getUnlimitedSubmissions() {
    return this.unlimitedSubmissions;
  }

  public Integer getSubmissionsAllowed()
  {
    return submissionsAllowed;
  }

  public Date getRetractDate() {
    return this.retractDate;
  }

  public Integer getFeedbackDelivery()
  {
    return feedbackDelivery;
  }

 public Integer getFeedbackAuthoring()
  {
    return feedbackAuthoring;
  }

  public Date getFeedbackDate() {
    return this.feedbackDate;
  }

  public void setOwnerSite(String ownerSiteName){
    this.ownerSiteName = ownerSiteName;
  }

  public String getOwnerSite(){
    PublishedAssessmentService service = new PublishedAssessmentService();
    String ownerSiteId = service.getPublishedAssessmentOwner(this.publishedAssessmentId);
    log.debug("**** ownerSiteId="+ownerSiteId);
    this.ownerSiteName = AgentFacade.getSiteName(ownerSiteId);
    return this.ownerSiteName;
  }

  public String getOwnerSiteId(){
    PublishedAssessmentService service = new PublishedAssessmentService();
    return service.getPublishedAssessmentOwner(this.publishedAssessmentId);
  }

  public Float getTotalScore(){
    float total = 0;
    Iterator iter = this.publishedSectionSet.iterator();
    while (iter.hasNext()){
      SectionDataIfc s = (SectionDataIfc) iter.next();
      ArrayList list = s.getItemArray();
      Iterator iter2 = null;
      if ((s.getSectionMetaDataByLabel(SectionDataIfc.AUTHOR_TYPE)!=null) && (s.getSectionMetaDataByLabel(SectionDataIfc.AUTHOR_TYPE
).equals(SectionDataIfc.RANDOM_DRAW_FROM_QUESTIONPOOL.toString())))
{
        ArrayList randomsample = new ArrayList();
        Integer numberToBeDrawn= null;
        if (s.getSectionMetaDataByLabel(SectionDataIfc.NUM_QUESTIONS_DRAWN) !=null ) {
          numberToBeDrawn= new Integer(s.getSectionMetaDataByLabel(SectionDataIfc.NUM_QUESTIONS_DRAWN));
        }

        int samplesize = numberToBeDrawn.intValue();
        for (int i=0; i<samplesize; i++){
          randomsample.add(list.get(i));
        }
        iter2 = randomsample.iterator();
      }
      else {
        iter2 = list.iterator();
      }

      while (iter2.hasNext()){
        ItemDataIfc item = (ItemDataIfc)iter2.next();
        total= total + item.getScore().floatValue();
      }
    }
    return new Float(total);
  }

  public PublishedAssessmentFacade clonePublishedAssessment(){
    try{
      return (PublishedAssessmentFacade)this.clone();
    }
    catch(CloneNotSupportedException e){ 
      log.warn(e.getMessage());
      return null;
    }
  }
}
