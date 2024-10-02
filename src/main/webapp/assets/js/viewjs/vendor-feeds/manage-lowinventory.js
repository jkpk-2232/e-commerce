jQuery(document).ready(function() {

	setMenuActiveSub("43");

    jQuery("#btnlowinventory").click(function(e) {

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
});

function loadDatatable(tempStartDate, tempEndDate) {
    var staticData = [
        // Example: [ID, SrNo, VendorName, BrandName, MRP, Discount, Weight, ProductStocks, Action]
        [1, '001', 'Vendor A', 'Brand X', 1000, 10, '500g', colorStock(20), 
         '<button class="btn btn-sm" style="background-color: #808080; color: white; border: none;">Requested</button>'
        ],
        [2, '002', 'Vendor B', 'Brand Y', 1500, 15, '1kg', colorStock(10), 
         '<button class="btn btn-sm btn-success">Add Request</button>'
        ],
        [3, '003', 'Vendor C', 'Brand Z', 2000, 20, '250g', colorStock(20), 
         '<button class="btn btn-sm btn-success">Add Request</button>'
        ],
        [4, '004', 'Vendor D', 'Brand W', 2500, 25, '750g', colorStock(4), 
         '<button class="btn btn-sm btn-success">Add Request</button>'
        ]
        // Add more rows as necessary
    ];

    function colorStock(stock) {
        if (stock < 5) {
            return '<span style="color: #ff0000;">' + stock + '</span>'; // Red for less than 5
        } else if (stock <= 15) {
            return '<span style="color: #ffa500;">' + stock + '</span>'; // Orange for 5 to 15
        } else {
            return '<span style="color: #008000;">' + stock + '</span>'; // Green for above 15
        }
    }

    jQuery("#datatableDefault").DataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": false, // Disable server-side processing
        "data": staticData, // Use the static data directly
        "columnDefs": [
            { "width": "5%", "targets": 0 },  // ID column
            { "width": "5%", "targets": 1 },  // SrNo column
            { "width": "10%", "targets": 2 }, // Vendor Name column
            { "width": "10%", "targets": 3 }, // Brand Name column
            { "width": "10%", "targets": 4 }, // MRP column
            { "width": "10%", "targets": 5 }, // Discount column
            { "width": "10%", "targets": 6 }, // Weight column
            { "width": "10%", "targets": 7 }, // Product Stocks column
            { "width": "15%", "targets": 8, "orderable": false } // Action column
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}

