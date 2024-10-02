<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="java.util.Map"%>
<%@page import="com.webapp.actions.BusinessAction"%>


<%
	HashMap itLocalFeedFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.FEED_NAME, BusinessAction.messageForKeyAdmin("labelFeedName"), true, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalFeedFields, "col-sm-3", "col-sm-5")%>
<%-- <%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalFeedFields, "col-sm-3", "col-sm-5")%> --%>
<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.SERVICE_ID, BusinessAction.messageForKeyAdmin("labelService"), true, 1, itLocalFeedFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendor"), true, 1, itLocalFeedFields, "col-sm-3", "col-sm-5") %>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), true, 1, itLocalFeedFields, "col-sm-3", "col-sm-5")%>

<div id = "countOuterDiv">
	<%=NewThemeUiUtils.outputStaticField(FieldConstants.STORES_COUNT, BusinessAction.messageForKeyAdmin("labelStoresCount"), false, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5") %>
	<%=NewThemeUiUtils.outputStaticField(FieldConstants.LED_COUNT, BusinessAction.messageForKeyAdmin("labelLedCount"), false, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5") %>
</div>
<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.FEED_MESSAGE,
		BusinessAction.messageForKeyAdmin("labelFeedMessage"), true, 1, 30, itLocalFeedFields, "text", "col-sm-3",
		"col-sm-5", 5)%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.START_DATE, BusinessAction.messageForKeyAdmin("labelStartDate"), true, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5")%>
							
<%=NewThemeUiUtils.outputInputField(FieldConstants.END_DATE, BusinessAction.messageForKeyAdmin("labelEndDate"), true, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputStaticField(FieldConstants.ESTIMATED_COST, BusinessAction.messageForKeyAdmin("labelEstimatedCost"), false, 1, 30, itLocalFeedFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.FEED_BANNER, BusinessAction.messageForKeyAdmin("labelFeedBaner"), itLocalFeedFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.FEED_BANNER_HIDDEN_IMAGE, itLocalFeedFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, itLocalFeedFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>