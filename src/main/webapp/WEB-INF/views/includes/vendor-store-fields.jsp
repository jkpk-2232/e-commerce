<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalVendorStoreFields = (HashMap) request.getAttribute("it");
%>

<ul class="nav nav-tabs nav-tabs-v2">
	
	<li class="nav-item me-3">
		<a href="#tab1" class="nav-link active" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelStoreLocation")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab2" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelStoreTimings")%>
		</a>
	</li>
	
	<li class="nav-item me-3" id="showWarehouseFields">
		<a href="#tab3" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelWarehouseRacks")%>
		</a>
	</li>
	
</ul>
<div class="tab-content pt-3">
	
	<div class="tab-pane fade show active" id="tab1">
	
		<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
		
		<%=NewThemeUiUtils.outputLinSepartor()%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MULTICITY_REGION_ID, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.STORE_NAME, BusinessAction.messageForKeyAdmin("labelStoreName"), true, 1, 30, itLocalVendorStoreFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.TYPE, BusinessAction.messageForKeyAdmin("labelType"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.STORE_ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, itLocalVendorStoreFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.LED_DEVICE_FOR_STORE, BusinessAction.messageForKeyAdmin("labelLedDeviceForStore"), false, 1,  itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.LED_DEVICE_COUNT, BusinessAction.messageForKeyAdmin("labelLedDeviceCount"), true, 1, 30, itLocalVendorStoreFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.PHONEPE_STORE_ID, BusinessAction.messageForKeyAdmin("labelPhonepeStoreId"), false, 1, 30, itLocalVendorStoreFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.STORE_IMAGE, BusinessAction.messageForKeyAdmin("labelStoreImage"), itLocalVendorStoreFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_IMAGE_HIDDEN, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, itLocalVendorStoreFields)%>
		
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_STORE_ID, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LAT, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LNG, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_ADDRESS_LAT, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_ADDRESS_LNG, itLocalVendorStoreFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_PLACE_ID, itLocalVendorStoreFields)%>

	</div>
	
	<div class="tab-pane fade" id="tab2">
		
		<%=NewThemeUiUtils.outputSelectInputField("dateType", BusinessAction.messageForKeyAdmin("labelDateType"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField("numberOfShifts", BusinessAction.messageForKeyAdmin("labelNumberOfShifts"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField("shiftType", BusinessAction.messageForKeyAdmin("labelShiftType"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField("isClosedToday", BusinessAction.messageForKeyAdmin("labelClosedToday"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
		 		
		<% 
			if (itLocalVendorStoreFields.containsKey("daysOfWeekOrSpecificSpan")) {
		%>
			<%=NewThemeUiUtils.outputErrorDiv(itLocalVendorStoreFields.get("daysOfWeekOrSpecificSpan").toString())%>
		<%
			}
		%>
		 		
 		<div id="specificDaysDiv">
 		
 			<%=NewThemeUiUtils.outputFormHiddenField("fromDate", itLocalVendorStoreFields)%>
 			<%=NewThemeUiUtils.outputFormHiddenField("toDate", itLocalVendorStoreFields)%>
 			<%=NewThemeUiUtils.outputInputField(FieldConstants.REPORT_RANGE, BusinessAction.messageForKeyAdmin("labelDates"), true, 1, 30, itLocalVendorStoreFields, "text", "col-sm-3", "col-sm-5")%>
			
			<%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours", BusinessAction.messageForKeyAdmin("labelOpeningHours"), true, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>

	        <%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours", BusinessAction.messageForKeyAdmin("labelClosingHours"), true, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
			
			<%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
	        
	        <%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
					 		
 		</div>
		
		<div id="daysOfWeekDiv">
			<%@include file="/WEB-INF/views/includes/vendor-store-timing-date-fields.jsp"%>
		</div>
		
		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
	
	</div>
	
	<div class="tab-pane fade" id="tab3">
		<div id="showWarehouseFields">
		
			<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RACK_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelRackCategory"), true, 1, itLocalVendorStoreFields, "col-sm-3", "col-sm-5")%>
			<%=NewThemeUiUtils.outputInputField(FieldConstants.NUMBER_OF_RACKS, BusinessAction.messageForKeyAdmin("labelNumberOfRacks"), true, 1, 30, itLocalVendorStoreFields, "number", "col-sm-3", "col-sm-5")%>
		</div>	
	</div>
	
</div>