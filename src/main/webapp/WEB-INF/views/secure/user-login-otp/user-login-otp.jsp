<!DOCTYPE html>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.jeeutils.StringUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelVerificationCode")%></title>
	</head>

	<body class="pace-top">
	
		<!-- BEGIN #app -->
		<div id="app" class="app app-full-height app-without-header">
		
			<!-- BEGIN login -->
			<div class="login">
		
				<!-- BEGIN login-content -->
				<div class="login-content">
		
					<form method="POST" name="verifyOtpForm" action="${pageContext.request.contextPath}/verify-otp/<%=it.get("userLoginOtpId")%>.do">
					
						<h1 class="text-center"><%=BusinessAction.messageForKeyAdmin("labelVerificationCode")%></h1>
						
						<div class="text-inverse text-opacity-50 text-center mb-4">
							<%=BusinessAction.messageForKeyAdmin("labelVerifyOtpScreenInfo")%> <%=it.get("phoneNumber")%>
						</div>
						
						<% 
							if (StringUtils.validString(it.get("invalidLoginError"))) {
						%>
							<%=NewThemeUiUtils.outputErrorDiv(it.get("invalidLoginError"))%>
						<%
							}
						%>
						
						<%=NewThemeUiUtils.outputFieldForLogin("verificationCode", BusinessAction.messageForKeyAdmin("labelVerificationCode"), true, 1, 30, it, "password") %>

						<button type="submit" id="btnVerifyOtp" class="btn btn-outline-theme btn-lg d-block w-100 fw-500 mb-3">
							<%=BusinessAction.messageForKeyAdmin("labelValidate")%>
						</button>
						
						<div class="text-center text-inverse text-opacity-50">
							<a id="btnResendOtp" href="#"><%=BusinessAction.messageForKeyAdmin("labelResendOneTimePasssword")%></a>
						</div>
						
						<div class="text-center text-inverse text-opacity-50">
							<a href="${pageContext.request.contextPath}/logout.do"><%=BusinessAction.messageForKeyAdmin("labelWrongAccount")%></a>
						</div>
						
						<%=NewThemeUiUtils.outputFormHiddenField("userId", it)%>
						<%=NewThemeUiUtils.outputFormHiddenField("userLoginOtpId", it)%>
						<%=NewThemeUiUtils.outputFormHiddenField("phoneNumber", it)%>
						
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