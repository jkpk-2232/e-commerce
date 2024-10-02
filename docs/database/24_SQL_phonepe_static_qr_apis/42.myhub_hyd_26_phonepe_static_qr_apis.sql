ALTER TABLE user_info ADD phonepe_merchant_id character varying(50);
ALTER TABLE user_info ADD phonepe_merchant_name character varying(50);
ALTER TABLE user_info ADD salt_key character varying(50);
ALTER TABLE user_info ADD salt_index character varying(10);

CREATE INDEX idx_user_info_phonepe_merchant_id ON user_info(phonepe_merchant_id);
CREATE INDEX idx_user_info_phonepe_merchant_name ON user_info(phonepe_merchant_name);
CREATE INDEX idx_user_info_salt_key ON user_info(salt_key);
CREATE INDEX idx_user_info_salt_index ON user_info(salt_index);

ALTER TABLE vendor_stores ADD phonepe_store_id character varying(50);

CREATE INDEX idx_vendor_stores_phonepe_store_id ON vendor_stores(phonepe_store_id);


CREATE TABLE qr_profiles(
    qr_profile_id character varying(50)  NOT NULL,
    vendor_store_id character varying(50)  NOT NULL,
    qr_code_id character varying(50)  NOT NULL,
    terminal_id character varying(50),
    created_by character varying(50) ,
    created_at bigint,
    updated_by character varying(50) ,
    updated_at bigint,
    CONSTRAINT qr_profile_id_pk PRIMARY KEY (qr_profile_id)
);

CREATE INDEX idx_qr_profiles_vendor_store_id ON qr_profiles(vendor_store_id);
CREATE INDEX idx_qr_profiles_qr_code_id ON qr_profiles(qr_code_id);
CREATE INDEX idx_qr_profiles_terminal_id ON qr_profiles(terminal_id);