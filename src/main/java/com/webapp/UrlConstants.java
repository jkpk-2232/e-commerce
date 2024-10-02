package com.webapp;

import javax.servlet.http.HttpServletRequest;

import com.utils.myhub.WebappPropertyUtils;

public class UrlConstants {

	public static String getUrl(HttpServletRequest request, String url) {
		return (request.getContextPath() + url);
	}

	public class PAGE_URLS {

		public static final String LOGIN_URL = "/login.do";
		public static final String VENDOR_LOGIN_URL = "/vendor/login.do";
		public static final String LOGOUT_URL = "/logout.do";
		public static final String FORGOT_PASSWORD_URL = "/forgot-password.do?roleId=%s";

		public final static String ADMIN_BOOKING_URL = "/admin-bookings.do";

		public final static String ADMIN_PROFILE_URL = "/admin-profile.do";
		public final static String VENDOR_PROFILE_URL = "/vendor-profile.do";

		public final static String MANAGE_ADMIN_SETTINGS_URL = "/manage-admin-settings.do";
		public final static String MANAGE_ADMIN_SMS_SETTINGS_URL = "/manage-admin-sms-sending.do";
		public final static String MANAGE_DYNAMIC_CARS_URL = "/manage-dynamic-cars.do";
		public final static String ADD_DYNAMIC_CAR_URL = "/add-dynamic-car.do";
		public final static String EDIT_DYNAMIC_CAR_URL = "/edit-dynamic-car.do";
		public final static String MANAGE_DYNAMIC_CARS_ACTIVATE_DEACTIVATE_URL = "/manage-dynamic-cars/active-deactive.do";

		public final static String MANAGE_ADMIN_USERS_URL = "/manage-admin-users.do";
		public final static String ADD_ADMIN_USERS_URL = "/add-admin-user.do";
		public final static String EDIT_ADMIN_USERS_URL = "/edit-admin-user.do";

		public final static String MANAGE_SUPER_SERVICES_URL = "/manage-super-services.do";
		public final static String ADD_SUPER_SERVICE_URL = "/add-super-service.do";
		public final static String EDIT_SUPER_SERVICE_URL = "/edit-super-service.do";
		public final static String MANAGE_SUPER_SERVICES_ACTIVATE_DEACTIVATE_URL = "/manage-super-services/active-deactive.do";

		public final static String MANAGE_CATEGORIES_URL = "/manage-categories.do";
		public final static String ADD_CATEGORIES_URL = "/add-category.do";
		public final static String EDIT_CATEGORIES_URL = "/edit-category.do";
		public final static String MANAGE_CATEGORIES_ACTIVATE_DEACTIVATE_URL = "/manage-categories/active-deactive.do";

		public final static String MANAGE_SUBCRIBERS_URL = "/manage-subscribers.do";

		public final static String MANAGE_VENDOR_FEEDS_URL = "/manage-vendor-feeds.do";
		public final static String MANAGE_VENDOR_FEEDS_DELETE_URL = "/manage-vendor-feeds/delete.do";
		public final static String ADD_VENDOR_FEEDS_URL = "/add-feed.do";
		public final static String MANAGE_VENDOR_FEEDS_REPOST_URL = "/manage-vendor-feeds/repost.do";

		public final static String MANAGE_VENDORS_URL = "/manage-vendors.do";

		public final static String MANAGE_CAR_URL = "/manage-cars.do";
		public final static String ADD_CAR_URL = "/add-car.do";
		public final static String EDIT_CAR_URL = "/edit-car.do";

		public final static String MANAGE_PASSENGER_URL = "/manage-passengers.do";
		public final static String PASSENGER_TOURS_URL = "/passengers/tours.do";
		public final static String MANAGE_DRIVER_SUBSCRIBERS_URL = "/manage-driver-subscribers.do";

		public final static String ADMIN_BOOKING_DETAILS_URL = "/admin-bookings-details.do";

		public final static String ANNOUNCEMENT_URL = "/announcements.do";

		public final static String ADMIN_BOOKINGS_URL = "/admin-bookings.do";

		public final static String MANAGE_DRIVER_URL = "/manage-drivers.do";
		public final static String ADD_DRIVER_URL = "/add-driver.do";
		public final static String EDIT_DRIVER_URL = "/edit-driver.do";
		public final static String DRIVER_TOURS_URL = "/driver/tours.do";

		public final static String SUBSCRIBE_PACKAGE_URL = "/subscribe-package.do";
		public final static String DRIVER_SUBSCRIPTION_EXTENSTION_URL = "/driver-subscription-extension.do";

		public final static String MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_URL = "/manage-driver-subscription-package-reports.do";

		public final static String MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_URL = "/manage-driver-transaction-history-reports.do";

		public final static String REPORTS_URL = "/manage-driver-reports.do";

		public final static String MANAGE_PRODUCTS_URL = "/manage-products.do";
		public final static String ADD_PRODUCTS_URL = "/add-product.do";
		public final static String EDIT_PRODUCTS_URL = "/edit-product.do";
		public final static String MANAGE_PRODUCTS_ACTIVE_DEACTIVE_URL = "/manage-products/active-deactive.do";

		public final static String VIEW_ORDER_DETAILS_URL = "/view-order.do";

		public final static String MANAGE_PROMO_CODE_URL = "/manage-promo-code.do";
		public final static String ADD_PROMO_CODE_URL = "/add-promo-code.do";
		public final static String EDIT_PROMO_CODE_URL = "/edit-promo-code.do";
		public final static String PROMO_CODE_DEACTIVATE_URL = "/manage-promo-code/deactivate-promo-code.do";
		public final static String PROMO_CODE_ACTIVATE_URL = "/manage-promo-code/activate-promo-code.do";
		public final static String PROMO_CODE_DELETE_URL = "/manage-promo-code/delete-promo-code.do";

		public final static String REFUND_URL = "/refund.do";

		public final static String BOOKING_URL = "/bookings.do";

		public final static String MANAGE_ADMIN_CONTACT_US_URL = "/manage-admin-contact-us.do";
		public final static String MANAGE_TERMS_CONDITIONS_URL = "/manage-terms-conditions.do";
		public final static String MANAGE_ABOUT_US_URL = "/manage-about-us.do";
		public final static String MANAGE_REFUND_POLICY_URL = "/manage-refund-policy.do";
		public final static String MANAGE_PRIVACY_POLICY_URL = "/manage-privacy-policy.do";
		public final static String MANAGE_DRIVER_WALLET_SETTINGS_URL = "/manage-driver-wallet/settings.do";
		public final static String MANAGE_ORDER_SETTINGS_URL = "/manage-order-settings.do";
		public final static String EDIT_ORDER_SETTINGS_URL = "/edit-order-settings.do";
		public final static String MANAGE_TAX_URL = "/manage-tax.do";
		public final static String ADD_TAX_URL = "/add-tax.do";
		public final static String EDIT_TAX_URL = "/edit-tax.do";
		public final static String MANAGE_TAX_ACTIVATE_DEACTIVATE_URL = "/manage-tax/active-deactive.do";
		public final static String MANAGE_TAX_DELETE_URL = "/manage-tax/delete-tax.do";

		public final static String MANAGE_RIDE_LATER_SETTINGS_URL = "/manage-ride-later-settings.do";

		public final static String MANAGE_DRIVER_BOOKINGS_URL = "/manage-driver-bookings.do";

		public final static String MANAGE_DRIVER_LOGGED_IN_TIME_REPORT_URL = "/driver/loggedin/time-report.do";
		public final static String DRIVER_DUTY_REPORTS_URL = "/driver-duty-reports.do";
		public final static String DRIVER_KM_DETAIL_REPORTS_URL = "/driver-km-detail-reports.do";

		public final static String DRIVER_REFER_BENEFITS_DETAIL_REPORTS_URL = "/driver/refer-benefit/details-reports.do";
		public final static String DRIVER_REFER_BENEFITS_TRIP_REPORTS_URL = "/driver/refer-benefit/trips-reports.do";

		public final static String MANAGE_VENDOR_URL = "/manage-vendors.do";
		public final static String ADD_VENDOR_URL = "/add-vendor.do";
		public final static String EDIT_VENDOR_URL = "/edit-vendor.do";
		public final static String MANAGE_SUB_VENDOR_URL = "/manage-sub-vendors.do";
		public final static String ADD_SUB_VENDOR_URL = "/add-sub-vendor.do";
		public final static String EDIT_SUB_VENDOR_URL = "/edit-sub-vendor.do";
		public final static String VENDOR_TOURS_URL = "/vendor/tours.do";
		public final static String MANAGE_VENDOR_DYNAMIC_CARS_URL = "/manage-vendor-dynamic-cars.do";
		public final static String MANAGE_VENDOR_AIRPORT_REGIONS_URL = "/manage-vendor-airport-regions.do";
		public final static String VENDOR_MONTHLY_SUBSCRIPTION_URL = "/vendor-monthly-subscription.do";
		public final static String MANAGE_VENDOR_SUBSCRIPTION_URL = "/manage-vendors/subscription.do";
		public final static String MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL = "/manage-vendor-monthly-subscription-history.do";
		public final static String MANAGE_VENDOR_STORE_URL = "/manage-vendor-store.do";
		public final static String EDIT_VENDOR_STORE_URL = "/edit-vendor-store.do";
		public final static String ADD_VENDOR_STORE_URL = "/add-vendor-store.do";
		public final static String MANAGE_VENDOR_STORE_ACTIVATE_DEACTIVATE_URL = "/manage-vendor-store/active-deactive.do";

		public final static String MANAGE_AIRPORT_REGION_URL = "/manage-airport-regions.do";
		public final static String MANAGE_VENDOR_AIRPORT_REGION_URL = "/manage-vendor-airport-regions.do";
		public final static String ADD_AIRPORT_REGION_URL = "/add-airport-region.do";
		public final static String EDIT_AIRPORT_REGION_URL = "/edit-airport-region.do";
		public final static String EDIT_VENDOR_AIRPORT_REGION_URL = "/edit-vendor-airport-region.do";
		public final static String MANAGE_AIRPORT_REGION_ACTIVATE_DEACTIVATE_URL = "/manage-airport-regions/activate-deactivate-airport-region.do";

		public final static String MANAGE_MULTICITY_URL = "/manage-multicity-settings-city.do";
		public final static String ADD_MULTICITY_URL = "/add-multicity-city.do";
		public final static String EDIT_MULTICITY_URL = "/edit-multicity-city.do";
		public final static String MANAGE_MULTICITY_ACTIVATE_DEACTIVATE_URL = "/manage-multicity-settings-city/active-deactive.do";
		public final static String MANAGE_MULTICITY_DELETE_URL = "/manage-multicity-settings-city/delete.do";

		public final static String MANAGE_SUBSCRIPTION_PACKAGE_URL = "/manage-subscription-packages.do";
		public final static String ADD_SUBSCRIPTION_PACKAGE_URL = "/add-subscription-package.do";
		public final static String EDIT_SUBSCRIPTION_PACKAGE_URL = "/edit-subscription-package.do";
		public final static String MANAGE_SUBSCRIPTION_PACKAGE_ACTIVATE_DEACTIVATE_URL = "/manage-subscription-packages/active-deactive.do";
		public final static String MANAGE_SUBSCRIPTION_PACKAGE_DELETE_URL = "/manage-subscription-packages/delete.do";

