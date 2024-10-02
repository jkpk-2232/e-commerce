<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditUnitOfMeasure")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditUnitOfMeasure"), "")%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-uom" name="edit-uom" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_UNIT_OF_MEASURE_URL%>">
							
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.UOM_NAME, BusinessAction.messageForKeyAdmin("labelUomName"), true, 1, 30, it, "text", "col-sm-2", "col-sm-3")%>

							<%=NewThemeUiUtils.outputStaticField(FieldConstants.UOM_SHORT_NAME, BusinessAction.messageForKeyAdmin("labelUomShortName"), true, 1, 30, it, "text", "col-sm-2", "col-sm-3")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.UOM_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelUomDescription"), true, 1, 30, it, "text", "col-sm-2", "col-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.UOM_ID, it)%>

							

							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>
							
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