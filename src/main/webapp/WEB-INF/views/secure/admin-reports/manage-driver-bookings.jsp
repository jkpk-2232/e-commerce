<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriverBookings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverBookings"), UrlConstants.JSP_URLS.MANAGE_REPORTS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputStaticField(BusinessAction.messageForKeyAdmin("labelSettlementAmount"), FieldConstants.SETTLEMENT_AMOUNT, "col-sm-2", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTourId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPassengerName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelFare"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPromoDiscount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTaxAmount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCredits"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAmountCollected"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPercentage"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTripIncome"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMarkupIncome"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentMode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAdminSettlement"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAction"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ID, it)%>
						
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