<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
* $Id: multipleChoice.jsp 59352 2009-03-31 16:59:41Z arwhyte@umich.edu $
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

******************************************************************************************
***** GOPAL TEST/TEMP - /jsf/author/item/extendedMatchingItem.jsp ************************
******************************************************************************************



<%-- "checked in wysiwyg code but disabled, added in lydia's changes between 1.9 and 1.10" --%>
  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{authorMessages.item_display_author}"/></title>
      <!-- AUTHORING -->
      <samigo:script path="/js/authoring.js"/>
      </head>
<body onload="resetInsertAnswerSelectMenus();<%= request.getAttribute("html.body.onload") %>">

<div class="portletBody">
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

  <!-- NOTE:  Had to call this.form.onsubmit(); when toggling between single  -->
  <!-- and multiple choice, or adding additional answer choices.  -->
  <!-- to invoke the onsubmit() function for htmlarea to save the htmlarea contents to bean -->
  <!-- otherwise, when toggleing or adding more choices, all contents in wywisyg editor are lost -->

  <!-- QUESTION PROPERTIES -->
  <!-- this is for creating multiple choice questions -->
  <!-- 1 POINTS -->
<div class="tier2">

     <div class="shorttext"> <h:outputLabel value="#{authorMessages.answer_point_value}" />
    <h:inputText id="answerptr" value="#{itemauthor.currentItem.itemScore}"required="true" size="6" >
<f:validateDoubleRange /></h:inputText>

<h:message for="answerptr" styleClass="validate"/>
</div>
<br/>

<!-- DISCOUNT -->
<div class="longtext">
<h:panelGrid columns="2" border="0" rendered="#{itemauthor.currentItem.itemType == 13}">
  <h:panelGrid border="0">
    <h:outputLabel value="#{authorMessages.negative_point_value}"/>
    <h:outputText value="&nbsp;" escape="false"/>
  </h:panelGrid>
  <h:panelGrid border="0">
    <h:panelGroup>
    <h:inputText id="answerdsc" value="#{itemauthor.currentItem.itemDiscount}" required="true" >
  	  <f:validateDoubleRange />
    </h:inputText>
    <h:message for="answerdsc" styleClass="validate"/>
    </h:panelGroup>
    <h:outputText value="#{authorMessages.note_negative_point_value_question}" />
  </h:panelGrid>
</h:panelGrid>
</div>


  <!-- 2 QUESTION THEME TEXT -->

   <div class="longtext"><h:outputLabel value="#{authorMessages.question_theme_text}" />
</div>
  <!-- WYSIWYG -->
   
  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.itemText}" hasToggle="yes">
     <f:validateLength minimum="1" maximum="4000"/>
   </samigo:wysiwyg>

  </h:panelGrid>

  <!-- 2a ATTACHMENTS -->
  <%@ include file="/jsf/author/item/attachment.jsp" %>



  <!-- 3 ANSWER OPTIONS -->
  <div class="longtext">
    <h:outputLabel value="#{authorMessages.answer_options} " />  </div>

<!-- dynamicaly generate rows of answer options -->
<div class="tier2">
 <h:dataTable id="emiAnswerOptions" value="#{itemauthor.currentItem.emiAnswerOptions}" var="answer" headerClass="navView longtext">
<h:column>

<h:panelGrid columns="2">
  <h:panelGroup>
	
    <h:outputText value="#{answer.label}"  />
    <f:verbatim><br/></f:verbatim>
    <h:commandLink title="#{authorMessages.t_removeC}" id="removelink" onfocus="document.forms[1].onsubmit();" action="#{itemauthor.currentItem.removeEmiAnswerOptions}" rendered="#{itemauthor.currentItem.itemType == 13}">
      <h:outputText id="text" value="#{authorMessages.button_remove}"/>
      <f:param name="emiAnswerOptionId" value="#{answer.label}"/>
    </h:commandLink>		 

  </h:panelGroup>
  
        <!-- WYSIWYG -->
  <h:panelGrid>
    <samigo:wysiwyg rows="140" value="#{answer.text}" hasToggle="yes" >
      <f:validateLength maximum="4000"/>
    </samigo:wysiwyg>
  </h:panelGrid>
			
         <h:outputText value="#{authorMessages.feedback_optional}" rendered="#{itemauthor.target == 'questionpool' || (itemauthor.target != 'questionpool' && (author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '1') || (!author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '1'))}" />

        <!-- WYSIWYG -->
  <h:panelGrid rendered="#{itemauthor.target == 'questionpool' || (itemauthor.target != 'questionpool' && (author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '1') || (!author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '1'))}">
         <samigo:wysiwyg rows="140" value="#{answer.feedback}" hasToggle="yes" >
           <f:validateLength maximum="4000"/>
         </samigo:wysiwyg>
  </h:panelGrid>
