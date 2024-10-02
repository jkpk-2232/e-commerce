<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditOrderSettings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditOrderSettings"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-order-settings" name="edit-order-settings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_ORDER_SETTINGS_URL%>">
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelServiceName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.MAX_NUMBER_OF_ITEMS, BusinessAction.messageForKeyAdmin("labelMaxNumberOfItems"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.MAX_WEIGHT_ALLOWED, BusinessAction.messageForKeyAdmin("labelMaxWeightAllowed"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.FREE_CANCELLATION_TIME_MINS, BusinessAction.messageForKeyAdmin("labelFreeCancellationTimeMins"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS, BusinessAction.messageForKeyAdmin("labelOrderJobExpirationTimeHours"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS, BusinessAction.messageForKeyAdmin("labelNewOrderExpirationTimeHours"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DELIVERY_BASE_FEE, BusinessAction.messageForKeyAdmin("labelDeliveryBaseFee"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DELIVERY_BASE_KM, BusinessAction.messageForKeyAdmin("labelDeliveryBaseKm"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DELIVERY_FEE_PER_KM, BusinessAction.messageForKeyAdmin("labelDeliveryFeePerKm"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_ID, it)%>
							
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