		public final static String MANAGE_SURGE_PRICE_URL = "/manage-surge-price.do";
		public final static String ADD_SURGE_PRICE_URL = "/add-surge-price.do";
		public final static String EDIT_SURGE_PRICE_URL = "/edit-surge-price.do";
		public final static String MANAGE_SURGE_PRICE_ACTIVATE_DEACTIVATE_URL = "/manage-surge-price/active-deactive.do";
		public final static String MANAGE_SURGE_PRICE_DELETE_URL = "/manage-surge-price/delete.do";

		public final static String MANAGE_RENTAL_PACKAGE_URL = "/manage-rental-packages.do";
		public final static String ADD_RENTAL_PACKAGE_URL = "/add-rental-package.do";
		public final static String EDIT_RENTAL_PACKAGE_URL = "/edit-rental-package.do";
		public final static String MANAGE_RENTAL_PACKAGE_ACTIVATE_DEACTIVATE_URL = "/manage-rental-packages/active-deactive.do";
		public final static String MANAGE_RENTAL_PACKAGE_DELETE_URL = "/manage-rental-packages/delete.do";

		public final static String MANAGE_TAKE_RIDE_URL = "/manage-take-ride.do";
		public final static String TAKE_RIDE_URL = "/manage-take-ride/take-ride.do";

		public final static String MANAGE_BOOK_LATER_URL = "/manage-ride-later.do";
		public final static String MANAGE_CRITICAL_BOOK_LATER_URL = "/manage-critical-ride-later.do";

		public final static String VIEW_COURIER_DETAILS_URL = "/view-courier-details.do";
		public final static String VENDOR_BOOKINGS_URL = "/vendor-bookings.do";

		public final static String MANAGE_VENDOR_CITY_SETTINGS_URL = "/manage-vendor-city-settings.do";
		public final static String EDIT_VENDOR_CITY_SETTINGS_URL = "/edit-vendor-city-settings.do";
		public final static String SET_VENDOR_CAR_PRIORITY_URL = "/set-vendor-car-priority.do";

		public final static String MANAGE_CITY_SURGE_URL = "/manage-city-surge.do";
		public final static String ADD_CITY_SURGE_URL = "/add-city-surge.do";
		public final static String EDIT_CITY_SURGE_URL = "/edit-city-surge.do";
		public final static String MANAGE_CITY_SURGE_DELETE_URL = "/manage-city-surge/delete.do";
		public final static String MANAGE_CITY_SURGE_ACTIVTAE_DEACTIVATE_URL = "/manage-city-surge/active-deactive.do";

		public final static String MANAGE_DRIVER_ACCOUNT_URL = "/manage-drivers/account.do";
		public final static String MANAGE_VENDOR_ACCOUNT_URL = "/manage-vendor/account.do";
		public final static String MANAGE_VENDOR_DRIVER_ACCOUNT_URL = "/manage-vendor-drivers/account.do";
		public final static String VENDOR_MY_ACCOUNT_URL = "/vendor/my-account.do";
		public final static String MANAGE_DRIVER_ACCOUNT_LOGS_URL = "/manage-drivers/account-logs.do";

		public final static String MANAGE_TRANSFERRED_ENCASH_REQUEST_URL = "/encash-requests/transferred.do";
		public final static String MANAGE_REJECTED_ENCASH_REQUEST_URL = "/encash-requests/rejected.do";
		public final static String MANAGE_APPROVED_ENCASH_REQUEST_URL = "/encash-requests/approved.do";
		public final static String MANAGE_HOLD_ENCASH_REQUEST_URL = "/encash-requests/hold.do";

		public final static String MANUAL_BOOKINGS_URL = "/manual-bookings.do";

		public final static String IMPORT_CSV_PRODUCTS_URL = "/import-csv-products.do";

		public final static String MANAGE_DRIVER_FARE_URL = "/manage-driver-fare.do";

		public final static String MANAGE_FEED_SETTINGS_URL = "/manage-feed-settings.do";
		public final static String ADD_FEED_SETTINGS_URL = "/add-feed-settings.do";
		public final static String EDIT_FEED_SETTINGS_URL = "/edit-feed-settings.do";

		public final static String MANAGE_VENDOR_FEATURE_FEEDS_URL = "/manage-vendor-feature-feeds.do";
		public final static String MANAGE_VENDOR_FEATURE_FEEDS_DELETE_URL = "/manage-vendor-feature-feeds/delete.do";
		public final static String ADD_VENDOR_FEATURE_FEEDS_URL = "/add-feature-feed.do";

		public final static String MANAGE_PRODUCT_CATEGORY_URL = "/manage-product-category.do";
		public final static String ADD_PRODUCT_CATEGORY_URL = "/add-product-category.do";
		public final static String EDIT_PRODUCT_CATEGORY_URL = "/edit-product-category.do";
		public final static String MANAGE_PRODUCT_CATEGORY_ACTIVE_DEACTIVE_URL = "/manage-product-category/active-deactive.do";

		public final static String MANAGE_PRODUCT_SUB_CATEGORY_URL = "/manage-product-sub-category.do";
		public final static String ADD_PRODUCT_SUB_CATEGORY_URL = "/add-product-sub-category.do";
		public final static String EDIT_PRODUCT_SUB_CATEGORY_URL = "/edit-product-sub-category.do";
		public final static String MANAGE_PRODUCT_SUB_CATEGORY_ACTIVE_DEACTIVE_URL = "/manage-product-sub-category/active-deactive.do";

		public final static String MANAGE_BRAND_URL = "/manage-brands.do";
		public final static String ADD_BRAND_URL = "/add-brand.do";
		public final static String EDIT_BRAND_URL = "/edit-brand.do";
		public final static String MANAGE_BRAND_ACTIVE_DEACTIVE_URL = "/manage-brands/active-deactive.do";
		public final static String MANAGE_BRAND_APPROVE_REJECT_URL = "/manage-brands/approve-reject.do";

		public final static String MANAGE_UNIT_OF_MEASURE_URL = "/manage-uoms.do";
		public final static String ADD_UNIT_OF_MEASURE_URL = "/add-uom.do";
		public final static String EDIT_UNIT_OF_MEASURE_URL = "/edit-uom.do";
		public final static String MANAGE_UNIT_OF_MEASURE_ACTIVE_DEACTIVE_URL = "/manage-uoms/active-deactive.do";

		public final static String MANAGE_PRODUCT_TEMPLATE_URL = "/manage-product-templates.do";
		public final static String ADD_PRODUCT_TEMPLATE_URL = "/add-product-template.do";
		public final static String EDIT_PRODUCT_TEMPLATE_URL = "/edit-product-template.do";
		public final static String MANAGE_PRODUCT_TEMPLATE_ACTIVE_DEACTIVE_URL = "/manage-product-templates/active-deactive.do";

		public final static String MANAGE_PRODUCT_VARIANT_URL = "/manage-product-variants.do";
		public final static String ADD_PRODUCT_VARIANT_URL = "/add-product-variant.do";
		public final static String EDIT_PRODUCT_VARIANT_URL = "/edit-product-variant.do";
		public final static String MANAGE_PRODUCT_VARIANT_ACTIVE_DEACTIVE_URL = "/manage-product-variants/active-deactive.do";

		public final static String MANAGE_PRODUCT_IMAGE_URL = "/manage-product-images.do";
		public final static String ADD_PRODUCT_IMAGE_URL = "/add-product-image.do";
		public final static String EDIT_PRODUCT_IMAGE_URL = "/edit-product-image.do";
		public final static String MANAGE_PRODUCT_IMAGE_ACTIVE_DEACTIVE_URL = "/manage-product-images/active-deactive.do";

		public final static String MANAGE_VENDOR_PRODUCTS_URL = "/manage-vendor-products.do";
		public final static String ADD_VENDOR_PRODUCTS_URL = "/add-vendor-product.do";
		public final static String EDIT_VENDOR_PRODUCTS_URL = "/edit-vendor-product.do";
		public final static String MANAGE_VENDOR_PRODUCTS_ACTIVE_DEACTIVE_URL = "/manage-vendor-products/active-deactive.do";

		public final static String MANAGE_DELIVERY_TYPE_URL = "/manage-delivery-types.do";
		public final static String ADD_DELIVERY_TYPE_URL = "/add-delivery-type.do";
		public final static String EDIT_DELIVERY_TYPE_URL = "/edit-delivery-type.do";
		public final static String MANAGE_DELIVERY_TYPE_ACTIVE_DEACTIVE_URL = "/manage-delivery-types/active-deactive.do";

		public final static String MANAGE_APPOINTMENT_SETTINGS_URL = "/manage-appointment-settings.do";
		public final static String EDIT_APPOINTMENT_SETTINGS_URL = "/edit-appointment-settings.do";
		public final static String MANAGE_APPOINTMENT_URL = "/manage-appointments.do";
		public final static String VIEW_APPOINMENTS_DETAILS_URL = "/view-appointment.do";
		
		public final static String LED_STORE_DETAILS_URL = "/led-store-details.do";
		public final static String SEND_LED_STORE_DETAILS_URL = "/led-store-details.do";
		
		public final static String MANAGE_ADS_URL = "/manage-ads.do";
		public final static String ADD_AD_URL = "/add-ad.do";
		public final static String VIEW_AD_DETAILS_URL = "/view-ad.do";
		
		public final static String MANAGE_CAMPAIGNS_URL = "/manage-campaigns.do";
		public final static String ADD_CAMPAIGN_URL = "/add-campaign.do";
		
		public final static String ADD_RACK_URL = "/add-rack.do";
		
		public final static String MANAGE_RACK_CATEGORY_URL = "/manage-rack-categories.do";
		public final static String ADD_RACK_CATEGORY_URL = "/add-rack-category.do";
		public final static String EDIT_RACK_CATEGORY_URL = "/edit-rack-category.do";
		public final static String MANAGE_RACK_CATEGORY_ACTIVE_DEACTIVE_URL = "/manage-rack-category/active-deactive.do";
		
		public final static String MANAGE_RACK_SLOT_BOOKING_URL = "/manage-rack-slot-booking.do";
		
		public final static String MANAGE_BUSINESS_INTERESTED_USERS_URL = "/manage-business-interested-users.do";
		
		public final static String QR_CODE_PROFILES_URL = "/qr-code-profiles.do";
		public final static String ADD_QR_CODE_PROFILE_URL = "/add-qr-code-profile.do";
		
		public final static String MANAGE_WAREHOUSE_USERS_URL = "/manage-warehouse-users.do";
		
		public final static String MANAGE_ERP_USERS_URL = "/manage-erp-users.do";
		public final static String ADD_ERP_USER_URL = "/add-erp-user.do";
		public final static String EDIT_ERP_USER_URL = "/edit-erp-user.do";
		public final static String MANAGE_BRAND_ASSOCIATION_URL = "/manage-brand-association.do";
		public final static String ADD_BRAND_ASSOCIATION_URL = "/add-brand-association.do";
		public final static String EDIT_BRAND_ASSOCIATION_URL = "/edit-brand-association.do";
		public final static String MANAGE_BRAND_ASSOCIATION_ACTIVATE_DEACTIVATE_URL = "/manage-brand-association/active-deactive.do";
		public static final String BRAND_LOGIN_URL = "/brand/login.do";
		
