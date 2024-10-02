jQuery(document).ready(function() {
    setMenuActive("60");

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
        "sAjaxSource": basePath + "/manage-onboarding/list.json",
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
            {"sWidth": "15%", "bSortable": false},     // Employee Name
            {"sWidth": "15%", "bSortable": false},     // Brand Name
            {"sWidth": "15%", "bSortable": false},     // Vendor Name
            {"sWidth": "10%", "bSortable": false},     // Phone Number
            {"sWidth": "10%", "bSortable": false},     // Email ID
            {"sWidth": "15%", "bSortable": false},     // Store Name
            {"sWidth": "10%", "bSortable": false}      // Action buttons
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
