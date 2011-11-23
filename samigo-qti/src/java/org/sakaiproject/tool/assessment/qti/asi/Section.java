/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/qti/asi/Section.java $
 * $Id: Section.java 9274 2006-05-10 22:50:48Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
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



package org.sakaiproject.tool.assessment.qti.asi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.sakaiproject.tool.assessment.data.dao.assessment.AttachmentData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemMetaDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionMetaDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;
import org.sakaiproject.tool.assessment.qti.constants.QTIConstantStrings;
import org.sakaiproject.tool.assessment.qti.constants.QTIVersion;
import org.sakaiproject.tool.assessment.qti.helper.QTIHelperFactory;
import org.sakaiproject.tool.assessment.qti.helper.item.ItemHelperIfc;
import org.sakaiproject.util.FormattedText;

/**
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley esmiley@stanford.edu
 * @author Shastri, Rashmi <rshastri@iupui.edu>
 * @version $Id: Section.java 9274 2006-05-10 22:50:48Z daisyf@stanford.edu $
 */
public class Section extends ASIBaseClass
{
  private static Log log = LogFactory.getLog(Section.class);
  public String basePath;

  /**
   * Explicitly setting serialVersionUID insures future versions can be
     * successfully restored. It is essential this variable name not be changed
     * to SERIALVERSIONUID, as the default serialization methods expects this
   * exact name.
   */
  private static final long serialVersionUID = 1;
  private int qtiVersion;

  /**
   * Creates a new Section object.
   */
  public Section()
  {
    super();
    this.basePath = QTIConstantStrings.SECTION;
  }

  /**
   * Creates a new Section object.
   *
   * @param document DOCUMENTATION PENDING
   */
  public Section(Document document, int qtiVersion)
  {
    super(document);
    if (!QTIVersion.isValid(qtiVersion))
    {
      throw new IllegalArgumentException("Invalid Section QTI version.");
    }
    this.qtiVersion = qtiVersion;
    this.basePath = QTIConstantStrings.SECTION;
  }

  /**
   * set section ident (id)
   * @param ident
   */
  @SuppressWarnings("unchecked")
public void setIdent(String ident)
  {
    String xpath = "section";
    List<Element> list = this.selectNodes(xpath);
    if (list.size() > 0)
    {
      Element element = list.get(0);
      element.setAttribute("ident", ident);
    }
  }
  /**
   * set section title
   * @param title
   */
  @SuppressWarnings("unchecked")
public void setTitle(String title)
  {
    String xpath = "section";
    List<Element> list = this.selectNodes(xpath);
    if (list.size() > 0)
    {
      Element element = list.get(0);
      element.setAttribute("title", escapeXml(title));
    }
  }

  /**
   * Update XML from persistence
   * @param section
   */
  @SuppressWarnings("unchecked")
public void update(SectionDataIfc section)
  {
    // identity
    setIdent("" + section.getSectionId());
    setTitle(FormattedText.convertFormattedTextToPlaintext(section.getTitle()));
    // metadata
    // Where the heck do these come from?  Looks like not being used.
    // If required we could extract keywords by weighting, and
    // set rubrics identical to description, or, we could eliminate these from XML.

    // well, we can add metadata from users' input - lydial 
    
    setFieldentry("SECTION_INFORMATION", section.getDescription());
    setFieldentry("SECTION_OBJECTIVE", section.getSectionMetaDataByLabel(SectionMetaDataIfc.OBJECTIVES));
    setFieldentry("SECTION_KEYWORD", section.getSectionMetaDataByLabel(SectionMetaDataIfc.KEYWORDS));
    setFieldentry("SECTION_RUBRIC", section.getSectionMetaDataByLabel(SectionMetaDataIfc.RUBRICS));
    setFieldentry("ATTACHMENT", getAttachment(section));
    
    // items
    addItems(section.getItemArray());
  }
  
