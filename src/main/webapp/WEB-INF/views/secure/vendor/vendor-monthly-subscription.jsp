
<%-- 										<%=NewUiViewUtils.outputStaticFields("emailAddress", "Vendor Email", it, "","email-data-div", "col-md-3", "col-md-9")%> --%>
<%-- 										<%=NewUiViewUtils.outputStaticFields("phoneNumber", "Vendor Phone Number", it, "","phoneNumber-data-div", "col-md-3", "col-md-9")%> --%>
<%-- 									<%=NewUiViewUtils.outputGlobalInputField("vendorMonthlySubscriptionFee", "Monthly Subscription Amount", true, 1, 30, it, "text") %> --%>
<%-- 									<%=NewUiViewUtils.outputGlobalInputField("vendorCurrentSubscriptionStartDateTime", "Start Date", true, 1, 30, it, "text") %> --%>
<%-- 									<%=NewUiViewUtils.outputGlobalInputField("vendorCurrentSubscriptionEndDateTime", "End Date", true, 1, 30, it, "text") %> --%>
<%-- 									<%=NewUiViewUtils.outputGlobalInputField("paymentType", "Payment Type", true, 1, 30, it, "text") %> --%>
<%-- 									<%=NewUiViewUtils.outputGlobalInputField("transactionId", "Transaction Id", true, 1, 30, it, "text") %> --%>
<%-- 									<%=ViewUtils.outputFormHiddenField("vendorId", it) %> --%>

<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscription")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscription"), UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="vendor-monthly-subscription" name="vendor-monthly-subscription" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.VENDOR_MONTHLY_SUBSCRIPTION_URL%>">
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_NAME, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.PHONE_NO, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, BusinessAction.messageForKeyAdmin("labelMonthlySubscriptionAmount"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_START_DATE_TIME, BusinessAction.messageForKeyAdmin("labelStartDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_END_DATE_TIME, BusinessAction.messageForKeyAdmin("labelEndDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.PAYMENT_TYPE, BusinessAction.messageForKeyAdmin("labelPaymentType"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.TRANSACTION_ID, BusinessAction.messageForKeyAdmin("labelTransactionId"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
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