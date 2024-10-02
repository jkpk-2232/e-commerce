var map;
var markers = [];
var latlngbounds = new google.maps.LatLngBounds();
var taxiDatatable;

jQuery(document).ready(function() {

    var tabId = "";

    if (jQuery("#type").val() === "orders-new-tab") {
        tabId = "2";
    } else if (jQuery("#type").val() === "orders-active-tab") {
        tabId = "3";
    } else {
        tabId = "4";
    }

    setMenuActiveSub(tabId);

    loadDatatable();

    initializeMap();

    jQuery("#btnChangeOrderStatus").click(function(e) {
    	
    	e.preventDefault();
    	
        changeOrderStatus();
    });
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});

function changeOrderStatus() {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/view-order/vendor/order-delivery-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "orderId": jQuery("#orderId").val(),
            "orderStatusFilter": jQuery("#orderStatusFilter").val(),
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
    var dLatLng = new google.maps.LatLng(jQuery("#orderDeliveryLat").val(), jQuery("#orderDeliveryLng").val());

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

function loadDatatable() {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/view-order/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "orderId",
                "value": jQuery("#orderId").val()
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