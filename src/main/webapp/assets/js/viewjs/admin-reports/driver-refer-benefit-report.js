jQuery(document).ready(function() {

    setMenuActiveSub("156");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        redirectToUrl("/export/driver/refer-benefits-reports.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted);
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
        "sAjaxSource": basePath + "/driver/refer-benefit/reports/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
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
            "sWidth": "1%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}