
--DROP INDEX IF EXISTS idx_admin_area_area_place_id; --Example to drop index

-- 1. admin_area

CREATE INDEX idx_admin_area_area_place_id ON admin_area(area_place_id);
CREATE INDEX idx_admin_area_record_status ON admin_area(record_status);

--CREATE INDEX idx_admin_area_created_at ON admin_area(created_at);
--CREATE INDEX idx_admin_area_updated_at ON admin_area(updated_at);

-- 2. admin_company_contact

CREATE INDEX idx_admin_company_contact_record_status ON admin_company_contact(record_status);

--CREATE INDEX idx_admin_company_contact_created_at ON admin_company_contact(created_at);
--CREATE INDEX idx_admin_company_contact_updated_at ON admin_company_contact(updated_at);

-- 3. admin_faq

CREATE INDEX idx_admin_faq_record_status ON admin_faq(record_status);

--CREATE INDEX idx_admin_faq_created_at ON admin_faq(created_at);
--CREATE INDEX idx_admin_faq_updated_at ON admin_faq(updated_at);

-- 4. admin_settings

--CREATE INDEX idx_admin_settings_created_at ON admin_settings(created_at);
--CREATE INDEX idx_admin_settings_updated_at ON admin_settings(updated_at);

-- 5. admin_sms_sending

--CREATE INDEX idx_admin_sms_sending_created_at ON admin_sms_sending(created_at);
--CREATE INDEX idx_admin_sms_sending_updated_at ON admin_sms_sending(updated_at);

-- 6. admin_tip

CREATE INDEX idx_admin_tip_admin_id ON admin_tip(admin_id);
--CREATE INDEX idx_admin_tip_created_at ON admin_tip(created_at);
--CREATE INDEX idx_admin_tip_updated_at ON admin_tip(updated_at);

-- 7. admin_top_up_logs

-- 8. announcements

CREATE INDEX idx_announcements_is_deleted ON announcements(is_deleted);
CREATE INDEX idx_announcements_message ON announcements(message);

CREATE INDEX idx_announcements_created_at ON announcements(created_at);
CREATE INDEX idx_announcements_updated_at ON announcements(updated_at);

-- 9. apns_devices

CREATE INDEX idx_apns_devices_device_token ON apns_devices(device_token);
CREATE INDEX idx_apns_devices_user_id ON apns_devices(user_id);
CREATE INDEX idx_apns_devices_device_uid ON apns_devices(device_uid);
CREATE INDEX idx_apns_devices_is_deleted ON apns_devices(is_deleted);

CREATE INDEX idx_apns_devices_created_at ON apns_devices(created_at);
--CREATE INDEX idx_apns_devices_updated_at ON apns_devices(updated_at);

-- 10. apns_notification_messages

CREATE INDEX idx_apns_notification_messages_is_deleted ON apns_notification_messages(is_deleted);
CREATE INDEX idx_apns_notification_messages_to_user_id ON apns_notification_messages(to_user_id);

CREATE INDEX idx_apns_notification_messages_created_at ON apns_notification_messages(created_at);
CREATE INDEX idx_apns_notification_messages_updated_at ON apns_notification_messages(updated_at);

-- 11. attached_referrer_driver

-- 12. authorize_credit_card_details

CREATE INDEX idx_authorize_credit_card_details_user_id ON authorize_credit_card_details(user_id);
CREATE INDEX idx_authorize_credit_card_details_record_status ON authorize_credit_card_details(record_status);
CREATE INDEX idx_authorize_credit_card_details_email ON authorize_credit_card_details(email);

--CREATE INDEX idx_authorize_credit_card_details_created_at ON authorize_credit_card_details(created_at);
--CREATE INDEX idx_authorize_credit_card_details_updated_at ON authorize_credit_card_details(updated_at);

-- 13. business_operator

CREATE INDEX idx_business_operator_operator_id ON business_operator(operator_id);

--CREATE INDEX idx_business_operator_created_at ON business_operator(created_at);
--CREATE INDEX idx_business_operator_updated_at ON business_operator(updated_at);

-- 14. cancellation_charges

-- 15. cars

CREATE INDEX idx_cars_car_type_id ON cars(car_type_id);
CREATE INDEX idx_cars_driver_id ON cars(driver_id);

