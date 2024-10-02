ALTER TABLE admin_settings ADD driver_processing_via integer DEFAULT 1;
ALTER TABLE admin_settings ADD cron_job_trip_expiry_after_x_mins integer DEFAULT 5;

Set cron job -> /api/cron-job-process-assign-drivers/tours.json

ALTER TABLE tours ADD driver_processing_via_cron_time bigint DEFAULT 0;