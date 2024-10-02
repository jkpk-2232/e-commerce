var currentLat = "";
var currentLng = "";
var map;
var storeAddressLat = "";
var storeAddressLng = "";
var storePlaceId = "";
var sLatlng;
var markers = [];
var smarker;

jQuery(document).ready(function() {

	//setMenuActiveDoubleTab("21", "42");
	setMenuActiveSub("50");
	
	setMultiSelectPicker("type");

    storeAddressLat = jQuery("#storeAddressLat").val();
    storeAddressLng = jQuery("#storeAddressLng").val();
    storePlaceId = jQuery("#storePlaceId").val();

    if (jQuery("#storeAddress").val() != '') {
        currentLat = jQuery("#storeAddressLat").val();
        currentLng = jQuery("#storeAddressLng").val();
    } else {
        currentLat = jQuery("#currentLat").val();
        currentLng = jQuery("#currentLng").val();
    }

    initialize();

    initializeMap(currentLat, currentLng);

    initializeFileUpload("storeImage", "hiddenStoreImage_dummy", "hiddenStoreImage");
    
    jQuery("#specificDaysDiv").hide();
    jQuery("#daysOfWeekDiv").show();
    
    initTimePicker("dateOpeningMorningHours");
    initTimePicker("dateClosingMorningHours");
    initTimePicker("dateOpeningEveningHours");
    initTimePicker("dateClosingEveningHours");
    
    initTimePicker("dateOpeningMorningHours0");
    initTimePicker("dateClosingMorningHours0");
    initTimePicker("dateOpeningEveningHours0");
    initTimePicker("dateClosingEveningHours0");
    
    initTimePicker("dateOpeningMorningHours1");
    initTimePicker("dateClosingMorningHours1");
    initTimePicker("dateOpeningEveningHours1");
    initTimePicker("dateClosingEveningHours1");
    
    initTimePicker("dateOpeningMorningHours2");
    initTimePicker("dateClosingMorningHours2");
    initTimePicker("dateOpeningEveningHours2");
    initTimePicker("dateClosingEveningHours2");
    
    initTimePicker("dateOpeningMorningHours3");
    initTimePicker("dateClosingMorningHours3");
    initTimePicker("dateOpeningEveningHours3");
    initTimePicker("dateClosingEveningHours3");
    
    initTimePicker("dateOpeningMorningHours4");
    initTimePicker("dateClosingMorningHours4");
    initTimePicker("dateOpeningEveningHours4");
    initTimePicker("dateClosingEveningHours4");
    
    initTimePicker("dateOpeningMorningHours5");
    initTimePicker("dateClosingMorningHours5");
    initTimePicker("dateOpeningEveningHours5");
    initTimePicker("dateClosingEveningHours5");
    
    initTimePicker("dateOpeningMorningHours6");
    initTimePicker("dateClosingMorningHours6");
    initTimePicker("dateOpeningEveningHours6");
    initTimePicker("dateClosingEveningHours6");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

//        jQuery("#dateOpeningMorningHours").prop("readonly", false);
//        jQuery("#dateClosingMorningHours").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours").prop("readonly", false);
//        jQuery("#dateClosingEveningHours").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours0").prop("readonly", false);
//        jQuery("#dateClosingMorningHours0").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours0").prop("readonly", false);
//        jQuery("#dateClosingEveningHours0").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours1").prop("readonly", false);
//        jQuery("#dateClosingMorningHours1").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours1").prop("readonly", false);
//        jQuery("#dateClosingEveningHours1").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours2").prop("readonly", false);
//        jQuery("#dateClosingMorningHours2").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours2").prop("readonly", false);
//        jQuery("#dateClosingEveningHours2").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours3").prop("readonly", false);
//        jQuery("#dateClosingMorningHours3").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours3").prop("readonly", false);
//        jQuery("#dateClosingEveningHours3").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours4").prop("readonly", false);
//        jQuery("#dateClosingMorningHours4").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours4").prop("readonly", false);
//        jQuery("#dateClosingEveningHours4").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours5").prop("readonly", false);
//        jQuery("#dateClosingMorningHours5").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours5").prop("readonly", false);
//        jQuery("#dateClosingEveningHours5").prop("readonly", false);
//
//        jQuery("#dateOpeningMorningHours6").prop("readonly", false);
//        jQuery("#dateClosingMorningHours6").prop("readonly", false);
//        jQuery("#dateOpeningEveningHours6").prop("readonly", false);
//        jQuery("#dateClosingEveningHours6").prop("readonly", false);

        document.forms["add-vendor-store"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    if (jQuery("#dateType").val() == "1") {
        jQuery("#specificDaysDiv").hide();
        jQuery("#daysOfWeekDiv").show();
    } else {
        jQuery("#specificDaysDiv").show();
        jQuery("#daysOfWeekDiv").hide();
    }

    jQuery("#dateType").change(function() {

        if (jQuery("#dateType").val() == "1") {
            jQuery("#specificDaysDiv").hide();
            jQuery("#daysOfWeekDiv").show();
        } else {
            jQuery("#specificDaysDiv").show();
            jQuery("#daysOfWeekDiv").hide();
        }
    });
    
    if (jQuery("#type").val().includes("2")) {
		jQuery("#showWarehouseFields").show();
	} else {
		jQuery("#showWarehouseFields").hide();
	}
	
	jQuery('#type').on('sp-change', function() {

       if (jQuery("#type").val().includes("2")) {
			jQuery("#showWarehouseFields").show();
		} else {
			jQuery("#showWarehouseFields").hide();
		}
    });

    if (jQuery("#numberOfShifts").val() == "1") {

        jQuery("#dateOpeningMorningHoursLabel").text("Opening Hours");
        jQuery("#dateClosingMorningHoursLabel").text("Closing Hours");

        jQuery("#dateOpeningEveningHoursParent").hide();
        jQuery("#dateClosingEveningHoursParent").hide();

        jQuery("#dateOpeningEveningHours0Parent").hide();
        jQuery("#dateClosingEveningHours0Parent").hide();

        jQuery("#dateOpeningEveningHours1Parent").hide();
        jQuery("#dateClosingEveningHours1Parent").hide();

        jQuery("#dateOpeningEveningHours2Parent").hide();
        jQuery("#dateClosingEveningHours2Parent").hide();

        jQuery("#dateOpeningEveningHours3Parent").hide();
        jQuery("#dateClosingEveningHours3Parent").hide();

        jQuery("#dateOpeningEveningHours4Parent").hide();
        jQuery("#dateClosingEveningHours4Parent").hide();

        jQuery("#dateOpeningEveningHours5Parent").hide();
        jQuery("#dateClosingEveningHours5Parent").hide();

        jQuery("#dateOpeningEveningHours6Parent").hide();
        jQuery("#dateClosingEveningHours6Parent").hide();

    } else {

        jQuery("#dateOpeningMorningHoursLabel").text("Morning Opening Hours");
        jQuery("#dateClosingMorningHoursLabel").text("Morning Closing Hours");

        jQuery("#dateOpeningEveningHoursParent").show();
        jQuery("#dateClosingEveningHoursParent").show();

        jQuery("#dateOpeningEveningHours0Parent").show();
        jQuery("#dateClosingEveningHours0Parent").show();

        jQuery("#dateOpeningEveningHours1Parent").show();
        jQuery("#dateClosingEveningHours1Parent").show();

        jQuery("#dateOpeningEveningHours2Parent").show();
        jQuery("#dateClosingEveningHours2Parent").show();

        jQuery("#dateOpeningEveningHours3Parent").show();
        jQuery("#dateClosingEveningHours3Parent").show();

        jQuery("#dateOpeningEveningHours4Parent").show();
        jQuery("#dateClosingEveningHours4Parent").show();

        jQuery("#dateOpeningEveningHours5Parent").show();
        jQuery("#dateClosingEveningHours5Parent").show();

        jQuery("#dateOpeningEveningHours6Parent").show();
        jQuery("#dateClosingEveningHours6Parent").show();
    }

    jQuery("#numberOfShifts").change(function() {

        if (jQuery("#numberOfShifts").val() == "1") {

            jQuery("#dateOpeningMorningHoursLabel").text("Opening Hours");
            jQuery("#dateClosingMorningHoursLabel").text("Closing Hours");

            jQuery("#dateOpeningEveningHoursParent").hide();
            jQuery("#dateClosingEveningHoursParent").hide();

            jQuery("#dateOpeningEveningHours0Parent").hide();
            jQuery("#dateClosingEveningHours0Parent").hide();

            jQuery("#dateOpeningEveningHours1Parent").hide();
            jQuery("#dateClosingEveningHours1Parent").hide();

            jQuery("#dateOpeningEveningHours2Parent").hide();
            jQuery("#dateClosingEveningHours2Parent").hide();

            jQuery("#dateOpeningEveningHours3Parent").hide();
            jQuery("#dateClosingEveningHours3Parent").hide();

            jQuery("#dateOpeningEveningHours4Parent").hide();
            jQuery("#dateClosingEveningHours4Parent").hide();

            jQuery("#dateOpeningEveningHours5Parent").hide();
            jQuery("#dateClosingEveningHours5Parent").hide();

            jQuery("#dateOpeningEveningHours6Parent").hide();
            jQuery("#dateClosingEveningHours6Parent").hide();

        } else {

            jQuery("#dateOpeningMorningHoursLabel").text("Morning Opening Hours");
            jQuery("#dateClosingMorningHoursLabel").text("Morning Closing Hours");

            jQuery("#dateOpeningEveningHoursParent").show();
            jQuery("#dateClosingEveningHoursParent").show();

            jQuery("#dateOpeningEveningHours0Parent").show();
            jQuery("#dateClosingEveningHours0Parent").show();

            jQuery("#dateOpeningEveningHours1Parent").show();
            jQuery("#dateClosingEveningHours1Parent").show();

            jQuery("#dateOpeningEveningHours2Parent").show();
            jQuery("#dateClosingEveningHours2Parent").show();

            jQuery("#dateOpeningEveningHours3Parent").show();
            jQuery("#dateClosingEveningHours3Parent").show();

            jQuery("#dateOpeningEveningHours4Parent").show();
            jQuery("#dateClosingEveningHours4Parent").show();

            jQuery("#dateOpeningEveningHours5Parent").show();
            jQuery("#dateClosingEveningHours5Parent").show();

            jQuery("#dateOpeningEveningHours6Parent").show();
            jQuery("#dateClosingEveningHours6Parent").show();
        }
    });

    var fromDateMoment;
    var toDateMoment;
    
    if (jQuery("#fromDate").val() != '' && jQuery("#toDate").val() != '') {
    	fromDateMoment = moment.unix(jQuery("#fromDate").val());
    	toDateMoment = moment.unix(jQuery("#toDate").val());
    } else {
    	fromDateMoment = moment();
    	toDateMoment = moment();
    }

	jQuery("#reportrange span").html(fromDateMoment.format(DATATABLE_FORMAT_DATE) + ' - ' + toDateMoment.format(DATATABLE_FORMAT_DATE));

    jQuery("#reportrange").daterangepicker({
        "showDropdowns": true,
        "autoApply": true,
        "startDate": fromDateMoment,
        "endDate": toDateMoment
    }, function(start, end, label) {
        jQuery("#reportrange span").html(start.format(DATATABLE_FORMAT_DATE) + ' - ' + end.format(DATATABLE_FORMAT_DATE));
        jQuery("#fromDate").val(start.unix());
        jQuery("#toDate").val(end.unix());
    });
});

function initialize() {

    var sinput = document.getElementById("storeAddress");

    new google.maps.places.Autocomplete(sinput);

    geocoder = new google.maps.Geocoder();

    sLatlng = new google.maps.LatLng(currentLat, currentLng);

    mapOptions = {
        zoom: 15,
        center: sLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);

    var source = document.getElementById("storeAddress");

    var autocompleteSource = new google.maps.places.Autocomplete(source);
    autocompleteSource.bindTo("bounds", map);

    autocompleteSource.addListener("place_changed", function() {

        console.log("Removing marker");

        for (i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }

        var place = autocompleteSource.getPlace();

        if (!place.geometry) {
            window.alert("Invalid location. Please select the place from the suggestions.");
            return;
        }

        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);
        }

        storeAddressLat = place.geometry.location.lat();
        storeAddressLng = place.geometry.location.lng();
        storePlaceId = place.place_id;

        jQuery("#storeAddressLat").val(storeAddressLat);
        jQuery("#storeAddressLng").val(storeAddressLng);
        jQuery("#storePlaceId").val(storePlaceId);

        console.log("storeAddressLat\t" + storeAddressLat);
        console.log("storeAddressLng\t" + storeAddressLng);
        console.log("storePlaceId\t" + storePlaceId);

        sLatlng = new google.maps.LatLng(storeAddressLat, storeAddressLng);

        smarker = new google.maps.Marker({
            map: map,
            draggable: true,
            icon: basePath + "/assets/image/user_pin_1.png",
            position: sLatlng
        });

        google.maps.event.addListener(smarker, 'dragend', function() {
            codeLatLng(smarker.position.lat(), smarker.position.lng());
        });

        console.log("Setting marker");

        markers.push(smarker);
    });
}

function initializeMap(lat, lng) {

    sLatlng = new google.maps.LatLng(lat, lng);

    mapOptions = {
        zoom: 15,
        center: sLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);

    smarker = new google.maps.Marker({
        map: map,
        draggable: true,
        icon: basePath + "/assets/image/user_pin_1.png",
        position: sLatlng
    });

    google.maps.event.addListener(smarker, 'dragend', function() {
        codeLatLng(smarker.position.lat(), smarker.position.lng());
    });

    console.log("Setting marker");

    markers.push(smarker);
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

                jQuery("#storeAddress").val(storeAddress);
                jQuery("#storeAddressLat").val(storeAddressLat);
                jQuery("#storeAddressLng").val(storeAddressLng);
                jQuery("#storePlaceId").val(storePlaceId);

            } else {
                displayBootstrapMessage("No results found");
            }
        } else {
            displayBootstrapMessage("Geocoder failed due to: " + status);
        }
    });
}