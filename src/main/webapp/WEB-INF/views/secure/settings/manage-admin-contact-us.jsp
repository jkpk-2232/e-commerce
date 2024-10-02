<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAdminContactUs")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAdminContactUs"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="admin-contact-us" name="admin-contact-us" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_ADMIN_CONTACT_US_URL%>">
						
							<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
							<%=NewThemeUiUtils.outputLinSepartor()%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.AREA_NAME, BusinessAction.messageForKeyAdmin("labelAreaName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 500, it, "text", "col-sm-3", "col-sm-5", 5)%>
							
							<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE_NUMBER_ONE, FieldConstants.COUNTRY_CODE_ONE, BusinessAction.messageForKeyAdmin("labelPhoneNumber1"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE_NUMBER_TWO, FieldConstants.COUNTRY_CODE_TWO, BusinessAction.messageForKeyAdmin("labelPhoneNumber2"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL, BusinessAction.messageForKeyAdmin("labelEmail"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.FAX, BusinessAction.messageForKeyAdmin("labelFax"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADMIN_COMPANY_CONTACT_ID, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LATITUDE, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LONGITUDE, it)%>
						
						</form>
						
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