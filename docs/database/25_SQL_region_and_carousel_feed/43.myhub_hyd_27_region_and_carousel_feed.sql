ALTER TABLE vendor_feeds ADD vendor_product_id character varying(50);

CREATE INDEX idx_vendor_feeds_vendor_product_id ON vendor_feeds(vendor_product_id);