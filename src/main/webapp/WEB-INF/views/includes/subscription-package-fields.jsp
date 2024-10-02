<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalSubscriptionPackageFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SUBSCRIPTION_PACKAGE_ID, itLocalSubscriptionPackageFields)%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PACKAGE_NAME, BusinessAction.messageForKeyAdmin("labelPackageName"), true, 1, 30, itLocalSubscriptionPackageFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DURATION_DAYS, BusinessAction.messageForKeyAdmin("labelDurationInDays"), true, 1, itLocalSubscriptionPackageFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRICE, BusinessAction.messageForKeyAdmin("labelPrice"), true, 1, 30, itLocalSubscriptionPackageFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CAR_TYPE_ID, BusinessAction.messageForKeyAdmin("labelCarType"), true, 1, itLocalSubscriptionPackageFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	