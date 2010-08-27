<%-- $Id: ExtendedMatchingItem.jsp 2009-12-10 gopalrc $
include file for delivering extended matching items
should be included in file importing DeliveryMessages
--%>
<!--
<%--
***********************************************************************************
*
* Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
*
* Licensed under the Educational Community License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.osedu.org/licenses/ECL-2.0
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
  ***** GOPAL TEST/TEMP - /jsf/author/preview_item/ExtendedMatchingItem.jsp *****
  ***** Included In /jsf/author/previewAssessment.jsp ************
  <h:outputText escape="false" value="#{question.class.name}" />
  



      
      <h:outputText escape="false" value="#{question.itemData.themeText}" />
      <f:verbatim><br/><br/></f:verbatim>


      <h:dataTable value="#{question.itemData.emiAnswerOptions}" var="option"  rendered="#{question.itemData.isAnswerOptionsSimple}" border="1" style="border-style:solid">
        <h:column> 
          <h:panelGroup rendered="#{option.text != null && option.text ne ''}">
            <h:outputText escape="false" value="#{option.label}. #{option.text}" /> 
          </h:panelGroup>
        </h:column>
      </h:dataTable>
      

  <!-- RICH TEXT - EMI RICH ANSWER OPTIONS-->
  <h:outputText value="#{question.itemData.emiAnswerOptionsRichText}"  escape="false" rendered="#{question.itemData.isAnswerOptionsRich}"/>
      
  <!-- ATTACHMENTS BELOW - EMI RICH ANSWER OPTIONS-->
  <h:dataTable value="#{question.itemData.itemAttachmentList}" var="attach"  rendered="#{question.itemData.isAnswerOptionsRich}">
    <h:column>
      <%@ include file="/jsf/shared/mimeicon.jsp" %>
    </h:column>
    <h:column>
      <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputLink value="#{attach.location}" target="new_window">
         <h:outputText escape="false" value="#{attach.filename}" />
      </h:outputLink>
    </h:column>
    <h:column>
      <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputText escape="false" value="#{attach.fileSize} #{generalMessages.kb}" rendered="#{!attach.isLink}"/>
    </h:column>
  </h:dataTable>
  <!-- ATTACHMENTS ABOVE - EMI RICH ANSWER OPTIONS-->


      
      <f:verbatim><br/><br/></f:verbatim>
      <h:outputText escape="false" value="#{question.itemData.leadInText}" />
      <f:verbatim><br/><br/></f:verbatim>
      
      

      <h:dataTable value="#{question.itemData.emiQuestionAnswerCombinations}" var="item" border="1" style="border-style:solid">

        <h:column> 
      <h:dataTable value="#{item.answerArraySorted}" var="answerOption">
        <h:column>
         <h:panelGroup rendered="#{answerOption.text != null && answerOption.text ne '' && answerOption.isCorrect}">
          <h:outputText escape="false" value="#{answerOption.label}" /> 
        </h:panelGroup>
        </h:column><h:column>
        <h:panelGroup rendered="#{answerOption.text ne null && answerOption.text ne '' && author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '1' && answerOption.generalAnswerFbIsNotEmpty}">    
         <h:outputLabel value=" #{authorMessages.feedback}: " />
         <h:outputText escape="false" value="#{answerOption.generalAnswerFeedback}" />
		</h:panelGroup>
        <h:panelGroup rendered="#{answerOption.text ne null && answerOption.text ne '' && !author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '1' && answerOption.generalAnswerFbIsNotEmpty}">    
         <h:outputLabel value=" #{authorMessages.feedback}: " />
         <h:outputText escape="false" value="#{answerOption.generalAnswerFeedback}" />
		</h:panelGroup>
        </h:column>
      </h:dataTable>
        </h:column>

        <h:column> 
         <h:panelGroup rendered="#{item.text != null && item.text ne ''}">
          <h:outputText escape="false" value="#{item.sequence}. #{item.text}" />
          
  <!-- ATTACHMENTS BELOW - EMI ITEMTEXT ATTACHMENTS-->
  <h:dataTable value="#{item.itemTextAttachmentList}" var="attach">
    <h:column>
      <%@ include file="/jsf/shared/mimeicon.jsp" %>
    </h:column>
    <h:column>
      <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputLink value="#{attach.location}" target="new_window">
         <h:outputText escape="false" value="#{attach.filename}" />
      </h:outputLink>
    </h:column>
    <h:column>
      <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:outputText escape="false" value="#{attach.fileSize} #{generalMessages.kb}" rendered="#{!attach.isLink}"/>
    </h:column>
  </h:dataTable>
  <!-- ATTACHMENTS ABOVE - EMI ITEMTEXT ATTACHMENTS-->
           
         </h:panelGroup>
        </h:column>
        
        

      </h:dataTable>
      
      <f:verbatim><br/><br/><br/></f:verbatim>



<h:panelGroup>
  <h:outputLabel value="#{authorMessages.answerKey}: "/>
  <h:outputText escape="false" value="#{question.itemData.answerKey}" />
  <f:verbatim><br/></f:verbatim>
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.correctItemFbIsNotEmpty && author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '2'}">
  <h:outputLabel value="#{authorMessages.correctItemFeedback}: "/>
  <h:outputText  value="#{question.itemData.correctItemFeedback}" escape="false" />
 <f:verbatim><br/></f:verbatim>
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.incorrectItemFbIsNotEmpty && author.isEditPendingAssessmentFlow && assessmentSettings.feedbackAuthoring ne '2'}">
  <h:outputLabel value="#{authorMessages.incorrectItemFeedback}: "/>
  <h:outputText value="#{question.itemData.inCorrectItemFeedback}" escape="false" />
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.correctItemFbIsNotEmpty && !author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '2'}">
  <h:outputLabel value="#{authorMessages.correctItemFeedback}: "/>
  <h:outputText  value="#{question.itemData.correctItemFeedback}" escape="false" />
 <f:verbatim><br/></f:verbatim>
</h:panelGroup>
<h:panelGroup rendered="#{question.itemData.incorrectItemFbIsNotEmpty && !author.isEditPendingAssessmentFlow && publishedSettings.feedbackAuthoring ne '2'}">
  <h:outputLabel value="#{authorMessages.incorrectItemFeedback}: "/>
  <h:outputText value="#{question.itemData.inCorrectItemFeedback}" escape="false" />
</h:panelGroup>
