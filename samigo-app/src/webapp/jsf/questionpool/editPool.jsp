<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- $Id$
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
    <f:loadBundle
       basename="org.sakaiproject.tool.assessment.bundle.QuestionPoolMessages"
       var="msg"/>
    <f:loadBundle
       basename="org.sakaiproject.tool.assessment.bundle.AuthorMessages"
       var="authmsg"/>
    <f:loadBundle
       basename="org.sakaiproject.tool.assessment.bundle.GeneralMessages"
       var="genMsg"/>

    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{msg.edit_p}"/></title>

<%@ include file="/js/delivery.js" %>

<script language="javascript" type="text/JavaScript">
<!--
<%@ include file="/js/samigotree.js" %>
<%@ include file="/js/authoring.js" %>
//-->
</script>
      </head>

<f:verbatim><body onload="collapseRowsByLevel(</f:verbatim><h:outputText value="#{questionpool.htmlIdLevel}"/><f:verbatim>);flagRows();<%= request.getAttribute("html.body.onload") %>"></f:verbatim>
 <div class="portletBody">
<h:form id="editform">
  <!-- HEADINGS -->
  <%@ include file="/jsf/questionpool/questionpoolHeadings.jsp" %>

<!-- dataLine here is not working -->
<br />
<samigo:dataLine value="#{questionpool.currentPool.parentPoolsArray}" var="parent"
   separator=" > " first="0" rows="100" >
  <h:column>
    <h:commandLink action="#{questionpool.editPool}"  immediate="true">
      <h:outputText value="#{parent.displayName}" />
      <f:param name="qpid" value="#{parent.questionPoolId}"/>
    </h:commandLink>
  </h:column>
</samigo:dataLine>

<h:outputText rendered="#{questionpool.currentPool.showParentPools}" value=" > " />
<h:outputText rendered="#{questionpool.currentPool.showParentPools}" value="#{questionpool.currentPool.displayName}"/>

<h3 class="insColor insBak insBor">
<h:outputText value="#{msg.qp}#{msg.column} #{questionpool.currentPool.displayName}"/>
</h3>
<h:messages styleClass="validation" />
<h:outputText rendered="#{questionpool.importToAuthoring == 'true'}" value="#{msg.msg_imp_editpool}"/>
 <div class="tier2">
<h:panelGrid columns="2" columnClasses="shorttext">
  <h:outputLabel for="namefield" value="#{msg.p_name}"/>
  <h:inputText disabled="#{questionpool.importToAuthoring == 'true'}"  id="namefield" size="30" value="#{questionpool.currentPool.displayName}" />
  <h:outputLabel for="ownerfield" value="#{msg.creator}"/>
  <h:outputText id="ownerfield"  value="#{questionpool.currentPool.owner}"/>

  <h:outputLabel rendered="!#{questionpool.currentPool.showParentPools}"  for="orgfield" value="#{msg.dept}"/>
  <h:inputText disabled="#{questionpool.importToAuthoring == 'true'}" id="orgfield" size="30" value="#{questionpool.currentPool.organizationName}" rendered="!#{questionpool.currentPool.showParentPools}"/>
 
  <h:outputLabel rendered="!#{questionpool.currentPool.showParentPools}" for="descfield" value="#{msg.desc}" />
  <h:inputTextarea disabled="#{questionpool.importToAuthoring == 'true'}"  id="descfield" rendered="!#{questionpool.currentPool.showParentPools}" value="#{questionpool.currentPool.description}" cols="30" rows="5"/>

  <h:outputLabel for="objfield" value="#{msg.obj} " rendered="!#{questionpool.currentPool.showParentPools}"/>
  <h:inputText disabled="#{questionpool.importToAuthoring == 'true'}" id="objfield" size="30" value="#{questionpool.currentPool.objectives}" rendered="!#{questionpool.currentPool.showParentPools}"/>

  <h:outputLabel for="keyfield" value="#{msg.keywords} " rendered="!#{questionpool.currentPool.showParentPools}" />
  <h:inputText disabled="#{questionpool.importToAuthoring == 'true'}" id="keyfield" size="30" value="#{questionpool.currentPool.keywords}" rendered="!#{questionpool.currentPool.showParentPools}" />
</h:panelGrid>
 </div>


