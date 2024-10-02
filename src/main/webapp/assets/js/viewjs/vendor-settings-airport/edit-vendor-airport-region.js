var sourcePlaceId = "";
var sourcePlaceLat = "";
var sourcePlaceLng = "";
var sourcePlaceCountry = "";
var markerSource = "";
var map = "";
var autocompleteSource = "";
var markers = [];
var flightPlanCoordinates = [];
var polygons = [];
var mymarkersforPolygon = [];
var latlongStr = "";

var bound = new google.maps.LatLngBounds();

var polygonPoints = "";

jQuery(document).ready(function() {

//	setMenuActive("21");
  setMenuActiveSub("50");
	setMenuActiveSub("btnVendorAirport");

    initialize();

    if (jQuery("#areaLatitude").val() != "") {
        mapInitialize(jQuery("#areaLatitude").val(), jQuery("#areaLongitude").val());
    } else {
        getMapLoad();
    }

    if (jQuery("#latlong").val() != "") {
        polygonPoints = jQuery("#latlong").val();
        plotPolygonEdit();
    }

    jQuery("#btnSave").click(function(e) {

    	e.preventDefault();
    	
        var address = jQuery("#address").val();
        if (latlongStr === "" || latlongStr.split(",").length < 4) {
            displayBootstrapMessage("At least three points required.");
        } else {
            validLocation(address);
        }
    });
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    jQuery("#btnClearPoly").click(function(e) {

        e.preventDefault();

        clearPolygon();

        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }

        markers.length = 0;

        jQuery("#address").val("");
    });
});

function initialize() {
    var sinput = document.getElementById("address");
    new google.maps.places.Autocomplete(sinput);
}

function mapInitialize(currentLat, currentLng) {

    var mapProp = {
        center: new google.maps.LatLng(currentLat, currentLng),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapProp);

    addListnerToMap(map);

    if (jQuery("#areaLatitude").val() != "") {

        var siconImage = basePath + "/assets/image/user_pin_1.png";

        markerSource = new google.maps.Marker({
            map: map,
            icon: siconImage,
            position: new google.maps.LatLng(jQuery("#areaLatitude").val(), jQuery("#areaLongitude").val())
        });

        markers.push(markerSource);

        sourcePlaceLat = jQuery("#areaLatitude").val();
        sourcePlaceLng = jQuery("#areaLongitude").val();
        sourcePlaceId = jQuery("#areaPlaceId").val();
    }

    var source = document.getElementById("address");
    var autocompleteSource = new google.maps.places.Autocomplete(source);
    autocompleteSource.bindTo('bounds', map);

    autocompleteSource.addListener('place_changed', function() {

        var place = autocompleteSource.getPlace();
        
        if (!place.geometry) {
            window.alert("Invalid location. Please select the place from the suggestions.");
            return;
        }

        clearPolygon();

        for (var i = 0; i < markers.length; i++) {

            markers[i].setMap(null);
        }

        markers.length = 0;

        var siconImage = basePath + "/assets/image/user_pin_1.png";

        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);
        }

        markerSource = new google.maps.Marker({
            map: map,
            icon: siconImage,
            anchorPoint: new google.maps.Point(0, -29)
        });

        markerSource.setPosition(place.geometry.location);
        markerSource.setVisible(true);

        markers.push(markerSource);

        sourcePlaceLat = place.geometry.location.lat();
        sourcePlaceLng = place.geometry.location.lng();
        sourcePlaceId = place.place_id;

        var res = sourcePlaceCountry.split(",");

        jQuery("#areaCountry").val(res[res.length - 1]);
        jQuery("#areaLatitude").val(sourcePlaceLat);
        jQuery("#areaLongitude").val(sourcePlaceLng);
        jQuery("#areaPlaceId").val(sourcePlaceId);
    });
}

function plotPolygonEdit() {

    var polypoints = [];
    var polyLatitude = [];
    var polyLongitude = [];
    var polygonLatLong = polygonPoints.split(/[ ,]+/);
    var latitude = "";
    var longitude = "";

    polygonLatLong = polygonLatLong.slice(0);

    for (i = 0; i <= (polygonPoints.length); i += 2) {
        polyLatitude.push(polygonLatLong[i]);
    }

    for (i = 1; i <= (polygonPoints.length); i += 2) {
        polyLongitude.push(polygonLatLong[i]);
    }

    for (i = 0; i < (polygonLatLong.length / 2); i++) {

        latitude = polyLatitude[i];
        longitude = polyLongitude[i];

        if ((latitude != null) && (latitude != "") && (latitude != "undefined") && (longitude != null) && (longitude != "") && (longitude != "undefined")) {

            polypoints.push({

                lat: parseFloat(latitude),
                lng: parseFloat(longitude)
            });

            latlongStr += latitude + " " + longitude + ", ";

            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(latitude, longitude),
                map: map,
                title: 'Start'
            });

            mymarkersforPolygon.push(marker);

            drawPolygon(map, new google.maps.LatLng(latitude, longitude));
        }
    }
}

function getMapLoad() {

    jQuery.ajax({
        url: basePath + "/add-airport-region/load-map.json?multicityCityRegionId=" + jQuery("#multicityCityRegionId").val(),
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        success: function(responseData) {
        	mapInitialize(responseData.cityLatitude, responseData.cityLogitude);
        }
    });
}

function validLocation(address) {

    var geocoder = new google.maps.Geocoder();

    geocoder.geocode({
        'address': address
    }, function(results, status) {
    	
        if (jQuery("#address").val() != '' && status == google.maps.GeocoderStatus.OK) {
            jQuery("#latlong").val(latlongStr);
            document.forms["edit-vendor-airport-region"].submit();
        } else {
            displayBootstrapMessage("Enter valid address");
        }
    });
}