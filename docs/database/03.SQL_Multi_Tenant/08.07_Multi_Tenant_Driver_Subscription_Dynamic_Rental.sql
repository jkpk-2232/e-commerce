--01. Table to have subscription packages list

CREATE TABLE subscription_packages
(
  subscription_package_id character varying(50) NOT NULL,
  package_name character varying(50),
  duration_days bigint,
  price double precision,
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  is_permanent_deleted boolean DEFAULT false,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT subscription_package_id_pk PRIMARY KEY (subscription_package_id)
);

CREATE TABLE driver_subscription_package_history
(
  driver_subscription_package_history_id character varying(50) NOT NULL,
  driver_id character varying(50),
  vendor_id character varying(50),
  subscription_package_id character varying(50),
  payment_mode character varying(1),
  package_name character varying(50),
  duration_days bigint,
  price double precision,
  package_start_time bigint,
  package_end_time bigint,
  is_current_package boolean DEFAULT false,
  is_active boolean DEFAULT true,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_subscription_package_history_id_pk PRIMARY KEY (driver_subscription_package_history_id)
);

--02. Flag to control subscription is booking flow.

ALTER TABLE users ADD is_vendor_driver_subscription_applied_in_booking_flow BOOLEAN DEFAULT TRUE;
--For other users set the flag as false
UPDATE users SET is_vendor_driver_subscription_applied_in_booking_flow = FALSE WHERE role_id !='7';

--03. Flag for driver subscription reports export

ALTER TABLE export_accesses ADD is_driver_subscription_export BOOLEAN DEFAULT TRUE;

--04. Driver subscription id in tours table

ALTER TABLE tours ADD subscription_package_id character varying(50);