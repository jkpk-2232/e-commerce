CREATE TABLE services
(
  service_id character varying(50) NOT NULL,
  service_name character varying(100),
  service_description text,
  is_default boolean DEFAULT FALSE,
  is_active boolean DEFAULT TRUE,
  is_deleted boolean DEFAULT FALSE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT service_id_pk PRIMARY KEY (service_id)
);

CREATE INDEX idx_services_service_name ON services(service_name);
CREATE INDEX idx_services_service_description ON services(service_description);

CREATE TABLE categories
(
  category_id character varying(50) NOT NULL,
  service_id character varying(50),
  category_name character varying(100),
  category_description text,
  is_active boolean DEFAULT TRUE,
  is_deleted boolean DEFAULT FALSE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT category_id_pk PRIMARY KEY (category_id)
);

CREATE INDEX idx_categories_service_id ON categories(service_id);
CREATE INDEX idx_categories_category_name ON categories(category_name);
CREATE INDEX idx_categories_category_description ON categories(category_description);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(36,'Super Services','A',36,1633028766000,'1',1633028766000,'1'),
	(37,'Categories','A',37,1633028766001,'1',1633028766001,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(36,36,'Super Services','/manage-super-services.do',-1,'A',1633028766000,'1',1633028766000,'1','icon icon-fire'),
	(37,37,'Categories','/manage-categories.do',-1,'A',1633028766001,'1',1633028766001,'1','icon icon-fire');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',36,'A',1633028766000,'1',1633028766000,'1'),
	('1',37,'A',1633028766001,'1',1633028766001,'1');

INSERT INTO services
(service_id, service_name, service_description, is_default, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES 
('1', 'Cab Transportation', 'Cab Transportation', true, true, false, '1', 1633115167000, '1', 1633115167000),
('2', 'Delivery', 'Delivery', false, true, false, '1', 1633115167001, '1', 1633115167001);

INSERT INTO categories
(category_id, service_id, category_name, category_description, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES 
('1', '1', 'Cab Transportation', 'Cab Transportation', true, false, '1', 1633115167000, '1', 1633115167000),
('2', '2', 'Delivery', 'Delivery', true, false, '1', 1633115167001, '1', 1633115167001);

CREATE TABLE vendor_service_categories
(
  vendor_service_category_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  service_id character varying(50),
  category_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_service_category_id_pk PRIMARY KEY (vendor_service_category_id)
);

CREATE INDEX idx_vendor_service_categories_vendor_id ON vendor_service_categories(vendor_id);
CREATE INDEX idx_vendor_service_categories_service_id ON vendor_service_categories(service_id);
CREATE INDEX idx_vendor_service_categories_category_id ON vendor_service_categories(category_id);

ALTER TABLE user_info ADD vendor_brand_name character varying(100);
ALTER TABLE user_info ADD vendor_brand_address text;
ALTER TABLE user_info ADD vendor_brand_address_lat character varying(300);
ALTER TABLE user_info ADD vendor_brand_address_lng character varying(300);
ALTER TABLE user_info ADD vendor_brand_place_id character varying(300);
ALTER TABLE user_info ADD vendor_brand_address_geolocation geography(Point,4326);
ALTER TABLE user_info ADD vendor_brand_image character varying(300);
ALTER TABLE user_info ADD vendor_brand_search_keywords text;

CREATE INDEX idx_user_info_vendor_brand_name ON user_info(vendor_brand_name);
CREATE INDEX idx_user_info_vendor_brand_address ON user_info(vendor_brand_address);
CREATE INDEX idx_user_info_vendor_brand_address_lat ON user_info(vendor_brand_address_lat);
CREATE INDEX idx_user_info_vendor_brand_address_lng ON user_info(vendor_brand_address_lng);
CREATE INDEX idx_user_info_vendor_brand_place_id ON user_info(vendor_brand_place_id);
CREATE INDEX idx_user_info_vendor_brand_address_geolocation ON user_info(vendor_brand_address_geolocation);
CREATE INDEX idx_user_info_vendor_brand_search_keywords ON user_info(vendor_brand_search_keywords);

CREATE TABLE vendor_products
(
  vendor_product_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  product_name text,
  product_information text,
  product_actual_price double precision DEFAULT 0,
  product_discounted_price double precision DEFAULT 0,
  product_weight double precision DEFAULT 0,
  product_weight_unit int DEFAULT 1,
  product_specification text,
  product_image character varying(300),
  is_active boolean DEFAULT TRUE,
  is_deleted boolean DEFAULT FALSE,
  is_paid boolean DEFAULT TRUE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_product_id_pk PRIMARY KEY (vendor_product_id)
);

CREATE INDEX idx_vendor_products_vendor_id ON vendor_products(vendor_id);
CREATE INDEX idx_vendor_products_product_name ON vendor_products(product_name);
CREATE INDEX idx_vendor_products_product_information ON vendor_products(product_information);
CREATE INDEX idx_vendor_products_product_actual_price ON vendor_products(product_actual_price);
CREATE INDEX idx_vendor_products_product_discounted_price ON vendor_products(product_discounted_price);
CREATE INDEX idx_vendor_products_product_weight ON vendor_products(product_weight);
CREATE INDEX idx_vendor_products_product_specification ON vendor_products(product_specification);

CREATE TABLE vendor_subscribers
(
  vendor_subscriber_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  user_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_subscriber_id_pk PRIMARY KEY (vendor_subscriber_id)
);

CREATE INDEX idx_vendor_subscribers_vendor_id ON vendor_subscribers(vendor_id);
CREATE INDEX idx_vendor_subscribers_user_id ON vendor_subscribers(user_id);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(38,'Products','A',38,1633637750000,'1',1633637750000,'1'),
	(39,'Subscribers','A',39,1633637750000,'1',1633637750000,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(38,38,'Products','/manage-products.do',-1,'A',1633637750000,'1',1633637750000,'1','icon icon-fire'),
	(39,39,'Subscribers','/manage-subscribers.do',-1,'A',1633637750000,'1',1633637750000,'1','icon icon-ticket');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',38,'A',1633637750000,'1',1633637750000,'1'),
	('1',39,'A',1633637750000,'1',1633637750000,'1');

UPDATE urls SET url_title='Customers' WHERE url_id='5';
UPDATE urls SET url_title='Book Later' WHERE url_id='17';
UPDATE urls SET url_title='Critical Book Later' WHERE url_id='18';
UPDATE urls SET url_title='Book Later' WHERE url_id='25';
UPDATE urls SET url_title='Critical Book Later' WHERE url_id='26';
UPDATE urls SET url_title='Take Bookings' WHERE url_id='35';

UPDATE url_groups SET url_group_name='Customers' WHERE url_group_id='5';
UPDATE url_groups SET url_group_name='Book Later' WHERE url_group_id='17';
UPDATE url_groups SET url_group_name='Critical Book Later' WHERE url_group_id='18';
UPDATE url_groups SET url_group_name='Book Later' WHERE url_group_id='25';
UPDATE url_groups SET url_group_name='Critical Book Later' WHERE url_group_id='26';
UPDATE url_groups SET url_group_name='Take Bookings' WHERE url_group_id='35';
