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
	
	</head>
	
	<body>
	
	    <%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
	
	    <!-- BEGIN #content -->
	    <div id="content" class="app-content">
	
	        <%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelMarketShares"), UrlConstants.JSP_URLS.MANAGE_MARKET_SHARE_DASHBOARD_ICON)%>
	
	        <!-- BEGIN #formGrid -->
	        <div id="formGrid" class="mb-5">
	
	            <div class="card">
	                <div class="card-body">
	                    <div class="row">
	                        
	                        <div >
	                            <%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>
	                        </div>
	                    </div>
	                    <%=NewThemeUiUtils.outputLinSepartor()%>
	
	                    <!-- Charts Section -->
	                    <div class="row">
	                        <!-- Bar Chart with reduced height -->
	                        <div class="col-md-7 d-flex flex-column">
	                            <h4><%=BusinessAction.messageForKeyAdmin("labelRevenue")%></h4>
	                            <div class="flex-grow-1 d-flex">
	                                <%=NewThemeUiUtils.outputChart("salesChart", " ", "bar")%>
	                            </div>
	                        </div>
	
	                        <!-- Doughnut Chart with reduced height -->
	                        <div class="col-md-5 d-flex flex-column">
	                            <h4><%=BusinessAction.messageForKeyAdmin("labelInventoryStores")%></h4>
	                            <div class="flex-grow-1 d-flex align-items-center justify-content-center">
	                                <%=NewThemeUiUtils.outputChart("inventoryChart", " ", "doughnut")%>
	                            </div>
	                        </div>
	                    </div>
	
	                    <!-- Google Map Section -->
	                    <div class="row mt-5">
	                        <div class="col-md-12">
	                            <h4><%=BusinessAction.messageForKeyAdmin("labelStoreLocations")%></h4>
	                            <%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas",
	                            BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 60vh !important;")%>
	                        </div>
	                    </div>
	                </div>
	                <%=NewThemeUiUtils.outputCardBodyArrows()%>
	            </div>
	
	        </div>
	        <!-- END #formGrid -->
	
	    <!-- END #content -->
	
	    <%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
	
	</body>
	
	</html>
