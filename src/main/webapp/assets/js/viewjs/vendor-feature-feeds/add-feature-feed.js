var serviceId;
var currentLat = "";
var currentLng = "";
var map;
var storeAddressLat = "";
var storeAddressLng = "";
var storePlaceId = "";
var storeAddress = "";

jQuery(document).ready(function() {

    setMenuActive("44");

    // getVendorMonthlySubscriptionStatus();
    
    // jQuery("#btnSave").hide();
    
    currentLat = jQuery("#currentLat").val();
	currentLng = jQuery("#currentLng").val();
	
	console.log("*** current lat ***"+currentLat+"*** current lang ***"+currentLng);
	
	jQuery("#countOuterDiv").hide();
    jQuery('#storesCount').attr('readonly', true);
    jQuery('#ledCount').attr('readonly', true);
	
	initializeCurrentLocationMap();
	
	// initializeMap(currentLat, currentLng);

    initializeFileUpload("feedBaner", "hiddenFeedBanerImage_dummy", "hiddenFeedBanerImage");
    
    jQuery("#btnSave").click(function() {
        jQuery("#add-feed").submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    jQuery("#vendorId").change(function(e) {

        e.preventDefault();
		getVendorStoresByVendorId(false, false);
		getFilterDataBasedOnVendor();
		//getStoresCountAndLEDDisplayDeviceCount();
		// getFilterDataBasedOnVendor();
      //  getVendorMonthlySubscriptionStatus();
    });
    
	jQuery("#serviceId").change(function(e) {
		console.log("*** region list ****"+jQuery("#regionList").val());
		console.log("*** in service id function ****"+jQuery("#serviceId").val());
		serviceId = jQuery("#serviceId").val()
		getEstimatedCost();
		jQuery("#countOuterDiv").hide();
		getStoresCountAndLEDDisplayDeviceCount();
		
		
	});
	
	jQuery(function() {
		console.log('*** in start date ***');
		 jQuery("#startDate").datepicker({
        //jQuery("#startDate").bootstrapMaterialDatePicker({
		//	format: 'dd/mm/yyyy HH:mm:ss',
		
            maxDate: new Date(),
            // minDate: new Date(),
            format: 'dd/mm/yyyy',
          //  timeFormat: 'hh:mm',
         //   timepicker: true,
          //  format: 'dd/mm/yyyy HH:mm:ss',
          //  controls: ['calendar', 'time'],
         //   changeMonth: true,
          //  changeYear: true,
          //  showWeek: true,
          //  yearRange: "-100:-18"
        });
        /*
        jQuery("#startDate").timepicker({
			
			 controlType: 'select',  
            oneLine: true
			
        });
        */
        /*
		jQuery("#startDate").datetimepicker({
			
			maxDate: new Date(),
			format: 'dd/mm/yyyy HH:mm:ss',
			changeMonth: true,
            changeYear: true,
            showWeek: true,
            yearRange: "-100:-18"
			//format: 'dd/mm/yyyy HH:mm',
			moment().format('MMMM Do YYYY, h:mm:ss a'),
			lang: 'fr',
			weekStart: 1,
			cancelText: 'ANNULER',
			nowButton: true,
			switchOnClick: true

		});*/
		//jQuery("#startDate").moment().format('MMMM Do YYYY, h:mm:ss a')
    });
    
    
    
    jQuery(function() {
        jQuery("#endDate").datepicker({
            maxDate: new Date(),
            format: 'dd/mm/yyyy',
            changeMonth: true,
            changeYear: true,
            showWeek: true,
            yearRange: "-100:-18"
        });
    });
    
    jQuery("#startDate").change(function(e) {
		
		getEstimatedCost();
	});
	
	jQuery("#endDate").change(function(e) {
		
		getEstimatedCost();
	});
	
	jQuery("#regionList").change(function(e) {
		
		getEstimatedCost();
		
	});
	
	
	
});

function getEstimatedCost() {
	console.log("**** in get estimate fun service id ***"+ serviceId)
	 var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-feeds/get-estimated-cost.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
			"regionList": jQuery("#regionList").val(),
			"serviceId": serviceId.toString(),
			"startDate": jQuery("#startDate").val(),
			"endDate": jQuery("#endDate").val()
		},
		success: function(responseData) {
			console.log("*** response is ***"+responseData.estimatedCost)
            if (responseData.type === "ERROR") {
              //  jQuery("#vendorMonthlySubscriptionExpiredMessage").show();
            } else {
                console.log("*** estimatedin else  cost ***"+responseData.estimatedCost);
                jQuery("#estimatedCost").val(responseData.estimatedCost);
            }

            
        }
	});

}

function getStoresCountAndLEDDisplayDeviceCount() {
		vendorStoresData = '';
		// console.log('****  services count ****'+jQuery("#serviceId").val());
	var storesCountandLEDCount = jQuery.ajax({
		url: basePath + "/manage-vendor-store/vendor-stores-info-by-service.json?serviceIds="+jQuery("#serviceId").val(),
		type: 'GET',
		cache: false,
		dataType: 'json',
		timeOut: 50000,
		/*
		data: {
			"id": jQuery("#categoryId").val()
		},*/
		success: function(responseData) {
		
			if (responseData.length > 0) {
				vendorStoresData = responseData;
				getFilterData();
			} else {
				 console.log('*** in else condition ***');
				jQuery("#countOuterDiv").hide();
				var map = new google.maps.Map(document.getElementById('dashboardMapCanvas'), {
					zoom: 10,
					center: new google.maps.LatLng(currentLat, currentLng),
					mapTypeId: google.maps.MapTypeId.ROADMAP
				});
				
				marker = new google.maps.Marker({
					position: new google.maps.LatLng(currentLat, currentLng),
					map: map
				});
				google.maps.event.addListener(marker, 'click', (function(marker) {

					return function() {
						infowindow.setContent("test");
						infowindow.open(map, marker);
					}
				})(marker));
			 }
		}
	})
}

