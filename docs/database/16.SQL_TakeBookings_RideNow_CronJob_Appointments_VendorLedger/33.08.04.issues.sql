CREATE TABLE driver_subscription_extension_logs
(
  driver_subscription_extension_log_id character varying(32) NOT NULL,
  status character varying(50), 
  multicity_city_region_id character varying(32),
  total_drivers int,
  total_completed_drivers int,
  total_failed_drivers int,
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT driver_subscription_extension_log_id_pk PRIMARY KEY (driver_subscription_extension_log_id)
);

CREATE INDEX idx_driver_subscription_extension_logs_multicity_city_region_id ON driver_subscription_extension_logs(multicity_city_region_id);
CREATE INDEX idx_driver_subscription_extension_logs_status ON driver_subscription_extension_logs(status);
CREATE INDEX idx_driver_subscription_extension_logs_created_by ON driver_subscription_extension_logs(created_by);
CREATE INDEX idx_driver_subscription_extension_logs_created_at ON driver_subscription_extension_logs(created_at);
CREATE INDEX idx_driver_subscription_extension_logs_updated_by ON driver_subscription_extension_logs(updated_by);
CREATE INDEX idx_driver_subscription_extension_logs_updated_at ON driver_subscription_extension_logs(updated_at);

ALTER TABLE driver_transaction_history ADD is_driver_subscription_extension BOOLEAN DEFAULT FALSE;
ALTER TABLE driver_transaction_history ADD driver_subscription_extension_log_id character varying(32);