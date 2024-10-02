jQuery(document).ready(function() {

    setMenuActiveSub("151");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/driver-bookings.do?driverId=" + jQuery("#driverId").val() + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&searchString=" + searchString);
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
        "sAjaxSource": basePath + "/manage-driver-bookings/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {

            aoData.push({
                "name": "driverId",
                "value": jQuery("#driverId").val()
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
            "sWidth": "1%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "13%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "9%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "8%",
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
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }]
    });

    jQuery.ajax({
        url: basePath + "/manage-driver-bookings/driver-payable-info.json?driverId=" + jQuery("#driverId").val() + "&startDate=" + tempStartDate + "&endDate=" + tempEndDate,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
        	jQuery("#settlementAmount").html(responseData.settlementAmount);
        }
    });

    commonCssOverloadForDatatable("datatableDefault");
}