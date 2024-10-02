<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelRentalPackages")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelRentalPackages"), UrlConstants.JSP_URLS.MANAGE_RENTAL_PACKAGE_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddRentalPackage", BusinessAction.messageForKeyAdmin("labelAddRentalPackage"), NewThemeUiUtils.BUTTON_FLOAT_LEFT, NewThemeUiUtils.BUTTON_FLOAT_LEFT)%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
						
								filterOptionIds.put(FieldConstants.REGION_LIST, false);
						
								if (UserRoleUtils.isSuperAdminAndAdminRole(roleId)) {
									filterOptionIds.put(FieldConstants.VENDOR_ID, false);
								}
								
								filterOptionIds.put(FieldConstants.RENTAL_PACKAGE_TYPE, false);
						%>
						
						<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPackageDetails"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPackageType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDriver"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMini"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSedan"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSuv"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRegion"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));
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