jQuery(document).ready(function() {

    setMenuActiveSub("156");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();

        redirectToUrl("/export/driver/refer-benefits/trips-reports.do?driverId=" + jQuery("#driverId").val() + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted);
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
        "sAjaxSource": basePath + "/driver/refer-benefit/trips-reports/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "driverId",
                "value": jQuery('#driverId').val()
            });
            $.getJSON(sSource, aoData, function(json) {
                jQuery("#benefitAmount").html(json.name);
                fnCallback(json);
            });
        },
        "aoColumns": [{
            "sWidth": "1%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}