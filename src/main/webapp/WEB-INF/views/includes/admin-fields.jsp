<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalAdminFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalAdminFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputInputField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFirstName"), true, 1, 30, itLocalAdminFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), true, 1, 30, itLocalAdminFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, itLocalAdminFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, itLocalAdminFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), true, 1, 30, itLocalAdminFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, itLocalAdminFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.MAXIMUM_MARKUP_FARE, BusinessAction.messageForKeyAdmin("labelMaximumMarkupFare"), true, 1, 30, itLocalAdminFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.EXPORTACCESSLIST, BusinessAction.messageForKeyAdmin("labelExportAccess"), true, 1, itLocalAdminFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.ACCESS_LIST, BusinessAction.messageForKeyAdmin("labelCapabilities"), true, 1, itLocalAdminFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.USER_ID, itLocalAdminFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	