<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditProduct")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditProduct"), UrlConstants.JSP_URLS.MANAGE_PRODUCT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-product" name="edit-product" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_VENDOR_PRODUCTS_URL%>">
						
						<%=NewThemeUiUtils.outputErrorBaner("storeAndProductErrorMessage", it.get("errorMessage"), "") %>
						
						<%=NewThemeUiUtils.outputStaticField(FieldConstants.PRODUCT_NAME, BusinessAction.messageForKeyAdmin("labelProductName"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
							<%@include file="/WEB-INF/views/includes/vendor-product-fields.jsp"%>
							
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