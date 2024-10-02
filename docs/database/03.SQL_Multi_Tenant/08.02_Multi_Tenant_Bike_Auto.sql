--01. Add a new car type option "Bike"-----------------------------------

INSERT INTO car_type 
		(
			car_type_id, car_type, car_priority, is_active,
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		(
			'6', 'Bike', '7', true,
			'1', 1558770952000, '1', 1558770952000
		);
		
--02. vendor_car_fare table to keep track of vendor wise car fare
UPDATE car_type SET car_type_id='7' WHERE car_type_id='6';