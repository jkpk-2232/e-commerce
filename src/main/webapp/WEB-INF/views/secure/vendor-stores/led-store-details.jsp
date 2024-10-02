<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelLedStoreDetails")%></title>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelLedStoreDetails"), UrlConstants.JSP_URLS.VENDOR_STORE_LOCATION_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="led-store-details" name="led-store-details" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.SEND_LED_STORE_DETAILS_URL%>">
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CATEGORY_GROUP, BusinessAction.messageForKeyAdmin("labelCategoryGroup"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.STORE_CATEGROY , BusinessAction.messageForKeyAdmin("labelStoreCategory"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.LOCATION , BusinessAction.messageForKeyAdmin("labelLocations"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_STORE_ID, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
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