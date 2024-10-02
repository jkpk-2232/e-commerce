-- Clear all the admin sessions
DELETE FROM session_attributes;
DELETE FROM sessions;

-- Change the icons, titles

UPDATE urls SET url_title='labelDashboard', url_icon='fab fa-first-order' WHERE url_id='1';
UPDATE url_groups SET url_group_name='labelDashboard' WHERE url_group_id='1';

UPDATE urls SET url_title='labelManualBookings', url_icon='fas fa-book' WHERE url_id='2'; 
UPDATE url_groups SET url_group_name='labelManualBookings' WHERE url_group_id='2';

UPDATE urls SET url_title='labelAdminUser', url_icon='fas fa-users' WHERE url_id='4'; 
UPDATE url_groups SET url_group_name='labelAdminUser' WHERE url_group_id='4';

UPDATE urls SET url_title='labelPassengers', url_icon='fas fa-user' WHERE url_id='5'; 
UPDATE url_groups SET url_group_name='labelPassengers' WHERE url_group_id='5';

UPDATE urls SET url_title='labelDrivers', url_icon='fas fa-user-secret' WHERE url_id='6'; 
UPDATE url_groups SET url_group_name='labelDrivers' WHERE url_group_id='6';

UPDATE urls SET url_title='labelCars', url_icon='fas fa-taxi' WHERE url_id='7'; 
UPDATE url_groups SET url_group_name='labelCars' WHERE url_group_id='7';

UPDATE urls SET url_title='labelOperator', url_icon='fas fa-user-md' WHERE url_id='9'; 
UPDATE url_groups SET url_group_name='labelOperator' WHERE url_group_id='9';

UPDATE urls SET url_title='labelAnnouncments', url_icon='fas fa-bullhorn' WHERE url_id='10'; 
UPDATE url_groups SET url_group_name='labelAnnouncments' WHERE url_group_id='10';

UPDATE urls SET url_title='labelRefund', url_icon='fas fa-money-bill-alt' WHERE url_id='11'; 
UPDATE url_groups SET url_group_name='labelRefund' WHERE url_group_id='11';

UPDATE urls SET url_title='labelDashboard', url_icon='fab fa-first-order' WHERE url_id='12'; 
UPDATE url_groups SET url_group_name='labelDashboard' WHERE url_group_id='12';

UPDATE urls SET url_title='labelMyBookings', url_icon='fas fa-history' WHERE url_id='13'; 
UPDATE url_groups SET url_group_name='labelMyBookings' WHERE url_group_id='13';

UPDATE urls SET url_title='labelBookings', url_icon='fas fa-history' WHERE url_id='15'; 
UPDATE url_groups SET url_group_name='labelBookings' WHERE url_group_id='15';

UPDATE urls SET url_title='labelReports', url_icon='fas fa-file-pdf' WHERE url_id='16'; 
UPDATE url_groups SET url_group_name='labelReports' WHERE url_group_id='16';

UPDATE urls SET url_title='labelRideLater', url_icon='fas fa-clock' WHERE url_id='17'; 
UPDATE url_groups SET url_group_name='labelRideLater' WHERE url_group_id='17';

UPDATE urls SET url_title='labelCriticalRideLater', url_icon='fas fa-hourglass-start' WHERE url_id='18'; 
UPDATE url_groups SET url_group_name='labelCriticalRideLater' WHERE url_group_id='18';

UPDATE urls SET url_title='labelFareCalculator', url_icon='fas fa-calculator' WHERE url_id='19'; 
UPDATE url_groups SET url_group_name='labelFareCalculator' WHERE url_group_id='19';

UPDATE urls SET url_title='labelSettings', url_icon='fas fa-cogs' WHERE url_id='20'; 
UPDATE url_groups SET url_group_name='labelSettings' WHERE url_group_id='20';

UPDATE urls SET url_title='labelVendors', url_icon='fas fa-universal-access' WHERE url_id='21'; 
UPDATE url_groups SET url_group_name='labelVendors' WHERE url_group_id='21';

UPDATE urls SET url_title='labelBookings', url_icon='fas fa-book' WHERE url_id='22'; 
UPDATE url_groups SET url_group_name='labelBookings' WHERE url_group_id='22';

UPDATE urls SET url_title='labelDrivers', url_icon='fas fa-user-secret' WHERE url_id='23'; 
UPDATE url_groups SET url_group_name='labelDrivers' WHERE url_group_id='23';

UPDATE urls SET url_title='labelCars', url_icon='fas fa-taxi' WHERE url_id='24'; 
UPDATE url_groups SET url_group_name='labelCars' WHERE url_group_id='24';

UPDATE urls SET url_title='labelRideLater', url_icon='fas fa-clock' WHERE url_id='25'; 
UPDATE url_groups SET url_group_name='labelRideLater' WHERE url_group_id='25';

UPDATE urls SET url_title='labelCriticalRideLater', url_icon='fas fa-hourglass-start' WHERE url_id='26'; 
UPDATE url_groups SET url_group_name='labelCriticalRideLater' WHERE url_group_id='26';