  /**
   * Add item list to this section document.
   *
   * @param items list of ItemDataIfc
   */
  private void addItems(List<ItemDataIfc> items)
  {
    if (log.isDebugEnabled())
    {
      log.debug("addItems(ArrayList " + items + ")");
    }
//    ItemHelper itemHelper = new ItemHelper();
    QTIHelperFactory factory = new QTIHelperFactory();
    ItemHelperIfc itemHelper =
        factory.getItemHelperInstance(this.qtiVersion);

    try
    {
      String xpath = basePath;
      for (ItemDataIfc item: items)
      {
        Long type = item.getTypeId();
        if(false && 14 == type.intValue()){//XXX
        	printItem(item);
        }
        Item itemXml;
        if ( (TypeIfc.MULTIPLE_CHOICE_SURVEY).equals(type))
        {
	  // deprecated, keep it for backward compatibility
          String scale = item.getItemMetaDataByLabel(ItemMetaDataIfc.SCALENAME);   
	  // PREDEFINED_SCALE is the new metadata key 
          if (scale==null) {
            scale = item.getItemMetaDataByLabel(ItemMetaDataIfc.PREDEFINED_SCALE);
          }
          itemXml = itemHelper.readTypeSurveyItem(scale);
        }
        else
        {
          itemXml = itemHelper.readTypeXMLItem(type);
        }

        // update item data
        itemXml.setIdent(item.getItemIdString());
        itemXml.update(item);
        Element itemElement = itemXml.getDocument().
                              getDocumentElement();
        log.debug(
          "Item ident is: " + itemElement.getAttribute("ident"));
        this.addElement(xpath, itemElement);
      }
    }
    catch (Exception e)
    {
      log.error(e.getMessage(), e);
    }
  }


  /**
   * Method for meta data.
   * @todo use QTIConstantStrings
   * @param fieldlabel field label
   *
   * @return value
   */
  public String getFieldentry(String fieldlabel)
  {
    if (log.isDebugEnabled())
    {
      log.debug("getFieldentry(String " + fieldlabel + ")");
    }

    String xpath =
      "section/qtimetadata/qtimetadatafield/fieldlabel[text()='" +
      fieldlabel +
      "']/following-sibling::fieldentry";

    return super.getFieldentry(xpath);
  }

  /**
   * Method for meta data.
   *
   * @param fieldlabel label
   * @param setValue value
   */
  public void setFieldentry(String fieldlabel, String setValue)
  {
    if (log.isDebugEnabled())
    {
      log.debug(
        "setFieldentry(String " + fieldlabel + ", String " + setValue +
        ")");
    }

    String xpath =
      "section/qtimetadata/qtimetadatafield/fieldlabel[text()='" +
      fieldlabel +
      "']/following-sibling::fieldentry";
    super.setFieldentry(xpath, setValue);
  }

  /**
   * Method for meta data.
   *
   * @param fieldlabel to be added
   */
  public void createFieldentry(String fieldlabel)
  {
    if (log.isDebugEnabled())
    {
      log.debug("createFieldentry(String " + fieldlabel + ")");
    }

    String xpath = "section/qtimetadata";
    super.createFieldentry(xpath, fieldlabel);
  }

  /**
   * ASI OKI implementation
   *
   * @param itemId item id
   */
  public void addItemRef(String itemId)
  {
    if (log.isDebugEnabled())
    {
      log.debug("addItem(String " + itemId + ")");
    }

    try {
    	String xpath = basePath;
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = dbf.newDocumentBuilder();
    	Document document = db.newDocument();
    	Element element = document.createElement(QTIConstantStrings.ITEMREF);
    	element.setAttribute(QTIConstantStrings.LINKREFID, itemId);
    	this.addElement(xpath, element);
    } catch(ParserConfigurationException pce) {
    	log.error("Exception thrown from addItemRef() : " + pce.getMessage());
		pce.printStackTrace();
    }
  }

  /**
   * remove item ref
   *
   * @param itemId igem id
   */
  public void removeItemRef(String itemId)
  {
    if (log.isDebugEnabled())
    {
      log.debug("removeItem(String " + itemId + ")");
    }

    String xpath =
      basePath + "/" + QTIConstantStrings.ITEMREF + "[@" +
      QTIConstantStrings.LINKREFID + "=\"" + itemId + "\"]";
    this.removeElement(xpath);
  }

