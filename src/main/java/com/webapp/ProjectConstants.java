package com.webapp;

import java.text.DecimalFormat;

public class ProjectConstants {

	public static final String BASE_LATITUDE = "17.3850";
	public static final String BASE_LONGITUDE = "78.4867";

	public static final String UNKNOWNCARD = "unknown card";

	public static final String ANDROID = "android";

	public static final String FIRST_VERSION = "1.0";

	public static final int PHONE_NO_MIN = 10;

	public static final int PHONE_NO_MAX = 10;

	public static final String TAXI_SOL_THEME_COLOR = "#FF8240";

	public static final String TAXI_SOL_THEME_COLOR_1 = "#178a43";

	public static final String GOOGLE_PLACE_KEY = "AIzaSyAT3wIjV73qVXPAlgkyifnns38GztnbNF4";

	public static final String DEFAULT_STATE = "Hyderabad";

	public static final String COMPANY_DRIVER = "Company";

	public static final String COMPANY_DRIVER_ID = "1";

	public static final String INDIVIDUAL_DRIVER = "Individual";

	public static final String INDIVIDUAL_DRIVER_ID = "0";

	public static final String PERCENTAGE_TEXT = "Percentage";

	public static final String PERCENTAGE_ID = "1";

	public static final String AMOUNT_TEXT = "Amount";

	public static final String AMOUNT_ID = "2";

	public static final String LIMITED_TEXT = "Limited";

	public static final String UNLIMITED_TEXT = "Unlimited";

	public static final String LIMITED_ID = "1";

	public static final String UNLIMITED_ID = "2";

	public static final String ALL_TEXT = "All";

	public static final String INDIVIDUAL_TEXT = "Individual";

	public static final String ALL_ID = "1";

	public static final String INDIVIDUAL_ID = "2";

	public static final String DEFAULT_OPERATING_RADIUS = "10";

	public static final String REFERRAL_CODE_PHONE = "+233 123456";

	// -----------------------------------------------------------------------

	public static final long REQUEST_TIME_ONE_ORDERS = 60;

	public static final long REQUEST_TIME_ONE = 40;

	public static final long REQUEST_TIME = (REQUEST_TIME_ONE + 6) * 5;

	public static final long ALGORITHM_EXPIRE_TIME = (REQUEST_TIME_ONE * 1000) + 10;

	public static final String JOB_EXPIRE_TIME = REQUEST_TIME_ONE + "";
	public static final String JOB_EXPIRE_TIME_ORDERS = REQUEST_TIME_ONE_ORDERS + "";

	// -----------------------------------------------------------------------

	public static final String ENGLISH = "English";

	public static final String ENGLISH_ID = "2";

	public static final String NO_NEW_RELEASE = "NO_NEW_RELEASE";

	public static final String OPTIONAL_RELEASE = "OPTIONAL_RELEASE_AVAILABLE";

	public static final String MANDATORY_RELEASE = "MANDATORY_RELEASE";

	public static final String UNSUPPORTED_RELEASE = "UNSUPPORTED_RELEASE";

	public static final String PENDING_REQUEST = "pending";

	public static final String CANCELLED_REQUEST = "cancelled";

	public static final int MAX_AGE = 7 * 24 * 60 * 60;

	public class UserRoles {

		public static final String SUPER_ADMIN_USER_ID = "1";

		public static final String SUPER_ADMIN_ROLE = "super admin";
		public static final String SUPER_ADMIN_ROLE_ID = "1";

		public static final String ADMIN_ROLE = "admin";
		public static final String ADMIN_ROLE_ID = "2";

		public static final String BUSINESS_OWNER_ROLE = "business owner";
		public static final String BUSINESS_OWNER_ROLE_ID = "3";

		public static final String BUSINESS_OPERATOR_ROLE_ID = "4";
		public static final String OPERATOR_ROLE = "operator";

		public static final String PASSENGER_ROLE = "passenger";
		public static final String PASSENGER_ROLE_ID = "5";

		public static final String DRIVER_ROLE_ID = "6";
		public static final String DRIVER_ROLE = "driver";

		public static final String VENDOR_ROLE = "vendor";
		public static final String VENDOR_ROLE_ID = "7";

		public static final String SUB_VENDOR_ROLE = "sub vendor";
		public static final String SUB_VENDOR_ROLE_ID = "8";
		
		public static final String WAREHOUSE_ROLE = "warehouse";
		public static final String WAREHOUSE_ROLE_ID = "9";
		
		public static final String ERP_ROLE = "erp";
		public static final String ERP_ROLE_ID = "12";
		
		public static final String ERP_EMPLOYEE_ROLE = "erp employee";
		public static final String ERP_EMPLOYEE_ROLE_ID = "13";
	}

	public static final String All = "All";

	public static final String DRIVER_APPLICATION_PENDING = "pending";

	public static final String DRIVER_APPLICATION_ACCEPTED = "accepted";

	public static final String DRIVER_APPLICATION_REJECTED = "rejected";

