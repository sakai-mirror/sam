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

package org.sakaiproject.tool.assessment.data.dao.shared;
import java.util.Date;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;

public class TypeD implements TypeIfc{
  // This has the exact same list as TypeFacade. Please keep both list updated
  public static final Long MULTIPLE_CHOICE = new Long(1);
  public static final Long MULTIPLE_CORRECT = new Long(2);
  public static final Long MULTIPLE_CHOICE_SURVEY = new Long(3);
  public static final Long TRUE_FALSE = new Long(4);
  public static final Long ESSAY_QUESTION = new Long(5);
  public static final Long FILE_UPLOAD = new Long(6);
  public static final Long AUDIO_RECORDING = new Long(7);
  public static final Long FILL_IN_BLANK = new Long(8);
  public static final Long MATCHING = new Long(9);
  // these are section type available in this site,
  public static final Long DEFAULT_SECTION = new Long(21);
  // these are assessment template type available in this site,
  public static final Long TEMPLATE_SYSTEM_DEFINED = new Long(142);
  public static final Long TEMPLATE_QUIZ = new Long(41);
  public static final Long TEMPLATE_HOMEWORK = new Long(42);
  public static final Long TEMPLATE_MIDTERM = new Long(43);
  public static final Long TEMPLATE_FINAL = new Long(44);
  // these are assessment type available in this site,
  public static final Long QUIZ = new Long(61);
  public static final Long HOMEWORK = new Long(62);
  public static final Long MIDTERM = new Long(63);
  public static final Long FINAL = new Long(64);

  private Long typeId;
  private String authority;
  private String domain;
  private String keyword;
  private String description;
  private int status;
  private String createdBy;
  private Date createdDate;
  private String lastModifiedBy;
  private Date lastModifiedDate;

  public TypeD(){
  }

  public TypeD(String authority, String domain,
                  String keyword) {
    this.authority = authority;
    this.domain = domain;
    this.keyword = keyword;
  }

  public TypeD(String authority, String domain,
                  String keyword, String description) {
    this.authority = authority;
    this.domain = domain;
    this.keyword = keyword;
    this.description = description;
  }

  public TypeD(String authority, String domain,
                  String keyword, String description,
                  int status,
                  String createdBy, Date createdDate,
                  String lastModifiedBy, Date lastModifiedDate) {
    this.authority = authority;
    this.domain = domain;
    this.keyword = keyword;
    this.description = description;
    this.status = status;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.lastModifiedBy = lastModifiedBy;
    this.lastModifiedDate = lastModifiedDate;
  }

  /**
   * Return the itemTypeId from ItemType
   */
  public Long getTypeId(){
      return this.typeId;
  }

  /**
   * Set the itemTypeID for this ItemType.
   */
  public void setTypeId(Long typeId){
      this.typeId = typeId;
  }

  public String getAuthority(){
      return this.authority;
  }

  public void setAuthority(String authority){
      this.authority = authority;
  }
  public String getDomain(){
      return this.domain;
  }

  public void setDomain(String domain){
      this.domain = domain;
  }

  /**
   * Return the Name for this ItemType.
   */
  public String getKeyword(){
      return this.keyword;
  }

  /**
   * Set the Name for this ItemType.
   */
  public void setKeyword(String keyword){
      this.keyword = keyword;
  }

  public String getDescription(){
      return this.description;
  }

  public void setDescription(String description){
      this.description = description;
  }

  /**
   * Return the status for this ItemType.
   */
  public int getStatus(){
      return this.status;
  }

  /**
   * Set the status for this ItemType.
   */
  public void setStatus(int status){
      this.status = status;
  }

  /**
   * Return the createdBy for this ItemType.
   */
  public String getCreatedBy(){
      return this.createdBy;
  }

  /**
   * Set the createdBy for this ItemType.
   */
  public void setCreatedBy (String createdBy){
      this.createdBy = createdBy;
  }

  /**
   * Return the createdDate for this ItemType.
   */
  public Date getCreatedDate(){
      return this.createdDate;
  }

  /**
   * Set the createdDate for this ItemType.
   */
  public void setCreatedDate (Date createdDate){
      this.createdDate = createdDate;
  }

  /**
   * Return the lastModifiedBy for this ItemType.
   */
  public String getLastModifiedBy(){
      return this.lastModifiedBy;
  }
  /**
   * Set the lastModifiedBy for this ItemType.
   */
  public void setLastModifiedBy (String lastModifiedBy){
      this.lastModifiedBy = lastModifiedBy;
  }

  /**
   * Return the lastModifiedDate for this ItemType.
   */
  public Date getLastModifiedDate(){
      return this.lastModifiedDate;
  }

  /**
   * Set the lastModifiedDate for this ItemType.
   */
  public void setLastModifiedDate (Date lastModifiedDate){
      this.lastModifiedDate = lastModifiedDate;
  }

}
