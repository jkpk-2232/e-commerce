ALTER TABLE subscription_packages ADD car_type_id character varying(50) DEFAULT '2';
ALTER TABLE driver_subscription_package_history ADD car_type_id character varying(50) DEFAULT '2';