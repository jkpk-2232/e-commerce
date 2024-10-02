jQuery(document).ready(function() {

    setMenuActiveSub("152");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/manage-refund-reports.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&searchString=" + searchString);
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
        "sAjaxSource": basePath + "/manage-refund-reports/list.json",
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
            "sWidth": "5%",
            "bSortable": false
        }, {
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
            "sWidth": "25%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    jQuery.ajax({
        url: basePath + "/manage-refund-reports/refund-payable-info.json?startDate=" + tempStartDate + "&endDate=" + tempEndDate,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
            jQuery("#amountRefunded").html(responseData.amountRefunded);
        }
    });

    commonCssOverloadForDatatable("datatableDefault");
}