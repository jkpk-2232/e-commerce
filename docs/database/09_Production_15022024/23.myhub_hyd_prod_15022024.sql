UPDATE versions SET is_madatory = FALSE;

INSERT INTO versions
(	
	version_id, version, release_note, app_link, device_type, is_madatory, 
	remove_support_from, release_date, record_status, created_by, 
	created_datetime, updated_by, updated_datetime
)
VALUES 
('4','1.2','Place api fixes','http://www.google.com','android',TRUE,7708111010000,1708111010000,'A','1',1708111010000,'1',1708111010000);