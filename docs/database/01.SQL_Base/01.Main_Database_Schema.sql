
CREATE EXTENSION postgis; 
CREATE EXTENSION postgis_topology;

--1-------------------------------------------------------------------------------------

CREATE TABLE roles
(
  role_id character varying(50) NOT NULL,
  role character varying(100),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT role_id_pk PRIMARY KEY (role_id)
);

--2-------------------------------------------------------------------------------------

CREATE TABLE users
(
  user_id character varying(50) NOT NULL,
  email character varying(50),
  password character varying(50),
  photo_url character varying(200),
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  role_id character varying(50),
  is_notification_send_status boolean NOT NULL DEFAULT true,
  is_on_duty boolean DEFAULT false,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_id_pk PRIMARY KEY (user_id),
  CONSTRAINT role_id_fk FOREIGN KEY (role_id)
      REFERENCES roles (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--3-------------------------------------------------------------------------------------

CREATE TABLE user_info
(
  user_info_id character varying(50) NOT NULL,
  first_name character varying(50),
  last_name character varying(50),
  phone_no character varying(50),
  phone_no_code character varying(50) DEFAULT 0,
  driving_license character varying(250),
  joining_date bigint,
  mail_address_line_1 character varying(250),
  mail_address_line_2 character varying(250),
  mail_zip_code character varying(50),
  bill_address_line_1 character varying(250),
  bill_address_line_2 character varying(250),
  bill_zip_code character varying(50),
  gender character varying(50),
  user_id character varying(50) NOT NULL,
  card_type character varying(50),
  account_number character varying(50),
  mail_country_id character varying(50),
  mail_state_id character varying(50),
  mail_city_id character varying(50),
  bill_country_id character varying(50),
  bill_state_id character varying(50),
  bill_city_id character varying(50),
  tip bigint DEFAULT 0,
  application_status character varying(50),
  driving_license_photo character varying(250),
  verification_code character varying(10),
  is_verified boolean DEFAULT false,
  is_same_as_mailing boolean DEFAULT false,
  company_name character varying(50),
  company_address character varying(50),
  company_driver bigint DEFAULT 0,
  credit double precision DEFAULT 0,
  referral_code character varying(50),
  is_first_time boolean DEFAULT TRUE,
  card_available boolean DEFAULT false,
  ride_later_visited_time bigint DEFAULT 0,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_info_id_pk PRIMARY KEY (user_info_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--4-------------------------------------------------------------------------------------

CREATE TABLE user_documents
(
  user_document_id character varying(50) NOT NULL,
  document_id character varying(50) NOT NULL,
  exp_date bigint,
  is_aproved boolean,
  is_deleted boolean,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_document_id_pk PRIMARY KEY (user_document_id)
);

--5-------------------------------------------------------------------------------------

CREATE TABLE user_payment_profile
(
  user_payment_profile_id character varying(32) NOT NULL,
  user_id character varying(32) NOT NULL,
  user_authorize_profile_id bigint NOT NULL,
  user_authorize_payment_profile_id bigint NOT NULL,
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint NOT NULL,
  created_by character varying(32) NOT NULL,
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT user_payment_profile_pkey PRIMARY KEY (user_payment_profile_id)
);

--6-------------------------------------------------------------------------------------

CREATE TABLE user_role
(
  user_role_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  role_id character varying(50),
  parent_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_role_id_pk PRIMARY KEY (user_role_id),
  CONSTRAINT parent_id_fk FOREIGN KEY (parent_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT role_id_fk FOREIGN KEY (role_id)
      REFERENCES roles (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--7-------------------------------------------------------------------------------------

CREATE TABLE user_vendors
(
  user_vendor_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  vendor_id character varying(50) NOT NULL,
  is_deleted boolean DEFAULT false,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_vendor_id_pk PRIMARY KEY (user_vendor_id)
);

--8-------------------------------------------------------------------------------------

CREATE TABLE vendor_bills
(
  vendor_bill_id character varying(50) NOT NULL,
  bill_id character varying(50) NOT NULL,
  amount double precision,
  vendor_id character varying(50) NOT NULL,
  is_paid boolean DEFAULT false,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_bill_id_pk PRIMARY KEY (vendor_bill_id)
);

--9-------------------------------------------------------------------------------------

CREATE TABLE sessions
(
  session_id character varying(50) NOT NULL,
  created_at bigint,
  accessed_at bigint,
  CONSTRAINT session_id PRIMARY KEY (session_id)
);

--10------------------------------------------------------------------------------------

CREATE TABLE session_attributes
(
  session_attribute_id serial NOT NULL,
  session_id character varying(50) NOT NULL,
  attribute character varying(50),
  attribute_value character varying NOT NULL,
  CONSTRAINT session_attribute_id_pk PRIMARY KEY (session_attribute_id),
  CONSTRAINT session_id_fk FOREIGN KEY (session_id)
      REFERENCES sessions (session_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--11------------------------------------------------------------------------------------

CREATE TABLE login_trails
(
  login_trail_id character varying(50) NOT NULL,
  actor_id character varying(50),
  actor_role character varying(50),
  logged_in_at bigint,
  logged_out_at bigint,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  session_id character varying(50) NOT NULL,
  CONSTRAINT login_trail_id_pk PRIMARY KEY (login_trail_id),
  CONSTRAINT actor_id_fk FOREIGN KEY (actor_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT login_trails_session_id_fk FOREIGN KEY (session_id)
      REFERENCES sessions (session_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--12------------------------------------------------------------------------------------

CREATE TABLE remembered_login_sessions
(
  remembered_login_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  created_at bigint NOT NULL,
  CONSTRAINT remembered_login_sessions_pkey PRIMARY KEY (remembered_login_id),
  CONSTRAINT remembered_login_sessions_user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--13------------------------------------------------------------------------------------

CREATE TABLE chat_servers
(
  chat_server_id integer NOT NULL,
  server_ip character varying(50) NOT NULL,
  server_port integer NOT NULL,
  pass_key character varying(32) NOT NULL,
  CONSTRAINT chat_servers_pkey PRIMARY KEY (chat_server_id)
);

--14------------------------------------------------------------------------------------

CREATE TABLE chat_server_users
(
  chat_server_user_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  client_id character varying(300) NOT NULL,
  chat_server_id integer NOT NULL,
  connected_at bigint NOT NULL,
  CONSTRAINT chat_server_users_pkey PRIMARY KEY (chat_server_user_id),
  CONSTRAINT chat_server_users_server_id_fk FOREIGN KEY (chat_server_id)
      REFERENCES chat_servers (chat_server_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX chat_server_users_client_id
	ON chat_server_users
	USING btree
	(client_id COLLATE pg_catalog."default");

--15------------------------------------------------------------------------------------

CREATE TABLE admin_tip
(
  admin_tip_id character varying(50) NOT NULL,
  admin_id character varying(50),
  tip bigint DEFAULT 20,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT admin_tip_id_pk PRIMARY KEY (admin_tip_id)
);
	
--16------------------------------------------------------------------------------------

CREATE TABLE announcements
(
  announcement_id character varying(50) NOT NULL,
  message character varying(250),
  is_deleted boolean DEFAULT false,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT announcement_id_pk PRIMARY KEY (announcement_id)
);

--17------------------------------------------------------------------------------------

CREATE TABLE api_sessions
(
  api_session_id character varying(50) NOT NULL,
  user_id character varying(50),
  session_key character varying(100),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT api_session_id_pk PRIMARY KEY (api_session_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--18------------------------------------------------------------------------------------

CREATE TABLE apns_devices
(
  apns_device_id character varying(50) NOT NULL,
  user_id character varying(50),
  device_type character varying(50),
  app_name character varying(250),
  app_version character varying(250),
  device_uid character varying(250),
  device_token character varying(250),
  device_name character varying(250),
  device_model character varying(250),
  device_version character varying(250),
  push_badge boolean,
  push_alert boolean,
  push_sound boolean,
  development boolean,
  timezone character varying(50) DEFAULT 'GMT',
  is_deleted boolean DEFAULT false,
  created_at bigint,
  updated_at bigint,
  api_session_key character varying(50),
  CONSTRAINT apns_device_id_pk PRIMARY KEY (apns_device_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--19------------------------------------------------------------------------------------

CREATE TABLE apns_notification_messages
(
  apns_notification_message_id character varying(50) NOT NULL,
  from_user_id character varying(50),
  to_user_id character varying(50),
  message character varying(250),
  message_type character varying(50),
  is_deleted boolean DEFAULT false,
  created_at bigint,
  updated_at bigint,
  CONSTRAINT apns_notification_message_id_pk PRIMARY KEY (apns_notification_message_id),
  CONSTRAINT from_user_id_fk FOREIGN KEY (from_user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT to_user_id_fk FOREIGN KEY (to_user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--20------------------------------------------------------------------------------------

CREATE TABLE business_operator
(
  business_operator_id character varying(50) NOT NULL,
  business_owner_id character varying(50),
  operator_id character varying(50),
  is_deleted boolean DEFAULT false,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT business_operator_id_fk PRIMARY KEY (business_operator_id),
  CONSTRAINT business_owner_id_fk FOREIGN KEY (business_owner_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT operator_id_fk FOREIGN KEY (operator_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--21------------------------------------------------------------------------------------

CREATE TABLE cars
(
  car_id character varying(50) NOT NULL,
  car_model character varying(250),
  car_color character varying(250),
  car_plate_no character varying(250),
  car_year bigint,
  no_of_passenger bigint,
  driver_id character varying(50),
  car_type_id character varying(50),
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  owner character varying(50),
  make character varying(250),
  front_img_url character varying(250),
  back_img_url character varying(250),
  car_title character varying(250),
  registration_photo_url character varying(250),
  insurance_photo_url character varying(250),
  inspection_report_photo_url character varying(250),
  vehicle_commercial_licence_photo_url character varying(250),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_id_pk PRIMARY KEY (car_id)
);

--22------------------------------------------------------------------------------------

CREATE TABLE car_type
(
  car_type_id character varying(50) NOT NULL,
  car_type character varying(50) NOT NULL,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_type_id_pk PRIMARY KEY (car_type_id)
);

--23------------------------------------------------------------------------------------

CREATE TABLE car_fare
(
  car_fare_id character varying(50) NOT NULL,
  car_type_id character varying(50),
  initial_fare double precision,
  per_km_fare double precision,
  per_minute_fare double precision,
  booking_fees double precision,
  minimum_fare double precision,
  discount double precision,
  driver_payable_percentage double precision,
  free_distance double precision,
  is_fixed_fare boolean,
  is_day_fare boolean,
  multicity_city_region_id character varying(50),
  multicity_country_id character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT care_fare_id_pk PRIMARY KEY (car_fare_id)
);

--24------------------------------------------------------------------------------------

CREATE TABLE car_live_gps
(
  car_gps_id character varying(50) NOT NULL,
  car_id character varying(50) NOT NULL,
  tour_id character varying(50) NOT NULL,
  latitude character varying(50),
  longitude character varying(50),
  car_location geography(Point,4326),
  is_deleted boolean,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_gps_id_pk PRIMARY KEY (car_gps_id),
  CONSTRAINT car_id_fk FOREIGN KEY (car_id)
      REFERENCES cars (car_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--25------------------------------------------------------------------------------------

CREATE TABLE cancellation_charges
(
  cancellation_charges_id character varying(50) NOT NULL,
  admin_id character varying(50),
  charge double precision,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT cancellation_charges_id_pk PRIMARY KEY (cancellation_charges_id)
);

--26------------------------------------------------------------------------------------

CREATE TABLE credit_card_details
(
  credit_card_details_id character varying(50) NOT NULL,
  user_id character varying(50),
  card_number character varying(200),
  token character varying(200),
  auth_code character varying(200),
  card_type character varying(50),
  braintree_profile_id character varying(250),
  nonce_token character varying(250),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT credit_card_details_id_pk PRIMARY KEY (credit_card_details_id)
);

--27------------------------------------------------------------------------------------

CREATE TABLE discounts
(
  car_type character varying(50) NOT NULL,
  title character varying(250),
  start_date bigint,
  end_date bigint,
  percentage bigint,
  is_active boolean,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_type_pk PRIMARY KEY (car_type)
);

--28------------------------------------------------------------------------------------

CREATE TABLE driver_bank_details
(
  driver_bank_details_id character varying(50) NOT NULL,
  user_id character varying(50),
  bank_name character varying(50),
  account_number character varying(50),
  account_name character varying(50),
  routing_number character varying(50),
  type character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_bank_details_id_pk PRIMARY KEY (driver_bank_details_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--29------------------------------------------------------------------------------------

CREATE TABLE driver_gps
(
  driver_gps_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  tour_id character varying(50) NOT NULL,
  latitude character varying(50),
  longitude character varying(50),
  car_location geography(Point,4326),
  is_deleted boolean,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_gps_pk PRIMARY KEY (driver_gps_id)
);

--30------------------------------------------------------------------------------------

CREATE TABLE driver_payable_percentage
(
  driver_payable_percentage_id character varying(50) NOT NULL,
  user_id character varying(50),
  percentage double precision,
  own_passenger_percentage double precision,
  preferred_customer_percentage double precision,
  referred_driver_percentage double precision,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT driver_payable_percentage_id_pk PRIMARY KEY (driver_payable_percentage_id)
);

--31------------------------------------------------------------------------------------

CREATE TABLE driver_tour_requests
(
  driver_tour_request_id character varying(50) NOT NULL,
  tour_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  status character varying(50),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_job_id_pk PRIMARY KEY (driver_tour_request_id)
);

--32------------------------------------------------------------------------------------

CREATE TABLE driver_tour_status
(
  driver_tour_status_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  status character varying(50) NOT NULL,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_tour_status_id_pk PRIMARY KEY (driver_tour_status_id)
);

--33------------------------------------------------------------------------------------

CREATE TABLE driver_trip_ratings
(
  driver_trip_ratings_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  trip_id character varying(50) NOT NULL,
  passenger_id character varying(50) NOT NULL,
  note character varying(200),
  rate double precision,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT driver_trip_ratings_id_pk PRIMARY KEY (driver_trip_ratings_id),
  CONSTRAINT driver_id_fk FOREIGN KEY (driver_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT passenger_id_fk FOREIGN KEY (passenger_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--34------------------------------------------------------------------------------------

CREATE TABLE driving_license_info
(
  driving_license_info_id character varying(50) NOT NULL,
  f_name character varying(50),
  m_name character varying(50),
  l_name character varying(50),
  driver_license_card_number character varying(50),
  state_id bigint,
  dob bigint,
  social_security_number character varying(50),
  insurance_effective_date bigint,
  insurance_expiration_date bigint,
  insurance_photo_url character varying(250),
  driving_license_photo_url character varying(250),
  user_id character varying(50),
  driving_license_back_photo_url character varying(250),
  birth_accreditation_passport_photo_url character varying(250),
  social_security_number_photo_url character varying(250),
  criminal_history_photo_url character varying(250),
  au_business_no character varying(50),
  licence_expiration_date bigint,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT driving_license_info_id_pk PRIMARY KEY (driving_license_info_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--35------------------------------------------------------------------------------------

CREATE TABLE forgot_password_sessions
(
  forgot_password_sessions_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  reset_requested_at bigint NOT NULL,
  reset_at bigint,
  requested_at bigint,
  expires_at bigint,
  CONSTRAINT forgot_password_sessions_id_pk PRIMARY KEY (forgot_password_sessions_id),
  CONSTRAINT user_id FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--36------------------------------------------------------------------------------------

CREATE TABLE invoices
(
  invoice_id character varying(50) NOT NULL,
  tour_id character varying(50),
  is_refunded boolean DEFAULT false,
  initial_fare double precision,
  per_km_fare double precision,
  per_minute_fare double precision,
  booking_fees double precision,
  minimum_fare double precision,
  discount double precision,
  distance_fare double precision,
  time_fare double precision,
  base_fare double precision,
  sub_total double precision,
  total double precision,
  distance double precision,
  charges double precision,
  percentage double precision,
  driver_amount double precision,
  duration double precision,
  avg_speed double precision,
  fine double precision,
  transaction_id character varying(50),
  refund_amount double precision DEFAULT 0.0,
  payment_mode character varying(50),
  cash_collected double precision DEFAULT 0.0,
  cash_to_be_collected double precision DEFAULT 0.0,
  payment_status character varying(50),
  is_cash_not_received boolean DEFAULT false,
  promo_code_id character varying(50),
  is_promo_code_applied boolean DEFAULT false,
  promo_discount double precision,
  used_credits double precision DEFAULT 0,
  waiting_time double precision,
  final_amount_collected double precision,
  arrived_waiting_time double precision,
  toll_amount double precision ,
  admin_settlement_amount double precision DEFAULT 0.0,
  static_map_img_url character varying(200),
  is_surge_price_applied boolean DEFAULT false,
  surge_price_id character varying(50),
  surge_price double precision DEFAULT 0,
  total_with_surge double precision DEFAULT 0,
  surge_fare double precision DEFAULT 0,
  is_payment_paid boolean DEFAULT true,
  total_tax_amount double precision DEFAULT 0,
  arrived_waiting_time_fare double precision DEFAULT 0,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT invoice_id_pk PRIMARY KEY (invoice_id)
);

--37------------------------------------------------------------------------------------

CREATE TABLE last_notifications_viewed_times
(
  last_notifications_viewed_time_id character varying(50) NOT NULL,
  user_id character varying(50),
  is_deleted boolean DEFAULT false,
  created_at bigint,
  updated_at bigint,
  CONSTRAINT last_notifications_viewed_time_id_pk PRIMARY KEY (last_notifications_viewed_time_id),
  CONSTRAINT user_id FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--38------------------------------------------------------------------------------------

CREATE TABLE passenger_trip_ratings
(
  passenger_trip_ratings_id character varying(50) NOT NULL,
  passenger_id character varying(50) NOT NULL,
  trip_id character varying(50) NOT NULL,
  driver_id character varying(50) NOT NULL,
  note character varying(200),
  rate double precision,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT passenger_trip_ratings_id_pk PRIMARY KEY (passenger_trip_ratings_id),
  CONSTRAINT driver_id_fk FOREIGN KEY (driver_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT passenger_id_fk FOREIGN KEY (passenger_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--39------------------------------------------------------------------------------------

CREATE TABLE tours
(
  tour_id character varying(50) NOT NULL,
  user_tour_id serial NOT NULL,
  driver_id character varying(50),
  passenger_id character varying(50) NOT NULL,
  distance double precision,
  charges double precision,
  total double precision,
  promo_discount double precision,
  used_credits double precision DEFAULT 0,
  percentage double precision,
  driver_amount double precision,
  initial_fare double precision,
  per_km_fare double precision,
  per_minute_fare double precision,
  booking_fees double precision,
  minimum_fare double precision,
  discount double precision,
  p_first_name character varying(50),
  p_last_name character varying(50),
  p_email character varying(50),
  p_phone character varying(50),
  p_phone_code character varying(50),
  source_geolocation geography(Point,4326),
  destination_geolocation geography(Point,4326),
  source_address character varying(250),
  destination_address character varying(250),
  car_type_id character varying(50),
  p_photo_url character varying(250),
  status character varying(250),
  booking_type character varying(50),
  language character varying(50),
  card_owner character varying(50),
  promo_code_id character varying(50),
  is_promo_code_applied boolean DEFAULT false,
  card_booking boolean DEFAULT false,
  payment_type character varying(50),
  car_id character varying(50),
  is_fixed_fare boolean DEFAULT false,
  final_amount_collected double precision,
  toll_amount double precision,
  ride_later_pickup_time bigint DEFAULT 0,
  ride_later_pickup_time_logs character varying(100),
  is_ride_later boolean DEFAULT false,
  ride_later_last_notification bigint DEFAULT 0,
  is_tour_ride_later boolean DEFAULT false,
  is_critical_tour_ride_later boolean DEFAULT false,
  is_acknowledged boolean DEFAULT true,
  free_distance double precision,
  multicity_city_region_id character varying(50),
  multicity_country_id character varying(50),
  currency_symbol character varying(100),
  currency_symbol_html character varying(50),
  distance_type character varying(50) DEFAULT 'km',
  distance_units double precision DEFAULT 1000,
  cancellation_charges double precision DEFAULT 0,
  is_surge_price_applied boolean DEFAULT false,
  surge_price_id character varying(50),
  surge_price double precision DEFAULT 0,
  total_with_surge double precision DEFAULT 0,
  surge_fare double precision DEFAULT 0,
  distance_live double precision,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT tour_id_pk PRIMARY KEY (tour_id),
  CONSTRAINT passenger_id_fk FOREIGN KEY (passenger_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--40------------------------------------------------------------------------------------

CREATE TABLE url_groups
(
  url_group_id serial NOT NULL,
  url_group_name character varying(50) NOT NULL,
  menu_priority smallint,
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT url_group_id_pk PRIMARY KEY (url_group_id)
);

--41------------------------------------------------------------------------------------

CREATE TABLE urls
(
  url_id integer NOT NULL,
  url_group_id bigint,
  url_title character varying(50) NOT NULL,
  url character varying(100) NOT NULL,
  parent_url_id bigint NOT NULL,
  url_icon character varying(50),
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT url_id_pk PRIMARY KEY (url_id)
);

--42------------------------------------------------------------------------------------

CREATE TABLE url_accesses
(
  url_access_id serial NOT NULL,
  user_id character varying(50) NOT NULL,
  url_id bigint NOT NULL,
  record_status character varying(1) NOT NULL,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT url_access_id_pk PRIMARY KEY (url_access_id),
  CONSTRAINT url_access_user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--43------------------------------------------------------------------------------------

CREATE TABLE versions
(
  version_id character varying(50) NOT NULL,
  version character varying(100),
  release_note character varying(3000),
  app_link character varying(1000),
  device_type character varying(50),
  is_madatory boolean,
  remove_support_from bigint,
  release_date bigint,
  record_status character varying(1),
  created_by character varying(50),
  created_datetime bigint,
  updated_by character varying(50),
  updated_datetime bigint,
  CONSTRAINT version_id PRIMARY KEY (version_id)
);

--44--------------------------------------------------------------------------------------

CREATE TABLE payment_type
(
  payment_type_id character varying(50) NOT NULL,
  payment_type character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT payment_type_id_pk PRIMARY KEY (payment_type_id)
);

--45--------------------------------------------------------------------------------------

CREATE TABLE transaction_history
(
  transaction_history_id character varying(50) NOT NULL,
  order_id bigserial NOT NULL,
  user_id character varying(50),
  payment_type_id character varying(50),
  transaction_id character varying(50),
  status character varying(50),
  amount double precision,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT transaction_history_id_pk PRIMARY KEY (transaction_history_id)
);

--46--------------------------------------------------------------------------------------

CREATE TABLE admin_top_up_logs
(
  admin_top_up_logs_id character varying(50) NOT NULL,
  admin_id character varying(50),
  passenger_id character varying(50),
  previous_amount double precision,
  new_amount double precision,
  status character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT admin_top_up_logs_id_pk PRIMARY KEY (admin_top_up_logs_id)
);

--47--------------------------------------------------------------------------------------

CREATE TABLE admin_sms_sending
(
  admin_sms_sending_id character varying(50) NOT NULL,
  p_accept_by_driver boolean DEFAULT false,
  p_arrived_and_waiting boolean DEFAULT false,
  p_cancelled_by_driver boolean DEFAULT false,
  p_cancelled_by_business_o boolean DEFAULT false,
  p_invoice boolean DEFAULT false,
  p_book_by_owner boolean DEFAULT false,
  p_refund boolean DEFAULT false,
  d_booking_request boolean DEFAULT false,
  d_cancelled_by_passenger_business_o boolean DEFAULT false,
  d_payment_received boolean DEFAULT false,
  bo_accepted boolean DEFAULT false,
  bo_arrived_and_waiting boolean DEFAULT false,
  bo_cancelled_by_driver boolean DEFAULT false,
  bo_invoice boolean DEFAULT false,
  language character varying(50),
  p_credit_update_admin boolean DEFAULT false,
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint NOT NULL,
  created_by character varying(32) NOT NULL,
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT admin_sms_sending_id_pk PRIMARY KEY (admin_sms_sending_id)
);

--48--------------------------------------------------------------------------------------

CREATE TABLE admin_settings
(
  admin_settings_id character varying(50) NOT NULL,
  radius bigint,
  about_us character varying(500000),
  privacy_policy character varying(500000),
  terms_conditions character varying(500000),
  refund_policy character varying(500000),
  sender_benefit double precision DEFAULT 0,
  receiver_benefit double precision DEFAULT 0,
  distance_type character varying(100),
  distance_units bigint,
  currency_symbol character varying(50),
  currency_symbol_html character varying(50),
  no_of_cars bigint,
  country_code character varying(50),
  currency_id character varying(100),
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT admin_settings_id_pk PRIMARY KEY (admin_settings_id)
);

--49--------------------------------------------------------------------------------------

CREATE TABLE admin_area
(
  admin_area_id character varying(50) NOT NULL,
  user_id character varying(50),
  area_display_name character varying(1000),
  area_name character varying(1000),
  area_place_id character varying(100),
  area_latitude character varying(250),
  area_longitude character varying(250),
  area_country character varying(250),
  area_geolocation geography(Point,4326),
  area_radius bigint,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT admin_area_id_pk PRIMARY KEY (admin_area_id)
);

--50--------------------------------------------------------------------------------------

CREATE TABLE admin_faq
(
  admin_faq_id character varying(50) NOT NULL,
  user_id character varying(50),
  question character varying(5000),
  answer character varying(10000),
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT admin_faq_id_pk PRIMARY KEY (admin_faq_id)
);

--51--------------------------------------------------------------------------------------

CREATE TABLE admin_company_contact
(
  admin_company_contact_id character varying(50) NOT NULL,
  user_id character varying(50),
  address character varying(1000),
  phone_number_one character varying(50),
  phone_number_two character varying(50),
  phone_number_one_code character varying(50),
  phone_number_two_code character varying(50),
  fax character varying(50),
  email character varying(50),
  latitude character varying(250),
  longitude character varying(250),
  geolocation geography(Point,4326),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT admin_company_contact_id_pk PRIMARY KEY (admin_company_contact_id)
);

--52--------------------------------------------------------------------------------------

CREATE TABLE promo_code
(
  promo_code_id character varying(50) NOT NULL,
  promo_code character varying(50),
  user_id character varying(50),
  usage character varying(50),
  usage_count bigint,
  usage_type character varying(50),
  used_count bigint,
  mode character varying(50),
  discount double precision,
  start_date bigint,
  end_date bigint,
  multicity_country_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT promo_code_id_pk PRIMARY KEY (promo_code_id)
);

--53-------------------------------------------------------------------------------------------

CREATE TABLE free_waiting_time
(
  free_waiting_time_id character varying(50) NOT NULL,
  user_id character varying(50),
  waiting_time double precision,
  cancel_time double precision,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT free_waiting_time_id_pk PRIMARY KEY (free_waiting_time_id)
);

--54-------------------------------------------------------------------------------------------

CREATE TABLE tour_time
(
  tour_time_id character varying(50) NOT NULL,
  tour_id character varying(50),
  booking_time bigint,
  accept_time bigint,
  arrived_waiting_time bigint,
  start_time bigint,
  end_time bigint,
  created_at bigint,
  updated_at bigint,
  CONSTRAINT tour_time_id_pk PRIMARY KEY (tour_time_id)
);

--55-------------------------------------------------------------------------------------------

CREATE TABLE user_promo_code
(
  user_promo_code_id character varying(50) NOT NULL,
  promo_code_id character varying(50),
  user_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_promo_code_id_pk PRIMARY KEY (user_promo_code_id)
);

--56-------------------------------------------------------------------------------------------

CREATE TABLE referral_code_logs
(
  referral_code_logs_id character varying(50) NOT NULL,
  sender_id character varying(50),
  receiver_id character varying(50),
  sender_benefit double precision,
  receiver_benefit double precision,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT user_referral_code_logs_pk PRIMARY KEY (referral_code_logs_id)
);

--57-------------------------------------------------------------------------------------------

CREATE TABLE utilized_user_promo_code
(
  utilized_user_promo_code_id character varying(50) NOT NULL,
  promo_code_id character varying(50),
  user_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT utilized_user_promo_code_id_pk PRIMARY KEY (utilized_user_promo_code_id)
);

--58-------------------------------------------------------------------------------------------

CREATE TABLE favourite_driver
(
  favourite_driver_id character varying(50) NOT NULL,
  passenger_id character varying(50),
  driver_id character varying(50),
  record_status character(1) DEFAULT 'A'::bpchar,
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT favourite_driver_id_pk PRIMARY KEY (favourite_driver_id)
);

--59--------------------------------------------------------------------------------------



--60--------------------------------------------------------------------------------------



--61-------------------------------------------------------------------------------------------

CREATE TABLE car_drivers
(
  car_drivers_id character varying(50) NOT NULL,
  car_id character varying(50),
  driver_id character varying(50),
  is_active boolean DEFAULT true,
  record_status character varying(10),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_drivers_id_pk PRIMARY KEY (car_drivers_id)
);
--62-------------------------------------------------------------------------------
CREATE TABLE driver_commission_types
(
  driver_commission_type_id character varying(50) NOT NULL,
  driver_commission_type character varying(50) NOT NULL,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_commission_type_id_pk PRIMARY KEY (driver_commission_type_id)
);

--63---------------------------------------------------------------------------------  
  
CREATE TABLE driver_trip_commission
(
  driver_trip_commission_id character varying(50) NOT NULL,
  tour_id character varying(50),
  invoice_id character varying(50),
  driver_id character varying(50),
  driver_commission_type_id character varying(50),
  driver_percentage double precision,
  driver_amount double precision,
  record_status character varying(10),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_trip_commission_id_pk PRIMARY KEY (driver_trip_commission_id),
  CONSTRAINT driver_id_fk FOREIGN KEY (driver_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tour_id_fk FOREIGN KEY (tour_id)
      REFERENCES tours (tour_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id)
      REFERENCES invoices (invoice_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT driver_commission_type_id_fk FOREIGN KEY (driver_commission_type_id)
      REFERENCES driver_commission_types (driver_commission_type_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--64---------------------------------------------------------------------------------

CREATE TABLE attached_referrer_driver
(
  attached_referrer_driver_id character varying(50) NOT NULL,
  attached_driver_id character varying(50),
  user_id character varying(50),
  referred_user_role_id character varying(50),
  record_status character varying(10),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT attached_referrer_driver_id_pk PRIMARY KEY (attached_referrer_driver_id),
  CONSTRAINT attached_driver_id_fk FOREIGN KEY (attached_driver_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT referred_user_role_id_fk FOREIGN KEY (referred_user_role_id)
      REFERENCES roles (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


--65--------------------------------------------------------------------------------



--66-----------------------------------------------------------------------------------

CREATE TABLE authorize_credit_card_details
(
  authorize_credit_card_details_id character varying(50) NOT NULL,
  user_id character varying(50),
  card_number character varying(200),
  card_type character varying(50),
  gateway_type character varying(50),
  expiry_month character varying(50),
  expiry_year character varying(50),
  first_name character varying(50),
  last_name character varying(50),
  email character varying(250),
  phone_no character varying(50),
  phone_no_code character varying(50) DEFAULT 0,
  customer_profile_id character varying(250),
  customer_payment_profile_id character varying(250),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT authorize_credit_card_details_id_pk PRIMARY KEY (authorize_credit_card_details_id)
);

--67---------------------------------------------------------------------------------------------

CREATE TABLE driver_duty_logs
(
  driver_duty_logs_id character varying(50) NOT NULL,
  driver_id character varying(50),
  latitude character varying(50),
  longitude character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  duty_status boolean,
  CONSTRAINT driver_duty_logs_id_pk PRIMARY KEY (driver_duty_logs_id)
);

--68-------------------------------------------------------------------------------------------------

CREATE TABLE ride_later_settings
(
  ride_later_settings_id character varying(50) NOT NULL,
  min_booking_time bigint,
  max_booking_time bigint,
  driver_job_request_time bigint,
  driver_allocate_before_time bigint,
  driver_allocate_after_time bigint,
  passenger_tour_before_time bigint,
  passenger_tour_after_time bigint,
  record_status character varying(10),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT ride_later_settings_id_pk PRIMARY KEY (ride_later_settings_id)
);

--69----------------------------------------------------------------------------------------------

CREATE TABLE ride_later_driver_request_logs
(
  ride_later_driver_request_logs_id character varying(50) NOT NULL,
  tour_id character varying(50),
  driver_id character varying(50),
  status character varying(50),
  record_status character varying(10),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT ride_later_driver_request_logs_id_pk PRIMARY KEY (ride_later_driver_request_logs_id)
);

--70-------------------------------------------------------------------------------------------

CREATE TABLE emergency_numbers
(
  emergency_number_id character varying(50) NOT NULL,
  name character varying(100),
  phone_no character varying(50),
  no_type character varying(50),
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT emergency_number_id_pk PRIMARY KEY (emergency_number_id)
);

--71---------------------------------------------------------------------------------------------

CREATE TABLE emergency_numbers_personal
(
  emergency_number_personal_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  name character varying(100),
  phone_no character varying(50),
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT emergency_number_personal_id_pk PRIMARY KEY (emergency_number_personal_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--72------------------------------------------------------------------------------------------------

CREATE TABLE passenger_geo_location
(
  passenger_geo_location_id character varying(50) NOT NULL,
  passenger_id character varying(50) NOT NULL,
  latitude character varying(50),
  longitude character varying(50),
  geo_location geometry(Point,4326),
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT passenger_geo_location_id_pk PRIMARY KEY (passenger_geo_location_id),
  CONSTRAINT passenger_id_fk FOREIGN KEY (passenger_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--73---------------------------------------------------------------------------------------------------

CREATE TABLE track_location_tokens
(
  track_location_token_id character varying(50) NOT NULL,
  user_id character varying(50) NOT NULL,
  tour_id character varying(50),
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT track_location_token_id_pk PRIMARY KEY (track_location_token_id),
  CONSTRAINT user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--74---------------------------------------------------------------------------------------------------

CREATE TABLE recharge_amount
(
  recharge_amount_id character varying(50) NOT NULL,
  amount double precision,
  record_status character varying(1),
  created_at bigint,
  created_by character varying(50),
  updated_at bigint,
  updated_by character varying(50),
  CONSTRAINT recharge_amount_id_pk PRIMARY KEY (recharge_amount_id)
);

--75---------------------------------------------------------------------------------------------------

CREATE TABLE driver_app_versions
(
  driver_app_version_id character varying(50) NOT NULL,
  version character varying(100),
  release_note character varying(3000),
  app_link character varying(1000),
  device_type character varying(50),
  is_madatory boolean,
  remove_support_from bigint,
  release_date bigint,
  record_status character varying(1),
  created_by character varying(50),
  created_datetime bigint,
  updated_by character varying(50),
  updated_datetime bigint,
  CONSTRAINT driver_app_version_id_pk PRIMARY KEY (driver_app_version_id)
);

--76---------------------------------------------------------------------------------------------------

CREATE TABLE multicity_country
(
  multicity_country_id character varying(50) NOT NULL,
  country_name character varying(50),
  country_short_name character varying(50),
  currency_symbol character varying(100),
  currency_symbol_html character varying(50),
  sender_benefit double precision DEFAULT 0,
  receiver_benefit double precision DEFAULT 0,
  phone_no_code character varying(50),
  distance_type character varying(50),
  distance_units double precision,
  cancellation_charges double precision,
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  is_permanent_delete boolean DEFAULT false,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT multicity_country_id_pk PRIMARY KEY (multicity_country_id)
);

--77---------------------------------------------------------------------------------------------------

CREATE TABLE multicity_city_region
(
  multicity_city_region_id character varying(50) NOT NULL,
  multicity_country_id character varying(50),
  city_display_name character varying(500),
  city_original_name character varying(500),
  region_name character varying(500),
  region_place_id character varying(500),
  region_latitude character varying(250),
  region_longitude character varying(250),
  region_geolocation geography(Point,4326),
  region_radius bigint,
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  is_permanent_delete boolean DEFAULT false,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT multicity_city_region_id_pk PRIMARY KEY (multicity_city_region_id)
);

--78---------------------------------------------------------------------------------------------------

CREATE TABLE multicity_user_region
(
  multicity_user_region_id character varying(50) NOT NULL,
  multicity_country_id character varying(50),
  multicity_city_region_id character varying(50),
  user_id character varying(50),
  role_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT multicity_user_region_id_pk PRIMARY KEY (multicity_user_region_id)
);

--79---------------------------------------------------------------------------------------------------

CREATE TABLE surge_prices
(
  surge_price_id character varying(50) NOT NULL,
  multicity_city_region_id character varying(50) NOT NULL,
  surge_price double precision,
  start_time bigint,
  end_time bigint,
  is_active boolean DEFAULT true,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT surge_price_id_pk PRIMARY KEY (surge_price_id)
);

--80----------------------------------------------------------------------------------------------------

CREATE TABLE driver_car_types
(
  driver_car_type_id character varying(50) NOT NULL,
  driver_id character varying(50),
  car_type_id character varying(50),
  record_status character varying(10),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT driver_car_type_id_pk PRIMARY KEY (driver_car_type_id)
);

--81----------------------------------------------------------------------------------------------------

CREATE TABLE ccavenue_rsa_orders
(
  ccavenue_rsa_order_id character varying(50) NOT NULL,
  order_id character varying(50),
  tour_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT ccavenue_rsa_order_id_pk PRIMARY KEY (ccavenue_rsa_order_id)
);

--82----------------------------------------------------------------------------------------------------

CREATE TABLE ccavenue_rsa_requests
(
  ccavenue_rsa_request_id character varying(50) NOT NULL,
  user_id character varying(50),
  rsa_key character varying(1000),
  ccavenue_rsa_order_id character varying(50),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT ccavenue_rsa_request_id_pk PRIMARY KEY (ccavenue_rsa_request_id)
);

--83----------------------------------------------------------------------------------------------------

CREATE TABLE ccavenue_response_logs
(
  ccavenue_response_log_id character varying(50) NOT NULL,
  ccavenue_rsa_request_id character varying(50),
  tour_id character varying(50),
  tracking_id character varying(50),
  amount double precision DEFAULT 0,
  order_status character varying(20),
  failure_message character varying(1000),
  payment_mode character varying(50),
  bank_ref_no character varying(50),
  retried_payment character varying(5),
  bank_response_code character varying(10),
  card_name character varying(50),
  currency character varying(50),
  billing_country character varying(100),
  billing_tel character varying(50),
  billing_email character varying(250),
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT ccavenue_response_log_pk PRIMARY KEY (ccavenue_response_log_id)
);

--84----------------------------------------------------------------------------------------------------

CREATE TABLE taxes
(
  tax_id character varying(50) NOT NULL,
  tax_name character varying(250),
  tax_percentage double precision DEFAULT 0,
  is_active boolean DEFAULT true,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT tax_id_pk PRIMARY KEY (tax_id)
);

--85----------------------------------------------------------------------------------------------------

CREATE TABLE tour_taxes
(
  tour_tax_id character varying(50) NOT NULL,
  tour_id character varying(50),
  tax_id character varying(50),
  tax_percentage double precision DEFAULT 0,
  tax_amount double precision DEFAULT 0,
  record_status character varying(1),
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT tour_tax_id_pk PRIMARY KEY (tour_tax_id)
);
