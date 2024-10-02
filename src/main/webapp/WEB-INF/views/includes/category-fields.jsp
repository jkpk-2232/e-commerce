<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalCategoryFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_ID, BusinessAction.messageForKeyAdmin("labelServiceName"), true, 1, itLocalCategoryFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelCategoryName"), true, 1, 30, itLocalCategoryFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.CATEGORY_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelCategoryDescription"), true, 1, 30, itLocalCategoryFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CATEGORY_ID, itLocalCategoryFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	