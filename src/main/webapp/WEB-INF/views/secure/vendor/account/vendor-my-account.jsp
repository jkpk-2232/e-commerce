<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelMyAccount")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelMyAccount"), UrlConstants.JSP_URLS.MANAGE_VENDOR_MY_ACCOUNT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnEncash", BusinessAction.messageForKeyAdmin("labelEncash"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFullName"), false, 1, 30, it, "text", "col-sm-4", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, it, "text", "col-sm-4", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.PHONE_NUMBER, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), false, 1, 30, it, "text", "col-sm-4", "col-sm-5")%>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.CURRENT_BALANCE, BusinessAction.messageForKeyAdmin("labelCurrentBalance"), false, 1, 30, it, "text", "col-sm-4", "col-sm-5")%>
						
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
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.USER_ID, it)%>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
			<%@include file="/WEB-INF/views/includes/encash-my-request-modal.jsp"%>
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>