var map;
var markers = [];
var latlngbounds = new google.maps.LatLngBounds();
var taxiDatatable;

jQuery(document).ready(function() {

    var tabId = "";

    if (jQuery("#type").val() === "appointments-new-tab") {
        tabId = "33";
    } else if (jQuery("#type").val() === "appointments-active-tab") {
        tabId = "34";
    } else {
        tabId = "35";
    }

    setMenuActiveSub(tabId);

    loadDatatable();

    initializeMap();

    jQuery("#btnChangeAppointmentStatus").click(function(e) {
    	
    	e.preventDefault();
    	
        changeAppointmentStatus();
    });
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});

function changeAppointmentStatus() {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/view-appointment/vendor/appointment-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "appointmentId": jQuery("#appointmentId").val(),
            "appointmentStatusFilter": jQuery("#appointmentStatusFilter").val(),
            "type": jQuery("#type").val()
        },
        success: function(responseData) {

            console.log("\n\n\tresponseData.type\t" + responseData.type);
            console.log("\n\n\tresponseData.message\t" + responseData.message);

            if (responseData.type === "ERROR") {
                displayBootstrapMessage(responseData.message);
            } else {
                displayBootstrapMessageWithRedirectionUrl(responseData.message, responseData.url);
            }
        }
    });
}

function initializeMap() {

    var sLatLng = new google.maps.LatLng(jQuery("#storeAddressLat").val(), jQuery("#storeAddressLng").val());

    var latlng = [
        sLatLng,
    ];

    for (var i = 0; i < latlng.length; i++) {
        latlngbounds.extend(latlng[i]);
    }

    var mapOptions = {
        zoom: 15,
        center: sLatLng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);

    var siconImage = basePath + "/assets/image/user_pin_1.png";
    var smarker = new google.maps.Marker({
        map: map,
        icon: siconImage,
        position: sLatLng
    });

    markers.push(smarker);

    map.fitBounds(latlngbounds);
}

function loadDatatable() {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/view-appointment/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "appointmentId",
                "value": jQuery("#appointmentId").val()
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json)
            });
        },
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "100%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
}