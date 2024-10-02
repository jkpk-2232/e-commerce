CREATE TABLE phonepe_payments (
	phonepe_payment_id character varying(50)  NOT NULL,
	vendor_id character varying(50),
	merchant_transaction_id character varying(50),
	transaction_id character varying(50),
	user_id character varying(50),
	code character varying(30),
	message character varying(500),
	amount double precision DEFAULT 0,
	state character varying(150),
	response_code character varying(150),
	payment_instrument_type character varying(150),
	utr character varying(100),
	card_type character varying(100),
	pg_transaction_id character varying(250),
	pg_service_transaction_id character varying(250),
	bank_transaction_id character varying(250),
    pg_authorization_code character varying(250),
    arn character varying(150),
    bank_id character varying(150),
	brn character varying(150),
    response_code_description character varying(500),
	created_by character varying(50),
    created_at bigint,
    updated_by character varying(50),
    updated_at bigint,
	CONSTRAINT phonepe_payment_id_pk PRIMARY KEY (phonepe_payment_id)
);

CREATE INDEX idx_phonepe_payments_merchant_transaction_id ON phonepe_payments(merchant_transaction_id);
CREATE INDEX idx_phonepe_payments_phonepe_payment_id ON phonepe_payments(phonepe_payment_id);
CREATE INDEX idx_phonepe_payments_created_by ON phonepe_payments(created_by);
CREATE INDEX idx_phonepe_payments_created_at ON phonepe_payments(created_at);
CREATE INDEX idx_phonepe_payments_updated_by ON phonepe_payments(updated_by);
CREATE INDEX idx_phonepe_payments_updated_at ON phonepe_payments(updated_at);