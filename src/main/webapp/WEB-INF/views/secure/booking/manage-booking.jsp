<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

<head>
<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
<title><%=BusinessAction.messageForKeyAdmin("labelDashboard")%></title>

<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
 <style >
.modal-open #sidebar {
	display: none;
}
</style>
</head>

<body>

	<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>

	<!-- BEGIN #content -->
	<div id="content" class="app-content">

		<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDashboard" ), UrlConstants.JSP_URLS.MANAGE_DASHBOARD_ICON)%>

		<%
		Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
		String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);

		if (UserRoleUtils.isErpRole(roleIdLocal)) {
		%>

		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.ROLE_ID, it)%>
		
		
			

		<!-- BEGIN #formGrid -->
		<div id="formGrid" class="mb-5">

			<div class="card">

				<div class="card-body">
                     <div class="row">
                     
					       <div class="col-sm-7">
                              <%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_STORE_ID,BusinessAction.messageForKeyAdmin("labelBrands"), false, 1, it, "col-sm-3", "col-sm-7")%>
                            </div>
                            <div class="col-sm-5 d-flex justify-content-end">
                    <%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-5")%>
                </div>
                <%=NewThemeUiUtils.outputLinSepartor()%>
                
                      </div>

					<div class="row mb-5">

						<div class="col-md-4  d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelTotalSales")%></h4>
							<div class="flex-grow-1 d-flex">
								<%=NewThemeUiUtils.outputChart12("salesChart", " ", "bar", true, "100%", "100%")%>
							</div>
						</div>

						<div class="col-md-8 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelProduct")%></h4>
							<%
							List<String> columnNames = new ArrayList<>();
							columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductName"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductValue"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelProductInCart"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelCartValue"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPromoCode"));
							columnNames.add(BusinessAction.messageForKeyAdmin("labelPromoCodeDiscount"));
							%>


							<%=NewThemeUiUtils.dashboardDataTable12("datatableDefault", "", columnNames, true, "100%", "100%")%>


							<div id="datatableModal" class="modal fade" tabindex="-1"
								role="dialog" aria-labelledby="modalTitle" aria-hidden="true">
								<div class="modal-dialog modal-fullscreen">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title" id="modalTitle">
												<%=BusinessAction.messageForKeyAdmin("labelTotalProduct")%>
											</h5>
										</div>
										<button id="closeModalBtn" class="btn close"style="z-index: 999;">
											<i class="fa-solid fa-xmark"></i>
										</button>
										<div class="modal-body" style="padding: 0; height: 100%;">
											<%=NewThemeUiUtils.outputDatatable("datatableDefault_enlarged", "", columnNames)%>
										</div>
									</div>
								</div>
							</div>

						</div>

					</div>


					<%=NewThemeUiUtils.outputLinSepartor()%>

					<div class="row md-5">
						<div class="col-md-4">
							<a href="#totalSubscribers"> <%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_SUBSCRIBERS,
		BusinessAction.messageForKeyAdmin("labelTotalSubscribers"), it, "",
		UrlConstants.JSP_URLS.MANAGE_PASSENGER_ICON)%>
							</a>
						</div>
						<div class="col-md-4">
							<a href="#Feeds"> <%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEEDS, BusinessAction.messageForKeyAdmin("labelFeeds"),
		it, "", UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
							</a>
						</div>
						<div class="col-md-4">
							<a href="#Active Feeds"> <%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.ACTIVE_FEEDS,
		BusinessAction.messageForKeyAdmin("labelActiveFeeds"), it, "", UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
							</a>
						</div>
					</div>

					<%=NewThemeUiUtils.outputLinSepartor()%>

					<div class="row d-flex">
						<div class="col-md-4 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelSubscribersales")%></h4>
							<div class="flex-grow-1 d-flex">
								<%=NewThemeUiUtils.outputChart("salesChart1", "", "line")%>
							</div>
						</div>

						<!-- Static Boxes Section -->
						<div class="col-md-8 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelFeeds")%></h4>
							<!-- Container for all six static boxes -->
							<div class="card p-3 flex-grow-1">
								<div class="row">
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEED_WISHLIST_COUNT,
		BusinessAction.messageForKeyAdmin("labelWishList"), it, "", UrlConstants.JSP_URLS.MANAGE_WHISHLIST_ICON)%>
										</div>
									</div>
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEED_IMPRESSION_COUNT,
		BusinessAction.messageForKeyAdmin("labelImpressions"), it, "", UrlConstants.JSP_URLS.MANAGE_IMPRESSION_ICON)%>
										</div>
									</div>
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEED_VIEWS_COUNT,
		BusinessAction.messageForKeyAdmin("labelViews"), it, "", UrlConstants.JSP_URLS.MANAGE_VIEWS_ICON)%>
										</div>
									</div>
								</div>

								<!-- Second row of static boxes with top margin -->
								<div class="row mt-5">
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEED_LIKES_COUNT,
		BusinessAction.messageForKeyAdmin("labelLikes"), it, "", UrlConstants.JSP_URLS.MANAGE_lIKE_ICON)%>
										</div>
									</div>
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.COMMENTS_COUNT,
		BusinessAction.messageForKeyAdmin("labelComments"), it, "", UrlConstants.JSP_URLS.MANAGE_COMMENTS_ICON)%>
										</div>
									</div>
									<div class="col-md-4">
										<div class="mx-2 inner-box">
											<%=NewThemeUiUtils.dashboarStatisticsField(FieldConstants.FEED_SHARES_COUNT,
		BusinessAction.messageForKeyAdmin("labelShares"), it, "", UrlConstants.JSP_URLS.MANAGE_SHARE_ICON)%>
										</div>
									</div>
								</div>
								<%=NewThemeUiUtils.outputCardBodyArrows()%>
							</div>
						</div>
					</div>
					<div><%=NewThemeUiUtils.outputLinSepartor()%></div>

					<div class="row d-flex">

						<div class="col-md-4 d-flex flex-column">
							<h4><%=BusinessAction.messageForKeyAdmin("labelInventoryStores")%></h4>
							<div class="d-flex flex-grow-1">
								<div
									class="flex-grow-1 d-flex align-items-center justify-content-center">
									<%=NewThemeUiUtils.outputChart("inventoryChart", " ", "doughnut")%>
								</div>
							</div>
						</div>

						<div class="col-md-8">
							<h4><%=BusinessAction.messageForKeyAdmin("labelStoreLocations")%></h4>
							<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas",
		BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 50vh !important;")%>
						</div>
						<%-- <div class="col-md-6">
				            		<h4><%=BusinessAction.messageForKeyAdmin("labelFeeds")%> </h4>
				            		<div class="card">
					            		<div class="card-body">
						            		<table class="table table-borderless table-sm m-0">
						            			<tbody>
						            				<tr>
														<td><%=BusinessAction.messageForKeyAdmin("labelViews")%></td>
														<td><%=it.get(FieldConstants.FEED_VIEWS_COUNT)%></td>
													</tr>
													<tr>
														<td><%=BusinessAction.messageForKeyAdmin("labelLikes")%></td>
														<td><%=it.get(FieldConstants.FEED_LIKES_COUNT)%></td>
													</tr>
													<tr>
														<td><%=BusinessAction.messageForKeyAdmin("labelComments")%></td>
														<td><%=it.get(FieldConstants.COMMENTS_COUNT)%></td>
													</tr>
						            			</tbody>
						            		</table>
					            		</div>
				            			<%=NewThemeUiUtils.outputCardBodyArrows()%>
				            		</div>
				            	</div> --%>
					</div>


				</div>
				<%=NewThemeUiUtils.outputCardBodyArrows()%>

			</div>

		</div>
		<!-- END #formGrid -->



		<%
		} else {
		%>

		<!-- BEGIN #formGrid -->
		<div id="formGrid" class="mb-5">

			<div class="card">

				<div class="card-body">

					<%=NewThemeUiUtils.outputLinSepartor()%>

					<div class="row">
                <!-- First static column with multiple fields -->
                <div class="col-sm-4">
                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_BOOKINGS_COUNT,
                    BusinessAction.messageForKeyAdmin("labelTotalBookingsByDay"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.BOOKINGS_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_PEOPLE_OPENEED_APP_COUNT,
                    BusinessAction.messageForKeyAdmin("labelTotalConnectedUsers"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.MANAGE_PASSENGER_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.AVAILABLE_DRIVERS_COUNT,
                    BusinessAction.messageForKeyAdmin("labelTotalNumberOfAvailableDrivers"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.MANAGE_DRIVER_ICON) %>
                </div>

                <!-- Second column -->
                <div class="col-sm-4">
                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.ORDER_VALUE,BusinessAction.messageForKeyAdmin("labelActiveOrders"), it, "col-sm-12",UrlConstants.JSP_URLS.ORDER_VALUE_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_ORDER_VALUE,
                    BusinessAction.messageForKeyAdmin("labelOrderValue"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.VENDOR_PROFILE_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TRANSPORTATION_ONLINE,
                    BusinessAction.messageForKeyAdmin("labelTransportationOnline"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.VENDOR_PROFILE_ICON) %>
                </div>

                <!-- Third column -->
                <div class="col-sm-4">
                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_TRANSPORTATION_VALUE,
                    BusinessAction.messageForKeyAdmin("labelTransportationValue"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.BOOKINGS_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_ERP_USERS,
                    BusinessAction.messageForKeyAdmin("labelTotalErpusers"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.ERP_USERS_ICON) %>

                    <%= NewThemeUiUtils.dashboarStatisticsField(FieldConstants.TOTAL_WAREHOUSE_SLOTS_BOOKED,
                    BusinessAction.messageForKeyAdmin("labelTotalWarehouseSlotsBooked"), it, "col-sm-12",
                    UrlConstants.JSP_URLS.WARE_HOUSE_SLOT_BOOKED_ICON) %>
                </div>
            </div>
					<%=NewThemeUiUtils.outputLinSepartor()%>

					<%
					LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
					filterOptionIds.put(FieldConstants.REGION_LIST, true);
					%>

					<%=NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-4")%>

					<%=NewThemeUiUtils.outputLinSepartor()%>

					<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas",
		BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>

					<%=NewThemeUiUtils.outputFormHiddenField("currentLat", it)%>
					<%=NewThemeUiUtils.outputFormHiddenField("currentLng", it)%>
					<%=NewThemeUiUtils.outputFormHiddenField("isFirstTimeHidden", it)%>

				</div>
				

				<%=NewThemeUiUtils.outputCardBodyArrows()%>

			</div>

		</div>
		<!-- END #formGrid -->

		<%
		}
		%>

	</div>
	<!-- END #content -->

	<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>

</body>

</html>