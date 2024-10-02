DROP TABLE vendor_warehouses;
DROP TABLE vendor_warehouse_timings;
DROP TABLE vendor_business_association;

ALTER TABLE vendor_stores ADD type character varying(30) DEFAULT '1';
ALTER TABLE vendor_stores ADD number_of_racks integer;