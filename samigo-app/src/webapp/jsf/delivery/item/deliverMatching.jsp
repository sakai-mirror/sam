<%-- $Id$
include file for delivering matching questions
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
  <h:outputText value="#{question.text}"  escape="false"/>
  <!-- ATTACHMENTS -->
  <%@ include file="/jsf/delivery/item/attachment.jsp" %>

  <h:dataTable value="#{question.answers}" var="answer">
   <h:column>
     <h:outputText value="#{answer}" escape="false" />
   </h:column>
  </h:dataTable>
  <h:dataTable value="#{question.matchingArray}" var="matching">
    <h:column rendered="#{delivery.feedback eq 'true' &&
       delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true'}">
      <h:graphicImage id="image"
        rendered="#{matching.isCorrect}"
        alt="#{deliveryMessages.alt_correct}" url="/images/checkmark.gif" >
      </h:graphicImage>
      <h:graphicImage id="image2"
        rendered="#{matching.isCorrect}"
        width="16" height="16"
        alt="#{deliveryMessages.alt_incorrect}" url="/images/delivery/spacer.gif">
      </h:graphicImage>
   </h:column>
   <h:column>
    <h:selectOneMenu value="#{matching.response}"
      disabled="#{delivery.actionString=='previewAssessment'
               || delivery.actionString=='reviewAssessment'
               || delivery.actionString=='gradeAssessment'}">
        <f:selectItems value="#{matching.choices}" />
    </h:selectOneMenu>
   </h:column>
   <h:column>
     <h:outputText value="#{matching.text}" escape="false"/>
     <h:panelGroup rendered="#{delivery.feedback eq 'true' &&
       delivery.feedbackComponent.showSelectionLevel && 
	   matching.feedback ne '' && matching.feedback != 'null' && matching.feedback != null}" >
	   <!-- The above != 'null' is for SAK-5475. Once it gets fixed, we can remove this condition -->
       <f:verbatim><br /></f:verbatim>
       <h:outputText value="#{deliveryMessages.feedback}#{deliveryMessages.column} " />
       <h:outputText value="#{matching.feedback}" escape="false" />
     </h:panelGroup>
  </h:column>
  </h:dataTable>

<f:verbatim><br /></f:verbatim>
<h:selectBooleanCheckbox value="#{question.review}" id="mark_for_review"
   rendered="#{(delivery.actionString=='previewAssessment'
                || delivery.actionString=='takeAssessment'
                || delivery.actionString=='takeAssessmentViaUrl')
             && delivery.navigation ne '1'}" />
<h:outputLabel for="mark_for_review" value="#{deliveryMessages.mark}"
  rendered="#{(delivery.actionString=='previewAssessment'
                || delivery.actionString=='takeAssessment'
                || delivery.actionString=='takeAssessmentViaUrl')
             && delivery.navigation ne '1'}" />

<h:panelGroup rendered="#{delivery.feedback eq 'true'}">
  <f:verbatim><br /></f:verbatim>
  <h:panelGroup rendered="#{delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true'}" >
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="answerKeyMC" value="#{deliveryMessages.ans_key}: " />
     <f:verbatim></b></f:verbatim>
    <h:outputText id="answerKeyMC"
       value="#{question.key}" escape="false" />

  </h:panelGroup>
  <h:panelGroup rendered="#{delivery.feedbackComponent.showItemLevel && !delivery.noFeedback=='true' && question.feedbackIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="feedSC" value="#{deliveryMessages.feedback}: " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="feedSC" value="#{question.feedback}" escape="false" />
  </h:panelGroup>
  <h:panelGroup rendered="#{delivery.actionString !='gradeAssessment' && delivery.feedbackComponent.showGraderComment && !delivery.noFeedback=='true' && question.gradingCommentIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="commentSC" value="#{deliveryMessages.comment}: " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="commentSC" value="#{question.gradingComment}"
      escape="false" />
  </h:panelGroup>
</h:panelGroup>