CREATE INDEX idx_cars_created_at ON cars(created_at);
--CREATE INDEX idx_cars_created_at ON cars(created_at);

-- 16. car_drivers

CREATE INDEX idx_car_drivers_driver_id ON car_drivers(driver_id);
CREATE INDEX idx_car_drivers_car_id ON car_drivers(car_id);
CREATE INDEX idx_car_drivers_record_status ON car_drivers(record_status);

--CREATE INDEX idx_car_drivers_created_at ON car_drivers(created_at);
--CREATE INDEX idx_car_drivers_updated_at ON car_drivers(updated_at);

-- 17. car_fare

CREATE INDEX idx_car_fare_car_type_id ON car_fare(car_type_id);
CREATE INDEX idx_car_fare_multicity_city_region_id ON car_fare(multicity_city_region_id);
CREATE INDEX idx_car_fare_multicity_country_id ON car_fare(multicity_country_id);

--CREATE INDEX idx_car_fare_created_at ON car_fare(created_at);
--CREATE INDEX idx_car_fare_updated_at ON car_fare(updated_at);

-- 18. car_type

CREATE INDEX idx_car_type_is_active ON car_type(is_active);

--CREATE INDEX idx_car_type_created_at ON car_type(created_at);
--CREATE INDEX idx_car_type_updated_at ON car_type(updated_at);

-- 19. ccavenue_response_logs

CREATE INDEX idx_ccavenue_response_logs_tour_id ON ccavenue_response_logs(tour_id);
CREATE INDEX idx_ccavenue_response_logs_record_status ON ccavenue_response_logs(record_status);
CREATE INDEX idx_ccavenue_response_logs_order_status ON ccavenue_response_logs(order_status);

CREATE INDEX idx_ccavenue_response_logs_created_at ON ccavenue_response_logs(created_at);
--CREATE INDEX idx_ccavenue_response_logs_updated_at ON ccavenue_response_logs(updated_at);

-- 20. ccavenue_rsa_orders

CREATE INDEX idx_ccavenue_rsa_orders_order_id ON ccavenue_rsa_orders(order_id);

--CREATE INDEX idx_ccavenue_rsa_orders_created_at ON ccavenue_rsa_orders(created_at);
--CREATE INDEX idx_ccavenue_rsa_orders_updated_at ON ccavenue_rsa_orders(updated_at);

-- 21. ccavenue_rsa_requests

--CREATE INDEX idx_ccavenue_rsa_requests_created_at ON ccavenue_rsa_requests(created_at);
--CREATE INDEX idx_ccavenue_rsa_requests_updated_at ON ccavenue_rsa_requests(updated_at);

-- 22. currency

--CREATE INDEX idx_currency_currency_id ON currency(currency_id);

-- 23. driver_app_versions

CREATE INDEX idx_driver_app_versions_device_type ON driver_app_versions(device_type);
CREATE INDEX idx_driver_app_versions_version ON driver_app_versions(version);
CREATE INDEX idx_driver_app_versions_release_date ON driver_app_versions(release_date);
CREATE INDEX idx_driver_app_versions_is_madatory ON driver_app_versions(is_madatory);

--CREATE INDEX idx_driver_app_versions_created_at ON driver_app_versions(created_at);
--CREATE INDEX idx_driver_app_versions_updated_at ON driver_app_versions(updated_at);

-- 24. driver_bank_details

CREATE INDEX idx_driver_bank_details_user_id ON driver_bank_details(user_id);

--CREATE INDEX idx_driver_bank_details_created_at ON driver_bank_details(created_at);
--CREATE INDEX idx_driver_bank_details_updated_at ON driver_bank_details(updated_at);

-- 25. driver_car_types

CREATE INDEX idx_driver_car_types_driver_id ON driver_car_types(driver_id);
CREATE INDEX idx_driver_car_types_record_status ON driver_car_types(record_status);

--CREATE INDEX idx_driver_car_types_created_at ON driver_car_types(created_at);
--CREATE INDEX idx_driver_car_types_updated_at ON driver_car_types(updated_at);

-- 26. driver_commission_types

-- 27. driver_duty_logs

CREATE INDEX idx_driver_duty_logs_driver_id ON driver_duty_logs(driver_id);
CREATE INDEX idx_driver_duty_logs_duty_status ON driver_duty_logs(duty_status);

