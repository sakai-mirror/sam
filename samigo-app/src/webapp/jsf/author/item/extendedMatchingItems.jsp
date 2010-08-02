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


***** GOPAL TEST/TEMP - /jsf/author/item/extendedMatchingItems.jsp ************************



<%-- "checked in wysiwyg code but disabled, added in lydia's changes between 1.9 and 1.10" --%>
  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{authorMessages.item_display_author}"/></title>
      <!-- AUTHORING -->
      <samigo:script path="/js/jquery-1.3.2.min.js"/>
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

  <!-- NOTE:  Had to call this.form.onsubmit(); -->
  <!-- and multiple choice, or adding additional answer choices.  -->
  <!-- to invoke the onsubmit() function for htmlarea to save the htmlarea contents to bean -->
  <!-- otherwise, when toggleing or adding more choices, all contents in wywisyg editor are lost -->

  <!-- QUESTION PROPERTIES -->
  <!-- this is for creating emi questions -->
  
  <!-- 1 POINTS -->
<div class="tier2">
     <div class="shorttext"> <h:outputLabel value="#{authorMessages.answer_point_value}" />
    <h:inputText id="answerptr" value="#{itemauthor.currentItem.itemScore}"required="true" size="6" >
<f:validateDoubleRange /></h:inputText>

<h:message for="answerptr" styleClass="validate"/>
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
  <h:selectOneRadio id="emiAnswerOptionsSimpleOrRich" value="#{itemauthor.currentItem.emiAnswerOptionsSimpleOrRich}" layout="pageDirection" required="yes">
    <f:selectItem itemLabel="Simple text – for a list of items with no formatting" itemValue="0"/>
    <f:selectItem itemLabel="Rich text / attachments – for styled text, tables, labelled images" itemValue="1"/>
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

 
 <!-- ATTACHMENTS BELOW-->
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
 <!-- ATTACHMENTS ABOVE-->


  <f:verbatim><br/><br/></f:verbatim>


  <div class="shorttext tier2">
    <h:outputText value="#{authorMessages.answer_options_count}" />
    <h:selectOneMenu  id="answerOptionsCount"  onchange="this.form.onsubmit(); clickAddEmiAnswerOptionsCountLink();" value="#{itemauthor.currentItem.emiAnswerOptionsCount}" >
      <f:selectItem itemLabel="#{authorMessages.select_menu}" itemValue="0"/>
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
    <h:commandLink id="hiddenAddEmiAnswerOptionsCountActionlink" action="#{itemauthor.currentItem.addEmiAnswerOptionsCountAction}" value="">
  </h:commandLink>
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
     <div class="longtext"><h:outputLabel value="#{authorMessages.answer_options_paste}"/></div>
     <h:inputTextarea id="emiAnswerOptionsPaste" rows="6" cols="50" value="#{itemauthor.currentItem.emiAnswerOptionsPaste}">
     </h:inputTextarea>
     <!-- gopalrc TODO - Move population with pasted text from ItemBean into ItemAddListener -->
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

 <h:dataTable id="emiQuestionAnswerCombinations" value="#{itemauthor.currentItem.emiQuestionAnswerCombinations}" var="answer" headerClass="navView longtext">
 <h:column>

 <h:panelGrid id="Row" columns="4">
  <h:panelGroup>
    <h:outputText id="Label" value="#{answer.label}"  />
    <f:verbatim><br/></f:verbatim>
  </h:panelGroup>
  
  <!-- WYSIWYG -->
  <h:panelGrid>
    <samigo:wysiwyg rows="140" value="#{answer.text}" hasToggle="yes" >
      <f:validateLength maximum="64000"/>
    </samigo:wysiwyg>
    
    
 <!-- ATTACHMENTS BELOW-->
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
 <!-- ATTACHMENTS ABOVE-->


    
 </h:panelGrid>

			
  <h:panelGroup>
    <h:outputLabel value="#{authorMessages.correct_option_labels}" />
    <f:verbatim><br/></f:verbatim>
    <h:inputText id="correctOptionLabels" value="#{answer.correctOptionLabels}" size="6" style="text-transform:uppercase;"
       validator="#{answer.validateCorrectOptionLabels}"/>
       
    <f:verbatim><br/><br/></f:verbatim>
       
    <h:outputText value="#{authorMessages.required_options_count}" />
    <f:verbatim><br/></f:verbatim>
    <h:selectOneMenu  id="requiredOptionsCount"  onchange="this.form.onsubmit(); clickAddEmiAnswerOptionsCountLink();" value="#{answer.requiredOptionsCount}" >
      <f:selectItem itemLabel="#{authorMessages.select_menu}" itemValue="0"/>
      <f:selectItem itemLabel="2" itemValue="2"/>
      <f:selectItem itemLabel="3" itemValue="3"/>
      <f:selectItem itemLabel="4" itemValue="4"/>
    </h:selectOneMenu>
    <h:commandLink id="hiddenAddEmiAnswerOptionsCountActionlink" action="#{itemauthor.currentItem.addEmiAnswerOptionsCountAction}" value="">
    </h:commandLink>
       
  </h:panelGroup>
			
  <h:panelGroup>
   <h:outputLink id="RemoveLink" title="#{authorMessages.t_removeI}" rendered="#{itemauthor.currentItem.itemType == 13}">
     <f:verbatim><img src="/library/image/silk/cross.png" border="0"></f:verbatim>
   </h:outputLink>		
  </h:panelGroup>
			
  
</h:panelGrid>
        
</h:column>
</h:dataTable>

</div>

<div class="shorttext tier2">
  <h:outputLink id="addEmiQuestionAnswerCombinationsLink" >
    <f:verbatim><img src="/library/image/silk/add.png" border="0"></f:verbatim>
    <h:outputText value="#{authorMessages.add_more_items}"/>
  </h:outputLink>		 
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
<!-- end content -->
</div>

    </body>
  </html>
</f:view>

