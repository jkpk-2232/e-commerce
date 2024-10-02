jQuery(document).ready(function() {

  //  setMenuActive("21");
  setMenuActiveSub("50");
    setMenuActiveSub("btnVendorAirport");

    jQuery("#regionList").change(function() {
        loadDatatable();
    });

    jQuery("#status").change(function() {
        loadDatatable();
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
        "sAjaxSource": basePath + "/manage-vendor-airport-regions/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "status",
                "value": jQuery("#status").val()
            }, {
                "name": "regionList",
                "value": jQuery("#regionList").val()
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
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}