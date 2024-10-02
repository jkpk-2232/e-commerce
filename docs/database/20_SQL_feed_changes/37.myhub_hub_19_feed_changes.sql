ALTER TABLE vendor_feeds ADD media_type character varying(50) ;
ALTER TABLE vendor_feeds ADD is_sponsored BOOLEAN DEFAULT FALSE;
ALTER TABLE vendor_feeds ADD is_deleted BOOLEAN DEFAULT FALSE;