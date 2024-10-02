ALTER TABLE subscription_packages ADD short_subscription_id serial NOT NULL;

ALTER TABLE ccavenue_rsa_orders ADD user_id character varying(50);
ALTER TABLE ccavenue_rsa_orders ADD payment_request_type character varying(5) DEFAULT '1';
ALTER TABLE ccavenue_rsa_orders ADD subscription_id  character varying(50);

ALTER TABLE ccavenue_rsa_requests ADD payment_request_type character varying(5) DEFAULT '1';

ALTER TABLE ccavenue_response_logs ADD order_id  character varying(50);
ALTER TABLE ccavenue_response_logs ADD subscription_id  character varying(50);
ALTER TABLE ccavenue_response_logs ADD user_id  character varying(50);
