jQuery(document).ready(function() {

   // setMenuActiveSub("55");
	
   setMenuActiveSub("52");

	jQuery("#btnAddErpEmployee").click(function(e) {
		
        e.preventDefault();
        
        redirectToUrl(ADD_URL);
    });

    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);

    jQuery("#vendorId").change(function() {
        loadDatatable(startDateFormatted, endDateFormatted);
    });
	// Load the doughnut chart
	   loadDoughnutChart();

	   // Load Google Map after DOM is ready
	   initializeMap();
});

function loadDatatable(tempStartDate, tempEndDate) {

    var taxiDatatable = jQuery("#datatableDefault").dataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": basePath + "/manage-erp-employees/list.json",
        "fnServerData": function(sSource, aoData, fnCallback) {
            aoData.push({
                "name": "startDate",
                "value": tempStartDate
            }, {
                "name": "endDate",
                "value": tempEndDate
            },{
                "name": "vendorId",
                "value": jQuery("#vendorId").val()
            });
            $.getJSON(sSource, aoData, function(json) {
                fnCallback(json);
            });
        },
        "aoColumns": [{
            "bSearchable": false,
            "bVisible": false,
            "asSorting": ["desc"]
        }, {
            "sWidth": "1%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }, {
            "sWidth": "5%",
            "bSortable": false
        }]
    });

    commonCssOverloadForDatatable("datatableDefault");
	
	// Employee data with two values: Online and Offline
	const employeeData = {
	    labels: ['Online', 'Offline'],
	    datasets: [
	        {
	            label: ' ',
	            data: [23, 28],  // Static data values for Online and Offline
	            backgroundColor: [
	                'rgba(56, 212, 164, 1)',
	                'rgba(50, 133, 119, 1)'
	            ],
	            borderColor: [
	                'rgba(56, 212, 164, 1)',
	                'rgba(50, 133, 119, 1)'
	            ],
	            borderWidth: 1
	        }
	    ]
	};

	// Custom plugin to draw the total value in the center of the doughnut and value on each segment
	const textOnDoughnutPlugin = {
	    afterDraw(chart) {
	        const { ctx, chartArea: { left, right, top, bottom } } = chart;
	        const datasets = chart.data.datasets[0];
	        const total = datasets.data.reduce((acc, val) => acc + val, 0);
	        const meta = chart.getDatasetMeta(0);

	        // Draw the total value in the center
	        const centerX = (left + right) / 2;
	        const centerY = (top + bottom) / 2;
	        ctx.save();
	        ctx.font = 'bold 18px Arial';
	        ctx.textAlign = 'center';
	        ctx.textBaseline = 'middle';
	        ctx.fillStyle = '#808080';
	        ctx.fillText(`Total: ${total}`, centerX, centerY);
	        ctx.restore();

	        // Place the value inside each segment
	        meta.data.forEach((element, index) => {
	            const value = datasets.data[index];
	            const { x, y } = element.tooltipPosition();
	            ctx.save();
	            ctx.font = 'bold 14px Arial';
	            ctx.textAlign = 'center';
	            ctx.textBaseline = 'middle';
	            ctx.fillStyle = '#000000';
	            ctx.fillText(`${value}`, x, y);
	            ctx.restore();
	        });
	    }
	};

	// Updated doughnut chart configuration for employee data
	const employeeChartConfig = {
	    type: 'doughnut',
	    data: employeeData,
	    options: {
	        responsive: true,
	        maintainAspectRatio: false,
	        plugins: {
	            legend: {
	                position: 'right',  // Move legend to the right
	                align: 'end',  // Align legend at the bottom right
	                labels: {
	                    boxWidth: 10,  // Make the legend boxes small (dots)
	                    padding: 20,   // Add padding around the legend
	                    usePointStyle: true  // Use dots instead of square boxes
	                }
	            }
	        },
	        layout: {
	            padding: {
	                top: 10,
	                bottom: 10,
	            }
	        }
	    },
	    plugins: [textOnDoughnutPlugin]
	};

	// Initialize employee doughnut chart
	const employeeChart = new Chart(
	    $("#employeeChart"),
	    employeeChartConfig
	);

	    function initializeMap() {
	           const storesLatLngList = [
	               { lat: 28.7041, lng: 77.1025, title: 'Store 1' },
	               { lat: 19.0760, lng: 72.8777, title: 'Store 2' }
	           ];

	           const mapOptions = {
	               zoom: 12,
	               center: new google.maps.LatLng(storesLatLngList[0].lat, storesLatLngList[0].lng)
	           };

	           const map = new google.maps.Map($('#dashboardMapCanvas')[0], mapOptions);

	           storesLatLngList.forEach(store => {
	               new google.maps.Marker({
	                   position: new google.maps.LatLng(store.lat, store.lng),
	                   map: map,
	                   title: store.title
	               });
	           });
	       }

	       initializeMap();
	   }