	public static final String ACTION_ADD = "add";

	public static final String ACTION_EDIT = "edit";

	public static final String ACTION_DELETE = "delete";

	public class TourStatusConstants {

		public static final String NEW_TOUR = "new";
		public static final String NEW_TOUR_ID = "2";

		public static final String ASSIGNED_TOUR = "assigned";
		public static final String ASSIGNED_TOUR_ID = "5";

		public static final String ACCEPTED_REQUEST = "accepted";
		public static final String ACCEPTED_REQUEST_ID = "7";

		public static final String REJECTED_TOUR = "rejected";
		public static final String IGNORED_TOUR = "ignored";

		public static final String ARRIVED_WAITING_TOUR = "arrived & waiting";
		public static final String ARRIVED_WAITING_TOUR_ID = "11";

		public static final String STARTED_TOUR = "started";
		public static final String STARTED_TOUR_ID = "3";

		public static final String ENDED_TOUR = "ended";
		public static final String ENDED_TOUR_ID = "4";

		public static final String CANCELLED_REQUEST_BY_DRIVER = "cancelled by driver";
		public static final String CANCELLED_REQUEST_BY_DRIVER_ID = "8";

		public static final String PASSENGER_UNAVAILABLE = "passenger unavailable";
		public static final String PASSENGER_UNAVAILABLE_ID = "10";

		public static final String CANCELLED_REQUEST_BY_PASSENGER = "cancelled by passenger";
		public static final String CANCELLED_REQUEST_BY_PASSENGER_ID = "9";

		public static final String EXPIRED_TOUR = "expired";
		public static final String EXPIRED_TOUR_ID = "6";

		public static final String CANCELLED_REQUEST_BY_ADMIN = "cancelled by admin";

		public static final String RIDE_LATER_ASSIGNED_TOUR = "rl-assigned";
		public static final String RIDE_LATER_REASSIGNED_TOUR = "rl-reassigned";
		public static final String RIDE_LATER_EXPIRED_TOUR = "rl-expired";
		public static final String RIDE_LATER_NEW_TOUR = "rl-new";
		public static final String RIDE_LATER_PENDING_REQUEST = "rl-pending";
		public static final String RIDE_LATER_ACCEPTED_REQUEST = "rl-accepted";

		public static final String RIDE_LATER_PENDING = "pending";
		public static final String RIDE_LATER_ADVANCED = "advanced"; // not used
	}

	public static final String DRIVER_FREE_STATUS = "free";

	public static final String DRIVER_HIRED_STATUS = "hired";

	public static final String CURRENT = "current";

	public static final String IN_PROGRESS = "in progress";
	
	public static final String COMPLETED = "completed";

	public static final String CANCELLED = "cancelled";

	public static final String STATE = "Delhi";

	public static final String INDIVIDUAL_BOOKING = "individual booking";

	public static final String BUSINESS_OWNER_BOOKING = "business owner booking";

	public static final String BUSINESS_OWNER_BOOKING_B = "B";

	public static final String ADMIN_BOOKING = "admin booking";

	public static final String ADMIN_BOOKING_A = "A";

	public static final long DEFAULT_COUNTRY_US = 223;

	public static final long DEFAULT_STATE_ALABAMA = 3435;

	public static final long DEFAULT_CITY_ABBEVILLE = 124807;

	public static final String First_Vehicle_ID = "1";

	public static final String Second_Vehicle_ID = "2";

	public static final String Third_Vehicle_ID = "3";

	public static final String Fourth_Vehicle_ID = "4";

	public static final String Fifth_Vehicle_ID = "5";

	// This is commented as sixth vehicle id is assigned to rental
	// public static final String Sixth_Vehicle_ID = "6";

	public static final String Seventh_Vehicle_ID = "7";

	public static final String ALL_USERS = "All";

	public static final String ALL_USERS_ID = "1";

	public static final String ADMIN_USERS = "Admin";

	public static final String ADMIN_USERS_ID = "2";

	public static final String DRIVER_USERS = "Driver";

	public static final String DRIVER_USERS_ID = "3";

	public static final String PASSENGER_USERS = "Passenger";

	public static final String PASSENGER_USERS_ID = "4";

	public static final String CORPORATE_OWNERS_USERS = "Corporate Owners";

	public static final String CORPORATE_OWNERS_ID = "5";

	// ----------------- Payment Gateway ----------------

	public static final String PAYMENT_TYPE_CASH = "Cash";

	public static final String PAYMENT_TYPE_CREDIT = "Credit";

	public static final String PAYMENT_TYPE_CCAVENUE = "CCAVENUE";

	public static final String SUPER_ADMIN = "Super Admin";

	public static final String CASH = "cash";

	public static final String CARD = "card";

	public static final String WALLET = "wallet";

	public static final String CASH_ID = "1";

	public static final String CARD_ID = "2";

	public static final String WALLET_ID = "3";

	public static final String ONLINE_ID = "4";

	public static final String C_CASH = "Cash";

