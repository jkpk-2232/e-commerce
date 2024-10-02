<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.viewutils.NewUiViewUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAnnouncments")%></title>
	</head>
	 <style>
        /* Dark mode specific styling for dropdown */
        [data-bs-theme="dark"] .custom-dropdown select {
            background-color: rgba(29, 40, 53, 0.75); /* Dark background with transparency */
            color: rgba(255, 255, 255, 0.75); /* Light text color with transparency */
        }

        [data-bs-theme="dark"] .custom-dropdown select option {
            background-color: rgba(29, 40, 53, 0.75); /* Dark background with transparency */
            color: rgba(255, 255, 255, 0.75); /* Light text color with transparency */
        }

       

        /* Hover and active states */
        [data-bs-theme="dark"] .custom-dropdown select:hover,
        [data-bs-theme="dark"] .custom-dropdown select:focus {
            background-color: #1d2835; /* Solid dark background on hover */
            color: #ffffff; /* White text color on hover */
        }

        /* Custom background and text color for entire dark mode */
        [data-bs-theme="dark"] body {
            background-color: #1d2835; /* Background color for dark mode */
            color: rgba(255, 255, 255, 0.75); /* Light text with transparency */
        }
        
        /* Emphasis color for links and hover states */
        [data-bs-theme="dark"] a {
            color: #c0cacf; /* Link color */
        }

        [data-bs-theme="dark"] a:hover {
            color: #ffffff; /* Link color on hover */
        }
    </style>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAnnouncments"), UrlConstants.JSP_URLS.ANNOUNCEMENT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<form method="POST" id="add-announcements" name="add-announcements" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ANNOUNCEMENT_URL%>">
					
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.USER_ROLE, BusinessAction.messageForKeyAdmin("labelUserType"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.PASSENGER_LIST, BusinessAction.messageForKeyAdmin("labelPassenger"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.DRIVER_LIST, BusinessAction.messageForKeyAdmin("labelDriver"), true, 1, it, "col-sm-3", "col-sm-5")%>	
							
							<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.ANNOUNCEMENT_MESSAGE, BusinessAction.messageForKeyAdmin("labelBroadcastMessage"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5", 5)%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnPost", BusinessAction.messageForKeyAdmin("labelPost"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	
							
						</form>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelMessage"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDateAndTime"));
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.IS_DISPLAY_PASSENGER_LIST, it) %>
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ROLE_ID, it) %>
						
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