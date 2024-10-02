UPDATE vendor_app_versions SET is_madatory = FALSE;

INSERT INTO vendor_app_versions
(
	vendor_app_version_id, version, release_note, app_link, 
	device_type, is_madatory, remove_support_from, release_date, 
	record_status, created_by, created_datetime, updated_by, updated_datetime
)
VALUES 
(
	'4', '1.2', 'Order notification NOR', 'http://www.google.com',
	'android', TRUE, 7717090395000, 1717090395000, 
	'A', '1', 1717090395000, '1', 1717090395000
);