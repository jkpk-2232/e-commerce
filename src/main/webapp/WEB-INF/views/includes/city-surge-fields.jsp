<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%
	HashMap itLocalCitySurgeFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CITY_SURGE_ID, itLocalCitySurgeFields)%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalCitySurgeFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS, BusinessAction.messageForKeyAdmin("labelRadius"), true, 1, itLocalCitySurgeFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SURGE_FILTER, BusinessAction.messageForKeyAdmin("labelSurgeRate"), true, 1, itLocalCitySurgeFields, "col-sm-3", "col-sm-5")%>	

<% 
	if (itLocalCitySurgeFields.containsKey("radiusError")) {
%>
	<%=NewThemeUiUtils.outputErrorDiv(itLocalCitySurgeFields.get("radiusError").toString())%>
<%
	}
%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	

<%=NewThemeUiUtils.outputLinSepartor()%>

<%=NewThemeUiUtils.outputStaticField(FieldConstants.CITY_NAME, BusinessAction.messageForKeyAdmin("labelAvailableRadiusSurgeForRegion"), false, 1, 30, itLocalCitySurgeFields, "text", "col-sm-4", "col-sm-5")%>
						
<%=NewThemeUiUtils.outputLinSepartor()%>
						
<% 
	List<String> columnNames = new ArrayList<>();
	columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
	columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
	columnNames.add(BusinessAction.messageForKeyAdmin("labelCity"));
	columnNames.add(BusinessAction.messageForKeyAdmin("labelRadius"));
	columnNames.add(BusinessAction.messageForKeyAdmin("labelSurge"));
%>

<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>