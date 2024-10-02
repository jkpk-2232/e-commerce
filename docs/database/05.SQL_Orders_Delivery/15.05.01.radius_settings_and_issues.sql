-- Settings
---Setting - Delivery driver radius
---Setting - Self Delivery Radius
---Setting - Store Visibility Radius

ALTER TABLE admin_settings ADD radius_delivery_driver int DEFAULT 5;
ALTER TABLE admin_settings ADD radius_self_delivery int DEFAULT 5;
ALTER TABLE admin_settings ADD radius_store_visibility int DEFAULT 5;