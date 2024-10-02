--01. Add a new table to handle vendor wise pricing-----------------------------------
		
CREATE TABLE vendor_car_fare
(
  vendor_car_fare_id character varying(50) NOT NULL,
  vendor_id character varying(50),
  multicity_city_region_id character varying(50),
  multicity_country_id character varying(50),
  car_type_id character varying(50),
  initial_fare double precision DEFAULT 0,
  per_km_fare double precision DEFAULT 0,
  per_minute_fare double precision DEFAULT 0,
  booking_fees double precision DEFAULT 0,
  minimum_fare double precision DEFAULT 0,
  discount double precision DEFAULT 0,
  driver_payable_percentage double precision DEFAULT 0,
  free_distance double precision DEFAULT 0,
  fare_after_specific_km double precision DEFAULT 0,
  km_to_increase_fare double precision DEFAULT 0,
  airport_region_id character varying(50),
  airport_booking_type character varying(25),
  is_fixed_fare boolean,
  is_day_fare boolean,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_car_fare_id_pk PRIMARY KEY (vendor_car_fare_id)
);

CREATE INDEX idx_vendor_car_fare_car_type_id ON vendor_car_fare(car_type_id);
CREATE INDEX idx_vendor_car_fare_vendor_id ON vendor_car_fare(vendor_id);
CREATE INDEX idx_vendor_car_fare_multicity_city_region_id ON vendor_car_fare(multicity_city_region_id);
CREATE INDEX idx_vendor_car_fare_multicity_country_id ON vendor_car_fare(multicity_country_id);

ALTER TABLE tours ADD passenger_vendor_id character varying(50);
ALTER TABLE tours ADD driver_vendor_id character varying(50);