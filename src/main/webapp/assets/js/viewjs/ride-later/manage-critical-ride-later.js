jQuery(document).ready(function() {
	
	
 //   setMenuActiveDoubleTab("18", "26");
 
      setMenuActiveSub("45");
	
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
        "sAjaxSource": basePath + "/manage-critical-ride-later/list.json",
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
            "sWidth": "15%",
            "bVisible": false,
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "20%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "12%",
            "bSortable": false
        }, {
            "sWidth": "14%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "6%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}