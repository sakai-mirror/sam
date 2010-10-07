<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
* $Id: extendedMatchingItems.jsp 59352 2009-03-31 16:59:41Z gopal@zestware.com $
<%--
***********************************************************************************
*
* Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
*
* Licensed under the Educational Community License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.osedu.org/licenses/ECL-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License. 
*
**********************************************************************************/
--%>
-->


  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{authorMessages.item_display_author}"/></title>
      <!-- AUTHORING -->
	  <link type="text/css" href="/samigo-app/css/ui-lightness/jquery-ui-1.7.2.custom.css" rel="Stylesheet" />	
	  <script type="text/javascript">var emiAuthoring=true</script>
      <samigo:script path="/js/jquery-1.3.2.min.js"/>
      <samigo:script path="/js/jquery-ui-1.7.2.custom.min.js"/>
      <samigo:script path="/js/authoring.js"/>
      </head>
<body onload="resetInsertAnswerSelectMenus();<%= request.getAttribute("html.body.onload") %>">

<div id="portletContent" class="portletBody" style="display:none;">
<!-- content... -->
<!-- FORM -->
<!-- HEADING -->
<%@ include file="/jsf/author/item/itemHeadings.jsp" %>
<h:form id="itemForm" onsubmit="return editorCheck();">
<p class="act">
  <h:commandButton accesskey="#{authorMessages.a_save}" rendered="#{itemauthor.target=='assessment'}" value="#{authorMessages.button_save}" action="#{itemauthor.currentItem.getOutcome}" styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>

  <h:commandButton accesskey="#{authorMessages.a_save}" rendered="#{itemauthor.target=='questionpool'}" value="#{authorMessages.button_save}" action="#{itemauthor.currentItem.getPoolOutcome}"  styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>


  <h:commandButton accesskey="#{authorMessages.a_cancel}" rendered="#{itemauthor.target=='assessment'}" value="#{authorMessages.button_cancel}" action="editAssessment" immediate="true">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ResetItemAttachmentListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.EditAssessmentListener" />
  </h:commandButton>

 <h:commandButton rendered="#{itemauthor.target=='questionpool'}" value="#{authorMessages.button_cancel}" action="editPool" immediate="true">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ResetItemAttachmentListener" />
 </h:commandButton>
</p>

  <!-- NOTE:  Had to call this.form.onsubmit(); -->
  <!-- and multiple choice, or adding additional answer choices.  -->
  <!-- to invoke the onsubmit() function for htmlarea to save the htmlarea contents to bean -->
  <!-- otherwise, when toggleing or adding more choices, all contents in wywisyg editor are lost -->

  <!-- QUESTION PROPERTIES -->
  <!-- this is for creating emi questions -->
  
  <!-- 1 POINTS and DISCOUNT -->
<div class="tier2">
     <div class="shorttext"> <h:outputLabel value="#{authorMessages.answer_point_value}" />
    <h:inputText id="answerptr" value="#{itemauthor.currentItem.itemScore}"required="true" size="6" onchange="toPoint(this.id);">
	<f:validateDoubleRange /></h:inputText>
	<h:message for="answerptr" styleClass="validate"/>
</div>
<br/>


<div id="discountDiv" class="shorttext">
  <h:outputLabel value="#{authorMessages.negative_point_value}"/>
  <h:inputText id="answerdsc" value="#{itemauthor.currentItem.itemDiscount}" required="true"  size="6" onchange="toPoint(this.id);">
    <f:validateDoubleRange />
  </h:inputText>
  <h:message for="answerdsc" styleClass="validate"/>
</div>
<br/>



  <!-- 2 QUESTION THEME TEXT -->

  <div class="longtext"> <h:outputLabel value="#{authorMessages.question_theme_text}" />
    <h:inputText id="themetext" value="#{itemauthor.currentItem.itemText}"required="true" size="60" >
    </h:inputText>
    <h:message for="themetext" styleClass="validate"/>
  </div>
  <br/>



  <!-- 3 ANSWER OPTIONS - SIMPLE OR RICH TEXT-->

  <h:outputLabel value="#{authorMessages.select_appropriate_format}" />
  <f:verbatim><br/></f:verbatim>
  <h:selectOneRadio id="emiAnswerOptionsSimpleOrRich" value="#{itemauthor.currentItem.answerOptionsSimpleOrRich}" layout="pageDirection" required="yes">
    <f:selectItem itemLabel="#{authorMessages.simple_text_option_label}" itemValue="0"/>
    <f:selectItem itemLabel="#{authorMessages.rich_text_option_label}" itemValue="1"/>
  </h:selectOneRadio>

  <f:verbatim><br/></f:verbatim>
  
  
  <!-- 3.1(a) ANSWER RICH - TABLE/GRAPHIC -->
