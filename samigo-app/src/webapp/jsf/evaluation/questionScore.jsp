<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>

<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<!--
$Id$
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
    <f:loadBundle
     basename="org.sakaiproject.tool.assessment.bundle.EvaluationMessages"
     var="msg"/>
     <f:loadBundle
     basename="org.sakaiproject.tool.assessment.bundle.GeneralMessages"
     var="genMsg"/>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText
        value="#{msg.title_question}" /></title>
      </head>
      <body onload="<%= request.getAttribute("html.body.onload") %>">

 <!-- JAVASCRIPT -->
<%@ include file="/js/delivery.js" %>

<!-- content... -->
 <div class="portletBody">
<h:form id="editTotalResults">
  <h:inputHidden id="publishedId" value="#{questionScores.publishedId}" />
  <h:inputHidden id="itemId" value="#{questionScores.itemId}" />

  <!-- HEADINGS -->
  <%@ include file="/jsf/evaluation/evaluationHeadings.jsp" %>

  <h3>
   
  <h:outputText value="#{msg.part} #{questionScores.partName}#{msg.comma} #{msg.question} #{questionScores.itemName} (#{totalScores.assessmentName}) "/>
  </h3>
  <p class="navViewAction">
    <h:commandLink title="#{msg.t_totalScores}" action="totalScores" immediate="true">
    <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.ResetTotalScoreListener" />
    <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.TotalScoreListener" />
      <h:outputText value="#{msg.title_total}" />
    </h:commandLink>
    <h:outputText value=" | " />
      <h:outputText value="#{msg.q_view}" />
    <h:outputText value=" | " rendered="#{!totalScores.hasRandomDrawPart}" />
    <h:commandLink title="#{msg.t_histogram}" action="histogramScores" immediate="true" rendered="#{!totalScores.hasRandomDrawPart}">
      <h:outputText value="#{msg.stat_view}" />
      <f:param name="hasNav" value="true"/>
      <f:actionListener
        type="org.sakaiproject.tool.assessment.ui.listener.evaluation.HistogramListener" />
    </h:commandLink>
  </p>
<div class="tier1">
  <h:messages styleClass="validation"/>

  <h:dataTable value="#{questionScores.sections}" var="partinit">
    <h:column>
      <h:outputText value="#{msg.part} #{partinit.partNumber}#{msg.column}" />
    </h:column>
    <h:column>
      <samigo:dataLine value="#{partinit.questionNumberList}" var="iteminit" separator=" | " first="0" rows="100" rendered="#{!partinit.isRandomDrawPart}" >
        <h:column>
          <h:commandLink title="#{msg.t_questionScores}"action="questionScores" immediate="true" >
            <h:outputText value="#{msg.q} #{iteminit.partNumber} "/>
			<f:actionListener
              type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScorePagerListener" />
            <f:actionListener
              type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
            <f:param name="newItemId" value="#{iteminit.id}" />
          </h:commandLink>
		          </h:column>

      </samigo:dataLine>

  	      <h:outputText value="#{msg.random_drow_part} " rendered="#{partinit.isRandomDrawPart}"/>
		  <h:commandLink title="#{msg.t_totalScores}" action="totalScores" immediate="true" rendered="#{partinit.isRandomDrawPart}">
		    <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.ResetTotalScoreListener" />
		    <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.TotalScoreListener" />
		    <h:outputText value="#{msg.grade_by_student}" />
	      </h:commandLink>

  	      <h:outputText value="#{msg.no_questions} " rendered="#{partinit.noQuestions}"/>
    </h:column>
  </h:dataTable>

