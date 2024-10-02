<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelBookLaterSettings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelBookLaterSettings"), UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-ride-later-settings" name="edit-ride-later-settings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.MANAGE_RIDE_LATER_SETTINGS_URL%>">
									
							<%=NewThemeUiUtils.outputInputField(FieldConstants.MIN_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMinimumBookingTime"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.HOUR, BusinessAction.messageForKeyAdmin("labelMaximumBookingTime"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVER_JOB_REQUEST_TIME, BusinessAction.messageForKeyAdmin("labelDriverJobRequestTime"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, BusinessAction.messageForKeyAdmin("labelAllocationBeforeTime"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, BusinessAction.messageForKeyAdmin("labelAllocationAfterTime"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS, BusinessAction.messageForKeyAdmin("labelTakeBookingForNextXDays"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED, BusinessAction.messageForKeyAdmin("labelTakeBookingMaximumNumber"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
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