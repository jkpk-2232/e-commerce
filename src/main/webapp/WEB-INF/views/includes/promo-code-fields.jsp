<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>

<%
	HashMap itLocalPromoCodeFields = (HashMap) request.getAttribute("it");
%>

<% 
	Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
	String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);

	if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_TYPE_ID, BusinessAction.messageForKeyAdmin("labelServiceType"), true, 1, itLocalPromoCodeFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, itLocalPromoCodeFields, "col-sm-3", "col-sm-5")%>

<% 
	}
%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.START_DATE, itLocalPromoCodeFields) %>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.END_DATE, itLocalPromoCodeFields) %>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PROMO_CODE, BusinessAction.messageForKeyAdmin("labelPromoCode"), true, 1, 30, itLocalPromoCodeFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MODE, BusinessAction.messageForKeyAdmin("labelMode"), true, 1, itLocalPromoCodeFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.DISCOUNT, BusinessAction.messageForKeyAdmin("labelDiscount"), true, 1, 30, itLocalPromoCodeFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.MAX_DISCOUNT, BusinessAction.messageForKeyAdmin("labelMaxDiscount"), true, 1, 30, itLocalPromoCodeFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.VALIDITY, BusinessAction.messageForKeyAdmin("labelValidity"), true, 1, 30, itLocalPromoCodeFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	