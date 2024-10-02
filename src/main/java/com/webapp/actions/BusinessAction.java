package com.webapp.actions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;
import org.glassfish.jersey.server.mvc.Viewable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.FrameworkConstants;
import com.jeeutils.MessagesUitls;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.AbstractValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.dbsession.DbSession;
import com.utils.myhub.SessionUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UrlModel;
import com.webapp.models.UserProfileModel;

public class BusinessAction extends Action {

	public static Logger logger = Logger.getLogger(BusinessAction.class);

	protected static final String SUCCESS_MESSAGE_TYPE = "Success";

	public static final DecimalFormat df = new DecimalFormat("##.##");

	private Validator validator = new Validator();
	private ArrayList<String> errorMessages = new ArrayList<String>();
	protected Map<String, String> loginSessionMap = new HashMap<>();
	protected UserProfileModel loggedInUserModelViaSession;

	protected JSONObject dtuJsonObject = new JSONObject();
	protected JSONArray dtuOuterJsonArray = new JSONArray();
	protected JSONArray dtuInnerJsonArray = new JSONArray();
	protected StringBuilder btnGroupStr = new StringBuilder();

	public HttpSession getSession() {
		return request.getSession(false);
	}

	protected void setMessages(List<String> successMessages, String messageType) {
		HttpSession session = getSession();
		session.setAttribute(messageType, successMessages);
	}

	public void setSuccessMessage(String message, String messageType) {
		List<String> successMessages = new ArrayList<String>();
		successMessages.add(message);
		setMessages(successMessages, messageType);
	}

	public void setSuccessMessage(String message) {
		setMessages(Arrays.asList(message), SUCCESS_MESSAGE_TYPE);
	}

	protected void addValidationMapping(String paramName, String fieldName, AbstractValidationRule rule) {
		validator.addValidationMapping(paramName, fieldName, rule);
	}

	protected void clearValidator() {
		validator = new Validator();
	}

