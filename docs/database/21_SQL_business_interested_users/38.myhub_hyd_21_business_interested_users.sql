CREATE TABLE business_interested_users(
	
	business_interested_user_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
	name character varying(250),
	email character varying(100),
	phone_no_code character varying(10),
	phone_no  character varying(50),
	city	character varying(100),
	role_id character varying(10),
	vechicle_type character varying(50),
	store_name character varying(50),
	no_of_stores integer,
	business_category character varying(100),
	brand_name character varying(50),
	no_of_outlets integer,
	description character varying(300),
	created_at bigint,
    created_by character varying(32) COLLATE pg_catalog."default",
    updated_at bigint,
    updated_by character varying(32) COLLATE pg_catalog."default",
	CONSTRAINT business_interested_user_id_pk PRIMARY KEY (business_interested_user_id)
);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(49,'labelBusinessUsers','A',6,1715300326000,'1',1715300326000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id, record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(49,49,'labelBusinessUsers','/manage-business-interested-users.do',-1,'A',1715300326000,'1',1715300326000,'1','fas fa-user');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',49,'A',1715300326000,'1',1715300326000,'1');
	


INSERT INTO roles 
		(
			role_id, role, created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('10','transport','1',1715316403000,'1',1715316403000), 
		('11','retail','1',1715316403000,'1',1715316403000), 
		('12','erp','1',1715316403000,'1',1715316403000);