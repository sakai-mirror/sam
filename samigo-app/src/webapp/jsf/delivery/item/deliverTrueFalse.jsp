<%-- $Id$
include file for delivering multiple choice single correct survey questions
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

  <h:dataTable value="#{question.itemData.itemTextArraySorted}" var="itemText">
   <h:column rendered="#{delivery.feedback eq 'true' &&
           delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true'}">
      <h:dataTable value="#{question.answers}" var="answer">
        <h:column>
          <h:graphicImage id="image" rendered="#{answer.description eq 'true' && question.responseId eq answer.value}"
            alt="#{deliveryMessages.alt_correct}" url="/images/checkmark.gif" >
          </h:graphicImage>
          <h:graphicImage id="image2" rendered="#{answer.description ne 'true' && question.responseId eq answer.value}
"
            width="16" height="16"
            alt="#{deliveryMessages.alt_incorrect}" url="/images/delivery/spacer.gif">
          </h:graphicImage>
       </h:column>
     </h:dataTable>
   </h:column>
   <h:column>
      <h:selectOneRadio id="question" value="#{question.responseId}" layout="pagedirection" 
        disabled="#{delivery.actionString=='reviewAssessment'
                 || delivery.actionString=='gradeAssessment'}" >
        <f:selectItems value="#{question.answers}" />
      </h:selectOneRadio>
   </h:column>
  </h:dataTable>

  <h:panelGroup rendered="#{question.itemData.hasRationale}">
    <f:verbatim><br /></f:verbatim>
    <h:outputLabel for="rationale" value="#{deliveryMessages.rationale}" />
    <f:verbatim><br /></f:verbatim>
    <h:inputTextarea id="rationale" value="#{question.rationale}" rows="5" cols="40" 
        rendered="#{delivery.actionString!='reviewAssessment' 
                 && delivery.actionString!='gradeAssessment'}" />
    <h:outputText id="rationale2" value="#{question.rationale}" 
        rendered="#{delivery.actionString=='reviewAssessment'
                 || delivery.actionString=='gradeAssessment'}"/>
  </h:panelGroup>

<h:panelGroup>
	<f:verbatim><br /></f:verbatim>
	<h:commandLink id="cmdclean" value="#{deliveryMessages.reset_selection}" action="#{delivery.cleanRadioButton}" 
		rendered="#{(delivery.actionString=='previewAssessment' || delivery.actionString=='previewAssessmentPublished'
                || delivery.actionString=='takeAssessment' 
                || delivery.actionString=='takeAssessmentViaUrl')}">
		<f:param name="radioId" value="#{question.itemData.itemId}" />
	</h:commandLink> 
</h:panelGroup>

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

  <h:panelGroup rendered="#{delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true'}" >
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="answerKeyMC" value="#{deliveryMessages.ans_key}#{deliveryMessages.column} " />
     <f:verbatim></b></f:verbatim>
    <h:outputText id="answerKeyMC" escape="false"
       value="#{question.key}"/>

  </h:panelGroup>
  <h:panelGroup rendered="#{delivery.feedbackComponent.showItemLevel && !delivery.noFeedback=='true' && question.feedbackIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="feedSC" value="#{deliveryMessages.feedback}#{deliveryMessages.column} " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="feedSC" value="#{question.feedback}" escape="false" />
  </h:panelGroup>
  <h:panelGroup rendered="#{delivery.actionString !='gradeAssessment' && delivery.feedbackComponent.showGraderComment && !delivery.noFeedback=='true' && question.gradingCommentIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="commentSC" value="#{deliveryMessages.comment}#{deliveryMessages.column} " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="commentSC" value="#{question.gradingComment}"
      escape="false" />
  </h:panelGroup>
</h:panelGroup>
