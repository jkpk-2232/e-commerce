--01. Demand supply settings

ALTER TABLE admin_settings ADD demand_vendor_percentage double precision DEFAULT 0;
ALTER TABLE admin_settings ADD supplier_vendor_percentage double precision DEFAULT 0;

--02. Demand supply fare applied vendor id

ALTER TABLE tours ADD vendor_id_fare_applied character varying(50);
ALTER TABLE tours ADD demand_vendor_percentage double precision DEFAULT 0;
ALTER TABLE tours ADD supplier_vendor_percentage double precision DEFAULT 0;
ALTER TABLE tours ADD demand_vendor_amount double precision DEFAULT 0;
ALTER TABLE tours ADD supplier_vendor_amount double precision DEFAULT 0;
ALTER TABLE tours ADD amount_for_demand_supplier double precision DEFAULT 0;

--03. Take ride

ALTER TABLE tours ADD is_take_ride BOOLEAN DEFAULT FALSE;
ALTER TABLE tours ADD is_tour_take_ride BOOLEAN DEFAULT FALSE;
ALTER TABLE tours ADD marked_take_ride_user_id character varying(50);

INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(35,'Take Ride','A',35,1444203417000,'1',1444203417000,'1');
		
INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(35,35,'Take Ride','/manage-take-ride.do',-1,'A',1444203417000,'1',1444203417000,'1','glyphicon glyphicon-home');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',35,'A',1444203417000,'1',1444203417000,'1');
