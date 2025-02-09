jQuery(document).ready(function() {

    setMenuActiveSub("404");

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        var userType = "vendorDrivers"; //Possible values drivers/vendors/vendorDrivers
        var searchString = jQuery("#datatableDefault_filter > label > input").val();
        
        redirectToUrl("/export/user-account/reports.do?userType=" + userType + "&searchString=" + searchString);
    });

    loadDatatable();
});

function loadDatatable() {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-vendor-drivers/account/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push();
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json)
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
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "14%",
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
            "sWidth": "11%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}