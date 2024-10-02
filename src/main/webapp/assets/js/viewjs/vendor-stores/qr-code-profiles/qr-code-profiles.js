jQuery(document).ready(function() {

   // setMenuActiveDoubleTab("21", "42");
   
   setMenuActiveSub("50");
   
  
    jQuery("#btnAddQrCodeProfile").click(function(e) {

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
        "sAjaxSource": basePath + "/qr-code-profiles/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push( {
                "name": "vendorStoreId",
                "value": jQuery("#vendorStoreId").val()
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
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}