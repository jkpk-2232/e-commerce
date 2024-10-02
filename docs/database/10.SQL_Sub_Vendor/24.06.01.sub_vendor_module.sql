-- Manage sub vendors entry

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(46,'labelSubVendors','A',22,1706021186000,'1',1706021186000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(46,46,'labelSubVendors','/manage-sub-vendors.do',-1,'A',1706021186000,'1',1706021186000,'1','fas fa-child');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',46,'A',1706021186000,'1',1706021186000,'1');

-- New role for sub vendor

INSERT INTO roles 
(
	role_id, role, created_by, created_at, updated_by, updated_at
) 
VALUES 
('8','sub vendor','1',1706556856000,'1',1706556856000);

-- Sub vendor and store mapping

CREATE TABLE vendor_store_sub_vendors
(
  vendor_store_sub_vendor_id character varying(32) NOT NULL,
  vendor_store_id character varying(32),
  vendor_id character varying(32),
  sub_vendor_id character varying(32), 
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT vendor_store_sub_vendor_id_pk PRIMARY KEY (vendor_store_sub_vendor_id)
);

CREATE INDEX idx_vendor_store_sub_vendors_vendor_store_id ON vendor_store_sub_vendors(vendor_store_id);
CREATE INDEX idx_vendor_store_sub_vendors_vendor_id ON vendor_store_sub_vendors(vendor_id);
CREATE INDEX idx_vendor_store_sub_vendors_sub_vendor_id ON vendor_store_sub_vendors(sub_vendor_id);
CREATE INDEX idx_vendor_store_sub_vendors_created_by ON vendor_store_sub_vendors(created_by);
CREATE INDEX idx_vendor_store_sub_vendors_created_at ON vendor_store_sub_vendors(created_at);
CREATE INDEX idx_vendor_store_sub_vendors_updated_by ON vendor_store_sub_vendors(updated_by);
CREATE INDEX idx_vendor_store_sub_vendors_updated_at ON vendor_store_sub_vendors(updated_at);