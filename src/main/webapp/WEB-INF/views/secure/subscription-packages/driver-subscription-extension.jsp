<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.viewutils.NewUiViewUtils"%>
<html lang="en" data-bs-theme="dark">

<head>
    <%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
    <title><%=BusinessAction.messageForKeyAdmin("labelDriverSubscriptionExtension")%></title>
  
 
</head>

<body>
    <%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>

    <!-- BEGIN #content -->
    <div id="content" class="app-content">
        <%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelDriverSubscriptionExtension"), UrlConstants.JSP_URLS.SUBSCRIBE_PACKAGE_ICON)%>

        <!-- BEGIN #formGrid -->
        <div id="formGrid" class="mb-5">
            <div class="card">
                <div class="card-body">
                    <form method="POST" id="driver-subscription-extension" name="driver-subscription-extension" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.DRIVER_SUBSCRIPTION_EXTENSTION_URL%>">
                        <%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MULTICITY_REGION_ID, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, it, "col-sm-3", "col-sm-5  ")%>
                        <%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DRIVER_ALL, BusinessAction.messageForKeyAdmin("labelAllDrivers"), true, 1, it, "col-sm-3", "col-sm-5  ")%>
                        <%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.DRIVER_LIST, BusinessAction.messageForKeyAdmin("labelDriver"), true, 1, it, "col-sm-3", "col-sm-5  ")%>
                        <%=NewThemeUiUtils.outputInputField(FieldConstants.PRICE, BusinessAction.messageForKeyAdmin("labelPrice"), true, 1, 30, it, "number", "col-sm-3", "col-sm-5  ")%>
                        <%=NewThemeUiUtils.outputSelectInputField(FieldConstants.DURATION_DAYS, BusinessAction.messageForKeyAdmin("labelDurationInDays"), true, 1, it, "col-sm-3", "col-sm-5  ")%>

                        <%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
                    </form>

                    <%=NewThemeUiUtils.outputLinSepartor()%>

                    <%=NewThemeUiUtils.outputDatatableTimeRangePicker("customDateRange", "col-sm-3")%>

                    <%=NewThemeUiUtils.outputLinSepartor()%>

                    <% 
                        List<String> columnNames = new ArrayList<>();
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelRegionName"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelJobCreatedBy"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelStatus"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelTotalDrivers"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelTotalCompletedDrivers"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelTotalFailedDrivers"));
                        columnNames.add(BusinessAction.messageForKeyAdmin("labelDateAndTime"));
                    %>

                    <%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
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
