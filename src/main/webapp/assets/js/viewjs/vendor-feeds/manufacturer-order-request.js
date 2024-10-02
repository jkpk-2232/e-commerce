jQuery(document).ready(function() {

    setMenuActiveSub("47");

    jQuery("#btnAddVendorFeed").click(function(e) {
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
	    // Sample row 1
	    [1, '001', 'Product A', 'Store X', '500g', 50, 5, 10, 1000, '1 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '5 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '1234567890', 'B001', '2024-12-31', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 2
	    [2, '002', 'Product B', 'Store Y', '1kg', 100, 10, 20, 2000, '2 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '10 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '0987654321', 'B002', '2024-11-30', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 3
	    [3, '003', 'Product C', 'Store Z', '250g', 30, 3, 7, 700, '3 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '3 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '1231231234', 'B003', '2025-01-31', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 4
	    [4, '004', 'Product D', 'Store A', '750g', 75, 8, 15, 1500, '4 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '8 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '3213213210', 'B004', '2024-10-15', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 5
	    [5, '005', 'Product E', 'Store B', '600g', 60, 6, 12, 1200, '5 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '6 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '9876543210', 'B005', '2025-02-28', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 6
	    [6, '006', 'Product F', 'Store C', '400g', 40, 4, 8, 800, '6 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '4 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '0123456789', 'B006', '2024-09-30', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 7
	    [7, '007', 'Product G', 'Store D', '300g', 35, 2, 6, 600, '7 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '2 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '3211234567', 'B007', '2025-03-15', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 8
	    [8, '008', 'Product H', 'Store E', '150g', 20, 1, 4, 400, '8 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '1 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '9871234567', 'B008', '2024-08-31', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 9
	    [9, '009', 'Product I', 'Store F', '200g', 25, 3, 5, 500, '9 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '3 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '6543210987', 'B009', '2024-07-31', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	    // Sample row 10
	    [10, '010', 'Product J', 'Store G', '800g', 90, 12, 18, 1800, '10 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '12 <i class="fa-regular fa-pen-to-square" style="color: #3cd2a5;"></i>', '4567891230', 'B010', '2025-04-30', 
	     '<div style="display: flex; gap: 5px;">' +
	     '<button class="btn btn-sm btn-success">Accept</button>' +
	     '<button class="btn btn-sm btn-danger">Reject</button>' +
	     '</div>'
	    ],
	];

    jQuery("#datatableDefault").DataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": false,
        "data": staticData,
        "createdRow": function (row, data, dataIndex) {
            var barcodeValue = data[11];  // Barcode is at index 11
            var barcodeCell = jQuery('td', row).eq(11);  // Get the correct cell
            barcodeCell.html('<svg class="barcode"></svg>');  // Set SVG as barcode placeholder

            // Use JsBarcode with CODE39 format
            JsBarcode(barcodeCell.find('.barcode')[0], barcodeValue, {
                format: "CODE128",  // Using "CODE128" format
                displayValue: true,
                height: 10,  // You can adjust height if needed
                width: 1  // Adjust width for better readability
            });
        },
        "columnDefs": [
            { "width": "5%", "targets": 0 },
            { "width": "5%", "targets": 1 },
            { "width": "10%", "targets": 2 },
            { "width": "10%", "targets": 3 },
            { "width": "5%", "targets": 4 },
            { "width": "5%", "targets": 5 },
            { "width": "5%", "targets": 6 },
            { "width": "5%", "targets": 7 },
            { "width": "10%", "targets": 8 },
            { "width": "5%", "targets": 9 },
            { "width": "5%", "targets": 10 },
            { "width": "10%", "targets": 11 },
            { "width": "5%", "targets": 12 },
            { "width": "10%", "targets": 13 },
            { "width": "10%", "targets": 14, "orderable": false }
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
