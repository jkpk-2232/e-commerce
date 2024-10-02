ALTER TABLE vendor_products ADD product_quantity_type character varying(50);
CREATE INDEX idx_vendor_products_product_quantity_type ON vendor_products(product_quantity_type);

ALTER TABLE offline_order_items ADD prd_qty_type character varying(50);
CREATE INDEX idx_offline_order_items_prd_qty_type ON offline_order_items(prd_qty_type);

ALTER TABLE offline_order_items ALTER COLUMN quantity TYPE double precision;

ALTER TABLE offline_orders ADD rounded_value double precision DEFAULT 0;

ALTER TABLE delivery_addresses ADD name character varying(50);
ALTER TABLE delivery_addresses ADD phone_num character varying(30);


CREATE INDEX idx_delivery_addresses_name ON delivery_addresses(name);
CREATE INDEX idx_delivery_addresses_phone_num ON delivery_addresses(phone_num);


CREATE TABLE IF NOT EXISTS public.vendor_product_category_assoc
(
    vendor_product_category_assoc_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    vendor_id character varying(50) COLLATE pg_catalog."default",
    product_category_id character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
	CONSTRAINT vendor_product_category_assoc_id_pk PRIMARY KEY (vendor_product_category_assoc_id)
);

CREATE INDEX idx_vendor_product_category_assoc_vendor_id ON vendor_product_category_assoc(vendor_id);
CREATE INDEX idx_vendor_product_category_assoc_product_category_id ON vendor_product_category_assoc(product_category_id);

CREATE TABLE IF NOT EXISTS public.product_sub_category
(
    product_sub_category_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    product_sub_category_name character varying(100) COLLATE pg_catalog."default",
    product_sub_category_description text COLLATE pg_catalog."default",
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    product_sub_category_image character varying(300) COLLATE pg_catalog."default",
    product_category_id character varying(50) COLLATE pg_catalog."default",
    created_by character varying(50) COLLATE pg_catalog."default",
    created_at bigint,
    updated_by character varying(50) COLLATE pg_catalog."default",
    updated_at bigint,
    CONSTRAINT product_sub_category_id_pk PRIMARY KEY (product_sub_category_id)
);


CREATE INDEX idx_product_sub_category_product_sub_category_id ON product_sub_category(product_sub_category_id);
CREATE INDEX idx_product_sub_category_product_sub_category_name ON product_sub_category(product_sub_category_name);
CREATE INDEX idx_product_sub_category_product_sub_category_description ON product_sub_category(product_sub_category_description);
CREATE INDEX idx_product_sub_category_product_category_id ON product_sub_category(product_category_id);
CREATE INDEX idx_product_sub_category_created_by ON product_sub_category(created_by);
CREATE INDEX idx_product_sub_category_created_at ON product_sub_category(created_at);
CREATE INDEX idx_product_sub_category_updated_by ON product_sub_category(updated_by);
CREATE INDEX idx_product_sub_category_updated_at ON product_sub_category(updated_at);


INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(27,45,'labelProductSubCategory','/manage-product-sub-category.do',2,'A',1692038897000,'1',1692038897000,'1');
	
ALTER TABLE vendor_products ADD product_sub_category_id character varying(50);
ALTER TABLE vendor_products ADD gst double precision;
ALTER TABLE vendor_products ADD is_non_veg  boolean DEFAULT false;
	
CREATE INDEX idx_vendor_products_product_sub_category_id ON vendor_products(product_sub_category_id);
CREATE INDEX idx_vendor_products_gst ON vendor_products(gst);
CREATE INDEX idx_vendor_products_is_non_veg ON vendor_products(is_non_veg);	