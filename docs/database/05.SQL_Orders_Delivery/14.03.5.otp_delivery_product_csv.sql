-- Delivery within X km

ALTER TABLE user_info ADD is_self_delivery_within_x_km BOOLEAN DEFAULT FALSE;
ALTER TABLE user_info ADD self_delivery_fee double precision DEFAULT 0;

ALTER TABLE orders ADD is_self_delivery_within_x_km BOOLEAN DEFAULT FALSE; 

-- OTP for delivery

ALTER TABLE orders ADD end_otp character varying(4) DEFAULT '1234';
ALTER TABLE unified_history ADD end_otp character varying(4) DEFAULT '1234';