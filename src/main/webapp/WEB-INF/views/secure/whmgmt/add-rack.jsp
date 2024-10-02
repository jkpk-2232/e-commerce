<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelRacks")%></title>
	</head>
	
	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelRacks"), UrlConstants.JSP_URLS.RACK_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="add-rack" name="add-rack" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADD_RACK_URL%>" >
						<% 
							if (it.containsKey("isCreate") && it.get("isCreate").toString().equalsIgnoreCase("true")) {
						%>
						
							<%=NewThemeUiUtils.outputButtonSingleField("btnAddRack", BusinessAction.messageForKeyAdmin("labelAddRack"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_LEFT)%>
						<% 
							}
						%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_STORE_ID, it)%>	
						</form>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelRackNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStoreName"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
				</div>
					
			</div>
			
		</div>
		
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
	</body>
	
</html>