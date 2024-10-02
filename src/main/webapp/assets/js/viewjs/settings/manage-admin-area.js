var sourcePlaceId = "";
var sourcePlaceLat = "";
var sourcePlaceLng = "";
var sourcePlaceCountry = "";
var markerSource = "";
var map = "";
var directionsService = new google.maps.DirectionsService();
var directionsDisplay;
var currentLat = 18.5522;
var currentLng = 73.9436;
var km_meter = 1000;
var adminAreaId = "";
var autocompleteSource = "";
var circle = "";
var langPath = "";

jQuery(document).ready(function() {

    jQuery("#btnAdminArea").addClass("settingsButtonActiveClass");
    jQuery("#btnAdminArea div").addClass("activeSettingsButtonsText")
    jQuery("#btnAdminArea").parent().css({
        'display': 'block'
    });
    
    if (jQuery("#language").val() == 1) {
        langPath = basePath + '/assets/datatable/language/French.lang';
    } else if (jQuery("#language").val() == 2)  {
        langPath = basePath + '/assets/datatable/language/English.lang';
    }else{
    	langPath = basePath + '/assets/datatable/language/English.lang';
    }

    initialize();

    mapInitialize();

    jQuery("#btnSave").click(function() {
    	
    	if (jQuery("#areaDisplayName").val() == "") {
        	displaySucessMsg("Please enter display name.");
        } else if (jQuery("#areaName").val() == "") {
        	displaySucessMsg("Please enter area name.");
        } else {
            document.forms["adminArea"].submit();
        }
    });

    jQuery(document).on('click', '.activeDeactive', function(e) {

        e.preventDefault();

        var adminAreaId = this.id;
        
        var warningDialogHtml = jQuery('<div></div>').html('<br><br><center>Are you sure you want to delete this operating area?</center><br><br>');

        warningDialogHtml.dialog({
            modal: true,
            title: 'Warning!',
            resizable: false,
            draggable: false,
            buttons: {
                Ok: function() {
                	document.location.href = basePath + "/manage-admin-area/delete-area.do?adminAreaId=" + adminAreaId;
                }
            }
        });
    });
    
    jQuery("#btnCancel").click(function() {

        document.location.href = basePath + "/manage-admin-area.do";
    });

    jQuery("#btnSearchAnnouncement").click(function() {

        searchString = jQuery("#searchBooking").val();

        document.location.href = basePath + "/manage-admin-area.do?searchString=" + searchString;
    });

    jQuery("#btnDeleteArea").click(function() {

        if (adminAreaId == "") {

        	displaySucessMsg("No row selected");

        } else {

            document.location.href = basePath + "/manage-admin-area/delete-area.do?adminAreaId=" + adminAreaId;
        }
    });

    jQuery("#radius").change(function() {
    	
    	if(jQuery("#areaName").val() == "") {
    		displaySucessMsg("Please select area 1st.");
    	} else {
    		
    	    circle.setMap(null);
    		
    		getArea();	
    	}
    });
    
    adminAreaTable = jQuery('#adminAreaListTable').dataTable({

        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "iDisplayLength": 5,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-admin-area/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {

            aoData.push({
                "name": "more_data",
                "value": jQuery("#searchBooking").val()
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
            "sWidth": "8%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "60%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }, {
            "sWidth": "10%",
            "bSortable": false
        }],
        "oLanguage": {
            "sUrl": langPath
        }
    });

    jQuery("#adminAreaListTable_first").hide();
    jQuery("#adminAreaListTable_previous").html("<i class='icon-chevron-left'></i>");
    jQuery("#adminAreaListTable_next").html("<i class='icon-chevron-right'></i>");
    jQuery("#adminAreaListTable_last").hide();
});

function initialize() {

    var sinput = document.getElementById('areaName');

    new google.maps.places.Autocomplete(sinput);
}

function mapInitialize() {

    var mapProp = {
        center: new google.maps.LatLng(currentLat, currentLng),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("fareMap"), mapProp);

    var areaName = document.getElementById('areaName');

    autocompleteSource = new google.maps.places.Autocomplete(areaName);
    autocompleteSource.bindTo('bounds', map);

    autocompleteSource.addListener('place_changed', function() {

    	getArea();
    });
}

function getArea() {
		
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

    var radius = jQuery("#radius").val() * km_meter

    circle = new google.maps.Circle({
        map: map,
        radius: radius,
        fillColor: '#AA0000'
    });

    circle.bindTo('center', markerSource, 'position');

    map.fitBounds(circle.getBounds());

    sourcePlaceLat = place.geometry.location.lat();
    sourcePlaceLng = place.geometry.location.lng();
    sourcePlaceId = place.place_id;
    sourcePlaceCountry = place.formatted_address;
    
    var res = sourcePlaceCountry.split(",");
    
    jQuery("#areaCountry").val(res[res.length-1]);
    jQuery("#areaLatitude").val(sourcePlaceLat);
    jQuery("#areaLongitude").val(sourcePlaceLng);
    jQuery("#areaPlaceId").val(sourcePlaceId);
    jQuery("#areaRadius").val(jQuery("#radius").val());
}

function displaySucessMsg(message) {

    var displayStatusDialogHtml = jQuery('<div></div>').html(
        '<br><br><center>' + message + '</center><br><br>');

    displayStatusDialogHtml.dialog({
        modal: true,
        resizable: false,
        draggable: false,
        title: 'Message!',
        buttons: {
            Ok: function() {
                jQuery(this).dialog("close");
            }
        }
    });
}

function fnGetSelected(oTableLocal) {

    var anSelected = fnGetSelectedTr(oTableLocal);

    if (anSelected[0] != undefined) {
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