		public final static String MANAGE_ERP_EMPLOYEES_URL = "/manage-erp-employees.do";
		public final static String ADD_ERP_EMPLOYEE_URL = "/add-erp-employee.do";
		public final static String EDIT_ERP_EMPLOYEE_URL = "/edit-erp-employee.do";
		public final static String MANUALBOOKING_ADDPRODUCTS_URL ="/manual-booking-add-products.do";//added
		public final static String  MANAGE_CUSTOM_FEEDS ="/manage-custom-feeds.do";//added
		public final static String MANAGE_LOW_INVENTORY = "/manage-low-inventory.do";//added
		public final static String MANAGE_RE_INVENTORY= "/manage-reinventory.do";//added
		public final static String MANAGE_ALL_BRANDS = "/manage-allbrands.do";//added
		public final static String MANAGE_ADD_NEW_BRANDS ="/manage-add-newbrands.do";//added
		public final static String MANAGE_MANUFACTURER_ORDER_REQUEST = "/manufacturer-order-request.do";//added
		public final static String MANAGE_MANUFACTURER_ORDER_REQUEST_STATUS = "/manufacturer-order-request-status.do";//added
		
		public final static String MANAGE_CLOUD_WAREHOUSE ="/manage-cloud-warehouse.do";//added
	
		public final static String MANAGE_MARKET_SHARES = "//manage-market-Shares.do";//added
		public final static String MANAGE_TOTAL_STORES = "/manage-total-stores.do"; //added
		public final static String MANAGE_OWN_STORES ="/manage-own-stores.do";//added
		
		 // Service-related URLs
	    public final static String MANAGE_SERVICES = "/manage-services.do";
	    public final static String MANAGE_ONBOARDING = "/manage-onboarding.do";
	    public final static String MANAGE_LOGISTICS = "/manage-logistics.do";
	    public final static String MANAGE_MARKET_SURVEY = "/manage-market-survey.do";
	    public final static String MANAGE_MARKET_SHARE = "/manage-market-share.do";
	    public final static String MANAGE_RETAIL_SUPPLY = "/manage-retail-supply.do";
	    public final static String MANAGE_MARKETING = "/manage-demand.do";  
	    public final static String MANAGE_INVENTORY = "/manage-inventory.do";
	    //ACTION-RELATED URLS
	    public final static String MANAGE_SERVICES_URL = "/manage-services.do";
	    public final static String MANAGE_ONBOARDING_URL = "/manage-onboarding.do";
	    public final static String MANAGE_LOGISTICS_URL = "/manage-logistics.do";
	    public final static String MANAGE_MARKET_SURVEY_URL = "/manage-market-survey.do";
	    public final static String MANAGE_MARKET_SHARE_URL = "/manage-market-share.do";
	    public final static String MANAGE_RETAIL_SUPPLY_URL= "/manage-retail-supply.do";
	    public final static String MANAGE_MARKETING_URL = "/manage-demand.do";  
	    public final static String MANAGE_INVENTORY_URL = "/manage-inventory.do";
	    public final static String MANAGE_USERS_URL = "/manage-users.do";

	    
	    
	}

	public class JSP_URLS {

		public final static String LOGIN_JSP = "/new-theme-login.jsp";
		public final static String VENDOR_LOGIN_JSP = "/new-theme-vendor-login.jsp";
		public final static String ERROR_JSP = "/new-theme-error.jsp";
		public final static String FORGOT_PASSWORD_JSP = "/new-theme-forgot-password.jsp";
		public final static String USER_LOGIN_OTP_JSP = "/secure/user-login-otp/user-login-otp.jsp";

		public final static String ADMIN_PROFILE_JSP = "/secure/admin-user/admin-profile.jsp";
		public final static String ADMIN_PROFILE_ICON = "far fa-user-circle fa-fw me-2 text-theme";
		public final static String VENDOR_PROFILE_JSP = "/secure/vendor/vendor-profile.jsp";
		public final static String VENDOR_PROFILE_ICON = "fas fa-universal-access fa-fw me-2 text-theme";

		public final static String MANAGE_ADMIN_SETTINGS_JSP = "/secure/settings/manage-admin-settings.jsp";
		public final static String MANAGE_ADMIN_SETTINGS_ICON = "fas fa-cogs fa-fw me-2 text-theme";
		public final static String MANAGE_ADMIN_SMS_SETTINGS_JSP = "/secure/settings/manage-admin-sms-sending.jsp";
		public final static String MANAGE_DYNAMIC_CARS_JSP = "/secure/settings/manage-dynamic-cars.jsp";
		public final static String ADD_DYNAMIC_CAR_JSP = "/secure/settings/add-dynamic-car.jsp";
		public final static String EDIT_DYNAMIC_CAR_JSP = "/secure/settings/edit-dynamic-car.jsp";

		public final static String MANAGE_ADMIN_USER_JSP = "/secure/admin-user/manage-admin-user.jsp";
		public final static String MANAGE_ADMIN_USER_ICON = "fas fa-users fa-fw me-2 text-theme";
		public final static String ADD_ADMIN_USER_JSP = "/secure/admin-user/add-admin-user.jsp";
		public final static String EDIT_ADMIN_USER_JSP = "/secure/admin-user/edit-admin-user.jsp";

		public final static String MANAGE_SUPER_SERVICES_JSP = "/secure/super-services/manage-super-services.jsp";
		public final static String MANAGE_SUPER_SERVICES_ICON = "fas fa-paper-plane fa-fw me-2 text-theme";
		public final static String ADD_SUPER_SERVICE_JSP = "/secure/super-services/add-super-service.jsp";
		public final static String EDIT_SUPER_SERVICE_JSP = "/secure/super-services/edit-super-service.jsp";

		public final static String MANAGE_CATEGORIES_JSP = "/secure/categories/manage-categories.jsp";
		public final static String MANAGE_CATEGORIES_ICON = "fas fa-sitemap fa-fw me-2 text-theme";
		public final static String ADD_CATEGORIES_JSP = "/secure/categories/add-category.jsp";
		public final static String EDIT_CATEGORIES_JSP = "/secure/categories/edit-category.jsp";

		public final static String MANAGE_SUBSCRIBERS_JSP = "/secure/vendor-subscribers/manage-subscribers.jsp";
		public final static String MANAGE_SUBSCRIBERS_ICON = "fas fa-user-plus fa-fw me-2 text-theme";

		public final static String MANAGE_VENDORS_ICON = "fas fa-universal-access fa-fw me-2 text-theme";
		public final static String MANAGE_BRAND_ICON="fa-regular fa-circle-bookmark";

		public final static String MANAGE_VENDOR_FEEDS_JSP = "/secure/vendor-feeds/manage-vendor-feeds.jsp";
		public final static String MANAGE_VENDOR_FEEDS_ICON = "fas fa-bell fa-fw me-2 text-theme";
		public final static String ADD_VENDOR_FEEDS_JSP = "/secure/vendor-feeds/add-feed.jsp";
		public final static String MANAGE_CUSTOM_FEEDS_JSP="/secure/vendor-feeds/manage-custom-feeds.jsp";//added
		public final static String MANAGE_LOW_INVENTORY_JSP ="/secure/vendor-feeds/manage-low-inventory.jsp";//added
		public final static String MANAGE_RE_INVENTORY_JSP ="/secure/vendor-feeds/manage-reinventory.jsp";//added
		public final static String MANAGE_ALL_BRANDS_JSP="/secure/vendor-feeds/manage-allbrands.jsp";//added
        public final static String MANAGE_ADD_NEW_BRANDS_JSP = "/secure/vendor-feeds/manage-add-newbrands.jsp";//added
        public final static String MANAGE_MANUFACTURER_ORDER_REQUEST_JSP = "/secure/vendor-feeds/manufacturer-order-request.jsp";//added
        public final static String MANAGE_MANUFACTURER_ORDER_REQUEST_STATUS_JSP = "/secure/vendor-feeds/manage-manufacturer-order-request-status.jsp";//added
             
        
		public final static String MANAGE_CAR_JSP = "/secure/car/manage-car.jsp";
		public final static String MANAGE_CAR_ICON = "fas fa-taxi fa-fw me-2 text-theme";
		public final static String ADD_CAR_JSP = "/secure/car/add-car.jsp";
		public final static String EDIT_CAR_JSP = "/secure/car/edit-car.jsp";

		public final static String MANAGE_PASSENGER_JSP = "/secure/passenger/manage-passenger.jsp";
		public final static String MANAGE_PASSENGER_ICON = "fas fa-user fa-fw me-2 text-theme";

		public final static String PASSENGER_TOUR_JSP = "/secure/passenger/passenger-tours.jsp";
		public final static String BOOKINGS_ICON = "fas fa-history fa-fw me-2 text-theme";

		public final static String MANAGE_DRIVER_SUBSCRIBERS_JSP = "/secure/passenger/manage-driver-subscribers.jsp";

		public final static String ANNOUNCEMENT_JSP = "/secure/announcement/manage-announcement.jsp";
		public final static String ANNOUNCEMENT_ICON = "fas fa-bullhorn fa-fw me-2 text-theme";

		public final static String ADMIN_BOOKINGS_JSP = "/secure/admin-bookings/manage-admin-bookings.jsp";
		public final static String ADMIN_BOOKINGS_ICON = "fas fa-history fa-fw me-2 text-theme";
		public final static String VENDOR_BOOKINGS_JSP = "/secure/vendor-bookings/manage-vendor-bookings.jsp";
		public final static String VENDOR_BOOKINGS_ICON = "fas fa-history fa-fw me-2 text-theme";

		public final static String MANAGE_DRIVER_JSP = "/secure/driver/manage-driver.jsp";
		public final static String MANAGE_DRIVER_ICON = "fas fa-user-secret fa-fw me-2 text-theme";
		public final static String ADD_DRIVER_JSP = "/secure/driver/add-driver.jsp";
		public final static String EDIT_DRIVER_JSP = "/secure/driver/edit-driver.jsp";

		public final static String SUBSCRIBE_PACKAGE_JSP = "/secure/subscription-packages/subscribe-package.jsp";
		public final static String SUBSCRIBE_PACKAGE_ICON = "fas fa-dollar-sign fa-fw me-2 text-theme";

		public final static String DRIVER_SUBSCRIPTION_EXTENSTION_JSP = "/secure/subscription-packages/driver-subscription-extension.jsp";

		public final static String MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_JSP = "/secure/subscription-packages/manage-driver-subscription-package-reports.jsp";
		public final static String MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_ICON = "fas fa-book fa-fw me-2 text-theme";

		public final static String MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_JSP = "/secure/admin-reports/manage-driver-transaction-history-reports.jsp";
		public final static String MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_ICON = "fas fa-history fa-fw me-2 text-theme";

		public final static String DRIVER_TOUR_JSP = "/secure/driver/driver-tours.jsp";

