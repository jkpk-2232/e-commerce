<!DOCTYPE html>
<%@page import="com.jeeutils.StringUtils"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.models.TourTaxModel"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelBookingDetails")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelBookingDetails"), UrlConstants.JSP_URLS.BOOKINGS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
					
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputButtonDoubleField("btnEmail", BusinessAction.messageForKeyAdmin("labelEmail"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnRefund", BusinessAction.messageForKeyAdmin("labelRefund"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "")%>	
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
					
						<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
				
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<i class="fas fa-car fa-fw me-2 text-theme"></i>
								<%=BusinessAction.messageForKeyAdmin("labelTourDetails")%> - #<%=it.get("userTourId").toString()%>
							</div>
						
							<div class="card-body">
								
								<%=NewThemeUiUtils.outputStaticField("userTourId", BusinessAction.messageForKeyAdmin("labelUserTourId"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("tripRequestTime", BusinessAction.messageForKeyAdmin("labelTripRequestTime"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("sourceAddress", BusinessAction.messageForKeyAdmin("labelSourceAddress"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<div id="destinationAddressDiv">
									<%=NewThemeUiUtils.outputStaticField("destinationAddress", BusinessAction.messageForKeyAdmin("labelDestinationAddress"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								</div>
								
								<%=NewThemeUiUtils.outputStaticField("charges", BusinessAction.messageForKeyAdmin("labelTotalFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("refundAmount", BusinessAction.messageForKeyAdmin("labelRefundAmount"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("paymentMode", BusinessAction.messageForKeyAdmin("labelPaymentMode"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<% 
									if (it.get("isAirportFixedFareApplied").toString().equalsIgnoreCase("true")) {
								%>
									<%=NewThemeUiUtils.outputStaticField("airportBookingType", BusinessAction.messageForKeyAdmin("labelAirportBookingType"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								<% 
									}
								%>
								
								<div id="surgeDiv">
								
									<%=NewThemeUiUtils.outputStaticField("surgeType", BusinessAction.messageForKeyAdmin("labelSurgeType"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
									<%=NewThemeUiUtils.outputStaticField("surgeRadius", BusinessAction.messageForKeyAdmin("labelSurgeRadius"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								</div>
								
								<%=NewThemeUiUtils.outputStaticField("tourStatus", BusinessAction.messageForKeyAdmin("labelTourStatus"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("paymentStatusString", BusinessAction.messageForKeyAdmin("labelPaymentStatus"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("takeBookingByDriver", BusinessAction.messageForKeyAdmin("labelTakeBookingByDriver"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("takeBookingByDriverTime", BusinessAction.messageForKeyAdmin("labelTakeBookingByDriverTime"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
				
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<i class="fas fa-signal fa-fw me-2 text-theme"></i>
								<%=BusinessAction.messageForKeyAdmin("labelTripStatistics")%>
							</div>
						
							<div class="card-body">
								
								<%=NewThemeUiUtils.outputStaticField("tourTypeStatus", BusinessAction.messageForKeyAdmin("labelRideType"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("carType", BusinessAction.messageForKeyAdmin("labelServiceType"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("distance", BusinessAction.messageForKeyAdmin("labelDistance"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<%=NewThemeUiUtils.outputStaticField("duration", BusinessAction.messageForKeyAdmin("labelDuration"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								<div id="waitingTimeDiv">
									<%=NewThemeUiUtils.outputStaticField("waitingTime", BusinessAction.messageForKeyAdmin("labelWaitingTime"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								</div>
								
								<div id="rentalPackageDiv">
									<%=NewThemeUiUtils.outputStaticField("rentalPackage", BusinessAction.messageForKeyAdmin("labelRentalPackage"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								</div>
								
								<div id="updatedAmtDetailsDiv">
								
									<%=NewThemeUiUtils.outputStaticField("updatedAmountCollected", BusinessAction.messageForKeyAdmin("labelUpdatedAmountCollected"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
									<%=NewThemeUiUtils.outputStaticField("remark", BusinessAction.messageForKeyAdmin("labelRemark"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								</div>
								
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>

						<div class="card">
				
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<i class="fas fa-sitemap fa-fw me-2 text-theme"></i>
								<%=BusinessAction.messageForKeyAdmin("labelFareBreakdown")%>
							</div>
						
							<div class="card-body">
								
								<div id="fareBreakdownDiv">
								
									<%=NewThemeUiUtils.outputStaticField("baseFare", BusinessAction.messageForKeyAdmin("labelBaseFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("markupFare", BusinessAction.messageForKeyAdmin("labelMarkupFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<div id="distTimeFareForRentalDiv">
									
										<%=NewThemeUiUtils.outputStaticField("distanceFare", BusinessAction.messageForKeyAdmin("labelAdditionalDistanceFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
										
										<%=NewThemeUiUtils.outputStaticField("timeFare", BusinessAction.messageForKeyAdmin("labelAdditionalTimeFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									</div>
									
									<div id="distTimeFareForTaxiDiv">
									
										<%=NewThemeUiUtils.outputStaticField("distanceFareBeforeSpecificKm", it.get("labelDistanceFareBeforeSpecificKm").toString(), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
										
										<%=NewThemeUiUtils.outputStaticField("distanceFareAfterSpecificKm", it.get("labeldistanceFareAfterSpecificKm").toString(), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
										<%=NewThemeUiUtils.outputStaticField("distanceFare", BusinessAction.messageForKeyAdmin("labelDistanceFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
										
										<%=NewThemeUiUtils.outputStaticField("timeFare", BusinessAction.messageForKeyAdmin("labelTimeFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									</div>
									
									<div id="waitingFareDiv">
										<%=NewThemeUiUtils.outputStaticField("waitingFare", BusinessAction.messageForKeyAdmin("labelWaitingFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									</div>
									
									<div id="surgeFareDiv">
										<%=NewThemeUiUtils.outputStaticField("surgeFare", BusinessAction.messageForKeyAdmin("labelSurgeFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									</div>
									
									<%=NewThemeUiUtils.outputStaticField("totalFare", BusinessAction.messageForKeyAdmin("labelFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("promoDiscount", BusinessAction.messageForKeyAdmin("labelPromoDiscount"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
									<%=NewThemeUiUtils.outputStaticField("taxAmount", BusinessAction.messageForKeyAdmin("labelTaxAmount"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<div id="taxDetailsDiv">
									
										<% 
											List<TourTaxModel> tourTaxModelList = TourTaxModel.getTourTaxListByTourId(it.get("tourId").toString());
										
											for (TourTaxModel tourTaxModel : tourTaxModelList) {
										%>
											<%=NewThemeUiUtils.outputStaticField(BusinessAction.messageForKeyAdmin("labelAppliedTax") + tourTaxModel.getTaxName(), tourTaxModel.getTaxName(), StringUtils.valueOf(tourTaxModel.getTaxAmount()), "col-sm-4 px-5", "col-sm-5")%>
										<%
											}
										%>
									
									</div>
									
									<%=NewThemeUiUtils.outputStaticField("total", BusinessAction.messageForKeyAdmin("labelTotalTotal"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("credits", BusinessAction.messageForKeyAdmin("labelCredits"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("amountCollected", BusinessAction.messageForKeyAdmin("labelAmountCollected"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								</div>
								
								<div id="airportFareBreakdownDiv">
								
									<%=NewThemeUiUtils.outputStaticField("baseFare", BusinessAction.messageForKeyAdmin("labelAirportFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
									<%=NewThemeUiUtils.outputStaticField("waitingFare", BusinessAction.messageForKeyAdmin("labelWaitingFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("totalFare", BusinessAction.messageForKeyAdmin("labelFare"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("promoDiscount", BusinessAction.messageForKeyAdmin("labelPromoDiscount"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("taxAmount", BusinessAction.messageForKeyAdmin("labelTaxAmount"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("total", BusinessAction.messageForKeyAdmin("labelTotalTotal"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("credits", BusinessAction.messageForKeyAdmin("labelCredits"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("amountCollected", BusinessAction.messageForKeyAdmin("labelAmountCollected"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								</div>
								
								<div id="fareBreakdownDivForCancelledTour">
								
									<%=NewThemeUiUtils.outputStaticField("cancellationCharges", BusinessAction.messageForKeyAdmin("labelCancellationFees"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
									<%=NewThemeUiUtils.outputStaticField("usedCredits", BusinessAction.messageForKeyAdmin("labelUsedCredits"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("taxAmount", BusinessAction.messageForKeyAdmin("labelTotalTax"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField("amountCollected", BusinessAction.messageForKeyAdmin("labelTotal"), false, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
								
								</div>

							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
				
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<i class="fas fa-user fa-fw me-2 text-theme"></i>
								<%=BusinessAction.messageForKeyAdmin("labelPassengerDetails")%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelCustomerName")%></td>
											<td><%=it.get("pFullName").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassengerPhone")%></td>
											<td><%=it.get("pPhone").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassengerEmail")%></td>
											<td><%=it.get("pEmail").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassengerRatings")%></td>
											<td><%=it.get("passengerRate").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelPassengerComments")%></td>
											<td><%=it.get("passengerComment").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div class="card">
				
							<div class="card-header d-flex align-items-center bg-inverse bg-opacity-10 fw-400">
								<i class="fas fa-user-secret fa-fw me-2 text-theme"></i>
								<%=BusinessAction.messageForKeyAdmin("labelDriverDetails")%>
							</div>
						
							<div class="card-body">
								<table class="table table-borderless table-sm m-0">
									<tbody>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriverName")%></td>
											<td><%=it.get("dFullName").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriverPhone")%></td>
											<td><%=it.get("dPhone").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriverEmail")%></td>
											<td><%=it.get("dEmail").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelServiceType")%></td>
											<td><%=it.get("dServiceType").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriverRating")%></td>
											<td><%=it.get("driverRate").toString()%></td>
										</tr>
										<tr>
											<td><%=BusinessAction.messageForKeyAdmin("labelDriverComments")%></td>
											<td><%=it.get("driverComment").toString()%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<%=NewThemeUiUtils.outputCardBodyArrows()%>
						
						</div>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<%=NewThemeUiUtils.outputFormHiddenField("statusForFareBreakdown", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("tourId", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("currentLat", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("currentLng", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("tourType", it) %> 
						<%=NewThemeUiUtils.outputFormHiddenField("isRefunded", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("isRentalBooking", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("finalAmountCollectedHidden", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("isAirportFixedFareApplied", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("airportBookingType", it) %>
						<%=NewThemeUiUtils.outputFormHiddenField("isSurgeApplied", it) %>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
			<%@include file="/WEB-INF/views/includes/refund-modal.jsp"%>
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>