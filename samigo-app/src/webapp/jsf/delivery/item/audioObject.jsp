<!--
* $Id: audioObject.jsp 6874 2006-03-22 17:01:47Z hquinn@stanford.edu $
<%--
***********************************************************************************
*
* Copyright (c) 2005, 2006 The Sakai Foundation.
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
***********************************************************************************/
--%>
-->
<h:panelGroup rendered="#{!person.isMacNetscapeBrowser && delivery.actionString != 'reviewAssessment'}">
<f:verbatim>
<object
  classid = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
  codebase = "http://java.sun.com/update/1.5.0/jinstall-1_5-windows-i586.cab#Version=1,5,0,0"
  WIDTH = "500" HEIGHT = "350" NAME = "Test Audio Applet" ALIGN = "middle" VSPACE = "2" HSPACE = "2" >
  <PARAM NAME = CODE VALUE = "org.sakaiproject.tool.assessment.audio.AudioRecorderApplet.class" >
  <PARAM NAME = ARCHIVE VALUE = "sakai-samigo-audio-dev.jar" >
  <PARAM NAME = CODEBASE VALUE = "/samigo/applets/" >
</f:verbatim>

  <%@ include file="/jsf/delivery/item/audioSettings.jsp" %>
<f:verbatim>
  <comment>
   <embed
      type = "application/x-java-applet;version=1.5" \
      CODE = "org.sakaiproject.tool.assessment.audio.AudioRecorderApplet.class" \
      JAVA_CODEBASE = "/samigo/applets/" \
      ARCHIVE = "sakai-samigo-audio-dev.jar"
      NAME = "Record Audio" \
      WIDTH = "500" \
      HEIGHT = "350" \
      ALIGN = "middle" \
      VSPACE = "2" \
      HSPACE = "2" \
      saveAu ="true" \
      saveWave ="false" \
      saveAiff ="false" \
      saveToUrl ="</f:verbatim><h:outputText value="true" rendered="#{delivery.actionString=='takeAssessment' || delivery.actionString=='takeAssessmentViaUrl'}"/><h:outputText value="false" rendered="#{delivery.actionString!='takeAssessment' && delivery.actionString!='takeAssessmentViaUrl'}"/><f:verbatim>" \
      fileName ="audio_#{delivery.assessmentGrading.assessmentGradingId}" \
      url ="</f:verbatim><h:outputText
     value="#{delivery.protocol}/samigo/servlet/UploadAudio?media=jsf/upload_tmp/assessment#{delivery.assessmentId}/question#{question.itemData.itemId}/#{person.eid}/audio_#{delivery.assessmentGrading.assessmentGradingId}" /><f:verbatim>" \
      imageUrl ="</f:verbatim><h:outputText value="#{delivery.protocol}/samigo/images/" /><f:verbatim>" \
      compression ="linear" \
      frequency ="44100" \
      bits ="16" \
      signed ="true" \
      bigendian ="true6" \
      stereo ="false" \
      agentId ="</f:verbatim><h:outputText
         value="#{person.id}" escape="false"/><f:verbatim>" \
      maxSeconds ="</f:verbatim><h:outputText
         value="#{question.duration}" escape="false"/><f:verbatim>" \
      attemptsAllowed ="</f:verbatim><h:outputText
         value="#{question.triesAllowed}" escape="false"/><f:verbatim>" \
      attemptsRemaining ="</f:verbatim><h:outputText
         value="#{question.attemptsRemaining}" escape="false"/><f:verbatim>" \
      aassessmentGrading ="</f:verbatim><h:outputText
         value="#{delivery.assessmentGrading.assessmentGradingId}" escape="false"/><f:verbatim>" \
      scriptable = false
      pluginspage = "http://java.sun.com/products/plugin/index.html#download">
      <noembed>

      </noembed>
   </embed>
  </comment>
</f:verbatim>

<f:verbatim>
</object>
</f:verbatim>
</h:panelGroup>