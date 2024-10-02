jQuery(document).ready(function() {

    setMenuActiveSub("153");

    jQuery("#btnExport").click(function() {
        redirectToUrl("/export/driver/loggedin/time-report.do?driverId=" + jQuery("#driverId").val() + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted);
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
        "sAjaxSource": basePath + "/driver/loggedin/time-report/list.json",
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
            });
        },
        "aoColumns": [{
            "sWidth": "15%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "40%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}