UPDATE urls SET url_title='labelDriverAccounts', url_icon='fas fa-credit-card' WHERE url_id='27'; 
UPDATE url_groups SET url_group_name='labelDriverAccounts' WHERE url_group_id='27';

UPDATE urls SET url_title='labelVendorAccounts', url_icon='fas fa-credit-card' WHERE url_id='28'; 
UPDATE url_groups SET url_group_name='labelVendorAccounts' WHERE url_group_id='28';

UPDATE urls SET url_title='labelHoldEncashRequests', url_icon='fas fa-dollar-sign' WHERE url_id='29'; 
UPDATE url_groups SET url_group_name='labelHoldEncashRequests' WHERE url_group_id='29';

UPDATE urls SET url_title='labelApprovedEncashRequests', url_icon='fas fa-dollar-sign' WHERE url_id='30'; 
UPDATE url_groups SET url_group_name='labelApprovedEncashRequests' WHERE url_group_id='30';

UPDATE urls SET url_title='labelTransferredEncashRequests', url_icon='fas fa-dollar-sign' WHERE url_id='31'; 
UPDATE url_groups SET url_group_name='labelTransferredEncashRequests' WHERE url_group_id='31';

UPDATE urls SET url_title='labelRejectedEncashRequests', url_icon='fas fa-dollar-sign' WHERE url_id='32'; 
UPDATE url_groups SET url_group_name='labelRejectedEncashRequests' WHERE url_group_id='32';

UPDATE urls SET url_title='labelDriverAccounts', url_icon='fas fa-edit' WHERE url_id='33'; 
UPDATE url_groups SET url_group_name='labelDriverAccounts' WHERE url_group_id='33';

UPDATE urls SET url_title='labelMyAccount', url_icon='fas fa-edit' WHERE url_id='34'; 
UPDATE url_groups SET url_group_name='labelMyAccount' WHERE url_group_id='34';

UPDATE urls SET url_title='labelTakeRide', url_icon='fas fa-book' WHERE url_id='35'; 
UPDATE url_groups SET url_group_name='labelTakeRide' WHERE url_group_id='35';

UPDATE urls SET url_title='labelSuperServices', url_icon='fas fa-paper-plane' WHERE url_id='36'; 
UPDATE url_groups SET url_group_name='labelSuperServices' WHERE url_group_id='36';

UPDATE urls SET url_title='labelCategories', url_icon='fas fa-sitemap' WHERE url_id='37'; 
UPDATE url_groups SET url_group_name='labelCategories' WHERE url_group_id='37';

UPDATE urls SET url_title='labelProducts', url_icon='fas fa-shopping-cart' WHERE url_id='38'; 
UPDATE url_groups SET url_group_name='labelProducts' WHERE url_group_id='38';

UPDATE urls SET url_title='labelSubscribers', url_icon='fas fa-user-plus' WHERE url_id='39'; 
UPDATE url_groups SET url_group_name='labelSubscribers' WHERE url_group_id='39';

UPDATE urls SET url_title='labelPromoCode', url_icon='fas fa-qrcode' WHERE url_id='40'; 
UPDATE url_groups SET url_group_name='labelPromoCode' WHERE url_group_id='40';

UPDATE urls SET url_title='labelFeeds', url_icon='fas fa-bell' WHERE url_id='41'; 
UPDATE url_groups SET url_group_name='labelFeeds' WHERE url_group_id='41';

UPDATE urls SET url_title='labelVendorStores', url_icon='fas fa-home' WHERE url_id='42'; 
UPDATE url_groups SET url_group_name='labelVendorStores' WHERE url_group_id='42';

UPDATE urls SET url_title='labelVendorMonthlySubscriptionHistory', url_icon='fas fa-history' WHERE url_id='43'; 
UPDATE url_groups SET url_group_name='labelVendorMonthlySubscriptionHistory' WHERE url_group_id='43';

-- Changes for sub menu
ALTER TABLE urls ADD is_sub_menu_option BOOLEAN DEFAULT FALSE;
UPDATE urls SET is_sub_menu_option = TRUE WHERE url_id IN (16, 20, 27, 28, 29, 30, 31, 32, 33, 34, 38, 44);

-- Delete the url access for vendor urls for admin and super admin
DELETE FROM url_accesses WHERE url_id IN (22, 23, 24, 25, 26) AND user_id IN (SELECT user_id FROM users WHERE role_id IN ('1', '2'));

-- Change the url for settings in the urls table
UPDATE urls SET url='/manage-admin-settings.do' WHERE url_id='20';

-- Give promo code access to super admin
INSERT INTO url_accesses 
(user_id, url_id, record_status, created_at, created_by, updated_at, updated_by) 
VALUES 
('1',40,'A',1692038897000,'1',1692038897000,'1');

CREATE TABLE url_sub_categories
(
  url_sub_category_id integer NOT NULL,
  url_id integer NOT NULL,
  url_title character varying(50) NOT NULL,
  url character varying(100) NOT NULL,
  url_icon character varying(50),
  menu_priority integer NOT NULL, 
  record_status character varying(1),
  created_at bigint,
  created_by character varying(32),
  updated_at bigint,
  updated_by character varying(32),
  CONSTRAINT url_sub_category_id_pk PRIMARY KEY (url_sub_category_id)
);

