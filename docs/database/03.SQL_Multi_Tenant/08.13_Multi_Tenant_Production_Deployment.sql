update car_type set is_active=true where car_type_id='1';

update car_type set car_priority=1 where car_type_id='1';
update car_type set car_priority=2 where car_type_id='2';
update car_type set car_priority=3 where car_type_id='3';
update car_type set car_priority=4 where car_type_id='4';
update car_type set car_priority=5 where car_type_id='5';
update car_type set car_priority=6 where car_type_id='6';
update car_type set car_priority=7 where car_type_id='7';

ALTER TABLE user_info ADD agent_number character varying(50);
