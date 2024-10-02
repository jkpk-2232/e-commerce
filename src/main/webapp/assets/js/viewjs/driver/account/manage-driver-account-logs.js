jQuery(document).ready(function() {

    if ("vendorAccount" === jQuery("#urlCallFrom").val()) {

        setMenuActiveSub("402");

    } else if ("vendorDriverAccount" === jQuery("#urlCallFrom").val()) {

        setMenuActiveSub("403");

    } else {

        setMenuActiveSub("401");
    }

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();

        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/user-account/logs-reports.do?userId=" + jQuery("#userIdHidden").val() + "&startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&searchString=" + searchString);
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
        "sAjaxSource": basePath + "/manage-drivers/account-logs/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "userId",
                "value": jQuery("#userIdHidden").val()
            }, {
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json)
            });
        },
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "17%",
            "bSortable": false
        }, {
            "sWidth": "21%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}