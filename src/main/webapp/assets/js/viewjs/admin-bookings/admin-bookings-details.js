var map;
var bound;
var markers = [];
var smarker;
var dmarker;
var cmarker;
var destinationLat = "";
var destinationLng = "";
//var directionsService = new google.maps.DirectionsService();
//var directionsDisplay = new google.maps.DirectionsRenderer();

jQuery(document).ready(function() {

	if (jQuery("#tourType").val() == "current" || jQuery("#tourType").val() == "completed") {
		setMenuActive("1");
    } else if (jQuery("#tourType").val() == "bookings") {
    	setMenuActiveSub("43");
    } else if (jQuery("#tourType").val() == "rideLater") {
    	setMenuActiveSub("44");
    } else if (jQuery("#tourType").val() == "criticalRideLater") {
    		setMenuActiveSub("45");
    } else if (jQuery("#tourType").val() == "passenger") {
            setMenuActive("5");
    } else if (jQuery("#tourType").val() == "driver") {
    	setMenuActive("6");
    } else if (jQuery("#tourType").val() == "owner") {
    	setMenuActive("7");
    } else if (jQuery("#tourType").val() == "refund") {
        setMenuActive("11");
    } else if (jQuery("#tourType").val() == "mybookings") {
    	setMenuActive("12");
    } else {
    	setMenuActive("15");
    }
	
    jQuery("#btnRefund").hide();
    jQuery("#surgeDiv").hide();
    jQuery("#airportFareBreakdownDiv").hide();
    
    if (jQuery("#tourType").val() == "refund") {

        if (jQuery("#isRefunded").val() === "YES") {
            jQuery("#btnRefund").hide();
        } else {
            jQuery("#btnRefund").show();
        }
    }

    if (jQuery("#isRentalBooking").val() === "true") {

        jQuery("#rentalPackageDiv").show();

        jQuery("#waitingTimeDiv").hide();
        jQuery("#waitingFareDiv").hide();
        jQuery("#surgeFareDiv").hide();

        jQuery("#distTimeFareForRentalDiv").show();
        jQuery("#distTimeFareForTaxiDiv").hide();

        jQuery("#destinationAddressDiv").hide();

    } else {

        jQuery("#rentalPackageDiv").hide();

        jQuery("#waitingTimeDiv").show();
        jQuery("#waitingFareDiv").show();
        jQuery("#surgeFareDiv").show();

        jQuery("#distTimeFareForRentalDiv").hide();
        jQuery("#distTimeFareForTaxiDiv").show();

        jQuery("#destinationAddressDiv").show();
    }

    if (jQuery("#statusForFareBreakdown").val() === "cancel by passenger") {

        jQuery("#fareBreakdownDiv").hide();
        jQuery("#fareBreakdownDivForCancelledTour").show();

    } else if (jQuery("#statusForFareBreakdown").val() === "passenger unavailable") {

        jQuery("#fareBreakdownDiv").hide();
        jQuery("#fareBreakdownDivForCancelledTour").show();

    } else if (jQuery("#statusForFareBreakdown").val() === "cancel by driver") {

        jQuery("#fareBreakdownDiv").hide();
        jQuery("#fareBreakdownDivForCancelledTour").show();

    } else {

        if (jQuery("#isSurgeApplied").val() === "true") {
            jQuery("#surgeDiv").show();
        } else {
            jQuery("#surgeDiv").hide();
        }

        jQuery("#fareBreakdownDiv").show();
        jQuery("#fareBreakdownDivForCancelledTour").hide();

        if (jQuery("#isAirportFixedFareApplied").val() === "true") {
            jQuery("#fareBreakdownDiv").hide();
            jQuery("#airportFareBreakdownDiv").show();
        } else {
            jQuery("#fareBreakdownDiv").show();
            jQuery("#airportFareBreakdownDiv").hide();
        }
    }

    jQuery("#btnEmail").on("click", function(e) {

        e.preventDefault();

        sendEmail();
    });

    jQuery("#btnRefund").on("click", function(e) {

        e.preventDefault();

        jQuery("#amountRefundedError").html("");
        jQuery("#amountRefunded").val("");

        jQuery("#refundModal").modal("show"); 
    });

    jQuery("#btnRefundCancelModal").click(function() {
        jQuery("#refundModal").modal("hide"); 
    });

    jQuery("#btnRefundProcessModal").click(function() {
        makeRefund();
    });
    
    getTripDetailsForMap();
    
    // TODO
    jQuery('#finalAmtCollectedACDialog').attr('readonly', true);

    // TODO
    jQuery("#editSpan").on("click", function(e) {

        console.log("Edit final amount clicked: " + jQuery("#tourId").val());

        e.preventDefault();

        jQuery('#finalAmtCollectedACDialog').val(jQuery('#finalAmountCollectedHidden').val());
        jQuery('#newFinalAmtCollectedACDialog').val("");
        jQuery('#remarkACDialog').val("");

        jQuery("#newFinalAmtCollectedACDialogError").html("");
        jQuery("#remarkACDialogError").html("");

        jQuery("#updateAmountCollectedDialog").modal();
    });

    // TODO
    jQuery("#aCDialogBtnSave").on("click", function(e) {

        e.preventDefault();

        jQuery("#newFinalAmtCollectedACDialogError").html("");
        jQuery("#remarkACDialogError").html("");

        updateFinalAmountCollected();
    });

    // TODO
    jQuery("#aCDialogBtnCancel").on("click", function(e) {

        jQuery('#newFinalAmtCollectedACDialog').val("");
        jQuery('#remarkACDialog').val("");

        jQuery("#newFinalAmtCollectedACDialogError").html("");
        jQuery("#remarkACDialogError").html("");

        jQuery('#updateAmountCollectedDialog').modal('toggle');
    });
    
    // TODO
    jQuery(".releasePassengerDiv .checkBoxImg").click(function() {
        jQuery(".releasePassengerDiv .checkBoxImg").toggleClass("visited");
    });
});

