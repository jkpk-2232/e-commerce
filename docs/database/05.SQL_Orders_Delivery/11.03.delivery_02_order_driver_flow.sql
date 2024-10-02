--Add vendor feed into access management
INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(41,'Feeds','A',41,1679771180000,'1',1679771180000,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(41,41,'Feeds','/manage-vendor-feeds.do',-1,'A',1679771180000,'1',1679771180000,'1','glyphicon glyphicon-bullhorn');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',41,'A',1679771180000,'1',1679771180000,'1');

--Vendor feeds flow tables
CREATE TABLE vendor_feeds
(
  vendor_feed_id character varying(32) NOT NULL,
  vendor_id character varying(32),
  feed_name character varying(50),
  feed_message character varying(300),
  feed_baner character varying(200),
  feed_views_count bigint,
  feed_likes_count bigint, 
  feed_notification_status character varying(10),
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT vendor_feed_id_pk PRIMARY KEY (vendor_feed_id)
);

CREATE INDEX idx_vendor_feeds_vendor_id ON vendor_feeds(vendor_id);
CREATE INDEX idx_vendor_feeds_feed_name ON vendor_feeds(feed_name);
CREATE INDEX idx_vendor_feeds_feed_message ON vendor_feeds(feed_message);
CREATE INDEX idx_vendor_feeds_feed_views_count ON vendor_feeds(feed_views_count);
CREATE INDEX idx_vendor_feeds_feed_likes_count ON vendor_feeds(feed_likes_count);
CREATE INDEX idx_vendor_feeds_feed_notification_status ON vendor_feeds(feed_notification_status);
CREATE INDEX idx_vendor_feeds_created_by ON vendor_feeds(created_by);
CREATE INDEX idx_vendor_feeds_created_at ON vendor_feeds(created_at);
CREATE INDEX idx_vendor_feeds_updated_by ON vendor_feeds(updated_by);
CREATE INDEX idx_vendor_feeds_updated_at ON vendor_feeds(updated_at);

CREATE TABLE vendor_feed_likes
(
  vendor_feed_like_id character varying(32) NOT NULL,
  vendor_feed_id character varying(32),
  vendor_id character varying(32),
  user_id character varying(32),
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT vendor_feed_like_id_pk PRIMARY KEY (vendor_feed_like_id)
);

CREATE INDEX idx_vendor_feed_likes_vendor_feed_id ON vendor_feed_likes(vendor_feed_id);
CREATE INDEX idx_vendor_feed_likes_vendor_id ON vendor_feed_likes(vendor_id);
CREATE INDEX idx_vendor_feed_likes_user_id ON vendor_feed_likes(user_id);
CREATE INDEX idx_vendor_feed_likes_created_by ON vendor_feed_likes(created_by);
CREATE INDEX idx_vendor_feed_likes_created_at ON vendor_feed_likes(created_at);
CREATE INDEX idx_vendor_feed_likes_updated_by ON vendor_feed_likes(updated_by);
CREATE INDEX idx_vendor_feed_likes_updated_at ON vendor_feed_likes(updated_at);

CREATE TABLE vendor_feed_views
(
  vendor_feed_view_id character varying(32) NOT NULL,
  vendor_feed_id character varying(32),
  vendor_id character varying(32),
  user_id character varying(32),
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT vendor_feed_view_id_pk PRIMARY KEY (vendor_feed_view_id)
);

CREATE INDEX idx_vendor_feed_views_vendor_feed_id ON vendor_feed_views(vendor_feed_id);
CREATE INDEX idx_vendor_feed_views_vendor_id ON vendor_feed_views(vendor_id);
CREATE INDEX idx_vendor_feed_views_user_id ON vendor_feed_views(user_id);
CREATE INDEX idx_vendor_feed_views_created_by ON vendor_feed_views(created_by);
CREATE INDEX idx_vendor_feed_views_created_at ON vendor_feed_views(created_at);
CREATE INDEX idx_vendor_feed_views_updated_by ON vendor_feed_views(updated_by);
CREATE INDEX idx_vendor_feed_views_updated_at ON vendor_feed_views(updated_at);

--Vendor feed id in apns notifications
ALTER TABLE apns_notification_messages ADD vendor_feed_id character varying(50);
CREATE INDEX idx_apns_notification_messages_vendor_feed_id ON apns_notification_messages(vendor_feed_id);

--Vendor stores multiple locations table
CREATE TABLE vendor_stores
(
  vendor_store_id character varying(32) NOT NULL,
  vendor_id character varying(32),
  store_name character varying(50),
  store_address text,
  store_address_lat character varying(50),
  store_address_lng character varying(50),
  store_place_id character varying(300),
  store_address_geolocation geography(Point,4326),
  store_image character varying(300),
  date_type int,
  number_of_shifts int,
  shift_type int,
  start_date bigint DEFAULT 0,
  end_date bigint DEFAULT 0,
  date_opening_morning_hours bigint DEFAULT 0,
  date_closing_morning_hours bigint DEFAULT 0,
  date_opening_evening_hours bigint DEFAULT 0,
  date_closing_evening_hours bigint DEFAULT 0,
  is_closed_today boolean DEFAULT false,
  is_active boolean DEFAULT TRUE,
  is_deleted boolean DEFAULT FALSE,
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT vendor_store_id_pk PRIMARY KEY (vendor_store_id)
);

CREATE INDEX idx_vendor_stores_vendor_id ON vendor_stores(vendor_id);
CREATE INDEX idx_vendor_stores_store_name ON vendor_stores(store_name);
CREATE INDEX idx_vendor_stores_store_address ON vendor_stores(store_address);
CREATE INDEX idx_vendor_stores_store_address_lat ON vendor_stores(store_address_lat);
CREATE INDEX idx_vendor_stores_store_address_lng ON vendor_stores(store_address_lng);
CREATE INDEX idx_vendor_stores_store_place_id ON vendor_stores(store_place_id);
CREATE INDEX idx_vendor_stores_store_address_geolocation ON vendor_stores(store_address_geolocation);
CREATE INDEX idx_vendor_stores_date_type ON vendor_stores(date_type);
CREATE INDEX idx_vendor_stores_number_of_shifts ON vendor_stores(number_of_shifts);
CREATE INDEX idx_vendor_stores_shift_type ON vendor_stores(shift_type);
CREATE INDEX idx_vendor_stores_start_date ON vendor_stores(start_date);
CREATE INDEX idx_vendor_stores_end_date ON vendor_stores(end_date);
CREATE INDEX idx_vendor_stores_date_opening_morning_hours ON vendor_stores(date_opening_morning_hours);
CREATE INDEX idx_vendor_stores_date_closing_morning_hours ON vendor_stores(date_closing_morning_hours);
CREATE INDEX idx_vendor_stores_date_opening_evening_hours ON vendor_stores(date_opening_evening_hours);
CREATE INDEX idx_vendor_stores_date_closing_evening_hours ON vendor_stores(date_closing_evening_hours);
CREATE INDEX idx_vendor_stores_is_closed_today ON vendor_stores(is_closed_today);
CREATE INDEX idx_vendor_stores_is_active ON vendor_stores(is_active);
CREATE INDEX idx_vendor_stores_is_deleted ON vendor_stores(is_deleted);
CREATE INDEX idx_vendor_stores_created_by ON vendor_stores(created_by);
CREATE INDEX idx_vendor_stores_created_at ON vendor_stores(created_at);
CREATE INDEX idx_vendor_stores_updated_by ON vendor_stores(updated_by);
CREATE INDEX idx_vendor_stores_updated_at ON vendor_stores(updated_at);

--Vendor store timings table to store timings
CREATE TABLE vendor_store_timings
(
  vendor_store_timing_id character varying(32) NOT NULL,
  vendor_store_id character varying(32),
  day character varying(1),
  opening_morning_hours bigint DEFAULT 0,
  closing_morning_hours bigint DEFAULT 0,
  opening_evening_hours bigint DEFAULT 0,
  closing_evening_hours bigint DEFAULT 0,
  record_status character varying(1),
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT vendor_store_timing_id_pk PRIMARY KEY (vendor_store_timing_id)
);

CREATE INDEX idx_vendor_store_timings_vendor_store_id ON vendor_store_timings(vendor_store_id);
CREATE INDEX idx_vendor_store_timings_day ON vendor_store_timings(day);
CREATE INDEX idx_vendor_store_timings_opening_morning_hours ON vendor_store_timings(opening_morning_hours);
CREATE INDEX idx_vendor_store_timings_closing_morning_hours ON vendor_store_timings(closing_morning_hours);
CREATE INDEX idx_vendor_store_timings_opening_evening_hours ON vendor_store_timings(opening_evening_hours);
CREATE INDEX idx_vendor_store_timings_closing_evening_hours ON vendor_store_timings(closing_evening_hours);
CREATE INDEX idx_vendor_store_timings_record_status ON vendor_store_timings(record_status);
CREATE INDEX idx_vendor_store_timings_created_by ON vendor_store_timings(created_by);
CREATE INDEX idx_vendor_store_timings_created_at ON vendor_store_timings(created_at);
CREATE INDEX idx_vendor_store_timings_updated_by ON vendor_store_timings(updated_by);
CREATE INDEX idx_vendor_store_timings_updated_at ON vendor_store_timings(updated_at);

--Add vendor store id in orders flow
ALTER TABLE orders ADD vendor_store_id character varying(32);
CREATE INDEX idx_orders_vendor_store_id ON orders(vendor_store_id);

--Remove ununsed columns for vendor address
ALTER TABLE user_info DROP COLUMN vendor_brand_address_lat;
ALTER TABLE user_info DROP COLUMN vendor_brand_address_lng;
ALTER TABLE user_info DROP COLUMN vendor_brand_place_id;
ALTER TABLE user_info DROP COLUMN vendor_brand_address;
ALTER TABLE user_info DROP COLUMN vendor_brand_address_geolocation;

--Order delivery management by vendor
ALTER TABLE orders ADD is_delievery_managed_by_vendor_driver BOOLEAN DEFAULT FALSE;
ALTER TABLE orders ADD driver_id character varying(32) DEFAULT '-1';
CREATE INDEX idx_orders_is_delievery_managed_by_vendor_driver ON orders(is_delievery_managed_by_vendor_driver);
CREATE INDEX idx_orders_driver_id ON orders(driver_id);

--Order requests mapping against driver
CREATE TABLE driver_order_requests
(
    driver_order_request_id character varying(32) NOT NULL,
    order_id character varying(32),
    driver_id character varying(32),
    status character varying(10),
    created_by character varying(32),
    created_at bigint,
    updated_by character varying(32),
    updated_at bigint,
    CONSTRAINT driver_order_request_id_pk PRIMARY KEY (driver_order_request_id)
);

CREATE INDEX idx_driver_order_requests_order_id ON driver_order_requests(order_id);
CREATE INDEX idx_driver_order_requests_driver_id ON driver_order_requests(driver_id);
CREATE INDEX idx_driver_order_requests_status ON driver_order_requests(status);
CREATE INDEX idx_driver_order_requests_created_by ON driver_order_requests(created_by);
CREATE INDEX idx_driver_order_requests_created_at ON driver_order_requests(created_at);
CREATE INDEX idx_driver_order_requests_updated_by ON driver_order_requests(updated_by);
CREATE INDEX idx_driver_order_requests_updated_at ON driver_order_requests(updated_at);

--Order settings for order cancellation
ALTER TABLE order_settings ADD order_job_cancellation_time_hours int DEFAULT 2;
CREATE INDEX idx_order_settings_order_job_cancellation_time_hours ON order_settings(order_job_cancellation_time_hours);

--Order settings for order new expirity
ALTER TABLE order_settings ADD order_new_cancellation_time_hours int DEFAULT 2;
CREATE INDEX idx_order_settings_order_new_cancellation_time_hours ON order_settings(order_new_cancellation_time_hours);

--Vendor feed id in apns notifications
ALTER TABLE apns_notification_messages DROP vendor_feed_id;
ALTER TABLE apns_notification_messages ADD extra_info_type character varying(20);
ALTER TABLE apns_notification_messages ADD extra_info_id character varying(32);
CREATE INDEX idx_apns_notification_messages_extra_info_type ON apns_notification_messages(extra_info_type);
CREATE INDEX idx_apns_notification_messages_extra_info_id ON apns_notification_messages(extra_info_id);

--New menu tab for Vendor Stores
INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(42,'Vendor Stores','A',42,1680711482000,'1',1680711482000,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(42,42,'Vendor Stores','/manage-vendor-store.do',-1,'A',1680711482000,'1',1680711482000,'1','icon icon-location-arrow');

--Change vendor subscription to vendor store subscription
DELETE FROM vendor_subscribers;
ALTER TABLE vendor_subscribers ADD vendor_store_id character varying(32); 
CREATE INDEX idx_vendor_subscribers_vendor_store_id ON vendor_subscribers(vendor_store_id);

--Add vendor store id in vendor products
ALTER TABLE vendor_products ADD is_product_for_all_vendor_stores boolean DEFAULT FALSE; 
ALTER TABLE vendor_products ADD vendor_store_id character varying(32); 
CREATE INDEX idx_vendor_products_is_product_for_all_vendor_stores ON vendor_products(is_product_for_all_vendor_stores);
CREATE INDEX idx_vendor_products_vendor_store_id ON vendor_products(vendor_store_id);

--Add region to vendor stores
ALTER TABLE vendor_stores ADD multicity_country_id character varying(32); 
ALTER TABLE vendor_stores ADD multicity_city_region_id character varying(32); 
CREATE INDEX idx_vendor_stores_multicity_country_id ON vendor_stores(multicity_country_id);
CREATE INDEX idx_vendor_stores_multicity_city_region_id ON vendor_stores(multicity_city_region_id);

--Add region to orders
ALTER TABLE orders ADD multicity_country_id character varying(32); 
ALTER TABLE orders ADD multicity_city_region_id character varying(32);
CREATE INDEX idx_orders_multicity_country_id ON orders(multicity_country_id);
CREATE INDEX idx_orders_multicity_city_region_id ON orders(multicity_city_region_id);

--Add car type id to orders - Default will be bike
ALTER TABLE orders ADD car_type_id character varying(32) DEFAULT '7'; 
CREATE INDEX idx_orders_car_type_id ON orders(car_type_id);
UPDATE orders SET car_type_id='7';

--SKU in products flow
ALTER TABLE vendor_products ADD product_sku text;
CREATE INDEX idx_vendor_products_product_sku ON vendor_products(product_sku);

ALTER TABLE order_items ADD product_sku text;
CREATE INDEX idx_order_items_product_sku ON order_items(product_sku);

--Vendor product category
ALTER TABLE vendor_products ADD product_category character varying(100);
CREATE INDEX idx_vendor_products_product_category ON vendor_products(product_category);

ALTER TABLE order_items ADD product_category character varying(100);
CREATE INDEX idx_order_items_product_category ON order_items(product_category);

--Customer delivery address
CREATE TABLE delivery_addresses
(
    delivery_address_id character varying(32) NOT NULL,
    user_id character varying(32),
    address_type character varying(1),
    address character varying(300),
    address_lat character varying(50),
    address_lng character varying(50),
    place_id character varying(300),
    address_geolocation geography(Point,4326),
    record_status character varying(1),
    created_by character varying(32),
    created_at bigint,
    updated_by character varying(32),
    updated_at bigint,
    CONSTRAINT delivery_address_id_pk PRIMARY KEY (delivery_address_id)
);

CREATE INDEX idx_delivery_addresses_user_id ON delivery_addresses(user_id);
CREATE INDEX idx_delivery_addresses_address_type ON delivery_addresses(address_type);
CREATE INDEX idx_delivery_addresses_address ON delivery_addresses(address);
CREATE INDEX idx_delivery_addresses_address_geolocation ON delivery_addresses(address_geolocation);
CREATE INDEX idx_delivery_addresses_record_status ON delivery_addresses(record_status);
CREATE INDEX idx_delivery_addresses_created_by ON delivery_addresses(created_by);
CREATE INDEX idx_delivery_addresses_created_at ON delivery_addresses(created_at);
CREATE INDEX idx_delivery_addresses_updated_by ON delivery_addresses(updated_by);
CREATE INDEX idx_delivery_addresses_updated_at ON delivery_addresses(updated_at);

--Product inventory count in products flow
ALTER TABLE vendor_products ADD product_inventory_count bigint;
CREATE INDEX idx_vendor_products_product_inventory_count ON vendor_products(product_inventory_count);
