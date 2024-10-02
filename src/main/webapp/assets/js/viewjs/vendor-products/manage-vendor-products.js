jQuery(document).ready(function() {

	setMenuActiveSub("33");

    jQuery("#btnAddProduct").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(ADD_URL);
    });

	jQuery("#vendorId").change(function() {
		getVendorStoresByVendorId(true, false);
		loadDatatable(startDateFormatted, endDateFormatted);
    });
    
    jQuery("#vendorStoreId").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });
    
	jQuery("#serviceId").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });
	
	initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);
});

function loadDatatable(tempStartDate, tempEndDate) {

	var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-vendor-products/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            }, {
                "name": "vendorStoreId",
                "value": jQuery("#vendorStoreId").val()
            }, {
                "name": "serviceId",
                "value": jQuery("#serviceId").val()
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