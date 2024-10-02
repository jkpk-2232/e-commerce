ALTER TABLE vendor_stores ADD rack_category_id character varying(50) ;

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(48,'labelWarehouse','A',40,1712555891000,'1',1712555891000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(48,48,'labelWarehouse','/manage-rack-categories.do',-1,'A',1712555891000,'1',1712555891000,'1','fas fa-warehouse');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',48,'A',1712555891000,'1',1712555891000,'1');

UPDATE urls SET is_sub_menu_option=true WHERE url_id=48;

INSERT INTO url_sub_categories
(url_sub_category_id, url_id, url_title, url, menu_priority, record_status, created_at, created_by, updated_at, updated_by)
VALUES 
(40, 48, 'labelRackCategory', '/manage-rack-categories.do', 1, 'A',1712555891000, '1',1712555891000, '1');

INSERT INTO url_sub_categories
(url_sub_category_id, url_id, url_title, url, menu_priority, record_status, created_at, created_by, updated_at, updated_by)
VALUES 
(41, 48, 'labelSlotStatus', '/manage-rack-slot-booking.do', 1, 'A',1712837332000, '1',1712837332000, '1');