<!--h:panelGrid styleClass="navModeQuestion" columns="2" columnClasses="alignLeft,alignCenter" width="100%" -->
<h4>
  <h:dataTable value="#{questionScores.deliveryItem}" var="question" border="0" >
    <h:column>
     <h:panelGroup rendered="#{questionScores.typeId == '7'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_aud}"/>
     </h:panelGroup>
      <h:panelGroup rendered="#{questionScores.typeId == '6'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_fu}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '8'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_fib}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '11'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_fin}"/>
     </h:panelGroup>
     
      <h:panelGroup rendered="#{questionScores.typeId == '9'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_match}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '2'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_mult_mult}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '4'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_tf}"/>
     </h:panelGroup>

     <h:panelGroup rendered="#{questionScores.typeId == '5'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_short_ess}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '3'}">
         <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_mult_surv}"/>
     </h:panelGroup>
     <h:panelGroup rendered="#{questionScores.typeId == '1'}">
    <h:outputText value="#{msg.question}#{question.sequence} - #{msg.q_mult_sing}"/>
      </h:panelGroup>
 </h:column>

  <!-- following columns are for formatting -->
  <h:column></h:column>
  <h:column></h:column>
  <h:column></h:column>
  <h:column></h:column>

  <h:column>
     <h:outputText value="#{questionScores.maxPoint}" style="instruction"/>
  </h:column>
  </h:dataTable>
</h4>

  <h:dataTable value="#{questionScores.deliveryItem}" var="question">
  <h:column>
  <h:panelGroup rendered="#{questionScores.typeId == '7'}">
    <f:subview id="displayAudioRecording">
      <%@ include file="/jsf/evaluation/item/displayAudioRecordingQuestion.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '6'}">
    <f:subview id="displayFileUpload">
    <%@ include file="/jsf/evaluation/item/displayFileUploadQuestion.jsp" %>
    </f:subview>
  </h:panelGroup>
    <h:panelGroup rendered="#{questionScores.typeId == '11'}">
      <f:subview id="displayFillInNumeric">
      <%@ include file="/jsf/evaluation/item/displayFillInNumeric.jsp" %>
      </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '8'}">
    <f:subview id="displayFillInTheBlank">
    <%@ include file="/jsf/evaluation/item/displayFillInTheBlank.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '9'}">
    <f:subview id="displayMatching">
    <%@ include file="/jsf/evaluation/item/displayMatching.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '2'}">
    <f:subview id="displayMultipleChoiceMultipleCorrect">
  <%@ include file="/jsf/evaluation/item/displayMultipleChoiceMultipleCorrect.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup
    rendered="#{questionScores.typeId == '1' ||
                questionScores.typeId == '3'}">
    <f:subview id="displayMultipleChoiceSingleCorrect">
    <%@ include file="/jsf/evaluation/item/displayMultipleChoiceSingleCorrect.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '5'}">
    <f:subview id="displayShortAnswer">
   <%@ include file="/jsf/evaluation/item/displayShortAnswer.jsp" %>
    </f:subview>
  </h:panelGroup>
  <h:panelGroup rendered="#{questionScores.typeId == '4'}">
    <f:subview id="displayTrueFalse">
    <%@ include file="/jsf/evaluation/item/displayTrueFalse.jsp" %>
    </f:subview>
  </h:panelGroup>
  </h:column>
  </h:dataTable>

<h4>
<h:panelGrid columns="2" columnClasses="navView,navList" width="100%">	
	<h:panelGroup>
	 <p class=" navView navModeAction">
	<h:outputText value="#{msg.responses}"/>
	</p>
	</h:panelGroup>
	<h:panelGroup rendered="#{questionScores.typeId == '6'}">
		<h:outputLink title="#{msg.t_fileUpload}" value="/samigo/servlet/DownloadAllMedia?publishedId=#{questionScores.publishedId}&publishedItemId=#{questionScores.itemId}&createdBy=#{question.itemData.createdBy}&assessmentName=#{totalScores.assessmentName}&partNumber=#{part.partNumber}&anonymous=#{totalScores.anonymous}&scoringType=#{questionScores.allSubmissions}">
		<h:outputText escape="false" value="#{msg.download_all}" />
		</h:outputLink>
	 </h:panelGroup>
</h:panelGrid>
</h4>

