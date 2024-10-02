<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelPromoCode")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelPromoCode"), UrlConstants.JSP_URLS.MANAGE_PROMO_CODE_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddPromoCode", BusinessAction.messageForKeyAdmin("labelAddPromoCode"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId)) {

								LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
								filterOptionIds.put(FieldConstants.SERVICE_TYPE_ID, false);
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPromoCode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStartDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEndDate"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDiscount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMaxDiscount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelServiceTypeName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
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