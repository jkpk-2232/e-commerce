<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>

<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelImportCSVToAddProducts")%></title>
	</head>
	
	<body>
		
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelImportCSVToAddProducts"), UrlConstants.JSP_URLS.MANAGE_PRODUCT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
				
					<div class="card-body">
						
						
					
						<form method="POST" id="import-csv-product" name="import-csv-product" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.IMPORT_CSV_PRODUCTS_URL%>" enctype = "multipart/form-data" >
							
							<% 
								Map<String, String> sessionAttributesProductFieldsLocal = SessionUtils.getSession(request, response, true);
								String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesProductFieldsLocal, LoginUtils.ROLE_ID);
							
								if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal)) {
							%>
							
								<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, it, "col-sm-3", "col-sm-5")%>

							<% 
								}
							%>
							
							
							<%-- <%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), true, 1, it, "col-sm-3", "col-sm-5")%> --%>	
								
							<% 
								if (it.containsKey(FieldConstants.ADD_PRODUCT_FLOW) && it.get(FieldConstants.ADD_PRODUCT_FLOW).toString().equalsIgnoreCase("true")) {
							%>
							
								<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORES, BusinessAction.messageForKeyAdmin("labelProductForAllStores"), true, 1, it, "col-sm-3", "col-sm-5")%>	
								
								<div id="vendorStoreIdDivOuterDiv">
									<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), true, 1, it, "col-sm-3", "col-sm-5")%>	
								</div>
							
							<% 
								}
							%>
							
							
							<input type="file" id="productCSV" name="productCSV" class="upload" accept=".csv" >
							
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