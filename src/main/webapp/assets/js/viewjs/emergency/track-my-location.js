
var map;
var myLatLng;
var myOptions;
var marker;
var markers = [];
var currentLat;
var currentLng;

//var currentLat= 18.590478;
//var currentLng= 73.727191;

var sourseMarker;
var destMarker;
var sourseLat;
var sourseLng;
var destLat;
var destLng;

var directionsService = new google.maps.DirectionsService();
var directionsDisplay = new google.maps.DirectionsRenderer();

var driverId;
var driverLat;
var driverLng;

var currentDriverLatLng;
var currentDriverMarker;



jQuery(document).ready(function() {
	
	console.log("trackLocationStatus : "+ jQuery("#trackLocationStatus").val());
	
	jQuery("#errorMessageDiv").hide();
	
	if(jQuery("#trackLocationStatus").val() === "INVALID TOKEN"){
		jQuery("#trackingDetailsDiv").hide();
		jQuery("#errorMessageDiv").addClass("error-message-div");
		jQuery("#errorMessageDiv").show();
	}
	
	if(jQuery("#trackLocationStatus").val() === "NOT TOUR"){
		currentLat = parseFloat(jQuery("#currentLat").val());
		currentLng = parseFloat(jQuery("#currentLng").val());
		
		mapInitailize();
	}
	
	if(jQuery("#trackLocationStatus").val() === "ENDED TOUR"){
		sourseLat = parseFloat(jQuery("#sourseLat").val());
		sourseLng = parseFloat(jQuery("#sourseLng").val());
		destLat = parseFloat(jQuery("#destLat").val());
		destLng = parseFloat(jQuery("#destLng").val());
		
		mapInitailizeForEndedTour();
	}
	
	if(jQuery("#trackLocationStatus").val() === "ACTIVE TOUR"){
		sourseLat = parseFloat(jQuery("#sourseLat").val());
		sourseLng = parseFloat(jQuery("#sourseLng").val());
		destLat = parseFloat(jQuery("#destLat").val());
		destLng = parseFloat(jQuery("#destLng").val());
		
		driverId = jQuery("#driverId").val();
		driverLat = parseFloat(jQuery("#driverLat").val());
		driverLng = parseFloat(jQuery("#driverLng").val());
		
		mapInitailizeForActiveTour();
	}
	
	
});

function initailize(latitude, longitude){
	myLatLng = new google.maps.LatLng(latitude, longitude);
    mapOptions = {
        zoom: 15,
        center: myLatLng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };
    
    map = new google.maps.Map(document.getElementById("mapCanvas"), mapOptions);
}


// Code for passenger last location

function mapInitailize() {
	console.log("In mapInitailize");
	
	initailize(currentLat, currentLng);
    
    var siconImage = basePath + "/assets/image/user.png";

    marker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: myLatLng
    });
    markers.push(marker);
    
    var refreshId = setInterval(updateMarker, 5000);
    
}

function updateMarker() {
	
	console.log("id : "+ jQuery("#passengerId").val());
	
	jQuery.ajax({
        url: basePath + "/track-me/passenger/last-location.json?passengerId=" + jQuery("#passengerId").val(),
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
        	console.log("Error.");
//            displayMsg("Error processing message.");
        },
        success: function(responseData) {
            if (responseData.type == "SUCCESS") {
            	
            	var lastUpdatedLat = parseFloat(responseData.passengerGeoLocation.latitude);
            	var lastUpdatedLng = parseFloat(responseData.passengerGeoLocation.longitude);
            	
            	for (var i = 0; i < markers.length; i++ ) {
            		if(i == (markers.length-1)){
            			markers[i].setMap(null);
            		}
            	}
            	markers.length = 0;
            	
            	console.log("For last location latitude : "+ responseData.passengerGeoLocation.latitude);
            	console.log("For last location longitude : "+ responseData.passengerGeoLocation.longitude);
            	
            	var siconImage = basePath + "/assets/image/user.png";            	
            	var myLatLng = new google.maps.LatLng(lastUpdatedLat, lastUpdatedLng);
    			
        		marker = new google.maps.Marker({
        			map: map,
        			icon: siconImage,
        			position: myLatLng
        		});
        			
        		markers.push(marker);            	
            } else if (responseData.type == "INTERNAL ERROR"){
            	console.log("INTERNAL ERROR !!");
            }
        }
    });	
}


// Code for ended tour

function mapInitailizeForEndedTour(){
	console.log("In mapInitailizeForEndedTour");
	
	sourseLatLng = new google.maps.LatLng(sourseLat, sourseLng);
	destLatLng = new google.maps.LatLng(destLat, destLng);
	
	 var diconImage = basePath + "/assets/image/user_pin_2.png";
     var siconImage = basePath + "/assets/image/user_pin_1.png";
     
     initailize(sourseLat, sourseLng);
     
     sourseMarker = new google.maps.Marker({
         map: map,
         icon: siconImage,
         position: sourseLatLng
     });

     markers.push(sourseMarker);
	
     destMarker = new google.maps.Marker({
         map: map,
         icon: diconImage,
         position: destLatLng
     });
     
     markers.push(destMarker);
     
     var polylineOptionsActual = new google.maps.Polyline({
//         strokeColor: "#a59100",
    	 strokeColor: "#008000",
         strokeOpacity: 1.0,
         strokeWeight: 5
     });
     
     directionsDisplay.setMap(null);
     
     directionsDisplay = new google.maps.DirectionsRenderer({
         suppressMarkers: true,
         polylineOptions: polylineOptionsActual
     });
     
     var request = {
             origin: new google.maps.LatLng(sourseLat, sourseLng),
             destination: new google.maps.LatLng(destLat, destLng),
             travelMode: google.maps.TravelMode["DRIVING"]
         };
         
         directionsService.route(request, function(response, status) {
             if (status == google.maps.DirectionsStatus.OK) {
                 directionsDisplay.setDirections(response);
             }
         });

         directionsDisplay.setMap(map);
	
}