<sakai:flowState bean="#{questionScores}" />
<h:panelGrid columns="2" columnClasses="samLeftNav,samRightNav" width="100%" rendered="#{totalScores.anonymous eq 'false'}">
  <h:panelGroup>
    <h:panelGrid columns="1" columnClasses="samLeftNav" width="100%">
	  <h:panelGroup>
        <!-- SECTION AWARE -->
        <h:outputText value="#{msg.view}"/>
        <h:outputText value="#{msg.column}"/>
        <h:selectOneMenu value="#{questionScores.selectedSectionFilterValue}" id="sectionpicker" required="true" onchange="document.forms[0].submit();">
          <f:selectItems value="#{totalScores.sectionFilterSelectItems}"/>
          <f:valueChangeListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener"/>
        </h:selectOneMenu>

	
        <h:selectOneMenu value="#{questionScores.allSubmissions}" id="allSubmissionsL1"
         required="true" onchange="document.forms[0].submit();" rendered="#{totalScores.scoringOption eq '2'  && totalScores.multipleSubmissionsAllowed eq 'true'  }">
          <f:selectItem itemValue="3" itemLabel="#{msg.all_sub}" />
          <f:selectItem itemValue="2" itemLabel="#{msg.last_sub}" />
          <f:valueChangeListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu>

        <h:selectOneMenu value="#{questionScores.allSubmissions}" id="allSubmissionsH1"
         required="true" onchange="document.forms[0].submit();" rendered="#{totalScores.scoringOption eq '1'  && totalScores.multipleSubmissionsAllowed eq 'true' }">
          <f:selectItem itemValue="3" itemLabel="#{msg.all_sub}" />
          <f:selectItem itemValue="1" itemLabel="#{msg.highest_sub}" />
          <f:valueChangeListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu>

        <h:selectOneMenu value="#{questionScores.selectedSARationaleView}" id="inlinepopup1" 
         rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4'  || questionScores.typeId == '5')}" 
       	 required="true" onchange="document.forms[0].submit();" >
           <f:selectItem itemValue="1" itemLabel="#{msg.responses_popup}" />
           <f:selectItem itemValue="2" itemLabel="#{msg.responses_inline}" />
           <f:valueChangeListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu> 
      </h:panelGroup>

	  <h:panelGroup>
 	        <h:outputText value="#{msg.search}"/>
            <h:outputText value="#{msg.column}"/>
			<h:inputText
				id="searchString"
				value="#{questionScores.searchString}"
				onfocus="clearIfDefaultString(this, '#{msg.search_default_student_search_string}')"
				onkeypress="return submitOnEnter(event, 'editTotalResults:searchSubmitButton');"/>
			<f:verbatim> </f:verbatim>
			<h:commandButton actionListener="#{questionScores.search}" value="#{msg.search_find}" id="searchSubmitButton" />
			<f:verbatim> </f:verbatim>
			<h:commandButton actionListener="#{questionScores.clear}" value="#{msg.search_clear}"/>
	  </h:panelGroup>
    </h:panelGrid>
  </h:panelGroup>
   
   <h:panelGroup>
	<sakai:pager id="pager1" totalItems="#{questionScores.dataRows}" firstItem="#{questionScores.firstRow}" pageSize="#{questionScores.maxDisplayedRows}" textStatus="#{msg.paging_status}" >
		  <f:valueChangeListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScorePagerListener" />
	</sakai:pager>
  </h:panelGroup>
</h:panelGrid>

