<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
* $Id$
<%--
***********************************************************************************
*
* Copyright (c) 2005 The Regents of the University of Michigan, Trustees of Indiana University,
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
--%>
-->
<%-- "checked in wysiwyg code but disabled, added in lydia's changes between 1.9 and 1.10" --%>
  <f:view>
    <f:loadBundle
     basename="org.sakaiproject.tool.assessment.bundle.AuthorMessages"
     var="msg"/>
   <f:loadBundle
     basename="org.sakaiproject.tool.assessment.bundle.GeneralMessages"
     var="genMsg"/>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{msg.item_display_author}"/></title>
      <!-- HTMLAREA -->
      <samigo:stylesheet path="/htmlarea/htmlarea.css"/>
      <samigo:script path="/htmlarea/htmlarea.js"/>
      <samigo:script path="/htmlarea/lang/en.js"/>
      <samigo:script path="/htmlarea/dialog.js"/>
      <samigo:script path="/htmlarea/popupwin.js"/>
      <samigo:script path="/htmlarea/popups/popup.js"/>
      <samigo:script path="/htmlarea/navigo_js/navigo_editor.js"/>
      <samigo:script path="/jsf/widget/wysiwyg/samigo/wysiwyg.js"/>
      <!-- AUTHORING -->
      <samigo:script path="/js/authoring.js"/>
<%--
<script language="javascript" type="text/JavaScript">
<!--
<%@ include file="/js/authoring.js" %>
//-->
</script>
--%>
      </head>
<%-- unfortunately have to use a scriptlet here --%>
<body onload="countNum();<%= request.getAttribute("html.body.onload") %>">
<%--
      <body onload="javascript:initEditors('<%=request.getContextPath()%>');;<%= request.getAttribute("html.body.onload") %>">
--%>

<div class="portletBody">
<!-- content... -->
<!-- FORM -->

<!-- HEADING -->
<%@ include file="/jsf/author/item/itemHeadings.jsp" %>
<h:form id="itemForm">
  <!-- QUESTION PROPERTIES -->
  <!-- this is for creating multiple choice questions -->
  <%-- kludge: we add in 1 useless textarea, the 1st does not seem to work --%>
  <div style="display:none">
  <h:inputTextarea id="ed0" cols="10" rows="10" value="            " />
  </div>



  <!-- 1 POINTS -->
  <div class="tier2">

   <span id="num1" class="number"></span>
   <h:panelGrid columns="2" columnClasses="shorttext"><h:outputLabel for="answerptr" value="#{msg.answer_point_value}" />
    <h:inputText id="answerptr" value="#{itemauthor.currentItem.itemScore}" required="true">
<f:validateDoubleRange/>
</h:inputText>
</h:panelGrid>
<br/><h:message for="answerptr" styleClass="validate"/>


<br/>
<!-- 2 TEXT -->

    <span id="num2" class="number"></span>
  <div class="longtext"> <h:outputLabel value="#{msg.q_text}" />
  <br/>
  <!-- WYSIWYG -->
  <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.instruction}" >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>

  </h:panelGrid>
  </div>

  <!-- 3 ANSWER -->

      <span id="num3" class="number"></span>
    <div class="longtext"> <h:outputLabel value="#{msg.create_pairing} " /></div>
<div class="tier2">
  <!-- display existing pairs -->

<h:dataTable cellpadding="0" cellspacing="0" styleClass="listHier" id="pairs" value="#{itemauthor.currentItem.matchItemBeanList}" var="pair">
      
      <h:column>
        <f:facet name="header">
          
          <h:outputText value=""  />
        </f:facet>

          <h:outputText value="#{pair.sequence}"  />
      </h:column>

      <h:column>
        <f:facet name="header">
          <h:outputText value="#{msg.matching_choice_col}"  />
        </f:facet>
          <h:outputText escape="false" value="#{pair.choice}"  />
      </h:column>

      <h:column>
        <f:facet name="header">
          <h:outputText value="#{msg.matching_match_col}"  />
        </f:facet>
          <h:outputText escape="false" value="#{pair.match}"  />
      </h:column>

      <h:column>
        <f:facet name="header">
          <h:outputText value=""/>
        </f:facet>

     <h:panelGrid>
     <h:panelGroup>
