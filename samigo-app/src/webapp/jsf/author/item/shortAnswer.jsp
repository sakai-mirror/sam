
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
* $Id$
<%--
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
--%>
-->
  <f:view>
    <!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{authorMessages.item_display_author}"/></title>
      <samigo:script path="/js/authoring.js"/>
      </head>
<body onload="countNum();<%= request.getAttribute("html.body.onload") %>">

<div class="portletBody">
<!-- content... -->
<!-- FORM -->



<!-- HEADING -->
<%@ include file="/jsf/author/item/itemHeadings.jsp" %>
<h:form id="itemForm">
<!-- QUESTION PROPERTIES -->
  <!-- 1 POINTS -->
  <div class="tier2">
    <div class="shorttext">  <h:outputLabel value="#{authorMessages.answer_point_value}" />
    <h:inputText id="answerptr" value="#{itemauthor.currentItem.itemScore}" required="true">
	<f:validateDoubleRange/>
	</h:inputText>
	<br/> 
	<h:message for="answerptr" styleClass="validate"/>
	</div>
	<br/>
  <!-- 2 TEXT -->
   <div class="longtext"><h:outputLabel value="#{authorMessages.q_text}" />
  <br/>
  <!-- WYSIWYG -->

  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.itemText}" hasToggle="yes">
     <f:validateLength minimum="1" maximum="4000"/>
   </samigo:wysiwyg>

  </h:panelGrid>
  </div>

  <!-- 2a ATTACHMENTS -->
  <%@ include file="/jsf/author/item/attachment.jsp" %>

   <!-- 3 PART -->
  <h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">
  <f:verbatim>&nbsp;</f:verbatim>
  <h:outputLabel value="#{authorMessages.assign_to_p}" />
  <h:selectOneMenu id="assignToPart" value="#{itemauthor.currentItem.selectedSection}">
     <f:selectItems  value="#{itemauthor.sectionSelectList}" />
     <!-- use this in real  value="#{section.sectionNumberList}" -->
  </h:selectOneMenu>
  </h:panelGrid>


  <!-- 4 POOL -->
  <h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">
  <f:verbatim>&nbsp;</f:verbatim>
  <h:outputLabel value="#{authorMessages.assign_to_question_p}" />
  <h:selectOneMenu id="assignToPool" value="#{itemauthor.currentItem.selectedPool}">
     <f:selectItem itemValue="" itemLabel="#{authorMessages.select_a_pool_name}" />
     <f:selectItems value="#{itemauthor.poolSelectList}" />
  </h:selectOneMenu>
  </h:panelGrid>
  <br/>
 <!-- 5 ANSWER and ANSWERFEEDBACK -->
   <div class="longtext">
  <h:outputLabel value="#{authorMessages.answer_provide_a_mo}" />  </div>
<div class="tier2">
<h:outputText value="#{authorMessages.model_short_answer}" />

 <!-- WYSIWYG -->
 <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.corrAnswer}" hasToggle="yes">
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
</h:panelGrid>
<br/>
 <h:panelGroup rendered="#{assessmentSettings.feedbackAuthoring ne '2'}">
  <h:outputText value="#{authorMessages.feedback_optional}" />
 <h:panelGrid>
   <!-- WYSIWYG  -->
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.generalFeedback}" hasToggle="yes">
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
</h:panelGrid>
</h:panelGroup>
  </div>


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
  <h:commandButton accesskey="#{authorMessages.a_save}" rendered="#{itemauthor.target=='questionpool'}" value="#{authorMessages.button_save}" action="#{itemauthor.currentItem.getPoolOutcome}" styleClass="active">
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