		public final static String MANAGE_PRODUCT_JSP = "/secure/vendor-products/manage-products.jsp";
		public final static String MANAGE_PRODUCT_ICON = "fas fa-shopping-cart fa-fw me-2 text-theme";
		public final static String ADD_PRODUCT_JSP = "/secure/vendor-products/add-product.jsp";
		public final static String EDIT_PRODUCT_JSP = "/secure/vendor-products/edit-product.jsp";

		public final static String MANAGE_ORDERS_NEW_JSP = "/secure/vendor-orders/manage-orders-new.jsp";
		public final static String MANAGE_ORDERS_ACTIVE_JSP = "/secure/vendor-orders/manage-orders-active.jsp";
		public final static String MANAGE_ORDERS_ALL_OTHERS_JSP = "/secure/vendor-orders/manage-orders-all-others.jsp";
		public final static String MANAGE_COURIER_HISTORY_JSP = "/secure/vendor-orders/manage-courier-history.jsp";
		public final static String MANAGE_COURIER_HISTORY_ICON = "fas fa-history fa-fw me-2 text-theme";
		public final static String VIEW_ORDERS_JSP = "/secure/vendor-orders/view-order.jsp";
		public final static String VIEW_COURIER_DETAILS_JSP = "/secure/vendor-orders/view-courier-details.jsp";

		public final static String MANAGE_PROMO_CODE_JSP = "/secure/settings/manage-promo-code.jsp";
		public final static String MANAGE_PROMO_CODE_ICON = "fas fa-qrcode fa-fw me-2 text-theme";
		public final static String ADD_PROMO_CODE_JSP = "/secure/settings/add-promo-code.jsp";
		public final static String EDIT_PROMO_CODE_JSP = "/secure/settings/edit-promo-code.jsp";

		public final static String REFUND_JSP = "/secure/refund/manage-refund.jsp";
		public final static String REFUND_ICON = "fas fa-money-bill-alt fa-fw me-2 text-theme";

		public final static String MANAGE_BOOKINGS_JSP = "/secure/booking/manage-booking.jsp";
		public final static String MANAGE_BOOKINGS_ICON = "fab fa-first-order fa-fw me-2 text-theme";

		public final static String MANAGE_ADMIN_CONTACT_US_JSP = "/secure/settings/manage-admin-contact-us.jsp";
		public final static String MANAGE_TERMS_CONDITIONS_JSP = "/secure/settings/manage-terms-conditions.jsp";
		public final static String MANAGE_ABOUT_US_JSP = "/secure/settings/manage-about-us.jsp";
		public final static String MANAGE_REFUND_POLICY_JSP = "/secure/settings/manage-refund-policy.jsp";
		public final static String MANAGE_PRIVACY_POLICY_JSP = "/secure/settings/manage-privacy-policy.jsp";
		public final static String MANAGE_DRIVER_WALLET_SETTINGS_JSP = "/secure/settings/manage-driver-wallet-settings.jsp";

		public final static String MANAGE_ORDER_SETTINGS_JSP = "/secure/settings/manage-order-settings.jsp";
		public final static String EDIT_ORDER_SETTINGS_JSP = "/secure/settings/edit-order-settings.jsp";
		public final static String MANAGE_TAX_JSP = "/secure/settings/manage-tax.jsp";
		public final static String ADD_TAX_JSP = "/secure/settings/add-tax.jsp";
		public final static String EDIT_TAX_JSP = "/secure/settings/edit-tax.jsp";

		public final static String MANAGE_RIDE_LATER_SETTINGS_JSP = "/secure/settings/manage-ride-later-settings.jsp";

		public final static String MANAGE_REPORTS_ICON = "fas fa-file-pdf fa-fw me-2 text-theme";
		public final static String MANAGE_DRIVER_REPORTS_JSP = "/secure/admin-reports/manage-driver-reports.jsp";
		public final static String MANAGE_DRIVER_BOOKINGS_JSP = "/secure/admin-reports/manage-driver-bookings.jsp";

		public final static String MANAGE_DRIVER_DUTY_REPORTS_JSP = "/secure/admin-reports/manage-driver-duty-reports.jsp";
		public final static String DRIVER_DUTY_REPORTS_JSP = "/secure/admin-reports/driver-duty-reports.jsp";
		public final static String DRIVER_LOGGED_IN_TIME_REPORT_JSP = "/secure/admin-reports/driver-loggedin-time-report.jsp";

		public final static String MANAGE_DRIVER_DRIVER_REPORTS_JSP = "/secure/admin-reports/manage-drivers-drive-reports.jsp";
		public final static String MANAGE_REFUND_REPORTS_JSP = "/secure/admin-reports/manage-refund-reports.jsp";
		public final static String MANAGE_CCAVENUE_LOGS_REPORTS_JSP = "/secure/admin-reports/manage-ccavenue-logs-reports.jsp";
		public final static String DRIVER_DRIVE_DETAIL_REPORTS_JSP = "/secure/admin-reports/driver-drive-detail-report.jsp";
		public final static String DRIVER_REFER_BENEFITS_DETAIL_REPORTS_JSP = "/secure/admin-reports/driver-refer-benefit-details-report.jsp";
		public final static String DRIVER_REFER_BENEFITS_REPORTS_JSP = "/secure/admin-reports/driver-refer-benefit-report.jsp";
		public final static String DRIVER_REFER_BENEFITS_TRIP_REPORTS_JSP = "/secure/admin-reports/driver-refer-benefit-trips-report.jsp";
		public final static String DRIVER_REFER_BENEFITS_REPORTS_ICON = "fas fa-gift fa-fw me-2 text-theme";

		public final static String MANAGE_VENDOR_JSP = "/secure/vendor/manage-vendor.jsp";
		public final static String MANAGE_VENDOR_ICON = "fas fa-universal-access fa-fw me-2 text-theme";
		public final static String ADD_VENDOR_JSP = "/secure/vendor/add-vendor.jsp";
		public final static String EDIT_VENDOR_JSP = "/secure/vendor/edit-vendor.jsp";
		public final static String MANAGE_SUB_VENDOR_JSP = "/secure/vendor/manage-sub-vendor.jsp";
		public final static String MANAGE_SUB_VENDOR_ICON = "fas fa-child fa-fw me-2 text-theme";
		public final static String ADD_SUB_VENDOR_JSP = "/secure/vendor/add-sub-vendor.jsp";
		public final static String EDIT_SUB_VENDOR_JSP = "/secure/vendor/edit-sub-vendor.jsp";
		public final static String TRANSPORTATION_ICON = "fas fa-taxi fa-fw me-2 text-theme";
		public final static String COURIER_ICON = "fas fa-bus fa-fw me-2 text-theme";
		public final static String ECOMMERCE_ICON = "fas fa-shopping-cart fa-fw me-2 text-theme";
		public final static String VENDOR_STORE_LOCATION_ICON = "far fa-building fa-fw me-2 text-theme";
		public final static String VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_ICON = "fas fa-history fa-fw me-2 text-theme";
		public final static String VENDOR_MONTHLY_SUBSCRIPTION_ICON = "fas fa-dollar-sign fa-fw me-2 text-theme";
		public final static String VENDOR_TOURS_JSP = "/secure/vendor/vendor-tours.jsp";
		public final static String MANAGE_VENDOR_DYNAMIC_JSP = "/secure/vendor-settings/manage-vendor-dynamic-cars.jsp";
		public final static String SET_VENDOR_CAR_PRIORITY_JSP = "/secure/vendor-settings/set-vendor-car-priority.jsp";
		public final static String MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_JSP = "/secure/vendor/manage-vendor-monthly-subscription-history.jsp";
		public final static String VENDOR_MONTHLY_SUBSCRIPTION_JSP = "/secure/vendor/vendor-monthly-subscription.jsp";
		public final static String MANAGE_VENDOR_STORE_JSP = "/secure/vendor-stores/manage-vendor-store.jsp";
		public final static String ADD_VENDOR_STORE_JSP = "/secure/vendor-stores/add-vendor-store.jsp";
		public final static String EDIT_VENDOR_STORE_JSP = "/secure/vendor-stores/edit-vendor-store.jsp";
		public final static String  MANAGE_CLOUD_WAREHOUSE_JSP ="/secure/vendor-stores/manage-cloud-warehouse.jsp";//added

		public final static String MANAGE_AIRPORT_JSP = "/secure/airport/manage-airport-region.jsp";
		public final static String MANAGE_AIRPORT_ICON = "fas fa-plane fa-fw me-2 text-theme";
		public final static String ADD_AIRPORT_JSP = "/secure/airport/add-airport-region.jsp";
		public final static String EDIT_AIRPORT_JSP = "/secure/airport/edit-airport-region.jsp";
		public final static String MANAGE_VENDOR_AIRPORT_JSP = "/secure/vendor-settings-airport/manage-vendor-airport-region.jsp";
		public final static String EDIT_VENDOR_AIRPORT_JSP = "/secure/vendor-settings-airport/edit-vendor-airport-region.jsp";

		public final static String MANAGE_MULTICITY_JSP = "/secure/multicity/manage-multicity-settings-city.jsp";
		public final static String MANAGE_MULTICITY_ICON = "far fa-map fa-fw me-2 text-theme";
		public final static String ADD_MULTICITY_JSP = "/secure/multicity/add-multicity-city.jsp";
		public final static String EDIT_MULTICITY_JSP = "/secure/multicity/edit-multicity-city.jsp";

		public final static String MANAGE_SUBSCRIPTION_PACKAGE_JSP = "/secure/subscription-packages/manage-subscription-packages.jsp";
		public final static String MANAGE_SUBSCRIPTION_PACKAGE_ICON = "far fa-plus-square fa-fw me-2 text-theme";
		public final static String ADD_SUBSCRIPTION_PACKAGE_JSP = "/secure/subscription-packages/add-subscription-package.jsp";
		public final static String EDIT_SUBSCRIPTION_PACKAGE_JSP = "/secure/subscription-packages/edit-subscription-package.jsp";

		public final static String MANAGE_SURGE_PRICE_JSP = "/secure/settings/manage-surge-price.jsp";
		public final static String MANAGE_SURGE_PRICE_ICON = "fas fa-arrow-alt-circle-up fa-fw me-2 text-theme";
		public final static String ADD_SURGE_PRICE_JSP = "/secure/settings/add-surge-price.jsp";
		public final static String EDIT_SURGE_PRICE_JSP = "/secure/settings/edit-surge-price.jsp";

		public final static String MANAGE_RENTAL_PACKAGE_JSP = "/secure/rentalpackage/manage-rental-packages.jsp";
		public final static String MANAGE_RENTAL_PACKAGE_ICON = "fas fa-car fa-fw me-2 text-theme";
		public final static String ADD_RENTAL_PACKAGE_JSP = "/secure/rentalpackage/add-rental-package.jsp";
		public final static String EDIT_RENTAL_PACKAGE_JSP = "/secure/rentalpackage/edit-rental-package.jsp";

		public final static String MANAGE_TAKE_RIDE_JSP = "/secure/ride-later/manage-take-ride.jsp";
		public final static String MANAGE_TAKE_RIDE_ICON = "fas fa-book fa-fw me-2 text-theme";
		public final static String RIDE_LATER_DRIVER_ASSIGNMENT_JSP = "/secure/ride-later/ride-later-driver-assignment.jsp";

