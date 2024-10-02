<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelViewOrder")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelViewOrder"), UrlConstants.JSP_URLS.MANAGE_PRODUCT_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
						
						<% 
							if (it.get("showChangeStatus").toString().equalsIgnoreCase("true")) {
						%>
						
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.ORDER_STATUS_FILTER, BusinessAction.messageForKeyAdmin("labelOrderStatus"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
							<%=NewThemeUiUtils.outputButtonDoubleField("btnChangeOrderStatus", BusinessAction.messageForKeyAdmin("labelChangeOrderStatus"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
						
							<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<% 
							}
						%>
						
						<div class="card">
						
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<%=BusinessAction.messageForKeyAdmin("labelOrderDetails")%> - #<%=it.get("orderShortId").toString()%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelOrderTime")%></td>
											<td><%=it.get("orderCreationTime").toString()%></td>
										</tr>
										<tr>
											<td class="w-450px"><%=BusinessAction.messageForKeyAdmin("labelVendor")%></td>
											<td><%=it.get("vendorName").toString()%></td><td><%=it.get("vendorAddress").toString()%></td>
										</tr>
										<tr>
											<td class="w-450px"><%=BusinessAction.messageForKeyAdmin("labelVendorStore")%></td>
											<td><%=it.get("storeName").toString()%></td><td><%=it.get("storeAddress").toString()%></td>
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
											<td><%=BusinessAction.messageForKeyAdmin("labelShippingInformation")%></td>
											<td><%=it.get("orderDeliveryAddress").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDeliveryStatus")%></td>
											<td><%=it.get("orderDeliveryStatus").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelEndDeliveryOtp")%></td>
											<%
												if (it.get("endOtp") != null) {
											%>
												<td><%=it.get("endOtp").toString()%></td>
											<%
												}
											%>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelIsSelfDeliveryWithinXKm")%></td>
											<td><%=it.get(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM).toString()%></td>
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
								<%=BusinessAction.messageForKeyAdmin("labelOrderBreakdown")%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td class="w-450px"><%=BusinessAction.messageForKeyAdmin("labelNoOfItems")%></td>
											<td></td>
											<td><%=it.get("orderNumberOfItems").toString()%> items</td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDistance")%></td>
											<td></td>
											<td><%=it.get("orderDeliveryDistance").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelOrderTotal")%></td>
											<td></td>
											<td><%=it.get("orderTotal").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDeliveryCharges")%></td>
											<td></td>
											<td><%=it.get("orderDeliveryCharges").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPromoDiscount")%></td>
											<td><%=BusinessAction.messageForKeyAdmin("labelPromoCode")%>: <u class="text-success fw-bold small"><%=it.get("promoCode").toString()%></u></td>
											<td><%=it.get("orderPromoCodeDiscount").toString()%></td>
										</tr>
										<tr>
											<td class="pb-2" colspan="2"><b><%=BusinessAction.messageForKeyAdmin("labelFinalCharges")%></b></td>
											<td class="pb-2 text-decoration-underline"><b><%=it.get("orderCharges").toString()%></b></td>
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
											<td class="pt-2 pb-2"><%=it.get("orderCharges").toString()%></td>
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
								<%=BusinessAction.messageForKeyAdmin("labelProduct")%> (<%=it.get("orderNumberOfItems").toString()%>)
							</div>
							
							<div class="card-body text-inverse">
								<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>

						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
						<%=NewThemeUiUtils.outputFormHiddenField("orderId", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("type", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("storeAddressLat", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("storeAddressLng", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("orderDeliveryLat", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("orderDeliveryLng", it) %>
						
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