	public static final String C_CARD = "Card";

	public static final String C_WALLET = "Wallet";

	public static final String C_ONLINE = "Online";

	public static final String CASH_INVOICE_MESSAGE_NOT_COLLECTED = "Cash not collected";

	public static final String CASH_INVOICE_MESSAGE_COLLECTED = "Cash collected:";

	public static final String CARD_INVOICE_MESSAGE = "Payment received:";

	public static final String CREDITS_STRING = "Credits";

	public static final String CARD_INVOICE_PENDING_MESSAGE = "Payment pending:";

	public static final String ADJUSTED_IN_CREDITS_MESSAGE = "Adjusted in credits:";
	
	public static final String PAYMENT_TYPE_PHONEPE = "Phonepe";

	// -------------------------Admin web top
	// up-----------------------------------------------

	public static final String ADMIN_STATUS_SUCCESS = "success";

	public static final String ADMIN_STATUS_PENDING = "pending";

	public static final String ADMIN_STATUS_FAILED = "failed";

	public static final int TOP_UP_MAX_LIMIT = 50000;

	public static final String MOBILE_APPLICATION = "mobile application";

	public static final String WEB = "web";

	// ------------------------Notification type---------

	public static final String NOTIFICATION_REFUND = "refund";

	public static final String NOTIFICATION_TRANSACTION = "Transaction_Notification";

	public static final String WALLET_DEPOSIT = "walletDeposit";

	public final static String NOTIFICATION_TYPE_MB_NEW_JOB = "MB_NEW_JOB";

	public final static String NOTIFICATION_TYPE_GENERAL = "GENERAL";

	public final static String NOTIFICATION_TYPE_VENDOR_FEED = "VENDOR_FEED";

	public final static String NOTIFICATION_TYPE_VENDOR_MONTHLY_SUBSCRIPTION_EXPIRED = "VENDOR_MONTHLY_SUBSCRIPTION_EXPIRED";

	// -------------------DISTANCE-------------------------------------------------------------

	public static final String KM = "km";

	public static final String MILES = "miles";

	public static final long KM_UNITS = 1000;

	public static final long MILES_UNITS = 1609;

	public final static String ON_DUTY = "ON DUTY";

	public final static String OFF_DUTY = "OFF DUTY";

	// ------------------------Ride
	// Later-----------------------------------------------

	public static final long ONE_MINUTE_MILLISECONDS_LONG = 60000;

	public static final long RIDE_LATER_CRON_JOB_TIME_1MIN = ONE_MINUTE_MILLISECONDS_LONG;

	public static final long RIDE_LATER_CRON_JOB_TIME = 5 * ONE_MINUTE_MILLISECONDS_LONG;

	public static final String RIDE_LATER_CRON_JOB = "Ride later trip";

	public static final String RIDE_NOW = "1";

	public static final String RIDE_NOW_STRING = "Ride Now";

	public static final String RIDE_LATER = "2";

	public static final String RIDE_LATER_STRING = "Book Later";

	// -----------------------------------------------------------------------

	public static String ACTIVATE_STATUS = "A";

	public static String DEACTIVATE_STATUS = "D";

	public static final String MALE = "Male";

	public static final String FEMALE = "Female";

	public static final int LIST_LIMIT = 10;

	public static final String WEEK_STRING = "WEEK";

	public static final String DAY_STRING = "DAY";

	public static final String MONTH_STRING = "MONTH";

	public static final String STATIC_MAP_IMG_PRE_STRING = "static_map_";

	public static final String STATIC_MAP_IMG_SIZE = "600x400";

	public static final long ONE_DAY_HOURS = 24;

	public static final long ONE_HOUR_MILLISECONDS_LONG = 60 * ONE_MINUTE_MILLISECONDS_LONG;

	public static final long ONE_DAY_MILLISECONDS_LONG = 24 * ONE_HOUR_MILLISECONDS_LONG;

	public static final String SURGE_FIRST_DAY_DATE = "01/01/2017"; // Format dd/mm/yyyy

	public static final String SURGE_NEXT_DAY_DATE = "02/01/2017"; // Format dd/mm/yyyy

	public static final String CCAVENUE_DEFAULT_ORDER_ID = "MYHUB-TRIP-";

	public static final String CCAVENUE_DEFAULT_SUBSCRIPTION_ORDER_ID = "MYHUB-SUBS-";

	public static final String CCAVENUE_DEFAULT_DELIVERY_ORDER_ID = "MYHUB-DEL-";

	public static final String REGISTERED_PASSENGER_ID = "1";

	public static final String NONREGISTERED_PASSENGER_ID = "2";

	public static final String REGISTERED_PASSENGER = "App User";

	public static final String NONREGISTERED_PASSENGER = "New User";

	public static final String DEFAUALT_CURRENCY_HTML_CODE = "&#x20b9;";

	public static final String PAYMENT_MODE = "Payment Mode";

	public static final String PAYMENT_CONFIRMATION = "Payment Confirmation";

