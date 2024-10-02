CREATE TABLE driver_transaction_history
(
  driver_transaction_history_id character varying(50) NOT NULL,
  order_id serial NOT NULL,
  driver_id character varying(50),
  vendor_id character varying(50),
  payment_type_id character varying(5),
  transaction_id character varying(50),
  transaction_type character varying(50),
  is_debit boolean DEFAULT TRUE,
  amount double precision DEFAULT 0,
  status character varying(50), 
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT driver_transaction_history_id_pk PRIMARY KEY (driver_transaction_history_id)
);

INSERT INTO payment_type 
		(
			payment_type_id, payment_type, record_status,
			created_by, created_at, updated_by, updated_at
		) 
VALUES 
		('1','Cash','A','1',1444203417001,'1',1444203417001),
		('2','Credit','A','1',1444203417001,'1',1444203417001);

ALTER TABLE export_accesses ADD is_driver_transaction_history_export boolean DEFAULT FALSE;
UPDATE export_accesses SET is_driver_transaction_history_export = TRUE;
