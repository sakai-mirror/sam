<%-- $Id: displayExtendedMatchingItems.jsp 70946 2010-01-06 14:47:07Z gopal.ramasammycook@gmail.com $
include file for displaying Extended Matching Items questions
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

******** GOPALRC - TEST/TEMP - displayExtendedMatchingItems.jsp ************


  <h:outputText value="#{question.class.name}"  escape="false"/>
      <f:verbatim><br/><br/></f:verbatim>

  <h:outputText value="#{question.text}"  escape="false"/>
  
  
      <f:verbatim><br/><br/></f:verbatim>


      <h:dataTable value="#{question.emiAnswerComponentsItemText.emiAnswerOptions}" var="option" border="1" style="border-style:solid">
        <h:column> 
          <h:panelGroup rendered="#{option.text != null && option.text ne ''}">
            <h:outputText escape="false" value="#{option.label}. #{option.text}" /> 
          </h:panelGroup>
        </h:column>
      </h:dataTable>
  
  
        
      <f:verbatim><br/><br/></f:verbatim>
      <h:outputText escape="false" value="#{question.leadInText}" />
      <f:verbatim><br/><br/></f:verbatim>
      

      
      <h:dataTable value="#{question.emiAnswerComponentsItemText.emiQuestionAnswerCombinations}" var="option" border="1" style="border-style:solid">
        <h:column> 
          <h:panelGroup rendered="#{option.text != null && option.text ne ''}">
            <h:outputText escape="false" value="#{option.label}. #{option.text}  Correct:#{option.correctOptionLabels}" /> 
          </h:panelGroup>
        </h:column>
      </h:dataTable>
      
  