--1---------------------------------------------------------------------------------------

INSERT INTO roles 
		(
			role_id, role, created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('1','super admin','1',1444203417000,'1',1444203417000), 
		('2','admin','1',1444203417000,'1',1444203417000), 
		('3','business owner','1',1444203417000,'1',1444203417000), 
		('4','operator','1',1444203417000,'1',1444203417000),
		('5','passenger','1',1444203417000,'1',1444203417000),
		('6','driver','1',1444203417000,'1',1444203417000),
		('7','vendor','1',1444203417000,'1',1444203417000);
		
--2---------------------------------------------------------------------------------------
		
INSERT INTO users 
		(
			user_id, email, password, photo_url,
			is_active, is_deleted, role_id, is_notification_send_status,
			is_on_duty, created_by, created_at, updated_by, updated_at
		) 
VALUES 
		(
			'1','admin@myhub.in','N/2sPQEMykY=',null,TRUE,FALSE,'1',TRUE,FALSE,
			'1',1444203417000,'1',1444203417000
		);
		
--3---------------------------------------------------------------------------------------	
			
INSERT INTO user_info 
		(
			user_info_id, user_id, first_name, last_name, phone_no, phone_no_code,
  			driving_license, joining_date, mail_address_line_1, mail_address_line_2,
  			mail_zip_code, bill_address_line_1, bill_address_line_2, bill_zip_code,
  			gender,card_type, account_number, mail_country_id, mail_state_id, mail_city_id,
  			bill_country_id, bill_state_id, bill_city_id, tip, application_status, 
  			driving_license_photo, verification_code, is_verified, is_same_as_mailing,
  			created_by, created_at, updated_by, updated_at 
		) 
VALUES 
		(
			'1','1','Admin','MyHub','1234567890','+91', null,1444203417000,
			'6-3- 348/10/501, 5 th Floor, Nirmal Towers Dwarakapuri colony,','Punjagutta Hyderabad, Telangana, India','500082',
			'6-3- 348/10/501, 5 th Floor, Nirmal Towers Dwarakapuri colony,','Punjagutta Hyderabad, Telangana, India','500082',
			'Male',null,'1',223,3460,139799, 223,3460,139799,0,null,
			 null,null,FALSE,FALSE,'1',1444203417000,'1',1444203417000
		);
		
--4---------------------------------------------------------------------------------------
		
INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 
		(1,'Dashboard','A',1,1444203417000,'1',1444203417000,'1'),
		(2,'Manual Bookings','A',2,1444203417000,'1',1444203417000,'1'), 
		(4,'Admin User','A',5,1444203417000,'1',1444203417000,'1'),
		(5,'Passengers','A',6,1444203417000,'1',1444203417000,'1'),
		(6,'Drivers','A',7,1444203417000,'1',1444203417000,'1'),
		(7,'Cars','A',10,1444203417000,'1',1444203417000,'1'),
		(9,'Operators','A',12,1444203417000,'1',1444203417000,'1'),
		(10,'Broadcast','A',13,1444203417000,'1',1444203417000,'1'),
		(11,'Refund','A',14,1444203417000,'1',1444203417000,'1'),
		(12,'Dashboard','A',15,1444203417000,'1',1444203417000,'1'),
		(13,'My Bookings','A',16,1444203417000,'1',1444203417000,'1'),
		(15,'Bookings','A',4,1444203417000,'1',1444203417000,'1'),
		(16,'Reports','A',18,1444203417000,'1',1444203417000,'1'),
		(17,'Ride Later','A',8,1444203417000,'1',1444203417000,'1'),
		(18,'Critical Ride Later','A',9,1444203417000,'1',1444203417000,'1'),
		(19,'Fare Calculator','A',19,1444203417000,'1',1444203417000,'1'),
		(21,'Vendors','A',21,1444203417000,'1',1444203417000,'1');

--5---------------------------------------------------------------------------------------	
		
INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 
		(1,1,'Dashboard','/bookings.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(2,2,'Manual Bookings','/manual-bookings.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-book'),
		(4,4,'Admin User','/manage-admin-users.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-user'),
		(5,5,'Passengers','/manage-passengers.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-user'),
		(6,6,'Drivers','/manage-drivers.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-road'),
		(7,7,'Cars','/manage-cars.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-cars'),
		(9,9,'Operators','/reports.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-signal'),
		(10,10,'Broadcast','/announcements.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-bullhorn'),
		(11,11,'Refund','/refund.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-bank'),
		(12,12,'Dashboard','/bookings.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(13,13,'My Bookings','/my-bookings.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(15,15,'Bookings','/admin-bookings.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(16,16,'Reports','/manage-driver-reports.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(17,17,'Ride Later','/manage-ride-later.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(18,18,'Critical Ride Later','/manage-critical-ride-later.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home'),
		(19,19,'Fare Calculator','/fare-calculator.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon-calculator'),
		(21,21,'Vendors','/manage-vendors.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-user');

--6---------------------------------------------------------------------------------------	

INSERT INTO url_accesses 
		(
			user_id, url_id, record_status, created_at, 
			created_by, updated_at, updated_by
		) 
VALUES 
		('1',1,'A',1444203417000,'1',1444203417000,'1'),
		('1',2,'A',1444203417000,'1',1444203417000,'1'),
		('1',4,'A',1444203417000,'1',1444203417000,'1'),
		('1',5,'A',1444203417000,'1',1444203417000,'1'),
		('1',6,'A',1444203417000,'1',1444203417000,'1'),
		('1',7,'A',1444203417000,'1',1444203417000,'1'),
		('1',10,'A',1444203417000,'1',1444203417000,'1'),
		('1',11,'A',1444203417000,'1',1444203417000,'1'),
		('1',15,'A',1444203417000,'1',1444203417000,'1'),
		('1',16,'A',1444203417000,'1',1444203417000,'1'),
		('1',21,'A',1444203417000,'1',1444203417000,'1');
		
--7---------------------------------------------------------------------------------------	

INSERT INTO car_type 
		(
			car_type_id, car_type, 
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('1','Auto','1',1444203417003,'1',1444203417003),
		('2','Mini','1',1444203417003,'1',1444203417003),
		('3','Sedan','1',1444203417003,'1',1444203417003),
		('4','SUV','1',1444203417003,'1',1444203417003);
		
--8---------------------------------------------------------------------------------------		

INSERT INTO car_fare 
		(
			car_fare_id, car_type_id, initial_fare, per_km_fare, 
			per_minute_fare, booking_fees, minimum_fare, discount, free_distance,
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('1','1',5,6,7,0,10,0,3,'1',1444203417001,'1',1444203417001),
		('2','2',10,2,10,0,11,0,3,'1',1444203417002,'1',1444203417002),
		('3','3',15,2,15,0,15,0,3,'1',1444203417003,'1',1444203417003),
		('4','4',20,2,20,0,20,0,3,'1',1444203417004,'1',1444203417004);

--9---------------------------------------------------------------------------------------		
	
INSERT INTO cancellation_charges 
		(
			cancellation_charges_id, admin_id, charge,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 
		('1','1',0,1444203417000,'1',1444203417000,'1');
		
--10---------------------------------------------------------------------------------------
		
			
INSERT INTO driver_payable_percentage 
		(
			driver_payable_percentage_id, user_id, percentage,
			created_at, created_by, updated_at, updated_by,own_passenger_percentage,
			preferred_customer_percentage,referred_driver_percentage
		) 
VALUES 
		('1','1',80,1443699245484,'1',1443699245484,'1',100,5,1);	
		
--11--------------------------------------------------------------------------------------
		
INSERT INTO payment_type 
		(
			payment_type_id, payment_type, record_status,
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('3','CCAVENUE','A','1',1444203417001,'1',1444203417001);
		
--12--------------------------------------------------------------------------------------
		
INSERT INTO admin_sms_sending
		(
	        admin_sms_sending_id, p_accept_by_driver, p_arrived_and_waiting, 
	        p_cancelled_by_driver, p_cancelled_by_business_o, p_invoice, 
	        p_book_by_owner, p_refund, d_booking_request, d_cancelled_by_passenger_business_o, 
	        bo_accepted, bo_arrived_and_waiting, bo_cancelled_by_driver, bo_invoice, language, p_credit_update_admin,
	        record_status, created_by, created_at, updated_by, updated_at
        )
VALUES 
		(
			'1', FALSE, FALSE,
			FALSE, FALSE, FALSE,
			FALSE, FALSE, FALSE, FALSE,
			FALSE, FALSE, FALSE, FALSE, '2', FALSE,
			'A','1',1444203417001,'1',1444203417001
		);
		
--13--------------------------------------------------------------------------------------
		
INSERT INTO admin_settings
		(
            admin_settings_id, radius, sender_benefit, receiver_benefit, 
            about_us, privacy_policy, terms_conditions,
            record_status, created_at, created_by, updated_at, updated_by,
        	distance_type, distance_units, currency_symbol,
        	currency_symbol_html, no_of_cars, country_code, currency_id
        )
VALUES 
		(
			'1', 25, 10, 5, null, null, null,
			'A','1',1444203417001,'1',1444203417001,
			'km', 1000, '&#8377;',
			'&#8377;', 5, '+91', 42
		);

--14--------------------------------------------------------------------------------------

INSERT INTO versions
		(	
	        version_id, version, release_note, app_link, device_type, is_madatory, 
	        remove_support_from, release_date, record_status, created_by, 
	        created_datetime, updated_by, updated_datetime
	    )
VALUES 
		('1','1.0','First Version','http://www.google.com','iphone',TRUE,3321773376000,1491568763000,'A','1',1491568763000,'1',1491568763000),
		('2','1.0','First Version','http://www.google.com','android',TRUE,3321773376000,1491568763000,'A','1',1491568763000,'1',1491568763000);
		
--15--------------------------------------------------------------------------------------

INSERT INTO admin_company_contact
		(
            admin_company_contact_id, user_id, address, phone_number_one, 
            phone_number_two, latitude, longitude, geolocation, 
            phone_number_one_code, phone_number_two_code, fax, email,
            record_status, created_by, created_at, updated_by, updated_at
        )
VALUES 
		(
			'1','1','Mobisoft Infotech Pune Maharastrta','1234567890','1234561234',
			'18.590478','73.727191','0101000020E610000084BC1E4C8A6E5240EB01F39029973240',
			'+91', '+91', '+91341234567', 'support@indiacabs.com',
			'A','1',1460720562370,'1',1460720562370
		);
		
--16--------------------------------------------------------------------------------------

INSERT INTO free_waiting_time
		(
            free_waiting_time_id, user_id, waiting_time, cancel_time, 
            created_at, created_by, updated_at, updated_by
        )
VALUES 
		(
			'1', '1', 2, 2,
			1463142563884,'1',1463231757798,'1'
        );
        
--17--------------------------------------------------------------------------------------

DELETE FROM url_groups WHERE url_group_id IN (8);
DELETE FROM urls WHERE url_id IN (8);
DELETE FROM url_accesses WHERE url_id IN (8);

--18--------------------------------------------------------------------------------------

INSERT INTO driver_commission_types 
		(
			driver_commission_type_id, driver_commission_type, created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('1','Driver Payable','1',1469183382000,'1',1469183382000),
		('2','Own Customer','1',1469183382000,'1',1469183382000),
		('3','Preferred Customer','1',1469183382000,'1',1469183382000),
		('4','Referred Driver','1',1469183382000,'1',1469183382000);
		
--19--------------------------------------------------------------------------------------

INSERT INTO ride_later_settings
		(
            ride_later_settings_id, min_booking_time, max_booking_time, driver_job_request_time, 
            driver_allocate_before_time, driver_allocate_after_time, 
            passenger_tour_before_time, passenger_tour_after_time, 
            record_status, created_at, created_by, updated_at, updated_by
        )
VALUES 
		(
			'1', 10, 100, 10, 10, 10, 
			10, 10, 'A', 1469523685000, '1', 1469523685000, '1'
        );
        
--20--------------------------------------------------------------------------------------

INSERT INTO emergency_numbers 
		(
			emergency_number_id, name, phone_no, no_type, 
			record_status, created_at, created_by, updated_at, updated_by
		) 
VALUES 
		('1', 'Police Help Line', '100', 'police', 'A', 1444203417000, '1', 1444203417000, '1');
		
--21--------------------------------------------------------------------------------------

INSERT INTO recharge_amount 
		(
			recharge_amount_id, amount, record_status, created_at, updated_at, created_by, updated_by  
		) 
VALUES 
		('1', 1000, 'A', 1444203417001, '1', 1444203417001, '1'),
		('2', 2000, 'A', 1444203417002, '1', 1444203417002, '2'),
		('3', 5000, 'A', 1444203417003, '1', 1444203417003, '3'),
		('4', 10000, 'A', 1444203417004, '1', 1444203417004, '4');
		
--22--------------------------------------------------------------------------------------
		
INSERT INTO driver_app_versions
		(	
	        driver_app_version_id, version, release_note, app_link, device_type, is_madatory, 
	        remove_support_from, release_date, record_status, created_by, 
	        created_datetime, updated_by, updated_datetime
	    )
VALUES 
		('1','1.0','First Version','http://www.google.com','iphone',TRUE,3321773376000,1491568763000,'A','1',1491568763000,'1',1491568763000),
		('2','1.0','First Version','http://www.google.com','android',TRUE,3321773376000,1491568763000,'A','1',1491568763000,'1',1491568763000);
		
--23--------------------------------------------------------------------------------------

INSERT INTO multicity_country
		(
            multicity_country_id, country_name, country_short_name, currency_symbol, currency_symbol_html, 
            sender_benefit, receiver_benefit, phone_no_code, distance_type, distance_units, cancellation_charges, 
            is_active, is_deleted, is_permanent_delete, record_status, created_by, created_at, updated_by, updated_at
        )
VALUES 	(
			'1', 'India', 'IN', '₹', '&#8377;', 0, 0, '+91', 'km', 1000, 0, TRUE, FALSE, FALSE, 'A', '1', 1486719750000, '1', 1486719750000
		);
