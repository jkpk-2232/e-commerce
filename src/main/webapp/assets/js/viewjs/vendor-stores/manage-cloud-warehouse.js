let map;
let markers = [];

$(document).ready(function () {
    initMap();
});

function initMap() {
    // Center of Hyderabad
    const hyderabad = { lat: 17.3850, lng: 78.4867 };

    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: hyderabad,
    });

    // Store locations with their corresponding rack data
    const storeData = [
        {
            location: { lat: 17.3850, lng: 78.4867 },
            name: 'Warehouse 1',
            racks: [
                {
                    rackNumber: 'Rack 1',
                    slots: [
                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 100 },
                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 100 },
                        { slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
                        { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
                        { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
                        { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
                        { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
                        { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }
                    ]
                },
				{
				                    rackNumber: 'Rack 2',
				                    slots: [
				                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 100 },
				                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 100 },
				                        { slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
				                        { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
				                        { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
				                        { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
				                        { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
				                        { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }
				                    ]
				                },
								{
								                    rackNumber: 'Rack 3',
								                    slots: [
								                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 100 },
								                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 100 },
								                        { slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
								                        { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
								                        { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
								                        { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
								                        { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
								                        { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }
								                    ]
								                },
												{
												                    rackNumber: 'Rack 4',
												                    slots: [
												                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 100 },
												                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 100 },
												                        { slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
												                        { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
												                        { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
												                        { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
												                        { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
												                        { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }
												                    ]
												                }
                // Additional Rack Data...
            ]
        },
        {
            location: { lat: 17.4329, lng: 78.4080 },
            name: 'Warehouse 2',
            racks: [
                {
                    rackNumber: 'Rack 1',
                    slots: [
                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 90 },
                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 90 },
						{ slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
						                       { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
						                       { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
						                       { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
						                       { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
						                       { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }						   
                    ]
                },
				{
								                    rackNumber: 'Rack 2',
								                    slots: [
								                        { slot_id: 1, slot_name: 'Slot 1', available: true, price: 100 },
								                        { slot_id: 2, slot_name: 'Slot 2', available: false, price: 100 },
								                        { slot_id: 3, slot_name: 'Slot 3', available: true, price: 100 },
								                        { slot_id: 4, slot_name: 'Slot 4', available: false, price: 100 },
								                        { slot_id: 5, slot_name: 'Slot 5', available: true, price: 120 },
								                        { slot_id: 6, slot_name: 'Slot 6', available: true, price: 120 },
								                        { slot_id: 7, slot_name: 'Slot 7', available: false, price: 120 },
								                        { slot_id: 8, slot_name: 'Slot 8', available: true, price: 120 }
								                    ]
								                },
            ]
        }
    ];

    storeData.forEach(function (store, index) {
        const marker = new google.maps.Marker({
            position: store.location,
            map: map,
            title: store.name
        });

        markers.push(marker);

        // Add click event to each marker
        marker.addListener('click', function () {
            $('#warehouse_name').text(store.name);
            loadRackData(store.racks);
        });
    });
}

function loadRackData(racks) {
    $('#rackTablesContainer').empty();

    if (racks.length > 0) {
        let table = '<div class="carousel-item active"><div class="col-12 row">';

        racks.forEach(function (rack, index) {
            if (index !== 0 && index % 2 === 0) {
                table += '</div></div><div class="carousel-item"><div class="col-12 row">';
            }

            table += '<div class="col-xl-6 col-lg-6 col-md-6">';
            table += '<h4 class="text-center">' + rack.rackNumber + '</h4>';
            table += '<table class="table table-bordered"><tbody>';

            let tableHtml = '';
            for (let i = 0; i < rack.slots.length; i += 2) {
                let rowHtml = '<tr>';

                // Slot 1
                const slot1 = rack.slots[i];
                rowHtml += '<td onclick="handleCellClick(this)" class="rack-cell" data-slot-id="' + slot1.slot_id + '" data-price="' + slot1.price + '" data-available="' + slot1.available + '"';
                if (!slot1.available) {
                    rowHtml += ' style="background-color: orange;"';
                }
                rowHtml += ' style="border: 1px solid #ddd; padding: 10px 25px;">' + slot1.slot_name + '</td>';

                // Slot 2 (if exists)
                if (i + 1 < rack.slots.length) {
                    const slot2 = rack.slots[i + 1];
                    rowHtml += '<td onclick="handleCellClick(this)" class="rack-cell" data-slot-id="' + slot2.slot_id + '" data-price="' + slot2.price + '" data-available="' + slot2.available + '"';
                    if (!slot2.available) {
                        rowHtml += ' style="background-color: orange;"';
                    }
                    rowHtml += ' style="border: 1px solid #ddd; padding: 10px 25px;">' + slot2.slot_name + '</td>';
                }

                rowHtml += '</tr>';
                tableHtml += rowHtml;
            }

            table += tableHtml + '</tbody></table></div>';
        });

        table += '</div></div>';
        $('#rackTablesContainer').html(table);
    }
}

// Function to handle the click event on rack cells
function handleCellClick(cell) {
    if ($(cell).data('available')) {
        $(cell).css('background-color', 'green');  // Change color to green
    }
}
