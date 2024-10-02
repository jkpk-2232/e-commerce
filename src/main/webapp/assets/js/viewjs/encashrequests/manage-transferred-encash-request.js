jQuery(document).ready(function() {

    setMenuActiveSub("303");

    jQuery("#btnExport").click(function() {

    	var requestsType = "TRANSFERRED";
    	var searchString = jQuery("#datatableDefault_filter > label > input").val();

        redirectToUrl("/export/encash-requests/reports.do?requestsType=" + requestsType + "&searchString=" + searchString + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted);
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
        "sAjaxSource": basePath + "/encash-requests/transferred/list.json",
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
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "17%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}