<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.viewutils.*,com.jeeutils.*"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelSubscribePackage")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelSubscribePackage"), UrlConstants.JSP_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="subscribe-package" name="subscribe-package" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.SUBSCRIBE_PACKAGE_URL%>">
								
							<% 
								if (StringUtils.validString(it.get(FieldConstants.PACKAGE_SUBSCRIBE_ERROR))) {
							%>
								<%=NewThemeUiUtils.outputErrorDiv(it.get(FieldConstants.PACKAGE_SUBSCRIBE_ERROR))%>
							<%
								}
							%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.DRIVER_NAME, BusinessAction.messageForKeyAdmin("labelDriverName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SUBSCRIPTION_PACKAGE_LIST, BusinessAction.messageForKeyAdmin("labelPackageList"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PAYMENT_MODE, BusinessAction.messageForKeyAdmin("labelPaymentMode"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ID, it)%>
							
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