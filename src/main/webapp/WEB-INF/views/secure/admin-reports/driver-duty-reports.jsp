<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriverDutyReports")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverDutyReports"), UrlConstants.JSP_URLS.MANAGE_REPORTS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
							filterOptionIds.put(FieldConstants.ON_OFF_STATUS, false);
						%>
						
						<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.DRIVER_NAME, BusinessAction.messageForKeyAdmin("labelDriverName"), false, 1, 30, it, "text", "col-sm-2", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.EMAIL, BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, it, "text", "col-sm-2", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.PHONE_NO, BusinessAction.messageForKeyAdmin("labelPhone"), false, 1, 30, it, "text", "col-sm-2", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.LOGS_DATE_FOR_HEADER, BusinessAction.messageForKeyAdmin("labelDate"), false, 1, 30, it, "text", "col-sm-2", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverDutyTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverDutyStatus"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ID, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LOGS_DATE, it)%>
						
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