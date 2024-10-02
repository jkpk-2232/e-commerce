jQuery(document).ready(function() {

    setMenuActiveSub("44");

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
        // Example: [ID, SrNo, VendorName, BrandName, MRP, Discount, Weight, ProductStocks, RequiredQty, MinQty, MaxQty]
        [1, '001', 'Vendor A', 'Brand X', 1000, 10, '500g', colorStock(20), 5, 1, 10],
        [2, '002', 'Vendor B', 'Brand Y', 1500, 15, '1kg', colorStock(10), 10, 2, 20],
        [3, '003', 'Vendor C', 'Brand Z', 2000, 20, '250g', colorStock(20), 7, 3, 15],
        [4, '004', 'Vendor D', 'Brand W', 2500, 25, '750g', colorStock(4), 8, 4, 18]
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

    function requiredQtyBox(requiredQty) {
        return '<div style="display: inline-block; padding: 2px 5px; border: 2px solid green; border-radius: 3px; text-align: center;">' 
                + requiredQty + 
                '</div>';
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
            { "width": "10%", "targets": 8, "createdCell": function(td, cellData) { 
                jQuery(td).html(requiredQtyBox(cellData)); 
            }}, // Required Qty column with green border
            { "width": "10%", "targets": 9 }, // Min Qty column
            { "width": "10%", "targets": 10 } // Max Qty column
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