  /**
   * add section ref
   *
   * @param sectionId section id
   */
  public void addSectionRef(String sectionId)
  {
    if (log.isDebugEnabled())
    {
      log.debug("addSection(String " + sectionId + ")");
    }
    try {
    	String xpath = basePath;
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = dbf.newDocumentBuilder();
    	Document document = db.newDocument();
    	Element element = document.createElement(QTIConstantStrings.SECTIONREF);
    	element.setAttribute(QTIConstantStrings.LINKREFID, sectionId);
    	this.addElement(xpath, element);
    } catch(ParserConfigurationException pce) {
    	log.error("Exception thrown from addSectionRef() : " + pce.getMessage());
		pce.printStackTrace();
    }
  }

  /**
   * remove section ref
   *
   * @param sectionId DOCUMENTATION PENDING
   */
  public void removeSectionRef(String sectionId)
  {
    if (log.isDebugEnabled())
    {
      log.debug("removeSection(String " + sectionId + ")");
    }

    String xpath =
      basePath + "/" + QTIConstantStrings.SECTIONREF + "[@" +
      QTIConstantStrings.LINKREFID + "=" + sectionId + "]";
    this.removeElement(xpath);
  }

  /**
   * Order item refs
   *
   * @param itemRefIds list of ref ids
   */
  public void orderItemRefs(List<String> itemRefIds)
  {
    if (log.isDebugEnabled())
    {
      log.debug("orderItemRefs(ArrayList " + itemRefIds + ")");
    }

    this.removeItemRefs();
    int size = itemRefIds.size();
    for (int i = 0; i < size; i++)
    {
      this.addItemRef(itemRefIds.get(i));
    }
  }

  /**
   * remove item refs
   */
  private void removeItemRefs()
  {
    log.debug("removeItems()");

    String xpath = basePath + "/" + QTIConstantStrings.ITEMREF;
    this.removeElement(xpath);
  }

  /**
   * get section refs
   *
   * @return list of section refs
   */
  @SuppressWarnings("unchecked")
public List<Element> getSectionRefs()
  {
    log.debug("getSectionRefs()");
    String xpath = basePath + "/" + QTIConstantStrings.SECTIONREF;

    return this.selectNodes(xpath);
  }

  /**
   * get section ref ids
   *
   * @return list of section ref ids
   */
  public List<String> getSectionRefIds()
  {
    List<Element> refs = this.getSectionRefs();
    List<String> ids = new ArrayList<String>();
    int size = refs.size();
    for (int i = 0; i < size; i++)
    {
      Element ref = refs.get(0);
      String idString = ref.getAttribute(QTIConstantStrings.LINKREFID);
      ids.add(idString);
    }

    return ids;
  }

  /**
   * get Xpath of section
   * @return the Xpath
   */
  public String getBasePath()
  {
    return basePath;
  }

  /**
   * set XPath of section
   * @param basePath
   */
  public void setBasePath(String basePath)
  {
    this.basePath = basePath;
  }
  
  @SuppressWarnings("unchecked")
private String getAttachment(SectionDataIfc section) {
	  Set<AttachmentData> attachmentSet = section.getSectionAttachmentSet();
	  StringBuilder attachment = new StringBuilder();
	  for(AttachmentData attachmentData: attachmentSet){
   		attachment.append(attachmentData.getResourceId().replaceAll(" ", ""));
   		attachment.append("|");
   		attachment.append(attachmentData.getFilename());
   		attachment.append("|");
   		attachment.append(attachmentData.getMimeType());
   		attachment.append("\n");
   	  }
	  return attachment.toString();
  }

