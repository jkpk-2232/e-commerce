<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalTaxFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.TAX_NAME, BusinessAction.messageForKeyAdmin("labelTaxName"), true, 1, 30, itLocalTaxFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.TAX_PERCENTAGE, BusinessAction.messageForKeyAdmin("labelTaxPercentage"), true, 1, 30, itLocalTaxFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.TAX_ID, itLocalTaxFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	