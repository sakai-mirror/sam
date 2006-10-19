<%-- $Id$
include file for delivering matching questions
should be included in file importing DeliveryMessages
--%>
<!--
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
  <!-- ATTACHMENTS -->
  <%@ include file="/jsf/author/preview_item/attachment.jsp" %>

  <h:outputText escape="false" value="#{question.instruction}" />
  <!-- 1. print out the matching choices -->
  <h:dataTable value="#{question.itemData.itemTextArraySorted}" var="itemText">
    <h:column>
      <h:dataTable value="#{itemText.answerArraySorted}" var="answer"
         rendered="#{itemText.sequence==1}">
        <h:column>
            <h:panelGrid columns="2">
              <h:outputText escape="false" value="#{answer.label}. "/>
              <h:outputText escape="false" value="#{answer.text}" />
            </h:panelGrid>
        </h:column>
      </h:dataTable>
    </h:column>
  </h:dataTable>

  <!-- 2. print out the matching text -->
  <h:dataTable value="#{question.itemData.itemTextArraySorted}" var="itemText">
    <h:column>
      <h:panelGrid columns="2">
        <h:selectOneMenu id="label" disabled="true">
          <f:selectItem itemValue="" itemLabel="select"/>
          <f:selectItem itemValue="" itemLabel="A"/>
          <f:selectItem itemValue="" itemLabel="B"/>
          <f:selectItem itemValue="" itemLabel="C"/>
        </h:selectOneMenu>
        <h:outputText escape="false" value="#{itemText.sequence}. #{itemText.text}" />

        <h:outputText value="" />

        <%-- show correct & incorrect answer feedback, only need to show the set that is attached
             to the correct answer. Look at the data in the table and you may understand this part
             better -daisyf --%>
        <h:dataTable value="#{itemText.answerArray}" var="answer">
            <h:column>
              <h:panelGroup rendered="#{answer.isCorrect && answer.correctAnswerFbIsNotEmpty && assessmentSettings.feedbackAuthoring ne '1'}" styleClass="longtext">
                <h:outputLabel value="#{msg.correct}: " />
                <h:outputText escape="false" value="#{answer.correctAnswerFeedback}" />
              </h:panelGroup>
            </h:column>
        </h:dataTable>

        <h:outputText value="" />

        <h:dataTable value="#{itemText.answerArray}" var="answer">
            <h:column>
              <h:panelGroup rendered="#{answer.isCorrect && answer.incorrectAnswerFbIsNotEmpty && assessmentSettings.feedbackAuthoring ne '1'}" styleClass="longtext">
                <h:outputLabel value="#{msg.incorrect}: " />
                <h:outputText escape="false" value="#{answer.inCorrectAnswerFeedback}" />
              </h:panelGroup>
            </h:column>
        </h:dataTable>

      </h:panelGrid>
    </h:column>
  </h:dataTable>

      <%-- answer key --%>
 <h:panelGroup>
      <h:outputLabel value="#{msg.answerKey}: "/>
      <h:outputText escape="false" value="#{question.itemData.answerKey}" />
<f:verbatim><br/></f:verbatim>
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.correctItemFbIsNotEmpty && assessmentSettings.feedbackAuthoring ne '2'}">
      <h:outputLabel value="#{msg.correct}:"/>
      <h:outputText value="#{question.itemData.correctItemFeedback}" escape="false" />
<f:verbatim><br/></f:verbatim>
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.incorrectItemFbIsNotEmpty && assessmentSettings.feedbackAuthoring ne '2'}">
     <h:outputLabel value="#{msg.incorrect}:"/>
      <h:outputText value="#{question.itemData.inCorrectItemFeedback}" escape="false" />
</h:panelGroup>