CREATE INDEX idx_url_sub_categories_url_sub_category_id ON url_sub_categories(url_sub_category_id);

INSERT INTO url_sub_categories
(url_sub_category_id, url_id, url_title, url, url_icon, menu_priority, record_status, created_at, created_by, updated_at, updated_by)
VALUES 
(1, 38, 'labelProduct', '/manage-products.do', 'fas fa-shopping-cart', 1, 'A', 1692038897000, '1', 1692038897000, '1'),
(2, 38, 'labelOrdersNew', '/manage-orders-new.do', 'fas fa-shopping-cart', 2, 'A', 1692038897000, '1', 1692038897000, '1'),
(3, 38, 'labelOrdersActive', '/manage-orders-active.do', 'fas fa-shopping-cart', 3, 'A', 1692038897000, '1', 1692038897000, '1'),
(4, 38, 'labelOrdersAllOthers', '/manage-orders-all-others.do', 'fas fa-shopping-cart', 4, 'A', 1692038897000, '1', 1692038897000, '1'),
(5, 38, 'labelCourierHistory', '/manage-courier-history.do', 'fas fa-history', 5, 'A', 1692038897000, '1', 1692038897000, '1'),
(6, 20, 'labelAdminSettings', '/manage-admin-settings.do', 'fas fa-cogs', 1, 'A', 1692038897000, '1', 1692038897000, '1'),
(7, 20, 'labelSMSSettings', '/manage-admin-sms-sending.do', 'fas fa-cogs', 2, 'A', 1692038897000, '1', 1692038897000, '1'),
(8, 20, 'labelDynamicCarSettings', '/manage-dynamic-cars.do', 'fas fa-cogs', 3, 'A', 1692038897000, '1', 1692038897000, '1'),
(9, 20, 'labelMultiCitySettings', '/manage-multicity-settings-city.do', 'fas fa-cogs', 4, 'A', 1692038897000, '1', 1692038897000, '1'),
(10, 20, 'labelAirportRegions', '/manage-airport-regions.do', 'fas fa-cogs', 5, 'A', 1692038897000, '1', 1692038897000, '1'),
(11, 20, 'labelSurgePriceSettings', '/manage-surge-price.do', 'fas fa-cogs', 6, 'A', 1692038897000, '1', 1692038897000, '1'),
(12, 20, 'labelCitySurgeSettings', '/manage-city-surge.do', 'fas fa-cogs', 7, 'A', 1692038897000, '1', 1692038897000, '1'),
(13, 20, 'labelManageTax', '/manage-tax.do', 'fas fa-cogs', 8, 'A', 1692038897000, '1', 1692038897000, '1'),
(14, 20, 'labelBookLaterSettings', '/manage-ride-later-settings.do', 'fas fa-cogs', 9, 'A', 1692038897000, '1', 1692038897000, '1'),
(15, 20, 'labelRentalPackages', '/manage-rental-packages.do', 'fas fa-cogs', 10, 'A', 1692038897000, '1', 1692038897000, '1'),
(16, 20, 'labelDriverWalletSettings', '/manage-driver-wallet/settings.do', 'fas fa-cogs', 11, 'A', 1692038897000, '1', 1692038897000, '1'),
(17, 20, 'labelOrderSettings', '/manage-order-settings.do', 'fas fa-cogs', 12, 'A', 1692038897000, '1', 1692038897000, '1'),
(18, 20, 'labelManageSubscriptionPackages', '/manage-subscription-packages.do', 'fas fa-cogs', 13, 'A', 1692038897000, '1', 1692038897000, '1'),
(19, 20, 'labelAboutUs', '/manage-about-us.do', 'fas fa-cogs', 14, 'A', 1692038897000, '1', 1692038897000, '1'),
(20, 20, 'labelPrivacyPolicy', '/manage-privacy-policy.do', 'fas fa-cogs', 15, 'A', 1692038897000, '1', 1692038897000, '1'),
(21, 20, 'labelRefundPolicy', '/manage-refund-policy.do', 'fas fa-cogs', 16, 'A', 1692038897000, '1', 1692038897000, '1'),
(22, 20, 'labelTermsAndConditions', '/manage-terms-conditions.do', 'fas fa-cogs', 17, 'A', 1692038897000, '1', 1692038897000, '1'),
(23, 20, 'labelContactUs', '/manage-admin-contact-us.do', 'fas fa-cogs', 18, 'A', 1692038897000, '1', 1692038897000, '1');

-- Remove vendor specific urls for drivers and cars
UPDATE urls SET url='/manage-drivers.do' WHERE url_id='23';
UPDATE urls SET url='/manage-cars.do' WHERE url_id='24';
UPDATE urls SET url='/manage-ride-later.do' WHERE url_id='25';
UPDATE urls SET url='/manage-critical-ride-later.do' WHERE url_id='26';