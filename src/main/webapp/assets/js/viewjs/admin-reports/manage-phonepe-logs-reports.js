jQuery(document).ready(function() {
	
	setMenuActiveSub("159");
	
	jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        redirectToUrl("/export/phonepe-log-reports.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted);
    });
    
    initializeDateRangePicker("customDateRange");
    
    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });
    
    jQuery("#phonepeStatus").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
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
        "sAjaxSource": basePath + "/phonepe/logs-reports/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "phonepeStatus",
                "value": jQuery("#phonepeStatus").val()
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
            });
        },
        "aoColumns": [{
            "sWidth": "1%",
            "bVisible": false,
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
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}