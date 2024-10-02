<!DOCTYPE html>
<%@page import="org.apache.poi.hssf.record.formula.functions.If"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.viewutils.*,com.jeeutils.*"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAddFeed")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddFeed"), UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="add-feed" name="add-feed" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADD_VENDOR_FEEDS_URL%>">
								
							<%=NewThemeUiUtils.outputErrorBaner("vendorMonthlySubscriptionExpiredMessage", BusinessAction.messageForKeyAdmin("errorVendorMonthlySubscriptionExpired"), "") %>
								
							<% 
								if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
							%>
								<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendors"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							<%	
								}
							%>
							
							<%
								if (UserRoleUtils.isErpRole(roleId)) {
							%>					
									<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelBrands"), false, 1, it, "col-sm-3", "col-sm-5")%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							<%
								} else {
							%>	
									<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), false, 1, it, "col-sm-3", "col-sm-5")%>						
							<%
								}
							%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.FEED_NAME, BusinessAction.messageForKeyAdmin("labelFeedName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.FEED_MESSAGE, BusinessAction.messageForKeyAdmin("labelFeedMessage"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5", 5)%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MEDIA_TYPE, BusinessAction.messageForKeyAdmin("labelMediaType"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_SPONSORED, BusinessAction.messageForKeyAdmin("labelIsSponsored"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.FEED_BANNER, BusinessAction.messageForKeyAdmin("labelFeedBaner"), it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.FEED_BANNER_HIDDEN_IMAGE, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, it)%>
							
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