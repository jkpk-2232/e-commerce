<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">
<head>
    <%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
    <title>Cloud Store Booking System</title>
    <script type="text/javascript"
        src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>

    <style>
        .carousel {
            width: 100%;
            margin: 0 auto;
        }
        .rack-container {
            text-align: center;
        }
        .rack-location {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .rack-table {
            margin: 0 auto;
            border: 1px solid white;
            width: 100%;
        }
        .rack-table table {
            width: 100%;
            border-collapse: collapse;
        }
        .rack-table td {
            border: 1px solid #ddd;
            text-align: center;
            padding: 10px 25px;
        }
        /* Ensure map and rack tables are displayed side by side */
        #map {
            height: 50vh;
            width: 100%;
        }
        #rackTablesContainer {
            height: 50vh;
            overflow-y: auto;
        }

        /* Legend styles aligned in a straight line */
        #legend {
            margin-top: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 50%; /* Adjust the width as needed */
        }

        .legend-item {
            display: flex;
            align-items: center;
        }

        .legend-color {
            width: 20px;
            height: 20px;
            margin-right: 10px;
        }

        /* Custom styles for the previous and next buttons */
        .navigation-buttons {
            margin-top: 20px;
            text-align: center;
        }

        .btn-custom {
            background-color: orange;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            margin: 5px;
        }

        .btn-custom:hover {
            background-color: darkorange;
        }
    </style>
</head>
<body>
    <%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>

    <div id="content" class="app-content">
        <div class="row">
            <div class="col-lg-6 col-md-6">
                <div id="map"></div> <!-- Google Map Container -->
            </div>
            <div class="col-lg-6 col-md-6">
                <h3 id="warehouse_name"></h3>

                <!-- Rack Tables Carousel -->
                <div id="rackCarousel" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-inner" id="rackTablesContainer">
                        <!-- Carousel items will be appended here -->
                    </div>
                </div>

                <!-- Legend Section (aligned in a straight line) -->
                <div id="legend">
                    <div class="legend-item">
                        <div class="legend-color" style="background-color: transparent; border: 1px solid #ddd;"></div>
                        <span>Available</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color" style="background-color: orange;"></div>
                        <span>Sold</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color" style="background-color: green;"></div>
                        <span>Selected</span>
                    </div>
                </div>

                <!-- Navigation Buttons Below the Table and Legend -->
                <div class="navigation-buttons">
                    <button class="btn-custom" data-bs-target="#rackCarousel" data-bs-slide="prev">Previous</button>
                    <button class="btn-custom" data-bs-target="#rackCarousel" data-bs-slide="next">Next</button>
                </div>
            </div>
        </div>
    </div>
    
     <%
                    List<String> columnNames1 = new ArrayList<>();
                    columnNames1.add(BusinessAction.messageForKeyAdmin("labelCloudStore"));
                    columnNames1.add(BusinessAction.messageForKeyAdmin("labelNoOfSlots"));
                    columnNames1.add(BusinessAction.messageForKeyAdmin("labelPricePerSlot"));
                    columnNames1.add(BusinessAction.messageForKeyAdmin("labelTotal"));
                    %>
                    <div class="mt-4">
                        <%=NewThemeUiUtils.outputDatatable12("datatableDefault", "", columnNames1)%>
                    </div>

    <%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
</body>
</html>
