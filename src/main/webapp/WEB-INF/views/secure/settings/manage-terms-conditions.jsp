<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelTermsAndConditions")%></title>
		
		<script>
			var CKEDITOR_BASEPATH = "${pageContext.request.contextPath}<%=UrlConstants.JS_URLS.CKEDITOR_FOLDER_PATH%>";
		</script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelTermsAndConditions"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="add-terms-conditions" name="add-terms-conditions" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_TERMS_CONDITIONS_URL%>">
						
							<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.TERMS_CONDITIONS, null, true, 1, 45000, it, "text", null, "col-sm-15", 5)%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-0")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.LANGUAGE, it)%>
							
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