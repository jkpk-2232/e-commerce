<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriverTransactionHistoryReport")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverTransactionHistoryReport"), UrlConstants.JSP_URLS.MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
						
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
								
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelOrderId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentMode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTransactionType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAmount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDateAndTime"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ID, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.IS_EXPORT_ACCESS, it)%>
						
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