<%-- $Id$
include file for delivering multiple choice questions
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
<h:outputText value="<script>" escape="false" />
<h:outputText value="var selectedRadioButton#{question.itemData.itemId};" escape="false" />
<h:outputText value="function uncheckRadioButtons#{question.itemData.itemId}(radioButton) {" escape="false" />
<h:outputText value="if (selectedRadioButton#{question.itemData.itemId} != null) {" escape="false" />
<h:outputText value="selectedRadioButton#{question.itemData.itemId}.checked = false;" escape="false" />
<h:outputText value="}" escape="false" />
<h:outputText value="selectedRadioButton#{question.itemData.itemId} = radioButton;" escape="false" />
<h:outputText value="selectedRadioButton#{question.itemData.itemId}.checked = true;" escape="false" />
<h:outputText value="}" escape="false" />
<h:outputText value="</script>" escape="false" />

  <h:outputText value="#{question.text}"  escape="false"/>
  <!-- ATTACHMENTS -->
  <%@ include file="/jsf/delivery/item/attachment.jsp" %>

  <h:dataTable value="#{question.selectionArray}" var="selection">
    <h:column rendered="#{delivery.feedback eq 'true' &&
       delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true'}">
      <h:graphicImage id="image"
        rendered="#{selection.answer.isCorrect eq 'true' && selection.response}"
        alt="#{msg.alt_correct}" url="/images/checkmark.gif" >
      </h:graphicImage>
      <h:graphicImage id="image2"
        rendered="#{selection.answer.isCorrect ne 'true' && selection.response}"
        width="16" height="16"
        alt="#{msg.alt_incorrect}" url="/images/delivery/spacer.gif">
      </h:graphicImage>
    </h:column>
    <h:column>

     <h:selectOneRadio onfocus="if (this.defaultChecked) { uncheckRadioButtons#{question.itemData.itemId}(this) };" onclick="uncheckRadioButtons#{question.itemData.itemId}(this);" onkeypress="uncheckRadioButtons#{question.itemData.itemId}(this);" required="false" 
        disabled="#{delivery.actionString=='reviewAssessment'
                 || delivery.actionString=='gradeAssessment'}" 
       value="#{question.responseId}" layout="pageLayout">
       <f:selectItem itemValue="#{selection.answerId}" />
     </h:selectOneRadio>

    </h:column>
    <h:column>
     <h:outputText value=" #{selection.answer.label}" escape="false" />
     <h:outputText value="." rendered="#{selection.answer.label ne ''}" />
     <h:outputText value=" #{selection.answer.text}" escape="false" />
    </h:column>
    <h:column>
      <h:panelGroup rendered="#{delivery.feedback eq 'true' &&
       delivery.feedbackComponent.showSelectionLevel && question.itemData.typeId != 3 &&
	   selection.answer.generalAnswerFeedback != 'null' && selection.answer.generalAnswerFeedback != null && selection.answer.generalAnswerFeedback != '' && selection.response}" >
   	   <!-- The above != 'null' is for SAK-5475. Once it gets fixed, we can remove this condition -->
       <f:verbatim><br /></f:verbatim>
       <h:outputText value="#{msg.feedback}#{msg.column} " />
       <h:outputText value="#{selection.answer.generalAnswerFeedback}" escape="false" />
      </h:panelGroup>
    </h:column>
  </h:dataTable>

  <h:panelGroup
    rendered="#{question.itemData.hasRationale && question.itemData.typeId != 3}" >
    <f:verbatim><br /></f:verbatim>
    <h:outputLabel for="rationale" value="#{msg.rationale}" />
    <f:verbatim><br /></f:verbatim>
    <h:inputTextarea id="rationale" value="#{question.rationale}" rows="5" cols="40" 
        disabled="#{delivery.actionString=='reviewAssessment'
                 || delivery.actionString=='gradeAssessment'}" />
  </h:panelGroup>

<f:verbatim><br /></f:verbatim>
<h:selectBooleanCheckbox value="#{question.review}" id="mark_for_review" 
   rendered="#{(delivery.actionString=='previewAssessment'
                || delivery.actionString=='takeAssessment'
                || delivery.actionString=='takeAssessmentViaUrl')
             && delivery.navigation ne '1'}" />
<h:outputLabel for="mark_for_review" value="#{msg.mark}"
  rendered="#{(delivery.actionString=='previewAssessment'
                || delivery.actionString=='takeAssessment'
                || delivery.actionString=='takeAssessmentViaUrl')
             && delivery.navigation ne '1'}" />

<h:panelGroup rendered="#{delivery.feedback eq 'true'}">
  <f:verbatim><br /></f:verbatim>

  <h:panelGroup rendered="#{delivery.feedbackComponent.showCorrectResponse && !delivery.noFeedback=='true' && question.itemData.typeId != 3}" >
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="answerKeyMC" value="#{msg.ans_key}#{msg.column} " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="answerKeyMC"
       value="#{question.key}" escape="false" />

  </h:panelGroup>

  <h:panelGroup rendered="#{delivery.feedbackComponent.showItemLevel && !delivery.noFeedback=='true' && question.feedbackIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="feedSC" value="#{msg.feedback}#{msg.column} " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="feedSC" value="#{question.feedback}" escape="false" />
  </h:panelGroup>
  <h:panelGroup rendered="#{delivery.feedbackComponent.showGraderComment && !delivery.noFeedback=='true' && question.gradingCommentIsNotEmpty}">
    <f:verbatim><br /></f:verbatim>
    <f:verbatim><b></f:verbatim>
    <h:outputLabel for="commentSC" value="#{msg.comment}#{msg.column} " />
    <f:verbatim></b></f:verbatim>
    <h:outputText id="commentSC" value="#{question.gradingComment}"
      escape="false" />
  </h:panelGroup>
</h:panelGroup>