<h:panelGrid columns="2" columnClasses="samLeftNav,samRightNav" width="100%" rendered="#{totalScores.anonymous eq 'true'}">
  <h:panelGroup>
    <h:panelGrid columns="1" columnClasses="samLeftNav" width="100%">
      <h:panelGroup>
        <h:selectOneMenu value="#{questionScores.allSubmissions}" id="allSubmissionsL2"
         required="true" onchange="document.forms[0].submit();" rendered="#{totalScores.scoringOption eq '2'  && totalScores.multipleSubmissionsAllowed eq 'true' }">
        <f:selectItem itemValue="3" itemLabel="#{msg.all_sub}" />
        <f:selectItem itemValue="2" itemLabel="#{msg.last_sub}" />
        <f:valueChangeListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu>

        <h:selectOneMenu value="#{questionScores.allSubmissions}" id="allSubmissionsH2"
         required="true" onchange="document.forms[0].submit();" rendered="#{totalScores.scoringOption eq '1'  && totalScores.multipleSubmissionsAllowed eq 'true' }">
          <f:selectItem itemValue="3" itemLabel="#{msg.all_sub}" />
          <f:selectItem itemValue="1" itemLabel="#{msg.highest_sub}" />
          <f:valueChangeListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu>

		<h:selectOneMenu value="#{questionScores.selectedSARationaleView}" id="inlinepopup2" 
         rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4'  || questionScores.typeId == '5')}" 
       	 required="true" onchange="document.forms[0].submit();" >
           <f:selectItem itemValue="1" itemLabel="#{msg.responses_popup}" />
           <f:selectItem itemValue="2" itemLabel="#{msg.responses_inline}" />
           <f:valueChangeListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        </h:selectOneMenu> 

      </h:panelGroup>
    </h:panelGrid>
  </h:panelGroup>
  
  <h:panelGroup>
	<sakai:pager id="pager2" totalItems="#{questionScores.dataRows}" firstItem="#{questionScores.firstRow}" pageSize="#{questionScores.maxDisplayedRows}" textStatus="#{msg.paging_status}" >
	  <f:valueChangeListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScorePagerListener" />
	</sakai:pager>
  </h:panelGroup>
</h:panelGrid>


</div>

  <!-- STUDENT RESPONSES AND GRADING -->
  <!-- note that we will have to hook up with the back end to get N at a time -->
