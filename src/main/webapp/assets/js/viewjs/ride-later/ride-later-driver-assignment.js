jQuery(document).ready(function() {

    jQuery("#btnCancelPopUp").click(function() {
        jQuery("#rideLaterDriverAssignmentModal").modal("hide");
    });

    jQuery(document).on('click', '.rideLaterAssign', function(e) {

        e.preventDefault();

        tourType = "Assign";
        tourId = this.id.split("_")[1];

        getDriverListForRideLater(tourType, tourId);
    });

    jQuery(document).on('click', '.rideLaterReAssign', function(e) {

        e.preventDefault();

        tourType = "Reassign";
        tourId = this.id.split("_")[1];

        getDriverListForRideLater(tourType, tourId);
    });

    jQuery(document).on('click', '.rideLaterDriverAssign', function(e) {

        e.preventDefault();

        tourId = this.id.split("_")[1];
        driverId = this.id.split("_")[2];

        assignDriverForRideLater(tourType, tourId, driverId);
    });
});

function assignDriverForRideLater(tourType, tourId, driverId) {

    var driverListAjax = jQuery.ajax({
        url: basePath + "/manage-ride-later/assign-driver.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "tourType": tourType,
            "tourId": tourId,
            "driverId": driverId
        },
        success: function(responseData) {
        	if (responseData.type == TYPE_SUCCESS) {
        		displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + jQuery("#SUCCESS_PAGE_REDIRECT_URL").val());
        	} else {
        		displayBootstrapMessage(responseData.message);
        	}
        }
    });
}

function getDriverListForRideLater(tourType, tourId) {

	console.log("calling datatableRideLaterDriverAssignment datatable");
    
    var taxiDatatable = jQuery("#datatableRideLaterDriverAssignment").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-ride-later/ride-later-driver-list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "tourType",
                "value": tourType
            }, {
                "name": "tourId",
                "value": tourId
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
                jQuery("#sourceAddressSpan").html(json.sourceAddress);
                jQuery("#destinationAddressSpan").html(json.destinationAddress);
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
        }]
    });

    commonCssOverloadForDatatable("datatableRideLaterDriverAssignment");
    
    jQuery("#rideLaterDriverAssignmentModal").modal("show");
}