<h:commandLink rendered="#{itemauthor.currentItem.currentMatchPair.sequence != pair.sequence}" id="modifylink" immediate="true" action="#{itemauthor.currentItem.editMatchPair}">
  <h:outputText id="modifytext" value="#{msg.button_edit}"/>
  <f:param name="sequence" value="#{pair.sequence}"/>
</h:commandLink>

          <h:outputText value="#{msg.matching_currently_editing}" rendered="#{itemauthor.currentItem.currentMatchPair.sequence== pair.sequence}"/>
          <h:outputText value=" #{msg.separator} " rendered="#{itemauthor.currentItem.currentMatchPair.sequence != pair.sequence}"/>

<h:commandLink id="removelink" immediate="true" action="#{itemauthor.currentItem.removeMatchPair}" rendered="#{itemauthor.currentItem.currentMatchPair.sequence != pair.sequence}">
  <h:outputText id="removetext" value="#{msg.button_remove}"/>
  <f:param name="sequence" value="#{pair.sequence}"/>
</h:commandLink>
     </h:panelGroup>
     </h:panelGrid>
      </h:column>

     </h:dataTable>
<h:outputLabel value="<p>#{msg.no_matching_pair}</p>" rendered="#{itemauthor.currentItem.matchItemBeanList eq '[]'}"/>

</div>
        <!-- WYSIWYG -->
<div class="tier2">
   
          <h:outputText value=" Choice"/>
<h:panelGrid>
  <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.currentMatchPair.choice}">
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
</h:panelGrid>
          <h:outputText value=" Match"/>

 <h:panelGrid>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.currentMatchPair.match}">
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>

   </h:panelGrid>
</div>

 <!-- Match FEEDBACK -->

<div class="tier2">
  <h:panelGroup rendered="#{assessmentSettings.feedbackAuthoring ne '1'}">
  <h:outputText value="#{msg.correct_match_feedback_opt}" />
<f:verbatim><br/></f:verbatim>
<!-- WYSIWYG -->
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.currentMatchPair.corrMatchFeedback}">
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
  <f:verbatim><br/></f:verbatim>

  <h:outputText value="#{msg.incorrect_match_feedback_opt}" />


 <f:verbatim><br/></f:verbatim>
  <!-- WYSIWYG -->
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.currentMatchPair.incorrMatchFeedback}" >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>

<f:verbatim><br/></f:verbatim>
</h:panelGroup>
  </div>


<f:verbatim><br/></f:verbatim>
<f:verbatim><br/></f:verbatim>
  <h:commandButton accesskey="#{msg.a_create}" value="#{msg.button_save_pair}" action="#{itemauthor.currentItem.addMatchPair}">
  </h:commandButton>
<f:verbatim><br/></f:verbatim>
<f:verbatim><br/></f:verbatim>
<f:verbatim><br/></f:verbatim>

<%--
    <!-- 4 RANDOMIZE -->

    <span id="num4" class="number"></span>
   <div class="longtext">  <h:outputText value="#{msg.randomize_answers}" />
    <h:selectOneRadio value="#{itemauthor.currentItem.randomized}" >
     <f:selectItem itemValue="true"
       itemLabel="#{msg.yes}" />
     <f:selectItem itemValue="false"
       itemLabel="#{msg.no}" />
    </h:selectOneRadio>
  </div>


    <!-- 5 RATIONALE -->

      <span id="num5" class="number"></span>
   <div class="longtext"> <h:outputText value="#{msg.req_rationale}" />
    <h:selectOneRadio value="#{itemauthor.currentItem.rationale}" >
     <f:selectItem itemValue="true"
       itemLabel="#{msg.yes}" />
     <f:selectItem itemValue="false"
       itemLabel="#{msg.no}" />
    </h:selectOneRadio>
  </div>

--%>
    <!-- 6 PART -->

