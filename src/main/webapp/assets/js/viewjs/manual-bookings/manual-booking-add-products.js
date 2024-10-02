$(document).ready(function() {
    const productData = [
        { "labelSrNo": 1, "labelProductName": "Product 1", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 2, "labelProductName": "Product 2", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 3, "labelProductName": "Product 3", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 4, "labelProductName": "Product 4", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 5, "labelProductName": "Product 5", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 6, "labelProductName": "Product 6", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 7, "labelProductName": "Product 7", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 },
        { "labelSrNo": 8, "labelProductName": "Product 8", "labelProductValue": "₹ 45", "labelPrice": 45, "initialPrice": 45 }
    ];

    const staticInfo = {
        driverName: "John Doe",
        driverPhone: "123-456-7890",
        customerName: "Jane Smith",
        customerPhoneNo: "098-765-4321",
        orderDeliveryAddress: "123 Main St, Anytown, USA"
    };

    function loadDatatables() {
        // DataTable for datatableDefault
        const $datatable = $("#datatableDefault");

        if ($.fn.DataTable.isDataTable($datatable)) {
            $datatable.DataTable().clear().destroy();
        }

        $datatable.DataTable({
            paging: false,
            info: false,
            searching: false,
            data: productData,
            columns: [
                { data: "labelSrNo", width: "10%", orderable: false, title: "Sr. No." },
                { data: "labelProductName", width: "30%", orderable: false, title: "Product Name" },
                { data: "labelProductValue", width: "20%", orderable: false, title: "Product Value" },
                {
                    data: "labelPrice",
                    width: "20%",
                    orderable: false,
                    title: "Price",
                    render: (data) => `₹ ${data} <div class='price-separator'></div>`
                },
                {
                    data: null,
                    width: "20%",
                    orderable: false,
                    title: "Quantity",
                    defaultContent: "<button class='btn btn-outline-primary btn-sm minus-btn green-btn'>-</button> <span class='qty'>1</span> <button class='btn btn-outline-primary btn-sm plus-btn green-btn'>+</button>"
                }
            ],
            createdRow: (row, data) => {
                $(row).data({
                    initialPrice: data.initialPrice,
                    currentQty: 1
                });
            }
        });

        // Handle quantity button clicks for datatableDefault
        $datatable.on('click', 'button.plus-btn, button.minus-btn', function () {
            const $button = $(this);
            const $row = $button.closest('tr');
            const $qtyElement = $button.siblings('.qty');
            let currentQty = parseInt($qtyElement.text());
            const initialPrice = $row.data('initialPrice');

            if ($button.hasClass('plus-btn')) {
                currentQty++;
            } else if ($button.hasClass('minus-btn')) {
                if (currentQty > 1) {
                    currentQty--;
                }
            }

            $qtyElement.text(currentQty);
            const newPrice = initialPrice * currentQty;
            $row.find('td:eq(3)').html(`₹ ${newPrice} <div class='price-separator'></div>`);
            $row.data('currentQty', currentQty);

            updateSummary(); // Update summary when quantity changes
        });

        commonCssOverloadForDatatable("datatableDefault");
    }

    function loadStaticInfo() {
        $('#driverName').text(staticInfo.driverName);
        $('#driverPhone').html(`<i class="bi bi-telephone-fill fa-fw"></i> ${staticInfo.driverPhone}`);
        $('#customerName').text(staticInfo.customerName);
        $('#customerPhoneNo').html(`<i class="bi bi-telephone-fill fa-fw"></i> ${staticInfo.customerPhoneNo}`);
        $('#orderDeliveryAddress').text(staticInfo.orderDeliveryAddress);

        updateSummary(); // Initial summary update
    }

    function updateSummary() {
        let totalItems = 0;
        let discountedPrice = 0;
        let estimatedCost = 20; // Static delivery charge
        let actualPrice = 0;
        let grossPrice = 0;
        let gst = 0;

        $('#datatableDefault tbody tr').each(function() {
            const $row = $(this);
            const qty = $row.data('currentQty');
            const price = $row.data('initialPrice') * qty;

            totalItems += qty;
            actualPrice += price;
            discountedPrice += price * 0.9; // 10% discount
        });

        grossPrice = discountedPrice * 1.04; // 4% price increase
        gst = discountedPrice * 0.18; // 18% GST
        estimatedCost = actualPrice + gst + 20; // Adding GST and delivery charge

        $('#totalItems').val(totalItems);
        $('#productDiscountedPrice').val(discountedPrice.toFixed(2));
        $('#estimatedCost').val(estimatedCost.toFixed(2));
        $('#productActualPrice').val(actualPrice.toFixed(2));
        $('#grossPrice').val(grossPrice.toFixed(2));
        $('#gst').val(gst.toFixed(2));
    }

    loadDatatables();
    loadStaticInfo();
});
