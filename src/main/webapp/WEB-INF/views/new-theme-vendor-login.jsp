<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.UrlConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.jeeutils.StringUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelLogin")%></title>
	</head>

	<body class="pace-top">
	
		<!-- BEGIN #app -->
		<div id="app" class="app app-full-height app-without-header">
		
			<!-- BEGIN login -->
			<div class="login">
		
				<!-- BEGIN login-content -->
				<div class="login-content">
		
					<form method="POST" name="loginForm" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.VENDOR_LOGIN_URL%>">
					
						<h1 class="text-center"><%=BusinessAction.messageForKeyAdmin("labelSignIn")%></h1>
						
						<div class="text-inverse text-opacity-50 text-center mb-4">
							<%=BusinessAction.messageForKeyAdmin("labelSignInInfoText")%>
						</div>
						
						<% 
							if (StringUtils.validString(it.get("invalidLoginError"))) {
						%>
							<%=NewThemeUiUtils.outputErrorDiv(it.get("invalidLoginError"))%>
						<%
							}
						%>
						
						<%=NewThemeUiUtils.outputFieldForLogin(FieldConstants.EMAIL, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, it, "text")%>
						
						<%=NewThemeUiUtils.outputFieldForLogin(FieldConstants.PASSWORD, BusinessAction.messageForKeyAdmin("labelPassword"), true, 2, 30, it, "password")%>

						<button type="submit" class="btn btn-outline-theme btn-lg d-block w-100 fw-500 mb-3"><%=BusinessAction.messageForKeyAdmin("labelSignIn")%></button>
						
						<div class="text-center text-inverse text-opacity-50">
							<% 
								String forgotPasswordUrl = String.format(UrlConstants.PAGE_URLS.FORGOT_PASSWORD_URL, it.get(FieldConstants.ROLE_ID).toString());
							%>
							<a href="${pageContext.request.contextPath}<%=forgotPasswordUrl%>"><%=BusinessAction.messageForKeyAdmin("labelForgotPassword")%></a>
						</div>

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