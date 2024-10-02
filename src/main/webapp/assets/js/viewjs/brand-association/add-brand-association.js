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
	
	setMenuActiveSub("61");
	
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


	jQuery("#btnSave").click(function(e) {

		e.preventDefault();

		document.forms["add-brand-association"].submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
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