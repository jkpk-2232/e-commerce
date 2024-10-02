<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="java.util.HashMap"%>

<html lang="en" data-bs-theme="dark">
<head>
    <%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
   
</head>
<body>
    <%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
    <!-- BEGIN #content -->
    <div id="content" class="app-content">
        <%
            HashMap itManaulBookings = (HashMap) request.getAttribute("it");
        %>
        <%
	HashMap itLocalVendorStoreFields = (HashMap) request.getAttribute("it");
        %>
            

                    <div class="row">
                        <div class="col-md-12">
                            <%= NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelCart"), "") %>
                           
                        </div>
                    </div>
                    
                    <div class="row">
                        <!-- Render the table -->
                        <div class="col-md-7">
                            <%
                                List<String> columnNames = new ArrayList<>();
                                columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
                                columnNames.add(BusinessAction.messageForKeyAdmin("labelProductName"));
                                columnNames.add(BusinessAction.messageForKeyAdmin("labelProductValue"));
                                columnNames.add(BusinessAction.messageForKeyAdmin("labelPrice"));
                                NewThemeUiUtils.addQuantityColumn(columnNames);
                            %>
                            <%= NewThemeUiUtils.dashboardDataTable("datatableDefault", "", columnNames, true, "300px", "100%") %> 
                        </div>

                        <div class="col-md-5">
                            <h4><%= BusinessAction.messageForKeyAdmin("labelReceiverDetails") %></h4>
                            <table class="table">
                                <tbody>
                                    <tr>
                                        <td><%= BusinessAction.messageForKeyAdmin("labelDriver") %></td>
                                        <td id="driverName"></td>
                                        <td id="driverPhone"></td>
                                    </tr>
                                    <tr>
                                        <td><%= BusinessAction.messageForKeyAdmin("labelPassenger") %></td>
                                        <td id="customerName"></td>
                                        <td id="customerPhoneNo"></td>
                                    </tr>
                                    <tr>
                                        <td><%= BusinessAction.messageForKeyAdmin("labelShippingInformation") %></td>
                                        <td id="orderDeliveryAddress"></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Input fields and Google Map section -->
                    <div class="row">
                        <div class="col-md-8">
                            <div class="row">
                                <div class="col-md-6">
                                    <%= NewThemeUiUtils.outputInputField("totalItems", BusinessAction.messageForKeyAdmin("labelTotalItems"), true, 1, 30, itManaulBookings, "number", "col-sm-12", "col-sm-12") %>
                                </div>
                                <div class="col-md-6">
                                <%= NewThemeUiUtils.outputInputField("productActualPrice", BusinessAction.messageForKeyAdmin("labelProductActualPrice"), true, 1, 30, itManaulBookings, "number", "col-sm-12", "col-sm-12") %>
                                   
                                </div>
                                <div class="col-md-6">
                                 <%= NewThemeUiUtils.outputInputField("productDiscountedPrice", BusinessAction.messageForKeyAdmin("labelProductDiscountedPrice"), true, 1, 30, itManaulBookings, "number", "col-sm-12", "col-sm-12") %>
                                </div>
                                <div class="col-md-6">
                                 <%= NewThemeUiUtils.outputInputField("estimatedCost", BusinessAction.messageForKeyAdmin("labelEstimatedCost"), true, 1, 30, itManaulBookings, "number", "col-sm-12", "col-sm-12") %>
                                </div>
                               
                                <div class="col-md-8">
                                   <%= NewThemeUiUtils.outputInputField("grossPrice", BusinessAction.messageForKeyAdmin("labelGrossPrice"), true, 1, 30, itManaulBookings, "number", "col-sm-12", "col-sm-12") %>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="google-map-section">
                              <%= NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 30vh !important;") %> 
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%= itManaulBookings.get("google_map_key").toString() %>&sensor=false&libraries=places"></script>
    <%@ include file="/WEB-INF/views/includes/new-theme-change-theme.jsp" %>
    <%@ include file="/WEB-INF/views/includes/new-theme-footer.jsp" %>
</body>
</html>
