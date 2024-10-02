jQuery(document).ready(function() {

    setMenuActiveSub("12");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        document.forms["edit-city-surge"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
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
        "sAjaxSource": basePath + "/add-city-surge/list.json",
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
                jQuery("#cityName").val(json.cityName);
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
            "sWidth": "25%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}