function sendEmail() {

    bootbox.prompt({
        title: "Email Recepient!",
        inputType: "email",
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> Cancel'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> Ok'
            }
        },
        callback: function(result) {

            if (result) {

                jQuery.ajax({
                    url: basePath + "/admin-bookings-details/send-email.json",
                    type: "GET",
                    cache: false,
                    dataType: 'json',
                    timeout: 50000,
                    data: {
                        "tourId": jQuery("#tourId").val(),
                        "email": result
                    },
                    success: function(responseData) {
                        displayBootstrapMessage(responseData.message);
                    }
                });

            } else {
            	displayBootstrapMessage("Failed to send email!");
            }
        }
    });
}

function initializeMap() {

    var dLatlng = new google.maps.LatLng(jQuery("#currentLat").val(), jQuery("#currentLng").val());

    mapOptions = {
        zoom: 15,
        center: dLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);
}

function getTripDetailsForMap() {

    jQuery(".dashboardMapCanvas").html("");

    driverListAjax = jQuery.ajax({
        url: basePath + "/bookings/booking-details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "tourId": jQuery("#tourId").val()
        },
        error: function() {

        },
        success: function(responseData) {

            if (responseData !== null) {

                destinationLat = responseData.dlatitude;
                destinationLng = responseData.dlongitude;

                if (responseData.tourStatus === "ended") {

                    drawRouteFromCSVFilePoints();

                } else {

                    var cLatlng = new google.maps.LatLng(responseData.cLat, responseData.cLong);
                    var dLatlng = new google.maps.LatLng(destinationLat, destinationLng);
                    var sLatLng = new google.maps.LatLng(responseData.slatitude, responseData.slongitude);

                    initializeMap();

                    var diconImage = "";
                    var siconImage = "";
                    var ciconImage = "";

                    if (responseData.tourStatus === "started") {
                        ciconImage = basePath + "/assets/image/car_icons/" + responseData.carIcon + "/" + responseData.carIcon + "_busy.png";
                    }

                    siconImage = basePath + "/assets/image/user_pin_1.png";
                    diconImage = basePath + "/assets/image/user_pin_2.png";

                    contentString = responseData.firstName + " " + responseData.lastName;

                    dmarker = new google.maps.Marker({
                        map: map,
                        icon: diconImage,
                        position: dLatlng
                    });

                    smarker = new google.maps.Marker({
                        map: map,
                        icon: siconImage,
                        position: sLatLng
                    });

                    markers.push(smarker);
                    markers.push(dmarker);

                    if (responseData.tourStatus === "started") {

                        cmarker = new google.maps.Marker({
                            map: map,
                            icon: ciconImage,
                            position: cLatlng,
                            info: contentString
                        });

                        markers.push(cmarker);
                    }

                    var infowindow;

                    google.maps.event.addListener(dmarker, 'mouseover', function() {
                        infowindow = new google.maps.InfoWindow({});
                        infowindow.setContent(this.info);
                        infowindow.open(map, this);
                    });

                    google.maps.event.addListener(dmarker, 'mouseout', function() {
                        infowindow.close(map, this);
                    });

                }

            }
        }
    });
}

function makeRefund() {

    var amountRefunded = jQuery("#amountRefunded").val();

    if (amountRefunded === "" || amountRefunded === null || amountRefunded === "undefined") {
        errorMessageFunction("amountRefunded", "Refund amount is empty or invalid.");
        return;
    }

    jQuery("#amountRefundedError").html("");

    jQuery.ajax({
        url: basePath + "/admin-bookings-details/refund.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "tourId": jQuery("#tourId").val(),
            "amountRefunded": amountRefunded
        },
        error: function() {
            console.log("Error.. ");
        },
        success: function(responseData) {

            if (responseData.type === TYPE_SUCCESS) {

            	jQuery("#refundModal").modal("hide"); 

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/admin-bookings-details.do?tourId=" + jQuery("#tourId").val() + "&tourType=refund");

            } else {

                errorMessageFunction("amountRefunded", responseData.message);
            }
        }
    });
}

