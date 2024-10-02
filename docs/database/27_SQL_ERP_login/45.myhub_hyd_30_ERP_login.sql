INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(51,'labelERPUsers','A',7,1721126001000,'1',1721126001000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id, record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(51,51,'labelERPUsers','/manage-erp-users.do',-1,'A',1721126001000,'1',1721126001000,'1','fas fa-user');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',51,'A',1721126001000,'1',1721126001000,'1');
	
	
ALTER TABLE vendor_stores ADD brand_id character varying(50);

CREATE INDEX idx_vendor_stores_brand_id ON vendor_stores(brand_id);

ALTER TABLE brands ADD is_public boolean DEFAULT false;

CREATE INDEX idx_brands_is_public ON brands(is_public);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(52,'labelERPEmployees','A',7,1723188553000,'1',1723188553000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id, record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(52,52,'labelERPEmployees','/manage-erp-employees.do',-1,'A',1723188553000,'1',1723188553000,'1','fas fa-child');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',52,'A',1723188553000,'1',1723188553000,'1');
	
	
INSERT INTO roles (role_id, role, created_by, created_at, updated_by, updated_at)
values (13,'erp employee','1',1723455303000,'1',1723455303000);