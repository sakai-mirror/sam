<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
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
* Copyright (c) 2005 The Regents of the University of Michigan, Trustees of Indiana University,
*                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
*
* Licensed under the Educational Community License Version 1.0 (the "License");
* By obtaining, using and/or copying this Original Work, you agree that you have read,
* understand, and will comply with the terms and conditions of the Educational Community License.
* You may obtain a copy of the License at:
*
*      http://cvs.sakaiproject.org/licenses/license_1_0.html
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
**********************************************************************************/
--%>
-->
  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{assessmentSettingsMessages.sakai_assessment_manager} #{assessmentSettingsMessages.dash} #{assessmentSettingsMessages.settings}" /></title>
      <samigo:script path="/jsf/widget/colorpicker/colorpicker.js"/>
      <samigo:script path="/jsf/widget/datepicker/datepicker.js"/>
      <samigo:script path="/jsf/widget/hideDivision/hideDivision.js"/>
      </head>
    <body onload="<%= request.getAttribute("html.body.onload") %>">

<script language="javascript" style="text/JavaScript">

// By convention we start all feedback JSF ids with "feedback".
var feedbackIdFlag = "assessmentSettingsAction:feedback";
var noFeedback = "3";

// If we select "No Feedback will be displayed to the student"
// it will disable and uncheck feedback as well as blank out text, otherwise,
// if a different radio button is selected, we reenable feedback checkboxes & text.
function disableAllFeedbackCheck(feedbackType)
{
  var feedbacks = document.getElementsByTagName('INPUT');
  for (i=0; i<feedbacks.length; i++)
  {
	  
    if (feedbacks[i].name.indexOf(feedbackIdFlag)==0)
	  {
      if (feedbackType == noFeedback)
      {
        if (feedbacks[i].type == 'checkbox')
		{
          feedbacks[i].checked = false;
          feedbacks[i].disabled = true;
        }
        else if (feedbacks[i].type == 'text')
        {
          feedbacks[i].value = "";
          feedbacks[i].disabled = true;
        }
      }
      else
      {
        feedbacks[i].disabled = false;
      }
    }
  }
  document.forms[0].submit();
}
</script>

<div class="portletBody">
<!-- content... -->
<h:form id="assessmentSettingsAction">
  <h:inputHidden id="assessmentId" value="#{publishedSettings.assessmentId}"/>

  <!-- HEADINGS -->
  <%@ include file="/jsf/author/allHeadings.jsp" %>

<p>
  <h:messages styleClass="validation"/>
</p>

    <h3>
     <h:outputText id="x1" value="#{assessmentSettingsMessages.settings} #{assessmentSettingsMessages.dash} #{publishedSettings.title}" />
    </h3>

<div class="tier1">
  <!-- *** GENERAL TEMPLATE INFORMATION *** -->

<h:outputLink value="#" title="#{templateMessages.t_showDivs}" onclick="showDivs();" onkeypress="showDivs();">
<h:outputText value="#{templateMessages.open}"/>
</h:outputLink>
<h:outputText value=" | " />
<h:outputLink value="#" title="#{templateMessages.t_hideDivs}" onclick="hideDivs();" onkeypress="hideDivs();">
<h:outputText value="#{templateMessages.close}"/>
</h:outputLink>
<h:outputText value="#{templateMessages.allMenus}"/>

  <samigo:hideDivision id="div1" title="#{assessmentSettingsMessages.t_assessmentIntroduction}" >
<div class="tier2">
    <h:panelGrid columns="2" columnClasses="shorttext"
      summary="#{templateMessages.enter_template_info_section}">
        <h:outputLabel value="#{assessmentSettingsMessages.assessment_title}"/>
        <h:inputText size="80" value="#{publishedSettings.title}"  disabled="true" />

        <h:outputLabel value="#{assessmentSettingsMessages.assessment_creator}"/>

        <h:outputText value="#{publishedSettings.creator}"/>

        <h:outputLabel value="#{assessmentSettingsMessages.assessment_authors}"/>

       <%-- this disabled is so weird - daisyf --%>
        <h:inputText size="80" value="#{publishedSettings.authors}" disabled="true"/>

        <h:outputLabel value="#{assessmentSettingsMessages.assessment_description}"/>

          <h:outputText value="#{publishedSettings.description}<br /><br /><br />"
            escape="false"/>

    </h:panelGrid>
