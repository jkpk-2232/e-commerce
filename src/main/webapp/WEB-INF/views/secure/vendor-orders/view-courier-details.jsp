<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelViewCourierDetails")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelViewCourierDetails"), UrlConstants.JSP_URLS.MANAGE_COURIER_HISTORY_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
						
						<div class="card">
						
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelCourierDetails")%> - #<%=it.get("userTourId").toString()%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelOrderTime")%></td>
											<td><%=it.get("createdAt").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPickupTime")%></td>
											<td><%=it.get("rideLaterPickupTime").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriver")%></td>
											<td><%=it.get("driverName").toString()%></td>
											<td><i class="bi bi-telephone-fill fa-fw"></i> <%=it.get("driverPhone").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassenger")%></td>
											<td><%=it.get("customerName").toString()%></td>
											<td><i class="bi bi-telephone-fill fa-fw"></i> <%=it.get("customerPhoneNo").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelSourceAddress")%></td>
											<td><%=it.get("sourceAddress").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDestinationAddress")%></td>
											<td><%=it.get("destinationAddress").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDeliveryStatus")%></td>
											<td><%=it.get("tourStatus").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
						
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelDeliveryDetails")%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelCourierPickupAddress")%></td>
											<td><%=it.get("courierPickupAddress").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelContactPersonName")%></td>
											<td><%=it.get("courierContactPersonName").toString()%></td>
											<td><i class="bi bi-telephone-fill fa-fw"></i> <%=it.get("courierContactPhoneNo").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelCourierDropAddress")%></td>
											<td><%=it.get("courierDropAddress").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDropContactPersonName")%></td>
											<td><%=it.get("courierDropContactPersonName").toString()%></td>
											<td><i class="bi bi-telephone-fill fa-fw"></i> <%=it.get("courierDropContactPhoneNo").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDetails")%></td>
											<td><%=it.get("courierDetails").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
						<%=NewThemeUiUtils.outputFormHiddenField("tourId", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("sLatitude", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("sLongitude", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("dLatitude", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("dLongitude", it) %>

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