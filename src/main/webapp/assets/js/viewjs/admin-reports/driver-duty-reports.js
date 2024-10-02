jQuery(document).ready(function() {

    setMenuActiveSub("153");

    jQuery("#onOffStatus").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#btnExport").click(function() {
        redirectToUrl("/export/driver-duty-detail-reports.do?driverId=" + jQuery("#driverId").val() + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&onOffStatusId=" + jQuery("#onOffStatus").val() + "&logsDate=" + jQuery("#logsDate").val());
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
        "sAjaxSource": basePath + "/driver-duty-reports/list.json",
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
            }, {
                "name": "onOffStatus",
                "value": jQuery("#onOffStatus").val()
            }, {
                "name": "logsDate",
                "value": jQuery("#logsDate").val()
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
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}