<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>


  <!-- *** DELIVERY DATES *** -->
  <samigo:hideDivision id="div2" title="#{assessmentSettingsMessages.t_deliveryDates}" >
    <div class="tier2">
    <h:panelGrid columns="2" columnClasses="shorttext"
      summary="#{templateMessages.delivery_dates_sec}">

      <h:outputText value="#{assessmentSettingsMessages.assessment_available_date}" />
      <samigo:datePicker value="#{publishedSettings.startDateString}" size="25" id="startDate" />

      <h:outputText value="#{assessmentSettingsMessages.assessment_due_date}" />
      <samigo:datePicker value="#{publishedSettings.dueDateString}" size="25" id="endDate" />

      <h:outputText value="#{assessmentSettingsMessages.assessment_retract_date}" />
      <samigo:datePicker value="#{publishedSettings.retractDateString}" size="25" id="retractDate" />

      <h:commandButton accesskey="#{assessmentSettingsMessages.a_retract}" type="submit" value="#{assessmentSettingsMessages.button_retract_now}" action="confirmAssessmentRetract"  styleClass="active" rendered="#{publishedSettings.active == true}"/>


    </h:panelGrid>
    </div>

  </samigo:hideDivision>

  <!-- *** RELEASED TO *** -->

  <samigo:hideDivision title="#{assessmentSettingsMessages.t_releasedTo}" id="div3">
<div class="tier2">
    <h:panelGrid   summary="#{templateMessages.released_to_info_sec}">
<%--
      <h:selectManyCheckbox disabled="true" layout="pagedirection" value="#{publishedSettings.targetSelected}">
        <f:selectItems value="#{assessmentSettings.publishingTargets}" />
      </h:selectManyCheckbox>
--%>
      <h:selectOneRadio disabled="true" layout="pagedirection" value="#{publishedSettings.firstTargetSelected}">
        <f:selectItems value="#{assessmentSettings.publishingTargets}" />
      </h:selectOneRadio>
      <h:panelGroup styleClass="longtext">
    <h:outputLabel value="#{assessmentSettingsMessages.published_assessment_url}: " />
        <h:outputText value="#{publishedSettings.publishedUrl}" />
      </h:panelGroup>
    </h:panelGrid>
 <f:verbatim>XXXXXXX - publishedSettings.jsp</f:verbatim>
    
p</div>
<%-- dublicate information

    <h:panelGrid columns="2">
      <h:outputText value="#{assessmentSettingsMessages.published_assessment_url}: " />
      <h:outputLink value="#{publishedSettings.publishedUrl}" target="newWindow">
        <h:outputText value="#{publishedSettings.publishedUrl}" />
      </h:outputLink>
    </h:panelGrid>
--%>
  </samigo:hideDivision>

  <!-- *** HIGH SECURITY *** -->
  <samigo:hideDivision title="#{assessmentSettingsMessages.t_highSecurity}" id="div4">
<div class="tier2">
    <h:panelGrid border="0" columns="2" columnClasses="longtext"
        summary="#{templateMessages.high_security_sec}">
      <h:outputText value="#{assessmentSettingsMessages.high_security_allow_only_specified_ip}" />
      <h:inputTextarea value="#{publishedSettings.ipAddresses}" cols="40" rows="5"
        disabled="true"/>
      <h:outputText value="#{assessmentSettingsMessages.high_security_secondary_id_pw}"/>
      <h:panelGrid border="0" columns="2"  >
        <h:outputLabel value="#{assessmentSettingsMessages.high_security_username}"/>
        <h:inputText size="20" value="#{publishedSettings.username}"
          disabled="true"/>

        <h:outputLabel value="#{assessmentSettingsMessages.high_security_password}"/>
        <h:inputText size="20" value="#{publishedSettings.password}"
          disabled="true"/>
      </h:panelGrid>
    </h:panelGrid>