		public final static String MANAGE_BOOK_LATER_JSP = "/secure/ride-later/manage-ride-later.jsp";
		public final static String MANAGE_BOOK_LATER_ICON = "fas fa-clock fa-fw me-2 text-theme";
		public final static String MANAGE_CRITICAL_BOOK_LATER_JSP = "/secure/ride-later/manage-critical-ride-later.jsp";
		public final static String MANAGE_CRITICAL_BOOK_LATER_ICON = "fas fa-hourglass-start fa-fw me-2 text-theme";

		public final static String MANAGE_VENDOR_CITY_SETTINGS_JSP = "/secure/vendor-city/manage-vendor-city-settings.jsp";
		public final static String EDIT_VENDOR_CITY_SETTINGS_JSP = "/secure/vendor-city/edit-vendor-city-settings.jsp";

		public final static String MANAGE_CITY_SURGE_JSP = "/secure/settings/manage-city-surge.jsp";
		public final static String MANAGE_CITY_SURGE_ICON = "fas fa-arrow-alt-circle-up fa-fw me-2 text-theme";
		public final static String ADD_CITY_SURGE_JSP = "/secure/settings/add-city-surge.jsp";
		public final static String EDIT_CITY_SURGE_JSP = "/secure/settings/edit-city-surge.jsp";

		public final static String MANAGE_DRIVER_ACCOUNT_JSP = "/secure/driver/account/manage-driver-account.jsp";
		public final static String MANAGE_DRIVER_ACCOUNT_ICON = "fas fa-user-secret fa-fw me-2 text-theme";

		public final static String MANAGE_VENDOR_ACCOUNT_JSP = "/secure/vendor/account/manage-vendor-account.jsp";
		public final static String MANAGE_VENDOR_ACCOUNT_ICON = "fas fa-universal-access fa-fw me-2 text-theme";

		public final static String MANAGE_VENDOR_DRIVER_ACCOUNT_JSP = "/secure/vendor-driver/account/manage-vendor-driver-account.jsp";

		public final static String MANAGE_VENDOR_MY_ACCOUNT_JSP = "/secure/vendor/account/vendor-my-account.jsp";
		public final static String MANAGE_VENDOR_MY_ACCOUNT_ICON = "fas fa-universal-access fa-fw me-2 text-theme";

		public final static String MANAGE_TRANSFERRED_ENCASH_REQUEST_JSP = "/secure/encashrequests/manage-transferred-encash-request.jsp";
		public final static String MANAGE_TRANSFERRED_ENCASH_REQUEST_ICON = "fas fa-exchange-alt fa-fw me-2 text-theme";

		public final static String MANAGE_REJECTED_ENCASH_REQUEST_JSP = "/secure/encashrequests/manage-rejected-encash-request.jsp";
		public final static String MANAGE_REJECTED_ENCASH_REQUEST_ICON = "fas fa-times-circle fa-fw me-2 text-theme";

		public final static String MANAGE_APPROVED_ENCASH_REQUEST_JSP = "/secure/encashrequests/manage-approved-encash-request.jsp";
		public final static String MANAGE_APPROVED_ENCASH_REQUEST_ICON = "fas fa-check fa-fw me-2 text-theme";

		public final static String MANAGE_HOLD_ENCASH_REQUEST_JSP = "/secure/encashrequests/manage-hold-encash-request.jsp";
		public final static String MANAGE_HOLD_ENCASH_REQUEST_ICON = "fas fa-exclamation-circle fa-fw me-2 text-theme";

		public final static String MANAGE_DRIVER_ACCOUNT_LOGS_JSP = "/secure/driver/account/manage-driver-account-logs.jsp";
		public final static String MANAGE_DRIVER_ACCOUNT_LOGS_ICON = "fas fa-download fa-fw me-2 text-theme";

		public final static String ADMIN_BOOKING_DETAILS_JSP = "/secure/admin-bookings/admin-bookings-details.jsp";

		public final static String IMPORT_CSV_PRODUCTS_JSP = "/secure/vendor-products/import-csv-products.jsp";

		public final static String MANAGE_DRIVER_FARE_JSP = "/secure/settings/manage-driver-fare.jsp";

		public final static String MANAGE_FEED_SETTINGS_JSP = "/secure/settings/manage-feed-settings.jsp";
		public final static String ADD_FEED_SETTINGS_JSP = "/secure/settings/add-feed-settings.jsp";
		public final static String EDIT_FEED_SETTINGS_JSP = "/secure/settings/edit-feed-settings.jsp";

		public final static String MANAGE_VENDOR_FEATURE_FEEDS_JSP = "/secure/vendor-feature-feeds/manage-vendor-feature-feeds.jsp";
		public final static String ADD_VENDOR_FEATURE_FEEDS_JSP = "/secure/vendor-feature-feeds/add-feature-feed.jsp";

		public final static String MANAGE_PRODUCT_CATEGORY_JSP = "/secure/master-data/manage-product-category.jsp";
		public final static String ADD_PRODUCT_CATEGORY_JSP = "/secure/master-data/add-product-category.jsp";
		public final static String EDIT_PRODUCT_CATEGORY_JSP = "/secure/master-data/edit-product-category.jsp";

		public final static String MANAGE_PRODUCT_SUB_CATEGORY_JSP = "/secure/master-data/manage-product-sub-category.jsp";
		public final static String ADD_PRODUCT_SUB_CATEGORY_JSP = "/secure/master-data/add-product-sub-category.jsp";
		public final static String EDIT_PRODUCT_SUB_CATEGORY_JSP = "/secure/master-data/edit-product-sub-category.jsp";

		public final static String MANAGE_BRAND_JSP = "/secure/master-data/manage-brands.jsp";
		public final static String ADD_BRAND_JSP = "/secure/master-data/add-brand.jsp";
		public final static String EDIT_BRAND_JSP = "/secure/master-data/edit-brand.jsp";

		public final static String MANAGE_UNIT_OF_MEASURE_JSP = "/secure/master-data/manage-unit-of-measures.jsp";
		public final static String ADD_UNIT_OF_MEASURE_JSP = "/secure/master-data/add-unit-of-measure.jsp";
		public final static String EDIT_UNIT_OF_MEASURE_JSP = "/secure/master-data/edit-unit-of-measure.jsp";

		public final static String MANAGE_PRODUCT_TEMPLATE_JSP = "/secure/master-data/manage-product-templates.jsp";
		public final static String ADD_PRODUCT_TEMPLATE_JSP = "/secure/master-data/add-product-template.jsp";
		public final static String EDIT_PRODUCT_TEMPLATE_JSP = "/secure/master-data/edit-product-template.jsp";

		public final static String MANAGE_PRODUCT_VARIANT_JSP = "/secure/master-data/manage-product-variants.jsp";
		public final static String ADD_PRODUCT_VARIANT_JSP = "/secure/master-data/add-product-variant.jsp";
		public final static String EDIT_PRODUCT_VARIANT_JSP = "/secure/master-data/edit-product-variant.jsp";

		public final static String MANAGE_PRODUCT_IMAGE_JSP = "/secure/master-data/manage-product-images.jsp";
		public final static String ADD_PRODUCT_IMAGE_JSP = "/secure/master-data/add-product-image.jsp";
		public final static String EDIT_PRODUCT_IMAGE_JSP = "/secure/master-data/edit-product-image.jsp";

		public final static String MANAGE_VENDOR_PRODUCT_JSP = "/secure/vendor-products/manage-vendor-products.jsp";
		public final static String ADD_VENDOR_PRODUCT_JSP = "/secure/vendor-products/add-vendor-product.jsp";
		public final static String EDIT_VENDOR_PRODUCT_JSP = "/secure/vendor-products/edit-vendor-product.jsp";

		public final static String MANAGE_APPOINTMENT_SETTINGS_JSP = "/secure/settings/manage-appointment-settings.jsp";
		public final static String MANAGE_APPOINTMENT_SETTINGS_ICON = "fas fa-cogs fa-fw me-2 text-theme";
		public final static String EDIT_APPOINTMENT_SETTINGS_JSP = "/secure/settings/edit-appointment-settings.jsp";

		public final static String MANAGE_APPOINTMENTS_NEW_JSP = "/secure/vendor-appointments/manage-appointments-new.jsp";
		public final static String MANAGE_APPOINTMENTS_ACTIVE_JSP = "/secure/vendor-appointments/manage-appointments-active.jsp";
		public final static String MANAGE_APPOINTMENTS_ALL_OTHERS_JSP = "/secure/vendor-appointments/manage-appointments-all-others.jsp";
		public final static String VIEW_APPOINTMENT_JSP = "/secure/vendor-appointments/view-appointment.jsp";
		
		public final static String LED_STORE_DETAILS_JSP = "/secure/vendor-stores/led-store-details.jsp";
		public final static String MANAGE_ADS_JSP = "/secure/vendor-feature-feeds/manage-ads.jsp";
		
		public final static String ADD_AD_JSP = "/secure/vendor-feature-feeds/add-ad.jsp";
		public final static String VIEW_ADS_JSP = "/secure/vendor-feature-feeds/view-ad.jsp";
		
		public final static String MANAGE_CAMPAIGNS_JSP = "/secure/vendor-feature-feeds/manage-campaigns.jsp";
		public final static String ADD_CAMPAIGN_JSP = "/secure/vendor-feature-feeds/add-campaign.jsp";
		
		public final static String ADD_RACK_JSP = "/secure/whmgmt/add-rack.jsp";
		public final static String RACK_ICON = "bi bi-grid";
		
		public final static String MANAGE_RACK_CATEGORIES_JSP = "/secure/whmgmt/rack-category/manage-rack-categories.jsp";
		public final static String ADD_RACK_CATEGORY_JSP = "/secure/whmgmt/rack-category/add-rack-category.jsp";
		public final static String EDIT_RACK_CATEGORY_JSP = "/secure/whmgmt/rack-category/edit-rack-category.jsp";
		
		public final static String MANAGE_RACK_SLOT_BOOKING_JSP = "/secure/whmgmt/rack-slot-booking/manage-rack-slot-booking.jsp";
		
		public final static String MANAGE_BUSINESS_INTERESTED_USERS_JSP = "/secure/business-interested-users/manage-business-interested-users.jsp";
		
		public final static String QR_CODE_PROFILES_JSP = "/secure/vendor-stores/qr-code-profiles/qr-code-profiles.jsp";
		public final static String ADD_QR_CODE_PROFILE_JSP = "/secure/vendor-stores/qr-code-profiles/add-qr-code-profile.jsp";
		public final static String QR_CODE_ICON = "fas fa-lg fa-fw me-2 fa-qrcode";
		
		public final static String MANAGE_PHONEPE_LOGS_REPORTS_JSP = "/secure/admin-reports/manage-phonepe-logs-reports.jsp";
		public final static String MANAGE_WAREHOUSE_JSP = "/secure/whmgmt/user/manage-warehouse-users.jsp";
		
