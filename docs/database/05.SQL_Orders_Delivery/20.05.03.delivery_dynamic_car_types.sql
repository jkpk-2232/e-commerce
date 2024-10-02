-- Order delivery dynamic cars

ALTER TABLE vendor_car_type ADD priority int DEFAULT 0;

-- Run the following script

ScriptAssignEcommerceBikeToAllRegionsAndExistingVendors.java

UPDATE vendor_car_type SET priority = 1 WHERE service_type_id='2';