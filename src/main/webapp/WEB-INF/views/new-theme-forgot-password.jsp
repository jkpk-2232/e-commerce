<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.jeeutils.StringUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelForgotPassword")%></title>
	</head>

	<body class="pace-top">
	
		<!-- BEGIN #app -->
		<div id="app" class="app app-full-height app-without-header">
		
			<!-- BEGIN login -->
			<div class="login">
		
				<!-- BEGIN login-content -->
				<div class="login-content">
		
					<form method="POST" name="forgot-password">
					
						<h1 class="text-center"><%=BusinessAction.messageForKeyAdmin("labelForgotPassword")%></h1>
						
						<%=NewThemeUiUtils.outputFieldForLogin(FieldConstants.EMAIL, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, it, "text") %>

						<button type="button" id="btnCancel" class="btn btn-outline-theme btn-lg d-block w-100 fw-500 mb-3">
							<%=BusinessAction.messageForKeyAdmin("labelCancel")%>
						</button>

						<button type="button" id="btnSend" class="btn btn-outline-theme btn-lg d-block w-100 fw-500 mb-3">
							<%=BusinessAction.messageForKeyAdmin("labelSend")%>
						</button>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ROLE_ID, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CANCEL_PAGE_REDIRECT_URL, it)%>
						
					</form>
		
				</div>
				<!-- END login-content -->
		
			</div>
			<!-- END login -->
			
			<%@include file="/WEB-INF/views/includes/new-theme-change-theme.jsp"%>
			
		</div>
		<!-- END #app -->
		
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>