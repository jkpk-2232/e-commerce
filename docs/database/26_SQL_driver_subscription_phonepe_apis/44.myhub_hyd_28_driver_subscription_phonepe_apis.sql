CREATE TABLE payment_orders (
    payment_order_id integer NOT NULL ,
    first_name character varying(50),
    last_name character varying(50),
    mobile_number character varying(15),
	order_type character varying(50),
	amount double precision DEFAULT 0,
    order_id character varying(50),
    user_id character varying(50),
    payment_status character varying(50),
    payment_type_id character varying(50),
    order_ref_id character varying(50),
    created_by character varying(50),
    created_at bigint,
    updated_by character varying(50),
    updated_at bigint,
    CONSTRAINT payment_order_id_pk PRIMARY KEY (payment_order_id)
);

CREATE SEQUENCE IF NOT EXISTS public.payment_orders_payment_order_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1
    OWNED BY payment_orders.payment_order_id;
    
ALTER TABLE payment_orders
    ALTER COLUMN payment_order_id SET DEFAULT nextval('payment_orders_payment_order_id_seq');
    
ALTER TABLE phonepe_payments add payment_order_id integer;
    
INSERT INTO payment_type (payment_type_id,payment_type,record_status,created_by,created_at,updated_by,updated_at)
values ('4','Phonepe','A','1',1719559270000,'1',1719559270000);


ALTER TABLE export_accesses ADD is_phonepe_log_report_export boolean DEFAULT false;

ALTER TABLE export_accesses ADD is_warehouse_export boolean DEFAULT false;


INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(50,'labelWarehouseUsers','A',6,1719918073000,'1',1719918073000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id, record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(50,50,'labelWarehouseUsers','/manage-warehouse-users.do',-1,'A',1719918073000,'1',1719918073000,'1','fas fa-user');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',50,'A',1719918073000,'1',1719918073000,'1');


CREATE INDEX idx_payment_orders_order_id ON payment_orders(order_id);
CREATE INDEX idx_payment_orders_payment_type_id ON payment_orders(payment_type_id);
CREATE INDEX idx_payment_orders_order_ref_id ON payment_orders(order_ref_id);
CREATE INDEX idx_payment_orders_payment_order_id ON payment_orders(payment_order_id);
CREATE INDEX idx_payment_orders_first_name ON payment_orders(first_name);
CREATE INDEX idx_payment_orders_last_name ON payment_orders(last_name);
CREATE INDEX idx_payment_orders_payment_status ON payment_orders(payment_status);
CREATE INDEX idx_payment_orders_mobile_number ON payment_orders(mobile_number);
CREATE INDEX idx_payment_orders_user_id ON payment_orders(user_id);
CREATE INDEX idx_payment_orders_order_type ON payment_orders(order_type);
CREATE INDEX idx_payment_orders_created_at ON payment_orders(created_at);
CREATE INDEX idx_payment_orders_updated_at ON payment_orders(updated_at);

ALTER TABLE user_info ADD dob bigint;

CREATE INDEX idx_user_info_dob ON user_info(dob);