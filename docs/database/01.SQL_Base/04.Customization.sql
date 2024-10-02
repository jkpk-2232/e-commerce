
--Command for to update old referral code as phone number
--UPDATE user_info  set referral_code = phone_no

ALTER TABLE  admin_settings  ADD driver_referral_benefit double precision DEFAULT 0;

---------------------------------------------------------------------------------------------
ALTER TABLE  car_type  ADD is_active boolean DEFAULT true;

ALTER TABLE  car_type  ADD car_priority smallint;

INSERT INTO car_type 
		(
			car_type_id, car_type, 
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('5','Driver','1',1498110554000,'1',1498110554000);

INSERT INTO car_fare 
		(
			car_fare_id, car_type_id, initial_fare, per_km_fare, 
			per_minute_fare, booking_fees, minimum_fare, discount, free_distance,
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('5','5',20,2,20,0,20,0,3,'1',1498110554000,'1',1498110554000);
		
UPDATE car_type  SET is_active = false WHERE car_type_id = '1';
		
----------------------------------------------------------------------------------------------
		
INSERT INTO url_accesses 
		(
			user_id, url_id, record_status, created_at, 
			created_by, updated_at, updated_by
		) 
VALUES 
		('1',17,'A',1444203417000,'1',1444203417000,'1'),
		('1',18,'A',1444203417000,'1',1444203417000,'1');
		
---------------------------------------------------------------------------------------------
		
CREATE TABLE rental_packages
(
  rental_package_id character varying(50) NOT NULL,
  multicity_city_region_id character varying(50) NOT NULL,
  package_time bigint,
  package_distance double precision,
  is_active boolean DEFAULT true,
  record_status character(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT rental_package_id_pk PRIMARY KEY (rental_package_id)
);

CREATE TABLE rental_package_fare
(
  rental_package_fare_id character varying(50) NOT NULL,
  rental_package_id character varying(50),
  car_type_id character varying(50),
  base_fare double precision,
  per_km_fare double precision,
  per_minute_fare double precision,
  record_status character(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT rental_package_fare_id_pk PRIMARY KEY (rental_package_fare_id)
);

ALTER TABLE  tours  ADD is_rental_booking boolean DEFAULT false;

ALTER TABLE  tours  ADD rental_package_id character varying(50);

ALTER TABLE  tours  ADD rental_package_time bigint;

ALTER TABLE  invoices  ADD is_rental_booking boolean DEFAULT false;

ALTER TABLE  invoices  ADD rental_package_id character varying(50);

ALTER TABLE  invoices  ADD rental_package_time bigint;

----------------------------------------------------------------------------------------------

ALTER TABLE  rental_packages  ADD rental_package_type character varying(30) DEFAULT '1'; -- Value 0: Intercity and 1: Outstation

-- Driver referral cr

CREATE TABLE driver_referral_code_logs
(
  driver_referral_code_log_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  passenger_id character varying(50) NOT NULL,
  driver_percentage double precision,
  record_status character(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT driver_referral_code_log_id_pk PRIMARY KEY (driver_referral_code_log_id)
);

CREATE TABLE tour_referrer_benefits
(
  tour_referrer_benefit_id character varying(50) NOT NULL,
  driver_referral_code_log_id character varying(50),
  tour_id character varying(50),
  referrer_driver_percentage double precision,
  referrer_driver_benefit double precision,
  tour_referrer_type character varying(50), --(OWN/OTHER)
  record_status character(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT tour_referrer_benefit_id_pk PRIMARY KEY (tour_referrer_benefit_id)
);

----------------------------------------------------------------------------------------------

ALTER TABLE  admin_settings  ADD average_speed double precision DEFAULT 80;

------------------------------------------------------------------------------------------------

CREATE TABLE favourite_locations
(
  favourite_locations_id character varying(50) NOT NULL,
  user_id character varying(50),
  favourite_nickname character varying(50),
  favourite_latitude character varying(50),
  favourite_longitude character varying(50),
  favourite_address character varying(2000),
  favourite_place_id character varying(100),
  favourite_geolocation geography(Point,4326),
  is_source boolean DEFAULT FALSE,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT favourite_locations_id_pk PRIMARY KEY (favourite_locations_id)
);

ALTER TABLE tours ADD pickup_favourite_locations_id character varying(50);
ALTER TABLE tours ADD destination_favourite_locations_id character varying(50);
------------------------------------------------------------------------------------------

ALTER TABLE  rental_package_fare  ADD driver_payable_percentage double precision;

-------------------------------------------------------------------------------------------

CREATE TABLE export_accesses
(
  export_access_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  is_booking_export boolean DEFAULT TRUE,
  is_passenger_export boolean DEFAULT TRUE,
  is_ride_later_export boolean DEFAULT TRUE,
  is_critical_ride_later_export boolean DEFAULT TRUE,
  is_driver_income_report_export boolean DEFAULT TRUE,
  is_refund_report_export boolean DEFAULT TRUE,
  is_driver_duty_report_export boolean DEFAULT TRUE,
  is_ccavenue_log_report_export boolean DEFAULT TRUE,
  is_driver_drive_report_export boolean DEFAULT TRUE,
  is_driver_benefit_report_export boolean DEFAULT TRUE,
  is_active boolean DEFAULT TRUE,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT export_access_id_pk PRIMARY KEY (export_access_id)
);

--------------------------------------------------------------------------------------

ALTER TABLE  invoices  ADD updated_amount_collected double precision;

ALTER TABLE  invoices  ADD remark character varying(500);

ALTER TABLE  invoices  ADD remark_by character varying(50);

----------------------------------Settings Access--------------------------------------

INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(20, 'Settings', 'A', 20,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(20, 20, 'Settings', '/settings.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon-calculator');
		
INSERT INTO url_accesses 
		(
			user_id, url_id, record_status, created_at, 
			created_by, updated_at, updated_by
		) 
VALUES 		
		('1', 20, 'A', 1504588886000, '1', 1504588886000, '1');
		
---------------------------------------------------------------------------------------
		
ALTER TABLE  admin_settings  ADD driver_ideal_time bigint DEFAULT 28800000;

---------------------------------------------------------------------------------------

ALTER TABLE  admin_settings  ADD is_auto_assign boolean DEFAULT false;

--------------------------------------------------------------------------------------

ALTER TABLE  car_fare  ADD fare_after_specific_km double precision DEFAULT 0;
ALTER TABLE  car_fare  ADD km_to_increase_fare double precision DEFAULT 0;

ALTER TABLE  tours  ADD fare_after_specific_km double precision DEFAULT 0;
ALTER TABLE  tours  ADD km_to_increase_fare double precision DEFAULT 0;
  
ALTER TABLE  invoices  ADD fare_after_specific_km double precision DEFAULT 0;
ALTER TABLE  invoices  ADD km_to_increase_fare double precision DEFAULT 0;
ALTER TABLE  invoices  ADD distance_before_specific_km double precision DEFAULT 0;
ALTER TABLE  invoices  ADD distance_after_specific_km double precision DEFAULT 0;
ALTER TABLE  invoices  ADD distance_fare_before_specific_km double precision DEFAULT 0;
ALTER TABLE  invoices  ADD distance_fare_after_specific_km double precision DEFAULT 0;

---------------------------------------------------------------------------------------

CREATE TABLE driver_logged_in_times
(
  driver_logged_in_time_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  logged_in_time bigint,
  date_time bigint,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_logged_in_time_id_pk PRIMARY KEY (driver_logged_in_time_id)
);

-------------------------------------------------------------------------------------------

ALTER TABLE  promo_code  ADD is_active boolean DEFAULT TRUE;

--------------------------------------------------------------------------------------------

ALTER TABLE  admin_settings  ADD is_car_Service_auto_assign boolean DEFAULT false;

--------------------------------------------------------------------------------------------

--------------------------------Vendor Managment--------------------------------

ALTER TABLE user_info ADD drive_transmission_type_id character varying(5);

UPDATE user_info AS UI SET drive_transmission_type_id='3' FROM car_drivers AS CD WHERE CD.driver_id = UI.user_id  AND CD.car_id = '-1'; 

ALTER TABLE tours ADD transmission_type_id character varying(5);

--------------------------------------------------------------------------------------------

--------------------------------Vendor Managment--------------------------------

ALTER TABLE users ADD COLUMN approvel_status boolean;

ALTER TABLE cars ADD COLUMN approvel_status boolean;

CREATE TABLE driver_vendors
(
  driver_vendors_id character varying(50) NOT NULL,
  driver_id character varying(50),
  vendor_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_vendors_id_pk PRIMARY KEY (driver_vendors_id)
);

CREATE TABLE car_vendors
(
 car_vendors_id character varying(50) NOT NULL,
  car_id character varying(50),
  vendor_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_vendors_id_pk PRIMARY KEY (car_vendors_id)
);

INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(22, 'Vendor Bookings', 'A', 4,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(22, 22, 'Vendor Bookings', '/vendor-bookings.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon glyphicon-home');
		
INSERT INTO url_accesses 
		(
			url_access_id, user_id, url_id, record_status, 
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(22,'1',22,'A',1504588886000,'1',1504588886000,'1');

INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(23, 'Vendor Drivers', 'A', 7,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(23, 23, 'Vendor Drivers', '/vendor-drivers.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon glyphicon-road');

INSERT INTO url_accesses 
		(
			url_access_id, user_id, url_id, record_status, 
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(23,'1',23,'A',1504588886000,'1',1504588886000,'1');
		
INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(24, 'Vendor Cars', 'A', 10,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(24, 24, 'Vendor Cars', '/vendor-cars.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon glyphicon-cars');
		
INSERT INTO url_accesses 
		(
			url_access_id, user_id, url_id, record_status, 
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(24,'1',24,'A',1504588886000,'1',1504588886000,'1');	
		
INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(25, 'Vendor Ride Later', 'A', 8,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(25, 25, 'Vendor Ride Later', '/manage-vendor-ride-later.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon glyphicon-home');
		
INSERT INTO url_accesses 
		(
			url_access_id, user_id, url_id, record_status, 
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(25,'1',25,'A',1504588886000,'1',1504588886000,'1');
		

INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(26, 'Vendor Critical Ride Later', 'A', 9,1504588886000, '1', 1504588886000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(26, 26, 'Vendor Critical Ride Later', '/manage-vendor-critical-ride-later.do', -1, 'A', 1504588886000, '1', 1504588886000, '1', 'glyphicon glyphicon-home');
		
INSERT INTO url_accesses 
		(
			url_access_id, user_id, url_id, record_status, 
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(26,'1',26,'A',1504588886000,'1',1504588886000,'1');

-----------------------------------Airport Region-----------------------------------------------

CREATE TABLE temp_airport_regions
(
  temp_airport_region_id character varying(50) NOT NULL,
  area_polygon geometry(Polygon),
  region_latitude character varying(250),
  region_longitude character varying(250),
  CONSTRAINT temp_airport_region_id_pk PRIMARY KEY (temp_airport_region_id)
);

CREATE TABLE airport_regions
(
  airport_region_id character varying(50) NOT NULL,
  alias_name character varying(100),
  address character varying(500),
  area_polygon geometry(Polygon),
  multicity_city_region_id character varying(50),
  airport_pickup_fixed_fare_mini double precision,
  airport_pickup_fixed_fare_sedan double precision,
  airport_pickup_fixed_fare_suv double precision,
  airport_drop_fixed_fare_mini double precision,
  airport_drop_fixed_fare_sedan double precision,
  airport_drop_fixed_fare_suv double precision,
  airport_distance double precision,
  is_active boolean DEFAULT true,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  region_latitude character varying(250),
  region_longitude character varying(250),
  CONSTRAINT airport_region_id_pk PRIMARY KEY (airport_region_id)
);
		

ALTER TABLE tours ADD is_airport_fixed_fare_applied boolean DEFAULT false;
ALTER TABLE tours ADD airport_booking_type character varying(50);

ALTER TABLE user_info ADD driver_payable_percentage double precision;
UPDATE user_info SET driver_payable_percentage = 85.0;

------------------------------------ City Surge ----------------------------------------

CREATE TABLE city_surge
(
  city_surge_id character varying(50) NOT NULL,
  multicity_city_region_id character varying(50),
  city_name character varying(50),
  radius double precision,
  surge_rate double precision,
  is_active boolean DEFAULT true,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT city_surge_id_pk PRIMARY KEY (city_surge_id)
);

ALTER TABLE tours ADD surge_type character varying(50);
ALTER TABLE tours ADD surge_radius double precision;
ALTER TABLE car_fare ADD airport_region_id character varying(50);

ALTER TABLE car_fare ADD airport_booking_type character varying(25);

-------------------------------- End Of City Surge ------------------------------------

------------------------------ Markup Fare---------------------------------------------

ALTER TABLE  user_info ADD maximum_markup_fare double precision DEFAULT 0;
ALTER TABLE  tours ADD markup_fare double precision DEFAULT 0;
ALTER TABLE  invoices ADD markup_fare double precision DEFAULT 0;

------------------------- Verify Otp Logs----------------------------------------------

CREATE TABLE user_login_otp
(
  user_login_otp_id character varying(50) NOT NULL,
  verification_code character varying(50),
  user_id character varying(50),
  is_verified boolean DEFAULT false,
  role_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_login_otp_id_pk PRIMARY KEY (user_login_otp_id)
);

ALTER TABLE  tours  ADD tour_booked_by character varying(50) DEFAULT 0;

------------------------------ Driver wallet ----------------------------------------------------------------

CREATE TABLE user_account
(
  user_account_id character varying(50) NOT NULL,
  user_id character varying(50),
  current_balance double precision DEFAULT 0,
  hold_balance double precision DEFAULT 0,
  approved_balance double precision DEFAULT 0,
  total_balance double precision DEFAULT 0,
  is_active boolean DEFAULT true,
  record_status character varying(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT user_account_id_pk PRIMARY KEY (user_account_id)
);

CREATE TABLE user_account_logs
(
  user_account_log_id character varying(50) NOT NULL,
  user_account_id character varying(50),
  user_id character varying(50),
  Description character varying(100),
  remark character varying(100), 
  credited_amount double precision DEFAULT 0,
  debited_amount double precision DEFAULT 0,
  transaction_type character varying(50),   --Its Credit/Debit
  transaction_status character varying(30),
  current_balance double precision DEFAULT 0,
  hold_balance double precision DEFAULT 0,
  approved_balance double precision DEFAULT 0,
  total_balance double precision DEFAULT 0,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT user_account_log_id_pk PRIMARY KEY (user_account_log_id)
);

CREATE TABLE encash_requests
(
  encash_request_id character varying(50) NOT NULL,
  user_id character varying(50),
  requested_amount double precision DEFAULT 0,
  requested_date bigint,
  status character varying(25),
  hold_date bigint,
  approved_date bigint,
  rejected_date bigint,
  transfer_date bigint,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT encash_request_id_pk PRIMARY KEY (encash_request_id)
);

CREATE TABLE driver_wallet_settings
(
  driver_wallet_setting_id character varying(50) NOT NULL,
  minimum_amount double precision DEFAULT 0,
  notify_amount double precision DEFAULT 0,
  is_active boolean DEFAULT true,
  record_status character varying(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT driver_wallet_setting_id_pk PRIMARY KEY (driver_wallet_setting_id)
);

ALTER TABLE user_account_logs ADD transaction_by character varying(50);

ALTER TABLE user_account_logs ADD is_account_recharge boolean DEFAULT false;

INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(27, 'Driver Accounts', 'A', 22, 1523882123000, '1', 1523882123000, '1'),
		(28, 'Vendor Accounts', 'A', 23, 1523882123000, '1', 1523882123000, '1'),
		(29, 'Hold Encash Requests', 'A', 24, 1523882123000, '1', 1523882123000, '1'),
		(30, 'Approved Encash Requests', 'A', 25, 1523882123000, '1', 1523882123000, '1'),
		(31, 'Transferred Encash Requests', 'A', 26, 1523882123000, '1', 1523882123000, '1'),
		(32, 'Rejected Encash Requests', 'A', 27, 1523882123000, '1', 1523882123000, '1'),
		(33, 'Vendor Driver Accounts', 'A', 28, 1523882123000, '1', 1523882123000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(27, 27, 'Driver Accounts', '/manage-drivers/account.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(28, 28, 'Vendor Accounts', '/manage-vendor/account.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(29, 29, 'Hold Encash Requests', '/encash-requests/hold.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(30, 30, 'Approved Encash Requests', '/encash-requests/approved.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(31, 31, 'Transferred Encash Requests', '/encash-requests/transferred.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(32, 32, 'Rejected Encash Requests', '/encash-requests/rejected.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase'),
		(33, 33, 'Vendor Driver Accounts', '/manage-vendor-drivers/account.do', -1, 'A', 1523882123000, '1', 1523882123000, '1', 'glyphicon-briefcase');
		
INSERT INTO url_accesses 
		(
			user_id, url_id, record_status, created_at, 
			created_by, updated_at, updated_by
		) 
VALUES 		
		('1', 27, 'A', 1523882123000, '1', 1523882123000, '1'),
		('1', 28, 'A', 1523882123000, '1', 1523882123000, '1'),
		('1', 29, 'A', 1523882123000, '1', 1523882123000, '1'),
		('1', 30, 'A', 1523882123000, '1', 1523882123000, '1'),
		('1', 31, 'A', 1523882123000, '1', 1523882123000, '1'),
		('1', 32, 'A', 1523882123000, '1', 1523882123000, '1');
		
ALTER TABLE export_accesses ADD is_driver_account_export boolean DEFAULT true;

ALTER TABLE export_accesses ADD is_vendor_account_export boolean DEFAULT true;

ALTER TABLE export_accesses ADD is_vendor_driver_account_export boolean DEFAULT true;

CREATE TABLE user_account_log_details
(
  user_account_log_details_id character varying(50) NOT NULL,
  user_account_log_id character varying(50) NOT NULL,
  user_account_id character varying(50),
  user_id character varying(50),
  Description character varying(100),
  remark character varying(100), 
  credited_amount double precision DEFAULT 0,
  debited_amount double precision DEFAULT 0,
  transaction_type character varying(50),   --Its Credit/Debit
  transaction_status character varying(30),
  current_balance double precision DEFAULT 0,
  hold_balance double precision DEFAULT 0,
  approved_balance double precision DEFAULT 0,
  total_balance double precision DEFAULT 0,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT user_account_log_details_id_pk PRIMARY KEY (user_account_log_details_id)
);

ALTER TABLE user_account_logs ADD encash_request_id character varying(50);

ALTER TABLE user_account_log_details ADD transaction_by character varying(50);

ALTER TABLE user_account_log_details ADD is_account_recharge boolean DEFAULT false;

INSERT INTO 
	driver_wallet_settings 
		(
			driver_wallet_setting_id, minimum_amount, notify_amount, is_active, 
			record_status, created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		('1', 0,  50, true, 'A', 1524209248000, '1', 1524209248000, '1');
		
INSERT INTO url_groups 
		(
			url_group_id, url_group_name, record_status, menu_priority,
			created_at, created_by, updated_at, updated_by
		) 
VALUES 		
		(34, 'Vendor My Account', 'A', 29, 1524720383000, '1', 1524720383000, '1');

INSERT INTO urls 
		(
			url_id, url_group_id, url_title, url, parent_url_id,
			record_status, created_at, created_by, updated_at, updated_by, url_icon
		) 
VALUES 		
		(34, 34, 'Vendor My Account', '/vendor/my-account.do', -1, 'A', 1524720383000, '1', 1524720383000, '1', 'glyphicon-briefcase');

------------------------------------------------------------------------------------------------------------

ALTER TABLE invoices ADD is_pass_released_by_admin boolean DEFAULT false;

ALTER TABLE invoices ADD passenger_released_at bigint;

ALTER TABLE invoices ADD passenger_released_by character varying(50);
