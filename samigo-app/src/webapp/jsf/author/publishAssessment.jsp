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
* Copyright (c) 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
*
* Licensed under the Educational Community License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*       http://www.osedu.org/licenses/ECL-2.0
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
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{assessmentSettingsMessages.publish_assessment_confirmation}" /></title>
      </head>
      <body onload="<%= request.getAttribute("html.body.onload") %>">

<div class="portletBody">
 <!-- content... -->
 <h:form id="publishAssessmentForm">
   <h:inputHidden id="assessmentId" value="#{assessmentSettings.assessmentId}"/>
   <h3>
      <h:outputText  value="#{assessmentSettingsMessages.publish_assessment_confirmation}" rendered="#{author.isEditPendingAssessmentFlow}"/>
      <h:outputText  value="#{assessmentSettingsMessages.republish_assessment_confirmation}" rendered="#{!author.isEditPendingAssessmentFlow && !author.isRepublishAndRegrade}"/>
      <h:outputText  value="#{assessmentSettingsMessages.regrade_republish_assessment_confirmation}" rendered="#{!author.isEditPendingAssessmentFlow && author.isRepublishAndRegrade}"/>
   </h3>
<div class="tier1">

  <!-- Error publishing assessment -->
  <h:messages globalOnly="true" infoClass="validation" warnClass="validation" errorClass="validation" fatalClass="validation"/>

<h:panelGrid border="0" width="100%">
  <h:outputText value=" " />
  <h:panelGroup rendered="#{author.isEditPendingAssessmentFlow}" styleClass="validation">
    <h:panelGrid  columns="1">
	   <h:outputText value="#{assessmentSettingsMessages.publish_confirm_message_1}" />
       <h:outputText value="#{assessmentSettingsMessages.publish_confirm_message_2_no_hyphen}"/>
    </h:panelGrid>
  </h:panelGroup>

  <h:panelGroup rendered="#{!author.isEditPendingAssessmentFlow && !author.isRepublishAndRegrade}">
       <h:outputText value="#{assessmentSettingsMessages.republish_confirm_message}" />
  </h:panelGroup>

  <h:panelGroup rendered="#{!author.isEditPendingAssessmentFlow && (author.isRepublishAndRegrade && !assessmentBean.hasSubmission)}" styleClass="validation">
    <h:panelGrid  columns="1">
      <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_1}" /> 
	  <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_2}" />
	  <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_3}" />
    </h:panelGrid>
  </h:panelGroup>

  <h:panelGroup rendered="#{publishedSettings.itemNavigation eq '2' && !author.isEditPendingAssessmentFlow && author.isRepublishAndRegrade && assessmentBean.hasSubmission}"  styleClass="validation">
    <h:panelGrid  columns="1">
      <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_1}" /> 
	  <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_2}" />
	  <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_tip_3}" />
	  <h:panelGroup>
        <h:selectBooleanCheckbox id="updateMostCurrentSubmissionCheckbox2" value="#{publishedSettings.updateMostCurrentSubmission}"/>
        <h:outputText value="#{assessmentSettingsMessages.update_most_current_submission_checkbox}" />
      </h:panelGroup>
    </h:panelGrid>
  </h:panelGroup>
  <h:outputText value=" " />
  <h:outputText value=" " />
</h:panelGrid>

