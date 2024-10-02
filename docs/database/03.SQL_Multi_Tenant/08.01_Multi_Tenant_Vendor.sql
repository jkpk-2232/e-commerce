--01.Drop user_vendors table. Not used-------------------------------------------

DROP TABLE IF EXISTS user_vendors;

--02.Add role_id in driver_vendors table for the table to support multi tenant---
--Default is set to driver role id i.e., 6

ALTER TABLE driver_vendors ADD role_id character varying(10) DEFAULT '6';