CREATE INDEX idx_driver_duty_logs_created_at ON driver_duty_logs(created_at);
--CREATE INDEX idx_driver_duty_logs_updated_at ON driver_duty_logs(updated_at);

-- 28. driver_gps

CREATE INDEX idx_driver_gps_driver_id ON driver_gps(driver_id);

CREATE INDEX idx_driver_gps_created_at ON driver_gps(created_at);
--CREATE INDEX idx_driver_gps_updated_at ON driver_gps(updated_at);

-- 29. driver_payable_percentage

-- 30. driver_referral_code_logs

CREATE INDEX idx_driver_referral_code_logs_passenger_id ON driver_referral_code_logs(passenger_id);
CREATE INDEX idx_driver_referral_code_logs_record_status ON driver_referral_code_logs(record_status);
CREATE INDEX idx_driver_referral_code_logs_driver_id ON driver_referral_code_logs(driver_id);

CREATE INDEX idx_driver_referral_code_logs_created_at ON driver_referral_code_logs(created_at);
--CREATE INDEX idx_driver_referral_code_logs_updated_at ON driver_referral_code_logs(updated_at);

-- 31. driver_tour_requests

CREATE INDEX idx_driver_tour_requests_driver_id ON driver_tour_requests(driver_id);
CREATE INDEX idx_driver_tour_requests_tour_id ON driver_tour_requests(tour_id);
CREATE INDEX idx_driver_tour_requests_status ON driver_tour_requests(status);

--CREATE INDEX idx_driver_tour_requests_created_at ON driver_tour_requests(created_at);
--CREATE INDEX idx_driver_tour_requests_updated_at ON driver_tour_requests(updated_at);

-- 32. driver_tour_status

CREATE INDEX idx_driver_tour_status_driver_id ON driver_tour_status(driver_id);
CREATE INDEX idx_driver_tour_status_status ON driver_tour_status(status);

--CREATE INDEX idx_driver_tour_status_created_at ON driver_tour_status(created_at);
--CREATE INDEX idx_driver_tour_status_updated_at ON driver_tour_status(updated_at);

-- 33. driver_trip_ratings

CREATE INDEX idx_driver_trip_ratings_driver_id ON driver_trip_ratings(driver_id);
CREATE INDEX idx_driver_trip_ratings_passenger_id ON driver_trip_ratings(passenger_id);
CREATE INDEX idx_driver_trip_ratings_trip_id ON driver_trip_ratings(trip_id);

--CREATE INDEX idx_driver_trip_ratings_created_at ON driver_trip_ratings(created_at);
--CREATE INDEX idx_driver_trip_ratings_updated_at ON driver_trip_ratings(updated_at);

-- 34. driving_license_info

CREATE INDEX idx_driving_license_info_user_id ON driving_license_info(user_id);

--CREATE INDEX idx_driving_license_info_created_at ON driving_license_info(created_at);
--CREATE INDEX idx_driving_license_info_updated_at ON driving_license_info(updated_at);

-- 35. emergency_numbers

CREATE INDEX idx_emergency_numbers_no_type ON emergency_numbers(no_type);
CREATE INDEX idx_emergency_numbers_record_status ON emergency_numbers(record_status);

--CREATE INDEX idx_emergency_numbers_created_at ON emergency_numbers(created_at);
--CREATE INDEX idx_emergency_numbers_updated_at ON emergency_numbers(updated_at);

-- 36. emergency_numbers_personal

CREATE INDEX idx_emergency_numbers_personal_user_id ON emergency_numbers_personal(user_id);
CREATE INDEX idx_emergency_numbers_personal_record_status ON emergency_numbers_personal(record_status);

--CREATE INDEX idx_emergency_numbers_personal_created_at ON emergency_numbers_personal(created_at);
--CREATE INDEX idx_emergency_numbers_personal_updated_at ON emergency_numbers_personal(updated_at);

-- 37. export_accesses

CREATE INDEX idx_export_accesses_user_id ON export_accesses(user_id);
CREATE INDEX idx_export_accesses_is_active ON export_accesses(is_active);

--CREATE INDEX idx_export_accesses_created_at ON export_accesses(created_at);
--CREATE INDEX idx_export_accesses_updated_at ON export_accesses(updated_at);

-- 38. favourite_driver

