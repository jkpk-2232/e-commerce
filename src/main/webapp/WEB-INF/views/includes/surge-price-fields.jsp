<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalSurgePriceFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SURGE_PRICE_ID, itLocalSurgePriceFields)%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalSurgePriceFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SURGE_FILTER, BusinessAction.messageForKeyAdmin("labelSurgePrice"), true, 1, itLocalSurgePriceFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputTimePickerInputField(FieldConstants.START_TIME_HOUR_MINS, BusinessAction.messageForKeyAdmin("labelStartTime"), true, itLocalSurgePriceFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputTimePickerInputField(FieldConstants.END_TIME_HOUR_MINS, BusinessAction.messageForKeyAdmin("labelEndTime"), true, itLocalSurgePriceFields, "col-sm-3", "col-sm-5")%>

<% 
	if (itLocalSurgePriceFields.containsKey("errorSurgeTimeSlotConflictInOtherSlot")) {
%>
	<%=NewThemeUiUtils.outputErrorDiv(itLocalSurgePriceFields.get("errorSurgeTimeSlotConflictInOtherSlot").toString())%>
<%
	}
%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	

