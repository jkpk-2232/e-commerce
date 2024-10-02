jQuery(document).ready(function() {

    setMenuActiveSub("11");

    jQuery("#btnAddSurgePrice").click(function(e) {

        e.preventDefault();

        redirectToUrl(ADD_URL);
    });

    jQuery("#surgeFilter").change(function() {
        loadDatatable();
    });

    jQuery("#regionList").change(function() {
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
        "sAjaxSource": basePath + "/manage-surge-price/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "surgeFilter",
                "value": jQuery("#surgeFilter").val()
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
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "25%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}