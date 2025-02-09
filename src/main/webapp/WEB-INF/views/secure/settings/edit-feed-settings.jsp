<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditFeedSettings")%></title>
	</head>
	
	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditFeedSettings"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="editFeedSettings" name="editFeedSettings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_FEED_SETTINGS_URL%>">
							
							<%@include file="/WEB-INF/views/includes/feed-settings-fields.jsp"%>
							
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