jQuery(document).ready(function() {

	//setMenuActiveDoubleTab("6", "23");
	setMenuActiveSub("48");

    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);

    jQuery("#status").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#vendorId").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#approvel").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });

    jQuery("#btnAddDriver").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(ADD_URL);
    });
    
    jQuery("#btnDriverSubscriptionExtension").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(jQuery("#OTHER_PAGE_URL").val());
    });
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-drivers/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "status",
                "value": jQuery("#status").val()
            }, {
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            }, {
                "name": "approvel",
                "value": jQuery("#approvel").val()
            }, {
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
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
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "7%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });
    
    commonCssOverloadForDatatable("datatableDefault");
}