--01. Table to maintain vendor and airport mapping

CREATE TABLE vendor_airport_regions
(
  vendor_airport_region_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  airport_region_id character varying(50),
  multicity_city_region_id character varying(50),
  multicity_country_id character varying(50),
  is_active BOOLEAN DEFAULT TRUE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_airport_region_id_pk PRIMARY KEY (vendor_airport_region_id)
);

--02. Table to maintain vendor and airport and it's car fare mapping

CREATE TABLE vendor_airport_region_car_fare
(
  vendor_airport_region_car_fare_id character varying(50) NOT NULL,
  vendor_airport_region_id character varying(50),
  vendor_id character varying(50),
  airport_region_id character varying(50),
  airport_booking_type character varying(25),
  multicity_city_region_id character varying(50),
  multicity_country_id character varying(50),
  car_type_id character varying(50),
  initial_fare double precision DEFAULT 0,
  per_km_fare double precision DEFAULT 0,
  per_minute_fare double precision DEFAULT 0,
  free_distance double precision DEFAULT 0,
  fare_after_specific_km double precision DEFAULT 0,
  km_to_increase_fare double precision DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_airport_region_car_fare_id_pk PRIMARY KEY (vendor_airport_region_car_fare_id)
);

--03. To have track of the airport assigned to a particluar tour

ALTER TABLE tours ADD is_airport_booking BOOLEAN DEFAULT FALSE;
ALTER TABLE tours ADD airport_region_id character varying(50);

--04. Added vendor_id in rental_packages table to make it vendor specific.

ALTER TABLE rental_packages ADD vendor_id character varying(50);
ALTER TABLE tours ADD rental_vendor_id character varying(50); 