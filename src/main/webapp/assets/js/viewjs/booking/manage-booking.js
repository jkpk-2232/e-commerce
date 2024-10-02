var markers = [];
var currentLat = "";
var currentLng = "";
var map;
var bound;
var directionsService = new google.maps.DirectionsService();
var salesChart = new Chart();
let storeLatLngObject = {
	lat:'',
	lng:'',
	title:''
}
let storesLatLngList = [];

jQuery(document).ready(function() {

   setMenuActive("1");
  // setMenuActiveSub("44");

    setMultiSelectPicker("regionList");
    
	if (jQuery("#roleId").val() == '12') {

		loadDatatables();
		getSalesDashboard();

		if (jQuery("#salesChart")) {
			// Destroy the existing chart
			salesChart.destroy();
		}

	} else {
		
		jQuery("#isFirstTimeHidden").val("YES");

    	currentLat = jQuery("#currentLat").val();
    	currentLng = jQuery("#currentLng").val();
    	
		initializeMap();
	}
    
	jQuery("#vendorStoreId").change(function() {

		loadDatatables();
		getSalesDashboard();

		if (jQuery("#salesChart")) {
			// Destroy the existing chart
			salesChart.destroy();
		}
	});

    
    jQuery("#totalproducts").click(function(e) {
		       e.preventDefault();
		       redirectToUrl("/manage-orders-all-others.do");
		   });

		   jQuery("a[href='#Feeds']").click(function(e) {
		       e.preventDefault();
		       redirectToUrl("/manage-vendor-feeds.do");
		   });

		   jQuery("a[href='#Active Feeds']").click(function(e) {
		       e.preventDefault();
		       redirectToUrl("/brand-accounts.do");
		   });

		   jQuery("a[href='#totalSubscribers']").click(function(e) {
		       e.preventDefault();
		       redirectToUrl("/manage-subscribers.do");
		   });

    

    var $elem = jQuery("#regionList");
	$elem.picker();
	$elem.on("sp-change", function() {
		jQuery("#isFirstTimeHidden").val("YES");
        initializeMap();
	});
	
	// Data for the line chart
				const salesData1 = {
				    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
				    datasets: [
				        {
				            label: 'Subscriber Sales',
				            data: [], // Empty data to simulate "No data available"
				            backgroundColor: 'rgba(189, 191, 194, 1)',
				            borderColor: 'rgba(189, 191, 194, 1)',
				            borderWidth: 1,
				            fill: true
				        },
				        {
				            label: 'Non-Subscriber Sales',
				            data: [], // Empty data to simulate "No data available"
				            backgroundColor: 'rgba(7, 224, 152, 1)',
				            borderColor: 'rgba(7, 224, 152, 1)',
				            borderWidth: 1,
				            fill: true
				        }
				    ]
				};
				// Custom plugin to adjust the legend height
				const plugin1 = {
				    beforeInit(chart) {
				        // Get a reference to the original fit function
				        const originalFit = chart.legend.fit;
				        chart.legend.fit = function fit() {
				            originalFit.bind(chart.legend)();
				            // Increase the height of the legend to create space
				            this.height += 30; // Adjust this value to control the space
				        };
				    }
				};
				const ctx1 = $("#salesChart1");
				const salesChart1 = new Chart(ctx1, {
				    type: 'line',
				    data: salesData1,
				    options: {
				        scales: {
				            y: {
				                beginAtZero: true
				            }
				        }
				    },
				    plugins: [plugin1] // Add the custom plugin here
				});

	
				
				
				const inventoryData = {
				    labels: ['Third Party Stores', 'Own Stores'],
				    datasets: [
				        {
				            label: ' ',
				            data: [23, 28],
				            backgroundColor: [
				                'rgba(56, 212, 164, 1)',
				                'rgba(50, 133, 119, 1)'
				            ],
				            borderColor: [
				                'rgba(56, 212, 164, 1)',
				                'rgba(50, 133, 119, 1)'
				            ],
				            borderWidth: 1
				        },
				    ]
				};

				// Custom plugin to adjust the legend height
				const plugin = {
				    beforeInit(chart) {
				        // Get a reference to the original fit function
				        const originalFit = chart.legend.fit;
				        chart.legend.fit = function fit() {
				            originalFit.bind(chart.legend)();
				            // Increase the height of the legend to create space
				            this.height += 30; // Adjust this value to control the space
				        };
				    }
				};

				// Custom plugin to draw the total value in the middle and text on each segment
				const textOnDoughnutPlugin = {
				    afterDraw(chart) {
				        const { ctx, chartArea: { left, right, top, bottom } } = chart;
				        const datasets = chart.data.datasets[0];
				        const meta = chart.getDatasetMeta(0); // Get the meta data for the first dataset
				        const total = datasets.data.reduce((acc, val) => acc + val, 0);

				        // Draw the total value in the center
				        const centerX = (left + right) / 2;
				        const centerY = (top + bottom) / 2;
				        ctx.save();
				        ctx.font = 'bold 18px Arial';
				        ctx.textAlign = 'center';
				        ctx.textBaseline = 'middle';
				        ctx.fillStyle = '#808080'; // Text color for total value
				        ctx.fillText(`Total: ${total}`, centerX, centerY);
				        ctx.restore();

				        // Loop through each segment and place only the value inside
				        meta.data.forEach((element, index) => {
				            const value = datasets.data[index];

				            // Get the position to place the text (center of each arc)
				            const { x, y } = element.tooltipPosition();

				            // Draw only the value on each segment
				            ctx.save();
				            ctx.font = 'bold 14px Arial';
				            ctx.textAlign = 'center';
				            ctx.textBaseline = 'middle';
				            ctx.fillStyle = '#000000'; // Text color
				            ctx.fillText(`${value}`, x, y); // Show only the value on the segment
				            ctx.restore();
				        });
				    }
				};

				const inventoryChartConfig = {
				    type: 'doughnut',
				    data: inventoryData,
				    options: {
				        responsive: true,
				        plugins: {
				            legend: {
				                position: 'top',
				            },
				        }
				    },
				    plugins: [plugin, textOnDoughnutPlugin] // Add both custom plugins
				};

				// Create the chart with the custom plugins
				const inventoryChart = new Chart(
				    $("#inventoryChart"),
				    inventoryChartConfig
				);



	

  

  
	/*function loadDatatables() {

		var erpProductsData = jQuery("#datatableDefault").dataTable({
			"bDestroy": true,
			"bJQueryUI": true,
			"paging": false,
			"searching": false,
			"info": false,
			"sPaginationType": "full_numbers",
			"bProcessing": true,
			"bServerSide": true,
			"sAjaxSource": basePath + "/bookings/erp-products.json",
			"fnServerData": function(sSource, aoData, fnCallback) {
				aoData.push({
					"name": "vendorStoreId",
					"value": jQuery("#vendorStoreId").val()
				}
				);
				$.getJSON(sSource, aoData, function(json) {
					fnCallback(json)
				});
			},
			"aoColumns": [{
				"bSearchable": false,
				"bVisible": false,
				"asSorting": ["desc"]
			}, {
				"sWidth": "10%",
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
			}, {
				"sWidth": "10%",
				"bSortable": false
			}, {
				"sWidth": "10%",
				"bSortable": false
			}]
		});
		*/
		initializeDateRangePicker("customDateRange");

		    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
		        reinitializeStartEndDate(picker.startDate, picker.endDate);
		        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
		    });
	
		function initDataTable(id) {
				    return jQuery("#" + id).dataTable({
				        "bDestroy": true,
				        "bJQueryUI": true,
				        "paging": false,
				        "searching": false,
				        "info": false,
				        "sPaginationType": "full_numbers",
				        "bProcessing": true,
				        "bServerSide": true,
				        "sAjaxSource": basePath + "/bookings/erp-products.json",
				        "fnServerData": function(sSource, aoData, fnCallback) {
				            aoData.push({
				                "name": "vendorStoreId",
				                "value": jQuery("#vendorStoreId").val()
				            });
				            $.getJSON(sSource, aoData, function(json) {
				                fnCallback(json);
				            });
				        },
				        "aoColumns": [
				            {"bSearchable": false, "bVisible": false, "asSorting": ["desc"]},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false}
				        ],
				        "scrollY": "40vh",  // Custom height for scrolling
				        "scrollX": true,    // Allow horizontal scrolling
				        "scrollCollapse": false,
				        "fixedHeader": true // Enable fixed header
				    });
				}

				function initEnlargedDataTable(id) {
				    return jQuery("#" + id).dataTable({
				        "bDestroy": true,
				        "bJQueryUI": true,
				        "paging": false,
				        "searching": false,
				        "info": false,
				        "sPaginationType": "full_numbers",
				        "bProcessing": true,
				        "bServerSide": true,
				        "sAjaxSource": basePath + "/bookings/erp-products.json",
				        "fnServerData": function(sSource, aoData, fnCallback) {
				            aoData.push({
				                "name": "vendorStoreId",
				                "value": jQuery("#vendorStoreId").val()
				            });
				            $.getJSON(sSource, aoData, function(json) {
				                fnCallback(json);
				            });
				        },
				        "aoColumns": [
				            {"bSearchable": false, "bVisible": false, "asSorting": ["desc"]},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false},
				            {"sWidth": "10%", "bSortable": false}
				        ],
				        "scrollY": "80vh",  // Full-screen height, adjust as needed
				        "scrollX": true,                   // Allow horizontal scrolling
				        "scrollCollapse": false,
				        "fixedHeader": true                // Enable fixed header
				    });
				}

				function loadDatatables() {
				    var erpProductsData = initDataTable("datatableDefault");

				    // Full-screen mode logic
				    jQuery("#enlargeModalBtn").on("click", function() {
				        var modal = jQuery("#datatableModal");
				        modal.modal('show');

				        // Remove the modal backdrop
				        modal.on('shown.bs.modal', function() {
				            jQuery(".modal-backdrop").remove();
				            // Initialize the DataTable in the modal with the full-screen settings
				            var erpProductsDataEnlarged = initEnlargedDataTable("datatableDefault_enlarged");
				            erpProductsDataEnlarged.columns.adjust().draw();
				        });
				    });

				    jQuery("#closeModalBtn").on("click", function() {
				        jQuery("#datatableModal").modal('hide');
				    });

				    // Apply CSS overloads if any
				    commonCssOverloadForDatatable("datatableDefault_enlarged");
				    commonCssOverloadForDatatable("datatableDefault");
				}


    
				function getSalesDashboard() {
					const ctx = $("#salesChart")[0].getContext('2d');

					// Custom plugin to add spacing between the legend and the chart
					const plugin = {
						beforeInit(chart) {
							// Get a reference to the original fit function
							const originalFit = chart.legend.fit;
							chart.legend.fit = function fit() {
								originalFit.bind(chart.legend)();
								// Increase the height of the legend to create space
								this.height += 30; // Adjust this value to control the space
							};
						}
					};

					var erpDashboardData = jQuery.ajax({
						url: basePath + "/bookings/erp-dashboard.json?vendorStoreId=" + jQuery("#vendorStoreId").val(),
						type: 'GET',
						cache: false,
						dataType: 'json',
						timeout: 50000,
						success: function(responseData) {

							const labelsList = JSON.parse(responseData.labelsList);
							const onlineList = JSON.parse(responseData.onlineList);
							const offlineList = JSON.parse(responseData.offlineList);

							const salesData = {
								labels: labelsList,
								datasets: [
									{
										label: 'Online Sales',
										data: onlineList,
										backgroundColor: 'rgba(189, 191, 194, 1)',
										borderColor: 'rgba(189, 191, 194, 1)',
										barPercentage: 0.7, 
										categoryPercentage: 0.6,
										maxBarThickness: 20
									},
									{
										label: 'Offline Sales',
										data: offlineList,
										backgroundColor: 'rgba(84, 231, 184, 1)',
										borderColor: 'rgba(84, 231, 184, 1)',
										borderWidth: 1,
										barPercentage: 0.7, 
										categoryPercentage: 0.6,
										maxBarThickness: 20
									}
								]
							};

							salesChart = new Chart(ctx, {
								type: 'bar',
								data: salesData,
								options: {
									scales: {
										y: {
											beginAtZero: true
										}
									}
								},
								plugins: [plugin]  // Add the custom plugin here
							});		


				const latList = JSON.parse(responseData.latList);
				const lngList = JSON.parse(responseData.lngList);
				const titleList = JSON.parse(responseData.titleList);

				storesLatLngList = [];

				for (i = 0; i < titleList.length; i++) {
					storeLatLngObject = new Object();
					storeLatLngObject.lat = latList[i];
					storeLatLngObject.lng = lngList[i];
					storeLatLngObject.title = titleList[i];
					storesLatLngList.push(storeLatLngObject);

				}

				if (titleList.length > 0) {
					var mapOptions = {
						zoom: 12,
						center: new google.maps.LatLng(storesLatLngList[0].lat, storesLatLngList[0].lng)
					};

					var map = new google.maps.Map($('#dashboardMapCanvas')[0], mapOptions);

					for (i = 0; i < storesLatLngList.length; i++) {

						var marker = new google.maps.Marker({
							position: new google.maps.LatLng(storesLatLngList[i].lat, storesLatLngList[i].lng),
							map: map,
							title: storesLatLngList[i].title
						});

					}

				}
			}
		});
	}
    
   
    
});
  
