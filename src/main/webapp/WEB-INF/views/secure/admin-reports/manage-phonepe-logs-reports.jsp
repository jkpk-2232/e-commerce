<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelPhonepeLogReport")%></title>
	</head>
	
	<body>
		
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelPhonepeLogReport"), UrlConstants.JSP_URLS.MANAGE_REPORTS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
				
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
							filterOptionIds.put(FieldConstants.PHONEPE_STATUS, false);
						%>
						
						
						<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentRequestType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelFullName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTrackingId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAmountCollected"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelOrderStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentMode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDateAndTime"));
							
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