CREATE INDEX idx_favourite_driver_passenger_id ON favourite_driver(passenger_id);
CREATE INDEX idx_favourite_driver_driver_id ON favourite_driver(driver_id);
CREATE INDEX idx_favourite_driver_record_status ON favourite_driver(record_status);

--CREATE INDEX idx_favourite_driver_created_at ON favourite_driver(created_at);
--CREATE INDEX idx_favourite_driver_updated_at ON favourite_driver(updated_at);

-- 39. favourite_locations

CREATE INDEX idx_favourite_locations_user_id ON favourite_locations(user_id);
CREATE INDEX idx_favourite_locations_record_status ON favourite_locations(record_status);

--CREATE INDEX idx_favourite_locations_created_at ON favourite_locations(created_at);
--CREATE INDEX idx_favourite_locations_updated_at ON favourite_locations(updated_at);

-- 40. forgot_password_sessions

CREATE INDEX idx_forgot_password_sessions_expires_at ON forgot_password_sessions(expires_at);

--CREATE INDEX idx_forgot_password_sessions_created_at ON forgot_password_sessions(created_at);
--CREATE INDEX idx_forgot_password_sessions_updated_at ON forgot_password_sessions(updated_at);

-- 41. free_waiting_time

CREATE INDEX idx_free_waiting_time_cancel_time ON free_waiting_time(cancel_time);

--CREATE INDEX idx_free_waiting_time_created_at ON free_waiting_time(created_at);
--CREATE INDEX idx_free_waiting_time_updated_at ON free_waiting_time(updated_at);

-- 42. invoices

CREATE INDEX idx_invoices_tour_id ON invoices(tour_id);
CREATE INDEX idx_invoices_is_refunded ON invoices(is_refunded);
CREATE INDEX idx_invoices_is_payment_paid ON invoices(is_payment_paid);

CREATE INDEX idx_invoices_created_at ON invoices(created_at);
CREATE INDEX idx_invoices_updated_at ON invoices(updated_at);

-- 43. multicity_city_region

--CREATE INDEX idx_multicity_city_region_multicity_country_id ON multicity_city_region(multicity_country_id);
CREATE INDEX idx_multicity_city_region_is_active ON multicity_city_region(is_active);
CREATE INDEX idx_multicity_city_region_is_deleted ON multicity_city_region(is_deleted);
CREATE INDEX idx_multicity_city_region_is_permanent_delete ON multicity_city_region(is_permanent_delete);

CREATE INDEX idx_multicity_city_region_created_at ON multicity_city_region(created_at);
CREATE INDEX idx_multicity_city_region_updated_at ON multicity_city_region(updated_at);

-- 44. multicity_country

CREATE INDEX idx_multicity_country_phone_no_code ON multicity_country(phone_no_code);
CREATE INDEX idx_multicity_country_is_active ON multicity_country(is_active);
CREATE INDEX idx_multicity_country_is_deleted ON multicity_country(is_deleted);

--CREATE INDEX idx_multicity_country_created_at ON multicity_country(created_at);
--CREATE INDEX idx_multicity_country_updated_at ON multicity_country(updated_at);

-- 45. multicity_user_region

CREATE INDEX idx_multicity_user_region_user_id ON multicity_user_region(user_id);
CREATE INDEX idx_multicity_user_region_record_status ON multicity_user_region(record_status);

--CREATE INDEX idx_multicity_user_region_created_at ON multicity_user_region(created_at);
--CREATE INDEX idx_multicity_user_region_updated_at ON multicity_user_region(updated_at);

-- 46. passenger_geo_location

CREATE INDEX idx_passenger_geo_location_passenger_id ON passenger_geo_location(passenger_id);
CREATE INDEX idx_passenger_geo_location_record_status ON passenger_geo_location(record_status);

CREATE INDEX idx_passenger_geo_location_created_at ON passenger_geo_location(created_at);
--CREATE INDEX idx_passenger_geo_location_updated_at ON passenger_geo_location(updated_at);

-- 47. passenger_trip_ratings

CREATE INDEX idx_passenger_trip_ratings_trip_id ON passenger_trip_ratings(trip_id);
CREATE INDEX idx_passenger_trip_ratings_driver_id ON passenger_trip_ratings(driver_id);
CREATE INDEX idx_passenger_trip_ratings_passenger_id ON passenger_trip_ratings(passenger_id);