<div id="emiAnswerOptionsRich">
 <div class="tier2">
  
   <div class="longtext"><h:outputLabel value="#{authorMessages.answer_options_rich}" />
   </div>

  <!-- WYSIWYG -->
  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.emiAnswerOptionsRich}" hasToggle="yes">
     <f:validateLength minimum="1" maximum="64000"/>
   </samigo:wysiwyg>

  </h:panelGrid>

 
 <!-- ATTACHMENTS BELOW - OPTIONS -->
 <div class="longtext">
  <h:panelGroup rendered="#{itemauthor.hasAttachment}">
    <h:dataTable value="#{itemauthor.attachmentList}" var="attach">
      <h:column>
        <%@ include file="/jsf/shared/mimeicon.jsp" %>
      </h:column>
      <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputLink value="#{attach.location}" target="new_window">
           <h:outputText escape="false" value="#{attach.filename}" />
        </h:outputLink>
      </h:column>
      <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputText escape="false" value="(#{attach.fileSize} #{generalMessages.kb})" rendered="#{!attach.isLink}"/>
      </h:column>
    </h:dataTable>
  </h:panelGroup>

  <h:panelGroup rendered="#{!itemauthor.hasAttachment}">
    <h:commandLink action="#{itemauthor.addAttachmentsRedirect}" value="" immediate="true">
       <f:verbatim><img src="/library/image/silk/attach.png" border="0"/></f:verbatim>
       <h:outputText value="#{authorMessages.add_attachments}"/>
    </h:commandLink>
  </h:panelGroup>

  <h:panelGroup rendered="#{itemauthor.hasAttachment}">
    <h:commandLink action="#{itemauthor.addAttachmentsRedirect}" value="" immediate="true">
       <f:verbatim><img src="/library/image/silk/attach.png" border="0"/></f:verbatim>
       <h:outputText value="#{authorMessages.add_remove_attachments}"/>
    </h:commandLink>
  </h:panelGroup>
 </div>
 <!-- ATTACHMENTS ABOVE - OPTIONS-->


  <f:verbatim><br/><br/></f:verbatim>


  <div class="shorttext tier2">
    <h:outputText value="#{authorMessages.answer_options_count}" />
    <h:selectOneMenu  id="answerOptionsRichCount"  value="#{itemauthor.currentItem.answerOptionsRichCount}" >
      <f:selectItem itemLabel="#{authorMessages.select_menu}" itemValue="0"/>
      <f:selectItem itemLabel="A-B(2)" itemValue="2"/>
      <f:selectItem itemLabel="A-C(3)" itemValue="3"/>
      <f:selectItem itemLabel="A-D(4)" itemValue="4"/>
      <f:selectItem itemLabel="A-E(5)" itemValue="5"/>
      <f:selectItem itemLabel="A-F(6)" itemValue="6"/>
      <f:selectItem itemLabel="A-G(7)" itemValue="7"/>
      <f:selectItem itemLabel="A-H(8)" itemValue="8"/>
      <f:selectItem itemLabel="A-I(9)" itemValue="9"/>
      <f:selectItem itemLabel="A-J(10)" itemValue="10"/>
      <f:selectItem itemLabel="A-B(11" itemValue="11"/>
      <f:selectItem itemLabel="A-B(12" itemValue="12"/>
      <f:selectItem itemLabel="A-B(13" itemValue="13"/>
      <f:selectItem itemLabel="A-B(14" itemValue="14"/>
      <f:selectItem itemLabel="A-B(15" itemValue="15"/>
      <f:selectItem itemLabel="A-B(16" itemValue="16"/>
      <f:selectItem itemLabel="A-B(17" itemValue="17"/>
      <f:selectItem itemLabel="A-B(18" itemValue="18"/>
      <f:selectItem itemLabel="A-B(19" itemValue="19"/>
      <f:selectItem itemLabel="20" itemValue="20"/>
      <f:selectItem itemLabel="21" itemValue="21"/>
      <f:selectItem itemLabel="22" itemValue="22"/>
      <f:selectItem itemLabel="23" itemValue="23"/>
      <f:selectItem itemLabel="24" itemValue="24"/>
      <f:selectItem itemLabel="25" itemValue="25"/>
      <f:selectItem itemLabel="26" itemValue="26"/>
    </h:selectOneMenu>
  </div>

  </div>
