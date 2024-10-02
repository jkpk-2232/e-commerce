CREATE TABLE service_types
(
  service_type_id character varying(5) NOT NULL,
  service_type_name character varying(100),
  service_type_description text,
  is_active boolean DEFAULT TRUE,
  is_deleted boolean DEFAULT FALSE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT service_type_id_pk PRIMARY KEY (service_type_id)
);

CREATE INDEX idx_service_types_service_type_name ON service_types(service_type_name);
CREATE INDEX idx_service_types_service_type_description ON service_types(service_type_description);
CREATE INDEX idx_service_types_created_by ON service_types(created_by);
CREATE INDEX idx_service_types_created_at ON service_types(created_at);
CREATE INDEX idx_service_types_updated_by ON service_types(updated_by);
CREATE INDEX idx_service_types_updated_at ON service_types(updated_at);

INSERT INTO service_types
(service_type_id, service_type_name, service_type_description, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES 
('1', 'Transportation', 'Transportation', true, false, '1', 1677755438000, '1', 1677755438000),
('2', 'Ecommerce', 'Ecommerce', true, false, '1', 1677755438001, '1', 1677755438001);

ALTER TABLE services ADD service_type_id character varying(5) DEFAULT '1';
UPDATE services SET service_type_id='1' WHERE service_id='1';
UPDATE services SET service_type_id='2' WHERE service_id='2';

CREATE TABLE order_settings
(
  order_setting_id character varying(50) NOT NULL,
  service_id character varying(50),
  max_number_of_items double precision DEFAULT 10,
  max_weight_allowed double precision DEFAULT 10,
  free_cancellation_time_mins int DEFAULT 0,
  delivery_base_fee double precision DEFAULT 0,
  delivery_base_km double precision DEFAULT 0,
  delivery_fee_per_km double precision DEFAULT 0,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT order_setting_id_pk PRIMARY KEY (order_setting_id)
);

CREATE INDEX idx_order_settings_service_id ON order_settings(service_id);
CREATE INDEX idx_order_settings_created_by ON order_settings(created_by);
CREATE INDEX idx_order_settings_created_at ON order_settings(created_at);
CREATE INDEX idx_order_settings_updated_by ON order_settings(updated_by);
CREATE INDEX idx_order_settings_updated_at ON order_settings(updated_at);

INSERT INTO order_settings
(
order_setting_id, service_id, max_number_of_items, max_weight_allowed, 	
free_cancellation_time_mins, delivery_base_fee, delivery_base_km, 
delivery_fee_per_km, created_by, created_at, updated_by, updated_at
)
VALUES 
('1', '1', 10, 10, 0, 0, 0, 0, '1', 1677755438000, '1', 1677755438000),
('2', '2', 10, 10, 0, 0, 0, 0, '1', 1677755438001, '1', 1677755438001);

CREATE TABLE driver_subscribers
(
  driver_subscriber_id character varying(50) NOT NULL,
  driver_id character varying(50),
  user_id character varying(50),
  priority_number int DEFAULT 0,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_subscriber_id_pk PRIMARY KEY (driver_subscriber_id)
);

CREATE INDEX idx_driver_subscribers_driver_id ON driver_subscribers(driver_id);
CREATE INDEX idx_driver_subscribers_user_id ON driver_subscribers(user_id);
CREATE INDEX idx_driver_subscribers_created_by ON driver_subscribers(created_by);
CREATE INDEX idx_driver_subscribers_created_at ON driver_subscribers(created_at);
CREATE INDEX idx_driver_subscribers_updated_by ON driver_subscribers(updated_by);
CREATE INDEX idx_driver_subscribers_updated_at ON driver_subscribers(updated_at);

CREATE TABLE orders
(
  order_id character varying(50) NOT NULL,
  order_short_id serial NOT NULL,
  order_user_id character varying(50),
  order_received_against_vendor_id character varying(50),
  order_creation_time bigint,
  order_delivery_status character varying(50),
  order_delivery_address text,
  order_delivery_address_geolocation geography(Point,4326),
  order_delivery_lat character varying(50),
  order_delivery_lng character varying(50),
  order_promo_code_id character varying(50),
  order_promo_code_discount double precision DEFAULT 0,
  order_total double precision DEFAULT 0,
  order_delivery_charges double precision DEFAULT 0,
  order_charges double precision DEFAULT 0,
  order_delivery_distance double precision DEFAULT 0,
  payment_mode character varying(1),
  payment_status character varying(10),
  order_number_of_items int,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT order_id_pk PRIMARY KEY (order_id)
);

CREATE INDEX idx_orders_order_short_id ON orders(order_short_id);
CREATE INDEX idx_orders_order_user_id ON orders(order_user_id);
CREATE INDEX idx_orders_order_received_against_vendor_id ON orders(order_received_against_vendor_id);
CREATE INDEX idx_orders_order_creation_time ON orders(order_creation_time);
CREATE INDEX idx_orders_order_delivery_status ON orders(order_delivery_status);
CREATE INDEX idx_orders_order_delivery_address ON orders(order_delivery_address);
CREATE INDEX idx_orders_order_delivery_address_geolocation ON orders(order_delivery_address_geolocation);
CREATE INDEX idx_orders_order_delivery_lat ON orders(order_delivery_lat);
CREATE INDEX idx_orders_order_delivery_lng ON orders(order_delivery_lng);
CREATE INDEX idx_orders_order_promo_code_id ON orders(order_promo_code_id);
CREATE INDEX idx_orders_payment_mode ON orders(payment_mode);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_order_number_of_items ON orders(order_number_of_items);
CREATE INDEX idx_orders_order_delivery_charges ON orders(order_delivery_charges);
CREATE INDEX idx_orders_order_delivery_distance ON orders(order_delivery_distance);
CREATE INDEX idx_orders_record_status ON orders(record_status);
CREATE INDEX idx_orders_created_by ON orders(created_by);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_updated_by ON orders(updated_by);
CREATE INDEX idx_orders_updated_at ON orders(updated_at);

CREATE TABLE order_items
(
  order_item_id character varying(50) NOT NULL,
  order_id character varying(50),
  number_of_items_ordered int,
  vendor_product_id character varying(50),
  vendor_id character varying(50),
  product_name text,
  product_information text,
  product_actual_price double precision DEFAULT 0,
  product_discounted_price double precision DEFAULT 0,
  product_weight double precision DEFAULT 0,
  product_weight_unit int DEFAULT 1,
  product_specification text,
  product_image character varying(300),
  is_paid boolean DEFAULT TRUE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT order_item_id_pk PRIMARY KEY (order_item_id)
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_number_of_items_ordered ON order_items(number_of_items_ordered);
CREATE INDEX idx_order_items_vendor_product_id ON order_items(vendor_product_id);
CREATE INDEX idx_order_items_vendor_id ON order_items(vendor_id);
CREATE INDEX idx_order_items_product_name ON order_items(product_name);
CREATE INDEX idx_order_items_product_information ON order_items(product_information);
CREATE INDEX idx_order_items_product_actual_price ON order_items(product_actual_price);
CREATE INDEX idx_order_items_product_discounted_price ON order_items(product_discounted_price);
CREATE INDEX idx_order_items_product_weight ON order_items(product_weight);
CREATE INDEX idx_order_items_product_specification ON order_items(product_specification);
CREATE INDEX idx_order_items_created_by ON order_items(created_by);
CREATE INDEX idx_order_items_created_at ON order_items(created_at);
CREATE INDEX idx_order_items_updated_by ON order_items(updated_by);
CREATE INDEX idx_order_items_updated_at ON order_items(updated_at);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(40,'Promo Code','A',40,1678561934000,'1',1678561934000,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(40,40,'Promo Code','/manage-promo-code.do',-1,'A',1678561934000,'1',1678561934000,'1','glyphicon glyphicon-cogwheel');

ALTER TABLE promo_code ADD service_type_id character varying(50);
ALTER TABLE promo_code ADD max_discount double precision DEFAULT 0;
ALTER TABLE promo_code RENAME COLUMN user_id to vendor_id;

CREATE INDEX idx_promo_code_service_type_id ON promo_code(service_type_id);
CREATE INDEX idx_promo_code_max_discount ON promo_code(max_discount);
CREATE INDEX idx_promo_code_vendor_id ON promo_code(vendor_id);



















