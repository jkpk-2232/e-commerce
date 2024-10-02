<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAddBrand")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddBrand"), "")%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="add-brand" name="add-brand" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADD_BRAND_URL%>">
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.BRAND_NAME, BusinessAction.messageForKeyAdmin("labelBrandName"), true, 1, 30, it, "text", "col-sm-2", "col-sm-3")%>

							<%=NewThemeUiUtils.outputInputField(FieldConstants.BRAND_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelBrandDescription"), true, 1, 30, it, "text", "col-sm-2", "col-sm-3")%>

							<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.BRAND_IMAGE, BusinessAction.messageForKeyAdmin("labelBrandImage"), it, "col-sm-3", "col-sm-10")%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.BRAND_IMAGE_HIDDEN, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.BRAND_IMAGE_HIDDEN_DUMMY, it)%>

							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	
							
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