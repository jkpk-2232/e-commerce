<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriverAccountLogs")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverAccountLogs"), UrlConstants.JSP_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputStaticField("dName", BusinessAction.messageForKeyAdmin("labelName"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
				
						<%=NewThemeUiUtils.outputStaticField("dEmail", BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField("dPhoneNumber", BusinessAction.messageForKeyAdmin("labelPhoneNumber"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField("dCurrentBalence", BusinessAction.messageForKeyAdmin("labelCurrentBalance"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField("dHoldBalence", BusinessAction.messageForKeyAdmin("labelHoldBalance"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField("dApprovedBalence", BusinessAction.messageForKeyAdmin("labelApprovedBalance"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRemark"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCredit"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDebit"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCurrentBalance"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelHoldBalance"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelApprovedBalance"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField("userIdHidden", it)%>
						<%=NewThemeUiUtils.outputFormHiddenField("urlCallFrom", it)%>
						
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