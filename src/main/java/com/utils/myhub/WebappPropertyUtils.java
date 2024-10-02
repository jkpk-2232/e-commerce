package com.utils.myhub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
public class WebappPropertyUtils {

	public static Logger LOGGER = Logger.getLogger(WebappPropertyUtils.class);

	public static final String PROJECT_NAME = getWebAppProperty("project_name");
	public static final String PROJECT_EMAIL = getWebAppProperty("project_email");
	public static final String WEB_SOCKET_URI = getWebAppProperty("web_socket_path");
	public static final String CLIENT_TIMEZONE = getWebAppProperty("time_zone");
	public static final String DEFAULT_VENDOR_ID = getWebAppProperty("defaultVendorId");
	public static final String DEFAULT_REGION_ID = getWebAppProperty("defaultRegionId");
	public static final String BASE_PATH = getWebAppProperty("project_base_url");
	public static final String APP_LAUNCH_DATE = getWebAppProperty("appLaunchDate");
	public static final String METAMORPH_USER_NAME = getWebAppProperty("metamorph_user_name");
	public static final String METAMORPH_PASSWORD = getWebAppProperty("metamorph_password");
	public static final String METAMORPH_FROM = getWebAppProperty("metamorph_from");
	public static final boolean RANDOM_CODE_PASS = Boolean.parseBoolean(getWebAppProperty("random_code_pass"));
	public static final String kP_MART_BASE_URL = getWebAppProperty("kp_mart_base_url");
	public static final String KP_MART_DEFAULT_VENDOR_ID = getWebAppProperty("KPMartDefaultVendorId");
	public static final String KP_MART_SERVICE_ID = getWebAppProperty("KPMartServiceId");
	public static final String MERCHANT_ID = getWebAppProperty("merchant_id");
	public static final String SALT_KEY = getWebAppProperty("salt_key");
	public static final String SALT_INDEX = getWebAppProperty("salt_index");
	public static final String PHONEPE_BASE_URL = getWebAppProperty("phonepe_base_url");
	public static final String LED_API_TOKEN = getWebAppProperty("led_api_token");
	public static final String LED_SERVICE_NAME = getWebAppProperty("led_service-name");
	public static final String LED_BASE_URL = getWebAppProperty("led_base_url");
	public static final String CDN_URL = getWebAppProperty("cdn_url");
	public static final String GUEST_SESSION_KEY = getWebAppProperty("guestSessionKey");
	public static final String FEED_AUTOMATION_PATH = getWebAppProperty("feedAutomationPath");
	public static final String DRIVER_MERCHANT_ID = getWebAppProperty("driver_merchant_id");
	public static final String DRIVER_SALT_KEY = getWebAppProperty("driver_salt_key");
	public static final String DRIVER_SALT_INDEX = getWebAppProperty("driver_salt_index");
	public static final String FIRE_BASE_CERTIFICATE_PATH = getWebAppProperty("fireBaseCertificatePath");

	// New Theme
	public static final String NEW_THEME_FOOTER_DISPLAY_APP_CSS = getWebAppProperty("newThemeFooterDisplayAppCss");

	private static Properties webAppProperties = null;

	private static void loadWebAppProperties() {

		if (webAppProperties == null) {

			try {

				InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("webapp.properties");

				if (in == null) {
					LOGGER.error("Falied to load properties.");
				} else {
					webAppProperties = new Properties();
					webAppProperties.load(in);
				}

			} catch (IOException ioe) {
				webAppProperties = null;
				LOGGER.error("Falied to load properties.", ioe);
			}
		}
	}

	public static String getWebAppProperty(String key) {

		if (webAppProperties == null) {

			LOGGER.info("Properties are null");

			loadWebAppProperties();

			if (webAppProperties == null) {
				return null;
			}
		}

		return webAppProperties.getProperty(key);
	}
}
