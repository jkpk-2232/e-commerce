<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscriptionHistory")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscriptionHistory"), UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId)) {
								
								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
								filterOptionIds.put(FieldConstants.VENDOR_ID, false);
						%>
						
								<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-3")%>
								
								<%=NewThemeUiUtils.outputLinSepartor()%>
								
						<%
							}
						%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelHistoryId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTransactionType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMonthlyFee"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStartDateTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEndDateTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSubscriptionType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSubscriptionStatus"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
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