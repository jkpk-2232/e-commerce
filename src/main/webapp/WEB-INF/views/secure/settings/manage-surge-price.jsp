<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelSurgePrice")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelSurgePrice"), UrlConstants.JSP_URLS.MANAGE_SURGE_PRICE_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddSurgePrice", BusinessAction.messageForKeyAdmin("labelAddSurgePrice"), NewThemeUiUtils.BUTTON_FLOAT_LEFT, NewThemeUiUtils.BUTTON_FLOAT_LEFT)%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId)) {

								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
								filterOptionIds.put(FieldConstants.SURGE_FILTER, false);
								filterOptionIds.put(FieldConstants.REGION_LIST, false);
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSurge"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStartTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEndTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRegion"));
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