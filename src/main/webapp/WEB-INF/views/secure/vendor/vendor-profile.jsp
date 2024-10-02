<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelVendorProfile")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelVendorProfile"), UrlConstants.JSP_URLS.VENDOR_PROFILE_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-vendor-profile" name="edit-vendor-profile" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.VENDOR_PROFILE_URL%>">
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelSuperService"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
		
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelCategory"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.CURRENT_BALANCE, BusinessAction.messageForKeyAdmin("labelCurrentBalance"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFirstName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
						
							<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PROFILE_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelProfileImage"), it, "col-sm-3", "col-sm-10")%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, it)%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
						</form>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
			<div>
				<a href="#changePasswordModal" data-bs-toggle="modal" class="<%=NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS%>">
					<%=BusinessAction.messageForKeyAdmin("labelChangePassword")%>
				</a>
			</div>
			
			<%@include file="/WEB-INF/views/includes/new-theme-change-password.jsp"%>
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>