var activeStatus;
var FirstVehicleId = "1";
var SecondVehicleId = "2";
var ThirdVehicleId = "3";
var FourthVehicleId = "4";
var FifthVehicleId = "5";
var SeventhVehicleId = "7";
var ajaxInProgress1 = false;
var approvelStatus;
var carStatus;

jQuery(document).ready(function() {
	jQuery(':input[type="number"]').attr("min","0");
});

jQuery("input").on("keypress", function(e) {
    if (e.which === 32 && !this.value.length)
        e.preventDefault();
});

jQuery("textarea").on("keypress", function(e) {
    if (e.which === 32 && !this.value.length)
        e.preventDefault();
});

setTimeout(function() { 
	jQuery("#sucMessage").hide();
}, 5000);

setInterval(function() {
	getCriticalRideLaterCount();
}, 30000);


function getCriticalRideLaterCount() {
	
    jQuery.ajax({
        url: basePath + "/manage-critical-ride-later/critical-tour-count.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        async : false,
        timeout: 50000,
        success: function(responseData) {
            
        	jQuery("#rideLaterCriticalCount").html(responseData.crticalTourCount);
        }
    });
}

function editItem(url) {
    window.location.assign(url);
}

function viewItem(url) {
    window.location.assign(url);
}

function clickUrl(url) {
    window.location.assign(url);
}

function deactivateItem(url) {
    var a = deactivateDialog(url);
}

function reactivateItem(url) {
    var a = reactivateDialog(url);
}

function deleteItem(url) {
    var a = deleteDialog(url);
}

function deactivateDialog(url) {
	
    var message = "Are you sure you want to deactivate this record?";
    
    displayConfirmationBootstrapMessage(message, url)
}

function reactivateDialog(url) {
	
    var message = "Are you sure you want to reactivate this record?";
    displayConfirmationBootstrapMessage(message, url);
}

function deleteDialog(url) {
	
    var message = "Are you sure you want to delete this record?";
    
    displayConfirmationBootstrapMessage(message, url);
}

function actionUser(userId, userType, url) {
	
	activateDeactivateUser(userId, userType, url);
}

function actionVendorAction(userId, userType, url, msg1) {
	displayConfirmationBootstrapMessage(msg1, url)
}

function takeRide(url, type) {
	
	var msg1 = "";
	if(type == "take-ride") {
		msg1 = "Are you sure you want to mark this trip as Take Bookings for other vendors?";	
	} else {
		msg1 = "Are you sure you want to mark this trip as UnTake Bookings from other vendors?";
	}
	displayConfirmationBootstrapMessage(msg1, url)
}

function displayBootstrapMessage(msg) {
	
	bootbox.alert({
		title: "Alert!",
		message: msg
	});
}

function displayConfirmationBootstrapMessage(message, url) {
	
	bootbox.confirm({
        title: "Warning!",
        message: message,
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> No'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> Yes'
            }
        },
        callback: function (result) {
        	
        	if(result) {
        		window.location.assign(url);
        	}
        }
    });
} 

function displayBootstrapMessageWithRedirectionUrl(msg, url) {
	
	bootbox.confirm({
        title: "Alert!",
        message: msg,
        buttons: {
            confirm: {
                label: '<i class="fa fa-check"></i> Ok'
            }
        },
        callback: function (result) {
        	window.location.assign(url);
        }
    });
}

function getStatusOfUser(userId, userType) {
    jQuery.ajax({
        url: basePath + "/activate-deactivate-user/status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        async : false,
        timeout: 50000,
        data: {
            "userId": userId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
            activeStatus = responseData.status;
        }
    });
}

