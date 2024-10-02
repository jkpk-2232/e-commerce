--01. Add a new table to have vendor wise settings-----------------------------

CREATE TABLE vendor_admin_settings
(
  vendor_admin_settings_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  about_us character varying(500000),
  privacy_policy character varying(500000),
  terms_conditions character varying(500000),
  refund_policy character varying(500000),
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT vendor_admin_settings_id_pk PRIMARY KEY (vendor_admin_settings_id)
);

ALTER TABLE admin_company_contact RENAME COLUMN user_id TO vendor_id;
