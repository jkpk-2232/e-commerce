ALTER TABLE car_type ADD car_type_icon_image character varying(300);

UPDATE car_type SET car_type_icon_image = '/getimage.do?imageId=default_car_type_icon_image.jpg';