function activateDeactivateUser(userId, userType, url) {
 
	var message;
    
	getStatusOfUser(userId, userType);

    var textActivateMessage = "Are you sure you want to activate the";
    var textDeactivateMessage = "Are you sure you want to deactivate the";

    if (activeStatus != 'active') {
        message = textActivateMessage + ' ' + userType + "?";
    } else {
        message = textDeactivateMessage + ' ' + userType + "?";
    }
    
    bootbox.confirm({
        title: "Warning!",
        message: message,
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> No'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> Yes'
            }
        },
        callback: function (result) {
        	
        	if(result) {
        		
                jQuery.ajax({
                    url: basePath + "/activate-deactivate-user.json",
                    type: 'GET',
                    cache: false,
                    dataType: 'json',
                    timeout: 50000,
                    data: {
                        "userId": userId
                    },
                    error: function() {
                        ajaxInProgress = false;
                    },
                    success: function(responseData) {

                        if (responseData.type == "SELF") {

                        	displayBootstrapMessage("Cannot deactivate own.");

                        } else if (responseData.type == "SUCCESS") {

                            if (responseData.userType == "driver") {

                                if (responseData.past == "active") {

                                    var message = responseData.labelDeactivatedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;

                                } else {

                                    var message = responseData.labelActivatedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;
                                }

                            } else {

                                if (responseData.past == "active") {

                                    var message = responseData.labelDeactivatedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;

                                } else {

                                    var message = responseData.labelActivatedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;
                                }
                            }

                            displayBootstrapMessageWithRedirectionUrl(msg, url);

                        } else {

                            if (responseData.past == "active") {

                                if (responseData.userType == "passenger" || responseData.userType == "driver") {

                                    if (responseData.flag == "0") {
                                    	displayBootstrapMessage(responseData.userTypeFailMessage);
                                    } else {

                                        var msgTxt = responseData.labelFailToDeactivate;
                                        displayBootstrapMessageWithRedirectionUrl(msgTxt + " " + responseData.userTypeCommon, url);
                                    }

                                } else {

                                    var txt = responseData.labelFailToDeactivate;
                                    displayBootstrapMessage(txt + " " + responseData.userTypeCommon);
                                }
                            } else {
                                var txt = responseData.labelFailToActiavate;
                                displayBootstrapMessage(txt + " " + responseData.userTypeCommon);
                            }
                        }
                    }
                });
        		
        	} else {
        		
        	}
        }
    });
}

function fnGetSelected(oTableLocal) {
	
    var anSelected = fnGetSelectedTr(oTableLocal);

    if (anSelected[0] !== undefined) {
        var rows = [];
        for (var i = 0; i < anSelected.length; i++) {
            var aPos = oTableLocal.fnGetPosition(anSelected[i]);
            var aData = oTableLocal.fnGetData(aPos);
            rows.push(aData);
        }

        return rows;
    } else {
        return null;
    }
}

function fnGetSelectedTr(oDataTable) {
	
    var aReturn = new Array();
    
    var aTrs = oDataTable.fnGetNodes();

    for (var i = 0; i < aTrs.length; i++) {
        if ($(aTrs[i]).hasClass('row_selected')) {
            aReturn.push(aTrs[i]);
        }
    }
    
    return aReturn;
}

function getAllStatesByCountryId(countryId) {

    jQuery.ajax({
        url: basePath + "/state.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "countryId": countryId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {

            stateSelectOptionHtml = "";

            for (var i in responseData) {

                stateSelectOptionHtml += "<option value='" + i + "'>" + responseData[i] + "</option>";

            }
        }
    });
}

function getAllCitiesByStateId(stateId) {

    jQuery.ajax({
        url: basePath + "/city.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "stateId": stateId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {

            citySelectOptionHtml = "";

            for (var i in responseData) {
                citySelectOptionHtml += "<option value='" + i + "'>" + responseData[i] + "</option>";
            }
        }
    });
}

jQuery("#btnFirstVehicle").click(function() {

    jQuery(".errorMessage").html("");
    jQuery("#sucMessage").html("");

    ajaxCallToGetCarFare(FirstVehicleId);
    jQuery("#carType").val(FirstVehicleId);

    jQuery("#btnSecondVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnSecondVehicle .SecondVehicleImg").removeClass('SecondVehicleSelect');
    jQuery("#btnThirdVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnThirdVehicle .ThirdVehicleImg").removeClass('ThirdVehicleSelect');
    jQuery("#btnFourthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFourthVehicle .FourthVehicleImg").removeClass('FourthVehicleSelect');
    jQuery("#btnFifthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFifthVehicle .FifthVehicleImg").removeClass('FifthVehicleSelect');
   
    jQuery("#btnFirstVehicle .FirstVehicleImg").addClass('FirstVehicleSelect');
    jQuery(this).addClass('leftButtonActiveClass');
});

