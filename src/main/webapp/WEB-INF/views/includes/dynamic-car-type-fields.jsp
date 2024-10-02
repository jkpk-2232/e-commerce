<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalDynamicCarTypeFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.CAR_MODEL_TYPE_ID, BusinessAction.messageForKeyAdmin("labelCarTypeName"), true, 1, 30, itLocalDynamicCarTypeFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CAR_ICON, BusinessAction.messageForKeyAdmin("labelCarIcons"), true, 1, itLocalDynamicCarTypeFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_TYPE_ICON_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelCarTypeIconImage"), itLocalDynamicCarTypeFields, "col-sm-3", "col-sm-10")%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN, itLocalDynamicCarTypeFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN_DUMMY, itLocalDynamicCarTypeFields)%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_TYPE_ID, itLocalDynamicCarTypeFields)%>