	public static final String DEFAULT_COUNTRY = "India";
	public static final String DEFAULT_CURRENCY = "₹";

	public static final String DATATABLE_DATE_FORMAT = "DD/MM/YYYY";

	public static final String DATATABLE_DATE_FORMAT_WITHOUTSEPARATOR = "DDMMYYYY";

	public static final String Rental_Type_ID = "6";

	public static final String RENTAL_INTERCITY_ID = "0";

	public static final String RENTAL_OUTSTATION_ID = "1";

	public static final String RENTAL_INTERCITY_STRING = "Intercity";

	public static final String RENTAL_OUTSTATION_STRING = "Outstation";

	public static final String TOUR_REFERRER_TYPE_OWN = "Own";

	public static final String TOUR_REFERRER_TYPE_OTHER = "Other";

	// Driver Duty report Statte -------------------------------------------------

	public final static String ONLINE_STRING = "Online";

	public final static String OFFLINE_STRING = "Offline";

	public final static String IDEAL_STRING = "Ideal";

	// Export access ----------------------------------------------------------
	public final static String EXPORT_ACCESS_BOOKING_ID = "booking";

	public final static String EXPORT_ACCESS_PASSENGER_ID = "passenger";

	public final static String EXPORT_ACCESS_RIDE_LATER_ID = "ride_later";

	public final static String EXPORT_ACCESS_CRITICAL_RIDE_LATER_ID = "critical_ride_later";

	public final static String EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID = "driver_income_report";

	public final static String EXPORT_ACCESS_REFUND_REPORT_ID = "refund_report";

	public final static String EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID = "driver_duty_report";

	public final static String EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID = "ccavenue_log_report";

	public final static String EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID = "driver_drive_report";

	public final static String EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID = "driver_benefit_report";

	public final static String EXPORT_DRIVER_ACCOUNT_ID = "driver_account";

	public final static String EXPORT_VENDOR_ACCOUNT_ID = "vendor_account";

	public final static String EXPORT_VENDOR_DRIVER_ACCOUNT_ID = "vendor_driver_account";

	public final static String EXPORT_DRIVER_SUBSCRIPTION_ID = "driver_subscription";

	public final static String EXPORT_DRIVER_TRANSACTION_HISTORY_ID = "driver_transaction_history";
	
	public final static String EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID = "phonepe_log_report";
	
	public final static String EXPORT_ACCESS_WAREHOUSE_ID = "warehouse";

	// ------------------------------------------------------------------------

	public final static String SORT_ASC = "ASC";

	public final static String SORT_DESC = "DESC";

	// -----------------------------------------------------------------------

	public final static long EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT = (ONE_HOUR_MILLISECONDS_LONG * 5) + (60000 * 30);

	// Vendor managment-----------------------------------------------------------

	public final static String APPROVED = "Approved";

	public final static String NOT_APPROVED = "Not Approved";

	public static final String VENDOR_BOOKING = "vendor booking";

	public final static String NOT_ATTATCHED = "Not Attatched";

	// -------------------------- TRANSMISSION_TYPES
	// ----------------------------------------------------------

	public final static String TRANSMISSION_TYPE_AUTOMATIC_ID = "1";

	public final static String TRANSMISSION_TYPE_NON_AUTOMATIC_ID = "2";

	public final static String TRANSMISSION_TYPE_BOTH_ID = "3";

	// -------------------------- Airport Pickup/Drop
	// ----------------------------------------------------------

	public final static String AIRPORT_BOOKING_TYPE_PICKUP = "Airport Pickup";

	public final static String AIRPORT_BOOKING_TYPE_DROP = "Airport Drop";

	public static final String SURGE_TYPE_TIME = "Time Surge";

	public static final String SURGE_TYPE_RADIUS = "Radius Surge";

	public static final String AIRPORT_PICKUP = "0";

	public static final String AIRPORT_DROP = "1";

	// Markup fees CR
	// -------------------------------------------------------------------------------------
	public static final String MAXIMUM_MARKUP_VALUE = "5000";

	public static final String TOUR_BOOKED_BY_ADMIN = "1";

	public static final String TOUR_BOOKED_BY_DRIVER = "2";

	public static final String TOUR_BOOKED_BY_PASSENGER = "3";

	public static final String TOUR_BOOKED_BY_VENDOR = "4";

	public static final String TOUR_BOOKED_BY_BUSINESS_OWNER = "5";

	// ---------------------- Driver encash requests status
	// ---------------------------------------------------

	public final static String ENCASH_REQUEST_STATUS_HOLD = "HOLD";

	public final static String ENCASH_REQUEST_STATUS_APPROVED = "APPROVED";

	public final static String ENCASH_REQUEST_STATUS_TRANSFERRED = "TRANSFERRED";

	public final static String ENCASH_REQUEST_STATUS_REJECTED = "REJECTED";

	public final static String TRANSACTION_LOG_TYPE_CREDIT = "CREDIT";

