<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelPerformance")%></title>
	</head>
	
	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelPerformance"),  UrlConstants.JSP_URLS.MANAGE_PERFORMANCE_DASHBOARD_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
				
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField(" ", BusinessAction.messageForKeyAdmin("labelAddPerformance"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
					
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						<div class="row md-5">
						<div class="col-md-4">
							<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.STORES_VISITED,BusinessAction.messageForKeyAdmin("labelStoreVisited"), it, "",UrlConstants.JSP_URLS.MANAGE_PASSENGER_ICON)%>
							
						</div>
						<div class="col-md-4">
							 <%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.STORES_ACTIVE, BusinessAction.messageForKeyAdmin("labelStoresActive"),it, "", UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
						
						</div>
						<div><%=NewThemeUiUtils.outputLinSepartor()%></div>
						
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStoreOwner"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStoreName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStorelocation"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmailId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPhoneNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmployeelocation"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAction"));
							
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