<h:panelGrid columns="2" rowClasses="shorttext" rendered="#{author.isEditPendingAssessmentFlow}" border="0">

     <h:outputLabel value="#{assessmentSettingsMessages.assessment_title}" rendered="#{assessmentSettings.title ne null}"/>
     <h:outputText value="#{assessmentSettings.title}" rendered="#{assessmentSettings.title ne null}" escape="false" />

     <h:outputLabel value="#{assessmentSettingsMessages.assessment_available_date}" />
     <h:panelGroup>
       <h:outputText rendered="#{assessmentSettings.startDate ne null}" value="#{assessmentSettings.startDate}" >
          <f:convertDateTime pattern="#{generalMessages.output_date_picker}"/>
       </h:outputText>
       <h:outputText rendered="#{assessmentSettings.startDate eq null}" value="Immediate" />
     </h:panelGroup>

     <h:outputLabel rendered="#{assessmentSettings.dueDate ne null}" 
        value="#{assessmentSettingsMessages.assessment_due_date}" />
     <h:outputText value="#{assessmentSettings.dueDate}" 
        rendered="#{assessmentSettings.dueDate ne null}" >
       <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
     </h:outputText>

     <h:outputLabel rendered="#{assessmentSettings.retractDate ne null}" value="#{assessmentSettingsMessages.assessment_retract_date}" />
     <h:outputText value="#{assessmentSettings.retractDate}" rendered="#{assessmentSettings.retractDate ne null}">
       <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
     </h:outputText>

     <h:outputLabel value="#{assessmentSettingsMessages.time_limit}" />
     <h:panelGroup>
       <h:outputText rendered="#{assessmentSettings.valueMap.hasTimeAssessment eq 'true'}"
          value="#{assessmentSettings.timedHours} hour,
          #{assessmentSettings.timedMinutes} minutes, #{assessmentSettings.timedSeconds} seconds. " />
       <h:outputText rendered="#{assessmentSettings.valueMap.hasTimeAssessment eq 'true'}"
          value="#{assessmentSettingsMessages.auto_submit_description}" />
       <h:outputText rendered="#{assessmentSettings.valueMap.hasTimeAssessment ne 'true'}"
          value="#{assessmentSettingsMessages.no_time_limit}" />
     </h:panelGroup>

     <h:outputLabel value="#{assessmentSettingsMessages.submissions}" />
     <h:panelGroup>
       <h:outputText value="#{assessmentSettingsMessages.unlimited_submission}" rendered="#{assessmentSettings.unlimitedSubmissions eq '1'}" />
       <h:outputText value="#{assessmentSettings.submissionsAllowed}"
         rendered="#{assessmentSettings.unlimitedSubmissions eq '0'}" />
     </h:panelGroup>


     <h:outputLabel value="#{assessmentSettingsMessages.feedback_type}" />
     <h:panelGroup>
       <h:outputText value="#{assessmentSettingsMessages.immediate}" rendered="#{assessmentSettings.feedbackDelivery eq '1'}" />
       <h:outputText value="#{assessmentSettingsMessages.no_feedback_short}" rendered="#{assessmentSettings.feedbackDelivery eq '3'}" />
       <h:outputText value="#{assessmentSettingsMessages.available_on} #{assessmentSettings.feedbackDate}"
          rendered="#{assessmentSettings.feedbackDelivery eq '2'}" >
         <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
       </h:outputText>
       <h:outputText value="#{assessmentSettingsMessages.feedback_on_submission}" rendered="#{assessmentSettings.feedbackDelivery eq '4'}" />
     </h:panelGroup>

     <h:outputLabel value="#{assessmentSettingsMessages.released_to_2}" />
     <h:outputText value="#{assessmentSettings.releaseTo}" />


     <h:outputLabel rendered="#{assessmentSettings.publishedUrl ne null}" value="#{assessmentSettingsMessages.published_assessment_url}" />
     <h:outputText value="#{assessmentSettings.publishedUrl}" />

	 <f:facet name="footer">
	   <h:panelGroup>
	     <h:outputText value="#{assessmentSettingsMessages.open_new_browser_for_publishedUrl}" />
	   </h:panelGroup>
     </f:facet>

</h:panelGrid>