<!-- display subpools  -->
<div class="tier1">
<h4>
<h:panelGrid width="100%" columns="2" columnClasses="h3text,navList">
<h:panelGroup >
<h:outputText value="#{questionpool.currentPool.numberOfSubpools}"/>
<h:outputText rendered="#{questionpool.currentPool.numberOfSubpools > 1}" value=" #{msg.subps}"/>
<h:outputText rendered="#{questionpool.currentPool.numberOfSubpools == 1}" value=" #{msg.subp}"/>
<h:outputText rendered="#{questionpool.currentPool.numberOfSubpools == 0}" value=" #{msg.subps}"/>
</h:panelGroup>
<h:commandLink title="#{msg.t_addSubpool}" rendered="#{questionpool.importToAuthoring != 'true'}" id="addlink" immediate="true" action="#{questionpool.addPool}">
  <h:outputText  id="add" value="#{msg.add}"/>
  <f:param name="qpid" value="#{questionpool.currentPool.id}"/>
  <f:param name="addsource" value="editpool"/>
</h:commandLink>
</h:panelGrid>
</h4>
<div class="tier2">
<h:panelGrid rendered="#{questionpool.currentPool.numberOfSubpools > 0 }" width="100%" >
<h:panelGroup>
<%@ include file="/jsf/questionpool/subpoolsTreeTable.jsp" %>

</h:panelGroup>
</h:panelGrid>
</div>
 </div>
<!-- dispaly questions-->


 <div class="tier1">
 <h4>
<h:panelGrid width="100%" columns="2" columnClasses="h3text,navList">
<h:panelGroup >
<h:outputText value="#{questionpool.currentPool.numberOfQuestions}"/>
<h:outputText rendered ="#{questionpool.currentPool.numberOfQuestions >1}"value=" #{msg.qs}"/>
<h:outputText rendered ="#{questionpool.currentPool.numberOfQuestions ==1}"value=" #{msg.q}"/>
<h:outputText rendered ="#{questionpool.currentPool.numberOfQuestions ==0}"value=" #{msg.qs}"/>
</h:panelGroup>
<h:commandLink title="#{msg.t_addQuestion}" rendered="#{questionpool.importToAuthoring != 'true'}" id="addQlink" immediate="true" action="#{questionpool.selectQuestionType}">
  <h:outputText id="addq" value="#{msg.add}"/>
  <f:param name="poolId" value="#{questionpool.currentPool.id}"/>
</h:commandLink>
</h:panelGrid>
</h4>
  <div class="tier2">
<h:panelGrid rendered="#{questionpool.currentPool.numberOfQuestions > 0 }" width="100%">
<h:panelGroup>
<%@ include file="/jsf/questionpool/questionTreeTable.jsp" %>
</h:panelGroup>
</h:panelGrid>
 </div>
</div>
<!-- END -->
<f:verbatim><br/></f:verbatim>
<h:panelGrid rendered="#{(questionpool.importToAuthoring == 'true') && (questionpool.currentPool.numberOfQuestions > 0)}" columnClasses="shorttext">  <h:panelGroup>
  <h:outputLabel value="#{authmsg.assign_to_p}" />
  <h:selectOneMenu id="assignToPart" value="#{questionpool.selectedSection}">
     <f:selectItems  value="#{itemauthor.sectionSelectList}" />
     <!-- use this in real  value="#{section.sectionNumberList}" -->
  </h:selectOneMenu>
  </h:panelGroup>
  </h:panelGrid>

<div class="tier1">
<!-- for normal pool operations -->
  <h:commandButton accesskey="#{msg.a_update}" id="submit"   rendered="#{questionpool.importToAuthoring == 'false'}" action="#{questionpool.getOutcomeEdit}"
        value="#{msg.update}">
  <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.questionpool.PoolSaveListener" />
 
  </h:commandButton>

<!-- for importing questions from pool to authoring -->
<!-- disable copy button once clicked.  show processing... -->

  <h:commandButton accesskey="#{msg.a_copy}" id="import"   rendered="#{(questionpool.importToAuthoring == 'true') && (questionpool.currentPool.numberOfQuestions > 0)}" action="#{questionpool.doit}"
   onclick="disableImport(); showNotif('submitnotif',this.name,'editform');" onkeypress="disableImport(); showNotif('submitnotif',this.name,'editform');"
        value="#{msg.copy}">
  <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.questionpool.ImportQuestionsToAuthoring" />
  </h:commandButton>


 <h:commandButton accesskey="#{msg.a_cancel}" style="act" value="#{msg.cancel}" action="poolList" immediate="true"/>
<h:outputText escape="false" value="<span id=\"submitnotif\" style=\"visibility:hidden\"> Processing.....</span>"/>
 </div>

</h:form>
</div>
</body>
</html>
</f:view>

