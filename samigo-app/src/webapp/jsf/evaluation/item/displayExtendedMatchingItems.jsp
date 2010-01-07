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

  <h:outputText value="#{question.text}"  escape="false"/>
  <h:dataTable value="#{question.itemTextArray}" var="itemText">
   <h:column>
   <h:dataTable value="#{itemText.answerArraySorted}" var="answer">
    <h:column rendered="#{answer.text!=null && answer.text!=''}">
      <h:graphicImage id="image6" rendered="#{answer.isCorrect}"
        alt="#{evaluationMessages.alt_correct}" url="/images/delivery/checkmark.gif" >
       </h:graphicImage>
      <h:graphicImage id="image7" rendered="#{!answer.isCorrect}"
        alt=" " url="/images/delivery/spacer.gif" >
       </h:graphicImage>
    </h:column>
    <h:column rendered="#{answer.text!=null && answer.text!=''}">
      <h:outputText value="#{answer.label}" escape="false"
        rendered="#{question.hint == '***'}" />
    </h:column>
    <h:column rendered="#{answer.text!=null && answer.text!=''}"><%-- checkbox or radio button, select answer --%>
      <h:selectManyCheckbox value="#{question.hint}" disabled="true"
          rendered="#{question.hint != '***'}">
        <f:selectItem itemLabel="#{answer.label}"
          itemValue="#{answer.sequence}"/>
      </h:selectManyCheckbox>
    </h:column>
    <h:column rendered="#{answer.text!=null && answer.text!=''}">
      <h:outputText value="#{answer.text}" escape="false" />
    </h:column>
   </h:dataTable>
   </h:column>
  </h:dataTable>

