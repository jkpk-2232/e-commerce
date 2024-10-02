<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageVendorFeeds")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddNewBrands"), UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					  <%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddVendorFeed", BusinessAction.messageForKeyAdmin("labelSendorder"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
								
								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
								filterOptionIds.put(FieldConstants.VENDOR_ID, false);
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMrp"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDiscount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBoxQuantityMin"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBoxQuantityMax"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRequiredQty"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBarcode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBatchNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelExpiry"));
							
							
							
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
		</div>
		<!-- END #content -->
		<script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.0/dist/JsBarcode.all.min.js"></script>
		
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>