// Code for active tour

function mapInitailizeForActiveTour(){
console.log("In mapInitailizeForActiveTour");
	
	sourseLatLng = new google.maps.LatLng(sourseLat, sourseLng);
	destLatLng = new google.maps.LatLng(destLat, destLng);
	currentDriverLatLng = new google.maps.LatLng(driverLat, driverLng);
	
	 var diconImage = basePath + "/assets/image/user_pin_2.png";
     var siconImage = basePath + "/assets/image/user_pin_1.png";
     var driverIconImage = basePath + "/assets/image/user.png";
     
     initailize(sourseLat, sourseLng);
     
     sourseMarker = new google.maps.Marker({
         map: map,
         icon: siconImage,
         position: sourseLatLng
     });

     markers.push(sourseMarker);
	
     destMarker = new google.maps.Marker({
         map: map,
         icon: diconImage,
         position: destLatLng
     });
     
     markers.push(destMarker);
     
     currentDriverMarker = new google.maps.Marker({
         map: map,
         icon: driverIconImage,
         position: currentDriverLatLng
     });
     
     markers.push(currentDriverMarker);

     
     
     var polylineOptionsActual = new google.maps.Polyline({
//         strokeColor: "#FF0000", 
         strokeColor: "#008000",
         strokeOpacity: 1.0,
         strokeWeight: 5
     });
     
     directionsDisplay.setMap(null);
     
     directionsDisplay = new google.maps.DirectionsRenderer({
         suppressMarkers: true,
         polylineOptions: polylineOptionsActual
     });
     
     var request = {
             origin: new google.maps.LatLng(sourseLat, sourseLng),
             destination: new google.maps.LatLng(destLat, destLng),
             travelMode: google.maps.TravelMode["DRIVING"]
         };
         
         directionsService.route(request, function(response, status) {
             if (status == google.maps.DirectionsStatus.OK) {
                 directionsDisplay.setDirections(response);
             }
         });

         directionsDisplay.setMap(map);
         
         var refreshId = setInterval(updateMarkerForActiveTour, 5000);
	
}

function updateMarkerForActiveTour() {
	jQuery.ajax({
        url: basePath + "/track-me/driver/current-location.json?driverId=" + jQuery("#driverId").val(),
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
        	console.log("Error.");
//            displayMsg("Error processing message.");
        },
        success: function(responseData) {
        	
            if (responseData.type == "SUCCESS") {
            	var currentLocLat = parseFloat(responseData.driverGeoLocation.latitude);
            	var currentLocLng = parseFloat(responseData.driverGeoLocation.longitude);
            	
            	for (var i = 0; i < markers.length; i++ ) {
            		if(i == (markers.length-1)){
            			markers[i].setMap(null);
            		}
            	}
            	markers.length = 0;
            	
            	console.log("For active tour latitude : "+ responseData.driverGeoLocation.latitude);
            	console.log("For active tour longitude : "+ responseData.driverGeoLocation.longitude);
            	           	
            	var currentLocLatLng = new google.maps.LatLng(currentLocLat, currentLocLng);
            	sourseLatLng = new google.maps.LatLng(sourseLat, sourseLng);
            	destLatLng = new google.maps.LatLng(destLat, destLng);
            	
            	 var diconImage = basePath + "/assets/image/user_pin_2.png";
                 var siconImage = basePath + "/assets/image/user_pin_1.png";
                 var driverIconImage = basePath + "/assets/image/user_pin.png"; 
    			
                 sourseMarker = new google.maps.Marker({
        	         map: map,
        	         icon: siconImage,
        	         position: sourseLatLng
        	     });
        	
        	     markers.push(sourseMarker);
        		
        	     destMarker = new google.maps.Marker({
        	         map: map,
        	         icon: diconImage,
        	         position: destLatLng
        	     });
        	     
        	     markers.push(destMarker);
        		
        		currentDriverMarker = new google.maps.Marker({
        	        map: map,
        	        icon: driverIconImage,
        	        position: currentLocLatLng
        	    });
        		
        		markers.push(currentDriverMarker);  
        		
            } else if (responseData.type == "INTERNAL ERROR"){
            	
            	console.log("INTERNAL ERROR !!");
            }
        }
    });	
	
}

/*function displayMsg(message) {

    var displayStatusDialogHtml = jQuery('<div></div>').html(
        '<br><br><center>' + message + '</center><br><br>');

    displayStatusDialogHtml.dialog({
        modal: true,
        resizable: false,
        draggable: false,
        title: jQuery("#labelTitle").val(),
        buttons: {
            Ok: function() {
                jQuery(this).dialog("close");
            }
        }
    });
}*/