</div>



  <!-- 3.1(b) ANSWER OPTIONS - SIMPLE -->

<!-- dynamicaly generate rows of answer options -->

<div id="emiAnswerOptionsSimple">
 <div class="tier2">

 <h:panelGrid columns="2">
   <h:panelGroup>

     <div class="longtext"><h:outputLabel value="#{authorMessages.answer_options}"/></div>

     <h:dataTable id="emiAnswerOptions" value="#{itemauthor.currentItem.emiAnswerOptions}" var="answer" headerClass="navView longtext">
     <h:column>
       <h:panelGrid id="Row" columns="3">

         <h:panelGroup>
           <h:outputText id="Label" value="#{answer.label}"  />
         </h:panelGroup>
  
         <h:panelGrid>
           <h:inputText id="Text" value="#{answer.text}" size="40" >
           </h:inputText>
         </h:panelGrid>

         <h:panelGroup>
           <h:outputLink id="RemoveLink" title="#{authorMessages.t_removeO}" rendered="#{itemauthor.currentItem.itemType == 13}">
             <f:verbatim><img src="/library/image/silk/cross.png" border="0"></f:verbatim>
           </h:outputLink>		 
         </h:panelGroup>

       </h:panelGrid>
     </h:column>
     </h:dataTable>

   </h:panelGroup>


   <h:panelGrid>
     <div class="longtext"><h:outputLabel id="pasteLabel" value="#{authorMessages.answer_options_paste}"/></div>
     <h:inputTextarea id="emiAnswerOptionsPaste" rows="6" cols="50" value="#{itemauthor.currentItem.emiAnswerOptionsPaste}">
     </h:inputTextarea>
   </h:panelGrid>
   

  </h:panelGrid>

 </div>


 <div class="shorttext tier2">
  <h:outputLink id="addEmiAnswerOptionsLink" >
    <f:verbatim><img src="/library/image/silk/add.png" border="0"></f:verbatim>
    <h:outputText value="#{authorMessages.add_more_options}"/>
  </h:outputLink>		 
 </div>

</div>



  <!-- 4 LEAD IN STATEMENT -->

<br/><br/>

  <div class="longtext"><h:outputLabel value="#{authorMessages.lead_in_statement}" /></div>
  <!-- WYSIWYG -->
  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.leadInStatement}" hasToggle="yes">
     <f:validateLength minimum="1" maximum="64000"/>
   </samigo:wysiwyg>
  </h:panelGrid>

<br/>




  <!-- 4 QUESTION-ANSWER COMBINATIONS -->

  <div class="longtext">
    <h:outputLabel value="#{authorMessages.question_answer_combinations} " />  </div>

