UPDATE driver_app_versions SET is_madatory = FALSE;
UPDATE versions SET is_madatory = FALSE;
UPDATE vendor_app_versions SET is_madatory = FALSE;

INSERT INTO driver_app_versions
(	
	driver_app_version_id, version, release_note, app_link, device_type, is_madatory, 
	remove_support_from, release_date, record_status, created_by, 
	created_datetime, updated_by, updated_datetime
)
VALUES 
('3','1.1','Driver OTP','http://www.google.com','android',TRUE,7704828014000,1704828014000,'A','1',1704828014000,'1',1704828014000);

INSERT INTO versions
(	
	version_id, version, release_note, app_link, device_type, is_madatory, 
	remove_support_from, release_date, record_status, created_by, 
	created_datetime, updated_by, updated_datetime
)
VALUES 
('3','1.1','CCavenue integration, delivery and otp','http://www.google.com','android',TRUE,7704828014000,1704828014000,'A','1',1704828014000,'1',1704828014000);

INSERT INTO vendor_app_versions
(	
	vendor_app_version_id, version, release_note, app_link, device_type, is_madatory, 
	remove_support_from, release_date, record_status, created_by, 
	created_datetime, updated_by, updated_datetime
)
VALUES 
('3','1.1','Self delivery and otp','http://www.google.com','android',TRUE,7704828014000,1704828014000,'A','1',1704828014000,'1',1704828014000);