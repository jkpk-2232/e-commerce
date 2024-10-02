jQuery(document).ready(function() {
	
	//setMenuActiveSub("71");

	setMenuActiveSub("58");
	
    initializeDateRangePicker("customDateRange");
    
    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        redirectToUrl("/export/warehouse-users-details.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted );
    });

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
        "sAjaxSource": basePath + "/manage-warehouse-users/list.json",
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
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "25%",
            "bSortable": false
        }, {
            "sWidth": "25%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "15%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }]
    });
    
    commonCssOverloadForDatatable("datatableDefault");
}