function initializeMap() {

    console.log("initializeMap called..");

    var dLatlng = new google.maps.LatLng(currentLat, currentLng);

    mapOptions = {
        zoom: 15,
        center: dLatlng,
        mapTypeId: google.maps.MapTypeId.ROAD
    };

    map = new google.maps.Map(document.getElementById("dashboardMapCanvas"), mapOptions);

    if (jQuery("#isFirstTimeHidden").val() === "YES") {

        getNearestCarListFunction();

//        var refreshId = setInterval(getNearestCarListFunction, 15000);
    }
}

function set_taxiMarkers(map, locations) {

    var marker, i;

    for (var j = 0; j < markers.length; j++) {

        markers[j].setMap(null);
    }

    markers.length = 0;

    if (jQuery("#isFirstTimeHidden").val() === "YES") {
        bound = new google.maps.LatLngBounds();
    }

    for (i = 0; i < locations.length; i++) {

        var lat = locations[i][0];
        var long = locations[i][1];
        var driverName = locations[i][2];
        var tourId = locations[i][3];
        var locationUpdatedWithinIdealTime = locations[i][4];
        var carIcon = locations[i][5];
        var driverId = locations[i][6]; 

        latlngset = new google.maps.LatLng(lat, long);

        var ciconImage = "";

        if (tourId === "-1") {
            if (locationUpdatedWithinIdealTime === "YES") {
                ciconImage = basePath + "/assets/image/car_icons/" + carIcon + "/" + carIcon + "_free.png";
            } else {
                ciconImage = basePath + "/assets/image/car_icons/" + carIcon + "/" + carIcon + "_ideal.png";
            }
        } else {
            ciconImage = basePath + "/assets/image/car_icons/" + carIcon + "/" + carIcon + "_busy.png";
        }

        var marker = new google.maps.Marker({
            map: map,
            title: driverName,
            position: latlngset,
            icon: ciconImage
        });

        //@formatter:off
        var content = "<div><a href='#' class='list-group-item list-group-item-action d-flex align-items-center text-inverse'>" + 
	    	"<div class='w-40px h-40px d-flex align-items-center justify-content-center ms-n1'>" +
	    		"<img src='" + ciconImage + "' alt='' class='ms-100 mh-100 rounded-circle'>" +
	    	"</div>" +
	    	"<div class='flex-fill ps-3 d-flex align-items-center'>" +
	    		"<div class='fw-bold flex-fill' style='color:red;'>" +
	    			driverName +
	    		"</div>" +
	    	"</div>" +
	    "</a></div>";
	   //@formatter:on
        
        var infowindow = new google.maps.InfoWindow()

        google.maps.event.addListener(marker, 'click', (function(marker, content, infowindow) {
            return function() {
                infowindow.setContent(content);
                infowindow.open(map, marker);
            };
        })(marker, content, infowindow));

        markers.push(marker);

        if (jQuery("#isFirstTimeHidden").val() === "YES") {

            bound.extend(latlngset);
        }
    }

    if (locations.length > 0) {

        if (jQuery("#isFirstTimeHidden").val() === "YES") {

            map.fitBounds(bound);
        }

        jQuery("#isFirstTimeHidden").val("NO");
    }
}

function getNearestCarListFunction() {

    var driverListAjax = jQuery.ajax({
        url: basePath + "/car-near-by.json?regionList=" + jQuery("#regionList").picker("get"),
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        success: function(responseData) {

            var updatedDriverAvailableCount = responseData.updatedDriverAvailableCount;

            jQuery("#availableDriversCount").html("");

            jQuery("#availableDriversCount").html(updatedDriverAvailableCount);

            if (responseData.carMapList.length > 0) {

                var x = new Array(responseData.carMapList.length);

                for (var i = 0; i < responseData.carMapList.length; i++) {

                    x[i] = new Array(7);

                    x[i][0] = responseData.carMapList[i].latitude;
                    x[i][1] = responseData.carMapList[i].longitude;
                    x[i][2] = responseData.carMapList[i].driverName;
                    x[i][3] = responseData.carMapList[i].tourId;
                    x[i][4] = responseData.carMapList[i].locationUpdatedWithinIdealTime;
                    x[i][5] = responseData.carMapList[i].carIcon;
                    x[i][6] = responseData.carMapList[i].driverId;
                }

                set_taxiMarkers(map, x);
            }
        }
    });
}