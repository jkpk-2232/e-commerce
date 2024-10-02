-- New service type for Appointments
INSERT INTO service_types
(service_type_id, service_type_name, service_type_description, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES 
('4', 'Appointment', 'Appointment', true, false, '1', 1710050521000, '1', 1710050521000);

-- Manage appointment entry
INSERT INTO url_groups 
	(url_group_id, url_group_name, record_status, menu_priority,created_at, created_by, updated_at, updated_by) 
VALUES 
	(47,'labelAppointments','A',22,1710050521000,'1',1710050521000,'1');    

INSERT INTO urls 
	(url_id, url_group_id, url_title, url, parent_url_id,record_status, created_at, created_by, updated_at, updated_by, url_icon) 
VALUES 
	(47,47,'labelAppointments','/manage-appointments.do',-1,'A',1710050521000,'1',1710050521000,'1','fas fa-clock');

INSERT INTO url_accesses 
	(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
	('1',47,'A',1710050521000,'1',1710050521000,'1');

UPDATE urls SET is_sub_menu_option=true WHERE url_id=47;
	
-- Rearrange menu priority
UPDATE url_groups SET menu_priority=39 WHERE url_group_id=47;

-- Sub menus
INSERT INTO url_sub_categories
(url_sub_category_id, url_id, url_title, url, url_icon, menu_priority, record_status, created_at, created_by, updated_at, updated_by)
VALUES 
(34, 47, 'labelAppointmentsNew', '/manage-appointments-new.do', 'fas fa-clock', 2, 'A', 1710050521000, '1', 1710050521000, '1'),
(35, 47, 'labelAppointmentsActive', '/manage-appointments-active.do', 'fas fa-clock', 3, 'A', 1710050521000, '1', 1710050521000, '1'),
(36, 47, 'labelAppointmentsAllOthers', '/manage-appointments-all-others.do', 'fas fa-clock', 4, 'A', 1710050521000, '1', 1710050521000, '1'),
(37, 20, 'labelAppointmentSettings', '/manage-appointment-settings.do', 'fas fa-cogs', 12, 'A', 1710050521000, '1', 1710050521000, '1');

-- Appointment Settings
CREATE TABLE appointment_settings
(
  appointment_setting_id character varying(32) NOT NULL,
  service_id character varying(32),
  min_booking_time bigint,
  max_booking_time bigint,
  free_cancellation_time_mins int DEFAULT 0,
  cron_job_expire_time_mins int DEFAULT 0,
  created_by character varying(32),
  created_at bigint,
  updated_by character varying(32),
  updated_at bigint,
  CONSTRAINT appointment_setting_id_pk PRIMARY KEY (appointment_setting_id)
);

CREATE INDEX idx_appointment_settings_service_id ON appointment_settings(service_id);
CREATE INDEX idx_appointment_settings_created_by ON appointment_settings(created_by);
CREATE INDEX idx_appointment_settings_created_at ON appointment_settings(created_at);
CREATE INDEX idx_appointment_settings_updated_by ON appointment_settings(updated_by);
CREATE INDEX idx_appointment_settings_updated_at ON appointment_settings(updated_at);

CREATE TABLE appointments
(
  appointment_id character varying(32) NOT NULL,
  appointment_short_id serial NOT NULL,
  appointment_user_id character varying(32),
  appointment_received_against_vendor_id character varying(32),
  appointment_vendor_store_id character varying(32),
  appointment_creation_time bigint,
  appointment_time bigint,
  appointment_time_string character varying(30),
  appointment_status character varying(32),
  appointment_promo_code_id character varying(32),
  appointment_promo_code_discount double precision DEFAULT 0,
  appointment_total double precision DEFAULT 0,
  appointment_charges double precision DEFAULT 0,
  appointment_number_of_items int,
  payment_mode character varying(1),
  payment_status character varying(10),
  service_type_id character varying(5),
  multicity_country_id character varying(32), 
  multicity_city_region_id character varying(32),
  end_otp character varying(4) DEFAULT '1234',
  payment_token character varying(32),
  payment_token_generated_time bigint DEFAULT 0,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT appointment_id_pk PRIMARY KEY (appointment_id)
);

CREATE INDEX idx_appointments_appointment_short_id ON appointments(appointment_short_id);
CREATE INDEX idx_appointments_appointment_user_id ON appointments(appointment_user_id);
CREATE INDEX idx_appointments_appointment_received_against_vendor_id ON appointments(appointment_received_against_vendor_id);
CREATE INDEX idx_appointments_appointment_vendor_store_id ON appointments(appointment_vendor_store_id);
CREATE INDEX idx_appointments_appointment_creation_time ON appointments(appointment_creation_time);
CREATE INDEX idx_appointments_appointment_time ON appointments(appointment_time);
CREATE INDEX idx_appointments_appointment_status ON appointments(appointment_status);
CREATE INDEX idx_appointments_appointment_promo_code_id ON appointments(appointment_promo_code_id);
CREATE INDEX idx_appointments_service_type_id ON appointments(service_type_id);
CREATE INDEX idx_appointments_multicity_city_region_id ON appointments(multicity_city_region_id);
CREATE INDEX idx_appointments_multicity_country_id ON appointments(multicity_country_id);
CREATE INDEX idx_appointments_payment_token ON appointments(payment_token);
CREATE INDEX idx_appointments_created_by ON appointments(created_by);
CREATE INDEX idx_appointments_created_at ON appointments(created_at);
CREATE INDEX idx_appointments_updated_by ON appointments(updated_by);
CREATE INDEX idx_appointments_updated_at ON appointments(updated_at);