--CREATE INDEX idx_passenger_trip_ratings_created_at ON passenger_trip_ratings(created_at);
--CREATE INDEX idx_passenger_trip_ratings_updated_at ON passenger_trip_ratings(updated_at);

-- 48. payment_type

CREATE INDEX idx_payment_type_payment_type ON payment_type(payment_type);
CREATE INDEX idx_payment_type_record_status ON payment_type(record_status);

--CREATE INDEX idx_payment_type_created_at ON payment_type(created_at);
--CREATE INDEX idx_payment_type_updated_at ON payment_type(updated_at);

-- 49. 

-- 50. 

-- 51. promo_code

CREATE INDEX idx_promo_code_promo_code ON promo_code(promo_code);
CREATE INDEX idx_promo_code_record_status ON promo_code(record_status);

CREATE INDEX idx_promo_code_created_at ON promo_code(created_at);
CREATE INDEX idx_promo_code_updated_at ON promo_code(updated_at);

-- 52. recharge_amount

--CREATE INDEX idx_recharge_amount_created_at ON recharge_amount(created_at);
--CREATE INDEX idx_recharge_amount_updated_at ON recharge_amount(updated_at);

-- 53. referral_code_logs

CREATE INDEX idx_referral_code_logs_sender_id ON referral_code_logs(sender_id);
CREATE INDEX idx_referral_code_logs_receiver_id ON referral_code_logs(receiver_id);
CREATE INDEX idx_referral_code_logs_record_status ON referral_code_logs(record_status);

CREATE INDEX idx_referral_code_logs_created_at ON referral_code_logs(created_at);
--CREATE INDEX idx_referral_code_logs_updated_at ON referral_code_logs(updated_at);

-- 54. rental_packages

CREATE INDEX idx_rental_packages_multicity_city_region_id ON rental_packages(multicity_city_region_id);
CREATE INDEX idx_rental_packages_rental_package_type ON rental_packages(rental_package_type);
CREATE INDEX idx_rental_packages_is_active ON rental_packages(is_active);
CREATE INDEX idx_rental_packages_record_status ON rental_packages(record_status);

CREATE INDEX idx_rental_packages_created_at ON rental_packages(created_at);
CREATE INDEX idx_rental_packages_updated_at ON rental_packages(updated_at);

-- 55. rental_package_fare

CREATE INDEX idx_rental_package_fare_rental_package_id ON rental_package_fare(rental_package_id);
CREATE INDEX idx_rental_package_fare_car_type_id ON rental_package_fare(car_type_id);
CREATE INDEX idx_rental_package_fare_record_status ON rental_package_fare(record_status);

--CREATE INDEX idx_rental_package_fare_created_at ON rental_package_fare(created_at);
--CREATE INDEX idx_rental_package_fare_updated_at ON rental_package_fare(updated_at);

-- 56. ride_later_settings

--CREATE INDEX idx_ride_later_settings_created_at ON ride_later_settings(created_at);
--CREATE INDEX idx_ride_later_settings_updated_at ON ride_later_settings(updated_at);

-- 57. roles

CREATE INDEX idx_roles_role ON roles(role);

--CREATE INDEX idx_roles_created_at ON roles(created_at);
--CREATE INDEX idx_roles_updated_at ON roles(updated_at);

-- 58. session_attributes

CREATE INDEX idx_session_attributes_session_id ON session_attributes(session_id);
CREATE INDEX idx_session_attributes_attribute ON session_attributes(attribute);

--CREATE INDEX idx_session_attributes_created_at ON session_attributes(created_at);
--CREATE INDEX idx_session_attributes_updated_at ON session_attributes(updated_at);

-- 59. surge_prices

CREATE INDEX idx_surge_prices_multicity_city_region_id ON surge_prices(multicity_city_region_id);
CREATE INDEX idx_surge_prices_surge_price ON surge_prices(surge_price);
CREATE INDEX idx_surge_prices_start_time ON surge_prices(start_time);
CREATE INDEX idx_surge_prices_end_time ON surge_prices(end_time);
CREATE INDEX idx_surge_prices_is_active ON surge_prices(is_active);
CREATE INDEX idx_surge_prices_record_status ON surge_prices(record_status);

