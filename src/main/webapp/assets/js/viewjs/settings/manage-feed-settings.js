jQuery(document).ready(function() {

    setMenuActiveSub("13");

 	console.log('in jquery function');

    jQuery("#btnAddFeedSettings").click(function(e) {

        e.preventDefault();

        redirectToUrl(ADD_URL);
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
        "sAjaxSource": basePath + "/manage-feed-settings/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
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
        },  {
            "sWidth": "8%",
            "bSortable": false
        }]
    });

    jQuery("#datatableDefault").removeClass("text-nowrap");
}