</div>
  </samigo:hideDivision>


  <!-- *** TIMED *** -->
  <samigo:hideDivision id="div5" title="#{assessmentSettingsMessages.t_timedAssessment}">
<div class="tier2">
<%--DEBUGGING:
     Time Limit= <h:outputText value="#{publishedSettings.timeLimit}" /> ;
     Hours= <h:outputText value="#{publishedSettings.timedHours}" /> ;
     Min= <h:outputText value="#{publishedSettings.timedMinutes}" /> ;
     hasQuestions?= <h:outputText value="#{not publishedSettings.hasQuestions}" />
--%>
    <h:panelGrid
        summary="#{templateMessages.timed_assmt_sec}">
      <h:panelGroup>
        <h:selectBooleanCheckbox  disabled="true"
         value="#{publishedSettings.valueMap.hasTimeAssessment}"/>
        <h:outputText value="#{assessmentSettingsMessages.timed_assessment}" />
        <h:selectOneMenu id="timedHours" value="#{publishedSettings.timedHours}"
          disabled="true">
          <f:selectItems value="#{publishedSettings.hours}" />
        </h:selectOneMenu>
        <h:outputText value="#{assessmentSettingsMessages.timed_hours}." />
        <h:selectOneMenu id="timedMinutes" value="#{publishedSettings.timedMinutes}"
           disabled="true">
          <f:selectItems value="#{publishedSettings.mins}" />
        </h:selectOneMenu>
        <h:outputText value="#{assessmentSettingsMessages.timed_minutes}. " />
        <h:outputText value="#{assessmentSettingsMessages.auto_submit_description}" />
      </h:panelGroup>
    </h:panelGrid>
<%-- SAK-3578: auto submit will always be true for timed assessment,
     so no need to have this option
    <h:panelGrid  >
      <h:panelGroup>
       <h:selectBooleanCheckbox  disabled="true"
         value="#{publishedSettings.valueMap.hasAutosubmit}"/>
        <h:outputText value="#{assessmentSettingsMessages.auto_submit}" />
     </h:panelGroup>
    </h:panelGrid>
--%>
</div>
  </samigo:hideDivision>

  <!-- *** ASSESSMENT ORGANIZATION *** -->
  <samigo:hideDivision id="div6" title="#{assessmentSettingsMessages.t_assessmentOrganization}" >
<%--     DEBUGGING:  Layout= <h:outputText value="#{publishedSettings.assessmentFormat}" /> ;
     navigation= <h:outputText value="#{publishedSettings.itemNavigation}" /> ;
     numbering= <h:outputText value="#{publishedSettings.itemNumbering}" />
--%>
    <!-- NAVIGATION -->
   <div class="tier2">
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.navigation}" /></div><div class="tier3">

      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="itemNavigation"  disabled="true"
           value="#{publishedSettings.itemNavigation}"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.linear_access}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.random_access}"/>
        </h:selectOneRadio>
      </h:panelGrid>
   </div>
    <!-- QUESTION LAYOUT -->
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.question_layout}" /></div><div class="tier3">

      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="assessmentFormat"  disabled="true"
            value="#{publishedSettings.assessmentFormat}"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.layout_by_question}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.layout_by_part}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.layout_by_assessment}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    </div>

    <!-- NUMBERING -->
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.numbering}" /></div><div class="tier3">

       <h:panelGrid columns="2"  >
         <h:selectOneRadio id="itemNumbering"  disabled="true"
             value="#{publishedSettings.itemNumbering}"  layout="pageDirection">
           <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.continous_numbering}"/>
           <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.part_numbering}"/>
         </h:selectOneRadio>
      </h:panelGrid>
    </div></div>
  </samigo:hideDivision>

  <!-- *** SUBMISSIONS *** -->
  <samigo:hideDivision id="div7" title="#{assessmentSettingsMessages.t_submissions}" >
<%--     DEBUGGING:
     Unlimited= <h:outputText value="#{publishedSettings.unlimitedSubmissions}" /> ;
     Submissions= <h:outputText value="#{publishedSettings.submissionsAllowed}" /> ;
     lateHandling= <h:outputText value="#{publishedSettings.lateHandling}" />
