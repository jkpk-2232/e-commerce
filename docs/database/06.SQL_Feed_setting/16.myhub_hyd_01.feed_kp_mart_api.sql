CREATE TABLE IF NOT EXISTS public.product_category
(
    product_category_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    product_category_name character varying(100) COLLATE pg_catalog."default",
    product_category_description text COLLATE pg_catalog."default",
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_by character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    CONSTRAINT product_category_id_pk PRIMARY KEY (product_category_id)
);

CREATE INDEX idx_product_category_product_category_id ON product_category(product_category_id);
CREATE INDEX idx_product_category_product_category_name ON product_category(product_category_name);
CREATE INDEX idx_product_category_product_category_description ON product_category(product_category_description);


INSERT INTO product_category
	(product_category_id,product_category_name,product_category_description,created_by,created_at,updated_by,updated_at)
VALUES
	(1,'Food','Food','1',1677755438000,'1',1677755438000);

INSERT INTO product_category
	(product_category_id,product_category_name,product_category_description,created_by,created_at,updated_by,updated_at)
VALUES
	(2,'Fruits','Fruits','1',1677755438000,'1',1677755438000);

ALTER TABLE vendor_products ADD product_barcode character varying(250) ;
ALTER TABLE vendor_products  ADD product_category_id character varying(50);

CREATE INDEX idx_vendor_products_product_barcode ON vendor_products(product_barcode);
CREATE INDEX idx_vendor_products_product_category_id ON vendor_products(product_category_id);


ALTER TABLE admin_settings ADD driver_fare character varying(500000);
ALTER TABLE vendor_admin_settings ADD driver_fare character varying(500000);


INSERT INTO url_sub_categories 
	(url_sub_category_id,url_id,url_title,url,url_icon,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(25,20,'labelDriverFare','/manage-driver-fare.do','fas fa-cogs',20,'A',1692038897000,'1' ,1692038897000  ,'1');


ALTER TABLE users ADD phone_num character varying(50);

CREATE INDEX idx_users_phone_num ON users(phone_num);

ALTER TABLE vendor_stores ADD led_device_for_store boolean;
ALTER TABLE vendor_stores ADD led_device_count integer;


CREATE INDEX idx_vendor_stores_led_device_for_store ON vendor_stores(led_device_for_store);
CREATE INDEX idx_vendor_stores_led_device_count ON vendor_stores(led_device_count);


CREATE TABLE IF NOT EXISTS public.feed_settings
(
  feed_settings_id character varying(50) NOT NULL,
  multicity_city_region_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
  created_at bigint,
    created_by character varying(50),
    updated_at bigint,
    updated_by character varying(50),
  CONSTRAINT feed_settings_id_pk PRIMARY KEY (feed_settings_id)
);

CREATE INDEX idx_feed_settings_multicity_city_region_id ON feed_settings(multicity_city_region_id);
CREATE INDEX idx_feed_settings_created_by ON feed_settings(created_by);
CREATE INDEX idx_feed_settings_created_at ON feed_settings(created_at);
CREATE INDEX idx_feed_settings_updated_by ON feed_settings(updated_by);
CREATE INDEX idx_feed_settings_updated_at ON feed_settings(updated_at);



CREATE TABLE IF NOT EXISTS public.feed_fare
(
  feed_fare_id character varying(50) NOT NULL,
  feed_settings_id  character varying(50) COLLATE pg_catalog."default" NOT NULL,
  service_id   character varying(50) COLLATE pg_catalog."default" NOT NULL ,
  base_fare double precision DEFAULT 0,
  per_minute_fare double precision DEFAULT 0,
  gst_fare double precision DEFAULT 0,
  record_status character varying(50),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT feed_fare_id_pk PRIMARY KEY (feed_fare_id)
);

CREATE INDEX idx_feed_fare_feed_settings_id ON feed_fare(feed_settings_id);
CREATE INDEX idx_feed_fare_service_id ON feed_fare(service_id);
CREATE INDEX idx_feed_fare_created_by ON feed_fare(created_by);
CREATE INDEX idx_feed_fare_created_at ON feed_fare(created_at);
CREATE INDEX idx_feed_fare_updated_by ON feed_fare(updated_by);
CREATE INDEX idx_feed_fare_updated_at ON feed_fare(updated_at);



INSERT INTO url_sub_categories 
	(url_sub_category_id,url_id,url_title,url,url_icon,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(24,20,'labelFeedSettings','/manage-feed-settings.do','fas fa-cogs', 13,'A','1692038897000','1','1692038897000',  '1');


INSERT INTO url_groups  
	(url_group_id,url_group_name,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(44,'labelFeed2',44,'A',1689277870000, '1',  1689277870000,  '1');

INSERT INTO urls
	(url_id,url_group_id,url_title,url,parent_url_id,url_icon,record_status,created_at,created_by,updated_at,updated_by,is_sub_menu_option)
VALUES 
	(44,44,'labelFeed2','/manage-vendor-feature-feeds.do',-1,'fas fa-bell','A',1504588886000,  '1',  1504588886000,  '1',  false);


INSERT INTO url_accesses
	(user_id,url_id,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(1,44,'A',1444203417000, '1',  1444203417000,  '1');


