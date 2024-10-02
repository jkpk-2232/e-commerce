jQuery(document).ready(function() {

	//setMenuActiveSub("72");
	
	setMenuActiveSub("59");
	
	initializeDateRangePicker("customDateRange");
	
	jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
		reinitializeStartEndDate(picker.startDate, picker.endDate);
		loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
	});

	loadDatatable(startDateFormatted, endDateFormatted);
	
	jQuery("#btnAddAdminUser").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(ADD_URL);
	});
});

function loadDatatable(tempStartDate, tempEndDate) {
	
	var taxiDatatable = jQuery("#datatableDefault").dataTable({
		"bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-admin-users/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            },{
                "name": "endDate",
                "value": tempEndDate
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
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
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "30%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });
	
	commonCssOverloadForDatatable("datatableDefault");
}