<h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">
       <f:verbatim> <span id="num6" class="number"></span></f:verbatim>

  <h:outputLabel for="assignToPart" value="#{msg.assign_to_p}" />
  <h:selectOneMenu id="assignToPart" value="#{itemauthor.currentItem.selectedSection}">
     <f:selectItems  value="#{itemauthor.sectionSelectList}" />
  </h:selectOneMenu>

  </h:panelGrid>

    <!-- 7 POOL -->
<h:panelGrid columns="3" columnClasses="shorttext" rendered="#{itemauthor.target == 'assessment'}">

   <f:verbatim> <span id="num7" class="number"></span></f:verbatim>

  <h:outputLabel for="assignToPool" value="#{msg.assign_to_question_p}" />
<%-- stub debug --%>
  <h:selectOneMenu id="assignToPool" value="#{itemauthor.currentItem.selectedPool}">
     <f:selectItem itemValue="" itemLabel="#{msg.select_a_pool_name}" />
     <f:selectItems value="#{itemauthor.poolSelectList}" />
  </h:selectOneMenu>

  </h:panelGrid>


 <h:panelGroup rendered="#{assessmentSettings.feedbackAuthoring ne '2'}">
  <f:verbatim><span id="num8" class="number"></span></f:verbatim>
<f:verbatim><div class="tier2"></f:verbatim>
  <h:outputLabel value="#{msg.correct_incorrect_an}" />
<f:verbatim><br/></br/></f:verbatim>
  <h:outputText value="#{msg.correct_answer_opti}" /><f:verbatim><br/></f:verbatim>

  <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.corrFeedback}"  >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
<f:verbatim><br/></f:verbatim>

  <h:outputText value="#{msg.incorrect_answer_op}" />
<f:verbatim><br/></f:verbatim>
   <samigo:wysiwyg rows="140" value="#{itemauthor.currentItem.incorrFeedback}"  >
     <f:validateLength maximum="4000"/>
   </samigo:wysiwyg>
<f:verbatim><br/></div></f:verbatim>
  </h:panelGroup>


<!-- METADATA -->

<h:panelGroup rendered="#{itemauthor.showMetadata == 'true'}" styleClass="longtext">
<f:verbatim><span id="num9" class="number"></span></f:verbatim>
<h:outputLabel  value="Metadata"/><br/>
<f:verbatim><div class="tier2"></f:verbatim>

<h:panelGrid columns="2" columnClasses="shorttext">
<h:outputLabel for="obj" value="#{msg.objective}" />
  <h:inputText size="30" id="obj" value="#{itemauthor.currentItem.objective}" />
<h:outputLabel for="keyword" value="#{msg.keyword}" />
  <h:inputText size="30" id="keyword" value="#{itemauthor.currentItem.keyword}" />
<h:outputLabel for="rubric" value="#{msg.rubric_colon}" />
  <h:inputText size="30" id="rubric" value="#{itemauthor.currentItem.rubric}" />
</h:panelGrid>
 <f:verbatim></div></f:verbatim>
</h:panelGroup>
</div>



<p class="act">
  <h:commandButton accesskey="#{msg.a_save}" rendered="#{itemauthor.target=='assessment'}" value="#{msg.button_save}" action="#{itemauthor.currentItem.getOutcome}" styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>
  <h:commandButton accesskey="#{msg.a_save}" rendered="#{itemauthor.target=='questionpool'}" value="#{msg.button_save}" action="#{itemauthor.currentItem.getPoolOutcome}" styleClass="active">
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.author.ItemAddListener" />
  </h:commandButton>

  <h:commandButton accesskey="#{msg.a_cancel}" rendered="#{itemauthor.target=='assessment'}" value="#{msg.button_cancel}" action="editAssessment" immediate="true"/>
 <h:commandButton rendered="#{itemauthor.target=='questionpool'}" value="#{msg.button_cancel}" action="editPool" immediate="true"/>
</p>
</h:form>
<!-- end content -->
</div>

    </body>
  </html>
</f:view>

