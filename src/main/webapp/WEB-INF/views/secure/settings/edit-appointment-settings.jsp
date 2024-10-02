<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditAppointmentSettings")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditAppointmentSettings"), UrlConstants.JSP_URLS.MANAGE_APPOINTMENT_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="edit-appointment-settings" name="edit-appointment-settings" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_APPOINTMENT_SETTINGS_URL%>">
							
							<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelServiceName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.MIN_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMinimumBookingTime"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MAX_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMaximumBookingTime"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.FREE_CANCELLATION_TIME_MINS, BusinessAction.messageForKeyAdmin("labelFreeCancellationTimeMins"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputInputField(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS, BusinessAction.messageForKeyAdmin("labelCronJobExpireTimeMins"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICE_ID, it)%>
							
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