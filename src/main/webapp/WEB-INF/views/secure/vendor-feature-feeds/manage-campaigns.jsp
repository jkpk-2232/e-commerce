<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageCampaigns")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManageCampaigns"), UrlConstants.JSP_URLS.MANAGE_CAMPAGIN_DASHBOARD_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>
						
						<%=NewThemeUiUtils.outputButtonSingleField("btnAddCampaign", BusinessAction.messageForKeyAdmin("labelAddCampaign"), NewThemeUiUtils.BUTTON_FLOAT_LEFT, NewThemeUiUtils.BUTTON_FLOAT_LEFT)%>
					
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCampaignId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCampaignName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBudget"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelViewsCount"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelDateAndTime"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelAction"));  
						%>
						
						<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						
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