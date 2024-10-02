jQuery(document).ready(function() {

    setMenuActiveSub("41");

	loadDatatable();
    
    jQuery("#vendorId").change(function() {
		getVendorStoresByVendorId(true, false);
		loadDatatable();
    });
    
    jQuery("#vendorStoreId").change(function() {
		loadDatatable();
    });
    
});

function loadDatatable() {

	var slotsDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-rack-slot-booking/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            }, {
                "name": "vendorStoreId",
                "value":  jQuery("#vendorStoreId").val()
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
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
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
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });
	
	commonCssOverloadForDatatable("datatableDefault");
}