jQuery("#btnSecondVehicle").click(function() {

    jQuery(".errorMessage").html("");
    jQuery("#sucMessage").html("");

    ajaxCallToGetCarFare(SecondVehicleId);
    jQuery("#carType").val(SecondVehicleId);
    
    jQuery("#btnFirstVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFirstVehicle .FirstVehicleImg").removeClass('FirstVehicleSelect');
    jQuery("#btnThirdVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnThirdVehicle .ThirdVehicleImg").removeClass('ThirdVehicleSelect');
    jQuery("#btnFourthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFourthVehicle .FourthVehicleImg").removeClass('FourthVehicleSelect');
    jQuery("#btnFifthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFifthVehicle .FifthVehicleImg").removeClass('FifthVehicleSelect');
    
    jQuery("#btnSecondVehicle .SecondVehicleImg").addClass('SecondVehicleSelect');
    jQuery(this).addClass('leftButtonActiveClass');
});

jQuery("#btnThirdVehicle").click(function() {

    jQuery(".errorMessage").html("");
    jQuery("#sucMessage").html("");

    ajaxCallToGetCarFare(ThirdVehicleId);
    jQuery("#carType").val(ThirdVehicleId);

    jQuery("#btnFirstVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFirstVehicle .FirstVehicleImg").removeClass('FirstVehicleSelect');
    jQuery("#btnSecondVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnSecondVehicle .SecondVehicleImg").removeClass('SecondVehicleSelect');
    jQuery("#btnFourthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFourthVehicle .FourthVehicleImg").removeClass('FourthVehicleSelect');
    jQuery("#btnFifthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFifthVehicle .FifthVehicleImg").removeClass('FifthVehicleSelect');
    
    jQuery("#btnThirdVehicle .ThirdVehicleImg").addClass('ThirdVehicleSelect');
    jQuery(this).addClass('leftButtonActiveClass');
});

jQuery("#btnFourthVehicle").click(function() {

    jQuery(".errorMessage").html("");
    jQuery("#sucMessage").html("");

    ajaxCallToGetCarFare(FourthVehicleId);
    jQuery("#carType").val(FourthVehicleId);

    jQuery("#btnFirstVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFirstVehicle .FirstVehicleImg").removeClass('FirstVehicleSelect');
    jQuery("#btnSecondVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnSecondVehicle .SecondVehicleImg").removeClass('SecondVehicleSelect');
    jQuery("#btnFifthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFifthVehicle .FifthVehicleImg").removeClass('FifthVehicleSelect');
    jQuery("#btnThirdVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnThirdVehicle .ThirdVehicleImg").removeClass('ThirdVehicleSelect');
    
    jQuery("#btnFourthVehicle .FourthVehicleImg").addClass('FourthVehicleSelect');
    jQuery(this).addClass('leftButtonActiveClass');
});

jQuery("#btnFifthVehicle").click(function() {

    jQuery(".errorMessage").html("");
    jQuery("#sucMessage").html("");

    ajaxCallToGetCarFare(FifthVehicleId);
    jQuery("#carType").val(FifthVehicleId);

    jQuery("#btnFirstVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFirstVehicle .FirstVehicleImg").removeClass('FirstVehicleSelect');
    jQuery("#btnSecondVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnSecondVehicle .SecondVehicleImg").removeClass('SecondVehicleSelect');
    jQuery("#btnFourthVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnFourthVehicle .FourthVehicleImg").removeClass('FourthVehicleSelect');
    jQuery("#btnThirdVehicle").removeClass('leftButtonActiveClass');
    jQuery("#btnThirdVehicle .ThirdVehicleImg").removeClass('ThirdVehicleSelect');
    
    jQuery("#btnFifthVehicle .FifthVehicleImg").addClass('FifthVehicleSelect');
    jQuery(this).addClass('leftButtonActiveClass');
});

function ajaxCallToGetCarFare(carTypeId) {

	if(jQuery(".form-group").hasClass("has-error")) {
		jQuery(".form-group").removeClass("has-error")
	}
	
    driverListAjax = jQuery.ajax({
        url: basePath + "/base-fare/car-fare-details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "carTypeId": carTypeId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {

            jQuery("#initialFare").val(responseData.initialFare);
            jQuery("#perKmFare").val(responseData.perKmFare);
            jQuery("#perMinuteFare").val(responseData.perMinuteFare);
            jQuery("#bookingFees").val(responseData.bookingFees);
            jQuery("#minimumFare").val(responseData.minimumFare);
            jQuery("#discount").val(responseData.discount);
            jQuery("#driverPayablePercentage").val(responseData.driverPayablePercentage);
            jQuery("#freeDistance").val(responseData.freeDistance);
        }
    });
}

