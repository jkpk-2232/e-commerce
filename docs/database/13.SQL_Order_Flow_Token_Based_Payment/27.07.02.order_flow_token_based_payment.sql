ALTER TABLE orders ADD payment_token character varying(50);
ALTER TABLE orders ADD payment_token_generated_time bigint DEFAULT 0;

CREATE INDEX idx_orders_payment_token ON orders(payment_token);
CREATE INDEX idx_orders_payment_token_generated_time ON orders(payment_token_generated_time);