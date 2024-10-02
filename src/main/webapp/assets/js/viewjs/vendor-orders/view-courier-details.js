var map;
var markers = [];
var latlngbounds = new google.maps.LatLngBounds();

jQuery(document).ready(function() {

	setMenuActiveSub("5");

	initializeMap();
});

function initializeMap() {

	var sLatLng = new google.maps.LatLng(jQuery("#sLatitude").val(), jQuery("#sLongitude").val());
    var dLatLng = new google.maps.LatLng(jQuery("#dLatitude").val(), jQuery("#dLongitude").val());
    
    var latlng = [
	    sLatLng,
	    dLatLng,
	];
	
	for (var i = 0; i < latlng.length; i++) {
	    latlngbounds.extend(latlng[i]);
	}

    var mapOptions = {
        zoom: 15,
        center: dLatLng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };
    
    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);
    
    var siconImage = basePath + "/assets/image/user_pin_1.png";
    var smarker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: sLatLng
    });

    var diconImage = basePath + "/assets/image/user_pin_2.png";
    var dmarker = new google.maps.Marker({
        map: map,
        icon: diconImage,
        position: dLatLng
    });

	markers.push(smarker);
    markers.push(dmarker);
    
    map.fitBounds(latlngbounds);
}