--%>
<div class="tier2">
    <!-- NUMBER OF SUBMISSIONS -->
     <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.submissions}" /></div> <div class="tier3"><f:verbatim><table><tr><td></f:verbatim>

        <h:selectOneRadio id="unlimitedSubmissions"  disabled="true"
            value="#{publishedSettings.unlimitedSubmissions}" layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.unlimited_submission}"/>
          <f:selectItem itemValue="0" itemLabel="#{assessmentSettingsMessages.only}" />

        </h:selectOneRadio>
             <f:verbatim></td><td valign="bottom"></f:verbatim>
            <h:panelGroup>
              <h:inputText size="5"  disabled="true"
                  value="#{publishedSettings.submissionsAllowed}" />
              <h:outputLabel value="#{assessmentSettingsMessages.limited_submission}" />
            </h:panelGroup>
    <f:verbatim></td></tr></table></f:verbatim>
     </div>
    <!-- LATE HANDLING -->

   <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.late_handling}" /></div><div class="tier3">
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="lateHandling"  disabled="true"
            value="#{publishedSettings.lateHandling}"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.not_accept_latesubmission}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.accept_latesubmission}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    </div>

    <!-- AUTOSAVE -->
<%-- hide for 1.5 release SAM-148
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.auto_save}" /></div>
    <div class="tier3">
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="autoSave"  disabled="true"
            value="#{publishedSettings.submissionsSaved}"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.user_click_save}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.save_automatically}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    </div>
--%>
</div>
  </samigo:hideDivision>

  <!-- *** SUBMISSION MESSAGE *** -->
  <samigo:hideDivision id="div8" title="#{assessmentSettingsMessages.t_submissionMessage}" >
    <div class="tier2"><div class="longtext">
      <h:outputLabel value="#{assessmentSettingsMessages.submission_message}" />
      <br/>
      <h:panelGrid width="630" border="1">
        <h:outputText value="#{publishedSettings.submissionMessage}<br /><br /><br />"
          escape="false"/>
      </h:panelGrid>
<%--
      <h:inputTextarea cols="80" rows="5"  disabled="true"
          value="#{publishedSettings.submissionMessage}" />
--%>

    <br/>
 </div>
  <div class="longtext">
      <h:outputLabel value="#{assessmentSettingsMessages.submission_final_page_url}" /><br/>
      <h:inputText size="80"  disabled="true" value="#{publishedSettings.finalPageUrl}" />
</div></div>

  </samigo:hideDivision>

  <!-- *** FEEDBACK *** -->
  <samigo:hideDivision id="div9" title="#{assessmentSettingsMessages.t_feedback}" >
 
 <!-- FEEDBACK AUTHORING -->
  <div class="tier2">
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.feedback_authoring}" /></div><div class="tier3">
    <h:panelGroup>
      <h:panelGrid columns="1"  >
        <h:selectOneRadio id="feedbackAuthoring"  disabled="true"
             value="#{publishedSettings.feedbackAuthoring}"
           layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.questionlevel_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.sectionlevel_feedback}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.both_feedback}"/>
        </h:selectOneRadio>
      </h:panelGrid>
   </h:panelGroup>
  </div>

    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.feedback_delivery}" /></div><div class="tier3">
    <h:panelGroup>
      <h:panelGrid columns="1" rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable!=true}" >
        <h:selectOneRadio id="feedbackDelivery1"  disabled="true" 
             value="#{publishedSettings.feedbackDelivery}"
           layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.immediate_feedback}"/>
          <f:selectItem itemValue="4" itemLabel="#{assessmentSettingsMessages.feedback_on_submission} #{assessmentSettingsMessages.note_of_feedback_on_submission}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.no_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.feedback_by_date}"/>
        </h:selectOneRadio>

        <h:panelGroup>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
		<h:inputText value="#{publishedSettings.feedbackDateString}" size="25" disabled="true"/>
        </h:panelGroup>
      </h:panelGrid>

      <h:panelGrid columns="1" rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true}" >
  		<h:selectOneRadio id="feedbackDelivery2" rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true}"
             value="#{publishedSettings.feedbackDelivery}"
           layout="pageDirection" onclick="disableAllFeedbackCheck(this.value);">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.immediate_feedback}"/>
		  <f:selectItem itemValue="4" itemLabel="#{assessmentSettingsMessages.feedback_on_submission} #{assessmentSettingsMessages.note_of_feedback_on_submission}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.no_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.feedback_by_date}"/>
        </h:selectOneRadio>

	    <h:panelGrid columns="7" >
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
          <samigo:datePicker value="#{publishedSettings.feedbackDateString}" size="25" id="feedbackDate" >
            <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
          </samigo:datePicker>
        </h:panelGrid>

	    <h:panelGrid columns="7" >
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
          <h:outputText value="#{assessmentSettingsMessages.gradebook_note_f}" />
        </h:panelGrid>
      </h:panelGrid>
    </h:panelGroup>
