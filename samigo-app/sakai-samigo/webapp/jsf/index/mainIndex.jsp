<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>

<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     
   <f:view>      
		<h:panelGroup rendered="#{authorization.adminPrivilege}">
			<%@ include file="../author/authorIndex_noHeader.jsp"%>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{!authorization.adminPrivilege}">
			<%@ include file="../select/selectIndex_noHeader.jsp"%>
		</h:panelGroup>
  </f:view>
