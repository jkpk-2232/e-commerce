<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelViewAppointment")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelViewAppointment"), UrlConstants.JSP_URLS.MANAGE_APPOINTMENT_SETTINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
						
						<% 
							if (it.get("showChangeStatus").toString().equalsIgnoreCase("true")) {
						%>
						
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.APPOINTMENT_STATUS_FILTER, BusinessAction.messageForKeyAdmin("labelAppointmentStatus"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnChangeAppointmentStatus", BusinessAction.messageForKeyAdmin("labelChangeAppointmentStatus"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
						
							<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							}
						%>
						
						<div class="card">
						
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelAppointmentDetails")%> - #<%=it.get("appointmentShortId").toString()%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelAppointmentCreationTime")%></td>
											<td><%=it.get("appointmentCreationTime").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelAppointmentTime")%></td>
											<td><%=it.get("appointmentTime").toString()%></td>
										</tr>
										<tr>
											<td class="w-450px"><%=BusinessAction.messageForKeyAdmin("labelVendor")%></td>
											<td><%=it.get("vendorName").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassenger")%></td>
											<td><%=it.get("customerName").toString()%></td>
											<td><i class="bi bi-telephone-fill fa-fw"></i> <%=it.get("customerPhoneNo").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelAppointmentStatus")%></td>
											<td><%=it.get("appointmentStatus").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelServiceCompletedOtp")%></td>
											<td><%=it.get("endOtp").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPaymentStatus")%></td>
											<td><%=it.get("paymentStatus").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
						
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelAppointmentBreakdown")%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td class="w-450px"><%=BusinessAction.messageForKeyAdmin("labelNoOfItems")%></td>
											<td></td>
											<td><%=it.get("appointmentNumberOfItems").toString()%> items</td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelOrderTotal")%></td>
											<td></td>
											<td><%=it.get("appointmentTotal").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPromoDiscount")%></td>
											<td><%=BusinessAction.messageForKeyAdmin("labelPromoCode")%>: <u class="text-success fw-bold small"><%=it.get("promoCode").toString()%></u></td>
											<td><%=it.get("appointmentPromoCodeDiscount").toString()%></td>
										</tr>
										<tr>
											<td class="pb-2" colspan="2"><b><%=BusinessAction.messageForKeyAdmin("labelFinalCharges")%></b></td>
											<td class="pb-2 text-decoration-underline"><b><%=it.get("appointmentCharges").toString()%></b></td>
										</tr>
										<tr>
											<td colspan="3">
												<hr class="m-0">
											</td>
										</tr>
										<tr>
											<td class="pt-2 pb-2 text-nowrap">
												<%=BusinessAction.messageForKeyAdmin("labelPaymentMode")%>
											</td>
											<td class="pt-2 pb-2">
												<%=BusinessAction.messageForKeyAdmin("labelVia")%> <a href="#" class="text-decoration-none"><%=it.get("paymentMode").toString()%></a>
											</td>
											<td class="pt-2 pb-2"><%=it.get("appointmentCharges").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
						%>
						
						<div class="card mb-4">
							
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelProduct")%> (<%=it.get("appointmentNumberOfItems").toString()%>)
							</div>
							
							<div class="card-body text-inverse">
								<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>

						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
						<%=NewThemeUiUtils.outputFormHiddenField("appointmentId", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("type", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("storeAddressLat", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("storeAddressLng", it) %>
						
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