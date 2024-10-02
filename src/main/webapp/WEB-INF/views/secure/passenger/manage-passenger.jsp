<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelPassengers")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelPassengers"), UrlConstants.JSP_URLS.MANAGE_PASSENGER_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputButtonSingleField("btnExport", BusinessAction.messageForKeyAdmin("labelExport"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
						
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
								filterOptionIds.put(FieldConstants.STATUS, false);
							}
							
							filterOptionIds.put(FieldConstants.VENDOR_ID, false);
						%>
						
						<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelFullName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmail"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelContactNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelTypeOfUser"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelReferrerDriver"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAction"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PASSENGER_ID, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.IS_EXPORT_ACCESS, it)%>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
			<!-- BEGIN #modalEdit -->
			<div class="modal fade" id="changeAttachDriverDialog">
			
				<div class="modal-dialog">
			
					<div class="modal-content">
			
						<div class="modal-header">
							<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelChangeAttachDriverDialogHeading")%></h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
						</div>
			
						<div class="modal-body">
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DRIVER_LIST, BusinessAction.messageForKeyAdmin("labelDriver"), true, 1, it, "col-sm-2", "col-sm-10")%>
			
						</div>
			
						<div class="modal-footer">
							<button type="button" class="btn btn-outline-default" id="btnCancelPopUp" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
							<button type="button" class="btn btn-outline-theme" id="btnSubmitPopUp"><%=BusinessAction.messageForKeyAdmin("labelAttachDriver")%></button>
						</div>
			
					</div>
			
				</div>
			
			</div>
			<!-- END #modalEdit -->
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>