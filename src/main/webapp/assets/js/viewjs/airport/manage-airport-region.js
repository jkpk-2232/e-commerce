jQuery(document).ready(function() {

    setMenuActiveSub("10");

    jQuery("#btnAddAirportRegion").click(function(e) {

        e.preventDefault();

        redirectToUrl(ADD_URL);
    });

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
        "sAjaxSource": basePath + "/manage-airport-regions/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "status",
                "value": jQuery("#status").val()
            }, {
                "name": "regionList",
                "value": jQuery("#regionList").val()
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