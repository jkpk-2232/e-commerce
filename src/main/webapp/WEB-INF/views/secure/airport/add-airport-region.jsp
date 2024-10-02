<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAddAirport")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddAirport"), UrlConstants.JSP_URLS.MANAGE_AIRPORT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="add-airport-region" name="add-airport-region" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADD_AIRPORT_REGION_URL%>">
						
							<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
							<%=NewThemeUiUtils.outputLinSepartor()%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MULTICITY_REGION_ID, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.ALIAS_NAME, BusinessAction.messageForKeyAdmin("labelAliasName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonTripleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "btnClearPoly", BusinessAction.messageForKeyAdmin("labelClear"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LAT_LONG, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LATITUDE, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LONGITUDE, it)%>
							
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