jQuery(document).ready(function() {

    setMenuActiveSub("153");

    jQuery("#onOffStatus").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/driver-duty-report.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&onOffStatusId=" + jQuery("#onOffStatus").val() + "&searchString=" + searchString);
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
        "sAjaxSource": basePath + "/manage-driver-duty-reports/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "onOffStatus",
                "value": jQuery("#onOffStatus").val()
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
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "13%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });

    jQuery.ajax({
        url: basePath + "/manage-driver-duty-reports/driver-duty-info.json?startDate=" + tempStartDate + "&endDate=" + tempEndDate,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
            jQuery("#allDriverTotalPayableAmount").html(responseData.allDriverTotalPayableAmount);
        }
    });

    commonCssOverloadForDatatable("datatableDefault");
}