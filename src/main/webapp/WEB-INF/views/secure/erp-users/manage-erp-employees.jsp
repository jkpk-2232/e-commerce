<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageEmployees")%></title>
    <script type="text/javascript"
	                  src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>   
	 <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	
	</head>
	
	<body>
		
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManageEmployees"), UrlConstants.JSP_URLS.MANAGE_SUB_VENDOR_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
				
					<div class="card-body">
					
						
						  <!-- Side-by-side Charts and Map Section -->
					<div class="row">
						<!-- Doughnut Chart -->
						<div class="col-md-5 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelEmployees")%></h4>
							<div class="flex-grow-1 d-flex align-items-center justify-content-center">
                          <%= NewThemeUiUtils.outputChart("employeeChart", " ", "doughnut") %>

                           </div>

						</div>

						<!-- Google Map -->
						<div class="col-md-7 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelEmployeeslocation")%></h4>
							 <%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas",BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 40vh !important;")%>
						
						</div>
					</div>
					<%=NewThemeUiUtils.outputLinSepartor()%>


					<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
					
						<% 
							if (UserRoleUtils.isErpRole(roleId)) {
						%>
								<%=NewThemeUiUtils.outputButtonSingleField("btnAddErpEmployee", BusinessAction.messageForKeyAdmin("labelAddErpEmployee"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						<%
							}
						%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
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
							columnNames.add(BusinessAction.messageForKeyAdmin("labelFullName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmailAddress"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPhoneNumber"));
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