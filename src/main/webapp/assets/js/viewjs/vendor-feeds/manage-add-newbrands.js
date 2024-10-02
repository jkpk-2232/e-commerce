jQuery(document).ready(function () {
    setMenuActiveSub("46");

    jQuery("#btnAddVendorFeed").click(function (e) {
        e.preventDefault();
        redirectToUrl(ADD_URL);
    });

    initializeDateRangePicker("customDateRange");

    jQuery("#customDateRange").on("apply.daterangepicker", function (ev, picker) {
        reinitializeStartEndDate(picker.startDate, picker.endDate);
        loadDatatable(getDatatableFormattedDate(picker.startDate), getDatatableFormattedDate(picker.endDate));
    });

    loadDatatable(startDateFormatted, endDateFormatted);

    jQuery("#vendorId").change(function () {
        loadDatatable(startDateFormatted, endDateFormatted);
    });
});

function loadDatatable(tempStartDate, tempEndDate) {
    var staticData = [
        // Example row data: [SrNo, ProductCategory, ProductName, BrandName, MRP, Discount, BoxQuantityMin, BoxQuantityMax, RequiredQty, Barcode, BatchNo, Expiry]
        ['001', 'Category A', 'Product X', 'Brand X', 1000, 10, 5, 10, 50, '123456789012', 'Batch001', '2024-12-31'],
        ['002', 'Category B', 'Product Y', 'Brand Y', 1500, 15, 3, 6, 30, '234567890123', 'Batch002', '2025-03-15'],
        ['003', 'Category C', 'Product Z', 'Brand Z', 2000, 20, 2, 4, 20, '345678901234', 'Batch003', '2024-11-20'],
        ['004', 'Category D', 'Product W', 'Brand W', 2500, 25, 7, 14, 70, '456789012345', 'Batch004', '2024-10-10']
    ];

    // Column Definitions
    var columnDefs = [
        { "title": "SrNo" },
        { "title": "ProductCategory" },
        { "title": "ProductName" },
        { "title": "BrandName" },
        { "title": "MRP" },
        { "title": "Discount" },
        { "title": "BoxQuantityMin" },
        { "title": "BoxQuantityMax" },
        { "title": "RequiredQty" },
        { "title": "Barcode" },
        { "title": "BatchNo" },
        { "title": "Expiry" }
    ];

    // Initialize DataTable
    jQuery("#datatableDefault").DataTable({
        "bDestroy": true,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": false,
        "data": staticData,  // Use the static data directly
        "columns": columnDefs,  // Dynamically set columns
        "columnDefs": [
            { "width": "5%", "targets": 0 },  // SrNo
            { "width": "10%", "targets": 1 }, // ProductCategory
            { "width": "10%", "targets": 2 }, // ProductName
            { "width": "10%", "targets": 3 }, // BrandName
            { "width": "10%", "targets": 4 }, // MRP
            { "width": "10%", "targets": 5 }, // Discount
            { "width": "5%", "targets": 6 }, // BoxQuantityMin
            { "width": "5%", "targets": 7 }, // BoxQuantityMax
            { "width": "5%", "targets": 8 }, // RequiredQty
            { "width": "10%", "targets": 9 }, // Barcode column for barcode generation
            { "width": "10%", "targets": 10 },// BatchNo
            { "width": "10%", "targets": 11 } // Expiry
        ],
		"createdRow": function (row, data, dataIndex) {
		    // For Barcode column
		    var barcodeValue = data[9];  // Barcode is at index 9
		    var barcodeCell = jQuery('td', row).eq(9);  // Get the correct cell
		    barcodeCell.html('<svg class="barcode"></svg>');  // Set SVG as barcode placeholder
		    JsBarcode(barcodeCell.find('.barcode')[0], barcodeValue, {
		        format: "CODE128",  // You can set other formats like CODE39, EAN, UPC, etc.
		        displayValue: true,
		        height: 20,  // You can adjust height if needed
		        width: 1  // Reduce width of each bar to make the barcode narrower
		    });
		},

        "initComplete": function () {
            commonCssOverloadForDatatable("datatableDefault");
        }
    });
}