<div class="tier2">
  <h:dataTable cellpadding="0" cellspacing="0" id="questionScoreTable" value="#{questionScores.agents}"
    var="description" styleClass="listHier" columnClasses="textTable">

    <!-- NAME/SUBMISSION ID -->
    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType ne 'lastName'}">
     <f:facet name="header">
        <h:commandLink title="#{msg.t_sortLastName}" id="lastName" action="questionScores">
          <h:outputText value="#{msg.name}" />
        <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="lastName" />
        <f:param name="sortAscending" value="true" />
        </h:commandLink>
     </f:facet>
     <h:panelGroup>
       <h:outputText value="<a name=\"" escape="false" />
       <h:outputText value="#{description.lastInitial}" />
       <h:outputText value="\"></a>" escape="false" />
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.firstName}" />
         <h:outputText value=" " />
         <h:outputText value="#{description.lastName}" />
         <h:outputText value="#{msg.na}" rendered="#{description.lastInitial eq 'Anonymous'}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>

    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType eq 'lastName' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortLastName}" action="questionScores">
          <h:outputText value="#{msg.name}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortLastNameDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
     <h:panelGroup>
       <h:outputText value="<a name=\"" escape="false" />
       <h:outputText value="#{description.lastInitial}" />
       <h:outputText value="\"></a>" escape="false" />
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.firstName}" />
         <h:outputText value=" " />
         <h:outputText value="#{description.lastName}" />
         <h:outputText value="#{msg.na}" rendered="#{description.lastInitial eq 'Anonymous'}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>    
    
    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType eq 'lastName' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortLastName}" action="questionScores">
          <h:outputText value="#{msg.name}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortLastNameAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
     <h:panelGroup>
       <h:outputText value="<a name=\"" escape="false" />
       <h:outputText value="#{description.lastInitial}" />
       <h:outputText value="\"></a>" escape="false" />
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.firstName}" />
         <h:outputText value=" " />
         <h:outputText value="#{description.lastName}" />
         <h:outputText value="#{msg.na}" rendered="#{description.lastInitial eq 'Anonymous'}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>  


	<!-- SUBMISSION ID -->
    <h:column rendered="#{questionScores.anonymous eq 'true' && questionScores.sortType ne 'assessmentGradingId'}">
     <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmissionId}" action="questionScores">
          <h:outputText value="#{msg.sub_id}" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="assessmentGradingId" />
        </h:commandLink>
     </f:facet>
     <h:panelGroup>
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.assessmentGradingId}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="studentName" value="#{description.assessmentGradingId}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>

    <h:column rendered="#{questionScores.anonymous eq 'true' && questionScores.sortType eq 'assessmentGradingId' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmissionId}" action="questionScores">
          <h:outputText value="#{msg.sub_id}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortSubmissionIdDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
     <h:panelGroup>
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.assessmentGradingId}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="studentName" value="#{description.assessmentGradingId}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>    
    
    <h:column rendered="#{questionScores.anonymous eq 'true' && questionScores.sortType eq 'assessmentGradingId' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmissionId}" action="questionScores">
          <h:outputText value="#{msg.sub_id}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortSubmissionIdAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
      <h:panelGroup>
       <h:commandLink title="#{msg.t_student}" action="studentScores" immediate="true">
         <h:outputText value="#{description.assessmentGradingId}" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
         <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
         <f:param name="studentid" value="#{description.idString}" />
         <f:param name="studentName" value="#{description.assessmentGradingId}" />
         <f:param name="publishedIdd" value="#{questionScores.publishedId}" />
         <f:param name="gradingData" value="#{description.assessmentGradingId}" />
       </h:commandLink>
     </h:panelGroup>
    </h:column>  


   <!-- STUDENT ID -->
    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType!='idString'}" >
     <f:facet name="header">
       <h:commandLink title="#{msg.t_sortUserId}" id="idString" action="questionScores" >
          <h:outputText value="#{msg.uid}" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="idString" />
        <f:param name="sortAscending" value="true" />
        </h:commandLink>
     </f:facet>
        <h:outputText value="#{description.agentEid}" />
    </h:column>

    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType eq 'idString' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortUserId}" action="questionScores">
          <h:outputText value="#{msg.uid}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortUserIdDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.agentEid}" />
    </h:column>    
    
    <h:column rendered="#{questionScores.anonymous eq 'false' && questionScores.sortType eq 'idString' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortUserId}" action="questionScores">
          <h:outputText value="#{msg.uid}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortUserIdAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.agentEid}" />
    </h:column>      


    <!-- ROLE -->
    <h:column rendered="#{questionScores.sortType ne 'role'}">
     <f:facet name="header" >
        <h:commandLink title="#{msg.t_sortRole}" id="role" action="questionScores">
          <h:outputText value="#{msg.role}" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="role" />
        <f:param name="sortAscending" value="true"/>
        </h:commandLink>
     </f:facet>
        <h:outputText value="#{description.role}"/>
    </h:column>

    <h:column rendered="#{questionScores.sortType eq 'role' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortRole}" action="questionScores">
          <h:outputText value="#{msg.role}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortRoleDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.role}"/>
    </h:column>    
    
    <h:column rendered="#{questionScores.sortType eq 'role' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortRole}" action="questionScores">
          <h:outputText value="#{msg.role}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortRoleAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.role}"/>
    </h:column>      


    <!-- DATE -->
    <h:column rendered="#{questionScores.sortType!='submittedDate'}">
     <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmittedDate}" id="submittedDate" action="questionScores">
          <h:outputText value="#{msg.date}" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
          type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="submittedDate" />
        <f:param name="sortAscending" value="true"/>
        </h:commandLink>
     </f:facet>
        <h:outputText value="#{description.submittedDate}">
         <f:convertDateTime pattern="#{genMsg.output_date_picker}"/>
        </h:outputText>

        <h:outputText styleClass="red" value="#{msg.all_late}" escape="false"
          rendered="#{description.isLate}"/>
    </h:column>

    <h:column rendered="#{questionScores.sortType eq 'submittedDate' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmittedDate}" action="questionScores">
          <h:outputText value="#{msg.date}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortSubmittedDateDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.submittedDate}">
         <f:convertDateTime pattern="#{genMsg.output_date_picker}"/>
        </h:outputText>
        <h:outputText styleClass="red" value="#{msg.all_late}" escape="false"
          rendered="#{description.isLate}"/>
    </h:column>    
    
    <h:column rendered="#{questionScores.sortType eq 'submittedDate' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortSubmittedDate}" action="questionScores">
          <h:outputText value="#{msg.date}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortSubmittedDateAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
        <h:outputText value="#{description.submittedDate}">
         <f:convertDateTime pattern="#{genMsg.output_date_picker}"/>
        </h:outputText>
        <h:outputText styleClass="red" value="#{msg.all_late}" escape="false"
          rendered="#{description.isLate}"/>
    </h:column>    


    <!-- SCORE -->
    <h:column rendered="#{questionScores.sortType!='totalAutoScore'}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortScore}" id="score" action="questionScores">
          <h:outputText value="#{msg.score}" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
             type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <f:param name="sortBy" value="totalAutoScore" />
        <f:param name="sortAscending" value="true" />
        </h:commandLink>
      </f:facet>
      <h:inputText value="#{description.totalAutoScore}" size="5" id="qscore" required="false">