jQuery("#btnChangePassword").click(function() {
	jQuery("#changePasswordModal").modal("show"); 
});

jQuery("#btnCancelPopUp").click(function() {
	jQuery("#changePasswordModal").modal("hide"); 
});

jQuery("#btnSubmitPopUp").click(function() {
	postChangePasswordRequest();
});

function postChangePasswordRequest() {

    if (ajaxInProgress1 === true) {
        return;
    }

    ajaxInProgress1 = true;

    passwordChangeAjax = jQuery.ajax({

        url: basePath + "/manage-admin-users/change-password.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            oldPassword: jQuery("#oldPassword").val(),
            newPassword: jQuery("#newPassword").val(),
            confirmPassword: jQuery("#confirmPassword").val()
        },
        success: function(responseData) {

            if (responseData.type == "Success") {

            	displayBootstrapMessage(responseData.message);
            	
                jQuery("#oldPassword").val("");
                jQuery("#newPassword").val("");
                jQuery("#confirmPassword").val("");
                jQuery("#changePasswordModal").modal("hide"); 

            } else if (responseData.type == "Failure") {

            	displayBootstrapMessage(responseData.message);

            } else {

                validateFields(responseData);
            }

            ajaxInProgress1 = false;
        }
    });
}

function validateFields(data) {

    var oldPasswordError = "";
    var newPasswordError = "";
    var confirmPasswordError = "";

    if ('oldPasswordError' in data) {
        oldPasswordError = data.oldPasswordError;
    }

    if ('newPasswordError' in data) {
        newPasswordError = data.newPasswordError;
    }

    if ('confirmPasswordError' in data) {
        confirmPasswordError = data.confirmPasswordError;
    }

    var pulError = errorMessageFunction("oldPassword", oldPasswordError);
    var fnError = errorMessageFunction("newPassword", newPasswordError);
    var lnError = errorMessageFunction("confirmPassword", confirmPasswordError);

    if (pulError || fnError || lnError) {
        return false;
    }
}

function errorMessageFunction(fieldId, errorMessage) {
	
    var hassError = false;

    jQuery("#" + fieldId + "Error").remove();
    jQuery("#" + fieldId).parent().parent().removeClass("has-error");

    if (errorMessage !== null || errorMessage !== "") {

        jQuery("#" + fieldId + "Error").remove();
		jQuery("#" + fieldId).after("<span id='" + fieldId + "Error' class='errorMessage'>" + errorMessage + "</span>");
		jQuery("#" + fieldId).parent().parent().addClass("has-error");
        
        hasError = true;
    }

    return hasError;
}

function clearErrorMessageFunction(fieldId) {
    jQuery("#" + fieldId + "Error").remove();
    jQuery("#" + fieldId).parent().parent().removeClass("has-error");
}

jQuery(".customLiClass").click(function(event) {
	
	if(jQuery(this).attr("id") == "docsLi") {
		jQuery(".dashboardWrapperRightPanelOverlayMainInner").addClass("fullPageClass");
	} else {
		jQuery(".dashboardWrapperRightPanelOverlayMainInner").removeClass("fullPageClass");
	}
});