</h:panelGrid>
        
</h:column>
</h:dataTable>

</div>

<div class="shorttext tier2">
  <h:outputText value="#{authorMessages.insert_additional_answer_options}" />
<h:selectOneMenu  id="insertAdditionalAnswerSelectMenu"  onchange="this.form.onsubmit(); clickAddEmiAnswerOptionsLink();" value="#{itemauthor.currentItem.additionalEmiAnswerOptions}" >
  <f:selectItem itemLabel="#{authorMessages.select_menu}" itemValue="0"/>
  <f:selectItem itemLabel="1" itemValue="1"/>
  <f:selectItem itemLabel="2" itemValue="2"/>
  <f:selectItem itemLabel="3" itemValue="3"/>
  <f:selectItem itemLabel="4" itemValue="4"/>
  <f:selectItem itemLabel="5" itemValue="5"/>
  <f:selectItem itemLabel="6" itemValue="6"/>
</h:selectOneMenu>
<h:commandLink id="hiddenAddEmiAnswerOptionsActionlink" action="#{itemauthor.currentItem.addEmiAnswerOptionsAction}" value="">
</h:commandLink>
</div>
<br/>



  <!-- 4 LEAD IN STATEMENT -->

  <div class="longtext"><h:outputLabel value="#{authorMessages.lead_in_statement}" /></div>
  <!-- WYSIWYG -->
  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.leadInStatement}" hasToggle="yes">
     <f:validateLength minimum="1" maximum="4000"/>
   </samigo:wysiwyg>

  </h:panelGrid>

<br/>




  <!-- 4 QUESTION-ANSWER COMBINATIONS -->

  <div class="longtext">
    <h:outputLabel value="#{authorMessages.question_answer_combinations} " />  </div>

<!-- dynamicaly generate rows of question-answer combos -->
<div class="tier2">
  <h:outputText value="#{authorMessages.note_emi_correct_options}" />
  <br/>
  <h:outputText value="#{authorMessages.note_emi_separate}" />
  <br/>
  <h:outputText value="#{authorMessages.note_emi_example}" />
  <br/>

 <h:dataTable id="emiQuestionAnswerCombinations" value="#{itemauthor.currentItem.emiQuestionAnswerCombinations}" var="answer" headerClass="navView longtext">
 <h:column>

 <h:panelGrid columns="2">
  <h:panelGroup>
	
    <h:outputText value="#{answer.label}"  />
    <f:verbatim><br/></f:verbatim>
    <h:commandLink title="#{authorMessages.t_removeC}" id="removelink" onfocus="document.forms[1].onsubmit();" action="#{itemauthor.currentItem.removeEmiQuestionAnswerCombinations}" rendered="#{itemauthor.currentItem.itemType == 13}">
      <h:outputText id="text" value="#{authorMessages.button_remove}"/>
      <f:param name="emiQuestionAnswerComboId" value="#{answer.label}"/>
    </h:commandLink>		 

  </h:panelGroup>
  
        <!-- WYSIWYG -->
  <h:panelGrid>
    <samigo:wysiwyg rows="140" value="#{answer.text}" hasToggle="yes" >
      <f:validateLength maximum="4000"/>
    </samigo:wysiwyg>
  </h:panelGrid>
			
         <h:outputText value="#{authorMessages.feedback_optional}" rendered="#{itemauthor.target == 'questionpool' || (itemauthor.target != 'questionpool' && (author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '1') || (!author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '1'))}" />

        <!-- WYSIWYG -->
  <h:panelGrid rendered="#{itemauthor.target == 'questionpool' || (itemauthor.target != 'questionpool' && (author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '1') || (!author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '1'))}">
         <samigo:wysiwyg rows="140" value="#{answer.feedback}" hasToggle="yes" >
           <f:validateLength maximum="4000"/>
         </samigo:wysiwyg>
  </h:panelGrid>
</h:panelGrid>
        