		public final static String MANAGE_ERP_USER_JSP = "/secure/erp-users/manage-erp-users.jsp";
		public final static String ADD_ERP_USER_JSP = "/secure/erp-users/add-erp-user.jsp";
		public final static String EDIT_ERP_USER_JSP = "/secure/erp-users/edit-erp-user.jsp";
		public final static String CAMPANY_ICON = "fas fa-lg fa-fw me-2 fa-globe";
		public final static String MANAGE_BRAND_ASSOCIATION_JSP = "/secure/brand-association/manage-brand-association.jsp";
		public final static String ADD_BRAND_ASSOCIATION_JSP = "/secure/brand-association/add-brand-association.jsp";
		public final static String EDIT_BRAND_ASSOCIATION_JSP = "/secure/brand-association/edit-brand-association.jsp";
		public final static String BRAND_LOGIN_JSP = "/new-theme-brand-login.jsp";
		
		public final static String MANAGE_ERP_EMPLOYEES_JSP = "/secure/erp-users/manage-erp-employees.jsp";
		public final static String ADD_ERP_EMPLOYEES_JSP = "/secure/erp-users/add-erp-employee.jsp";
		public final static String EDIT_ERP_EMPLOYEES_JSP = "/secure/erp-users/edit-erp-employee.jsp";
		public final static String MANAGE_lIKE_ICON ="fa-solid fa-thumbs-up text-success";
		public final static String MANAGE_IMPRESSION_ICON ="fa-solid fa-star text-success";
		public final static String MANAGE_VIEWS_ICON ="fa-solid fa-eye text-success ";
		public final static String MANAGE_COMMENTS_ICON ="fa-solid fa-comment text-success";
		public final static String MANAGE_SHARE_ICON ="fa-brands fa-telegram text-success"; 
		public final static String MANAGE_WHISHLIST_ICON ="fa-solid fa-heart text-success";
		public final static String MANUALBOOKING_ADDPRODUCTS_JSP="/secure/manual-bookings/manual-booking-add-products.jsp";//added
		
		
		public final static String MANAGE_MARKET_SHARES_JSP = "/secure/erp-users/manage-market-share.jsp";//added
		public final static String MANAGE_TOTAL_STORES_JSP = "/secure/erp-users/manage-total-stores.jsp";//added
		public final static String MANAGE_OWN_STORES_JSP ="/secure/erp-users/manage-own-stores.jsp";//added
		public final static String MANAGE_PERFORMANACE_JSP ="/secure/erp-users/manage-performance.jsp";//added
		
		public final static String MANAGE_ONBOARDING_JSP ="/secure/vendor/account/manage-onboarding.jsp";//added
	
		public final static String ERP_USERS_ICON ="fa-solid fa-users";//ADDED
		
		public final static String WARE_HOUSE_SLOT_BOOKED_ICON = "fa-sharp fa-solid fa-warehouse"; //ADDED
			
		public final static String ORDER_VALUE_ICON ="far fa-lg fa-fw me-2 fa-clipboard";//Added
		//////////////////////////////
		public final static String MANAGE_DASHBOARD_ICON = " fas fa-lg fa-fw me-2 fa-microchip text-theme";
		public final static String MANAGE_MANUAL_BOOKING_ICON = "fa-solid fa-address-book me-2 text-theme";
		public final static String MANAGE_DRIVER_DASHBOARD_ICON = "fa-solid fa-user-nurse me-2 text-theme";
		public final static String MANAGE_BRAND_DASHBOARD_ICON ="fa-solid fa-bookmark me-2 text-theme";
		public final static String MANAGE_UOM_DASHBOARD_ICON = "fa-solid fa-scale-balanced me-2 text-theme";
		public final static String MANAGE_PRODUCT_TEMPLATE_DASHOARD_ICON = " fa-solid fa-table me-2 text-theme "; 
		public final static String MANAGE_PRODUCT_VARIANT_DASHBOARD_ICON  = "fa-solid fa-clone me-2 text-theme";
		public final static String MANAGE_PRODUCT_IMAGE_DASHBOARD_ICON = " fa-solid fa-image me-2 text-theme";
		public final static String MANAGE_AD_DASHBOARD_ICON = "fas fa-lg fa-fw me-2 fa-audio-description text-theme";
		public final static String MANAGE_CAMPAGIN_DASHBOARD_ICON ="fa-solid fa-tv me-2 text-theme";
		public final static String MANAGE_FEEDS_DASHBOARD_ICON ="fas fa-lg fa-fw me-2 fa-credit-card text-theme";
		public final static String MANAGE_CATEGORY_DASHBOARD_ICON = "fa-solid fa-layer-group me-2 text-theme";
		public final static String MANAGE_CUSTOM_FEEDS_DASHBOARD_ICON = "fas fa-cogs me-2 text-theme";
		public final static String MANAGE_MARKET_SHARE_DASHBOARD_ICON = "fa-solid fa-chart-column me-2 text-theme";
		public final static String MANAGE_TOTAL_STORE_DASHBOARD_ICON = "fa-solid fa-store me-2 text-theme";
		public final static String MANAGE_OWN_STORE_DASHBOARD_ICON = "fa-solid fa-shop-lock me-2 text-theme";
		public final static String MANAGE_PERFORMANCE_DASHBOARD_ICON = "fa-solid fa-life-ring me-2 text-theme";
		public final static String MANAGE_RACK_CATEGORY_DASHBOARD_ICON = "fas fa-lg fa-fw me-2 fa-building  text-theme";
		public final static String MANAGE_SLOT_STATUS_DASHBOARD_ICON = "fas fa-lg fa-fw me-2 fa-th text-theme";
		
		
	}

	public class JS_URLS { 

		public final static String JS_BASE_PATH = "js/viewjs/";

		public final static String CKEDITOR_FOLDER_PATH = "/dist/assets/myhub-custom-js/ckeditor/";
		public final static String CKEDITOR_MIN_JS = "/dist/assets/myhub-custom-js/ckeditor/ckeditor.min.js";
		public final static String CKEDITOR_CONFIG_JS = "/dist/assets/myhub-custom-js/ckeditor/config.js";
		public final static String TIME_PLUGIN_JS = "/dist/assets/myhub-custom-js/jquery.plugin.js";
		public final static String TIMEENTRY_MIN_JS = "/dist/assets/myhub-custom-js/jquery.timeentry.min.js";
		public final static String TIMEENTRY_JS = "/dist/assets/myhub-custom-js/jquery.timeentry.js";
		public final static String TIMEENTRY_CSS = "/dist/assets/myhub-custom-css/jquery.timeentry.css";
		public final static String RATING_JS = "/dist/assets/myhub-custom-js/star-rating.js";
		public final static String RATING_CSS = "/dist/assets/myhub-custom-css/star-rating.css";

		public final static String LOGIN_JS = JS_BASE_PATH + "login.js";
		public final static String CHANGE_PASSWORD_JS = JS_BASE_PATH + "change-password.js";
		public final static String USER_LOGIN_OTP_JS = JS_BASE_PATH + "user-login-otp/user-login-otp.js";
		public final static String FORGOT_PASSWORD_JS = JS_BASE_PATH + "forgot-password.js";

		public final static String ADMIN_PROFILE_JS = JS_BASE_PATH + "admin-user/admin-profile.js";
		public final static String VENDOR_PROFILE_JS = JS_BASE_PATH + "vendor/vendor-profile.js";

		public final static String MANAGE_ADMIN_SETTINGS_JS = JS_BASE_PATH + "settings/manage-admin-settings.js";
		public final static String MANAGE_ADMIN_SMS_SETTINGS_JS = JS_BASE_PATH + "settings/manage-admin-sms-sending.js";
		public final static String MANAGE_DYNAMIC_CARS_JS = JS_BASE_PATH + "settings/manage-dynamic-cars.js";
		public final static String ADD_DYNAMIC_CAR_JS = JS_BASE_PATH + "settings/add-dynamic-car.js";
		public final static String EDIT_DYNAMIC_CAR_JS = JS_BASE_PATH + "settings/edit-dynamic-car.js";

		public final static String MANAGE_ADMIN_USER_JS = JS_BASE_PATH + "admin-user/manage-admin-user.js";
		public final static String ADD_ADMIN_USER_JS = JS_BASE_PATH + "admin-user/add-admin-user.js";
		public final static String EDIT_ADMIN_USER_JS = JS_BASE_PATH + "admin-user/edit-admin-user.js";

		public final static String MANAGE_SUPER_SERVICES_JS = JS_BASE_PATH + "super-services/manage-super-services.js";
		public final static String ADD_SUPER_SERVICES_JS = JS_BASE_PATH + "super-services/add-super-service.js";
		public final static String EDIT_SUPER_SERVICES_JS = JS_BASE_PATH + "super-services/edit-super-service.js";

		public final static String MANAGE_CATEGORIES_JS = JS_BASE_PATH + "categories/manage-categories.js";
		public final static String ADD_CATEGORIES_JS = JS_BASE_PATH + "categories/add-category.js";
		public final static String EDIT_CATEGORIES_JS = JS_BASE_PATH + "categories/edit-category.js";

		public final static String MANAGE_SUBSCRIBERS_JS = JS_BASE_PATH + "vendor-subscribers/manage-subscribers.js";

		public final static String MANAGE_VENDOR_FEEDS_JS = JS_BASE_PATH + "vendor-feeds/manage-vendor-feeds.js";
		public final static String ADD_VENDOR_FEEDS_JS = JS_BASE_PATH + "vendor-feeds/add-feed.js";
		public final static String MANAGE_CUSTOM_FEEDS_JS = JS_BASE_PATH + "vendor-feeds/manage-custom-feeds.js";//Added
        public final static String MANAGE_LOW_INVENTORY_JS= JS_BASE_PATH +"vendor-feeds/manage-lowinventory.js";//Added
        public final static String MANAGE_RE_INVENTORY_JS = JS_BASE_PATH + "vendor-feeds/manage-reinventory.js";//Added
        public final static String MANAGE_ALL_BRANDS_JS = JS_BASE_PATH + "vendor-feeds/manage-allbrands.js";//added
        public final static String MANAGE_ADD_NEW_BRANDS_JS = JS_BASE_PATH + "vendor-feeds/manage-add-new-brands.js";//added
        public final static String MANAGE_MANUFACTURER_ORDER_REQUEST_JS = JS_BASE_PATH + "vendor-feeds/manufacturer-order-request.js";//added
        
        
        
		public final static String MANAGE_CAR_JS = JS_BASE_PATH + "car/manage-car.js";
		public final static String ADD_CAR_JS = JS_BASE_PATH + "car/add-car.js";
		public final static String EDIT_CAR_JS = JS_BASE_PATH + "car/edit-car.js";

		public final static String MANAGE_PASSENGER_JS = JS_BASE_PATH + "passenger/manage-passenger.js";
		public final static String PASSENGER_TOUR_JS = JS_BASE_PATH + "passenger/passenger-tour.js";

		public final static String MANAGE_DRIVER_SUBSCRIBERS_JS = JS_BASE_PATH + "passenger/manage-driver-subscribers.js";

		public final static String ANNOUNCEMENT_JS = JS_BASE_PATH + "announcement/manage-announcement.js";

		public final static String ADMIN_BOOKINGS_JS = JS_BASE_PATH + "admin-bookings/manage-admin-bookings.js";
		public final static String VENDOR_BOOKINGS_JS = JS_BASE_PATH + "vendor-bookings/manage-vendor-bookings.js";

