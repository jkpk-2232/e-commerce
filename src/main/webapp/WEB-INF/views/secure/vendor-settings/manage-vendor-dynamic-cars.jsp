<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageDynamicCars")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManageDynamicCars"), it.get(FieldConstants.CAR_ICON))%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="manage-vendor-dynamic-cars" name="manage-vendor-dynamic-cars" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_VENDOR_DYNAMIC_CARS_URL%>">
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_NAME, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelServiceName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.CAR_TYPE_LIST, BusinessAction.messageForKeyAdmin("labelCarType"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_TYPE_ID, it)%>
							
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