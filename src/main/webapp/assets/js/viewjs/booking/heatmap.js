var map;
var heatmap;
var coordinatesListAjax;

jQuery(document).ready(function() {
	
	loadHeatMap();
	
	jQuery("#tooggleHeatMap").click(function() {

		toggleHeatmap();
	});
	
	jQuery("#timeFilter").change(function() {

		loadHeatMap();
		
	});

});

function toggleHeatmap() {
    heatmap.setMap(heatmap.getMap() ? null : map);
}

function loadHeatMap(){
	
	var points = [];
	
	coordinatesListAjax = jQuery.ajax({
        url: basePath + "/heatmap/coordinates-list.json?timeFilter=" + jQuery("#timeFilter").val(),
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        error: function() {
        	
            displayMsg("Error processing message.");
        },
        success: function(responseData) {
        	
            console.log("Success..");
            
            for (var i = 0; i < responseData.coordinatesList.length; i++) {
            	
            	points.push(new google.maps.LatLng(parseFloat(responseData.coordinatesList[i].sLatitude), parseFloat(responseData.coordinatesList[i].sLongitude)));
            }
            
            initializeMap(points);

        }
    });
	
}

function initializeMap(points){

	var myLatlng = new google.maps.LatLng(18.5308, 73.8475);
	
	
	var myOptions = {
			  zoom: 13,
			  center: myLatlng,
			  mapTypeId: google.maps.MapTypeId.ROAD
			};
	
	map = new google.maps.Map(document.getElementById("mapCanvas"), myOptions);
	
	heatmap = new google.maps.visualization.HeatmapLayer({
        data: points,
        map: map
    });
}