</div><div class="longtext">
   <h:outputLabel value="#{templateMessages.select_feedback_comp}" /></div><div class="tier3">
    <h:panelGroup rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable!=true}">
      <h:panelGrid columns="2"  >
       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox11"
              value="#{publishedSettings.showStudentResponse}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_response}" />
        </h:panelGroup>
       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox12"
              value="#{publishedSettings.showQuestionLevelFeedback}"/>
          <h:outputText value="#{assessmentSettingsMessages.question_level_feedback}" />
       </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox13"
              value="#{publishedSettings.showCorrectResponse}"/>
          <h:outputText value="#{assessmentSettingsMessages.correct_response}" />
        </h:panelGroup>

       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox14"
             value="#{publishedSettings.showSelectionLevelFeedback}"/>
          <h:outputText value="#{assessmentSettingsMessages.selection_level_feedback}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox15"
              value="#{publishedSettings.showStudentScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_assessment_score}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox16"
              value="#{publishedSettings.showGraderComments}"/>
          <h:outputText value="#{assessmentSettingsMessages.grader_comments}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox17"
              value="#{publishedSettings.showStudentQuestionScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_question_score}" />
        </h:panelGroup>
       
        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox18"
              value="#{publishedSettings.showStatistics}"/>
          <h:outputText value="#{assessmentSettingsMessages.statistics_and_histogram}" />
        </h:panelGroup>
   
      </h:panelGrid>
    </h:panelGroup>
	
	<h:panelGroup rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true}">
      <h:panelGrid columns="2"  >
       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox21"
              value="#{publishedSettings.showStudentResponse}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_response}" />
        </h:panelGroup>
       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox22"
              value="#{publishedSettings.showQuestionLevelFeedback}"/>
          <h:outputText value="#{assessmentSettingsMessages.question_level_feedback}" />
       </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox23"
              value="#{publishedSettings.showCorrectResponse}"/>
          <h:outputText value="#{assessmentSettingsMessages.correct_response}" />
        </h:panelGroup>

       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox24"
             value="#{publishedSettings.showSelectionLevelFeedback}"/>
          <h:outputText value="#{assessmentSettingsMessages.selection_level_feedback}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox25"
              value="#{publishedSettings.showStudentScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_assessment_score}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox26"
              value="#{publishedSettings.showGraderComments}"/>
          <h:outputText value="#{assessmentSettingsMessages.grader_comments}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox27"
              value="#{publishedSettings.showStudentQuestionScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_question_score}" />
        </h:panelGroup>
       
        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox28"
              value="#{publishedSettings.showStatistics}"/>
          <h:outputText value="#{assessmentSettingsMessages.statistics_and_histogram}" />
        </h:panelGroup>
   
      </h:panelGrid>
    </h:panelGroup>
	</div></div>
  </samigo:hideDivision>

  <!-- *** GRADING *** -->
  <samigo:hideDivision id="div10" title="#{assessmentSettingsMessages.t_grading}" >
<div class="tier2">
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.student_identity}" /></div><div class="tier3">
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="anonymousGrading"  disabled="true"
            value="#{publishedSettings.anonymousGrading}"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.not_anonymous}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.anonymous}"/>
        </h:selectOneRadio>
      </h:panelGrid>
