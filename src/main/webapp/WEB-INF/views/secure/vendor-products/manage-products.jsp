<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageProducts")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManageProducts"), UrlConstants.JSP_URLS.MANAGE_PRODUCT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.IMPORT_CSV_PRODUCTS, it)%>
					
					    <%=NewThemeUiUtils.outputButtonSingleField("btnImportCSV", BusinessAction.messageForKeyAdmin("labelImportCSV"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputButtonSingleField("btnSampleCSVFormat", BusinessAction.messageForKeyAdmin("labelSampleCSV"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<% 
							if (!UserRoleUtils.isSubVendorRole(roleId))	{
						%>	
							<%=NewThemeUiUtils.outputButtonSingleField("btnAddProduct", BusinessAction.messageForKeyAdmin("labelAddProduct"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						<% 
							}
						%>
					
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
						
								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
							
								filterOptionIds.put(FieldConstants.VENDOR_ID, false);
								filterOptionIds.put(FieldConstants.SERVICE_ID, false);
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductCategory"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStoreName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelActualPrice"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDiscountedPrice"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelWeight"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductStocks"));
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