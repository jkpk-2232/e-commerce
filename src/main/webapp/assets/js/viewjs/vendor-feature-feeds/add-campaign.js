jQuery(document).ready(function() {

	setMenuActiveSub("39");
	
	setMultiSelectPicker("AD");
	//setMultiSelectPicker("city");
	
	
	jQuery("#scheduleStartDate").datepicker({
        startDate: new Date(),
        format: 'dd-mm-yyyy'
    });
    
    jQuery("#scheduleEndDate").datepicker({
        startDate: new Date(),
        format: 'dd-mm-yyyy'
    });
    
    initTimePicker("scheduleStartTime");
    initTimePicker("scheduleEndTime");
    

	jQuery("#btnSave").click(function(e) {

		e.preventDefault();

		jQuery("#add-campaign").submit();
	});

	jQuery("#btnCancel").click(function(e) {

		e.preventDefault();

		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	jQuery("#city").click(function(e) {
		getLocations(jQuery("#city").val());

	});
	
	jQuery("#location").click(function(e) {
		getStores(jQuery("#location").val());

	});
	
	jQuery("#store").click(function(e) {
		getDevices(jQuery("#store").val());

	});


});

function getLocations(city) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-store/get-locations.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "city": jQuery("#city").val(),
            "allOption": true
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.locationOptions);
        	jQuery("#location").children('option').remove();
  	       	jQuery("#location").append(responseData.locationOptions);
  	       	jQuery("#location").trigger("chosen:updated");
        }
    });
}

function getStores(location) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-campaigns/get-stores.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "locationId": jQuery("#location").val(),
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.storeOptions);
        	jQuery("#store").children('option').remove();
  	       	jQuery("#store").append(responseData.storeOptions);
  	       	jQuery("#store").trigger("chosen:updated");
        }
    });
}

function getDevices(store) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-campaigns/get-devices.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "storeId": jQuery("#store").val(),
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.deviceOptions);
        	jQuery("#device").children('option').remove();
  	       	jQuery("#device").append(responseData.deviceOptions);
  	       	jQuery("#device").trigger("chosen:updated");
        }
    });
}
