<%--
include file for delivering audio questions
should be included in file importing DeliveryMessages
--%>
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
  <h:outputText escape="false" value="#{question.itemData.text}" />
  <!-- ATTACHMENTS -->
  <%@ include file="/jsf/author/preview_item/attachment.jsp" %>

  <f:verbatim><br/>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
  <h:outputText escape="false" value="#{msg.time_allowed_seconds} #{question.itemData.duration}" />
  <f:verbatim><br/>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>

  <h:outputText escape="false" value="#{msg.number_of_tries}: " />
  <h:panelGroup rendered="#{question.itemData.triesAllowed > 10}">
    <h:outputText escape="false" value="Unlimited" />
  </h:panelGroup>
  <h:panelGroup rendered="#{question.itemData.triesAllowed <= 10}">
    <h:outputText escape="false" value="#{question.itemData.triesAllowed}" />
  </h:panelGroup>

  <h:dataTable value="#{question.itemData.itemTextArraySorted}" var="itemText">
    <h:column>
      <h:dataTable value="#{itemText.answerArray}" var="answer">
        <h:column>
          <h:outputLabel rendered="#{answer.textIsNotEmpty}" value="#{msg.preview_model_short_answer}" />
          <h:outputText escape="false" value="#{answer.text}" />
        </h:column>
      </h:dataTable>
    </h:column>
  </h:dataTable>
 <h:panelGroup rendered="#{question.itemData.generalItemFbIsNotEmpty && assessmentSettings.feedbackAuthoring ne '2'}">
 <h:outputLabel value="#{msg.feedback}: " />
  <h:outputText escape="false" value="#{question.itemData.generalItemFeedback}" />
</h:panelGroup>