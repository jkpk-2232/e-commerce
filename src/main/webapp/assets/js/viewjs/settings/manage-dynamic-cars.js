jQuery(document).ready(function() {

setMenuActiveSub("50");

    loadDatatable();

    jQuery("#btnAddCarType").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(ADD_URL);
    });
});

function loadDatatable() {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-dynamic-cars/list.json",
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
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}