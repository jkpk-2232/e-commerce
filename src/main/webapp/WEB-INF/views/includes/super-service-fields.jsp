<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalSuperServicesFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_TYPE_ID, BusinessAction.messageForKeyAdmin("labelServiceTypeName"), true, 1, itLocalSuperServicesFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelServiceName"), true, 1, 30, itLocalSuperServicesFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.SERVICE_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelServiceDescription"), true, 1, 30, itLocalSuperServicesFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.SERVICE_PRIORITY, BusinessAction.messageForKeyAdmin("labelServicePriority"), true, 1, 30, itLocalSuperServicesFields, "number", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_ID, itLocalSuperServicesFields)%>

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.SERVICE_IMAGE, BusinessAction.messageForKeyAdmin("labelServiceImage"), itLocalSuperServicesFields, "col-sm-3", "col-sm-10")%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_IMAGE_HIDDEN, itLocalSuperServicesFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_IMAGE_HIDDEN_DUMMY, itLocalSuperServicesFields)%>

<%=NewThemeUiUtils.outputFormHiddenField("cdnUrl", itLocalSuperServicesFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	