	public final static String TRANSACTION_LOG_TYPE_DEBIT = "DEBIT";

	public final static String ENCASH_REQUEST_SEND_PENDING_FOR_APPROVAL_ID = "1";

	public final static String ENCASH_REQUEST_SEND_PENDING_FOR_TRANSFER_ID = "2";

	public final static String ENCASH_REQUEST_SEND_DIRECT_TRANSFER_ID = "3";

	// -------------------------------------------------------------------------------------------------------

	public final static String DEFAULT_COUNTRY_ID = "1";

	public final static String DRIVER_SUBSCRIPTION = "Driver subscription";

	public final static String TOURS = "Tour";

	public final static String CCAVENUE_RSA_REQUEST_TYPE_TOUR_ID = "1";

	public final static String CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID = "2";

	public final static String CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID = "3";

	public final static String DEFAULT_CAR_ID = "-1";
	public final static String DEFAULT_DRIVER_ID = "-1";
	public final static String DEFAULT_NUMBER_FOR_COMPARE = "-1";

	public class SMSConstants {

		// Approved new sms templates
		// https://docs.google.com/spreadsheets/d/13K0DJntZAP2HbiWAh_NCaIXwaL4Jos1N/edit#gid=1992417806
		public final static String SMS_CREDENTIALS_TEMPLATE_ID = "1707169700784249617";

		public final static String SMS_PASSENGER_TEMPLATE_ID = "1707169700800269177";

		public final static String SMS_PASSENGER_CANCELLED_TEMPLATE_ID = "1707169700766627360";

		public final static String SMS_WAITING_TEMPLATE_ID = "1707169700798001434";

		public final static String SMS_DRIVER_ARRIVED_TEMPLATE_ID = "1707169700807547138";

		public final static String SMS_INVOICE_TEMPLATE_ID = "1707169700736667595";

		public final static String SMS_RIDE_LATER_TEMPLATE_ID = "1707169700730246858";

		public final static String SMS_CANCELLED_BY_PASSENGER_TEMPLATE_ID = "1707169700726080316";

		public final static String SMS_NEW_JOB_TEMPLATE_ID = "1707169700715712218";

		public final static String SMS_PAYMENT_RECEIVED_TEMPLATE_ID = "1707169700710217366";

		public final static String SMS_OTP_TEMPLATE_ID = "1707169700705039879";

		public final static String SMS_ARRIVING_TEMPLATE_ID = "1707169700702759666";

		public final static String SMS_AWAY_TEMPLATE_ID = "1707169700698245503";

		public final static String SMS_REFUND_TEMPLATE_ID = "1707169700692352572";

		public final static String SMS_EINVOICE_GENERATED_TEMPLATE_ID = "1707169700685349417";

		public final static String SMS_DRIVER_CANCELLED_TEMPLATE_ID = "1707169700795569428";

		// --------------
		// not approved old sms templates

		public final static String SMS_BOOKING_TEMPLATE_ID = "1707162918406512682";

		public final static String SMS_TRACK_TEMPLATE_ID = "1707162928393464556";

		public final static String SMS_TRIP_STARTED_TEMPLATE_ID = "1707162928387709872";

//		public final static String SMS_TRIP_CANCELLED_TEMPLATE_ID = "1707162928088919355";

		// --------------
		public final static String SMS_DELIVERY_END_OTP_TEMPLATE_ID = "1707169700736667500";
	}

	public static final String ACTIVE = "Active";
	public static final String ACTIVE_ID = "1";

	public static final String DEACTIVE = "Deactive";
	public static final String DEACTIVE_ID = "2";

	public static final String DEFAULT_IMAGE = "/assets/image/default_avatar_male.jpg";
	public static final String DEFAULT_IMAGE_ADMIN = "/dist/assets/img/user/profile.jpg";

	public static final String YES = "Yes";

	public static final String NO = "No";

	public class WeightConstants {

		public static final int GRAMS_ID = 1;
		public static final String GRAMS = "grams";

		public static final int KILOGRAMS_ID = 2;
		public static final String KILOGRAMS = "kgs";

		public static final int MILLILITERS_ID = 3;
		public static final String MILLILITERS = "milliliters";

		public static final int LITERS_ID = 4;
		public static final String LITERS = "liters";
	}

	public class QuantityTypeConstants {

		public static final String LOOSE = "L";
		public static final String LOOSE_OPTION = "loose";

		public static final String PIECES = "P";
		public static final String PIECES_OPTION = "pieces";

		public static final String BUNDLE = "B";
		public static final String BUNDLE_OPTION = "bundle";
	}

	public class OrderDeliveryConstants {

		public final static String ORDER_STATUS_NEW_PAYMENT_PENDING = "new payment pending";
		public final static String ORDER_STATUS_NEW = "new";
		public final static String ORDER_STATUS_CANCELLED = "cancelled";
		public final static String ORDER_STATUS_REJECTED = "rejected";
		public final static String ORDER_STATUS_EXPIRED = "expired";
		public final static String ORDER_STATUS_OFFLINE = "offline";

