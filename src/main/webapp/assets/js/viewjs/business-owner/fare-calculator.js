var sourcePlaceId = "";
var destinationPlaceId = "";
var sourcePlaceLat = "";
var sourcePlaceLng = "";
var destinationPlaceLat = "";
var destinationPlaceLng = "";
var markerSource = "";
var markerDestination = "";
var map = "";
var directionsService = new google.maps.DirectionsService();
var directionsDisplay;
var markers = [];

jQuery(document).ready(function() {

	jQuery(".dashboardWrapperLeftPanelMenuList li a").removeClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    jQuery("#18 a").addClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    
    currentLat = jQuery("#currentLat").val();
    currentLng = jQuery("#currentLng").val();
    
    initialize();
    mapInitialize();
    
    jQuery("#btnFirstVehicle .FirstVehicleImg").addClass('FirstVehicleSelect');
    jQuery("#btnFirstVehicle").addClass('leftButtonActiveClass');
    jQuery("#carType").val(FirstVehicleId);
    
    jQuery("#btnSave").click(function() {
    	
    	calculateFare();
    });
    
    jQuery("#btnCancel").click(function() {
    	
        document.location.href = basePath + "/fare-calculator.do";
    });
    
    jQuery(".carClass").click(function() {
    	
    	calculateFare();
    });
});

function initialize() {

    var sinput = document.getElementById("pickUpLocation");

    new google.maps.places.Autocomplete(sinput);
    
    var dinput = document.getElementById("destLocation");

    new google.maps.places.Autocomplete(dinput);
}

function mapInitialize() {

    var mapProp = {
        center: new google.maps.LatLng(currentLat, currentLng),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("fareMap"), mapProp);

    var source = document.getElementById("pickUpLocation");
    var destination = document.getElementById("destLocation");

    var autocompleteSource = new google.maps.places.Autocomplete(source);
    autocompleteSource.bindTo('bounds', map);

    var autocompleteDestination = new google.maps.places.Autocomplete(destination);
    autocompleteDestination.bindTo('bounds', map);

    autocompleteSource.addListener('place_changed', function() {

        var place = autocompleteSource.getPlace();

        if (!place.geometry) {
            window.alert("Invalid location. Please select the place from the suggestions.");
            return;
        }

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
    });

    autocompleteDestination.addListener('place_changed', function() {

        var place = autocompleteDestination.getPlace();

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

        var diconImage = basePath + "/assets/image/user_pin_2.png";
        
        markerDestination = new google.maps.Marker({
            map: map,
            icon: diconImage,
            anchorPoint: new google.maps.Point(0, -29)
        });

        markerDestination.setPosition(place.geometry.location);
        markerDestination.setVisible(true);
        
        markers.push(markerDestination);

        destinationPlaceLat = place.geometry.location.lat();
        destinationPlaceLng = place.geometry.location.lng();
        destinationPlaceId = place.place_id;
    });
}

function calculateFare() {

	jQuery("#pickUpLocationError").remove();
	jQuery("#pickUpLocation").parent().parent().removeClass("has-error");
		
	jQuery("#destLocationError").remove();
	jQuery("#destLocation").parent().parent().removeClass("has-error");
	
    driverListAjax = jQuery.ajax({
        url: basePath + "/fare-calculator/fare-details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "carTypeId": jQuery("#carType").val(),
            "sourcePlaceId": sourcePlaceId,
            "destinationPlaceId": destinationPlaceId
        },
        success: function(responseData) {

        	if (responseData.type == "Success") {

                jQuery("#distance").html(responseData.distance);
                jQuery("#total").html(responseData.total);
                
                for (var i = 0; i < markers.length; i++) {
                	
                    markers[i].setMap(null);
                }
                
                var smarker = new google.maps.Marker({
                    map: map,
                    icon: basePath + "/assets/image/user_pin_1.png",
                    position: new google.maps.LatLng(responseData.souceLat, responseData.souceLongi)
                });

                markers.push(smarker);
                
                var dmarker = new google.maps.Marker({
                    map: map,
                    icon: basePath + "/assets/image/user_pin_2.png",
                    position: new google.maps.LatLng(responseData.destiLat, responseData.destiLongi)
                });

                markers.push(dmarker);
                
                var polylineOptionsActual = new google.maps.Polyline({
                    strokeColor: jQuery("#themeColor").val(),
                    strokeOpacity: 1.0,
                    strokeWeight: 5
                });

                directionsDisplay = new google.maps.DirectionsRenderer({
                    suppressMarkers: true,
                    polylineOptions: polylineOptionsActual
                });

                var request = {
                    origin: new google.maps.LatLng(responseData.souceLat, responseData.souceLongi),
                    destination: new google.maps.LatLng(responseData.destiLat, responseData.destiLongi),
                    travelMode: google.maps.TravelMode["DRIVING"]
                };
                
                directionsService.route(request, function(response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        directionsDisplay.setDirections(response);
                    }
                });

                directionsDisplay.setMap(map);
        	} else {
        		validateFields(responseData);
        	}
        }
    });
}

function validateFields(data) {

    var sourceError = "";
    var destinationError = "";

    if ('sourcePlaceIdError' in data) {
    	sourceError = data.sourcePlaceIdError;
    }

    if ('destinationPlaceIdError' in data) {
    	destinationError = data.destinationPlaceIdError;
    }

    var sError = errorMessageFunction("pickUpLocation", sourceError);
    var dError = errorMessageFunction("destLocation", destinationError);
    
    if (sError || dError) {

        return false;
    }
}