function getFilterData() {
	console.log("*** filter data ****")
	var storesCount = 0;
	var ledCount = 0;
	var map = new google.maps.Map(document.getElementById('dashboardMapCanvas'), {
		zoom: 10,
		center: new google.maps.LatLng(currentLat, currentLng),
		mapTypeId: google.maps.MapTypeId.ROADMAP
	});


	var infowindow = new google.maps.InfoWindow();

	var marker, i;

	for (i = 0; i < vendorStoresData.length; i++) {

		storesCount = storesCount + 1;
		ledCount = ledCount + vendorStoresData[i].ledDeviceCount;

		marker = new google.maps.Marker({
			position: new google.maps.LatLng(vendorStoresData[i].storeAddressLat, vendorStoresData[i].storeAddressLng),
			map: map
		});

		google.maps.event.addListener(marker, 'click', (function(marker, i) {
			var content = '<b>Store Name:</b>' + vendorStoresData[i].storeName + '<br>' +
				'<b>Store latitude:</b>' + vendorStoresData[i].storeAddressLat + '<br>' +
				'<b>Store langitude:</b>' + vendorStoresData[i].storeAddressLng + '<br>' +
				'<b>LED Count:</b>' + vendorStoresData[i].ledDeviceCount + '<br>';
			return function() {
				infowindow.setContent(content);
				infowindow.open(map, marker);
			}
		})(marker, i));
		jQuery("#countOuterDiv").show();
	}

	jQuery("#storesCount").val(storesCount);
	jQuery("#ledCount").val(ledCount);

}

function getFilterDataBasedOnVendor() {

	var storesCount = 0;
	var ledCount = 0;
	var map = new google.maps.Map(document.getElementById('dashboardMapCanvas'), {
		zoom: 10,
		center: new google.maps.LatLng(currentLat, currentLng),
		mapTypeId: google.maps.MapTypeId.ROADMAP
	});


	var infowindow = new google.maps.InfoWindow();

	var marker, i;

	for (i = 0; i < vendorStoresData.length; i++) {
		if (jQuery("#vendorId").val() === vendorStoresData[i].vendorId) {
			storesCount = storesCount + 1;
			ledCount = ledCount + vendorStoresData[i].ledDeviceCount;

			marker = new google.maps.Marker({
				position: new google.maps.LatLng(vendorStoresData[i].storeAddressLat, vendorStoresData[i].storeAddressLng),
				map: map
			});

			google.maps.event.addListener(marker, 'click', (function(marker, i) {
				var content = '<b>Store Name:</b>' + vendorStoresData[i].storeName + '<br>' +
					'<b>Store latitude:</b>' + vendorStoresData[i].storeAddressLat + '<br>' +
					'<b>Store langitude:</b>' + vendorStoresData[i].storeAddressLng + '<br>' +
					'<b>LED Count:</b>' + vendorStoresData[i].ledDeviceCount + '<br>';
				return function() {
					infowindow.setContent(content);
					infowindow.open(map, marker);
				}
			})(marker, i));

		}


	}
	jQuery("#countOuterDiv").show();
	jQuery("#storesCount").val(storesCount);
	jQuery("#ledCount").val(ledCount);

}




function initializeCurrentLocationMap() {
	console.log('in current location map fun');
	var map = new google.maps.Map(document.getElementById('dashboardMapCanvas'), {
		zoom: 10,
		center: new google.maps.LatLng(currentLat, currentLng),
		mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	marker = new google.maps.Marker({
		position: new google.maps.LatLng(currentLat, currentLng),
		map: map
	});
	google.maps.event.addListener(marker, 'click', (function(marker) {

		return function() {
			infowindow.setContent("test");
			infowindow.open(map, marker);
		}

	})(marker));

}

function codeLatLng(lat, lng) {

    var latlng = new google.maps.LatLng(lat, lng);

    geocoder.geocode({
        'latLng': latlng
    }, function(results, status) {

        if (status == google.maps.GeocoderStatus.OK) {

            if (results[1]) {

                storeAddress = results[1].formatted_address;
                storeAddressLat = results[1].geometry.location.lat();
                storeAddressLng = results[1].geometry.location.lng();
                storePlaceId = results[1].place_id;
                
               // jQuery("#storeAddress").val(storeAddress);
                jQuery("#storeAddressLat").val(storeAddressLat);
        		jQuery("#storeAddressLng").val(storeAddressLng);
        		jQuery("#storePlaceId").val(storePlaceId);
        
        		//console.log("storeAddress\t"+storeAddress);
			    console.log("storeAddressLat\t"+storeAddressLat);
			    console.log("storeAddressLng\t"+storeAddressLng);
			    console.log("storePlaceId\t"+storePlaceId);

            } else {
            	displayBootstrapMessage("No results found");
            }
        } else {
        	displayBootstrapMessage("Geocoder failed due to: " + status);
        }
    });
}

/*
// old implementation   commented
function getVendorMonthlySubscriptionStatus() {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-feeds/vendor-monthly-subscription-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "vendorId": jQuery("#vendorId").val()
        },
        success: function(responseData) {

            if (responseData.type === "ERROR") {
                jQuery("#vendorMonthlySubscriptionExpiredMessage").show();
            } else {
                jQuery("#vendorMonthlySubscriptionExpiredMessage").hide();
            }

            if (responseData.restrictAddFeed === "Yes") {
                jQuery("#btnSave").hide();
            } else {
                jQuery("#btnSave").show();
            }
        }
    });
}
*/