--01. Add new columns to db for dynamic car-----------------------------------

ALTER TABLE car_type ADD icon character varying(100);
ALTER TABLE car_type ADD is_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE car_type ADD is_permanent_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE car_type ADD is_predefined_car BOOLEAN DEFAULT FALSE;
UPDATE car_type SET is_predefined_car = TRUE;

CREATE TABLE vendor_car_type
(
  vendor_car_type_id character varying(50) NOT NULL,
  vendor_id character varying(50) NOT NULL,
  car_type_id character varying(50) NOT NULL,
  is_active boolean DEFAULT true,
  is_deleted boolean DEFAULT false,
  is_permanent_deleted boolean DEFAULT false,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT vendor_car_type_id_pk PRIMARY KEY (vendor_car_type_id)
);

UPDATE car_type SET icon = car_type_id;

--02. Add car_icons-----------------------------------------------------------

CREATE TABLE car_icons 
(
  car_icon_id character varying(50) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_by character varying(50),
  created_at bigint,
  updated_by character varying(50),
  updated_at bigint,
  CONSTRAINT car_icon_id_pk PRIMARY KEY (car_icon_id)
);

INSERT INTO car_icons
(
	car_icon_id, is_active, created_by, created_at, updated_by, updated_at
)
VALUES 
('1', TRUE, '1', 1561478496001, '1', 1561478496001),
('2', TRUE, '1', 1561478496002, '1', 1561478496002),
('3', TRUE, '1', 1561478496003, '1', 1561478496003),
('4', TRUE, '1', 1561478496004, '1', 1561478496004),
('5', TRUE, '1', 1561478496005, '1', 1561478496005),
('6', TRUE, '1', 1561478496006, '1', 1561478496006),
('7', TRUE, '1', 1561478496007, '1', 1561478496007),
('8', TRUE, '1', 1561478496008, '1', 1561478496008),
('9', TRUE, '1', 1561478496009, '1', 1561478496009),
('10', TRUE, '1', 1561478496010, '1', 1561478496010),
('11', TRUE, '1', 1561478496011, '1', 1561478496011),
('12', TRUE, '1', 1561478496012, '1', 1561478496012),
('13', TRUE, '1', 1561478496013, '1', 1561478496013),
('14', TRUE, '1', 1561478496014, '1', 1561478496014);