  	private StringBuilder buf = new StringBuilder();
	private void printItem(ItemDataIfc item) {
		pNewItem(item);
		p("ItemId", item.getItemId());//item/@ident
		p("ThemeText", item.getThemeText());//item/@label
		p("IsAnswerOptionsSimple", item.getIsAnswerOptionsSimple());//item/presentation/@label
		p("LeadInText", item.getLeadInText());//item/presentation/flow/material/mattext
		p("Sequence", item.getSequence());//item/presentation/flow/response_lid/@ident
		p("Score", item.getScore());//item/resprocessing/outcomes/decvar/@maxvalue
		p("Discount", item.getDiscount());//item/resprocessing/outcomes/decvar/@minvalue
		
		p("AnswerKey", item.getAnswerKey());
		p("AnswerOptionsRichCount", item.getAnswerOptionsRichCount());
//		p("CorrectItemFeedback", item.getCorrectItemFeedback());
		p("CreatedBy", item.getCreatedBy());
		p("CreatedDate", item.getCreatedDate());
//		p("Description", item.getDescription());
//		p("Duration", item.getDuration());
		p("EmiAnswerOptionLabels", item.getEmiAnswerOptionLabels());
		p("EmiAnswerOptionsRichText", item.getEmiAnswerOptionsRichText());
//		p("GeneralItemFeedback", item.getGeneralItemFeedback());
//		p("Grade", item.getGrade());
//		p("HasRationale", item.getHasRationale());
//		p("Hint", item.getHint());
//		p("InCorrectItemFeedback", item.getInCorrectItemFeedback());
//		p("Instruction", item.getInstruction());
//		p("IsTrue", item.getIsTrue());
		p("ItemIdString", item.getItemIdString());
		p("LastModifiedBy", item.getLastModifiedBy());
		p("LastModifiedDate", item.getLastModifiedDate());
		p("NumberOfCorrectEmiOptions", item.getNumberOfCorrectEmiOptions());
		p("PartialCreditFlag", item.getPartialCreditFlag());
		p("Status", item.getStatus());
//		p("Text", item.getText());
//		p("TriesAllowed", item.getTriesAllowed());
//		p("Type", item.getType());
//		p("TypeId", item.getTypeId());
//		pNewData("EmiAnswerOptions");///
//		for (AnswerIfc answer : item.getEmiAnswerOptions()) {
////			p("CorrectAnswerFeedback", answer.getCorrectAnswerFeedback());
//			p("Discount", answer.getDiscount());
////			p("GeneralAnswerFeedback", answer.getGeneralAnswerFeedback());
//			p("Grade", answer.getGrade());
//			p("Id", answer.getId());
////			p("InCorrectAnswerFeedback", answer.getInCorrectAnswerFeedback());
//			p("IsCorrect", answer.getIsCorrect());
//			p("Label", answer.getLabel());
//			p("PartialCredit", answer.getPartialCredit());
//			p("Score", answer.getScore());
//			p("Sequence", answer.getSequence());
//			p("Text", answer.getText());
//			p("ItemText", answer.getItemText());
////			pNewData("AnswerFeedbackSet");
////			for(AnswerFeedbackIfc af: answer.getAnswerFeedbackSet()){
////				p("", af.getId());
////				p("", af.getText());
////				p("", af.getTypeId());
////			}
////			pEndData();//AnswerFeedbackSet
//		}
//		pEndData();//EmiAnswerOptions
//		pNewData("EmiQuestionAnswerCombinations");XXX
//		for(ItemTextIfc it: item.getEmiQuestionAnswerCombinations()){
//			p(it.getId().toString(), it);
//		}
//		pEndData();//EmiQuestionAnswerCombinations
		pNewData("ItemAttachmentSet");
		for(ItemAttachmentIfc ia: item.getItemAttachmentSet()){
			p("AttachmentId", ia.getAttachmentId());
			p("AttachmentType", ia.getAttachmentType());
			p("CreatedBy", ia.getCreatedBy());
			p("CreatedDate", ia.getCreatedDate());
			p("Description", ia.getDescription());
			p("Filename", ia.getFilename());
			p("FileSize", ia.getFileSize());
			p("IsLink", ia.getIsLink());
			p("LastModifiedBy", ia.getLastModifiedBy());
			p("LastModifiedDate", ia.getLastModifiedDate());
			p("Location", ia.getLocation());
			p("MimeType", ia.getMimeType());
			p("ResourceId", ia.getResourceId());
			p("Status", ia.getStatus());
		}
		pEndData();//ItemAttachmentSet
//		pNewData("ItemFeedbackSet");
//		for(ItemFeedbackIfc ifb: item.getItemFeedbackSet()){
//			p("Id", ifb.getId());
//			p("Text", ifb.getText());
//			p("TypeId", ifb.getTypeId());
//		}
//		pEndData();//ItemFeedbackSet
//		pNewData("ItemMetaDataSet");//handled by global Item?
//		for(ItemMetaDataIfc im: item.getItemMetaDataSet()){
//			//p("Id", im.getId());
//			p("Label", im.getLabel());
//			p("Entry", im.getEntry());
//		}
//		pEndData();//ItemMetaDataSet
		pNewData("ItemTextSet");
		for(ItemTextIfc it: item.getItemTextSet()){
			p(it.getId().toString(), it);
		}
		pEndData();//ItemTextSet
		pEndData();//item
		log.fatal(buf.toString());
		buf.setLength(0);
	}
	
