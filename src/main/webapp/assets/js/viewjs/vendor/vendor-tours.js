jQuery(document).ready(function() {

    setMenuActive("21");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
    	
    	var searchString = jQuery("#datatableDefault_filter > label > input").val();
    	
        redirectToUrl("/export/vendor-bookings.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&searchString=" + searchString);
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
        "sAjaxSource": basePath + "/vendor/tours/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
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
            "sWidth": "0%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "11%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "9%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}