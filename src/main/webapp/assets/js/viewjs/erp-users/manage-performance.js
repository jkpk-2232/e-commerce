jQuery(document).ready(function() {
	//setMenuActiveSub("56");

	
	setMenuActiveSub("51");
	
    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);

});

function loadDatatable(tempStartDate, tempEndDate) {

    jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-performance/list.json", // Fetching static data from Java backend
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
            });
        },
        "aoColumns": [
            {"bSearchable": false, "bVisible": false}, 
            {"sWidth": "10%", "bSortable": false},    
            {"sWidth": "25%", "bSortable": false},     
            {"sWidth": "25%", "bSortable": false},     
            {"sWidth": "15%", "bSortable": false},     
            {"sWidth": "15%", "bSortable": false},     
            {"sWidth": "10%", "bSortable": false}, 
			{"sWidth": "10%", "bSortable": false},  
			{"sWidth": "10%", "bSortable": false}, 
			{"sWidth": "10%", "bSortable": false}
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