	private void p(String label, ItemTextIfc itemText){
		pNewData(label);
		p("EmiCorrectOptionLabels", itemText.getEmiCorrectOptionLabels());
		p("Id", itemText.getId());
		p("RequiredOptionsCount", itemText.getRequiredOptionsCount());
		p("Sequence", itemText.getSequence());
		p("Text", itemText.getText());
		p("HasAttachment", itemText.getHasAttachment());
		p("isEmiQuestionItemText", itemText.isEmiQuestionItemText());
		pNewData("AnswerSet");
		for(AnswerIfc answer: itemText.getAnswerSet()){
			pNewData(answer.getSequence().toString());
			p("Id", answer.getId());
			p("Sequence", answer.getSequence());
			p("Label", answer.getLabel());
			p("Text", answer.getText());
			
			p("IsCorrect", answer.getIsCorrect());
			p("Score", answer.getScore());
			p("Discount", answer.getDiscount());
			
//			p("CorrectAnswerFeedback", answer.getCorrectAnswerFeedback());
//			p("GeneralAnswerFeedback", answer.getGeneralAnswerFeedback());
//			p("Grade", answer.getGrade());
//			p("InCorrectAnswerFeedback", answer.getInCorrectAnswerFeedback());
//			p("PartialCredit", answer.getPartialCredit());
//			p("ItemText", answer.getItemText());//XXX Bad circular ref
//			pNewData("AnswerFeedbackSet");
//			for(AnswerFeedbackIfc af: answer.getAnswerFeedbackSet()){
//				p("", af.getId());
//				p("", af.getText());
//				p("", af.getTypeId());
//			}
//			pEndData();//AnswerFeedbackSet
			pEndData();//Answer
		}
		pEndData();//AnswerSet
		pNewData("ItemTextAttachmentSet");
		for(ItemTextAttachmentIfc ita: itemText.getItemTextAttachmentSet()){
			p("AttachmentId", ita.getAttachmentId());
			p("AttachmentType", ita.getAttachmentType());
			p("CreatedBy", ita.getCreatedBy());
			p("CreatedDate", ita.getCreatedDate());
			p("Description", ita.getDescription());
			p("Filename", ita.getFilename());
			p("FileSize", ita.getFileSize());
			p("IsLink", ita.getIsLink());
			p("LastModifiedBy", ita.getLastModifiedBy());
			p("LastModifiedDate", ita.getLastModifiedDate());
			p("Location", ita.getLocation());
			p("MimeType", ita.getMimeType());
			p("ResourceId", ita.getResourceId());
			p("Status", ita.getStatus());
		}
		pEndData();//ItemTextAttachmentSet
		pEndData();//ItemTextIfc
	}
	
	private int pTab = 0;

	private void pNewItem(ItemDataIfc item) {
		pTab = 0;
		p("************ " + item.getItemIdString() + ": " + item.getThemeText()
				+ " **************");
	}

	private void pNewData(String text) {
		p("----- " + text + " -----");
		pTab++;
	}

	private void pEndData() {
		pTab--;
		p("----- End -----");
	}
	
	@SuppressWarnings("unused")
	private void p(String label, Object text) {
		p(label, (text==null?"Object":text.getClass().getSimpleName()), (text==null?"null":text.toString()));
	}

	private void p(String label, String text) {
		p(label, "String", text);
	}

	private void p(String label, Boolean text) {
		p(label, "Boolean", String.valueOf(text));
	}

	private void p(String label, Integer text) {
		p(label, "Integer", String.valueOf(text));
	}

	private void p(String label, Long text) {
		p(label, "Long", String.valueOf(text));
	}

	private void p(String label, Float text) {
		p(label, "Float", String.valueOf(text));
	}

	private void p(String label, Date text) {
		p(label, "Date", String.valueOf(text));
	}

	private void p(String label, String type, String text) {
		p(label + "(" + type + "): " + text);
	}

	private void p(String text) {
		for(int i = 0; i < pTab; i++){
			buf.append("\t");
		}
		buf.append(text);
		buf.append("\n");
	}
}


