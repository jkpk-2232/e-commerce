jQuery(document).ready(function() {

    setMenuActive("5");
   // setMenuActiveSub("63");
   
    hideShowExportButton();
    
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

    jQuery("#btnCancelPopUp").click(function() {
    	hidePopUp();
    });

    jQuery("#btnSubmitPopUp").click(function() {

        var driverId = jQuery("#driverList").val();
        var passengerId = jQuery("#passengerId").val();

        if (driverId === "-1") {
            displayBootstrapMessage("Select driver from list.");
            hidePopUp();
            return;
        }

        updateReferrer(passengerId, driverId);
    });

    jQuery(document).on('click', '.referrerDetailsSpan', function(e) {

        e.preventDefault();

        jQuery("#passengerId").val(this.id);

        updateDriverList(jQuery("#passengerId").val());
    });

    jQuery("#btnExport").click(function(e) {

        e.preventDefault();
        
        redirectToUrl("/export/passenger-details.do?startDate=" + startDateFormatted + "&endDate=" + endDateFormatted + "&status=" + jQuery("#status").val());
    });
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-passengers/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "status",
                "value": jQuery("#status").val()
            }, {
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            }, {
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

function hidePopUp() {
	jQuery("#changeAttachDriverDialog").modal("hide");
}

function showPopUp() {
	jQuery("#changeAttachDriverDialog").modal("show");
}

function updateDriverList(passengerId) {

    jQuery.ajax({
        url: basePath + "/manage-passengers/driver-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "passengerId": passengerId
        },
        success: function(responseData) {

            var driverListOptionHtml = "<option value='-1'>Select Driver</option>";

            if (responseData.type === TYPE_SUCCESS) {

                if (responseData.driverList.length > 0) {

                    for (var k = 0; k < responseData.driverList.length; k++) {
                        driverListOptionHtml += "<option value='" + responseData.driverList[k].driverId + "'>" + responseData.driverList[k].driverName + ", " + responseData.driverList[k].driverEmail + "</option>";
                    }
                }
            }

            showPopUp();
            
            jQuery("#driverList").html(driverListOptionHtml).trigger("chosen:updated");
        }
    });
}

function updateReferrer(passengerId, driverId) {

    jQuery.ajax({
        url: basePath + "/manage-passengers/update-referrer.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            passengerId: passengerId,
            driverId: driverId
        },
        success: function(responseData) {

        	hidePopUp();
        	
            if (responseData.type == TYPE_SUCCESS) {
                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manage-passengers.do")
            } else {
                displayBootstrapMessage(responseData.message);
            }
        }
    });
}