		// label -> Preparing Your Order
		public final static String ORDER_STATUS_ACCEPTED_OWN_DRIVER = "accepted own driver";

		// label -> Looking for delivery partner
		public final static String ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER = "accepted platform driver";

		// label -> Driver has been assigned for your order
		public final static String ORDER_STATUS_DRIVER_ASSIGNED = "driver assigned";

		// label -> Driver is on the way to pickup your order
		public final static String ORDER_STATUS_DRIVER_ACCEPTED = "driver accepted";

		// label -> Driver has reached to collect your order
		public final static String ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP = "driver arrived at pickup";

		// label -> Driver is on the way to deliver your order
		public final static String ORDER_STATUS_DRIVER_STARTED = "driver started";

		// label -> Delivered
		public final static String ORDER_STATUS_DRIVER_DELIVERED = "driver delivered";

		// label -> Driver cancelled your order
		public final static String ORDER_STATUS_DRIVER_CANCELLED = "driver cancelled";

		// label -> Vendor cancelled your order
		public final static String ORDER_STATUS_VENDOR_CANCELLED = "vendor cancelled";

		public final static String ORDER_PAYMENT_PENDING = "pending";
		public final static String ORDER_PAYMENT_SUCCESS = "success";
		public final static String ORDER_PAYMENT_FAILED = "failed";

		public static final String ORDERS_NEW_TAB = "orders-new-tab";
		public static final String ORDERS_ACTIVE_TAB = "orders-active-tab";
		public static final String ORDERS_ALL_OTHERS_TAB = "orders-all-others-tab";

		public static final int NEW_PAYMENT_PENDING_ORDERS_EXPIRY_TIME = 30;
		
		public static final String ORDER = "Order";
	}

	public class AppointmentConstants {

		public final static String APPOINTMENT_STATUS_NEW_PAYMENT_PENDING = "new payment pending";
		public final static String APPOINTMENT_STATUS_NEW = "new";
		public final static String APPOINTMENT_STATUS_ACCEPTED = "accepted";
		public final static String APPOINTMENT_STATUS_CANCELLED = "cancelled";
		public final static String APPOINTMENT_STATUS_REJECTED = "rejected";
		public final static String APPOINTMENT_STATUS_EXPIRED = "expired";
		public final static String APPOINTMENT_STATUS_COMPLETED = "service completed";
		public final static String APPOINTMENT_STATUS_VENDOR_CANCELLED = "vendor cancelled";

		public final static String APPOINTMENT_PAYMENT_PENDING = "pending";
		public final static String APPOINTMENT_PAYMENT_SUCCESS = "success";
		public final static String APPOINTMENT_PAYMENT_FAILED = "failed";

		public static final String APPOINTMENTS_NEW_TAB = "appointments-new-tab";
		public static final String APPOINTMENTS_ACTIVE_TAB = "appointments-active-tab";
		public static final String APPOINTMENTS_ALL_OTHERS_TAB = "appointments-all-others-tab";

		public static final int NEW_PAYMENT_PENDING_APPOINTMENTS_EXPIRY_TIME = 30;
	}

	public static final String STATUS_TYPE = "type";
	public static final String STATUS_MESSAGE = "message";
	public static final String STATUS_MESSAGE_KEY = "messageKey";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String NOT_AVAILABLE = "NA";

	public class MENU_CONSTANTS {

		public final static int MENU_DASHBOARD_ID = 1;
	//public final static int MENU_MANUAL_BOOKINGS_ID = 2;
		public final static int MENU_lOGISTICS_ID = 2;
		
		public final static int MENU_RETAILSUPPLY_ID = 3;
		
		public final static int MENU_INVENTORY_ID = 53;
		
		public final static int MENU_MARKETSURVEY_ID = 4;
		public final static int MENU_MARKET_SHARE_ID = 6;
		
		public final static int MENU_MARKETING_ID = 7;
		
		public final static int MENU_USERS_ID = 10;
		//////////////////////////////////////////////
		public final static int MENU_ADMIN_USER_ID = 4;
	//public final static int MENU_CUSTOMERS_ID = 5;
		//public final static int MENU_DRIVERS_ID = 6;
		//public final static int MENU_CARS_ID = 7;
		
		
		