CREATE INDEX idx_surge_prices_created_at ON surge_prices(created_at);
CREATE INDEX idx_surge_prices_updated_at ON surge_prices(updated_at);

-- 60. taxes

CREATE INDEX idx_taxes_record_status ON taxes(record_status);

--CREATE INDEX idx_taxes_created_at ON taxes(created_at);
--CREATE INDEX idx_taxes_updated_at ON taxes(updated_at);

-- 61. tours

CREATE INDEX idx_tours_user_tour_id ON tours(user_tour_id);
CREATE INDEX idx_tours_passenger_id ON tours(passenger_id);
CREATE INDEX idx_tours_driver_id ON tours(driver_id);
CREATE INDEX idx_tours_booking_type ON tours(booking_type);
CREATE INDEX idx_tours_is_tour_ride_later ON tours(is_tour_ride_later);
CREATE INDEX idx_tours_is_ride_later ON tours(is_ride_later);
CREATE INDEX idx_tours_ride_later_pickup_time ON tours(ride_later_pickup_time);
CREATE INDEX idx_tours_multicity_city_region_id ON tours(multicity_city_region_id);
CREATE INDEX idx_tours_status ON tours(status);

CREATE INDEX idx_tours_created_at ON tours(created_at);
CREATE INDEX idx_tours_updated_at ON tours(updated_at);

-- 62. tour_referrer_benefits

CREATE INDEX idx_tour_referrer_benefits_tour_id ON tour_referrer_benefits(tour_id);
CREATE INDEX idx_tour_referrer_benefits_record_status ON tour_referrer_benefits(record_status);

CREATE INDEX idx_tour_referrer_benefits_created_at ON tour_referrer_benefits(created_at);
--CREATE INDEX idx_tour_referrer_benefits_updated_at ON tour_referrer_benefits(updated_at);

-- 63. tour_taxes

CREATE INDEX idx_tour_taxes_tour_id ON tour_taxes(tour_id);
CREATE INDEX idx_tour_taxes_record_status ON tour_taxes(record_status);

--CREATE INDEX idx_tour_taxes_created_at ON tour_taxes(created_at);
--CREATE INDEX idx_tour_taxes_updated_at ON tour_taxes(updated_at);

-- 64. tour_time

CREATE INDEX idx_tour_time_tour_id ON tour_time(tour_id);

--CREATE INDEX idx_tour_time_created_at ON tour_time(created_at);
--CREATE INDEX idx_tour_time_updated_at ON tour_time(updated_at);

-- 65. track_location_tokens

CREATE INDEX idx_track_location_tokens_record_status ON track_location_tokens(record_status);

--CREATE INDEX idx_track_location_tokens_created_at ON track_location_tokens(created_at);
--CREATE INDEX idx_track_location_tokens_updated_at ON track_location_tokens(updated_at);

-- 66. transaction_history

CREATE INDEX idx_transaction_history_user_id ON transaction_history(user_id);
CREATE INDEX idx_transaction_history_transaction_id ON transaction_history(transaction_id);
CREATE INDEX idx_transaction_history_record_status ON transaction_history(record_status);

--CREATE INDEX idx_transaction_history_created_at ON transaction_history(created_at);
--CREATE INDEX idx_transaction_history_updated_at ON transaction_history(updated_at);

-- 67. 

-- 68. urls

CREATE INDEX idx_urls_url_group_id ON urls(url_group_id);
CREATE INDEX idx_urls_parent_url_id ON urls(parent_url_id);
CREATE INDEX idx_urls_record_status ON urls(record_status);

--CREATE INDEX idx_urls_created_at ON urls(created_at);
--CREATE INDEX idx_urls_updated_at ON urls(updated_at);

-- 69. url_accesses

CREATE INDEX idx_url_accesses_user_id ON url_accesses(user_id);
CREATE INDEX idx_url_accesses_record_status ON url_accesses(record_status);

--CREATE INDEX idx_url_accesses_created_at ON url_accesses(created_at);
--CREATE INDEX idx_url_accesses_updated_at ON url_accesses(updated_at);

-- 70. url_groups

CREATE INDEX idx_url_groups_record_status ON url_groups(record_status);

--CREATE INDEX idx_url_groups_created_at ON url_groups(created_at);
--CREATE INDEX idx_url_groups_updated_at ON url_groups(updated_at);