function initializeFlightPlanCoordinates(flightPlanCoordinates) {

    dLatlng = new google.maps.LatLng(destinationLat, destinationLng);

    var mapOptions = {
        zoom: 14,
        center: dLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById('dashboardMapCanvas'), mapOptions);

    var flightPath = new google.maps.Polyline({
        path: flightPlanCoordinates,
        geodesic: true,
        strokeColor: jQuery("#themeColor").val(),
        strokeOpacity: 1.0,
        strokeWeight: 3
    });

    flightPath.setMap(map);
}

function drawRouteFromCSVFilePoints() {

    jQuery(".dashboardMapCanvas").html("");

    jQuery.ajax({
        url: basePath + "/bookings/booking-locations.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "tourId": jQuery("#tourId").val()
        },
        success: function(responseData) {

            if (responseData !== null) {

                var flightPlanCoordinates = [];

                for (var i = 0; i < responseData.locationList.length; i++) {
                    flightPlanCoordinates.push(new google.maps.LatLng(responseData.locationList[i].latitude, responseData.locationList[i].longitude));
                }

                destinationLat = responseData.locationList[responseData.locationList.length - 1].latitude;
                destinationLng = responseData.locationList[responseData.locationList.length - 1].longitude;

                initializeFlightPlanCoordinates(flightPlanCoordinates);

                sLatlng = new google.maps.LatLng(responseData.locationList[0].latitude, responseData.locationList[0].longitude);

                var siconImage = basePath + "/assets/image/user_pin_1.png";

                smarker = new google.maps.Marker({
                    map: map,
                    icon: siconImage,
                    position: sLatlng
                });

                markers.push(smarker);

                var disIcon = basePath + "/assets/image/user_pin_2.png";

                dLatlng = new google.maps.LatLng(destinationLat, destinationLng);

                dmarker = new google.maps.Marker({
                    map: map,
                    icon: disIcon,
                    position: dLatlng
                });

                markers.push(dmarker);
            }
        }
    });
}

function updateFinalAmountCollected() {

    var newFinalAmtCollectedACDialog = jQuery('#newFinalAmtCollectedACDialog').val();
    var remarkACDialog = jQuery('#remarkACDialog').val();
    var isPassengerReleased = false;

    if (jQuery(".releasePassengerDiv .checkBoxImg").hasClass("visited")) {
        isPassengerReleased = true;

        console.log("isPassengerReleased: " + isPassengerReleased);
    }

    console.log("tourId: " + jQuery("#tourId").val());
    console.log("newFinalAmtCollectedACDialog: " + newFinalAmtCollectedACDialog);
    console.log("remarkACDialog: " + remarkACDialog);
    console.log("isPassengerReleased: " + isPassengerReleased);
    console.log("Valid data ..");

    jQuery.ajax({
        url: basePath + "/admin-bookings-details/amount-collected/update.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "tourId": jQuery("#tourId").val(),
            "newFinalAmtCollectedACDialog": newFinalAmtCollectedACDialog,
            "remarkACDialog": remarkACDialog,
            "isPassengerReleased": isPassengerReleased
        },
        success: function(responseData) {

            jQuery("#newFinalAmtCollectedACDialogError").html("");
            jQuery("#remarkACDialogError").html("");

            if (responseData.type === "Success") {

                jQuery('#updateAmountCollectedDialog').modal('toggle');

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/admin-bookings-details.do?tourId=" + jQuery("#tourId").val() + "&tourType=bookings");

            } else if (responseData.type === "Failed") {

                displayBootstrapMessage(responseData.message);

            } else {

                validateACDialogFields(responseData);
            }
        }
    });
}

function validateACDialogFields(data) {

    console.log("validateACDialogFields called..");

    var newFinalAmtCollectedACDialogError = "";
    var remarkACDialogError = "";

    if ('newFinalAmtCollectedACDialogError' in data) {
        newFinalAmtCollectedACDialogError = data.newFinalAmtCollectedACDialogError;
    }

    if ('remarkACDialogError' in data) {
        remarkACDialogError = data.remarkACDialogError;
    }

    var p1Error = errorMessageFunction("newFinalAmtCollectedACDialog", newFinalAmtCollectedACDialogError);
    var p2Error = errorMessageFunction("remarkACDialog", remarkACDialogError);

    if (p1Error || p2Error) {
        return false;
    }
}