<!-- dynamicaly generate rows of question-answer combos -->
<div class="tier2">

 <h:dataTable id="emiQuestionAnswerCombinations" value="#{itemauthor.currentItem.emiQuestionAnswerCombinationsUI}" var="answer" headerClass="navView longtext">
 <h:column>

 <h:panelGrid id="Row" columns="4">
  <h:panelGroup>
    <h:outputText id="XX_Label" value="#{answer.label}" rendered="false" />
    <h:inputText id="Label" value="#{answer.label}" size="1"/>
    
    <f:verbatim><br/></f:verbatim>
  </h:panelGroup>
  
  <!-- WYSIWYG -->
  <h:panelGrid>
    <samigo:wysiwyg rows="140" value="#{answer.text}" hasToggle="yes">
      <f:validateLength maximum="64000"/>
    </samigo:wysiwyg>
    
    
 <!-- ATTACHMENTS BELOW - ITEMS-->
 <div class="longtext">
  <h:panelGroup rendered="#{answer.hasAttachment}">
    <h:dataTable value="#{answer.attachmentList}" var="attach">
      <h:column>
        <%@ include file="/jsf/shared/mimeicon.jsp" %>
      </h:column>
      <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputLink value="#{attach.location}" target="new_window">
           <h:outputText escape="false" value="#{attach.filename}" />
        </h:outputLink>
      </h:column>
      <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputText escape="false" value="(#{attach.fileSize} #{generalMessages.kb})" rendered="#{!attach.isLink}"/>
      </h:column>
    </h:dataTable>
  </h:panelGroup>

  <h:panelGroup rendered="#{!answer.hasAttachment}">
    <h:commandLink action="#{answer.addAttachmentsRedirect}" value="" immediate="true">
       <f:verbatim><img src="/library/image/silk/attach.png" border="0"/></f:verbatim>
       <h:outputText value="#{authorMessages.add_attachments}"/>
    </h:commandLink>
  </h:panelGroup>

  <h:panelGroup rendered="#{answer.hasAttachment}">
    <h:commandLink action="#{answer.addAttachmentsRedirect}" value="" immediate="true">
       <f:verbatim><img src="/library/image/silk/attach.png" border="0"/></f:verbatim>
       <h:outputText value="#{authorMessages.add_remove_attachments}"/>
    </h:commandLink>
  </h:panelGroup>
 </div>
 <!-- ATTACHMENTS ABOVE - ITEMS -->


    
 </h:panelGrid>

			
  <h:panelGroup>
    <h:outputLabel value="#{authorMessages.correct_option_labels}" />
    <f:verbatim><br/></f:verbatim>
    <h:inputText id="correctOptionLabels" value="#{answer.correctOptionLabels}" size="6" style="text-transform:uppercase;"/>
    <f:verbatim><br/><br/></f:verbatim>
       
    <h:outputText value="#{authorMessages.required_options_count}" />
    <f:verbatim><br/></f:verbatim>
    <h:selectOneMenu  id="requiredOptionsCount"  onchange="this.form.onsubmit(); clickAddEmiAnswerOptionsCountLink();" value="#{answer.requiredOptionsCount}" >
      <f:selectItem itemLabel="#{authorMessages.all}" itemValue="0"/>
      <f:selectItem itemLabel="1" itemValue="1"/>
      <f:selectItem itemLabel="2" itemValue="2"/>
      <f:selectItem itemLabel="3" itemValue="3"/>
      <f:selectItem itemLabel="4" itemValue="4"/>
      <f:selectItem itemLabel="5" itemValue="5"/>
      <f:selectItem itemLabel="6" itemValue="6"/>
      <f:selectItem itemLabel="7" itemValue="7"/>
      <f:selectItem itemLabel="8" itemValue="8"/>
      <f:selectItem itemLabel="9" itemValue="9"/>
      <f:selectItem itemLabel="10" itemValue="10"/>
      <f:selectItem itemLabel="11" itemValue="11"/>
      <f:selectItem itemLabel="12" itemValue="12"/>
      <f:selectItem itemLabel="13" itemValue="13"/>
      <f:selectItem itemLabel="14" itemValue="14"/>
      <f:selectItem itemLabel="15" itemValue="15"/>
      <f:selectItem itemLabel="16" itemValue="16"/>
      <f:selectItem itemLabel="17" itemValue="17"/>
      <f:selectItem itemLabel="18" itemValue="18"/>
      <f:selectItem itemLabel="19" itemValue="19"/>
      <f:selectItem itemLabel="20" itemValue="20"/>
      <f:selectItem itemLabel="21" itemValue="21"/>
      <f:selectItem itemLabel="22" itemValue="22"/>
      <f:selectItem itemLabel="23" itemValue="23"/>
      <f:selectItem itemLabel="24" itemValue="24"/>
      <f:selectItem itemLabel="25" itemValue="25"/>
      <f:selectItem itemLabel="26" itemValue="26"/>
    </h:selectOneMenu>
       
  </h:panelGroup>
			
  <h:panelGroup>
     <h:outputLink id="RemoveLink" title="#{authorMessages.t_removeI}" rendered="true">
       <f:verbatim><img src="/library/image/silk/cross.png" border="0"></f:verbatim>
     </h:outputLink>		 
  
  
    <h:commandLink title="#{authorMessages.t_removeI}" id="XX_RemoveLink" onfocus="document.forms[1].onsubmit();" action="#{itemauthor.currentItem.removeEmiQuestionAnswerCombinations}" rendered="false">
      <f:verbatim><img src="/library/image/silk/cross.png" border="0"></f:verbatim>
      <f:param name="emiQuestionAnswerComboId" value="#{answer.label}"/>
    </h:commandLink>		 
  </h:panelGroup>
			
  
</h:panelGrid>
        
</h:column>
</h:dataTable>

</div>

<div class="shorttext tier2">
  <h:outputLink id="addEmiQuestionAnswerCombinationsLink" rendered="true">
    <f:verbatim><img src="/library/image/silk/add.png" border="0"></f:verbatim>
    <h:outputText value="#{authorMessages.add_more_items}"/>
  </h:outputLink>		 


  <h:commandLink id="XX_addEmiQuestionAnswerCombinationsLink" action="#{itemauthor.currentItem.addEmiQuestionAnswerCombinationsAction}" rendered="false">
    <f:verbatim><img src="/library/image/silk/add.png" border="0"></f:verbatim>
    <h:outputText value="#{authorMessages.add_more_items}"/>
  </h:commandLink>
