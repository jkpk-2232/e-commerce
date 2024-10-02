-- Order delivery ccavenue flow

ALTER TABLE ccavenue_rsa_orders ADD delivery_order_id  character varying(50);
ALTER TABLE ccavenue_response_logs ADD delivery_order_id  character varying(50);