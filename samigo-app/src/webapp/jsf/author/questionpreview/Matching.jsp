<%-- $Id$
include file for delivering matching questions
should be included in file importing DeliveryMessages
--%>
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


  <h:outputText escape="false" value="#{itemContents.instruction}" />
  <!-- 1. print out the matching choices -->
  <h:dataTable value="#{itemContents.itemData.itemTextArraySorted}" var="itemText">
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
  <h:dataTable value="#{itemContents.itemData.itemTextArraySorted}" var="itemText">
    <h:column>
      <h:panelGrid columns="2">
        <h:selectOneMenu id="label" disabled="true">
          <f:selectItem itemValue="" itemLabel="#{authorMessages.select_combo}"/>
          <f:selectItem itemValue="" itemLabel="A"/>
          <f:selectItem itemValue="" itemLabel="B"/>
          <f:selectItem itemValue="" itemLabel="C"/>
        </h:selectOneMenu>
        <h:outputText escape="false" value="#{itemText.sequence}. #{itemText.text}" />

        <h:outputText value="" />
        <h:panelGroup>
          <h:outputText value="Correct Feedback:" />
          <h:dataTable value="#{itemText.answerArray}" var="answer">
            <h:column>
              <h:outputText escape="false" value="#{answer.correctAnswerFeedback}" />
            </h:column>
          </h:dataTable>
        </h:panelGroup>

        <h:outputText value="" />
        <h:panelGroup>
          <h:outputText value="Incorrect Feedback:" />
          <h:dataTable value="#{itemText.answerArray}" var="answer">
            <h:column>
              <h:outputText escape="false" value="#{answer.inCorrectAnswerFeedback}" />
            </h:column>
          </h:dataTable>
        </h:panelGroup>
      </h:panelGrid>
    </h:column>
  </h:dataTable>

      <%-- answer key --%>

      <h:outputText escape="false" value="Answer Key: #{itemContents.itemData.answerKey}" />
      <f:verbatim><br/></f:verbatim>
      <%-- question level feedback --%>
      <h:outputText escape="false" value="#{authorMessages.q_level_feedb}:" />
      <f:verbatim><br/>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputText escape="false" value="#{authorMessages.correct}:  #{itemContents.itemData.correctItemFeedback}" />
      <f:verbatim><br/>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputText escape="false" value="#{authorMessages.incorrect}:  #{itemContents.itemData.inCorrectItemFeedback}"/>

