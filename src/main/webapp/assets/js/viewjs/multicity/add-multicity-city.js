var markerSource = "";
var map = "";
var autocompleteSource = "";
var circle = "";
var markers = [];

jQuery(document).ready(function() {

    setMenuActiveSub("9");

    initialize();

    mapInitialize();

    selectCarAvailabilityWithServiceTypeId(jQuery("#errorDataAvailableCarList").val());

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        var carAvailableList = [];
        jQuery("input:checked").each(function() {
        	console.log("$(this).val()\t" + $(this).attr("name"));
        	carAvailableList.push($(this).attr("name"));
        });
        carAvailableList.join("&&");
        console.log(carAvailableList);

        var kvpairs = [];
        var form = document.forms.addMultiCity;
        for (var i = 0; i < form.elements.length; i++) {
            var e = form.elements[i];
            if ((e.name != null && e.name != "") && (e.value != null && e.value != "")) {
                if (e.name == "multicityCountryId" || e.name == "areaPlaceId" || e.name == "areaLatitude" ||
                    e.name == "areaLongitude" || e.name == "areaRadius" || e.name == "areaCountry" ||
                    e.name == "areaName" || e.name == "radius" || e.name == "areaDisplayName" ||
                    (e.name.includes("_IsAvailable") && e.value.includes("_IsAvailable"))) {

                } else {
                    kvpairs.push(encodeURIComponent(e.name) + "=" + encodeURIComponent(e.value));
                }
            }
        }

        var queryString = kvpairs.join("&&&");

        console.log(queryString);
        jQuery("#queryString").val(queryString);
        jQuery("#carAvailableList").val(carAvailableList);
        console.log("carAvailableList\t\t" + carAvailableList);

        document.forms["addMultiCity"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    jQuery("#radius").change(function() {

        if (jQuery("#areaName").val() == "") {

            displayBootstrapMessage("Please select region first.");

        } else {

            getArea();
        }
    });
});

function initialize() {
    var sinput = document.getElementById("areaName");
    new google.maps.places.Autocomplete(sinput);
}

function mapInitialize() {

    var mapProp = {
        center: new google.maps.LatLng(jQuery("#currentLat").val(), jQuery("#currentLng").val()),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapProp);

    var areaName = document.getElementById("areaName");

    autocompleteSource = new google.maps.places.Autocomplete(areaName);
    autocompleteSource.bindTo("bounds", map);

    autocompleteSource.addListener("place_changed", function() {
        getArea();
    });
}

function getArea() {

    if (markers) {

        for (i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }

        markers.length = 0;
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

    markerSource = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    markerSource.setIcon(({
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(35, 35)
    }));

    markerSource.setPosition(place.geometry.location);
    markerSource.setVisible(true);

    var radius = jQuery("#radius").val() * jQuery("#KM_UNITS").val();

    circle = new google.maps.Circle({
        map: map,
        radius: radius,
        fillColor: "#AA0000"
    });

    circle.bindTo("center", markerSource, "position");

    markers.push(markerSource);
    markers.push(circle);

    map.fitBounds(circle.getBounds());

    var res = place.formatted_address.split(",");

    jQuery("#areaCountry").val(res[res.length - 1]);
    jQuery("#areaLatitude").val(place.geometry.location.lat());
    jQuery("#areaLongitude").val(place.geometry.location.lng());
    jQuery("#areaPlaceId").val(place.place_id);
    jQuery("#areaRadius").val(jQuery("#radius").val());
}