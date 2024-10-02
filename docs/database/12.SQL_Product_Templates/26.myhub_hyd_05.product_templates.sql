CREATE TABLE brands
(
    brand_id character varying(50)   NOT NULL,
    brand_name character varying(100)  ,
    brand_description text  ,
    brand_image character varying(300)  ,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_by character varying(50)  ,
    created_at bigint,
    updated_by character varying(50)  ,
    updated_at bigint,
    CONSTRAINT brand_id_pk PRIMARY KEY (brand_id)
);

CREATE INDEX idx_brands_brand_name ON brands(brand_name);
CREATE INDEX idx_brands_brand_description ON brands(brand_description);
CREATE INDEX idx_brands_created_by ON brands(created_by);
CREATE INDEX idx_brands_created_at ON brands(created_at);
CREATE INDEX idx_brands_updated_by ON brands(updated_by);
CREATE INDEX idx_brands_updated_at ON brands(updated_at);


CREATE TABLE unit_of_measures
(
    uom_id serial NOT NULL  ,
    uom_name character varying(100) ,
    uom_short_name character varying(50) ,
    uom_description character varying(300) ,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_by character varying(50) ,
    created_at bigint,
    updated_by character varying(50) ,
    updated_at bigint,
    CONSTRAINT uom_id_pk PRIMARY KEY (uom_id)
);


CREATE INDEX idx_unit_of_measures_uom_name ON unit_of_measures(uom_name);
CREATE INDEX idx_unit_of_measures_uom_short_name ON unit_of_measures(uom_short_name);
CREATE INDEX idx_unit_of_measures_uom_description ON unit_of_measures(uom_description);
CREATE INDEX idx_unit_of_measures_created_by ON unit_of_measures(created_by);
CREATE INDEX idx_unit_of_measures_created_at ON unit_of_measures(created_at);
CREATE INDEX idx_unit_of_measures_updated_by ON unit_of_measures(updated_by);
CREATE INDEX idx_unit_of_measures_updated_at ON unit_of_measures(updated_at);


CREATE TABLE product_templates
(
    product_template_id character varying(50)  NOT NULL,
    product_name text ,
    product_information text ,
    product_specification text ,
    tax_applicable boolean DEFAULT true,
    product_image character varying(300) ,
    is_product_to_all boolean DEFAULT false,
    hsn_code character varying(50) ,
    brand_id character varying(50) ,
    product_category_id character varying(50) ,
	product_sub_category_id character varying(50) ,
    uom_id integer,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_by character varying(50) ,
    created_at bigint,
    updated_by character varying(50) ,
    updated_at bigint,
    CONSTRAINT product_template_id_pk PRIMARY KEY (product_template_id)
);

CREATE INDEX idx_product_templates_product_name ON product_templates(product_name);
CREATE INDEX idx_product_templates_product_information ON product_templates(product_information);  
CREATE INDEX idx_product_templates_product_specification ON product_templates(product_specification);
CREATE INDEX idx_product_templates_tax_applicable ON product_templates(tax_applicable); 
CREATE INDEX idx_product_templates_is_product_to_all ON product_templates(is_product_to_all);
CREATE INDEX idx_product_templates_hsn_code ON product_templates(hsn_code);
CREATE INDEX idx_product_templates_brand_id ON product_templates(brand_id);
CREATE INDEX idx_product_templates_product_category_id ON product_templates(product_category_id);  
CREATE INDEX idx_product_templates_uom_id ON product_templates(uom_id);
CREATE INDEX idx_product_templates_product_sub_category_id ON product_templates(product_sub_category_id); 
CREATE INDEX idx_product_templates_created_by ON product_templates(created_by);
CREATE INDEX idx_product_templates_created_at ON product_templates(created_at);
CREATE INDEX idx_product_templates_updated_by ON product_templates(updated_by);
CREATE INDEX idx_product_templates_updated_at ON product_templates(updated_at);

CREATE TABLE product_variants
(
    product_variant_id character varying(50)  NOT NULL,
    product_variant_name text ,
    product_variant_description text ,
    product_variant_price double precision DEFAULT 0,
    barcode character varying(100) ,
    weight double precision DEFAULT 0,
    color character varying(50) ,
    product_variant_sku text ,
    product_template_id character varying(50) ,
	product_quantity_type character varying(1)  DEFAULT 'P'::character varying,
    is_non_veg boolean DEFAULT false,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_by character varying(50) ,
    created_at bigint,
    updated_by character varying(50) ,
    updated_at bigint,
    CONSTRAINT product_variant_id_pk PRIMARY KEY (product_variant_id)
);

