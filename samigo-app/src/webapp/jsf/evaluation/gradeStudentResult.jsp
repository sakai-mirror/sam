<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
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
     basename="org.sakaiproject.tool.assessment.bundle.DeliveryMessages"
     var="dmsg"/>
   <f:loadBundle
     basename="org.sakaiproject.tool.assessment.bundle.GeneralMessages"
     var="genMsg"/>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText
        value="#{msg.title_total}" /></title>
    <samigo:script path="/jsf/widget/hideDivision/hideDivision.js" />

      </head>
    <%-- body onload="hideUnhideAllDivs('none');;<%= request.getAttribute("html.body.onload") %>"--%>
  <body onload="hideAll();;<%= request.getAttribute("html.body.onload") %>"--%>
<!-- $Id:  -->
<!-- content... -->
 <div class="portletBody">
<h:form id="editStudentResults">
  <h:inputHidden id="publishedIdd" value="#{studentScores.publishedId}" />
  <h:inputHidden id="publishedId" value="#{studentScores.publishedId}" />
  <h:inputHidden id="studentid" value="#{studentScores.studentId}" />
  <h:inputHidden id="studentName" value="#{studentScores.studentName}" />
  <h:inputHidden id="gradingData" value="#{studentScores.assessmentGradingId}" />
  <h:inputHidden id="itemId" value="#{studentScores.itemId}" />

  <!-- HEADINGS -->
  <%@ include file="/jsf/evaluation/evaluationHeadings.jsp" %>

  <h3>
    <h:outputText value="#{studentScores.studentName}" rendered="#{totalScores.anonymous eq 'false'}"/>
    <h:outputText value="#{msg.submission_id}#{dmsg.column} #{studentScores.assessmentGradingId}" rendered="#{totalScores.anonymous eq 'true'}"/>
  </h3>
  <p class="navViewAction">
    <h:commandLink title="#{msg.t_totalScores}" action="totalScores" immediate="true">
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.ResetTotalScoreListener" />
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.TotalScoreListener" />
      <h:outputText value="#{msg.title_total}" />
    </h:commandLink>
    <h:outputText value=" #{msg.separator} " rendered="#{totalScores.firstItem ne ''}"  />
    <h:commandLink title="#{msg.t_questionScores}" action="questionScores" immediate="true"
      rendered="#{totalScores.firstItem ne ''}" >
      <h:outputText value="#{msg.q_view}" />
      <f:actionListener
        type="org.sakaiproject.tool.assessment.ui.listener.evaluation.QuestionScoreListener" />
    </h:commandLink>
    <h:outputText value=" #{msg.separator} " rendered="#{totalScores.firstItem ne '' && !totalScores.hasRandomDrawPart}"  />
    <h:commandLink title="#{msg.t_histogram}" action="histogramScores" immediate="true"
      rendered="#{totalScores.firstItem ne '' && !totalScores.hasRandomDrawPart}" >
      <h:outputText value="#{msg.stat_view}" />
      <f:actionListener
        type="org.sakaiproject.tool.assessment.ui.listener.evaluation.HistogramListener" />
    </h:commandLink>
  </p>

  <h:messages styleClass="validation"/>

<f:verbatim><h4></f:verbatim>
<h:outputText value="#{totalScores.assessmentName}" />
<f:verbatim></h4></f:verbatim>
<div class="tier3">
<h:panelGrid columns="2">
   <h:outputText value="#{dmsg.comment}#{dmsg.column}"/>
   <h:inputTextarea value="#{studentScores.comments}" rows="3" cols="30"/>
   </h:panelGrid>
</div>
<f:verbatim><h4></f:verbatim>
<h:outputText value="#{dmsg.table_of_contents}" />
<f:verbatim></h4></f:verbatim>

<div class="tier2">
  <h:dataTable value="#{delivery.tableOfContents.partsContents}" var="part">
  <h:column>
    <h:panelGroup>

<%-- TEMP CLOSE - hideDivision does not work here!

    <samigo:hideDivision title = "#{dmsg.p} #{part.number} #{msg.dash} #{part.text} #{msg.dash}
       #{part.questions-part.unansweredQuestions}#{msg.splash}#{part.questions} #{dmsg.ans_q}, #{part.pointsDisplayString} #{part.maxPoints} #{dmsg.pt}" >

--%>

<%-- NEW --%>
<h:outputLink title="msg.t_toggleDiv" id="p"  value="#" onclick="toggleDiv(this.id)" onkeypress="toggleDiv(this.id)">
<h:graphicImage id="pIm" url="/images/right_arrow.gif"/>
</h:outputLink>
<h:outputLabel value=" #{dmsg.p} #{part.number} #{msg.dash} #{part.text} #{msg.dash}
       #{part.questions-part.unansweredQuestions}#{msg.splash}#{part.questions} #{dmsg.ans_q}, #{part.pointsDisplayString} #{part.maxPoints} #{dmsg.pt}" />


<%-- END NEW --%>

      <h:dataTable value="#{part.itemContents}" var="question">
      <h:column>
      <f:verbatim><h4 class="tier2"></f:verbatim>
        <h:panelGroup>
          <h:outputText value="<a href=\"#" escape="false" />
          <h:outputText value="#{part.number}#{dmsg.underscore}#{question.number}\">"
            escape="false" />
          <h:outputText id="q" value="#{question.number}#{dmsg.dot} #{question.strippedText} #{question.maxPoints} #{dmsg.pt} " escape="false" />
          <h:outputText value="</a>" escape="false" />
        </h:panelGroup>
        <f:verbatim></h4></f:verbatim>
       </h:column>
      </h:dataTable>