		public final static String MANAGE_DRIVER_JS = JS_BASE_PATH + "driver/manage-driver.js";
		public final static String ADD_DRIVER_JS = JS_BASE_PATH + "driver/add-driver.js";
		public final static String EDIT_DRIVER_JS = JS_BASE_PATH + "driver/edit-driver.js";

		public final static String SUBSCRIBE_PACKAGE_JS = JS_BASE_PATH + "subscription-packages/subscribe-package.js";
		public final static String DRIVER_SUBSCRIPTION_EXTENSTION_JS = JS_BASE_PATH + "subscription-packages/driver-subscription-extension.js";

		public final static String MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_JS = JS_BASE_PATH + "subscription-packages/manage-driver-subscription-package-reports.js";

		public final static String MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-driver-transaction-history-reports.js";

		public final static String DRIVER_TOUR_JS = JS_BASE_PATH + "driver/driver-tours.js";

		public final static String MANAGE_PRODUCT_JS = JS_BASE_PATH + "vendor-products/manage-products.js";
		public final static String ADD_PRODUCT_JS = JS_BASE_PATH + "vendor-products/add-product.js";
		public final static String EDIT_PRODUCT_JS = JS_BASE_PATH + "vendor-products/edit-product.js";

		public final static String MANAGE_ORDERS_NEW_JS = JS_BASE_PATH + "vendor-orders/manage-orders-new.js";
		public final static String MANAGE_ORDERS_ACTIVE_JS = JS_BASE_PATH + "vendor-orders/manage-orders-active.js";
		public final static String MANAGE_ORDERS_ALL_OTHERS_JS = JS_BASE_PATH + "vendor-orders/manage-orders-all-others.js";
		public final static String MANAGE_COURIER_HISTORY_JS = JS_BASE_PATH + "vendor-orders/manage-courier-history.js";
		public final static String VIEW_ORDERS_JS = JS_BASE_PATH + "vendor-orders/view-order.js";
		public final static String VIEW_COURIER_DETAILS_JS = JS_BASE_PATH + "vendor-orders/view-courier-details.js";

		public final static String MANAGE_PROMO_CODE_JS = JS_BASE_PATH + "settings/manage-promo-code.js";
		public final static String ADD_PROMO_CODE_JS = JS_BASE_PATH + "settings/add-promo-code.js";
		public final static String EDIT_PROMO_CODE_JS = JS_BASE_PATH + "settings/edit-promo-code.js";

		public final static String REFUND_JS = JS_BASE_PATH + "refund/manage-refund.js";

		public final static String MANAGE_BOOKINGS_JS = JS_BASE_PATH + "booking/manage-booking.js";

		public final static String MANAGE_ADMIN_CONTACT_US_JS = JS_BASE_PATH + "settings/manage-admin-contact-us.js";
		public final static String MANAGE_TERMS_CONDITIONS_JS = JS_BASE_PATH + "settings/manage-terms-conditions.js";
		public final static String MANAGE_ABOUT_US_JS = JS_BASE_PATH + "settings/manage-about-us.js";
		public final static String MANAGE_REFUND_POLICY_JS = JS_BASE_PATH + "settings/manage-refund-policy.js";
		public final static String MANAGE_PRIVACY_POLICY_JS = JS_BASE_PATH + "settings/manage-privacy-policy.js";
		public final static String MANAGE_DRIVER_WALLET_SETTINGS_JS = JS_BASE_PATH + "settings/manage-driver-wallet-settings.js";
		public final static String MANAGE_ORDER_SETTINGS_JS = JS_BASE_PATH + "settings/manage-order-settings.js";
		public final static String EDIT_ORDER_SETTINGS_JS = JS_BASE_PATH + "settings/edit-order-settings.js";

		public final static String MANAGE_TAX_JS = JS_BASE_PATH + "settings/manage-tax.js";
		public final static String ADD_TAX_JS = JS_BASE_PATH + "settings/add-tax.js";
		public final static String EDIT_TAX_JS = JS_BASE_PATH + "settings/edit-tax.js";

		public final static String MANAGE_RIDE_LATER_SETTINGS_JS = JS_BASE_PATH + "settings/manage-ride-later-settings.js";

		public final static String MANAGE_DRIVER_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-driver-reports.js";
		public final static String MANAGE_DRIVER_BOOKINGS_JS = JS_BASE_PATH + "admin-reports/manage-driver-bookings.js";

		public final static String MANAGE_DRIVER_DUTY_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-driver-duty-reports.js";
		public final static String DRIVER_DUTY_REPORTS_JS = JS_BASE_PATH + "admin-reports/driver-duty-reports.js";
		public final static String DRIVER_LOGGED_IN_TIME_REPORT_JS = JS_BASE_PATH + "admin-reports/driver-loggedin-time-report.js";

		public final static String MANAGE_DRIVER_DRIVER_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-drivers-drive-reports.js";
		public final static String MANAGE_REFUND_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-refund-reports.js";
		public final static String MANAGE_CCAVENUE_LOGS_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-ccavenue-logs-reports.js";
		public final static String DRIVER_DRIVE_DETAIL_REPORTS_JS = JS_BASE_PATH + "admin-reports/driver-drive-detail-report.js";
		public final static String DRIVER_REFER_BENEFITS_DETAIL_REPORTS_JS = JS_BASE_PATH + "admin-reports/driver-refer-benefit-details-report.js";
		public final static String DRIVER_REFER_BENEFITS_REPORTS_JS = JS_BASE_PATH + "admin-reports/driver-refer-benefit-report.js";
		public final static String DRIVER_REFER_BENEFITS_TRIP_REPORTS_JS = JS_BASE_PATH + "admin-reports/driver-refer-benefit-trips-report.js";

		public final static String MANAGE_VENDOR_JS = JS_BASE_PATH + "vendor/manage-vendor.js";
		public final static String ADD_VENDOR_JS = JS_BASE_PATH + "vendor/add-vendor.js";
		public final static String EDIT_VENDOR_JS = JS_BASE_PATH + "vendor/edit-vendor.js";
		public final static String MANAGE_SUB_VENDOR_JS = JS_BASE_PATH + "vendor/manage-sub-vendor.js";
		public final static String ADD_SUB_VENDOR_JS = JS_BASE_PATH + "vendor/add-sub-vendor.js";
		public final static String EDIT_SUB_VENDOR_JS = JS_BASE_PATH + "vendor/edit-sub-vendor.js";
		public final static String VENDOR_TOURS_JS = JS_BASE_PATH + "vendor/vendor-tours.js";
		public final static String MANAGE_VENDOR_DYNAMIC_JS = JS_BASE_PATH + "vendor-settings/manage-vendor-dynamic-cars.js";
		public final static String SET_VENDOR_CAR_PRIORITY_JS = JS_BASE_PATH + "vendor-settings/set-vendor-car-priority.js";
		public final static String MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_JS = JS_BASE_PATH + "vendor/manage-vendor-monthly-subscription-history.js";
		public final static String VENDOR_MONTHLY_SUBSCRIPTION_JS = JS_BASE_PATH + "vendor/vendor-monthly-subscription.js";
		public final static String MANAGE_VENDOR_STORE_JS = JS_BASE_PATH + "vendor-stores/manage-vendor-store.js";
		public final static String ADD_VENDOR_STORE_JS = JS_BASE_PATH + "vendor-stores/add-vendor-store.js";
		public final static String EDIT_VENDOR_STORE_JS = JS_BASE_PATH + "vendor-stores/edit-vendor-store.js";
		public final static String  MANAGE_CLOUD_WAREHOUSE_JS =JS_BASE_PATH +"vendor-stores/manage-cloud-warehouse.js";//added

		public final static String MANAGE_AIRPORT_JS = JS_BASE_PATH + "airport/manage-airport-region.js";
		public final static String ADD_AIRPORT_JS = JS_BASE_PATH + "airport/add-airport-region.js";
		public final static String EDIT_AIRPORT_JS = JS_BASE_PATH + "airport/edit-airport-region.js";
		public final static String MANAGE_VENDOR_AIRPORT_JS = JS_BASE_PATH + "vendor-settings-airport/manage-vendor-airport-region.js";
		public final static String EDIT_VENDOR_AIRPORT_JS = JS_BASE_PATH + "vendor-settings-airport/edit-vendor-airport-region.js";

		public final static String MANAGE_MULTICITY_JS = JS_BASE_PATH + "multicity/manage-multicity-settings-city.js";
		public final static String ADD_MULTICITY_JS = JS_BASE_PATH + "multicity/add-multicity-city.js";
		public final static String EDIT_MULTICITY_JS = JS_BASE_PATH + "multicity/edit-multicity-city.js";

		public final static String MANAGE_SUBSCRIPTION_PACKAGE_JS = JS_BASE_PATH + "subscription-packages/manage-subscription-packages.js";
		public final static String ADD_SUBSCRIPTION_PACKAGE_JS = JS_BASE_PATH + "subscription-packages/add-subscription-package.js";
		public final static String EDIT_SUBSCRIPTION_PACKAGE_JS = JS_BASE_PATH + "subscription-packages/edit-subscription-package.js";

		public final static String MANAGE_SURGE_PRICE_JS = JS_BASE_PATH + "settings/manage-surge-price.js";
		public final static String ADD_SURGE_PRICE_JS = JS_BASE_PATH + "settings/add-surge-price.js";
		public final static String EDIT_SURGE_PRICE_JS = JS_BASE_PATH + "settings/edit-surge-price.js";

		public final static String MANAGE_RENTAL_PACKAGE_JS = JS_BASE_PATH + "rentalpackage/manage-rental-packages.js";
		public final static String ADD_RENTAL_PACKAGE_JS = JS_BASE_PATH + "rentalpackage/add-rental-package.js";
		public final static String EDIT_RENTAL_PACKAGE_JS = JS_BASE_PATH + "rentalpackage/edit-rental-package.js";

		public final static String MANAGE_TAKE_RIDE_JS = JS_BASE_PATH + "ride-later/manage-take-ride.js";
		public final static String RIDE_LATER_DRIVER_ASSIGNMENT_JS = JS_BASE_PATH + "ride-later/ride-later-driver-assignment.js";
		public final static String MANAGE_BOOK_LATER_JS = JS_BASE_PATH + "ride-later/manage-ride-later.js";
		public final static String MANAGE_CRITICAL_BOOK_LATER_JS = JS_BASE_PATH + "ride-later/manage-critical-ride-later.js";

		public final static String MANAGE_VENDOR_CITY_SETTINGS_JS = JS_BASE_PATH + "vendor-city/manage-vendor-city-settings.js";
		public final static String EDIT_VENDOR_CITY_SETTINGS_JS = JS_BASE_PATH + "vendor-city/edit-vendor-city-settings.js";

		public final static String MANAGE_CITY_SURGE_JS = JS_BASE_PATH + "settings/manage-city-surge.js";
		public final static String ADD_CITY_SURGE_JS = JS_BASE_PATH + "settings/add-city-surge.js";
		public final static String EDIT_CITY_SURGE_JS = JS_BASE_PATH + "settings/edit-city-surge.js";

