CREATE TABLE IF NOT EXISTS public.cart
(
    cart_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    vendor_id character varying(50) COLLATE pg_catalog."default",
    vendor_store_id character varying(50) COLLATE pg_catalog."default",
    vendor_product_id character varying(50) COLLATE pg_catalog."default",
    quantity integer,
    user_id character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT cart_id_pk PRIMARY KEY (cart_id)
);


CREATE INDEX idx_cart_vendor_id ON cart(vendor_id);
CREATE INDEX idx_cart_vendor_store_id ON cart(vendor_store_id);
CREATE INDEX idx_cart_user_id ON cart(user_id);
CREATE INDEX idx_cart_created_by ON cart(created_by);
CREATE INDEX idx_cart_created_at ON cart(created_at);
CREATE INDEX idx_cart_updated_by ON cart(updated_by);
CREATE INDEX idx_cart_updated_at ON cart(updated_at);


CREATE TABLE IF NOT EXISTS public.offline_orders
(
    offline_order_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    offline_store_order_id character varying(50) COLLATE pg_catalog."default",
    ref_number character varying(50) COLLATE pg_catalog."default",
    discount double precision DEFAULT 0,
    status character varying(50) COLLATE pg_catalog."default",
    sub_total double precision DEFAULT 0,
    tax double precision DEFAULT 0,
    order_type character varying(50) COLLATE pg_catalog."default",
    date bigint,
    payment_type character varying(50) COLLATE pg_catalog."default",
    payment_info character varying(50) COLLATE pg_catalog."default",
    total double precision DEFAULT 0,
    paid double precision DEFAULT 0,
    change double precision DEFAULT 0,
    user_id character varying(50) COLLATE pg_catalog."default",
    cloud_order_id character varying(50) COLLATE pg_catalog."default",
    counter_no character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT offline_order_id_pk PRIMARY KEY (offline_order_id)
);

CREATE INDEX idx_offline_orders_offline_store_order_id ON offline_orders(offline_store_order_id);
CREATE INDEX idx_offline_orders_user_id ON offline_orders(user_id);
CREATE INDEX idx_offline_orders_order_type ON offline_orders(order_type);
CREATE INDEX idx_offline_orders_date ON offline_orders(date);
CREATE INDEX idx_offline_orders_counter_no ON offline_orders(counter_no);
CREATE INDEX idx_offline_orders_cloud_order_id ON offline_orders(cloud_order_id);
CREATE INDEX idx_offline_orders_payment_type ON offline_orders(payment_type);
CREATE INDEX idx_offline_orders_status ON offline_orders(status);
CREATE INDEX idx_offline_orders_total ON offline_orders(total);
CREATE INDEX idx_offline_orders_paid ON offline_orders(paid);
CREATE INDEX idx_offline_orders_change ON offline_orders(change);
CREATE INDEX idx_offline_orders_created_by ON offline_orders(created_by);
CREATE INDEX idx_offline_orders_created_at ON offline_orders(created_at);
CREATE INDEX idx_offline_orders_updated_by ON offline_orders(updated_by);
CREATE INDEX idx_offline_orders_updated_at ON offline_orders(updated_at);


CREATE TABLE IF NOT EXISTS public.offline_order_items
(
    offline_order_item_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    offline_store_order_item_id character varying(50) COLLATE pg_catalog."default",
    product_name text COLLATE pg_catalog."default",
    product_sku text COLLATE pg_catalog."default",
    price double precision DEFAULT 0,
    quantity integer,
    offline_order_id character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT offline_order_item_id_pk PRIMARY KEY (offline_order_item_id)
);


CREATE INDEX idx_offline_order_items_offline_store_order_item_id ON offline_order_items(offline_store_order_item_id);
CREATE INDEX idx_offline_order_items_product_name ON offline_order_items(product_name);
CREATE INDEX idx_offline_order_items_product_sku ON offline_order_items(product_sku);
CREATE INDEX idx_offline_order_items_price ON offline_order_items(price);
CREATE INDEX idx_offline_order_items_quantity ON offline_order_items(quantity);
CREATE INDEX idx_offline_order_items_offline_order_id ON offline_order_items(offline_order_id);
CREATE INDEX idx_offline_order_items_created_by ON offline_order_items(created_by);
CREATE INDEX idx_offline_order_items_created_at ON offline_order_items(created_at);
CREATE INDEX idx_offline_order_items_updated_by ON offline_order_items(updated_by);
CREATE INDEX idx_offline_order_items_updated_at ON offline_order_items(updated_at);




INSERT INTO url_groups 
	(url_group_id,url_group_name,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(45,'labelMasterData',45,'A',1689277870000,'1',1689277870000,'1');


INSERT INTO urls 
	(url_id,url_group_id,url_title,url,parent_url_id,record_status,created_at,created_by,updated_at,updated_by,is_sub_menu_option)
VALUES 
	(45,45,'labelMasterData','/manage-product-category.do',-1,'A',1504588886000,'1',1504588886000,'1',true);


INSERT INTO url_sub_categories 
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(26,45,'labelProductCategory','/manage-product-category.do',1,'A',1692038897000,'1',1692038897000,'1');


INSERT INTO url_accesses 
	(user_id,url_id,record_status,created_at,created_by,updated_at,updated_by)
VALUES 
	(1,45,'A',1692038897000,'1',1692038897000,'1');


ALTER TABLE product_category ADD product_category_image character varying(300);

CREATE INDEX idx_product_category_created_by ON product_category(created_by);
CREATE INDEX idx_product_category_created_at ON product_category(created_at);
CREATE INDEX idx_product_category_updated_by ON product_category(updated_by);
CREATE INDEX idx_product_category_updated_at ON product_category(updated_at);


ALTER TABLE delivery_addresses ADD is_default  boolean DEFAULT false;


CREATE TABLE IF NOT EXISTS public.customer
(
    customer_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    phone_num character varying(50) COLLATE pg_catalog."default",
    customer_name character varying(50) COLLATE pg_catalog."default",
    offline_order_id character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT customer_id_pk PRIMARY KEY (customer_id)
);

CREATE INDEX idx_customer_phone_num ON customer(phone_num);
CREATE INDEX idx_customer_customer_name ON customer(customer_name);
CREATE INDEX idx_customer_offline_order_id ON customer(offline_order_id);
CREATE INDEX idx_customer_created_by ON customer(created_by);
CREATE INDEX idx_customer_created_at ON customer(created_at);
CREATE INDEX idx_customer_updated_by ON customer(updated_by);
CREATE INDEX idx_customer_updated_at ON customer(updated_at);


ALTER TABLE offline_order_items ADD mrp  double precision DEFAULT 0;

CREATE INDEX idx_offline_order_items_mrp ON offline_order_items(mrp);