CREATE INDEX idx_product_variants_product_variant_name ON product_variants(product_variant_name);
CREATE INDEX idx_product_variants_product_variant_description ON product_variants(product_variant_description);  
CREATE INDEX idx_product_variants_product_variant_price ON product_variants(product_variant_price);
CREATE INDEX idx_product_variants_barcode ON product_variants(barcode); 
CREATE INDEX idx_product_variants_weight ON product_variants(weight);
CREATE INDEX idx_product_variants_color ON product_variants(color);
CREATE INDEX idx_product_variants_product_variant_sku ON product_variants(product_variant_sku);
CREATE INDEX idx_product_variants_product_template_id ON product_variants(product_template_id);
CREATE INDEX idx_product_variants_product_quantity_type ON product_variants(product_quantity_type);
CREATE INDEX idx_product_variants_is_non_veg ON product_variants(is_non_veg);  
CREATE INDEX idx_product_variants_created_by ON product_variants(created_by);
CREATE INDEX idx_product_variants_created_at ON product_variants(created_at);
CREATE INDEX idx_product_variants_updated_by ON product_variants(updated_by);
CREATE INDEX idx_product_variants_updated_at ON product_variants(updated_at);

CREATE TABLE product_images
(
    product_image_id character varying(50)  NOT NULL,
    product_image_url character varying(300) ,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    product_variant_id character varying(50) ,
    created_by character varying(50) ,
    created_at bigint,
    updated_by character varying(50) ,
    updated_at bigint,
    CONSTRAINT product_image_id_pk PRIMARY KEY (product_image_id)
);

CREATE INDEX idx_product_images_product_image_url ON product_images(product_image_url);
CREATE INDEX idx_product_images_product_variant_id ON product_images(product_variant_id);  
CREATE INDEX idx_product_images_created_by ON product_images(created_by);
CREATE INDEX idx_product_images_created_at ON product_images(created_at);
CREATE INDEX idx_product_images_updated_by ON product_images(updated_by);
CREATE INDEX idx_product_images_updated_at ON product_images(updated_at);

INSERT INTO unit_of_measures (uom_id,uom_name,uom_short_name,uom_description,is_active,is_deleted,created_by,created_at,updated_by,updated_at)
	VALUES
		(1,'grams','grams','grams',true,false,'1',1444203417000,'1',1444203417000);
		
INSERT INTO unit_of_measures (uom_id,uom_name,uom_short_name,uom_description,is_active,is_deleted,created_by,created_at,updated_by,updated_at)
	VALUES
		(2,'kgs','kgs','kgs',true,false,'1',1444203417000,'1',1444203417000);
		
INSERT INTO unit_of_measures (uom_id,uom_name,uom_short_name,uom_description,is_active,is_deleted,created_by,created_at,updated_by,updated_at)
	VALUES
		(3,'milliliters','milliliters','milliliters',true,false,'1',1444203417000,'1',1444203417000);
		
INSERT INTO unit_of_measures (uom_id,uom_name,uom_short_name,uom_description,is_active,is_deleted,created_by,created_at,updated_by,updated_at)
	VALUES
		(4,'liters','liters','liters',true,false,'1',1444203417000,'1',1444203417000);
		
ALTER SEQUENCE unit_of_measures_uom_id_seq RESTART WITH 5;


INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(28,45,'labelBrand','/manage-brands.do',3,'A',1692038897000,'1',1692038897000,'1');
	


INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(29,45,'labelUom','/manage-uoms.do',4,'A',1692038897000,'1',1692038897000,'1');
	
	
INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(30,45,'labelProductTemplate','/manage-product-templates.do',5,'A',1692038897000,'1',1692038897000,'1');	
	
	

INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(31,45,'labelProductVariants','/manage-product-variants.do',6,'A',1692038897000,'1',1692038897000,'1');	
	
	

INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(32,45,'labelProductImages','/manage-product-images.do',7,'A',1692038897000,'1',1692038897000,'1');	
	
	
INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,url_icon,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(33,38,'labelProductsNew','/manage-vendor-products.do','fas fa-shopping-cart',6,'A',1692038897000,'1',1692038897000,'1');	
	
ALTER TABLE vendor_products ADD COLUMN store_price double precision DEFAULT 0;
ALTER TABLE vendor_products ADD COLUMN user_product_sku character varying(30);
ALTER TABLE vendor_products ADD COLUMN product_template_id character varying(50);
ALTER TABLE vendor_products ADD COLUMN product_variant_id character varying(50);

CREATE INDEX idx_vendor_products_store_price ON vendor_products(store_price);
CREATE INDEX idx_vendor_products_user_product_sku ON vendor_products(user_product_sku);
CREATE INDEX idx_vendor_products_product_template_id ON vendor_products(product_template_id);
CREATE INDEX idx_vendor_products_product_variant_id ON vendor_products(product_variant_id);
