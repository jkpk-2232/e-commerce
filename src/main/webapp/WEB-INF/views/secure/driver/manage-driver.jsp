<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriver")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriver"), UrlConstants.JSP_URLS.MANAGE_DRIVER_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.OTHER_PAGE_URL, it)%>
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddDriver", BusinessAction.messageForKeyAdmin("labelAddDriver"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<% 
							
							Map<String, String> sessionAttributesLocal = SessionUtils.getSession(request, response, true);
							String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesLocal, LoginUtils.ROLE_ID);
							
							if (UserRoleUtils.isSuperAdminRole(roleIdLocal)) {
						%>
						
							<%=NewThemeUiUtils.outputButtonSingleField("btnDriverSubscriptionExtension", BusinessAction.messageForKeyAdmin("labelDriverSubscriptionExtension"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<% 
							}
						%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
							filterOptionIds.put(FieldConstants.STATUS, false);
							filterOptionIds.put(FieldConstants.APPROVEL, false);
							
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId)) {
								filterOptionIds.put(FieldConstants.VENDOR_ID, false);
							}
						%>
						
						<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmailAddress"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPassword"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPhoneNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelLicenceNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelApprovelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendor"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAgentNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscriptionStatus"));
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