-- 71. users

CREATE INDEX idx_users_password ON users(password);
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_users_is_on_duty ON users(is_on_duty);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_is_deleted ON users(is_deleted);

CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_updated_at ON users(updated_at);

-- 72. credit_card_details

CREATE INDEX idx_credit_card_details_user_id ON credit_card_details(user_id);
CREATE INDEX idx_credit_card_details_record_status ON credit_card_details(record_status);

--CREATE INDEX idx_credit_card_details_created_at ON credit_card_details(created_at);
--CREATE INDEX idx_credit_card_details_updated_at ON credit_card_details(updated_at);

-- 73. user_payment_profile

CREATE INDEX idx_user_payment_profile_user_id ON user_payment_profile(user_id);
CREATE INDEX idx_user_payment_profile_record_status ON user_payment_profile(record_status);

--CREATE INDEX idx_user_payment_profile_created_at ON user_payment_profile(created_at);
--CREATE INDEX idx_user_payment_profile_updated_at ON user_payment_profile(updated_at);

-- 74. user_info

CREATE INDEX idx_user_info_user_id ON user_info(user_id);
CREATE INDEX idx_user_info_referral_code ON user_info(referral_code);
CREATE INDEX idx_user_info_is_verified ON user_info(is_verified);
CREATE INDEX idx_user_info_application_status ON user_info(application_status);

CREATE INDEX idx_user_info_created_at ON user_info(created_at);
CREATE INDEX idx_user_info_updated_at ON user_info(updated_at);

-- 75. user_promo_code

CREATE INDEX idx_user_promo_code_promo_code_id ON user_promo_code(promo_code_id);
CREATE INDEX idx_user_promo_code_user_id ON user_promo_code(user_id);
CREATE INDEX idx_user_promo_code_record_status ON user_promo_code(record_status);

CREATE INDEX idx_user_promo_code_created_at ON user_promo_code(created_at);
--CREATE INDEX idx_user_promo_code_updated_at ON user_promo_code(updated_at);

-- 76. user_roles

CREATE INDEX idx_user_role_role_id ON user_role(role_id);
CREATE INDEX idx_user_role_user_id ON user_role(user_id);

--CREATE INDEX idx_user_role_created_at ON user_role(created_at);
--CREATE INDEX idx_user_role_updated_at ON user_role(updated_at);

-- 77. user_vendors

CREATE INDEX idx_user_vendors_user_id ON user_vendors(user_id);
CREATE INDEX idx_user_vendors_is_deleted ON user_vendors(is_deleted);

--CREATE INDEX idx_user_vendors_created_at ON user_vendors(created_at);
--CREATE INDEX idx_user_vendors_updated_at ON user_vendors(updated_at);

-- 78. utilized_user_promo_code

CREATE INDEX idx_utilized_user_promo_code_promo_code_id ON utilized_user_promo_code(promo_code_id);
CREATE INDEX idx_utilized_user_promo_code_user_id ON utilized_user_promo_code(user_id);
CREATE INDEX idx_utilized_user_promo_code_record_status ON utilized_user_promo_code(record_status);

--CREATE INDEX idx_utilized_user_promo_code_created_at ON utilized_user_promo_code(created_at);
--CREATE INDEX idx_utilized_user_promo_code_updated_at ON utilized_user_promo_code(updated_at);

-- 79. versions

CREATE INDEX idx_versions_device_type ON versions(device_type);
CREATE INDEX idx_versions_version ON versions(version);
CREATE INDEX idx_versions_release_date ON versions(release_date);
CREATE INDEX idx_versions_is_madatory ON versions(is_madatory);

--CREATE INDEX idx_versions_created_at ON versions(created_at);
--CREATE INDEX idx_versions_updated_at ON versions(updated_at);

--------------------------------------------------------------------------------------------------------------------------
--CR indexes

-- 80. driver_logged_in_times

CREATE INDEX idx_driver_logged_in_times_driver_id ON driver_logged_in_times(driver_id);
CREATE INDEX idx_driver_logged_in_times_date_time ON driver_logged_in_times(date_time);

--CREATE INDEX idx_driver_logged_in_times_created_at ON driver_logged_in_times(created_at);
--CREATE INDEX idx_driver_logged_in_times_updated_at ON driver_logged_in_times(updated_at);

