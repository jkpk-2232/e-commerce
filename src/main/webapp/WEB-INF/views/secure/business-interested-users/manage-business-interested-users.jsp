<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelManageBusinessUsers")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManageBusinessUsers"), "")%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
					
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelEmail"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPhoneNumber"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCityName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBusinessType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCarType"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCategory"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelStoreName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelNoOfStores"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelBrandName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelNoOfOutlets"));
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