<%-- TEMP CLOSE
     </samigo:hideDivision>
--%>

     </h:panelGroup>
   </h:column>
  </h:dataTable>
</div>


<div class="tier2">
  <h:dataTable value="#{delivery.pageContents.partsContents}" var="part">
    <h:column>
      <f:verbatim><h4 class="tier1"></f:verbatim>
      <h:outputText value="#{dmsg.p} #{part.number} #{dmsg.of} #{part.numParts}" />
      <!-- h:outputText value="#{part.unansweredQuestions}/#{part.questions} " / -->
      <!-- h:outputText value="#{dmsg.ans_q}, " / -->
      <!-- h:outputText value="#{part.points}/#{part.maxPoints} #{dmsg.pt}" / -->
      <f:verbatim></h4><div class="tier1"></f:verbatim>
      <h:outputText value="#{part.text}" escape="false" rendered="#{part.numParts ne '1'}" />
      <f:verbatim></div></f:verbatim>
      <f:verbatim></h4><div class="tier2"></f:verbatim>
     <h:outputText value="#{msg.no_questions}" escape="false" rendered="#{part.noQuestions}"/>
      <f:verbatim></div></f:verbatim>

      <h:dataTable value="#{part.itemContents}" columnClasses="tier2"
          var="question">
        <h:column>
          <h:outputText value="<a name=\"" escape="false" />
          <h:outputText value="#{part.number}_#{question.number}\"></a>"
            escape="false" />
          <f:verbatim><h4 class="tier2"></f:verbatim>
            <h:outputText value="#{dmsg.q} #{question.number} #{dmsg.of} " />
            <h:outputText value="#{part.questions}#{dmsg.column}  " />
            <h:inputText id="adjustedScore" value="#{question.pointsForEdit}" >
<f:validateDoubleRange/>
<%--SAK-3776    <f:convertNumber maxFractionDigits="2"/> --%>
            </h:inputText>
            <h:outputText value=" #{dmsg.splash} #{question.maxPoints} " />
            <h:outputText value="#{dmsg.pt}"/>
          <f:verbatim><br/></f:verbatim>
<h:message for="adjustedScore" style="color:red"/>
          <f:verbatim></h4></f:verbatim>

          <f:verbatim><div class="tier3"></f:verbatim>
            
            <h:panelGroup rendered="#{question.itemData.typeId == 7}">
              <f:subview id="deliverAudioRecording">
               <%@ include file="/jsf/evaluation/item/displayAudioRecording.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup rendered="#{question.itemData.typeId == 6}">
              <f:subview id="deliverFileUpload">
                <%@ include file="/jsf/evaluation/item/displayFileUpload.jsp" %>
              </f:subview>
            </h:panelGroup>

   			<h:panelGroup rendered="#{question.itemData.typeId == 11}">
	      	<f:subview id="deliverFillInNumeric">
	        <%@ include file="/jsf/delivery/item/deliverFillInNumeric.jsp" %>
	      </f:subview>
            </h:panelGroup>


            <h:panelGroup rendered="#{question.itemData.typeId == 8}">
              <f:subview id="deliverFillInTheBlank">
                <%@ include file="/jsf/delivery/item/deliverFillInTheBlank.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup rendered="#{question.itemData.typeId == 9}">
              <f:subview id="deliverMatching">
                <%@ include file="/jsf/delivery/item/deliverMatching.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup
              rendered="#{question.itemData.typeId == 1 || question.itemData.typeId == 3}">
              <f:subview id="deliverMultipleChoiceSingleCorrect">
                <%@ include file="/jsf/delivery/item/deliverMultipleChoiceSingleCorrect.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup rendered="#{question.itemData.typeId == 2}">
              <f:subview id="deliverMultipleChoiceMultipleCorrect">
                <%@ include file="/jsf/delivery/item/deliverMultipleChoiceMultipleCorrect.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup rendered="#{question.itemData.typeId == 5}">
              <f:subview id="deliverShortAnswer">
                <%@ include file="/jsf/delivery/item/deliverShortAnswerLink.jsp" %>
              </f:subview>
            </h:panelGroup>

            <h:panelGroup rendered="#{question.itemData.typeId == 4}">
              <f:subview id="deliverTrueFalse">
                <%@ include file="/jsf/delivery/item/deliverTrueFalse.jsp" %>
              </f:subview>
            </h:panelGroup>
          <f:verbatim></div></f:verbatim>

          <h:panelGrid columns="2">
            <h:outputText value="#{dmsg.comment}#{dmsg.column}"/>
            <h:inputTextarea value="#{question.gradingComment}" rows="3" cols="30"/>
          </h:panelGrid>

        </h:column>
      </h:dataTable>
    </h:column>
  </h:dataTable>
</div>

<p class="act">
   <h:commandButton accesskey="#{msg.a_save}" styleClass="active" value="#{msg.save_cont}" action="totalScores" type="submit">
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreUpdateListener" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.StudentScoreListener" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.ResetTotalScoreListener" />
      <f:actionListener
         type="org.sakaiproject.tool.assessment.ui.listener.evaluation.TotalScoreListener" />
   </h:commandButton>
   <h:commandButton accesskey="#{msg.a_cancel}" value="#{msg.cancel}" action="totalScores" immediate="true">
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.ResetTotalScoreListener" />
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.evaluation.TotalScoreListener" />
   </h:commandButton>
</p>
</h:form>
</div>
  <!-- end content -->
      </body>
    </html>
  </f:view>
