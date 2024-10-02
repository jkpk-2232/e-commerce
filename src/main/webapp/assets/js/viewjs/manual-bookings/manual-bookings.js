/*
var currentLat = "";
var currentLng = "";

var markers = [];
var markersCar = [];
var map;
var bound;
var directionsService = new google.maps.DirectionsService();
var directionsDisplay = new google.maps.DirectionsRenderer();
var polylineOptionsActual = "";
var sLatlng;
var dLatlng;

var sourcePlaceLat = "";
var sourcePlaceLng = "";
var sourcePlaceId = "";

var destinationPlaceLat = "";
var destinationPlaceLng = "";
var destinationPlaceId = "";

var ajaxInProgress = false;
var carAvailable = false;

var driverListHtml = "";

var rentalBooking = false;
var rentalPackagesList = [];
var rentalPackagesListOptionHtml = "";
var rentalPackageId = "";
var rentalPackageCarTypeId = "";

var rentalPackagesCallFristTime = "NO";

var airportFixedFare = "";
var airportBookingType = "";
var isAirportFixedFareApplied = false;
var markupFare = 0;

jQuery(document).ready(function() {

    jQuery(".dashboardWrapperLeftPanelMenuList li a").removeClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");
    jQuery("#2 a").addClass("dashboardWrapperLeftPanelMenuListCurrentActiveMenu");

    currentLat = jQuery("#currentLat").val();
    currentLng = jQuery("#currentLng").val();

//    var stripeApiPublishableKey = jQuery("#stripeApiPublishableKey").val();
//    Stripe.setPublishableKey(stripeApiPublishableKey);

    initialize();

    initializeMap(currentLat, currentLng);

    jQuery("#ajaxLoader").hide();
    jQuery("#ajaxLoaderManualBooking").hide();

    jQuery("#rideLaterPickupTimeLogs").bootstrapMaterialDatePicker({
        format: 'DD/MM/YYYY HH:mm:ss',
        minDate: jQuery("#beforePickupTime").val(),
        maxDate: jQuery("#afterPickupTime").val(),
        currentDate: jQuery("#beforePickupTime").val()
    });

    jQuery('#rideLaterPickupTimeLogs').bootstrapMaterialDatePicker().on('change', function(e, date) {

        console.log("rideLaterPickupTimeLogs changed: " + jQuery('#rideLaterPickupTimeLogs').val());
        updateDriverList();
    });

    jQuery("#rideLaterPickupTimeLogsParent").hide();
    jQuery("#creditCardDiv").hide();
    jQuery("#newPassengerDetailDiv").hide();
    jQuery("#btnGetRentalPackages").hide();
    jQuery(".selectedRentalPackageSpanDiv").hide();

    jQuery("#carType").change(function() {

        jQuery(".selectedRentalPackageSpanDiv").hide();

        if (jQuery("#carType").val() === "6") {

            jQuery("#destLocation").hide();
            jQuery("#btnNewBooking").hide();
            jQuery("#btnGetRentalPackages").show();

            rentalBooking = true;

            jQuery("#distanceDiv .control-label").text("Package");
            jQuery("#estimatedFareDiv .control-label").text("Base Fare");

        } else {

            jQuery("#destLocation").show();
            jQuery("#btnNewBooking").show();
            jQuery("#btnGetRentalPackages").hide();

            rentalBooking = false;

            jQuery("#distanceDiv .control-label").text("Distance");
            jQuery("#estimatedFareDiv .control-label").text("Estimate Fare");

            jQuery("#selectedRentalPackageSpan").text("");

            if (jQuery("#pickUpLocation").val() != "") {

                getNearestCarListFunction();
            }
        }
    });

    jQuery("#transmissionTypeList").change(function() {
        updateDriverList();
    });

    jQuery("#btnGetRentalPackages").click(function() {

        if (jQuery("#pickUpLocation").val() === "") {

            displayBootstrapMessage("Pickup location is required.");

        } else {

            jQuery("#ajaxLoaderRentalPackageModel").show();
            jQuery("#rentalPackagesDiv").hide();

            var rentalPackagesCarTypesListHtml = "<option value='-1'>Select Car Type</option>";
            jQuery("#rentalPackageCarTypes").html(rentalPackagesCarTypesListHtml).trigger("chosen:updated");

            jQuery("#packageTimeHeading").text("");
            jQuery("#packageTimeContent").text("");

            jQuery("#packageDistanceHeading").text("");
            jQuery("#packageDistanceContent").text("");

            jQuery("#packageBaseFareHeading").text("");
            jQuery("#packageBaseFareContent").text("");

            jQuery("#packageAddPerKmFareHeading").text("");
            jQuery("#packageAddPerKmFareContent").text("");

            jQuery("#packageAddPerMinuteFareHeading").text("");
            jQuery("#packageAddPerMinuteFareContent").text("");

            rentalPackagesCallFristTime = "YES";

            getRentalPackages();
        }
    });

    jQuery("#rentalPackageType").change(function() {

        rentalPackagesListOptionHtml = "";

        jQuery("#packageTimeHeading").text("");
        jQuery("#packageTimeContent").text("");

        jQuery("#packageDistanceHeading").text("");
        jQuery("#packageDistanceContent").text("");

        jQuery("#packageBaseFareHeading").text("");
        jQuery("#packageBaseFareContent").text("");

        jQuery("#packageAddPerKmFareHeading").text("");
        jQuery("#packageAddPerKmFareContent").text("");

        jQuery("#packageAddPerMinuteFareHeading").text("");
        jQuery("#packageAddPerMinuteFareContent").text("");

        var rentalPackagesCarTypesListHtml = "<option value='-1'>Select Car Type</option>";

        jQuery("#rentalPackageCarTypes").html(rentalPackagesCarTypesListHtml).trigger("chosen:updated");

        rentalPackagesCallFristTime = "NO";

        getRentalPackages();
    });

    jQuery("#rentalPackagesList").change(function() {

        jQuery("#packageTimeHeading").text("");
        jQuery("#packageTimeContent").text("");

        jQuery("#packageDistanceHeading").text("");
        jQuery("#packageDistanceContent").text("");

        jQuery("#packageBaseFareHeading").text("");
        jQuery("#packageBaseFareContent").text("");

        jQuery("#packageAddPerKmFareHeading").text("");
        jQuery("#packageAddPerKmFareContent").text("");

        jQuery("#packageAddPerMinuteFareHeading").text("");
        jQuery("#packageAddPerMinuteFareContent").text("");

        rentalPackageId = jQuery("#rentalPackagesList").val();

        if (rentalPackagesList.length > 0) {

            for (var k = 0; k < rentalPackagesList.length; k++) {

                var rentalPackagesCarTypesListHtml = "<option value='-1'>Select Car Type</option>";

                if (jQuery("#rentalPackagesList").val() === rentalPackagesList[k].rentalPackageId) {

                    var rentalPackagesCarTypesList = [];

                    rentalPackagesCarTypesList = rentalPackagesList[k].carWiseFareDetails;

                    for (var p = 0; p < rentalPackagesList[k].carWiseFareDetails.length; p++) {

                        rentalPackagesCarTypesListHtml += "<option value='" + rentalPackagesList[k].carWiseFareDetails[p].carTypeId + "'>" + rentalPackagesList[k].carWiseFareDetails[p].carTypeName + "</option>";

                    }

                    jQuery("#rentalPackageCarTypes").html(rentalPackagesCarTypesListHtml).trigger("chosen:updated");
                }
            }

        } else {

            displayBootstrapMessageWithRedirectionUrl("Invalid package.", basePath + "/manual-bookings.do");
        }

    });

    jQuery("#rentalPackageCarTypes").change(function() {

        console.log("rentalPackageCarTypes change rentalPackagesList.length: " + rentalPackagesList.length);

        jQuery("#packageTimeHeading").text("");
        jQuery("#packageTimeContent").text("");

        jQuery("#packageDistanceHeading").text("");
        jQuery("#packageDistanceContent").text("");

        jQuery("#packageBaseFareHeading").text("");
        jQuery("#packageBaseFareContent").text("");

        jQuery("#packageAddPerKmFareHeading").text("");
        jQuery("#packageAddPerKmFareContent").text("");

        jQuery("#packageAddPerMinuteFareHeading").text("");
        jQuery("#packageAddPerMinuteFareContent").text("");

        if (rentalPackagesList.length > 0) {

            for (var k = 0; k < rentalPackagesList.length; k++) {

                if (jQuery("#rentalPackagesList").val() === rentalPackagesList[k].rentalPackageId) {

                    var rentalPackagesCarTypesList = [];
                    rentalPackagesCarTypesList = rentalPackagesList[k].carWiseFareDetails;

                    if (rentalPackagesCarTypesList.length > 0) {

                        for (var p = 0; p < rentalPackagesCarTypesList.length; p++) {

                            if (jQuery("#rentalPackageCarTypes").val() === rentalPackagesCarTypesList[p].carTypeId) {

                                //	    						jQuery("#packageTimeHeading").text("Hours:");
                                //	    						jQuery("#packageTimeContent").text(rentalPackagesCarTypesList[p].packageTime);

                                //	    						jQuery("#packageDistanceHeading").text("KM:");
                                //	    						jQuery("#packageDistanceContent").text(rentalPackagesCarTypesList[p].packageDistance);

                                jQuery("#packageBaseFareHeading").text("Base Fare:");
                                jQuery("#packageBaseFareContent").text(rentalPackagesCarTypesList[p].baseFare);

                                jQuery("#packageAddPerKmFareHeading").text("Add. Per Minute Fare:");
                                jQuery("#packageAddPerKmFareContent").text(rentalPackagesCarTypesList[p].additionalPerMinuteFare);

                                jQuery("#packageAddPerMinuteFareHeading").text("Add. Per KM Fare:");
                                jQuery("#packageAddPerMinuteFareContent").text(rentalPackagesCarTypesList[p].additionalPerKmFare);
                            }
                        }
                    }
                }
            }

        } else {

            displayBootstrapMessageWithRedirectionUrl("Invalid package.", basePath + "/manual-bookings.do");
        }

    });

    jQuery("#btnRentalPackageCancel").click(function() {

        jQuery("#rentalPackageModel").modal("hide");
        location.reload();
    });

    jQuery("#btnRentalPackageOk").click(function() {

        if (jQuery("#rentalPackagesList").val() === "-1") {

            displayBootstrapMessage("Please select rental package.");

        } else if (jQuery("#rentalPackageCarTypes").val() === "-1") {

            displayBootstrapMessage("Please select car type.");

        } else {

            rentalPackageId = jQuery("#rentalPackagesList").val();
            rentalPackageCarTypeId = jQuery("#rentalPackageCarTypes").val();

            var rentalPackageBaseFare = "";

            //Get base fare ----------------------------------------
            if (rentalPackagesList.length > 0) {

                for (var k = 0; k < rentalPackagesList.length; k++) {

                    if (jQuery("#rentalPackagesList").val() === rentalPackagesList[k].rentalPackageId) {

                        var rentalPackagesCarTypesList = [];
                        rentalPackagesCarTypesList = rentalPackagesList[k].carWiseFareDetails;

                        if (rentalPackagesCarTypesList.length > 0) {

                            for (var p = 0; p < rentalPackagesCarTypesList.length; p++) {

                                if (jQuery("#rentalPackageCarTypes").val() === rentalPackagesCarTypesList[p].carTypeId) {

                                    rentalPackageBaseFare = ", " + rentalPackagesCarTypesList[p].baseFare;

                                }
                            }
                        }
                    }
                }

            }
            //---------------------------------------------------

            jQuery("#selectedRentalPackageSpan").text("Package: " + jQuery("#rentalPackageType option:selected").text() + ", " + jQuery("#rentalPackagesList option:selected").text() + rentalPackageBaseFare);

            jQuery(".selectedRentalPackageSpanDiv").show();

            jQuery("#btnNewBooking").show();
            jQuery("#btnGetRentalPackages").hide();

            jQuery("#rentalPackageModel").modal("hide");

            updateDriverList();
        }
    });

    jQuery("#changeRentalPackage").click(function() {

        if (jQuery("#pickUpLocation").val() === "") {

            displayBootstrapMessage("Pickup location is required.");

        } else {

            jQuery("#ajaxLoaderRentalPackageModel").show();
            jQuery("#rentalPackagesDiv").hide();

            var rentalPackagesCarTypesListHtml = "<option value='-1'>Select Car Type</option>";
            jQuery("#rentalPackageCarTypes").html(rentalPackagesCarTypesListHtml).trigger("chosen:updated");

            jQuery("#packageTimeHeading").text("");
            jQuery("#packageTimeContent").text("");

            jQuery("#packageDistanceHeading").text("");
            jQuery("#packageDistanceContent").text("");

            jQuery("#packageBaseFareHeading").text("");
            jQuery("#packageBaseFareContent").text("");

            jQuery("#packageAddPerKmFareHeading").text("");
            jQuery("#packageAddPerKmFareContent").text("");

            jQuery("#packageAddPerMinuteFareHeading").text("");
            jQuery("#packageAddPerMinuteFareContent").text("");

            getRentalPackages();
        }
    });

    jQuery("#driverListForRideLaterDiv").hide();

    jQuery("#rideType").change(function() {

        updateDriverList();

        if (jQuery("#rideType").val() == jQuery("#rideLaterType").val()) {

            jQuery("#rideLaterPickupTimeLogsParent").show();
            jQuery("#driverListDiv").hide();
            jQuery("#driverListForRideLaterDiv").show();

        } else {

            jQuery("#rideLaterPickupTimeLogsParent").hide();
            jQuery("#driverListDiv").show();
            jQuery("#driverListForRideLaterDiv").hide();
        }

        getEstimatedFare();
    });

    jQuery("#paymentType").change(function() {

        if (jQuery("#paymentType").val() == jQuery("#paymentTypeCard").val()) {
            jQuery("#creditCardDiv").show();
        } else {
            jQuery("#creditCardDiv").hide();
        }
    });

    jQuery("#passengerType").change(function() {

        if (jQuery("#passengerType").val() == jQuery("#passengerTypeRegistered").val()) {

            jQuery("#newPassengerDetailDiv").hide();
            jQuery("#passengerListDiv").show();

        } else {

            jQuery("#newPassengerDetailDiv").show();
            jQuery("#passengerListDiv").hide();
        }
    });

    jQuery("#btnNewBooking").click(function() {

        updateDriverList();

        if (jQuery("#carType").val() === "5" || jQuery("#rentalPackageCarTypes").val() === "5") {
            jQuery("#transmissionTypeListDiv").show();
        } else {
            jQuery("#transmissionTypeListDiv").hide();
        }

        if (jQuery("#pickUpLocation").val() == "") {

            if ((jQuery("#carType").val() === "6") || (rentalBooking)) {

                jQuery("#destLocation").hide();
                jQuery("#btnNewBooking").hide();
                jQuery("#btnGetRentalPackages").show();

                rentalBooking = true;

                jQuery("#selectedRentalPackageSpan").text("");
                jQuery(".selectedRentalPackageSpanDiv").hide();

            }

            displayBootstrapMessage("Pickup location is required.");

        } else if ((!rentalBooking) && (jQuery("#destLocation").val() == "")) {

            displayBootstrapMessage("Destination location is required.");

        } else {

            jQuery("#pickUpLocation1").val(jQuery("#pickUpLocation").val());

            if ((jQuery("#carType").val() === "6") || (rentalBooking)) {

                jQuery("#destLocation1").val("NA");

                jQuery("#distanceDiv .control-label").text("Package");
                jQuery("#estimatedFareDiv .control-label").text("Base Fare");

            } else {

                jQuery("#destLocation1").val(jQuery("#destLocation").val());

                jQuery("#distanceDiv .control-label").text("Distance");
                jQuery("#estimatedFareDiv .control-label").text("Estimate Fare");
            }

            getEstimatedFare();
        }

        //        } else {
        //
        //            if (jQuery("#pickUpLocation").val() == "") {
        //
        //                displayBootstrapMessage("Pickup location is required.");
        //
        //            } else if (jQuery("#destLocation").val() == "") {
        //
        //                displayBootstrapMessage("Destination location is required.");
        //
        //            } else {
        //
        //                displayBootstrapMessage("No cars/drivers are available currently. Please try after sometime.");
        //            }
        //        }
    });

    jQuery("#markupFare").change(function() {
        markupFare = jQuery("#markupFare").val();
        getEstimatedFare();

    });

    jQuery("#btnBookCar").click(function() {

        if (jQuery("#paymentType").val() == jQuery("#paymentTypeCard").val()) {

            var $form = jQuery("#payment-form");
            Stripe.card.createToken($form, stripeResponseHandler);

        } else {

            bookCar();
        }
    });

    jQuery("#btnCancel").click(function() {

        jQuery("#bookCarModel").modal("hide");

        location.reload();
    });

});

function stripeResponseHandler(status, response) {

    if (response.error) {

        jQuery(".payment-errors").text(response.error.message);
        jQuery(".payment-errors").css("color", "red");

        if (jQuery("#creditCardDiv").hasClass("has-error")) {
            jQuery("#creditCardDiv").removeClass("has-error");
        }

        jQuery("#creditCardDiv").addClass("has-error")

    } else {

        var token = response.id;

        jQuery("#stripeToken").val(token);

        bookCar();
    }
}

function bookCar() {

    jQuery("#ajaxLoaderManualBooking").show();
    jQuery(".manualBookingBody").hide();

    var updatedCarTypeId = jQuery("#carType").val();

    if (rentalBooking) {

        updatedCarTypeId = rentalPackageCarTypeId;
    }

    jQuery.ajax({
        url: basePath + "/manual-bookings.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            carType: updatedCarTypeId,
            pickUpLocation: jQuery("#pickUpLocation").val(),
            destLocation: jQuery("#destLocation").val(),
            sourcePlaceLat: sourcePlaceLat,
            sourcePlaceLng: sourcePlaceLng,
            destinationPlaceLat: destinationPlaceLat,
            destinationPlaceLng: destinationPlaceLng,
            rideType: jQuery("#rideType").val(),
            rideLaterPickupTimeLogs: jQuery("#rideLaterPickupTimeLogs").val(),
            passengerType: jQuery("#passengerType").val(),
            passengerList: jQuery("#passengerList").val(),
            firstName: jQuery("#firstName").val(),
            lastName: jQuery("#lastName").val(),
            email: jQuery("#email").val(),
            phone: jQuery("#phone").val(),
            paymentType: jQuery("#paymentType").val(),
            creditCardNo: jQuery("#creditCardNo").val(),
            month: jQuery("#month").val(),
            year: jQuery("#year").val(),
            cvv: jQuery("#cvv").val(),
            stripeToken: jQuery("#stripeToken").val(),
            driverList: jQuery("#driverList").val(),
            driverListForRideLater: jQuery("#driverListForRideLater").val(),
            rentalBooking: rentalBooking,
            rentalPackageId: rentalPackageId,
            rentalPackageCarTypeId: jQuery("#rentalPackageCarTypes").val(),
            transmissionTypeId: jQuery("#transmissionTypeList").val(),
            markupFare: jQuery("#markupFare").val(),
            isAirportFixedFareApplied: isAirportFixedFareApplied,
            airportBookingType: airportBookingType,
            airportFixedFare: airportFixedFare
        },
        success: function(responseData) {

            if (responseData.type == "SUCCESS") {

                if ("YES" === responseData.rideLaterBooking) {

                    if (jQuery("#roleId").val() === '7') {

                        displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manage-vendor-ride-later.do");
                    } else {
                        displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manage-ride-later.do");
                    }

                } else {

                    displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manual-bookings.do");
                }

            } else if (responseData.type == "Failure") {

                jQuery("#ajaxLoaderManualBooking").hide();
                jQuery(".manualBookingBody").show();

                displayBootstrapMessage(responseData.message);

            } else if (responseData.type == "AirportFailure") {

                jQuery("#ajaxLoaderManualBooking").hide();
                jQuery(".manualBookingBody").show();

                displayMessageAndReload(responseData.message);

            } else if (responseData.type == "FailToDriver") {

                jQuery("#ajaxLoaderManualBooking").hide();
                jQuery(".manualBookingBody").show();

                updateDriverList();

                displayBootstrapMessage(responseData.message);

            } else {

                jQuery("#ajaxLoaderManualBooking").hide();
                jQuery(".manualBookingBody").show();

                validateFields(responseData);
            }

            ajaxInProgress = false;
        }
    });
}

function getEstimatedFare() {

    jQuery("#estimatedFareDiv label").text('Estimated fare');

    var updatedCarTypeId = jQuery("#carType").val();

    if (rentalBooking) {

        updatedCarTypeId = rentalPackageCarTypeId;
    }

    jQuery.ajax({
        url: basePath + "/fare-calculator/fare-details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "carTypeId": updatedCarTypeId,
            "sourcePlaceId": sourcePlaceId,
            "destinationPlaceId": destinationPlaceId,
            "rideType": jQuery("#rideType").val(),
            "rentalBooking": rentalBooking,
            "rentalPackageId": rentalPackageId,
            "markupFare": markupFare
        },
        success: function(responseData) {

            if (responseData.type == "Success") {

                jQuery("#bookCarModel").modal("show");

                jQuery("#distance").val(responseData.distance);
                jQuery("#estimatedFare").val(responseData.total);

                if (responseData.isSurgePriceApplied) {

                    jQuery("#surgeMessage").text("(" + responseData.surgeMessage + ")");
                    jQuery("#estimatedFare").val(responseData.currency + responseData.estimateFareWithoutDiscount);

                } else if (responseData.isAirportFixedFareApplied) {

                    jQuery("#estimatedFareDiv label").text('Fixed fare');
                    jQuery("#estimatedFare").val(responseData.currency + responseData.estimateFareWithoutDiscount);
                    jQuery("#surgeMessage").text("(" + responseData.airportBookingType + ")");

                    isAirportFixedFareApplied = responseData.isAirportFixedFareApplied;
                    airportBookingType = responseData.airportBookingType;
                    airportFixedFare = responseData.estimateFareWithoutDiscount;

                } else {

                    jQuery("#surgeMessage").text("");
                }

            } else if (responseData.subType = "subType") {

                displayBootstrapMessage(responseData.message);

            } else {

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manual-bookings.do");
            }
        }
    });

}

function initialize() {

    var sinput = document.getElementById("pickUpLocation");

    new google.maps.places.Autocomplete(sinput);

    var dinput = document.getElementById("destLocation");

    new google.maps.places.Autocomplete(dinput);

    geocoder = new google.maps.Geocoder();

    sLatlng = new google.maps.LatLng(currentLat, currentLng);

    mapOptions = {
        zoom: 15,
        center: sLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);

    var source = document.getElementById("pickUpLocation");
    var destination = document.getElementById("destLocation");

    var autocompleteSource = new google.maps.places.Autocomplete(source);
    autocompleteSource.bindTo("bounds", map);

    var autocompleteDestination = new google.maps.places.Autocomplete(destination);
    autocompleteDestination.bindTo("bounds", map);

    autocompleteSource.addListener("place_changed", function() {

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

        sourcePlaceLat = place.geometry.location.lat();
        sourcePlaceLng = place.geometry.location.lng();
        sourcePlaceId = place.place_id;

        if (jQuery("#pickUpLocation").val() != "") {

        	//Get car type list
        	getCarTypeList();
        	
            if ((jQuery("#carType").val() === "6") || (rentalBooking)) {

                jQuery("#destLocation").hide();
                jQuery("#btnNewBooking").hide();
                jQuery("#btnGetRentalPackages").show();

                rentalBooking = true;

                jQuery("#selectedRentalPackageSpan").text("");
                jQuery(".selectedRentalPackageSpanDiv").hide();
            }
            
            //getNearestCarListFunction();

        } else {

            displayBootstrapMessage("Pickup location is required.");
            return false;
        }
    });

    autocompleteDestination.addListener("place_changed", function() {

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

        destinationPlaceLat = place.geometry.location.lat();
        destinationPlaceLng = place.geometry.location.lng();
        destinationPlaceId = place.place_id;

        if (jQuery("#pickUpLocation").val() == "") {

            displayBootstrapMessageWithRedirectionUrl("Pickup location is required.", basePath + "/manual-bookings.do");

        } else {

            if (jQuery("#destLocation").val() != "") {

                plotDestinationMarker();

            } else {

                displayBootstrapMessage("Destination location is required.");
                return false;
            }
        }
    });
}

function initializeMap(lat, lng) {

    dLatlng = new google.maps.LatLng(lat, lng);

    mapOptions = {
        zoom: 15,
        center: dLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);
}

function plotDestinationMarker() {

    for (i = 0; i < markers.length; i++) {

        markers[i].setMap(null);
    }

    directionsDisplay.setMap(null);

    var siconImage = "";
    var diconImage = "";

    siconImage = basePath + "/assets/image/user_pin_1.png";
    diconImage = basePath + "/assets/image/user_pin_2.png";

    sLatlng = new google.maps.LatLng(sourcePlaceLat, sourcePlaceLng);
    dLatlng = new google.maps.LatLng(destinationPlaceLat, destinationPlaceLng);

    amarker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: sLatlng
    });

    dmarker = new google.maps.Marker({
        map: map,
        icon: diconImage,
        position: dLatlng
    });

    markers.push(smarker);
    markers.push(dmarker);

    polylineOptionsActual = new google.maps.Polyline({
        strokeColor: jQuery("#themeColor").val(),
        strokeOpacity: 1.0,
        strokeWeight: 5
    });

    directionsDisplay = new google.maps.DirectionsRenderer({
        suppressMarkers: true,
        polylineOptions: polylineOptionsActual
    });

    var request = {
        origin: new google.maps.LatLng(sourcePlaceLat, sourcePlaceLng),
        destination: new google.maps.LatLng(destinationPlaceLat, destinationPlaceLng),
        travelMode: google.maps.TravelMode["DRIVING"]
    };

    directionsService.route(request, function(response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
        }
    });

    directionsDisplay.setMap(map);
}

function getNearestCarListFunction() {

    jQuery("#dashboardMapCanvas").hide();
    jQuery("#ajaxLoader").show();

    var carTypeId = jQuery("#carType").val();

    ajaxInProgress = false;

    if (ajaxInProgress === true) {
        return;
    }

    ajaxInProgress = true;

    driverListAjax = jQuery.ajax({
        url: basePath + "/car-near-by/available-cars.json?carTypeId=" + carTypeId + "&sourcePlaceLat=" + sourcePlaceLat + "&sourcePlaceLng=" + sourcePlaceLng,
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {},
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {

            sLatlng = new google.maps.LatLng(sourcePlaceLat, sourcePlaceLng);

            jQuery("#ajaxLoader").hide();
            jQuery("#dashboardMapCanvas").show();

            initializeMap(sourcePlaceLat, sourcePlaceLng);

            var siconImage = basePath + "/assets/image/user_pin_1.png";

            smarker = new google.maps.Marker({
                map: map,
                icon: siconImage,
                position: sLatlng
            });

            markers.push(smarker);

            if (responseData.status == "Failure") {

                carAvailable = false;
                displayBootstrapMessage(responseData.message);

            } else if (responseData.status == "RegionFailure") {

                carAvailable = false;
                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manual-bookings.do")

            } else if (responseData.status == "SUCCESS") {

                var nearestCarArr = [];

                for (var i = 0; i < responseData.carMapList.length; i++) {

                    var userLat = responseData.carMapList[i].latitude;
                    var userLng = responseData.carMapList[i].longitude;

                    if (userLat === null || userLng === null) {
                        continue;
                    }

                    if (((userLat - 0.0) < 0.000001) && ((userLng - 0.0) < 0.000001)) {
                        continue;
                    }

                    nearestCarArr.push(responseData.carMapList[i]);
                }

                if (nearestCarArr.length > 0) {

                    //                    carAvailable = true;
                    carAvailable = false;

                    for (var j = 0; j < nearestCarArr.length; j++) {

                        currentLat = nearestCarArr[j].latitude;
                        currentLng = nearestCarArr[j].longitude;

                        if (currentLat !== null && currentLng !== null) {
                            break;
                        }
                    }

                    driverListHtml = "<option value='-1'>Automatic driver select</option>";

                    for (var k = 0; k < nearestCarArr.length; k++) {

                        currentLat = nearestCarArr[k].latitude;
                        currentLng = nearestCarArr[k].longitude;

                        var time = nearestCarArr[k].time;

                        myLatlng = new google.maps.LatLng(currentLat, currentLng);

                        var iconImage = basePath + "/assets/image/car_icons/" + nearestCarArr[k].carIcon + "/" + nearestCarArr[k].carIcon + "_free.png";

                        marker = new google.maps.Marker({
                            map: map,
                            icon: iconImage,
                            position: myLatlng
                        });

                        //@formatter:off
                        var contentString = "<div class='dashboardMapInfo'>" +
                            "<div class='dashboardMapInfoInner'>" +
                            "<div class='dashboardMapInfoInnerTitle'>" +
                            "<h4>" +
                            "<i class='glyphicon glyphicon-old_man'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].driverName + "</span>" +
                            "</h4>" +
                            "</div>" +
                            "<div class='dashboardMapInfoInnerDriverInfo'>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='icon-envelope'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].email + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='glyphicon glyphicon-earphone'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].phoneNo + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='halfling-list-alt'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].carPlateNo + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='glyphicon glyphicon-calendar'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].dob + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='icon-star'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].ratings + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "<div class='dashboardMapInfoInnerDriverInfoName'>" +
                            "<i class='halfling-map-marker'></i>" +
                            "<span class='spanDriverInfo'>" + nearestCarArr[k].distance + "</span>" +
                            "</div>" +
                            "<div class='dashboardSeperator'></div>" +
                            "</div>" +
                            "</div>" +
                            "</div>";
                        //@formatter:on

                        marker = new google.maps.Marker({
                            map: map,
                            icon: iconImage,
                            position: myLatlng,
                            info: contentString
                        });

                        markersCar.push(marker);

                        var infowindow = new google.maps.InfoWindow()
*/
                        /*google.maps.event.addListener(marker, 'click', (function(marker, content, infowindow) {
                            return function() {
                                infowindow.setContent(contentString);
                                infowindow.open(map, marker);
                            };
                        })(marker, contentString, infowindow));*/

                        // Below code for marker shows same info -------------------------------------
                        //var infowindow;