function approveUserStatus(userId, userType, url) {
	 
	var message;
    
	driverApprovelStatus(userId, userType);
	
	if(carStatus === "Not Attatched") {
		message = "No car attatched to this driver, Do you want to attach the car?";
		displayConfirmationBootstrapMessage(message, basePath + "/edit-driver.do?driverId=" + userId);
		return;
	}

	if(carStatus === "Not Approved") {
		message = "Car attatched to this driver is not yet approved, Do you want to approve the car?";
		displayConfirmationBootstrapMessage(message, basePath + "/vendor-cars.do");
		return;
	}
	
    var textActivateMessage = "Are you sure you want to approve the";
   
    if (approvelStatus != 'approve') {
        message = textActivateMessage + ' ' + userType + "?";
   } 
 
    bootbox.confirm({
        title: "Warning!",
        message: message,
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> No'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> Yes'
            }
        },
        callback: function (result) {
        	
        	if(result) {
        		
                jQuery.ajax({
                    url: basePath + "/activate-deactivate-user/approvel-status.json",
                    type: 'GET',
                    cache: false,
                    dataType: 'json',
                    timeout: 50000,
                    data: {
                        "userId": userId
                    },
                    error: function() {
                        ajaxInProgress = false;
                    },
                    success: function(responseData) {
                    	
                        if (responseData.type == "SELF") {

                        	displayBootstrapMessage("Cannot deactivate own.");

                        } else if (responseData.type == "SUCCESS") {

                            if (responseData.userType == "driver") {

                                if (responseData.past == "approve") {

                                    var message = responseData.labelApprovedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;

                                }
                            } else {

                                if (responseData.past == "approve") {

                                    var message = responseData.labelApprovedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;

                                } else {

                                    var message = responseData.labelApprovelRejectedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;
                                }
                            }

                            displayBootstrapMessageWithRedirectionUrl(msg, url);

                        }
                    }
                });
        		
        	} else {
        		
        	}
        }
    });
}

function approveUser(userId, userType, url) {
	
	if(userType==="driver") {
	
		approveUserStatus(userId, userType, url);
	} else {
		approveCarStatus(userId, userType, url);
	}
	
}

function driverApprovelStatus(userId, userType) {
    jQuery.ajax({
        url: basePath + "/activate-deactivate-user/driver-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        async : false,
        timeout: 50000,
        data: {
            "userId": userId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
        	approvelStatus = responseData.status;
        	carStatus = responseData.carStatus;
        }
    });
}

function approveCarStatus(carId, userType, url) {
	 
	var message;
    
	approvelCarStatus(carId, userType);

    var textActivateMessage = "Are you sure you want to approve the";
    //var textDeactivateMessage = "Are you sure you want to reject the";
   
    if (approvelStatus != 'approve') {
        message = textActivateMessage + ' ' + userType + "?";
   } else {
        message = textDeactivateMessage + ' ' + userType + "?";
    }

 
    bootbox.confirm({
        title: "Warning!",
        message: message,
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> No'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> Yes'
            }
        },
        callback: function (result) {
        	
        	if(result) {
        		
                jQuery.ajax({
                    url: basePath + "/activate-deactivate-user/car-approvel-status.json",
                    type: 'GET',
                    cache: false,
                    dataType: 'json',
                    timeout: 50000,
                    data: {
                        "carId": carId
                    },
                    error: function() {
                        ajaxInProgress = false;
                    },
                    success: function(responseData) {
                    	
                         if (responseData.type == "SUCCESS") {


                                if (responseData.past == "approve") {

                                    var message = responseData.labelApprovedSucesfully;
                                    msg = responseData.userTypeCommon + " " + message;
                                } 

                          displayBootstrapMessageWithRedirectionUrl(msg, url);

                        }
                    }
                });
        		
        	} else {
        		
        	}
        }
    });
}

