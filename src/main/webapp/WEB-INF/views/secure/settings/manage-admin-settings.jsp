<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAdminSettings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAdminSettings"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-admin-settings" name="edit-admin-settings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL%>">
									
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_AUTO_ASSIGN, BusinessAction.messageForKeyAdmin("labelAutoAssignDriverServiceType"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_CAR_SERVICE_AUTO_ASSIGN, BusinessAction.messageForKeyAdmin("labelAutoAssignCarServiceType"), true, 1, it, "col-sm-5", "col-sm-5")%>	
									
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS, BusinessAction.messageForKeyAdmin("labelRadius"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS_DELIVERY_DRIVER, BusinessAction.messageForKeyAdmin("labelRadiusDeliveryDriver"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS_SELF_DELIVERY, BusinessAction.messageForKeyAdmin("labelRadiusSelfDelivery"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS_STORE_VISIBILITY, BusinessAction.messageForKeyAdmin("labelRadiusStoreVisibility"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DRIVER_PROCESSING_VIA, BusinessAction.messageForKeyAdmin("labelDriverProcessingVia"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS, BusinessAction.messageForKeyAdmin("labelCronJobTripExpiryTime"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CURRENCY_SYMBOL, BusinessAction.messageForKeyAdmin("labelCurrency"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DISTANCE_TYPE, BusinessAction.messageForKeyAdmin("labelDistanceUnit"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelCountryCode"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.NO_OF_CARS, BusinessAction.messageForKeyAdmin("labelNumberOfCars"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DRIVER_IDEAL_TIME, BusinessAction.messageForKeyAdmin("labelDriverIdleTime"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_RESTRICTED_DRIVER_VENDOR_SUBSCRIPTION_EXPIRY, BusinessAction.messageForKeyAdmin("labelRestrictDriverVendorSubscriptionExpiry"), true, 1, it, "col-sm-5", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.CHARGE, BusinessAction.messageForKeyAdmin("labelCancellationCharges"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.SENDER_BENEFIT, BusinessAction.messageForKeyAdmin("labelSenderBenefitPassenger"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.RECEIVER_BENEFIT, BusinessAction.messageForKeyAdmin("labelReceiverBenefitPassenger"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVER_REFERRAL_BENEFIT, BusinessAction.messageForKeyAdmin("labelSenderBenefitDriver"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.WAITING_TIME, BusinessAction.messageForKeyAdmin("labelWaitingTime"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.CANCEL_TIME, BusinessAction.messageForKeyAdmin("labelCancelTime"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DEMAND_VENDOR_PERCENTAGE, BusinessAction.messageForKeyAdmin("labelDemandVendorPercentage"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE, BusinessAction.messageForKeyAdmin("labelSupplierVendorPercentage"), true, 1, 30, it, "number", "col-sm-5", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-5")%>
							
						</form>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>