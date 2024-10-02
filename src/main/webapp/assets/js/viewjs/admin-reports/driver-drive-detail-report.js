jQuery(document).ready(function() {

    setMenuActiveSub("155");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/driver-drive-detail-report.do?driverId=" + driverId + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&searchString=" + searchString);
    });

    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/driver-km-detail-reports/list.json",
        "bDestroy": true,
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "driverId",
                "value": jQuery("#driverId").val()
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
                jQuery("#driverTotalDistance").text(json.totalDistance);
            });
        },
        "aoColumns": [{
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}