jQuery(document).ready(function() {

    setMenuActiveSub("45");  // Updated to match the new menu item

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
        // Example: [ID, SrNo, VendorName, BrandName, MRP, Discount, Weight, Expiry, Placeholder for buttons]
        [1, '001', 'Vendor A', 'Brand X', 1000, 10, '500g', '2024-12-31', ''], // Added an empty placeholder at the end
        [2, '002', 'Vendor B', 'Brand Y', 1500, 15, '1kg', '2025-03-15', ''],  // Placeholder for buttons
        [3, '003', 'Vendor C', 'Brand Z', 2000, 20, '250g', '2024-11-20', ''], // Placeholder for buttons
        [4, '004', 'Vendor D', 'Brand W', 2500, 25, '750g', '2024-10-10', '']  // Placeholder for buttons
    ];

    function yesNoButtons() {
        return '<button class="btn btn-sm btn-success">Yes</button>' +
               '<button class="btn btn-sm" style="background-color: #808080; color: white; border: none; margin-left: 5px;">No</button>';
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
            { "width": "10%", "targets": 7 }, // Expiry column
            { "width": "10%", "targets": 8, "createdCell": function(td, cellData) { 
                jQuery(td).html(yesNoButtons()); 
            }} // Yes/No buttons
        ]
    });

    commonCssOverloadForDatatable("datatableDefault");
}
