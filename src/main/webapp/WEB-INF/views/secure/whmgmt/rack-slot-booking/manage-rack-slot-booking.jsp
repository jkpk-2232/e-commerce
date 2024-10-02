<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelSlotStatus")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelSlotStatus"), UrlConstants.JSP_URLS.MANAGE_SLOT_STATUS_DASHBOARD_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<% 
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
						
								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
							
								filterOptionIds.put(FieldConstants.VENDOR_ID, false);
								filterOptionIds.put(FieldConstants.VENDOR_STORE_ID, false);
						%>
						
								<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
								
								<%=NewThemeUiUtils.outputLinSepartor()%>
						<% 
							} else {
						%>
						
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
						<%
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
						
							filterOptionIds.put(FieldConstants.VENDOR_STORE_ID, false);
						%>
						
							<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
								
							<%=NewThemeUiUtils.outputLinSepartor()%>
						<%
							}
						%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRackNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSlotNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPhoneNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStartDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEndDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
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