</h:column>
</h:dataTable>

</div>

<div class="shorttext tier2">
  <h:outputText value="#{authorMessages.insert_additional_q_a_combos}" />
<h:selectOneMenu  id="insertAdditionalEmiQAComboSelectMenu"  onchange="this.form.onsubmit(); clickAddEmiQuestionAnswerCombinationsLink();" value="#{itemauthor.currentItem.additionalEmiQuestionAnswerCombinations}" >
  <f:selectItem itemLabel="#{authorMessages.select_menu}" itemValue="0"/>
  <f:selectItem itemLabel="1" itemValue="1"/>
  <f:selectItem itemLabel="2" itemValue="2"/>
  <f:selectItem itemLabel="3" itemValue="3"/>
  <f:selectItem itemLabel="4" itemValue="4"/>
  <f:selectItem itemLabel="5" itemValue="5"/>
  <f:selectItem itemLabel="6" itemValue="6"/>
</h:selectOneMenu>
<h:commandLink id="hiddenAddEmiQuestionAnswerCombinationsActionlink" action="#{itemauthor.currentItem.addEmiQuestionAnswerCombinationsAction}" value="">
</h:commandLink>
</div>
<br/>


  <!-- 4 RANDOMIZE -->
  <div class="longtext">
    <h:outputLabel value="#{authorMessages.randomize_answers}" />    </div>
<div class="tier3">
    <h:selectOneRadio value="#{itemauthor.currentItem.randomized}" >
     <f:selectItem itemValue="true"
       itemLabel="#{authorMessages.yes}" />
     <f:selectItem itemValue="false"
       itemLabel="#{authorMessages.no}" />
    </h:selectOneRadio>
  </div>

    <!-- 5 RATIONALE -->
   <div class="longtext">
 <h:outputLabel value="#{authorMessages.require_rationale}" /></div>
<div class="tier3">
    <h:selectOneRadio value="#{itemauthor.currentItem.rationale}" >
     <f:selectItem itemValue="true" itemLabel="#{authorMessages.yes}"/>
     <f:selectItem itemValue="false" itemLabel="#{authorMessages.no}" />
    </h:selectOneRadio>
</div>
    <!-- 6 PART -->
<h:panelGrid columns="3"  columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">
<f:verbatim>&nbsp;</f:verbatim>
<h:outputLabel value="#{authorMessages.assign_to_p} " />
  <h:selectOneMenu id="assignToPart" value="#{itemauthor.currentItem.selectedSection}">
     <f:selectItems value="#{itemauthor.sectionSelectList}" />
  </h:selectOneMenu>
</h:panelGrid>


    <!-- 7 POOL -->

<h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment' && author.isEditPendingAssessmentFlow}">
<f:verbatim>&nbsp;</f:verbatim>
  <h:outputLabel value="#{authorMessages.assign_to_question_p} " />
  <h:selectOneMenu rendered="#{itemauthor.target == 'assessment'}" id="assignToPool" value="#{itemauthor.currentItem.selectedPool}">
     <f:selectItem itemValue="" itemLabel="#{authorMessages.select_a_pool_name}" />
     <f:selectItems value="#{itemauthor.poolSelectList}" />
  </h:selectOneMenu>

</h:panelGrid>


 <!-- 8 FEEDBACK -->
<h:panelGroup rendered="#{itemauthor.target == 'questionpool' || (itemauthor.target != 'questionpool' && (author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '2') || (!author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '2'))}">
 <h:outputText value=" " escape="false"/>
 <f:verbatim> <div class="longtext"></f:verbatim>
  <h:outputLabel value="#{authorMessages.correct_incorrect_an}" />
 <f:verbatim></div> </f:verbatim>
 <f:verbatim><div class="tier2"> </f:verbatim>
  <h:outputText value="#{authorMessages.correct_answer_opti}" />
<br/>
  <!-- WYSIWYG --> 
<h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.corrFeedback}" hasToggle="yes" >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
</h:panelGrid>
 <f:verbatim><br/> </f:verbatim>
 <h:outputText value="#{authorMessages.incorrect_answer_op}" />

  <!-- WYSIWYG -->
   <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.incorrFeedback}"  hasToggle="yes" >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
</h:panelGrid>
 <f:verbatim></div> </f:verbatim>
</h:panelGroup>

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
<!-- end content -->
</div>

    </body>
  </html>
</f:view>