</div>
<br/>


    <!-- 5 PART -->
<h:panelGrid columns="3"  columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">
<f:verbatim>&nbsp;</f:verbatim>
<h:outputLabel value="#{authorMessages.assign_to_p} " />
  <h:selectOneMenu id="assignToPart" value="#{itemauthor.currentItem.selectedSection}">
     <f:selectItems value="#{itemauthor.sectionSelectList}" />
  </h:selectOneMenu>
</h:panelGrid>


    <!-- 6 POOL -->

<h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment' && author.isEditPendingAssessmentFlow}">
<f:verbatim>&nbsp;</f:verbatim>
  <h:outputLabel value="#{authorMessages.assign_to_question_p} " />
  <h:selectOneMenu rendered="#{itemauthor.target == 'assessment'}" id="assignToPool" value="#{itemauthor.currentItem.selectedPool}">
     <f:selectItem itemValue="" itemLabel="#{authorMessages.select_a_pool_name}" />
     <f:selectItems value="#{itemauthor.poolSelectList}" />
  </h:selectOneMenu>
</h:panelGrid>



 <!-- METADATA -->
<h:panelGroup rendered="#{itemauthor.showMetadata == 'true'}" styleClass="longtext">
<f:verbatim></f:verbatim>
<h:outputLabel value="Metadata"/><br/>


<h:panelGrid columns="2" columnClasses="shorttext">
<h:outputText value="#{authorMessages.objective}" />
  <h:inputText size="30" id="obj" value="#{itemauthor.currentItem.objective}" />
<h:outputText value="#{authorMessages.keyword}" />
  <h:inputText size="30" id="keyword" value="#{itemauthor.currentItem.keyword}" />
<h:outputText value="#{authorMessages.rubric_colon}" />
  <h:inputText size="30" id="rubric" value="#{itemauthor.currentItem.rubric}" />
</h:panelGrid>
</h:panelGroup>
</div>

<p class="act">
  <h:commandButton accesskey="#{authorMessages.a_save}" rendered="#{itemauthor.target=='assessment'}" value="#{authorMessages.button_save}" action="#{itemauthor.currentItem.getOutcome}" styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>

  <h:commandButton accesskey="#{authorMessages.a_save}" rendered="#{itemauthor.target=='questionpool'}" value="#{authorMessages.button_save}" action="#{itemauthor.currentItem.getPoolOutcome}"  styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>


  <h:commandButton accesskey="#{authorMessages.a_cancel}" rendered="#{itemauthor.target=='assessment'}" value="#{authorMessages.button_cancel}" action="editAssessment" immediate="true">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ResetItemAttachmentListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.EditAssessmentListener" />
  </h:commandButton>

 <h:commandButton rendered="#{itemauthor.target=='questionpool'}" value="#{authorMessages.button_cancel}" action="editPool" immediate="true">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ResetItemAttachmentListener" />
 </h:commandButton>

</p>
</h:form>

	
    <!-- TEXT for JavaScript Processing-->
    <h:inputHidden id="all" value="#{authorMessages.all}" />


    <!-- ERROR MESSAGES for JavaScript Processing-->
    <h:inputHidden id="error_dialog_title_line1" value="#{authorMessages.error_dialog_title_line1}" />
    <h:inputHidden id="error_dialog_title_line2" value="#{authorMessages.error_dialog_title_line2}" />
    <h:inputHidden id="answer_point_value_error" value="#{authorMessages.answer_point_value_error}" />
    <h:inputHidden id="theme_text_error" value="#{authorMessages.theme_text_error}" />
    <h:inputHidden id="simple_text_options_blank_error" value="#{authorMessages.simple_text_options_blank_error}" />
    <h:inputHidden id="number_of_rich_text_options_error" value="#{authorMessages.number_of_rich_text_options_error}" />
    <h:inputHidden id="blank_or_non_integer_item_sequence_error" value="#{authorMessages.blank_or_non_integer_item_sequence_error}" />
    <h:inputHidden id="correct_option_labels_error" value="#{authorMessages.correct_option_labels_error}" />
    <h:inputHidden id="item_text_not_entered_error" value="#{authorMessages.item_text_not_entered_error}" />
    <h:inputHidden id="correct_option_labels_invalid_error" value="#{authorMessages.correct_option_labels_invalid_error}" />




<!-- end content -->
</div>

    </body>
  </html>
</f:view>

