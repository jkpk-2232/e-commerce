<!DOCTYPE html>
<%@page import="com.jeeutils.StringUtils"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditPromoCode")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditPromoCode"), UrlConstants.JSP_URLS.MANAGE_PROMO_CODE_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-promo-code" name="edit-promo-code" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_PROMO_CODE_URL%>">
									
							<% 
								if (UserRoleUtils.isSuperAdminAndAdminRole(roleId))	{
							%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_TYPE_ID, BusinessAction.messageForKeyAdmin("labelServiceType"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<% 
								}
							%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROMO_CODE_ID, it)%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.PROMO_CODE, BusinessAction.messageForKeyAdmin("labelPromoCode"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.MODE, BusinessAction.messageForKeyAdmin("labelMode"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.DISCOUNT, BusinessAction.messageForKeyAdmin("labelDiscount"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.MAX_DISCOUNT, BusinessAction.messageForKeyAdmin("labelMaxDiscount"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.PROMO_CODE_START_DATE, BusinessAction.messageForKeyAdmin("labelStartDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.PROMO_CODE_END_DATE, BusinessAction.messageForKeyAdmin("labelEndDate"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<% 
								if (StringUtils.validString(it.get(FieldConstants.PROMO_CODE_END_DATE_CUSTOM_ERROR))) {
							%>
								<%=NewThemeUiUtils.outputErrorDiv(it.get(FieldConstants.PROMO_CODE_END_DATE_CUSTOM_ERROR))%>
							<%
								}
							%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-5")%>
							
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