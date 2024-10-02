jQuery(document).ready(function() {

    setMenuActiveSub("15");

    loadDatatable();
    
    jQuery("#btnAddRentalPackage").click(function(e) {

        e.preventDefault();

        redirectToUrl(ADD_URL);
    });

    jQuery("#regionList").change(function() {
        loadDatatable();
    });

    jQuery("#vendorId").change(function() {
        loadDatatable();
    });

    jQuery("#rentalPackageType").change(function() {
        loadDatatable();
    });
});

function loadDatatable() {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-rental-packages/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "regionList",
                "value": jQuery("#regionList").val()
            }, {
                "name": "rentalPackageType",
                "value": jQuery("#rentalPackageType").val()
            }, {
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
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
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "15%",
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
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "8%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}