<f:validateDoubleRange/>
</h:inputText>
<br />
 <h:message for="qscore" style="color:red"/>
 </h:column>
 
 
    <h:column rendered="#{questionScores.sortType eq 'totalAutoScore' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortScore}" action="questionScores">
          <h:outputText value="#{msg.score}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortAdjustScoreDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
	  <h:inputText value="#{description.totalAutoScore}" size="5"  id="qscore2" required="false">
	  	<f:validateDoubleRange/>
	  </h:inputText>
	  <h:message for="qscore2" style="color:red"/>
    </h:column>    
    
    <h:column rendered="#{questionScores.sortType eq 'totalAutoScore' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortScore}" action="questionScores">
          <h:outputText value="#{msg.score}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortAdjustScoreAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
	  <h:inputText value="#{description.totalAutoScore}" size="5"  id="qscore3" required="false">
	  	<f:validateDoubleRange/>
	  </h:inputText>
	  <h:message for="qscore2" style="color:red"/>
    </h:column>    
 

    <!-- ANSWER -->
    <h:column rendered="#{questionScores.sortType!='answer'}">
      <f:facet name="header">
        <h:panelGroup>
		  <h:outputText value="#{msg.stud_resp}" 
             rendered="#{questionScores.typeId == '6' || questionScores.typeId == '7' }"/>
          <h:commandLink title="#{msg.t_sortResponse}" id="answer" action="questionScores" >
            <h:outputText value="#{msg.stud_resp}" 
               rendered="#{questionScores.typeId != '6' && questionScores.typeId != '7' }"/>
            <f:actionListener
               type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
            <f:actionListener
               type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
            <f:param name="sortBy" value="answer" />
            <f:param name="sortAscending" value="true" />
          </h:commandLink>
        </h:panelGroup>
      </f:facet>
      <!-- display of answer to file upload question is diffenent from other types - daisyf -->
      <h:outputText value="#{description.answer}" escape="false" rendered="#{questionScores.typeId != '6' && questionScores.typeId != '7' && questionScores.typeId != '5'}" />
     <f:verbatim><br/></f:verbatim>
   <!--h:outputLink rendered="#{questionScores.typeId == '5'}" value="#" onclick="javascript:window.alert('#{description.fullAnswer}');"-->

    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '1' && questionScores.typeId == '5'}">
    <h:outputText value="#{description.answer}" escape="false"/>
     <f:verbatim><br/></f:verbatim>
		<h:outputLink title="#{msg.t_fullShortAnswer}"   value="#" onclick="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_shortAnswer})" rendered="#{description.answer != 'No Answer'}"/>
    	</h:outputLink>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '2' && questionScores.typeId == '5'}">
		<h:outputText  escape="false" value="#{description.fullAnswer}"/>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '1'}">
		<h:outputLink title="#{msg.t_rationale}"  value="#" onclick="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_rationale})" />
    	</h:outputLink>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '2'}">
		<h:outputText escape="false" value="#{description.rationale}"/>
    </h:panelGroup>
    

    
<%--
    <h:outputLink title="#{msg.t_rationale}"
      rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne ''}" 
      value="#" onclick="javascript:window.alert('#{description.rationale}');" onkeypress="javascript:window.alert('#{description.rationale}');" >
    <h:outputText  value="(#{msg.click_rationale})"/>
    </h:outputLink>
