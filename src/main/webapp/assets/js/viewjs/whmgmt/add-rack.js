jQuery(document).ready(function() {

	jQuery("#btnAddRack").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-rack").submit();
	});
	
	loadDatatable('','');
	
});

function loadDatatable(tempStartDate, tempEndDate) {

	var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/add-rack/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            },{
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            }, {
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