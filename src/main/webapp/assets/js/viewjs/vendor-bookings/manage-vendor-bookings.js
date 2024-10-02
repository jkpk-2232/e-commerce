jQuery(document).ready(function() {

 //   setMenuActive("22");
 setMenuActiveSub("50");
    hideShowExportButton();

    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);

    jQuery("#statusFilter").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#surgeFilter").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();

        var statusFilter = jQuery("#statusFilter").val();
        var surgeFilter = jQuery("#surgeFilter").val();
        var searchString = jQuery("#datatableDefault_filter > label > input").val();

        redirectToUrl("/export/vendor-bookings.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&statusFilter=" + statusFilter + "&surgeFilter=" + surgeFilter + "&searchString=" + searchString);
    });
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/vendor-bookings/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "statusFilter",
                "value": jQuery("#statusFilter").val()
            }, {
                "name": "surgeFilter",
                "value": jQuery("#surgeFilter").val()
            }, {
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
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
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}