--%>
      <h:panelGroup rendered="#{questionScores.typeId == '6'}">
        <f:subview id="displayFileUpload2">
          <%@ include file="/jsf/evaluation/item/displayFileUploadAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>

      <h:panelGroup rendered="#{questionScores.typeId == '7'}">
        <f:subview id="displayAudioRecording2">
          <%@ include file="/jsf/evaluation/item/displayAudioRecordingAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>
    </h:column>

    <h:column rendered="#{questionScores.sortType eq 'answer' && questionScores.sortAscending}">
      <f:facet name="header">
      <h:panelGroup>
		  <h:outputText value="#{msg.stud_resp}" 
             rendered="#{questionScores.typeId == '6' || questionScores.typeId == '7' }"/>
          <h:commandLink title="#{msg.t_sortResponse}" action="questionScores" >
            <h:outputText value="#{msg.stud_resp}" 
               rendered="#{questionScores.typeId != '6' && questionScores.typeId != '7' }"/>
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortResponseDescending}" rendered="#{questionScores.sortAscending && questionScores.typeId != '6' && questionScores.typeId != '7'}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>  
          </h:panelGroup>  
      </f:facet>
      <h:outputText value="#{description.answer}" escape="false" rendered="#{questionScores.typeId != '6' and questionScores.typeId != '7' && questionScores.typeId != '5'}" />
     <f:verbatim><br/></f:verbatim>
   	<!--h:outputLink rendered="#{questionScores.typeId == '5'}" value="#" onclick="javascript:window.alert('#{description.fullAnswer}');"-->


    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '1' && questionScores.typeId == '5'}">
    <h:outputText value="#{description.answer}" escape="false"/>
	<f:verbatim><br/></f:verbatim>
		<h:outputLink title="#{msg.t_fullShortAnswer}"   value="#" onclick="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_shortAnswer})" rendered="#{description.answer != 'No Answer'}"/>
    	</h:outputLink>
    </h:panelGroup>

    
    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '2' && questionScores.typeId == '5'}">
		<h:outputText  escape="false" value="#{description.fullAnswer}"/>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '1'}">
		<h:outputLink title="#{msg.t_rationale}"  
	value="#" onclick="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_rationale})" />
    	</h:outputLink>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '2'}">
		<h:outputText escape="false" value="#{description.rationale}"/>
    </h:panelGroup>

    
    <h:panelGroup rendered="#{questionScores.typeId == '6'}">
        <f:subview id="displayFileUpload3">
          <%@ include file="/jsf/evaluation/item/displayFileUploadAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>

      <h:panelGroup rendered="#{questionScores.typeId == '7'}">
        <f:subview id="displayAudioRecording3">
          <%@ include file="/jsf/evaluation/item/displayAudioRecordingAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>
    </h:column>    
    
    <h:column rendered="#{questionScores.sortType eq 'answer' && !questionScores.sortAscending}">
      <f:facet name="header">
		  <h:panelGroup>
		  <h:outputText value="#{msg.stud_resp}" 
             rendered="#{questionScores.typeId == '6' || questionScores.typeId == '7' }"/>
          <h:commandLink title="#{msg.t_sortResponse}" action="questionScores" >
            <h:outputText value="#{msg.stud_resp}" 
               rendered="#{questionScores.typeId != '6' && questionScores.typeId != '7' }"/>
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortResponseAscending}" rendered="#{!questionScores.sortAscending && questionScores.typeId != '6' && questionScores.typeId != '7'}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
          </h:panelGroup>
      </f:facet>
	<h:outputText value="#{description.answer}" escape="false" rendered="#{questionScores.typeId != '6' and questionScores.typeId != '7' && questionScores.typeId != '5'}" />
     <f:verbatim><br/></f:verbatim>
   	<!--h:outputLink rendered="#{questionScores.typeId == '5'}" value="#" onclick="javascript:window.alert('#{description.fullAnswer}');"-->


    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '1' && questionScores.typeId == '5'}">
    <h:outputText value="#{description.answer}" escape="false"/>
	<f:verbatim><br/></f:verbatim>
		<h:outputLink title="#{msg.t_fullShortAnswer}"   value="#" onclick="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('fullShortAnswer.faces?idString=#{description.assessmentGradingId}','fullShortAnswer','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_shortAnswer})" rendered="#{description.answer != 'No Answer'}" />
    	</h:outputLink>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{questionScores.selectedSARationaleView == '2' && questionScores.typeId == '5'}">
		<h:outputText  escape="false" value="#{description.fullAnswer}"/>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '1'}">
		<h:outputLink title="#{msg.t_rationale}"  
	value="#" onclick="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('rationale.faces?idString=#{description.assessmentGradingId}','rationale','width=600,height=600,scrollbars=yes, resizable=yes');">
    		<h:outputText  value="(#{msg.click_rationale})" />
    	</h:outputLink>
    </h:panelGroup>
    
    <h:panelGroup rendered="#{(questionScores.typeId == '1' || questionScores.typeId == '2' || questionScores.typeId == '4') && description.rationale ne '' && questionScores.selectedSARationaleView == '2'}">
		<h:outputText escape="false" value="#{description.rationale}"/>
    </h:panelGroup>
    
          <h:panelGroup rendered="#{questionScores.typeId == '6'}">
        <f:subview id="displayFileUpload4">
          <%@ include file="/jsf/evaluation/item/displayFileUploadAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>

      <h:panelGroup rendered="#{questionScores.typeId == '7'}">
        <f:subview id="displayAudioRecording4">
          <%@ include file="/jsf/evaluation/item/displayAudioRecordingAnswer.jsp" %>
        </f:subview>
      </h:panelGroup>
    </h:column> 


    <!-- COMMENT -->
    <h:column rendered="#{questionScores.sortType!='comments'}">
     <f:facet name="header">
      <h:commandLink title="#{msg.t_sortComments}" id="comments" action="questionScores">
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
        <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
        <h:outputText value="#{msg.comment}"/>
        <f:param name="sortBy" value="comments" />
      </h:commandLink>
     </f:facet>
 <h:inputTextarea value="#{description.comments}" rows="3" cols="30"/>
