<!DOCTYPE html>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>
<html lang="en" data-bs-theme="dark">
<head>
<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
<title><%=BusinessAction.messageForKeyAdmin("labelManualBookings")%></title>
<style>
.map-container {
	margin-top: 20px;
	/* Adjust this value to control the space between the header and the map */
}

.content-container {
	margin-bottom: 20px;
	/* Adjust this value to control the space between the different sections */
}

#green-btn, #green-btn:hover {
	color: #fff !important;
	background-color: #178a43 !important;
	border-color: #178a43 !important;
	border-radius: 0px !important;
}
</style>
</head>
<body>
	<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
	<div id="content" class="app-content">
		<!-- Output the page header using the utility method -->
		<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelManualBookings"),  UrlConstants.JSP_URLS.MANAGE_MANUAL_BOOKING_ICON)%>

		<%
		// Retrieve the attribute 'it' which contains the data for manual bookings
		HashMap itManaulBookings = (HashMap) request.getAttribute("it");
		%>

		<div class="container content-container">
			<div class="row">
				<div class="col-md-4 mb-4">
					<!-- Output a statistics field for credit -->
					<%=NewThemeUiUtils.outputStatisticsField(FieldConstants.CREDIT, BusinessAction.messageForKeyAdmin("labelCredit"), it, "", UrlConstants.JSP_URLS.MANAGE_DRIVER_ICON)%>
				</div>
				<div class="col-md-4 mb-4">
					<!-- Output a hidden field for the add URL -->
					<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ADD_URL, it)%>

					<!-- Output a button field for adding products to the cart -->
					<%=NewThemeUiUtils.outputButtonSingleField("testbutton", BusinessAction.messageForKeyAdmin("labelAddproducts"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
				</div>

				<div class="col-md-4 mb-3">
					<!-- Output a select input field for service type -->
					<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_TYPE, BusinessAction.messageForKeyAdmin("labelServiceType"), true, 1, itManaulBookings, "col-sm-3", "col-sm-5")%>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<!-- Various input fields for manual bookings -->
					<div class="mb-3">
					
						<%=NewThemeUiUtils.outputInputField(FieldConstants.SOURCE_ADDRESS,BusinessAction.messageForKeyAdmin("labelSourceAddress"), true, 1, 30, itManaulBookings, "text", "col-sm-3","col-sm-5") %>
					</div>
					<div class="mb-3">
						
						<%=NewThemeUiUtils.outputInputField(FieldConstants.DESTINATION_ADDRESS,BusinessAction.messageForKeyAdmin("labelDestinationAddress"), true, 2, 100, itManaulBookings, "text", "col-sm-3","col-sm-5") %>
					</div>
					<div class="mb-3">
						<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VEHICLE_TYPE, BusinessAction.messageForKeyAdmin("labelVehicleType"), true, 1, itManaulBookings, "col-sm-3", "col-sm-5")%>
					</div>
					<div class="mb-3">
						<%=NewThemeUiUtils.outputInputField(FieldConstants.RECEIVER_NAME, BusinessAction.messageForKeyAdmin("labelReceiverName"), true, 1, 30, itManaulBookings, "text", "col-sm-3","col-sm-5")%>
					</div>
					<div class="mb-3">
						<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE,BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, itManaulBookings, "number", "col-sm-3","col-sm-5")%>
					</div>
					<div class="mb-3">
						<%=NewThemeUiUtils.outputInputField(FieldConstants.DELIVERY_ESTIMATE, BusinessAction.messageForKeyAdmin("labelDeliveryEstimate"), true, 1, 30, itManaulBookings, "number", "col-sm-3","col-sm-5")%>
					</div>
					<div class="text-center mt-3">
						<!-- Output buttons for order and cancel -->
						<%=NewThemeUiUtils.outputButtonDoubleField("btnOrder", BusinessAction.messageForKeyAdmin("labelOrder"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
					</div>
				</div>
				<div class="col-md-6 map-container">
					<!-- Output the Google Map field -->
					<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 40vh !important;")%>
					<!-- Google Maps API script -->
					<script type="text/javascript"
						src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
				</div>
			</div>
		</div>

		<div class="container content-container">
			<div class="modal fade" id="productPopup" tabindex="-1" role="dialog"
				aria-labelledby="productPopupLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<div class="float-left">
								<%=NewThemeUiUtils.outputButtonSingleField("btnAddProduct", BusinessAction.messageForKeyAdmin("labelViewCart"), NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT)%>
							</div>
							<!-- Updated close button -->
							<button id="closeModalBtn" class="btn btn-danger">
								
							close</button>
						</div>

						<div class="modal-body">
							<%
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductValue"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPrice"));
							NewThemeUiUtils.addQuantityColumn(columnNames);
							%>

							<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- End of Modal -->

		
	</div>

	<!-- Include Google Maps API script again if needed -->
	<script type="text/javascript"
		src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>

	
	<%@ include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
</body>
</html>
