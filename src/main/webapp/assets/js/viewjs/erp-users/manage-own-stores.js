jQuery(document).ready(function() {
	setMenuActiveSub("54");

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
        "sAjaxSource": basePath + "/manage-own-stores/list.json", // Fetching static data from Java backend
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
            {"bSearchable": false, "bVisible": false}, // ID column hidden
            {"sWidth": "10%", "bSortable": false},     // SrNo
            {"sWidth": "25%", "bSortable": false},     // Vendor Name
            {"sWidth": "25%", "bSortable": false},     // Store Name
            {"sWidth": "15%", "bSortable": false},     // Service Type
            {"sWidth": "15%", "bSortable": false},     // Status
            {"sWidth": "10%", "bSortable": false}      // Action buttons
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