		public final static String MANAGE_DRIVER_ACCOUNT_JS = JS_BASE_PATH + "driver/account/manage-driver-account.js";
		public final static String MANAGE_VENDOR_ACCOUNT_JS = JS_BASE_PATH + "vendor/account/manage-vendor-account.js";
		public final static String DRIVER_VENDOR_ACCOUNT_JS = JS_BASE_PATH + "driver-vendor-account.js";
		public final static String MANAGE_VENDOR_DRIVER_ACCOUNT_JS = JS_BASE_PATH + "vendor-driver/account/manage-vendor-driver-account.js";
		public final static String MANAGE_VENDOR_MY_ACCOUNT_JS = JS_BASE_PATH + "vendor/account/vendor-my-account.js";

		public final static String MANAGE_TRANSFERRED_ENCASH_REQUEST_JS = JS_BASE_PATH + "encashrequests/manage-transferred-encash-request.js";
		public final static String MANAGE_REJECTED_ENCASH_REQUEST_JS = JS_BASE_PATH + "encashrequests/manage-rejected-encash-request.js";
		public final static String MANAGE_APPROVED_ENCASH_REQUEST_JS = JS_BASE_PATH + "encashrequests/manage-approved-encash-request.js";
		public final static String MANAGE_HOLD_ENCASH_REQUEST_JS = JS_BASE_PATH + "encashrequests/manage-hold-encash-request.js";
		public final static String MANAGE_DRIVER_ACCOUNT_LOGS_JS = JS_BASE_PATH + "driver/account/manage-driver-account-logs.js";

		public final static String ADMIN_BOOKING_DETAILS_JS = JS_BASE_PATH + "admin-bookings/admin-bookings-details.js";

		public final static String IMPORT_CSV_PRODUCTS_JS = JS_BASE_PATH + "vendor-products/import-csv-products.js";

		public final static String MANAGE_DRIVER_FARE_JS = JS_BASE_PATH + "settings/manage-driver-fare.js";

		public final static String MANAGE_FEED_SETTINGS_JS = JS_BASE_PATH + "settings/manage-feed-settings.js";
		public final static String ADD_FEED_SETTINGS_JS = JS_BASE_PATH + "settings/add-feed-settings.js";
		public final static String EDIT_FEED_SETTINGS_JS = JS_BASE_PATH + "settings/edit-feed-settings.js";

		public final static String MANAGE_VENDOR_FEATURE_FEEDS_JS = JS_BASE_PATH + "vendor-feature-feeds/manage-vendor-feature-feeds.js";
		public final static String ADD_VENDOR_FEATURE_FEEDS_JS = JS_BASE_PATH + "vendor-feature-feeds/add-feature-feed.js";

		public final static String ADD_PRODUCT_CATEGORY_JS = JS_BASE_PATH + "master-data/add-product-category.js";
		public final static String EDIT_PRODUCT_CATEGORY_JS = JS_BASE_PATH + "master-data/edit-product-category.js";
		public final static String MANAGE_PRODUCT_CATEGORY_JS = JS_BASE_PATH + "master-data/manage-product-category.js";

		public final static String ADD_PRODUCT_SUB_CATEGORY_JS = JS_BASE_PATH + "master-data/add-product-sub-category.js";
		public final static String EDIT_PRODUCT_SUB_CATEGORY_JS = JS_BASE_PATH + "master-data/edit-product-sub-category.js";
		public final static String MANAGE_PRODUCT_SUB_CATEGORY_JS = JS_BASE_PATH + "master-data/manage-product-sub-category.js";

		public final static String ADD_BRAND_JS = JS_BASE_PATH + "master-data/add-brand.js";
		public final static String EDIT_BRAND_JS = JS_BASE_PATH + "master-data/edit-brand.js";
		public final static String MANAGE_BRAND_JS = JS_BASE_PATH + "master-data/manage-brands.js";

		public final static String ADD_UNIT_OF_MEASURE_JS = JS_BASE_PATH + "master-data/add-unit-of-measure.js";
		public final static String EDIT_UNIT_OF_MEASURE_JS = JS_BASE_PATH + "master-data/edit-unit-of-measure.js";
		public final static String MANAGE_UNIT_OF_MEASURE_JS = JS_BASE_PATH + "master-data/manage-unit-of-measures.js";

		public final static String ADD_PRODUCT_TEMPLATE_JS = JS_BASE_PATH + "master-data/add-product-template.js";
		public final static String EDIT_PRODUCT_TEMPLATE_JS = JS_BASE_PATH + "master-data/edit-product-template.js";
		public final static String MANAGE_PRODUCT_TEMPLATE_JS = JS_BASE_PATH + "master-data/manage-product-templates.js";

		public final static String ADD_PRODUCT_VARIANT_JS = JS_BASE_PATH + "master-data/add-product-variant.js";
		public final static String EDIT_PRODUCT_VARIANT_JS = JS_BASE_PATH + "master-data/edit-product-variant.js";
		public final static String MANAGE_PRODUCT_VARIANT_JS = JS_BASE_PATH + "master-data/manage-product-variants.js";

		public final static String ADD_PRODUCT_IMAGE_JS = JS_BASE_PATH + "master-data/add-product-image.js";
		public final static String EDIT_PRODUCT_IMAGE_JS = JS_BASE_PATH + "master-data/edit-product-image.js";
		public final static String MANAGE_PRODUCT_IMAGE_JS = JS_BASE_PATH + "master-data/manage-product-images.js";

		public final static String MANAGE_VENDOR_PRODUCT_JS = JS_BASE_PATH + "vendor-products/manage-vendor-products.js";
		public final static String ADD_VENDOR_PRODUCT_JS = JS_BASE_PATH + "vendor-products/add-vendor-product.js";
		public final static String EDIT_VENDOR_PRODUCT_JS = JS_BASE_PATH + "vendor-products/edit-vendor-product.js";

		public final static String MANAGE_APPOINTMENT_SETTINGS_JS = JS_BASE_PATH + "settings/manage-appointment-settings.js";
		public final static String EDIT_APPOINTMENT_SETTINGS_JS = JS_BASE_PATH + "settings/edit-appointment-settings.js";

		public final static String MANAGE_APPOINTMENTS_NEW_JS = JS_BASE_PATH + "vendor-appointments/manage-appointments-new.js";
		public final static String MANAGE_APPOINTMENTS_ACTIVE_JS = JS_BASE_PATH + "vendor-appointments/manage-appointments-active.js";
		public final static String MANAGE_APPOINTMENTS_ALL_OTHERS_JS = JS_BASE_PATH + "vendor-appointments/manage-appointments-all-others.js";
		public final static String VIEW_APPOINTMENT_JS = JS_BASE_PATH + "vendor-appointments/view-appointment.js";
		
		public final static String LED_STORE_DETAILS_JS = JS_BASE_PATH + "vendor-stores/led-store-details.js";
		
		public final static String MANAGE_ADS_JS = JS_BASE_PATH + "vendor-feature-feeds/manage-ads.js";
		public final static String ADD_AD_JS = JS_BASE_PATH + "vendor-feature-feeds/add-ad.js";
		
		public final static String MANAGE_CAMPAIGNS_JS = JS_BASE_PATH + "vendor-feature-feeds/manage-campaigns.js";
		public final static String ADD_CAMPAIGN_JS = JS_BASE_PATH + "vendor-feature-feeds/add-campaign.js";
		
		public final static String ADD_RACK_JS = JS_BASE_PATH + "whmgmt/add-rack.js";
		
		public final static String MANAGE_RACK_CATEGORIES_JS = JS_BASE_PATH + "whmgmt/rack-category/manage-rack-categories.js";
		public final static String ADD_RACK_CATEGORY_JS = JS_BASE_PATH + "whmgmt/rack-category/add-rack-category.js";
		public final static String EDIT_RACK_CATEGORY_JS = JS_BASE_PATH + "whmgmt/rack-category/edit-rack-category.js";
		
		public final static String MANAGE_RACK_SLOT_BOOKING_JS = JS_BASE_PATH + "whmgmt/rack-slot-booking/manage-rack-slot-booking.js";
		
		public final static String MANAGE_BUSINESS_INTERESTED_USERS_JS = JS_BASE_PATH + "business-interested-users/manage-business-interested-users.js";
		
		public final static String QR_CODE_PROFILES_JS = JS_BASE_PATH + "vendor-stores/qr-code-profiles/qr-code-profiles.js";
		public final static String ADD_QR_CODE_PROFILE_JS = JS_BASE_PATH + "vendor-stores/qr-code-profiles/add-qr-code-profile.js";
		
		public final static String MANAGE_PHONEPE_LOGS_REPORTS_JS = JS_BASE_PATH + "admin-reports/manage-phonepe-logs-reports.js";
		public final static String MANAGE_WAREHOUSE_JS = JS_BASE_PATH + "whmgmt/user/manage-warehouse-users.js";
		
		public final static String MANAGE_ERP_USERS_JS = JS_BASE_PATH + "erp-users/manage-erp-users.js";
		public final static String ADD_ERP_USER_JS = JS_BASE_PATH + "erp-users/add-erp-user.js";
		public final static String EDIT_ERP_USER_JS = JS_BASE_PATH + "erp-users/edit-erp-user.js";
		public final static String MANAGE_BRAND_ASSOCIATION_JS = JS_BASE_PATH + "brand-association/manage-brand-association.js";
		public final static String ADD_BRAND_ASSOCIATION_JS = JS_BASE_PATH + "brand-association/add-brand-association.js";
		public final static String EDIT_BRAND_ASSOCIATION_JS = JS_BASE_PATH + "brand-association/edit-brand-association.js";
		
		public final static String MANAGE_ERP_EMPLOYEES_JS = JS_BASE_PATH + "erp-users/manage-erp-employees.js";
		public final static String ADD_ERP_EMPLOYEE_JS = JS_BASE_PATH + "erp-users/add-erp-employee.js";
		public final static String EDIT_ERP_EMPLOYEE_JS = JS_BASE_PATH + "erp-users/edit-erp-employee.js";
	}

	public static final String VENDOR_LOGIN_URL = WebappPropertyUtils.BASE_PATH + "/vendor/login.do";

	public static final String STORES_WEBHOOK_URL = WebappPropertyUtils.kP_MART_BASE_URL + "/stores-webhook";
	public static final String VENDOR_PRODUCT_WEBHOOK_URL = WebappPropertyUtils.kP_MART_BASE_URL + "/vendor-product-webhook";
	
	public class LED_URLS {
		
		public static final String GET_CATEGORY_GROUPS = "/api/get-category-groups";
		public static final String GET_STORE_CATEGORIES = "/api/get-store-categories";
		public static final String GET_CITIES = "/api/cities-list";
		public static final String GET_LOCATIONS = "/api/get-locations";
		public static final String ADD_NEW_STORE = "/api/add-new-store";
		public static final String GET_RESOLUTIONS = "/api/get-resolutions";
		public static final String GET_ADS = "/api/ads-list";
		public static final String VIEW_AD = "/api/get-ad-details";
		public static final String GET_CAMPAIGNS = "/api/campaigns-list";
		public static final String ADD_NEW_AD = "/api/create-new-ad";
		public static final String ADD_NEW_CAMPAIGN = "/api/create-new-campaign";
		public static final String GET_STORES_BY_LOCATION = "/api/get-stores-by-locid";
		public static final String GET_DEVICES_BY_STORE = "/api/get-devices-by-storeid";
		
	}
}