		public final static int MENU_OPERATORS_ID = 9;
		public final static int MENU_BROADCAST_ID = 10;
		public final static int MENU_REFUND_ID = 11;
		public final static int MENU_Dashboard_ID = 12;
		public final static int MENU_MY_BOOKINGS_ID = 13;
		public final static int MENU_BOOKINGS_ID = 15;
		public final static int MENU_REPORTS_ID = 16;
		public final static int MENU_BOOK_LATER_ID_1 = 17;
		public final static int MENU_CRITICAL_BOOK_LATER_ID_1 = 18;
		public final static int MENU_FARE_CALCULATOR_ID = 19;
		public final static int MENU_SETTINGS_ID = 20;
		public final static int MENU_VENDORS_ID = 21;
		public final static int MENU_VENDOR_BOOKINGS_ID = 22;
		public final static int MENU_VENDOR_DRIVERS_ID = 23;
		//public final static int MENU_VENDOR_CARS_ID = 24;
		public final static int MENU_BOOK_LATER_ID_2 = 25;
		public final static int MENU_CRITICAL_BOOK_LATER_ID_2 = 26;
		public final static int MENU_DRIVER_ACCOUNTS_ID = 27;
		public final static int MENU_VENDOR_ACCOUNTS_ID = 28;
		public final static int MENU_HOLD_ENCASH_REQUESTS_ID = 24;//added
		public final static int MENU_APPROVED_ENCASH_REQUESTS_ID = 25;//added
		public final static int MENU_TRANSFERRED_ENCASH_REQUESTS_ID = 31;
		public final static int MENU_REJECTED_ENCASH_REQUESTS_ID = 32;
		public final static int MENU_VENDOR_DRIVER_ACCOUNTS_ID = 33;
		public final static int MENU_VENDOR_MY_ACCOUNT_ID = 34;
		public final static int MENU_TAKE_BOOKINGS_ID = 35;
		public final static int MENU_SUPER_SERVICES_ID = 36;
		public final static int MENU_CATEGORIES_ID = 37;
		public final static int MENU_PRODUCTS_SALES_ID = 38;//ADDED
		public final static int MENU_SUBSCRIBERS_ID = 31;
		public final static int MENU_PROMO_CODE_ID = 40;
		public final static int MENU_FEEDS_ID = 41;
		public final static int MENU_VENDOR_STORE_ID = 42;
		public final static int MENU_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_ID = 33;//ADDED
	public final static int MENU_HISTORY_ID = 44;
		public final static int MENU_PRODUCTS_AND_PRICE_ID = 34;//added
		public final static int MENU_SUB_VENDOR_ID = 46;
		public final static int MENU_APPOINTMENT_ID = 35;//ADDED
		public final static int MENU_FEED2_ID = 44;
		public final static int WAREHOUSE_ID = 36;//ADDED
		public final static int BUSINESS_INTERESTED_USERS = 49;
		public final static int WAREHOUSE_USER = 50;
		public final static int ERP_USER = 51;
		public final static int ERP_EMPLOYEE_ID = 52;
		///////////////////////////////////////
		
		public final static int MENU_MARKETING_ID1 = 55;
		public final static int  MENU_LOGISTICS_ID = 59;
		public final static int   MENU_SERVICES_ID = 61;
		public final static int    MENU_INVENTORY_ID1 = 54;
		public final static int  MENU_MARKETSURVEY_ID1 = 58;
		
	}

	public static final int DAY_SHIFT_ID = 1;

	public static final String DAY_SHIFT = "Day";

	public static final int NIGHT_SHIFT_ID = 2;

	public static final String NIGHT_SHIFT = "Night";

	public static final int VENDOR_STORE_TIME_DAY_OF_WEEK = 1;

	public static final int VENDOR_STORE_TIME_SPECIFIC_DATE = 2;

	public static final int NUMBER_OF_SHIFTS_2 = 2;

	public static final String CRON_JOB_USER_ID = "-100";

	public class DAY_OF_WEEK {

		public final static int SUNDAY_NO = 0;
		public final static int MONDAY_NO = 1;
		public final static int TUESDAY_NO = 2;
		public final static int WEDNESDAY_NO = 3;
		public final static int THURSDAY_NO = 4;
		public final static int FRIDAY_NO = 5;
		public final static int SATURDAY_NO = 6;
	}

	public class VENDOR_FEED_STATUS {

		public static final String NEW = "NEW";
		public static final String SUCCESS = "SUCCESS";
		public static final String FAILED = "FAILED";
	}

	public class CAR_TYPES {

		public static final String AUTO = "Auto";
		public static final String AUTO_ID = "1";

		public static final String MINI = "Mini";
		public static final String MINI_ID = "2";

		public static final String SEDAN = "Sedan";
		public static final String SEDAN_ID = "3";

		public static final String SUV = "SUV";
		public static final String SUV_ID = "4";

		public static final String DRIVER = "Driver";
		public static final String DRIVER_ID = "5";

		public static final String BIKE = "Bike";
		public static final String BIKE_ID = "7";
	}

	public static int TRIP_TYPE_TOUR_ID = 1;
	public static int TRIP_TYPE_ORDER_ID = 2;

	public class DELIVERY_ADDRESS {

		public static final String DELIVERY_ADDRESS_HOME = "Home";
		public static final String DELIVERY_ADDRESS_HOME_ID = "1";

		public static final String DELIVERY_ADDRESS_WORK = "Work";
		public static final String DELIVERY_ADDRESS_WORK_ID = "2";

		public static final String DELIVERY_ADDRESS_OTHER = "Other";
		public static final String DELIVERY_ADDRESS_OTHER_ID = "3";
	}

