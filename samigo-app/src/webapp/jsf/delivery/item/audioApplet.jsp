<!--
* $Id$
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
<h:panelGroup rendered="#{person.isMacNetscapeBrowser && delivery.actionString != 'reviewAssessment'}">
<f:verbatim>
<applet
  codebase = "/samigo/applets/"
  code = "org.sakaiproject.tool.assessment.audio.AudioRecorderApplet.class"
  archive = "sakai-samigo-audio-1.3-dev.jar"
  WIDTH = "500" HEIGHT = "350" ALIGN = "middle" VSPACE = "2" HSPACE = "2" >
</f:verbatim>

  <%@ include file="/jsf/delivery/item/audioSettings.jsp" %>

<f:verbatim>
</applet>
</f:verbatim>
</h:panelGroup>