	public String redirectToLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return UrlConstants.PAGE_URLS.LOGOUT_URL;
	}

	protected boolean hasError(Map<String, String> parameters) throws ServletException, IOException {

		boolean hasErrors = false;

		errorMessages = validator.validate(request, parameters);

		if (errorMessages.size() != 0) {
			hasErrors = true;
			request.setAttribute("hasErrors", true);
			request.setAttribute("hasErrorsAlert", errorMessages.get(0));
		}

		return hasErrors;
	}

	protected boolean hasError() throws ServletException, IOException {
		return hasError(null);
	}

	public void preprocessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		setRequest(request);
		setResponse(response);
		noCache(response);
		loadCssAndJs();
		loadResourceMessages();
		setGoogleMapKey();

		setDatatableStartEndTime(request, response);

		setThemeParams();
	}

	public void preprocessRequestNewTheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		setRequest(request);
		setResponse(response);
		noCache(response);

		loadCssAndJsNewTheme(request);
		loadResourceMessages();

		setGoogleMapKey();
		setThemeParams();
		setDatatableDateFormatNewTheme();

		validateSessionForNewTheme(request, response);
	}

	private void setThemeParams() {
		data.put("themeColor", ProjectConstants.TAXI_SOL_THEME_COLOR);
	}

	private void setDatatableStartEndTime(HttpServletRequest request, HttpServletResponse response) {

		long appLaunchDate = Long.parseLong(WebappPropertyUtils.APP_LAUNCH_DATE);

		String dataTableStartDateStr = DateUtils.dbTimeStampToSesionDate(appLaunchDate, WebappPropertyUtils.CLIENT_TIMEZONE, DateUtils.DATE_FORMAT_FOR_VIEW);
		String startDateStrWithoutSeparator = dataTableStartDateStr.replaceAll("/", "");

		data.put("startDateStringHidden", dataTableStartDateStr);
		data.put("startDateWithoutSeparatorHidden", startDateStrWithoutSeparator);
		data.put("dateFormatHidden", ProjectConstants.DATATABLE_DATE_FORMAT);
		data.put("dateFormatWithoutSeparatorHidden", ProjectConstants.DATATABLE_DATE_FORMAT_WITHOUTSEPARATOR);
	}

	private void setDatatableDateFormatNewTheme() {
		data.put(FieldConstants.DATE_FORMAT, DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.DATATABLE_FORMAT_DATE, DateUtils.DATATABLE_DATE_FORMAT_DISPALY);
		data.put(FieldConstants.DATATABLE_MIN_DATE, Long.parseLong(WebappPropertyUtils.APP_LAUNCH_DATE) + "");
	}

	protected Response redirectToPage(String path) {

		URI redirectUrl = null;

		try {
			redirectUrl = new URI(request.getContextPath() + path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return Response.seeOther(redirectUrl).build();
	}

	protected Response redirectToUrl(String url) {

		URI redirectUrl = null;

		try {
			redirectUrl = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return Response.seeOther(redirectUrl).build();
	}

	protected Response logoutUser() {
		return redirectToPage(UrlConstants.PAGE_URLS.LOGOUT_URL);
	}

	public static String getPriorityUrlByUserd(String userId) {
		return UrlModel.getFirstPriorityURL(userId);
	}

	protected void setErrorMessage(String message) {

		String logicalErrorMessage = (String) request.getAttribute("hasErrorsAlert");

		if (logicalErrorMessage == null) {
			request.setAttribute("hasErrorsAlert", message);
		}
	}

	protected String requiresAccess() {
		return null;
	}

	protected String messageForKey(String key) {

		if (!isLocalizationEnabled()) {
			return null;
		}

		String language = null;

		HttpSession session = request.getSession(false);

		if (session != null) {

			language = (String) session.getAttribute(FrameworkConstants.LANGUAGE_ATTRIBUTE);

			if (language == null) {

				language = getLanguageFromUrl();

				if (language == null) {

					language = FrameworkConstants.LANGUAGE_ENGLISH;
				}
			}

		} else {

			language = getLanguageFromUrl();
		}

		return MessagesUitls.messageForKey(key, language);
	}

	private String getLanguageFromUrl() {
		String language = request.getParameter("lang");
		return language == null ? FrameworkConstants.LANGUAGE_ENGLISH : language;
	}

	private boolean isLocalizationEnabled() {

		String localizationEnabled = WebappPropertyUtils.getWebAppProperty(FrameworkConstants.LOCALIZATION_ENABLED);

		if (localizationEnabled == null) {
			localizationEnabled = "no";
		}

		return localizationEnabled.equalsIgnoreCase(FrameworkConstants.LOCALIZATION_ENABLED_YES);
	}

	protected void loadResourceMessages() {

		if (!isLocalizationEnabled()) {
			return;
		}

		String[] commonMessages = commonMessages();

		for (String messageKey : commonMessages) {
			String message = messageForKey(messageKey);
			data.put(messageKey, message);
		}

		String[] requiredMessages = requiredMessages();

		for (String messageKey : requiredMessages) {
			String message = messageForKey(messageKey);
			data.put(messageKey, message);
		}
	}

	protected String[] requiredMessages() {
		return new String[0];
	}

	protected String[] commonMessages() {
		return new String[] {};

	}

	protected Response loadView(String url) {
		Viewable newView = new Viewable("/WEB-INF/views" + url, data);
		return Response.ok(newView).build();
	}

	protected void loadCssAndJs() {

		String[] commonCss = commonCss();
		String[] commonJs = commonJs();

		String[] requiredCss = requiredCss();
		String[] requiredJs = requiredJs();

		final String contextPath = request.getContextPath();
		final String cssBasePath = contextPath + "/assets/";
		final String jsBasePath = contextPath + "/assets/";

		if (isProductionEnvironment()) {
			logger.info("isProductionEnvironment:yes");
			return;
		}

		String cssStr = "";
		String jsStr = "";

		cssStr += "<link rel='shortcut icon' href='" + cssBasePath + "image/favicon.png' /> \n ";

		for (String messageKey : commonCss) {
			cssStr += "<link rel='stylesheet' type='text/css' href='" + cssBasePath + messageKey + "' /> \n ";
		}
		for (String messageKey : requiredCss) {
			cssStr += "<link rel='stylesheet' type='text/css' href='" + cssBasePath + messageKey + "' /> \n ";
		}

		for (String messageKey : commonJs) {
			jsStr += "<script type='text/javascript' src='" + jsBasePath + messageKey + "'> </script>\n ";
		}

		for (String messageKey : requiredJs) {
			jsStr += "<script type='text/javascript' src='" + jsBasePath + messageKey + "'> </script> \n ";
		}

		data.put("contextPath", contextPath);
		data.put("cssFile", cssStr);
		data.put("jsFile", jsStr);
	}

	protected void loadCssAndJsNewTheme(HttpServletRequest request) {

		String[] commonCss = commonCssNewTheme();
		String[] requiredCss = requiredCss();
		String[] requiredPageExtaSupportCss = requiredPageExtaSupportCss();

		String[] commonJs = commonJsNewTheme();
		String[] requiredJs = requiredJs();
		String[] requiredPageExtaSupportJs = requiredPageExtaSupportJs();

		final String contextPath = request.getContextPath();
		final String cssBasePath = contextPath + "/dist/assets/";
		final String cssRequiredBasePath = contextPath + "/assets/";

		final String jsBasePath = contextPath + "/dist/assets/";
		final String jsRequiredBasePath = contextPath + "/assets/";

		if (isProductionEnvironment()) {
			logger.info("isProductionEnvironment:yes");
			return;
		}

		String cssStr = "";
		String jsStr = "";

		cssStr += "<link rel='shortcut icon' href='" + cssBasePath + "image/favicon.png' /> \n ";

		for (String messageKey : commonCss) {
			cssStr += "<link rel='stylesheet' type='text/css' href='" + cssBasePath + messageKey + "' /> \n ";
		}

		for (String messageKey : requiredCss) {
			cssStr += "<link rel='stylesheet' type='text/css' href='" + cssRequiredBasePath + messageKey + "' /> \n ";
		}

		for (String messageKey : requiredPageExtaSupportCss) {
			cssStr += "<link rel='stylesheet' type='text/css' href='" + contextPath + messageKey + "' /> \n ";
		}

		for (String messageKey : commonJs) {
			jsStr += "<script type='text/javascript' src='" + jsBasePath + messageKey + "'> </script>\n ";
		}

		for (String messageKey : requiredJs) {
			jsStr += "<script type='text/javascript' src='" + jsRequiredBasePath + messageKey + "'> </script> \n ";
		}

		for (String messageKey : requiredPageExtaSupportJs) {
			jsStr += "<script type='text/javascript' src='" + contextPath + messageKey + "'> </script> \n ";
		}

		data.put("contextPath", contextPath);
		data.put("cssFile", cssStr);
		data.put("jsFile", jsStr);
	}

	protected void setGoogleMapKey() {
		data.put(FieldConstants.CURRENT_LAT, ProjectConstants.BASE_LATITUDE);
		data.put(FieldConstants.CURRENT_LNG, ProjectConstants.BASE_LONGITUDE);
		data.put(FieldConstants.KM_UNITS, StringUtils.valueOf(ProjectConstants.KM_UNITS));
		data.put("google_map_key", ProjectConstants.GOOGLE_PLACE_KEY);
	}

	protected String[] requiredCss() {
		return new String[0];
	}

	protected String[] requiredJs() {
		return new String[0];
	}

	protected String[] requiredPageExtaSupportJs() {
		return new String[0];
	}

	protected String[] requiredPageExtaSupportCss() {
		return new String[0];
	}

	private String[] commonCss() {

		List<String> requiredCss = new ArrayList<String>();

		//@formatter:off
		String commonCssFiles[] = { 
		};
		//@formatter:on

		requiredCss.addAll(new ArrayList<String>(Arrays.asList(commonCssFiles)));

		requiredCss.add("new-ui/css/daterangepicker-bs3.css");
		requiredCss.add("new-ui/css/bootstrap-theme.min.css");
		requiredCss.add("new-ui/css/bootstrap.min.css");
		requiredCss.add("new-ui/css/bootstrap.css");
		requiredCss.add("new-ui/css/plugins.css");
		requiredCss.add("new-ui/css/style.css");
		requiredCss.add("new-ui/css/bootstrap-select.css");
		requiredCss.add("new-ui/css/bootstrap-select.css.map");
		requiredCss.add("new-ui/css/bootstrap.icon-large.min.css");
		requiredCss.add("new-ui/js/jquery-ui-1.10.2/css/ui-lightness/jquery-ui-1.10.2.custom.css");
		requiredCss.add("new-ui/css/all.min.css");

		return requiredCss.toArray(new String[requiredCss.size()]);
	}

	private String[] commonJs() {

		//@formatter:off
		return new String[] { 
				"new-ui/js/css_browser_selector.js", 
				"new-ui/js/jquery-1.11.1.min.js",
				"new-ui/js/jquery-ui-1.10.2/js/jquery-ui-1.10.2.custom.js",
				"new-ui/js/bootstrap.min.js", 
				"new-ui/js/script.js", 
				"new-ui/js/plugins.js", 
				"new-ui/js/main.js", 
				"new-ui/js/common-function.js",
				"new-ui/js/jstz.main.js",
				"new-ui/js/moment.js", 
				"new-ui/js/moment.min.js", 
				"new-ui/js/daterangepicker.js",
				"new-ui/js/bootbox.1.js",
				"new-ui/js/bootstrap-select.js"
		};
		//@formatter:on
	}

	private String[] commonCssNewTheme() {

		List<String> requiredCss = new ArrayList<String>();

		String commonCssFiles[] = {};

		requiredCss.addAll(new ArrayList<String>(Arrays.asList(commonCssFiles)));

		requiredCss.add("css/app.min.css");
		requiredCss.add("css/vendor.min.css");
		requiredCss.add("plugins/tag-it/css/jquery.tagit.css");
		requiredCss.add("plugins/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css");
		requiredCss.add("plugins/bootstrap-daterangepicker/daterangepicker.css");
		requiredCss.add("plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css");
		requiredCss.add("plugins/bootstrap-slider/dist/css/bootstrap-slider.min.css");
		requiredCss.add("plugins/blueimp-file-upload/css/jquery.fileupload.css");
		requiredCss.add("plugins/summernote/dist/summernote-lite.css");
		requiredCss.add("plugins/spectrum-colorpicker2/dist/spectrum.min.css");
		requiredCss.add("plugins/select-picker/dist/picker.min.css");
		requiredCss.add("plugins/jquery-typeahead/dist/jquery.typeahead.min.css");
		requiredCss.add("plugins/datatables.net-bs5/css/dataTables.bootstrap5.min.css");
		requiredCss.add("plugins/datatables.net-buttons-bs5/css/buttons.bootstrap5.min.css");
		requiredCss.add("plugins/datatables.net-responsive-bs5/css/responsive.bootstrap5.min.css");
		requiredCss.add("plugins/bootstrap-table/dist/bootstrap-table.min.css");
		requiredCss.add("myhub-custom-css/fileinput.css");

		return requiredCss.toArray(new String[requiredCss.size()]);
	}

	private String[] commonJsNewTheme() {

		//@formatter:off
		return new String[] { 
			"js/app.min.js",
			"js/vendor.min.js",
			"plugins/jquery-migrate/dist/jquery-migrate.min.js",
			"plugins/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js",
			"plugins/moment/min/moment.min.js",
			"plugins/bootstrap-daterangepicker/daterangepicker.js",
			"plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js",
			"plugins/bootstrap-slider/dist/bootstrap-slider.min.js",
			"plugins/jquery-typeahead/dist/jquery.typeahead.min.js",
			"plugins/jquery.maskedinput/src/jquery.maskedinput.js",
			"plugins/tag-it/js/tag-it.min.js",
			"plugins/blueimp-file-upload/js/vendor/jquery.ui.widget.js",
			"plugins/blueimp-tmpl/js/tmpl.min.js",
			"plugins/blueimp-load-image/js/load-image.all.min.js",
			"plugins/blueimp-canvas-to-blob/js/canvas-to-blob.min.js",
			"plugins/blueimp-gallery/js/jquery.blueimp-gallery.min.js",
			"plugins/blueimp-file-upload/js/jquery.iframe-transport.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-process.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-image.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-audio.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-video.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-validate.js",
			"plugins/blueimp-file-upload/js/jquery.fileupload-ui.js",
			"plugins/summernote/dist/summernote-lite.min.js",
			"plugins/spectrum-colorpicker2/dist/spectrum.min.js",
			"plugins/select-picker/dist/picker.min.js",
			"plugins/@highlightjs/cdn-assets/highlight.min.js",
			"js/demo/highlightjs.demo.js",
			"js/demo/form-plugins.demo.js",
			"js/demo/sidebar-scrollspy.demo.js",
			"plugins/@highlightjs/cdn-assets/highlight.min.js",
			"js/demo/highlightjs.demo.js",
			"plugins/datatables.net/js/jquery.dataTables.min.js",
			"plugins/datatables.net-bs5/js/dataTables.bootstrap5.min.js",
			"plugins/datatables.net-buttons/js/dataTables.buttons.min.js",
			"plugins/datatables.net-buttons/js/buttons.colVis.min.js",
			"plugins/datatables.net-buttons/js/buttons.flash.min.js",
			"plugins/datatables.net-buttons/js/buttons.html5.min.js",
			"plugins/datatables.net-buttons/js/buttons.print.min.js",
			"plugins/datatables.net-buttons-bs5/js/buttons.bootstrap5.min.js",
			"plugins/datatables.net-responsive/js/dataTables.responsive.min.js",
			"plugins/datatables.net-responsive-bs5/js/responsive.bootstrap5.min.js",
			"plugins/bootstrap-table/dist/bootstrap-table.min.js",
			"js/demo/table-plugins.demo.js",
			"myhub-custom-js/fileinput.js",
			"myhub-custom-js/theme.js",
			"myhub-custom-js/bootbox.1.js",
			"myhub-custom-js/new-theme-common-function.js"
		};
		//@formatter:on
	}

	public Response sendDataResponse(Object obj) {
		return Response.ok(obj).build();
	}

	private void noCache(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
		response.setDateHeader("Expires", 1);
	}

	public static String messageForKeyAdmin(String key, String language) {
		return MessagesUitls.messageForKey(key, FrameworkConstants.LANGUAGE_ENGLISH);
	}

	public static String messageForKeyAdmin(String key) {
		return MessagesUitls.messageForKey(key, FrameworkConstants.LANGUAGE_ENGLISH);
	}

	public void validateSessionForNewTheme(HttpServletRequest request, HttpServletResponse response) {

		DbSession session = SessionUtils.getDbSessionObject(request, response, false);

		if (session == null) {
			loginSessionMap = null;
			return;
		}

		loginSessionMap = SessionUtils.sessionAttributes(session);

		if (loginSessionMap == null || loginSessionMap.isEmpty() || !loginSessionMap.containsKey(LoginUtils.USER_ID) || loginSessionMap.get(LoginUtils.USER_ID) == null) {
			loginSessionMap = null;
			return;
		}

		loggedInUserModelViaSession = UserProfileModel.getUserAccountDetailsById(loginSessionMap.get(LoginUtils.USER_ID));

		if (loggedInUserModelViaSession == null) {
			loginSessionMap = null;
			return;
		}

		List<UrlAccessesModel> userUrls = UrlAccessesModel.getUserUrlAccesses(loginSessionMap.get(LoginUtils.USER_ID));

		if (userUrls == null) {

			System.out.println("\n\n\n\tuserUrls null\t");

			loginSessionMap = null;
			return;
		}

		for (UrlAccessesModel urlAccessesModel : userUrls) {
			System.out.println("**** rul access ***"+urlAccessesModel.getUrl());
			loginSessionMap.put(urlAccessesModel.getUrl(), urlAccessesModel.getUrl());
		}

		data.put(LoginUtils.USER_FULL_NAME, loginSessionMap.get(LoginUtils.USER_FULL_NAME));
		data.put(LoginUtils.PHOTO_URL, loginSessionMap.containsKey(LoginUtils.PHOTO_URL) ? loginSessionMap.get(LoginUtils.PHOTO_URL) : ProjectConstants.DEFAULT_IMAGE_ADMIN);
	}
}