<%-- temp replaced by inputTextArea until resize is introduced
     <samigo:wysiwyg rows="140" value="#{description.comments}" >
       <f:validateLength maximum="4000"/>
     </samigo:wysiwyg>
--%>
    </h:column>

    <h:column rendered="#{questionScores.sortType eq 'comments' && questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortComments}" action="questionScores">
          <h:outputText value="#{msg.comment}" />
          <f:param name="sortAscending" value="false" />
          <h:graphicImage alt="#{msg.alt_sortCommentDescending}" rendered="#{questionScores.sortAscending}" url="/images/sortascending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
 <h:inputTextarea value="#{description.comments}" rows="3" cols="30"/>
<%-- temp replaced by inputTextArea until resize is introduced
     <samigo:wysiwyg rows="140" value="#{description.comments}" >
       <f:validateLength maximum="4000"/>
     </samigo:wysiwyg>
--%>
    </h:column>    
    
    <h:column rendered="#{questionScores.sortType eq 'comments' && !questionScores.sortAscending}">
      <f:facet name="header">
        <h:commandLink title="#{msg.t_sortComments}" action="questionScores">
          <h:outputText value="#{msg.comment}" />
          <f:param name="sortAscending" value="true" />
          <h:graphicImage alt="#{msg.alt_sortCommentAscending}" rendered="#{!questionScores.sortAscending}" url="/images/sortdescending.gif"/>
      	  <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
          <f:actionListener
           type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
          </h:commandLink>    
      </f:facet>
 <h:inputTextarea value="#{description.comments}" rows="3" cols="30"/>
<%-- temp replaced by inputTextArea until resize is introduced
     <samigo:wysiwyg rows="140" value="#{description.comments}" >
       <f:validateLength maximum="4000"/>
     </samigo:wysiwyg>
--%>
    </h:column> 

  </h:dataTable>
</div>
<p class="act">
   <%-- <h:commandButton value="#{msg.save_exit}" action="author"/> --%>
   <h:commandButton accesskey="#{msg.a_update}" styleClass="active" value="#{msg.save_cont}" action="questionScores" type="submit" >
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreUpdateListener" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
   </h:commandButton>
   <h:commandButton accesskey="#{msg.a_cancel}" value="#{msg.cancel}" action="totalScores" immediate="true"/>
</div>
</h:form>
</div>
  <!-- end content -->
      </body>
    </html>
  </f:view>
