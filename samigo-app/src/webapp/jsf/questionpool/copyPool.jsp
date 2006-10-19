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
  <f:view>
    <f:loadBundle
       basename="org.sakaiproject.tool.assessment.bundle.QuestionPoolMessages"
       var="msg"/>
  
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{msg.copy_p}"/></title>
                        <!-- stylesheet and script widgets -->
<script language="javascript" type="text/JavaScript">
<!--
<%@ include file="/js/samigotree.js" %>
//-->
</script>
      </head>
<body onload="collapseAllRowsForSelectList();flagRows();;<%= request.getAttribute("html.body.onload") %>">
<!-- content... -->
 <div class="portletBody">
<h:form id="copyPool">
<h:messages styleClass="validation"/>

<h3>
<h:outputText rendered="#{questionpool.actionType == 'pool'}" value="#{msg.copy_p}"/>
<h:outputText rendered="#{questionpool.actionType == 'item'}" value="#{msg.copy_q}"/>
</h3>


<div class="tier1">
<h:outputText value="#{msg.sel_dest_copy} "/>
<h:outputText rendered="#{questionpool.actionType == 'pool'}" value="#{questionpool.currentPool.displayName}"/>
<h:outputText  rendered="#{questionpool.actionType == 'item'}" value="#{questionpool.currentItem.text}" escape="false"/>

</div>
<div class="longtext tier2">
<%--
<h:outputText styleClass="number" value="1"/>
<h:outputLabel rendered="#{questionpool.actionType == 'pool'}" value="#{msg.copy_p_to}"/>
<h:outputLabel rendered="#{questionpool.actionType == 'item'}" value="#{msg.copy_q_to}"/>

<br/><br/>
--%>

<%@ include file="/jsf/questionpool/copyPoolTree.jsp" %>

<%--
<br/>
<h:outputText styleClass="number" value="2"/>
<h:outputLabel value="#{msg.click_copy}"/>

--%>
</div>

<p class="act">

  <h:commandButton accesskey="#{msg.a_copy}" id="copypoolsubmit" immediate="true" value="#{msg.copy}"
    action="#{questionpool.copyPool}" styleClass="active" rendered="#{questionpool.actionType == 'pool'}">
  </h:commandButton>

  <h:commandButton accesskey="#{msg.a_copy}" id="copyitemsubmit" immediate="true" value="#{msg.copy}"
    action="#{questionpool.copyQuestion}" styleClass="active" rendered="#{questionpool.actionType == 'item'}">
  </h:commandButton>

<h:commandButton accesskey="#{msg.a_cancel}" id="cancel" value="#{msg.cancel}" action="poolList"/>

</p>
</h:form>
</div>
</body>
</html>
</f:view>
