-- Settings for take booking by drivers

ALTER TABLE ride_later_settings ADD take_booking_for_next_X_days int DEFAULT 1;
ALTER TABLE ride_later_settings ADD take_booking_max_number_allowed int DEFAULT 2;

-- Flags for take booking

ALTER TABLE tours ADD is_take_booking_by_driver BOOLEAN DEFAULT FALSE;
ALTER TABLE tours ADD take_booking_driver_id character varying(32);
ALTER TABLE tours ADD take_booking_by_driver_time bigint DEFAULT 0;