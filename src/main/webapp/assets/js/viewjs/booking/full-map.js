var currentLat = 18.587607;
var currentLng = 73.738059;
var map;
var bound;
var markers = [];
var marker;
var myLatLng;
var currentLatLng;
var driverListAjax;
var ajaxInProgress = false;
var mapInitailizeCount = 0;

jQuery(document).ready(function() {

    getNearestCarListFunction();

    jQuery("#showAllSpan").click(function() {

        mapInitailizeCount = 0;

        location.reload();
    });
});

function mapInitailize() {

    currentLatLng = new google.maps.LatLng(currentLat, currentLng);
    mapOptions = {
        zoom: 12,
        center: currentLatLng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("mapCanvas"), mapOptions);

    var refreshId = setInterval(getNearestCarListFunction, 15000);
}

function getNearestCarListFunction() {

    if (ajaxInProgress === true) {
        return;
    }

    ajaxInProgress = true;

    driverListAjax = jQuery.ajax({
        url: basePath + "/car-near-by.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {},
        error: function() {
            ajaxInProgress = false;

            if (mapInitailizeCount == 0) {
                mapInitailize();
                mapInitailizeCount = 1;
            }
        },
        success: function(responseData) {

            console.log("before if In map initailize mapInitailizeCount : " + mapInitailizeCount);

            if (mapInitailizeCount == 0) {

                mapInitailize();

                bound = new google.maps.LatLngBounds();

                mapInitailizeCount = 1;

                console.log("in if In map initailize mapInitailizeCount : " + mapInitailizeCount);
            }

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

                for (var i = 0; i < markers.length; i++) {
                	
                    markers[i].setMap(null);
                }

                markers.length = 0;

                for (var j = 0; j < nearestCarArr.length; j++) {

                    currentLat = nearestCarArr[j].latitude;
                    currentLng = nearestCarArr[j].longitude;

                    if (currentLat !== null && currentLng !== null) {
                        break;
                    }
                }

                for (var k = 0; k < nearestCarArr.length; k++) {

                    currentLat = nearestCarArr[k].latitude;
                    currentLng = nearestCarArr[k].longitude;
                    tourId = nearestCarArr[k].tourId;

                    myLatlng = new google.maps.LatLng(currentLat, currentLng);

                    var iconImage = "";

                    if (tourId === "-1") {
                        iconImage = basePath + "/assets/image/car_icons/" + nearestCarArr[k].carIcon + "/" + nearestCarArr[k].carIcon + "_free.png";
                    } else {
                        iconImage = basePath + "/assets/image/car_icons/" + nearestCarArr[k].carIcon + "/" + nearestCarArr[k].carIcon + "_busy.png";
                    }

                    contentString = "";

                    contentString = "<div id='content' style='width:300px;height:100px;'>" +

                        "<div id='leftMenu' style='height:120px;width:88px;float:left;'>" +
                        "<img  src=" + basePath + nearestCarArr[k].photoUrl + " style='height:78px;width:88px;padding-top:25px;border-radius:60px;'>" +
                        "</div>" +

                        "<div id='rightMenu' style='height:120px;width:200px;margin-left: 100px;'>" +
                        "<h1 id='firstHeading' class='firstHeading' style='font-size:18px;'>" +
                        nearestCarArr[k].firstName + " " + nearestCarArr[k].lastName;
                    
                    if (tourId === "-1")
                        contentString += "<img  src='" + basePath + "/assets/image/car_icons/" + nearestCarArr[k].carIcon + "/" + nearestCarArr[k].carIcon + "_free.png"' style='height:20px;width:29px;float:left;margin-right:10px;padding-top:5px;'>";
                    else
                        contentString += "<img  src='" + basePath + "/assets/image/car_icons/" + nearestCarArr[k].carIcon + "/" + nearestCarArr[k].carIcon + "_busy.png"' style='height:20px;width:29px;float:left;margin-right:10px;padding-top:5px;'>";
                    
                    contentString += "</h1>" +
                        "<div id='bodyContent'>" +
                        "<div>" +
                        "<img  src=" + basePath + "/assets/image/icon_phone.jpg style='height:20px;width:29px;float:left;'>" +
                        "<p style='font-size:14px;margin-left: 38px;margin-top: 2px;'>" + nearestCarArr[k].phoneNo + "</p></br>" +
                        "</div>" +
                        "<div>" +
                        "<img  src=" + basePath + "/assets/image/icon_email.jpg style='height:20px;width:29px;float:left;'>" +
                        "<p style='font-size:14px;margin-left: 38px;margin-top: 2px;'>" + nearestCarArr[k].email + "</p></br>" +
                        "</div>" +
                        "<div>";
                    for (var i = 0; i < 5; i++) {

                        if (i < nearestCarArr[k].ratings)
                            contentString += "<img  src=" + basePath + "/assets/image/ratings_star.png style='height:20px;width:29px;float:left;margin-left:10px;'>";
                        else
                            contentString += "<img  src=" + basePath + "/assets/image/star.jpg style='height:20px;width:29px;float:left;margin-left:10px;'>";
                    }

                    contentString += "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>";

                    marker = new google.maps.Marker({
                        map: map,
                        icon: iconImage,
                        position: myLatlng,
                        info: contentString
                    });

                    markers.push(marker);

                    console.log("before if Bound mapInitailizeCount : " + mapInitailizeCount);

                    bound.extend(myLatlng);

                    if (mapInitailizeCount == 1) {
                        map.fitBounds(bound);
                        mapInitailizeCount = 2;

                        console.log("in if Bound mapInitailizeCount : " + mapInitailizeCount);
                    }

                    var infowindow;

                    google.maps.event.addListener(marker, 'mouseover', function() {
                        infowindow = new google.maps.InfoWindow({});
                        infowindow.setContent(this.info);
                        infowindow.open(map, this);
                    });

                    google.maps.event.addListener(marker, 'mouseout', function() {
                        infowindow.close(map, this);
                    });

                }
            }

            ajaxInProgress = false;
        }
    });
}

function close_window() {
    close();
}