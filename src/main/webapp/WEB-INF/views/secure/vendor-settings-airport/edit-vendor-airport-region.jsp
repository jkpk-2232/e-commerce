<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditAirport")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditAirport"), UrlConstants.JSP_URLS.MANAGE_AIRPORT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="edit-vendor-airport-region" name="edit-vendor-airport-region" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_VENDOR_AIRPORT_REGION_URL%>">
						
							<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
							<%=NewThemeUiUtils.outputLinSepartor()%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.REGION_NAME, BusinessAction.messageForKeyAdmin("labelRegionName"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.ALIAS_NAME, BusinessAction.messageForKeyAdmin("labelAliasName"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LAT_LONG, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LATITUDE, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LONGITUDE, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AIRPORT_REGION_ID, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_AIRPORT_REGION_ID, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.MULTICITY_REGION_ID, it)%>
							
							<%=NewThemeUiUtils.outputLinSepartor()%>
							
							<%@include file="/WEB-INF/views/includes/airport-fare-fields.jsp"%>
							
							<%=NewThemeUiUtils.outputLinSepartor()%>
						
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