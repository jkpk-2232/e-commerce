<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelDriverWalletSettings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverWalletSettings"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-driver-wallet-settings" name="edit-driver-wallet-settings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_DRIVER_WALLET_SETTINGS_URL%>">
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.MINIMUM_AMOUNT, BusinessAction.messageForKeyAdmin("labelMinimumAmount"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.NOTIFY_AMOUNT, BusinessAction.messageForKeyAdmin("labelNotifyAmount"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
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