function approvelCarStatus(carId, userType) {
    jQuery.ajax({
        url: basePath + "/activate-deactivate-user/car-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        async : false,
        timeout: 50000,
        data: {
            "carId": carId
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {
        	approvelStatus = responseData.status;
        }
    });
}

function drawPolygon(map, latLng) {

    if (flightPlanCoordinates.length <= 2) {

        flightPlanCoordinates.push(latLng);
    }

    if (flightPlanCoordinates.length > 2) {

        if (flightPlanCoordinates.length > 3) {

            flightPlanCoordinates.pop();
            flightPlanCoordinates.push(latLng);
            flightPlanCoordinates.push(flightPlanCoordinates[0]);

        } else {

            flightPlanCoordinates.push(flightPlanCoordinates[0]);
        }

        if (polygons.length > 0) {
            polygons.pop().setMap(null);
        }

        var myPolygon = new google.maps.Polygon({
            paths: flightPlanCoordinates,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        });

        myPolygon.setMap(map);

        polygons.push(myPolygon)
    }
}

function addListnerToMap(map) {

    google.maps.event.addListener(map, "click", function(event) {

        var latLng = event.latLng;

        var latitude = (event.latLng.lat());

        var longitude = (event.latLng.lng());

        latlongStr += latitude + " " + longitude + ", ";

        console.log("LatLong str" + latlongStr);

        var marker = new google.maps.Marker({
            position: latLng,
            map: map,
            title: 'Start'
        });

        mymarkersforPolygon.push(marker);

        drawPolygon(map, latLng);
    });
}

function clearMarkersForPolygon() {

    while (mymarkersforPolygon.length) {
        mymarkersforPolygon.pop().setMap(null);
    }

    mymarkersforPolygon.length = 0;

    if (flightPlanCoordinates.length > 0) {

        while (flightPlanCoordinates.length > 0) {

            flightPlanCoordinates.pop();
        }
    }
}

function clearPolygon() {

    latlongStr = "";

    clearMarkersForPolygon();

    if (polygons.length > 0) {
        polygons.pop().setMap(null);
    }

    polygons.length = 0
}

function plotPolygon() {

    var polypoints = [];
    var polyLatitude = [];
    var polyLongitude = [];
    var polygonLatLong = polygonPoints.split(/[ ,]+/);
    var latitude = "";
    var longitude = "";

//    polygonLatLong = polygonLatLong.slice(0, -2);
    
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
        
        if((latitude != null) && (latitude != "") && (latitude != "undefined") && (longitude != null) && (longitude != "") && (longitude != "undefined")){

	        polypoints.push({
	
	            lat: parseFloat(latitude),
	            lng: parseFloat(longitude)
	        });
	        
	        latlongStr += latitude + " " + longitude + ",";
	
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

function selectCarAvailability(selectedCarTypesIds) {

    console.log("selectedCarTypesIds\t" + selectedCarTypesIds);
    var carArray = selectedCarTypesIds.split(",");
    for (var i = 0; i < carArray.length; i++) {
        jQuery("#" + carArray[i].trim() + "_IsAvailable").prop("checked", true);
    }
}

function selectCarAvailabilityWithServiceTypeId(selectedCarTypesIds) {

    console.log("selectedCarTypesIds\t" + selectedCarTypesIds);
    var carArray = selectedCarTypesIds.split(",");
    for (var i = 0; i < carArray.length; i++) {
    	console.log(carArray[i].trim());
        jQuery("#" + carArray[i].trim()).prop("checked", true);
    }
    console.log("---------------------------------------");
}


function getCategoryServiceWise(showAll) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-categories/categories-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "serviceId": jQuery("#serviceId").val(),
            "showAll": showAll
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.categoryIdOptions);
        	jQuery("#categoryId").children('option').remove();
  	       	jQuery("#categoryId").append(responseData.categoryIdOptions);
  	       	jQuery("#categoryId").trigger("chosen:updated");
        }
    });
}

function getCategoryServiceWiseAddEdit(showAll, displayType) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-categories/categories-list-add-edit.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "serviceId": jQuery("#serviceId").val(),
            "showAll": showAll,
            "displayType": displayType
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.categoryIdOptions);
        	jQuery("#categoryId").children('option').remove();
  	       	jQuery("#categoryId").append(responseData.categoryIdOptions);
  	       	jQuery("#categoryId").trigger("chosen:updated");
        }
    });
}

function getServiceTypeWiseVendors(showAll, displayType) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-super-services/service-type-vendors.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "serviceTypeId": jQuery("#serviceTypeId").val(),
            "showAll": showAll,
            "displayType": displayType
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.vendorIdOptions);
        	jQuery("#vendorId").children('option').remove();
  	       	jQuery("#vendorId").append(responseData.vendorIdOptions);
  	       	jQuery("#vendorId").trigger("chosen:updated");
        }
    });
}

function getVendorStoresByVendorId(showAll, multiSelect) {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-store/store-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "vendorId": jQuery("#vendorId").val(),
            "showAll": showAll,
            "multiSelect": multiSelect
        },
        success: function(responseData) {

        	console.log("\n\n\tresponseData.type\t"+responseData.type);
        	console.log("\n\n\tresponseData.carTypeListOptions\t"+responseData.vendorStoreIdOptions);
        	jQuery("#vendorStoreId").children('option').remove();
  	       	jQuery("#vendorStoreId").append(responseData.vendorStoreIdOptions);
  	       	if (multiSelect) {
  	       		jQuery("#vendorStoreId").selectpicker('refresh');
  	       	} else {
  	       		jQuery("#vendorStoreId").trigger("chosen:updated");
  	       	}
        }
    });
}