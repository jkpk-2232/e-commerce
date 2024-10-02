<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">
	
	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAddCampaign")%></title>
	</head>
	
	<body>
		
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddCampaign"), UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
				
				<div class="card">
					
					<div class="card-body">
						
						<form method="POST" id="add-campaign" name="add-campaign" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADD_CAMPAIGN_URL%>">
							
							<% 
								Map<String, String> sessionAttributesADFieldsLocal = SessionUtils.getSession(request, response, true);
								String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesADFieldsLocal, LoginUtils.ROLE_ID);
							
								if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal)) {
							%>
							
								<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<% 
								} else {
							%>
								<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							<%
								}
							%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.CAMPAIGN_NAME, BusinessAction.messageForKeyAdmin("labelCampaignName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.AD, BusinessAction.messageForKeyAdmin("labelAD"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.SCHEDULE_START_DATE, BusinessAction.messageForKeyAdmin("labelScheduleStartDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputTimePickerInputField(FieldConstants.SCHEDULE_START_TIME, BusinessAction.messageForKeyAdmin("labelScheduleStartTime"), true, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.SCHEDULE_END_DATE, BusinessAction.messageForKeyAdmin("labelScheduleEndDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputTimePickerInputField(FieldConstants.SCHEDULE_END_TIME, BusinessAction.messageForKeyAdmin("labelScheduleEndTime"), true, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.TARGET_SPECIFIC, BusinessAction.messageForKeyAdmin("labelTargetSpecific"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCities"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.LOCATION , BusinessAction.messageForKeyAdmin("labelLocations"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.STORE , BusinessAction.messageForKeyAdmin("labelStroes"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DEVICE , BusinessAction.messageForKeyAdmin("labelDevices"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.BUDGET, BusinessAction.messageForKeyAdmin("labelBudget"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
						</form>
						
					</div>
					
						<%=NewThemeUiUtils.outputCardBodyArrows()%>	
				</div>
				
			</div>
			
		</div>
		
			<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
	</body>
</html>