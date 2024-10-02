--New service type courier
INSERT INTO service_types
(service_type_id, service_type_name, service_type_description, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES 
('3', 'Courier', 'Courier', true, false, '1', 1677755438002, '1', 1677755438002);

UPDATE urls SET url_icon='glyphicon glyphicon-shop' WHERE url_id='42';

--Assign super service type id

ALTER TABLE car_fare ADD service_type_id character varying(5) DEFAULT '1';
ALTER TABLE vendor_car_fare ADD service_type_id character varying(5) DEFAULT '1';
ALTER TABLE vendor_car_type ADD service_type_id character varying(5) DEFAULT '1';
ALTER TABLE tours ADD service_type_id character varying(5) DEFAULT '1';
ALTER TABLE orders ADD service_type_id character varying(5) DEFAULT '2';

UPDATE car_fare SET service_type_id = '1';
UPDATE vendor_car_fare SET service_type_id = '1';
UPDATE vendor_car_type SET service_type_id = '1';
UPDATE tours SET service_type_id = '1';
UPDATE orders SET service_type_id = '2';

CREATE INDEX idx_car_fare_service_type_id ON car_fare(service_type_id);
CREATE INDEX idx_vendor_car_fare_service_type_id ON vendor_car_fare(service_type_id);
CREATE INDEX idx_vendor_car_type_service_type_id ON vendor_car_type(service_type_id);
CREATE INDEX idx_tours_service_type_id ON tours(service_type_id);
CREATE INDEX idx_orders_service_type_id ON orders(service_type_id);

--Courier fields

ALTER TABLE tours ADD courier_pickup_address text;
ALTER TABLE tours ADD courier_contact_person_name character varying(50);
ALTER TABLE tours ADD courier_contact_phone_no character varying(15);
ALTER TABLE tours ADD courier_drop_address text;
ALTER TABLE tours ADD courier_drop_contact_person_name character varying(50);
ALTER TABLE tours ADD courier_drop_contact_phone_no character varying(15);
ALTER TABLE tours ADD courier_details character varying(100);
ALTER TABLE tours ADD courier_order_received_against_vendor_id character varying(32);

CREATE INDEX idx_tours_courier_pickup_address ON tours(courier_pickup_address);
CREATE INDEX idx_tours_courier_contact_person_name ON tours(courier_contact_person_name);
CREATE INDEX idx_tours_courier_contact_phone_no ON tours(courier_contact_phone_no);
CREATE INDEX idx_tours_courier_drop_address ON tours(courier_drop_address);
CREATE INDEX idx_tours_courier_drop_contact_person_name ON tours(courier_drop_contact_person_name);
CREATE INDEX idx_tours_courier_drop_contact_phone_no ON tours(courier_drop_contact_phone_no);
CREATE INDEX idx_tours_courier_details ON tours(courier_details);
CREATE INDEX idx_tours_courier_order_received_against_vendor_id ON tours(courier_order_received_against_vendor_id);

--Vendor subscription fields

ALTER TABLE user_info ADD vendor_monthly_subscription_fee double precision DEFAULT 0;
ALTER TABLE user_info ADD is_vendor_subscription_free_active boolean DEFAULT false;
ALTER TABLE user_info ADD is_vendor_subscription_current_active boolean DEFAULT false;
ALTER TABLE user_info ADD vendor_free_subscription_start_date_time bigint DEFAULT 0;
ALTER TABLE user_info ADD vendor_free_subscription_end_date_time bigint DEFAULT 0;
ALTER TABLE user_info ADD vendor_current_subscription_start_date_time bigint DEFAULT 0;
ALTER TABLE user_info ADD vendor_current_subscription_end_date_time bigint DEFAULT 0;
ALTER TABLE user_info ADD vendor_monthly_subscription_history_id character varying(32);
ALTER TABLE user_info ADD is_vendor_subscription_marked_expired_by_cronjob boolean DEFAULT false;
ALTER TABLE user_info ADD vendor_subscription_marked_expired_by_cronjob_timing bigint DEFAULT 0;

CREATE INDEX idx_user_info_vendor_monthly_subscription_fee ON user_info(vendor_monthly_subscription_fee);
CREATE INDEX idx_user_info_is_vendor_subscription_free_active ON user_info(is_vendor_subscription_free_active);
CREATE INDEX idx_user_info_is_vendor_subscription_current_active ON user_info(is_vendor_subscription_current_active);
CREATE INDEX idx_user_info_vendor_free_subscription_start_date_time ON user_info(vendor_free_subscription_start_date_time);
CREATE INDEX idx_user_info_vendor_free_subscription_end_date_time ON user_info(vendor_free_subscription_end_date_time);
CREATE INDEX idx_user_info_vendor_current_subscription_start_date_time ON user_info(vendor_current_subscription_start_date_time);
CREATE INDEX idx_user_info_vendor_current_subscription_end_date_time ON user_info(vendor_current_subscription_end_date_time);
CREATE INDEX idx_user_info_vendor_monthly_subscription_history_id ON user_info(vendor_monthly_subscription_history_id);
CREATE INDEX idx_user_info_vendor_subscription_marked_expired_by_cronjob_timing ON user_info(vendor_subscription_marked_expired_by_cronjob_timing);

CREATE TABLE vendor_monthly_subscription_history
(
  vendor_monthly_subscription_history_id character varying(32) NOT NULL,
  vendor_monthly_subscription_history_serial_id serial NOT NULL,
  vendor_id character varying(32),
  vendor_monthly_subscription_fee double precision DEFAULT 0,
  payment_type character varying(50), 
  transaction_id character varying(100), 
  start_date_time bigint,
  end_date_time bigint,
  is_free_subscription_entry boolean DEFAULT false,
  is_vendor_subscription_current_active boolean DEFAULT false,
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT vendor_monthly_subscription_history_id_pk PRIMARY KEY (vendor_monthly_subscription_history_id)
);

CREATE INDEX idx_vendor_monthly_subscription_history_vendor_id ON vendor_monthly_subscription_history(vendor_id);
CREATE INDEX idx_vendor_monthly_subscription_history_vendor_serial_id ON vendor_monthly_subscription_history(vendor_monthly_subscription_history_serial_id);
CREATE INDEX idx_vendor_monthly_subscription_history_fee ON vendor_monthly_subscription_history(vendor_monthly_subscription_fee);
CREATE INDEX idx_vendor_monthly_subscription_history_payment_type ON vendor_monthly_subscription_history(payment_type);
CREATE INDEX idx_vendor_monthly_subscription_history_transaction_id ON vendor_monthly_subscription_history(transaction_id);
CREATE INDEX idx_vendor_monthly_subscription_history_start_date_time ON vendor_monthly_subscription_history(start_date_time);
CREATE INDEX idx_vendor_monthly_subscription_history_end_date_time ON vendor_monthly_subscription_history(end_date_time);
CREATE INDEX idx_vendor_monthly_subscription_history_created_by ON vendor_monthly_subscription_history(created_by);
CREATE INDEX idx_vendor_monthly_subscription_history_created_at ON vendor_monthly_subscription_history(created_at);
CREATE INDEX idx_vendor_monthly_subscription_history_updated_by ON vendor_monthly_subscription_history(updated_by);
CREATE INDEX idx_vendor_monthly_subscription_history_updated_at ON vendor_monthly_subscription_history(updated_at);
CREATE INDEX idx_vendor_monthly_subscription_history_current_active ON vendor_monthly_subscription_history(is_vendor_subscription_current_active);

--New menu tab for Vendor Monthly Subscription History
INSERT INTO url_groups 
  (url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
  (43,'Monthly Subscription History','A',43,1689277870000,'1',1689277870000,'1');
    
INSERT INTO urls 
  (url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
  (43,43,'Monthly Subscription History','/manage-vendor-monthly-subscription-history.do',-1,'A',1689277870000,'1',1689277870000,'1','icon icon-exchange');

--Settings for admin to enable disable the driver restriction for vendor monthly subscription expiry
ALTER TABLE admin_settings ADD is_restrict_driver_vendor_subscription_expiry boolean DEFAULT FALSE;

--Vendor app versions

CREATE TABLE vendor_app_versions
(
  vendor_app_version_id character varying(32) NOT NULL,
  version character varying(10),
  release_note character varying(3000),
  app_link character varying(1000),
  device_type character varying(50),
  is_madatory boolean,
  remove_support_from bigint,
  release_date bigint,
  record_status character varying(1),
  created_by character varying(32),
  created_datetime bigint,
  updated_by character varying(32),
  updated_datetime bigint,
  CONSTRAINT vendor_app_version_id_pk PRIMARY KEY (vendor_app_version_id)
);

CREATE INDEX idx_vendor_app_versions_version ON vendor_app_versions(version);
CREATE INDEX idx_vendor_app_versions_device_type ON vendor_app_versions(device_type);
CREATE INDEX idx_vendor_app_versions_is_madatory ON vendor_app_versions(is_madatory);
CREATE INDEX idx_vendor_app_versions_remove_support_from ON vendor_app_versions(remove_support_from);
CREATE INDEX idx_vendor_app_versions_release_date ON vendor_app_versions(release_date);
CREATE INDEX idx_vendor_app_versions_created_datetime ON vendor_app_versions(created_datetime);
CREATE INDEX idx_vendor_app_versions_updated_datetime ON vendor_app_versions(updated_datetime);

INSERT INTO vendor_app_versions
( 
  vendor_app_version_id, version, release_note, app_link, device_type, is_madatory, 
  remove_support_from, release_date, record_status, created_by, 
  created_datetime, updated_by, updated_datetime
)
VALUES 
('1','1.0','First Version','http://www.google.com','iphone',TRUE,6672531200000,1672531200000,'A','1',1672531200000,'1',1672531200000),
('2','1.0','First Version','http://www.google.com','android',TRUE,6672531200000,1672531200000,'A','1',1672531200000,'1',1672531200000);

--Unified history

CREATE TABLE unified_history 
(
  unified_history_id character varying(32) NOT NULL,
  history_id character varying(32),
  short_id character varying(32),
  passenger_id character varying(32),
  passenger_vendor_id character varying(32),
  car_type_id character varying(32),
  service_type_id character varying(5),
  multicity_country_id character varying(32), 
  multicity_city_region_id character varying(32),

  source_address text, 
  destination_address text,
  p_first_name character varying(50),
  p_last_name character varying(50),
  p_email character varying(50),
  p_phone character varying(20),
  p_phone_code character varying(5),
  p_photo_url character varying(200),

  tour_booked_by character varying(32),
  booking_type character varying(32),
  ride_later_pickup_time bigint,
  is_ride_later boolean DEFAULT FALSE,

  order_number_of_items int DEFAULT 0,
  order_delivery_address text,
  order_delivery_charges double precision DEFAULT 0,
  order_creation_time bigint, 
  vendor_store_id character varying(32),
  order_received_against_vendor_id character varying(32),

  courier_pickup_address text, 
  courier_contact_person_name character varying(50), 
  courier_contact_phone_no character varying(15), 
  courier_drop_address text, 
  courier_drop_contact_person_name character varying(50), 
  courier_drop_contact_phone_no character varying(15), 
  courier_details character varying(100), 

  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,

  driver_id character varying(32), 
  driver_vendor_id character varying(32),
  car_id character varying(32),
  is_delievery_managed_by_vendor_driver boolean DEFAULT FALSE,
  total double precision DEFAULT 0,
  charges double precision DEFAULT 0,
  status character varying(50),
  promo_code_id character varying(50),
  is_promo_code_applied boolean DEFAULT FALSE,
  is_tour_ride_later boolean DEFAULT FALSE,
  is_critical_tour_ride_later boolean DEFAULT FALSE,
  is_take_ride boolean DEFAULT FALSE,
  is_tour_take_ride boolean DEFAULT FALSE,

  CONSTRAINT unified_history_id_pk PRIMARY KEY (unified_history_id)
);

CREATE INDEX idx_unified_history_history_id ON unified_history(history_id);
CREATE INDEX idx_unified_history_short_id ON unified_history(short_id);
CREATE INDEX idx_unified_history_passenger_id ON unified_history(passenger_id);
CREATE INDEX idx_unified_history_passenger_vendor_id ON unified_history(passenger_vendor_id);
CREATE INDEX idx_unified_history_car_type_id ON unified_history(car_type_id);
CREATE INDEX idx_unified_history_service_type_id ON unified_history(service_type_id);
CREATE INDEX idx_unified_history_multicity_country_id ON unified_history(multicity_country_id);
CREATE INDEX idx_unified_history_multicity_city_region_id ON unified_history(multicity_city_region_id);
CREATE INDEX idx_unified_history_source_address ON unified_history(source_address);
CREATE INDEX idx_unified_history_destination_address ON unified_history(destination_address);

CREATE INDEX idx_unified_history_tour_booked_by ON unified_history(tour_booked_by);
CREATE INDEX idx_unified_history_booking_type ON unified_history(booking_type);
CREATE INDEX idx_unified_history_ride_later_pickup_time ON unified_history(ride_later_pickup_time);

CREATE INDEX idx_unified_history_order_number_of_items ON unified_history(order_number_of_items);
CREATE INDEX idx_unified_history_order_delivery_address ON unified_history(order_delivery_address);
CREATE INDEX idx_unified_history_order_creation_time ON unified_history(order_creation_time);
CREATE INDEX idx_unified_history_vendor_store_id ON unified_history(vendor_store_id);
CREATE INDEX idx_unified_history_order_received_against_vendor_id ON unified_history(order_received_against_vendor_id);

CREATE INDEX idx_unified_history_courier_pickup_address ON unified_history(courier_pickup_address);
CREATE INDEX idx_unified_history_courier_contact_person_name ON unified_history(courier_contact_person_name);
CREATE INDEX idx_unified_history_courier_contact_phone_no ON unified_history(courier_contact_phone_no);
CREATE INDEX idx_unified_history_courier_drop_address ON unified_history(courier_drop_address);
CREATE INDEX idx_unified_history_courier_drop_contact_person_name ON unified_history(courier_drop_contact_person_name);
CREATE INDEX idx_unified_history_courier_drop_contact_phone_no ON unified_history(courier_drop_contact_phone_no);
CREATE INDEX idx_unified_history_courier_details ON unified_history(courier_details);

CREATE INDEX idx_unified_history_created_by ON unified_history(created_by);
CREATE INDEX idx_unified_history_created_at ON unified_history(created_at);
CREATE INDEX idx_unified_history_updated_by ON unified_history(updated_by);
CREATE INDEX idx_unified_history_updated_at ON unified_history(updated_at);

CREATE INDEX idx_unified_history_driver_id ON unified_history(driver_id);
CREATE INDEX idx_unified_history_driver_vendor_id ON unified_history(driver_vendor_id);
CREATE INDEX idx_unified_history_car_id ON unified_history(car_id);
CREATE INDEX idx_unified_history_status ON unified_history(status);
CREATE INDEX idx_unified_history_promo_code_id ON unified_history(promo_code_id);

--> Run the following script for making default monthly subscription entries for the existing vendors
ScriptAssignVendorMonthlySubscriptionFeeEntry.java -> /api/script-assign-vendor-monthly-subscription-fee-entry.json

--> Run the following script for moving data to unified history tables
ScriptMigrateDataToUnifiedHistory.java -> /api/script-migrate-data-to-unified-history.json

--> Cross verify all the cron job paths

--New menu tab for History
--INSERT INTO url_groups 
--	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
--VALUES 
--	(44,'History','A',43,1690987468000,'1',1690987468000,'1');    
--INSERT INTO urls 
--	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
--VALUES 
--	(44,44,'History','/manage-history.do',-1,'A',1690987468000,'1',1690987468000,'1','glyphicon glyphicon-history');
--INSERT INTO url_accesses 
--	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
--VALUES 
--	('1',44,'A',1690987468000,'1',1690987468000,'1');