jQuery(document).ready(function() {
    // Set active menu item
    //setMenuActiveSub("52");

	setMenuActiveSub("53");
	
    // Static inventory data for the doughnut chart
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
            }
        ]
    };

    // Custom plugin to adjust the legend height for the doughnut chart
    const legendHeightPlugin = {
        beforeInit(chart) {
            const originalFit = chart.legend.fit;
            chart.legend.fit = function fit() {
                originalFit.bind(chart.legend)();
                this.height += 30;  // Increase the height of the legend
            };
        }
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

    // Doughnut chart configuration
	const inventoryChartConfig = {
	    type: 'doughnut',
	    data: inventoryData,
	    options: {
	        responsive: true,
	        maintainAspectRatio: false,
	        plugins: {
	            legend: {
	                position: 'right',  // Move legend to the right
	                align: 'end',  // Align legend at the bottom of the right side
	                labels: {
	                    boxWidth: 10,  // Make the legend boxes small (dots)
	                    padding: 20,   // Add some padding around the legend
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

    // Initialize inventory doughnut chart
    const inventoryChart = new Chart(
        $("#inventoryChart"),
        inventoryChartConfig
    );

    // Date range picker initialization
    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function(ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    // Initialize sales dashboard chart with static data
    const ctx = $("#salesChart")[0].getContext('2d');

    // Custom plugin to add spacing between the legend and the chart
    const salesLegendSpacingPlugin = {
        beforeInit(chart) {
            const originalFit = chart.legend.fit;
            chart.legend.fit = function fit() {
                originalFit.bind(chart.legend)();
                this.height += 30;  // Add space between legend and chart
            };
        }
    };

    // Static data for the sales chart
    const salesData = {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [
            {
                label: 'Online Sales',
                data: [12000, 15000, 8000, 18000, 20000, 15000, 25000],
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                barPercentage: 0.7,
                categoryPercentage: 0.6,
                maxBarThickness: 20
            },
            {
                label: 'Offline Sales',
                data: [10000, 12000, 6000, 15000, 18000, 12000, 22000],
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
                barPercentage: 0.7,
                categoryPercentage: 0.6,
                maxBarThickness: 20
            }
        ]
    };

    // Initialize sales bar chart
    const salesChart = new Chart(ctx, {
        type: 'bar',
        data: salesData,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        },
        plugins: [salesLegendSpacingPlugin]
    });

    getSalesDashboard();
    
	// Function to initialize Google Map with store markers
	function initializeMap() {
	    // Static data for map markers
	    const storesLatLngList = [
	        { lat: 28.7041, lng: 77.1025, title: 'Store 1' },
	        { lat: 19.0760, lng: 72.8777, title: 'Store 2' }
	    ];

	    if (storesLatLngList.length > 0) {
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
	}

	// Initialize Google Map with static data
	initializeMap();
});