/*
                        google.maps.event.addListener(marker, 'mouseover', function() {
                            infowindow = new google.maps.InfoWindow({});
                            infowindow.setContent(this.info);
                            infowindow.open(map, this);
                        });

                        google.maps.event.addListener(marker, 'mouseout', function() {
                            infowindow.close(map, this);
                        });
                        //------------------------------------------------------------------------

                        carAvailable = true;

                        driverListHtml += "<option value='" + nearestCarArr[k].driverId + "'>" + nearestCarArr[k].driverInfo + "</option>";
                    }

                    setTimeout(function() {
                        jQuery("#driverList").html(driverListHtml).trigger("chosen:updated");
                    }, 500);

                } else {

                    carAvailable = false;
                    displayBootstrapMessage("No cars/drivers are available currently. Please try after sometime.");
                }

                ajaxInProgress = false;
            }
        }
    });
}

function validateFields(data) {

    var flagNewPassengerDetailDiv = false;
    var flagCreditCardDiv = false;

    if (jQuery("#newPassengerDetailDiv").hasClass("has-error")) {
        jQuery("#newPassengerDetailDiv").removeClass("has-error");
    }

    if (jQuery("#creditCardDiv").hasClass("has-error")) {
        jQuery("#creditCardDiv").removeClass("has-error");
    }

    var pickUpLocationError = "";
    var destLocationError = "";
    var rideLaterPickupTimeLogsError = "";
    var firstNameError = "";
    var lastNameError = "";
    var emailError = "";
    var phoneError = "";
    var creditCardNoError = "";
    var monthError = "";
    var yearError = "";
    var cvvError = "";
    var passengerListError = "";
    var driverListError = "";

    if ('pickUpLocationError' in data) {
        pickUpLocationError = data.pickUpLocationError;
    }

    if ('destLocationError' in data) {
        destLocationError = data.destLocationError;
    }

    if ('rideLaterPickupTimeLogsError' in data) {
        rideLaterPickupTimeLogsError = data.rideLaterPickupTimeLogsError;
    }

    if ('firstNameError' in data) {

        flagNewPassengerDetailDiv = true;

        firstNameError = data.firstNameError;
    }

    if ('lastNameError' in data) {

        flagNewPassengerDetailDiv = true;

        lastNameError = data.lastNameError;
    }

    if ('emailError' in data) {

        flagNewPassengerDetailDiv = true;

        emailError = data.emailError;
    }

    if ('phoneError' in data) {

        flagNewPassengerDetailDiv = true;

        phoneError = data.phoneError;
    }

    if ('creditCardNoError' in data) {

        flagCreditCardDiv = true;

        creditCardNoError = data.creditCardNoError;
    }

    if ('monthError' in data) {

        flagCreditCardDiv = true;

        monthError = data.monthError;
    }

    if ('yearError' in data) {

        flagCreditCardDiv = true;

        yearError = data.yearError;
    }

    if ('cvvError' in data) {

        flagCreditCardDiv = true;

        cvvError = data.cvvError;
    }

    if ('passengerListError' in data) {

        passengerListError = data.passengerListError;
    }

    if ('driverListError' in data) {

        driverListError = data.driverListError;
    }

    if (flagNewPassengerDetailDiv) {
        jQuery("#newPassengerDetailDiv").addClass("has-error");
    } else {
        jQuery("#newPassengerDetailDiv").removeClass("has-error");
    }

    if (flagCreditCardDiv) {
        jQuery("#creditCardDiv").addClass("has-error");
    } else {
        jQuery("#creditCardDiv").removeClass("has-error");
    }

    var p1Error = errorMessageFunction("pickUpLocation", pickUpLocationError);
    var p2Error = errorMessageFunction("destLocation", destLocationError);
    var p3Error = errorMessageFunction("rideLaterPickupTimeLogs", rideLaterPickupTimeLogsError);
    var p4Error = errorMessageFunction("firstName", firstNameError);
    var p5Error = errorMessageFunction("lastName", lastNameError);
    var p6Error = errorMessageFunction("email", emailError);
    var p7Error = errorMessageFunction("phone", phoneError);
    var p8Error = errorMessageFunction("creditCardNo", creditCardNoError);
    var p9Error = errorMessageFunction("month", monthError);
    var p10Error = errorMessageFunction("year", yearError);
    var p11Error = errorMessageFunction("cvv", cvvError);

    var p12Error = errorMessageFunction("passengerList_chosen", passengerListError);
    var p13Error = errorMessageFunction("driverList_chosen", driverListError);

    if (p1Error || p2Error || p3Error || p4Error || p5Error || p6Error || p7Error || p8Error || p9Error || p10Error || p11Error || p12Error || p13Error) {
        return false;
    }
}

function getRentalPackages() {

    console.log("rentalPackagesCallFristTime: " + rentalPackagesCallFristTime);

    jQuery.ajax({
        url: basePath + "/manual-bookings/rental-packages.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "latitude": sourcePlaceLat,
            "longitude": sourcePlaceLng,
            "rentalPackageType": jQuery("#rentalPackageType").val()
        },
        success: function(responseData) {

            jQuery("#ajaxLoaderRentalPackageModel").hide();
            jQuery("#rentalPackagesDiv").show();

            if (responseData.type == "SUCCESS") {

                rentalPackagesCallFristTime = "NO";

                rentalPackagesList = responseData.rentalPackagesList;

                rentalPackagesListOptionHtml = "<option value='-1'>Select Package</option>";

                for (var k = 0; k < rentalPackagesList.length; k++) {

                    rentalPackagesListOptionHtml += "<option value='" + rentalPackagesList[k].rentalPackageId + "'>" + rentalPackagesList[k].packageTime + ", " + rentalPackagesList[k].packageDistance + "</option>";
                }

                jQuery("#rentalPackagesList").html(rentalPackagesListOptionHtml).trigger("chosen:updated");

                jQuery("#rentalPackageModel").modal("show");

            } else {

                if (rentalPackagesCallFristTime === "YES") {

                    jQuery("#rentalPackageModel").modal("show");

                    //            		displayBootstrapMessage(responseData.message);

                } else {

                    displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/manual-bookings.do");
                }

            }
        }
    });
}

function displayMessageAndReload(msg, url) {

    bootbox.confirm({
        title: "Alert!",
        message: msg,
        buttons: {
            confirm: {
                label: '<i class="fa fa-check"></i> Ok'
            }
        },
        callback: function(result) {
            location.reload();
        }
    });
}

function updateDriverList() {

    var updatedCarTypeId = jQuery("#carType").val();

    if (rentalBooking) {

        updatedCarTypeId = rentalPackageCarTypeId;
    }

    updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manual-bookings/driver-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "carTypeId": updatedCarTypeId,
            "rentalPackageCarTypeId": jQuery("#transmissionTypeList").val(),
            "transmissionTypeId": jQuery("#transmissionTypeList").val(),
            "sourcePlaceLat": sourcePlaceLat,
            "sourcePlaceLng": sourcePlaceLng,
            "destinationPlaceLat": destinationPlaceLat,
            "destinationPlaceLng": destinationPlaceLng,
            "rideType": jQuery("#rideType").val(),
            "rideLaterPickupTimeLogs": jQuery("#rideLaterPickupTimeLogs").val()
        },
        success: function(responseData) {

            var driverListOptionHtml = "<option value='-1'>Automatic driver select</option>";

            var driverListForRideLaterOptionHtml = "<option value='-1'>Assign Later</option>";

            if (responseData.type === "SUCCESS") {

                console.log("responseData.driverList.length: " + responseData.driverList.length);

                console.log("responseData.driverListForRideLater.length: " + responseData.driverListForRideLater.length);

                if (responseData.driverList.length > 0) {

                    for (var k = 0; k < responseData.driverList.length; k++) {

                        driverListOptionHtml += "<option value='" + responseData.driverList[k].driverId + "'>" + responseData.driverList[k].driverName + ", " + responseData.driverList[k].driverEmail + "</option>";
                    }
                }

                if (responseData.driverListForRideLater.length > 0) {

                    for (var k = 0; k < responseData.driverListForRideLater.length; k++) {

                        driverListForRideLaterOptionHtml += "<option value='" + responseData.driverListForRideLater[k].driverId + "'>" + responseData.driverListForRideLater[k].driverName + ", " + responseData.driverListForRideLater[k].driverEmail + "</option>";
                    }
                }
            }

            jQuery("#driverList").html(driverListOptionHtml).trigger("chosen:updated");

            jQuery("#driverListForRideLater").html(driverListForRideLaterOptionHtml).trigger("chosen:updated");

        }

    });
}

function getCarTypeList() {

    var updatedCarTypeId = jQuery("#carType").val();

    updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manual-bookings/vendor-car-type.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "carTypeId": updatedCarTypeId,
            "sourcePlaceLat": sourcePlaceLat,
            "sourcePlaceLng": sourcePlaceLng
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	
        	if (responseData.type == "SUCCESS") {
        		console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.carTypeListOptions);
        		jQuery('#carType').children('option').remove();
      	       	jQuery("#carType").append(responseData.carTypeListOptions);
      	       	jQuery("#carType").trigger("chosen:updated");
        	}
        	
        	getNearestCarListFunction();
        }
    });
}
*/
var map;
    var sourceMarker;
    var destinationMarker;
    var directionsService;
    var directionsRenderer;
    var alternativeRoutes = [];
	
	
	//setMenuActive("2");
	setMenuActiveSub("42");
	
    $(document).ready(function () {
        initializeMap();

        // Autocomplete for Source Address
        var sourceInput = $('#sourceAddress')[0];
        var sourceAutocomplete = new google.maps.places.Autocomplete(sourceInput);
        sourceAutocomplete.bindTo('bounds', map);
        sourceAutocomplete.addListener('place_changed', function () {
            var place = sourceAutocomplete.getPlace();
            if (!place.geometry) {
                alert("Autocomplete's returned place contains no geometry");
                return;
            }

            if (sourceMarker) {
                sourceMarker.setMap(null);
            }

            // Set the source marker with a green icon
            sourceMarker = new google.maps.Marker({
                map: map,
                position: place.geometry.location,
                title: "Source Address",
                icon: {
                    url: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
                }
            });

            map.setCenter(place.geometry.location);
            map.setZoom(14);

            calculateAndDisplayRoute();
        });

        // Autocomplete for Destination Address
        var destinationInput = $('#destinationAddress')[0];
        var destinationAutocomplete = new google.maps.places.Autocomplete(destinationInput);
        destinationAutocomplete.bindTo('bounds', map);
        destinationAutocomplete.addListener('place_changed', function () {
            var place = destinationAutocomplete.getPlace();
            if (!place.geometry) {
                alert("Autocomplete's returned place contains no geometry");
                return;
            }

            if (destinationMarker) {
                destinationMarker.setMap(null);
            }

            // Set the destination marker with a red icon
            destinationMarker = new google.maps.Marker({
                map: map,
                position: place.geometry.location,
                title: "Destination Address",
                icon: {
                    url: "http://maps.google.com/mapfiles/ms/icons/red-dot.png"
                }
            });

            map.setCenter(place.geometry.location);
            map.setZoom(14);

            calculateAndDisplayRoute();
        });
        // Listen to changes in the selected route
        google.maps.event.addListener(directionsRenderer, 'directions_changed', function () {
            var directions = directionsRenderer.getDirections();
            var selectedRoute = directions.routes[0];
            var distance = selectedRoute.legs[0].distance.text;
            $('#distanceField').val(distance);
        });
    });

    function initializeMap() {
        // Initialize Google Map
        var mapOptions = {
            zoom: 7,
            center: { lat: 20.5937, lng: 78.9629 }  // Default to India's lat-long
        };
        map = new google.maps.Map($('#dashboardMapCanvas')[0], mapOptions);

        // Initialize Directions Service and Renderer
        directionsService = new google.maps.DirectionsService();
        directionsRenderer = new google.maps.DirectionsRenderer({
            map: map,
            draggable: true,
            suppressMarkers: true  // Disable default markers (A and B)
        });
    }

    function calculateAndDisplayRoute() {
        if (!sourceMarker || !destinationMarker) {
            return;
        }

        var request = {
            origin: sourceMarker.getPosition(),
            destination: destinationMarker.getPosition(),
            travelMode: google.maps.TravelMode.DRIVING,
            provideRouteAlternatives: true
        };

        directionsService.route(request, function (result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                alternativeRoutes = result.routes;

                // Render the first route by default
                directionsRenderer.setDirections(result);

                // Update distance for the first route
                var distance = result.routes[0].legs[0].distance.text;
                $('#distanceField').val(distance);

                // Optionally, you can display a list of routes for selection
                // (This part can be further customized based on your requirements)
            } else {
                alert("Directions request failed due to " + status);
            }
        });
    }
	  // Button Click Handlers
	    $("#testbutton").click(function () {
	        $("#productPopup").modal("show");
	        loadDatatables(productData);
	    });

	    $("#btnAddProduct").click(function (e) {
	        e.preventDefault();
	        redirectToUrl(ADD_URL);
	    });
	// Data for DataTable
	const productData = [
	    { "labelSrNo": 1, "labelProductName": "Product 1", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 2, "labelProductName": "Product 2", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 3, "labelProductName": "Product 3", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 4, "labelProductName": "Product 4", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 5, "labelProductName": "Product 5", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 6, "labelProductName": "Product 6", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 7, "labelProductName": "Product 7", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
	    { "labelSrNo": 8, "labelProductName": "Product 8", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 }
	];

	// Load DataTable with product data
	function loadDatatables(productData) {
	    const $datatable = $("#datatableDefault");

	    if ($.fn.DataTable.isDataTable($datatable)) {
	        $datatable.DataTable().clear().destroy();
	    }

	    $datatable.DataTable({
	        paging: false,
	        info: false,
	        searching: false,
	        data: productData,
	        scrollCollapse: true,
	        columns: [
	            { data: "labelSrNo", width: "10%", orderable: false, title: "Sr. No." },
	            { data: "labelProductName", width: "30%", orderable: false, title: "Product Name" },
	            { data: "labelProductValue", width: "20%", orderable: false, title: "Product Value" },
	            {
	                data: "labelPrice",
	                width: "20%",
	                orderable: false,
	                title: "Price",
	                render: (data, type, row) => {
	                    const currentQty = row.currentQty || 1; // Default to 1 if undefined
	                    return `₹ ${data * currentQty} <div class='price-separator'></div>`;
	                }
	            },
	            {
	                data: null,
	                width: "20%",
	                orderable: false,
	                title: "Quantity",
	                defaultContent: "<button class='btn btn-outline-primary btn-sm minus-btn green-btn'>-</button> <span class='qty'>1</span> <button class='btn btn-outline-primary btn-sm plus-btn green-btn'>+</button>"
	            }
	        ],
	        createdRow: (row, data) => {
	            // Set initial price and quantity when the row is created
	            $(row).data({
	                initialPrice: data.labelPrice,
	                currentQty: 1
	            });
	        }
	    });

	    $datatable.on('click', 'button.plus-btn, button.minus-btn', function () {
	        const $button = $(this);
	        const $row = $button.closest('tr');
	        const $qtyElement = $button.siblings('.qty');
	        let currentQty = parseInt($qtyElement.text());
	        const initialPrice = $row.data('initialPrice');

	        if ($button.hasClass('plus-btn')) {
	            currentQty++; 
	        } else if (currentQty > 1) {
	            currentQty--;
	        }

	        // Update the quantity and price
	        $qtyElement.text(currentQty);
	        $row.find('td:eq(3)').html(`₹ ${currentQty * initialPrice} <div class='price-separator'></div>`); 
	        $row.data('currentQty', currentQty);
	    });

	    // Close button functionality
	    $("#closeModalBtn").on("click", function() {
	        $("#productPopup").modal('hide'); // Hides the modal
	    });

	    // Apply CSS overloads if any
	    commonCssOverloadForDatatable("datatableDefault");
	}