	public class DATE_FILTER {
		public static final String ALL = "1";
		public static final String TODAY = "2";
		public static final String THIS_WEEK = "3";
		public static final String THIS_MONTH = "4";
		public static final String CUSTOM_LAST_X_DAYS = "5";
	}

	public static final int BATCH_INSERT_SIZE = 50;
	public static final int BATCH_UPDATE_SIZE = 50;

	public class SUPER_SERVICE_TYPE_ID {
		public static final String TRANSPORTATION_ID = "1";
		public static final String ECOMMERCE_ID = "2";
		public static final String COURIER_ID = "3";
		public static final String APPOINTMENT_ID = "4";
	}

	public static final int VENDOR_SUBSCRIPTION_MONTHLY_DAYS = 60;

	public class VENDOR_MONTHLY_SUBSCRIPTION_PAYMENT_TYPE {
		public static final String FREE = "Free";
	}

	public class VENDOR_MONTHLY_SUBSCRIPTION_SUBSCRIPTION_TYPE {

		public static final String FREE = "Free";
		public static final String PAID = "Paid";
	}

	public static final String VENDOR_MONTHLY_SUBSCRIPTION_STATUS_ACTIVE = "Active";

	public static final String VENDOR_MONTHLY_SUBSCRIPTION_STATUS_EXPIRED = "Expired";

	public static final String TRUE_STRING = "TRUE";
	public static final String FALSE_STRING = "FALSE";

	public static final String SERVICE_TYPE_CAR_ID = "1";
	public static final String SERVICE_TYPE_DRIVER_ID = "2";
	public static final String SERVICE_TYPE_COURIER_ID = "3";//added
	public static final String SERVICE_TYPE_DELIVERY_ID = "4";//added

	public static final String TIME_00_00 = "00:00";
	public static final DecimalFormat dfTwoDigit = new DecimalFormat("#00");

	public enum ADMIN_SMS_SETTINGS_ENUM {
		PASSENGER_1, PASSENGER_2, PASSENGER_3, PASSENGER_4, PASSENGER_5, PASSENGER_6, PASSENGER_7, PASSENGER_8, DRIVER_1, DRIVER_2, DRIVER_3, BO_1, BO_2, BO_3, BO_4
	}

	public final static int DEFAULT_TRIM_LENGTH = 50;

	public class DRIVER_APP_REGISTRATION_DEFAULT_VALUES {

		public static final String DEFAULT_NUMBER = "1234567890";
		public static final String DEFAULT_STRING = "DEFAULT";

		public static final String DEFAULT_AGENT_NUMBER = DEFAULT_NUMBER;
		public static final String DEFAULT_TRANSMISSION_TYPE_BOTH_ID = TRANSMISSION_TYPE_BOTH_ID;

		public static final String DEFAULT_ACCOUNT_NUMBER = DEFAULT_NUMBER;
		public static final String DEFAULT_ACCOUNT_NAME = DEFAULT_STRING;
		public static final String DEFAULT_BANK_NAME = DEFAULT_STRING;
		public static final String DEFAULT_ROUTING_NUMBER = "12345678901";
		public static final String DEFAULT_TYPE = DEFAULT_STRING;

		public static final String DEFAULT_DOB = "01/01/1990";
		public static final String DEFAULT_LICENSE_EXPIRATION = "01/01/2050";

		public static final String DEFAULT_INSURANCE_EFFECTIVE_DATE = DEFAULT_DOB;
		public static final String DEFAULT_INSURANCE_EXPIRATION_DATE = DEFAULT_LICENSE_EXPIRATION;

		public static final String DEFAULT_CAR_PLATE_NO = "TS01AB0001";
		public static final int DEFAULT_CAR_YEAR = 2023;
		public static final int DEFAULT_NO_OF_PASSENGERS = 4;
	}

	public class TypeConstants {

		public static final int STORE_ID = 1;
		public static final String STORE = "store";

		public static final int WAREHOUSE_ID = 2;
		public static final String WAREHOUSE = "warehouse";

	}

	public final static int CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MIN_VALUE = 5;
	public final static int CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MAX_VALUE = 20;

	public final static int DRIVER_PROCESSING_VIA_THREAD_BASED_ID = 1;
	public final static int DRIVER_PROCESSING_VIA_CRON_JOB_BASED_ID = 2;

	public static final String Image = "Image";
	public static final String Video = "Video";

	public class WEB_SOCKET_NOTIFICATION_TYPE {

		public final static String NOR = "NOR";// new order received.
		public final static String NOR_SOCKET = "`NOR,";// new order received.

		public final static String NGB_SOCKET = "`NGB,";

		public final static String CBA_SOCKET = "`CBA";

		public final static String AET_SOCKET = "`AET,";

		public final static String CGB_SOCKET = "`CGB";

		public final static String RLNJ_SOCKET = "`RLNJ,";

		public final static String PCB_SOCKET = "`PCB,";
		public final static String PCB_SOCKET_START = "PCB`";
	}
}