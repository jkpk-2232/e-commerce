<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditProductImage")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditProductImage"), "")%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-product-image" name="edit-product-image" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_PRODUCT_IMAGE_URL%>">
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_VARIANT_ID, BusinessAction.messageForKeyAdmin("labelProductVariantId"), true, 1, it, "col-sm-2", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_IMAGE_ID, it)%>

							<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PRODUCT_IMAGE, BusinessAction.messageForKeyAdmin("labelProductImage"), it, "col-sm-3", "col-sm-10")%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_IMAGE_HIDDEN, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, it)%>

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