-- 81. driver_vendors

CREATE INDEX idx_driver_vendors_driver_id ON driver_vendors(driver_id);

--CREATE INDEX idx_driver_vendors_created_at ON driver_vendors(created_at);
--CREATE INDEX idx_driver_vendors_updated_at ON driver_vendors(updated_at);

-- 82. car_vendors

CREATE INDEX idx_car_vendors_car_id ON car_vendors(car_id);

--CREATE INDEX idx_car_vendors_created_at ON car_vendors(created_at);
--CREATE INDEX idx_car_vendors_updated_at ON car_vendors(updated_at);

-- 83. airport_regions

CREATE INDEX idx_airport_regions_multicity_city_region_id ON airport_regions(multicity_city_region_id);
CREATE INDEX idx_airport_regions_is_active ON airport_regions(is_active);
CREATE INDEX idx_airport_regions_created_at ON airport_regions(created_at);

--CREATE INDEX idx_airport_regions_updated_at ON airport_regions(updated_at);

-- 84. city_surge

CREATE INDEX idx_city_surge_multicity_city_region_id ON city_surge(multicity_city_region_id);
CREATE INDEX idx_city_surge_radius ON city_surge(radius);
CREATE INDEX idx_city_surge_record_status ON city_surge(record_status);
CREATE INDEX idx_city_surge_is_active ON city_surge(is_active);

--CREATE INDEX idx_city_surge_created_at ON city_surge(created_at);
--CREATE INDEX idx_city_surge_updated_at ON city_surge(updated_at);

-- 85. user_login_otp

CREATE INDEX idx_user_login_otp_user_id ON user_login_otp(user_id);
CREATE INDEX idx_user_login_otp_verification_code ON user_login_otp(verification_code);

--CREATE INDEX idx_user_login_otp_created_at ON user_login_otp(created_at);
--CREATE INDEX idx_user_login_otp_updated_at ON user_login_otp(updated_at);

-- 86. user_account

CREATE INDEX idx_user_account_user_id ON user_account(user_id);
CREATE INDEX idx_user_account_is_active ON user_account(is_active);
CREATE INDEX idx_user_account_record_status ON user_account(record_status);

--CREATE INDEX idx_user_account_created_at ON user_account(created_at);
--CREATE INDEX idx_user_account_updated_at ON user_account(updated_at);

-- 87. user_account_logs

CREATE INDEX idx_user_account_logs_user_account_id ON user_account_logs(user_account_id);
CREATE INDEX idx_user_account_logs_user_id ON user_account_logs(user_id);
CREATE INDEX idx_user_account_logs_transaction_type ON user_account_logs(transaction_type);
CREATE INDEX idx_user_account_logs_created_at ON user_account_logs(created_at);

--CREATE INDEX idx_user_account_logs_updated_at ON user_account_logs(updated_at);

-- 88. encash_requests

CREATE INDEX idx_encash_requests_user_id ON encash_requests(user_id);
CREATE INDEX idx_encash_requests_status ON encash_requests(status);
CREATE INDEX idx_encash_requests_hold_date ON encash_requests(hold_date);
CREATE INDEX idx_encash_requests_approved_date ON encash_requests(approved_date);
CREATE INDEX idx_encash_requests_rejected_date ON encash_requests(rejected_date);
CREATE INDEX idx_encash_requests_transfer_date ON encash_requests(transfer_date);
CREATE INDEX idx_encash_requests_created_at ON encash_requests(created_at);

--CREATE INDEX idx_encash_requests_updated_at ON encash_requests(updated_at);

-- 89. user_account_log_details

CREATE INDEX idx_user_account_log_details_user_account_log_id ON user_account_log_details(user_account_log_id);
CREATE INDEX idx_user_account_log_details_user_account_id ON user_account_log_details(user_account_id);
CREATE INDEX idx_user_account_log_details_user_id ON user_account_log_details(user_id);
CREATE INDEX idx_user_account_log_details_transaction_type ON user_account_log_details(transaction_type);
CREATE INDEX idx_user_account_log_details_created_at ON user_account_log_details(created_at);

--CREATE INDEX idx_user_account_log_details_updated_at ON user_account_log_details(updated_at);

--------------------------------------------------------------------------------------------------------------
