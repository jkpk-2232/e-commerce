var map = "";
var markers = [];
var autocompleteSource = "";

jQuery(document).ready(function() {

    setMenuActiveSub("23");
    setMenuActiveSub("btnAdminContactUs");
    
    initialize();

    mapInitialize();

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["admin-contact-us"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});

function initialize() {

    var sinput = document.getElementById("areaName");

    new google.maps.places.Autocomplete(sinput);
}

function mapInitialize() {

    var mapProp = {
        center: new google.maps.LatLng(jQuery("#latitude").val(), jQuery("#longitude").val()),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapProp);

    var sLatlng = new google.maps.LatLng(jQuery("#latitude").val(), jQuery("#longitude").val());
    var siconImage = basePath + "/assets/image/user_pin_1.png";

    var smarker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: sLatlng
    });

    markers.push(smarker);

    var areaName = document.getElementById("areaName");

    autocompleteSource = new google.maps.places.Autocomplete(areaName);
    autocompleteSource.bindTo("bounds", map);

    autocompleteSource.addListener("place_changed", function() {
        getArea();
    });
}

function getArea() {

    var place = autocompleteSource.getPlace();

    if (!place.geometry) {
        window.alert("Invalid Location!");
        return;
    }

    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(17);
    }

    var marker;

    for (var j = 0; j < markers.length; j++) {
        markers[j].setMap(null);
    }

    markers.length = 0;

    jQuery("#latitude").val(place.geometry.location.lat());
    jQuery("#longitude").val(place.geometry.location.lng());

    var sLatlng = new google.maps.LatLng(jQuery("#latitude").val(), jQuery("#longitude").val());
    var siconImage = basePath + "/assets/image/user_pin_1.png";

    var smarker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: sLatlng
    });

    markers.push(smarker);
}