<h:panelGrid columns="2" rowClasses="shorttext" rendered="#{!author.isEditPendingAssessmentFlow}" border="0">

     <h:outputLabel value="#{assessmentSettingsMessages.assessment_title}" rendered="#{publishedSettings.title ne null}" />
     <h:outputText value="#{publishedSettings.title}" rendered="#{publishedSettings.title ne null}"  escape="false"/>

     <h:outputLabel value="#{assessmentSettingsMessages.assessment_available_date}" />
     <h:panelGroup>
       <h:outputText rendered="#{publishedSettings.startDate ne null}" value="#{publishedSettings.startDate}" >
          <f:convertDateTime pattern="#{generalMessages.output_date_picker}"/>
       </h:outputText>
       <h:outputText rendered="#{publishedSettings.startDate eq null}" value="Immediate" />
     </h:panelGroup>

     <h:outputLabel rendered="#{publishedSettings.dueDate ne null}" 
        value="#{assessmentSettingsMessages.assessment_due_date}" />
     <h:outputText value="#{publishedSettings.dueDate}" 
        rendered="#{publishedSettings.dueDate ne null}" >
       <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
     </h:outputText>

     <h:outputLabel rendered="#{publishedSettings.retractDate ne null}" value="#{assessmentSettingsMessages.assessment_retract_date}" />
     <h:outputText value="#{publishedSettings.retractDate}" rendered="#{publishedSettings.retractDate ne null}">
       <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
     </h:outputText>

     <h:outputLabel value="#{assessmentSettingsMessages.time_limit}" />
     <h:panelGroup>
       <h:outputText rendered="#{publishedSettings.valueMap.hasTimeAssessment eq 'true'}"
          value="#{publishedSettings.timedHours} hour,
          #{publishedSettings.timedMinutes} minutes, #{publishedSettings.timedSeconds} seconds. " />
       <h:outputText rendered="#{publishedSettings.valueMap.hasTimeAssessment eq 'true'}"
          value="#{assessmentSettingsMessages.auto_submit_description}" />
       <h:outputText rendered="#{publishedSettings.valueMap.hasTimeAssessment ne 'true'}"
          value="#{assessmentSettingsMessages.no_time_limit}" />
     </h:panelGroup>

     <h:outputLabel value="#{assessmentSettingsMessages.submissions}" />
     <h:panelGroup>
       <h:outputText value="#{assessmentSettingsMessages.unlimited_submission}" rendered="#{publishedSettings.unlimitedSubmissions eq '1'}" />
       <h:outputText value="#{publishedSettings.submissionsAllowed}"
         rendered="#{publishedSettings.unlimitedSubmissions eq '0'}" />
     </h:panelGroup>


     <h:outputLabel value="#{assessmentSettingsMessages.feedback_type}" />
     <h:panelGroup>
       <h:outputText value="#{assessmentSettingsMessages.immediate}" rendered="#{publishedSettings.feedbackDelivery eq '1'}" />
       <h:outputText value="#{assessmentSettingsMessages.no_feedback_short}" rendered="#{publishedSettings.feedbackDelivery eq '3'}" />
       <h:outputText value="#{assessmentSettingsMessages.available_on} #{publishedSettings.feedbackDate}"
          rendered="#{publishedSettings.feedbackDelivery eq '2'}" >
         <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
       </h:outputText>
     </h:panelGroup>

     <h:outputLabel value="#{assessmentSettingsMessages.released_to_2}" />
     <h:outputText value="#{publishedSettings.releaseTo}" />

     <h:outputLabel rendered="#{publishedSettings.publishedUrl ne null}" value="#{assessmentSettingsMessages.published_assessment_url}" />
     <h:outputText value="#{publishedSettings.publishedUrl}" />
     
	 <f:facet name="footer">
	   <h:panelGroup>
	     <h:outputText value="#{assessmentSettingsMessages.open_new_browser_for_publishedUrl}" />
	   </h:panelGroup>
     </f:facet>
</h:panelGrid>

<f:verbatim><p></p></f:verbatim>

<script language="javascript" type="text/JavaScript">
<!--
var clicked = 'false';
function toggle(){
  if (clicked == 'false'){
    clicked = 'true'
  }
  else{ // any subsequent click disable button & action
    document.forms[0].elements['publishAssessmentForm:publish'].disabled=true;
  }
}
//-->
</script>


<f:verbatim><p></p></f:verbatim>

     <p class="act">
       <!-- Publish, Republishe and Regrade, or Republish button -->
       <h:commandButton id="publish" value="#{assessmentSettingsMessages.button_save_and_publish}" type="submit"
         styleClass="active" action="publishAssessment" onclick="toggle()" onkeypress="toggle()" rendered="#{author.isEditPendingAssessmentFlow}">
          <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.author.PublishAssessmentListener" />
       </h:commandButton>

		<h:commandButton  value="#{authorMessages.button_republish_and_regrade}" type="submit" styleClass="active" rendered="#{!author.isEditPendingAssessmentFlow && author.isRepublishAndRegrade && assessmentBean.hasGradingData}" action="publishAssessment">
			<f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.RepublishAssessmentListener" />
		</h:commandButton>

		<h:commandButton  value="#{authorMessages.button_republish}" type="submit" styleClass="active" rendered="#{!author.isEditPendingAssessmentFlow && !author.isRepublishAndRegrade}" action="publishAssessment">
			<f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.RepublishAssessmentListener" />
		</h:commandButton>

       <h:commandButton value="#{assessmentSettingsMessages.button_cancel}" type="submit" action="editAssessment" rendered="#{author.isEditPendingAssessmentFlow}"/>
	   <h:commandButton value="#{assessmentSettingsMessages.button_cancel}" type="submit" action="editAssessment" rendered="#{!author.isEditPendingAssessmentFlow}">
		  <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.EditAssessmentListener" />
	   </h:commandButton>

</p>

 </h:form>
 <!-- end content -->
</div>

      </body>
    </html>

  </f:view>
