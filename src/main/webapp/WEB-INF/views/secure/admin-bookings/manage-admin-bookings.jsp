<!DOCTYPE html>
<%@ page import="com.webapp.FieldConstants" %>
<%@ page import="com.webapp.viewutils.NewThemeUiUtils" %>
<html lang="en" data-bs-theme="dark">

<head>
    <%@ include file="/WEB-INF/views/includes/new-theme-common-head.jsp" %>
    <title><%= BusinessAction.messageForKeyAdmin("labelAdminBookingsHeader") %></title>
    <style>
        #option {
            font-weight: normal;
            display: block;
            padding-block-start: 0px;
            padding-block-end: 1px;
            min-block-size: 1.2em;
            padding-inline: 2px;
            white-space: nowrap;
            background-color: black;
        }
    </style>
</head>

<body>

    <%@ include file="/WEB-INF/views/includes/new-theme-header.jsp" %>

    <!-- BEGIN #content -->
    <div id="content" class="app-content">

        <%= NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAdminBookingsHeader"), 
            UrlConstants.JSP_URLS.ADMIN_BOOKINGS_ICON) %>

        <!-- BEGIN #formGrid -->
        <div id="formGrid" class="mb-5">

            <div class="card">

                <div class="card-body">

                    <%= NewThemeUiUtils.outputButtonSingleField("btnExport", 
                        BusinessAction.messageForKeyAdmin("labelExport"), 
                        NewThemeUiUtils.BUTTON_FLOAT_RIGHT, NewThemeUiUtils.BUTTON_FLOAT_RIGHT) %>

                    <%= NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3") %>

                    <%= NewThemeUiUtils.outputLinSepartor() %>

                    <%
                        LinkedHashMap<String, Boolean> filterOptionIds = new LinkedHashMap<>();
                        filterOptionIds.put(FieldConstants.USERS, false);
                        filterOptionIds.put(FieldConstants.STATUS_FILTER, false);
                        filterOptionIds.put(FieldConstants.SURGE_FILTER, false);
                        filterOptionIds.put(FieldConstants.VENDOR_ID, false);
                    %>

                    <%= NewThemeUiUtils.outputSelectInputFieldForFilters(filterOptionIds, it, "col-sm-2") %>

                    <%= NewThemeUiUtils.outputLinSepartor() %>

                    <%
                        List<String> columnNames = new ArrayList<>();
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelTripId"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverName"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelPassengerName"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelBookingBy"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelBookingType"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelVendorName"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelInvoiceAmount"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelMarkupAmount"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelSurgePrice"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelAmountCollected"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelPaymentMode"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelTripStatus"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelServiceType"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelAction"));
                    %>

                    <%= NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames) %>

                    <%= NewThemeUiUtils.outputFormHiddenField(FieldConstants.IS_EXPORT_ACCESS, it) %>

                </div>

                <%= NewThemeUiUtils.outputCardBodyArrows() %>

            </div>

        </div>
        <!-- END #formGrid -->

    </div>
    <!-- END #content -->

    <%@ include file="/WEB-INF/views/includes/new-theme-footer.jsp" %>

</body>

</html>