</div>
    <!-- GRADEBOOK OPTIONS -->
    <h:panelGroup rendered="#{publishedSettings.valueMap.toGradebook_isInstructorEditable==true && publishedSettings.gradebookExists==true}">
     <f:verbatim> <div class="longtext"></f:verbatim> <h:outputLabel value="#{assessmentSettingsMessages.gradebook_options}" />
	 <f:verbatim></div><div class="tier3"></f:verbatim>
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="toDefaultGradebook"
            value="#{publishedSettings.toDefaultGradebook}"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.to_no_gradebook}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.to_default_gradebook} #{assessmentSettingsMessages.gradebook_note_g}"/>
        </h:selectOneRadio>
      </h:panelGrid>
	<f:verbatim></div></f:verbatim>
    </h:panelGroup>

    <!-- RECORDED SCORE AND MULTIPLES -->
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.recorded_score}" /></div><div class="tier3">
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="scoringType"  disabled="true"
            value="#{publishedSettings.scoringType}"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.highest_score}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.last_score}"/>
        </h:selectOneRadio>
      </h:panelGrid>
</div></div>
  </samigo:hideDivision>

  <!-- *** COLORS AND GRAPHICS	*** -->
  <samigo:hideDivision id="div11" title="#{assessmentSettingsMessages.t_graphics}" >
<div class="tier2">
    <h:panelGrid columns="2" columnClasses="shorttext" >
      <h:outputLabel value="#{assessmentSettingsMessages.background_color}" />
      <h:inputText size="80" value="#{publishedSettings.bgColor}"
          disabled="true" />

      <h:outputLabel value="#{assessmentSettingsMessages.background_image}"/>
      <h:inputText size="80" value="#{publishedSettings.bgImage}"
         disabled="true" />
    </h:panelGrid>
</div>
  </samigo:hideDivision>

  <!-- *** META *** -->

  <samigo:hideDivision title="#{assessmentSettingsMessages.t_metadata}" id="div13">
   <div class="tier2"><div class="longtext"> <h:outputLabel value="#{assessmentSettingsMessages.assessment_metadata}" /> </div><div class="tier3">
    <h:panelGrid columns="2" columnClasses="shorttext">
      <h:outputLabel value="#{assessmentSettingsMessages.metadata_keywords}"/>
      <h:inputText size="80" value="#{publishedSettings.keywords}"  disabled="true"/>

    <h:outputLabel value="#{assessmentSettingsMessages.metadata_objectives}"/>
      <h:inputText size="80" value="#{publishedSettings.objectives}"  disabled="true"/>

      <h:outputLabel value="#{assessmentSettingsMessages.metadata_rubrics}"/>
      <h:inputText size="80" value="#{publishedSettings.rubrics}"  disabled="true"/>
    </h:panelGrid></div>
    <div class="longtext"> <h:outputLabel value="#{assessmentSettingsMessages.record_metadata}" /></div><div class="tier3">
    <h:panelGrid columns="2"  >
<%-- see bug# SAM-117 -- no longer required in Samigo
     <h:selectBooleanCheckbox  disabled="true"
       value="#{publishedSettings.valueMap.hasMetaDataForPart}"/>
     <h:outputText value="#{assessmentSettingsMessages.metadata_parts}"/>
--%>
     <h:selectBooleanCheckbox disabled="true"
       value="#{publishedSettings.valueMap.hasMetaDataForQuestions}"/>
 <h:outputText value="#{assessmentSettingsMessages.metadata_questions}" />
    </h:panelGrid>
</div></div>
  </samigo:hideDivision>

</div>

<p class="act">
  <h:commandButton accesskey="#{assessmentSettingsMessages.a_saveSettings}" type="submit" value="#{assessmentSettingsMessages.button_save_settings}" action="#{publishedSettings.getOutcome}"  styleClass="active">
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.SavePublishedSettingsListener" />
  </h:commandButton>
  <h:commandButton accesskey="#{assessmentSettingsMessages.a_cancel}" value="#{assessmentSettingsMessages.button_cancel}" type="submit" action="author"  />
</p>
</h:form>
<!-- end content -->
</div>
        <script language="javascript" style="text/JavaScript">hideUnhideAllDivsExceptOne('none');</script>
      </body>
    </html>
  </f:view>
