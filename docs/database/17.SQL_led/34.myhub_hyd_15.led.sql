UPDATE urls SET is_sub_menu_option = true WHERE url_id = 44;

INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(38,44,'labelAD','/manage-ads.do',1,'A',1712102434000,'1',1712102434000,'1');
	
INSERT INTO url_sub_categories
	(url_sub_category_id,url_id,url_title,url,menu_priority,record_status,created_at,created_by,updated_at,updated_by)
VALUES 	
	(39,44,'labelCampaign','/manage-campaigns.do',1,'A',1712102434000,'1',1712102434000,'1');
