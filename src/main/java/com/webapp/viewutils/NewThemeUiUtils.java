package com.webapp.viewutils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jeeutils.StringUtils;
import com.utils.myhub.OrderUtils;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.OrderItemModel;
import com.webapp.models.ProductImageModel;

public class NewThemeUiUtils {

	private final static String HTML_SPACE = "' ";
	private final static String COL_FORM_LABEL = "col-form-label";
	private final static String COL_MAIN_LABEL = "mb-3 row";

	public final static String BUTTON_FLOAT_LEFT = "btn btn-outline-theme";
	public final static String BUTTON_FLOAT_RIGHT = "btn btn-outline-theme float-end";
	public final static String MAIN_BUTTON_CSS_CLASS = "btn btn-outline-theme";
	public final static String OTHER_BUTTON_CSS_CLASS = "btn btn-danger";

	public static String outputCardBodyArrows() {

		StringBuilder html = new StringBuilder();

		html.append("<div class='card-arrow'>");
		html.append("<div class='card-arrow-top-left'></div>");
		html.append("<div class='card-arrow-top-right'></div>");
		html.append("<div class='card-arrow-bottom-left'></div>");
		html.append("<div class='card-arrow-bottom-right'></div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputFormHiddenField(String id, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<input type='hidden' id='").append(id).append("' name='").append(id).append("' value='").append(value).append("'/>");
		return html.toString();
	}

	public static String outputPageHeader(String title, String icon) {
		return outputPageHeaderMain(title, icon, "");
	}

	public static String outputPageHeader(String title, String icon, String additionalNote) {
		return outputPageHeaderMain(title, icon, additionalNote);
	}

	private static String outputPageHeaderMain(String title, String icon, String additionalNote) {

		StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<h1 class='page-header'>");
			html.append("<i class='").append(icon).append("'></i>");
			html.append(title);
			html.append("<small>").append(additionalNote).append("</small>");
		html.append("</h1>");
		html.append("<hr class='mb-4'>");
		//@formatter:on

		return html.toString();
	}

	public static String outputButton(String id, String label, String class1) {

		StringBuilder sb = new StringBuilder();

		sb.append("<div>");
		sb.append("<button id='").append(id).append("' type='submit' class='me-2 ").append(BUTTON_FLOAT_LEFT).append(" ").append(class1).append("'>").append(label).append("</button>");
		sb.append("</div>");

		return sb.toString();
	}

	public static String outputButtonSingleField(String buttonId, String buttonLabel, String buttonDivCssClass, String buttonCssClass) {

		StringBuilder html = new StringBuilder();
		html.append("<button id='").append(buttonId).append("' type='submit' style='margin:5px;' class='").append(buttonCssClass).append("'>").append(buttonLabel).append("</button>");
		return html.toString();
	}

	public static String outputButtonDoubleField(String button1stId, String button1stLabel, String button1stCss, String button2ndId, String button2ndLabel, String button2ndCss, String buttonDivCssClass) {

		StringBuilder sb = new StringBuilder();

		sb.append("<div class='").append(buttonDivCssClass).append("'>");
		sb.append("<button id='").append(button1stId).append("' type='submit' class='me-2 ").append(button1stCss).append("'>").append(button1stLabel).append("</button>");
		sb.append("<button id='").append(button2ndId).append("' type='submit' class='me-2 ").append(button2ndCss).append("'>").append(button2ndLabel).append("</button>");
		sb.append("</div>");

		return sb.toString();
	}

	public static String outputButtonTripleField(String button1stId, String button1stLabel, String button1stCss, String button2ndId, String button2ndLabel, String button2ndCss, String button3rdId, String button3rdLabel, String button3rdCss, String buttonDivCssClass) {

		StringBuilder sb = new StringBuilder();

		sb.append("<div class='").append(buttonDivCssClass).append("'>");
		sb.append("<button id='").append(button1stId).append("' type='submit' class='me-2 ").append(button1stCss).append("'>").append(button1stLabel).append("</button>");
		sb.append("<button id='").append(button2ndId).append("' type='submit' class='me-2 ").append(button2ndCss).append("'>").append(button2ndLabel).append("</button>");
		sb.append("<button id='").append(button3rdId).append("' type='submit' class='me-2 ").append(button3rdCss).append("'>").append(button3rdLabel).append("</button>");
		sb.append("</div>");

		return sb.toString();
	}

	public static String outputFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<div class='mb-3'>");
		html.append("<label class='form-label'>" + label + (isRequired ? "<span class='text-danger'>*</span>" : "") + "</label>");
		html.append("<input type='").append(type).append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("value='").append(value).append(HTML_SPACE);
		html.append("class='form-control form-control-lg bg-inverse bg-opacity-5");

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		html.append("placeholder='" + label + "'>");

		appendErrorBlock(id, map, html);

		html.append("</div>");

		return html.toString();
	}

	public static String outputInputField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String inputCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("' ").append(" id='").append(id).append("Parent'").append(">");

		if (label != null) {

			html.append("<label class='").append(COL_FORM_LABEL);

			if (StringUtils.validString(labelCssClass)) {
				html.append(" ").append(labelCssClass);
			}

			html.append("'>").append(label);

			if (isRequired) {
				html.append("<span class='text-danger'>*</span>");
			}

			html.append("</label>");
		}

		html.append("<div class='").append(inputCssClass).append("'>");

		html.append("<input type='").append(type).append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("value='").append(value).append(HTML_SPACE);
		html.append("class='form-control");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		if (label != null) {
			html.append("placeholder='").append(label).append("'");
		}

		html.append(">");

		appendErrorBlock(id, map, html);

		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputInputTextAreaField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String inputCssClass, int rows) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");

		if (label != null) {

			html.append("<label class='").append(COL_FORM_LABEL);

			if (StringUtils.validString(labelCssClass)) {
				html.append(" ").append(labelCssClass);
			}

			html.append("'>").append(label);

			if (isRequired) {
				html.append("<span class='text-danger'>*</span>");
			}

			html.append("</label>");
		}

		html.append("<div class='").append(inputCssClass).append("'>");

		html.append("<textarea type='").append(type).append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("class='form-control");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		html.append("placeholder='").append(label).append(HTML_SPACE);
		html.append("rows='").append(rows).append("'>");
		html.append(value);
		html.append("</textarea>");

		appendErrorBlock(id, map, html);

		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputInputFieldForPhoneNumber(String id, String id1, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String inputCssClass) {

		String value = map.get(id);
		String options = map.get(id1 + "Options");

		if (value == null) {
			value = "";
		}

		if (options == null) {
			options = "";
		}

		StringBuilder html = new StringBuilder();
		  // Adding CSS for dark mode specifically for the dropdown (removing the body and general styles)
        html.append("<style>")
            .append("[data-bs-theme='dark'] .custom-dropdown select {")
            .append("background-color: rgba(29, 40, 53, 0.3);")
            .append("color: rgba(255, 255, 255, 0.75);")
            .append("}")
            .append("[data-bs-theme='dark'] .custom-dropdown select option {")
            .append("background-color: rgba(29, 40, 53, 0.3);")
            .append("color: rgba(255, 255, 255, 0.75);")
            .append("}")
            .append("[data-bs-theme='dark'] .custom-dropdown select:hover, [data-bs-theme='dark'] .custom-dropdown select:focus {")
            .append("background-color: #1d2835;")
            .append("color: #ffffff;")
            .append("}")
            .append("</style>");

		html.append("<div class='").append(COL_MAIN_LABEL).append(" custom-dropdown'>");

		html.append("<label class='").append(COL_FORM_LABEL);

		if (StringUtils.validString(labelCssClass)) {
			html.append(" ").append(labelCssClass);
		}

		html.append("'>").append(label);

		if (isRequired) {
			html.append("<span class='text-danger'>*</span>");
		}

		html.append("</label>");

		html.append("<div class='").append(inputCssClass).append("'>");

		html.append("<select class='form-control mb-2");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		html.append(HTML_SPACE);
		html.append("id='").append(id1).append(HTML_SPACE);
		html.append("name='").append(id1).append("'>");
		html.append(options);
		html.append("</select>");

		html.append("<input type='").append(type).append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("value='").append(value).append(HTML_SPACE);
		html.append("class='form-control");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		html.append("placeholder='").append(label).append("'>");

		appendErrorBlock(id, map, html);

		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputSelectInputFieldForFilters(LinkedHashMap<String, Boolean> filterOptionIds, HashMap<String, String> map, String inputCssClass) {

		String options;
		StringBuilder html = new StringBuilder();
		
		 // Adding dark mode CSS within the function
        html.append("<style>")
            .append("[data-bs-theme='dark'] .custom-dropdown select {")
            .append("background-color:rgba(29, 40, 53, 0.3);")
            .append("color: rgba(255, 255, 255, 0.75);")
            .append("}")
            .append("[data-bs-theme='dark'] .custom-dropdown select option {")
            .append("background-color: rgba(29, 40, 53, 0.3);")
            .append("color: rgba(255, 255, 255, 0.75);")
            .append("}")
            .append("[data-bs-theme='dark'] .custom-dropdown select:hover, [data-bs-theme='dark'] .custom-dropdown select:focus {")
            .append("background-color: #1d2835;")
            .append("color: #ffffff;")
            .append("}")
            .append("</style>");

		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");

		for (Map.Entry<String, Boolean> mapElement : filterOptionIds.entrySet()) {

			String id = mapElement.getKey();
			boolean isMultiSelect = mapElement.getValue();

			options = map.get(id + "Options");

			if (options == null) {
				options = "";
			}

			html.append("<div id='").append(id).append("Div' class='").append(inputCssClass).append(" custom-dropdown'>");

			if (isMultiSelect) {
				html.append("<select class='selectpicker form-control' id='").append(id).append("' name='").append(id).append("' multiple>");
			} else {
				html.append("<select class='form-control mb-2 ").append(inputCssClass).append("' ").append("id='").append(id).append("' name='").append(id).append("'>");
			}

			html.append(options);
			html.append("</select>");
			html.append("</div>");
		}

		html.append("</div>");

		return html.toString();
	}


	public static String outputSelectInputField(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String labelCssClass, String inputCssClass) {

	    String options = map.get(id + "Options");

	    if (options == null) {
	        options = "";
	    }

	    StringBuilder html = new StringBuilder();

	    // Adding CSS for dark mode specifically for the dropdown (removing the body and general styles)
	    html.append("<style>")
	        .append("[data-bs-theme='dark'] .custom-dropdown select {")
	        .append("background-color: rgba(29, 40, 53, 0.3);")
	        .append("color: rgba(255, 255, 255, 0.75);")
	        .append("}")
	        .append("[data-bs-theme='dark'] .custom-dropdown select option {")
	        .append("background-color: rgba(29, 40, 53, 0.3);")
	        .append("color: rgba(255, 255, 255, 0.75);")
	        .append("}")
	        .append("[data-bs-theme='dark'] .custom-dropdown select:hover, [data-bs-theme='dark'] .custom-dropdown select:focus {")
	        .append("background-color: #1d2835;")
	        .append("color: #ffffff;")
	        .append("}")
	        .append("</style>");

	    // Generating the dropdown structure with the correct classes
	    html.append("<div id='").append(id).append("Div' class='").append(COL_MAIN_LABEL).append(" custom-dropdown'>");

	    if (label != null) {
	        html.append("<label class='").append(COL_FORM_LABEL);

	        if (StringUtils.validString(labelCssClass)) {
	            html.append(" ").append(labelCssClass);
	        }

	        html.append("'>").append(label);

	        if (isRequired) {
	            html.append("<span class='text-danger'>*</span>");
	        }

	        html.append("</label>");
	    }

	    html.append("<div class='").append(inputCssClass).append("'>");

	    html.append("<select class='form-control mb-2");

	    if (StringUtils.validString(inputCssClass)) {
	        html.append(" ").append(inputCssClass);
	    }

	    html.append("' id='").append(id).append("' name='").append(id).append("'>");
	    html.append(options);
	    html.append("</select>");

	    appendErrorBlock(id, map, html);

	        html.append("</div>");
	    html.append("</div>");

	    return html.toString();
	}


	public static String outputSelectMultiInputField(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String labelCssClass, String inputCssClass) {

		String options = map.get(id + "Options");

		if (options == null) {
			options = "";
		}

		StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div id='").append(id).append("Div' class='").append(COL_MAIN_LABEL).append("'>");
				
				if (label != null) {
					html.append("<label class='form-label ").append(labelCssClass).append("'>").append(label);
					if (isRequired) {
						html.append("<span class='text-danger'>*</span>");
					}
					html.append("</label>");
				}
				
				html.append("<div class='").append(inputCssClass).append("'>");
					html.append("<select class='selectpicker form-control' id='" + id + "' name='" + id + "' multiple>");
						html.append(options);
					html.append("</select>");
					
					appendErrorBlock(id, map, html);
					
				html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	private static void appendErrorBlock(String id, HashMap<String, String> map, StringBuilder html) {

		if (map.get(id + "Error") != null) {
			html.append("<small class='form-text text-muted' ").append("id='").append(id + "Error").append("'>").append(map.get(id + "Error")).append("</small>");
		}
	}

	public static String outputDatatable(String id, String tableCssClass, List<String> columnNames) {

		StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<table id='").append(id).append("' class='table text-nowrap w-100").append(tableCssClass).append("'>");
			html.append("<thead>");
				html.append("<tr>");
					for (String columnName : columnNames) {
						html.append("<th>").append(columnName).append("</th>");
					}
				html.append("</tr>");
			html.append("</thead>");
			html.append("<tbody>");
			html.append("</tbody>");
		html.append("</table>");
		//@formatter:on

		return html.toString();
	}

	public static String outputDatatableTimeRangePicker(String id, String divCssClas) {

		StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='").append(divCssClas).append("'>");
			html.append("<div id='").append(id).append("' class='").append(MAIN_BUTTON_CSS_CLASS).append(" d-flex align-items-center text-start'>");
				html.append("<span class='text-truncate'>&nbsp;</span>");
				html.append("<i class='fa fa-caret-down ms-auto'></i>");
			html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String outputLinSepartor() {
		return ("<hr class='mb-4'>");
	}

	public enum OUTPUT_BUTTON_TYPES {
		VIEW, ADD, EDIT, DELETE, REACTIVATE, DEACTIVATE, APPROVE, REJECT
	}

	public static String outputFormButtonLink(OUTPUT_BUTTON_TYPES type, String title, String url) {

		StringBuilder html = new StringBuilder();

		html.append("<a data-toggle='tooltip' title='").append(title).append("' ");

		switch (type) {

		case VIEW:
			html.append("href='").append(url).append("'>");
			html.append("<i class='fas fa-lg fa-fw me-2 fa-eye'></i>");
			break;

		case EDIT:
			html.append("href='").append(url).append("'>");
			html.append("<i class='far fa-lg fa-fw me-2 fa-edit'></i>");
			break;

		case DELETE:
			html.append("href=\"javascript:deleteItem('" + url + "');\">");
			html.append("<i class='fas fa-lg fa-fw me-2 fa-trash-alt'></i>");
			break;

		case REACTIVATE:
			html.append("href=\"javascript:reactivateItem('" + url + "');\">");
			html.append("<i class='fas fa-lg fa-fw me-2 fa-retweet'></i>");
			break;

		case DEACTIVATE:
			html.append("href=\"javascript:deactivateItem('" + url + "');\">");
			html.append("<i class='fas fa-lg fa-fw me-2 fa-ban'></i>");
			break;
		
		case APPROVE:
			html.append("href=\"javascript:deactivateItem('" + url + "');\">");
			html.append("<i class='fa fa-check-circle' aria-hidden='true'></i>");
			break;
		case REJECT:
			html.append("href=\"javascript:deactivateItem('" + url + "');\">");
			html.append("<i class='fa fa-times-circle' aria-hidden='true'></i>");
			break;
		 

		default:
			break;
		}

		html.append("</a>");
		
		return html.toString();
	}

	public static String outputFormButtonLinkCustom(String title, String url, String icon) {

		StringBuilder html = new StringBuilder();
		html.append("<a data-toggle='tooltip' title='").append(title).append("' href='").append(url).append("'>");
		html.append("<i class='").append(icon).append("'></i>");
		html.append("</a>");
		return html.toString();
	}

	public static String outputDeleteFormButtonLink(String userId, String userType, String url, String type) {

		if (type == null) {
			return "";
		} else if (type.trim().equalsIgnoreCase("")) {
			return "";
		}

		String html = "";

		html = "<a data-toggle='tooltip'";

		if (type != null && type.trim().equalsIgnoreCase("userDeactivate")) {
			html += " href=\"javascript:actionUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Deactivate' > <i class='fas fa-lg fa-fw me-2 fa-ban'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userReactivate")) {
			html += " href=\"javascript:actionUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Reactivate' > <i class='fas fa-lg fa-fw me-2 fa-retweet'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userApproved")) {
			html += " href=\"javascript:approveUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Approve' > <i class='far fa-lg fa-fw me-2 fa-check-square'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userNotApproved")) {
			html += " href=\"javascript:approveUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Not Approved' > <i class='fas fa-lg fa-fw me-2 fa-ban'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscriptionDeactivate")) {
			html += " href=\"javascript:actionVendorAction('" + userId + "' ,'" + userType + "','" + url
						+ "', 'Are you sure you want to deactivate driver subscription in booking flow?');\" title='Deactivate Driver Subscription In Booking Flow' > <i class='fas fa-lg fa-fw me-2 fa-ban'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscriptionReactivate")) {
			html += " href=\"javascript:actionVendorAction('" + userId + "' ,'" + userType + "','" + url
						+ "', 'Are you sure you want to activate driver subscription in booking flow?');\" title='Reactivate Driver Subscription In Booking Flow' > <i class='fas fa-lg fa-fw me-2 fa-retweet'></i> ";
		} else if (type != null && (type.trim().equalsIgnoreCase("take-ride") || type.trim().equalsIgnoreCase("untake-ride"))) {
			String title = "";
			if (type.trim().equalsIgnoreCase("take-ride")) {
				title = BusinessAction.messageForKeyAdmin("labelTakeBookings");
			} else {
				title = BusinessAction.messageForKeyAdmin("labelUntakeBookings");
			}
			html += " href=\"javascript:takeRide('" + url + "', '" + type + "');\" title='" + title + "' > <i class='fas fa-lg fa-fw me-2 fa-book'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("recharge")) {
			html += " href=\"javascript:rechargeAccount('" + url + "');\" title='Recharge'> <i class='fas fa-lg fa-fw me-2 fa-rupee-sign'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("approve")) {
			html += " href=\"javascript:approveRequest('" + url + "');\" title='Approve'> <i class='fas fa-lg fa-fw me-2 fa-thumbs-up'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("transfer")) {
			html += " href=\"javascript:transferRequest('" + url + "');\" title='Transfer'> <i class='fas fa-lg fa-fw me-2 fa-long-arrow-alt-right'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("reject")) {
			html += " href=\"javascript:rejectRequest('" + url + "');\" title='Reject'> <i class='fas fa-lg fa-fw me-2 fa-trash-alt'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("encash")) {
			html += " href=\"javascript:encashRequest('" + url + "');\" title='Encash'> <i class='fas fa-lg fa-fw me-2 fa-money-bill-alt'></i> ";
		}

		html += " </a>";

		return html;
	}

	public enum OUTPUT_BADGE_TYPES {
		PRIMARY, SECONDARY, ACTIVE, DEACTIVE, WARNING, INFO, LIGHT, DARK,PENDING
	}

	public static String outputBadge(OUTPUT_BADGE_TYPES type, String text) {

		final StringBuilder html = new StringBuilder();

		switch (type) {

		case PRIMARY:
			html.append("<span class='badge bg-primary'>").append(text).append("</span>");
			break;
		case SECONDARY:
			html.append("<span class='badge bg-secondary'>").append(text).append("</span>");
			break;
		case ACTIVE:
			html.append("<span class='badge bg-success'>").append(text).append("</span>");
			break;
		case DEACTIVE:
			html.append("<span class='badge bg-danger'>").append(text).append("</span>");
			break;
		case WARNING:
			html.append("<span class='badge bg-warning'>").append(text).append("</span>");
			break;
		case INFO:
			html.append("<span class='badge bg-info'>").append(text).append("</span>");
			break;
		case LIGHT:
			html.append("<span class='badge bg-light'>").append(text).append("</span>");
			break;
		case DARK:
			html.append("<span class='badge bg-dark'>").append(text).append("</span>");
		case PENDING:
			html.append("<span class ='badge bg-danger'>").append(text).append("</span>");
			break;

		default:
			break;
		}

		return html.toString();
	}

	public static String outputErrorDiv(String errorMessage) {

		final StringBuilder html = new StringBuilder();

		html.append("<div id='error-msg'>");
		html.append("<div class='alert alert-danger m-b-0 m-t-15'>");
		html.append(errorMessage);
		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputErrorBaner(String id, String message, String cssClass) {

		final StringBuilder html = new StringBuilder();
		html.append("<div id='").append(id).append("' style='display: none;' class='").append(cssClass).append("'>");
		html.append("<div class='alert alert-danger m-b-0 m-t-15'>");
		html.append(message);
		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputImageUploadField(String id, String label, HashMap<String, String> map, String labelCssClass, String inputCssClass) {

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");
				
				if (label != null) {
					html.append("<label class='form-label ").append(labelCssClass).append("'>").append(label);
					html.append("</label>");
				}
				
				html.append("<div class='").append(inputCssClass).append("'>");
					html.append("<input id='" + id + "' name='inputgly[]' type='file' multiple class='file-loading'>");
					appendErrorBlock(id, map, html);
				html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String outputStaticField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String inputCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		final StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");

		html.append("<label class='").append(COL_FORM_LABEL);

		if (StringUtils.validString(labelCssClass)) {
			html.append(" ").append(labelCssClass);
		}

		html.append("'>").append(label);

		if (isRequired) {
			html.append("<span class='text-danger'>*</span>");
		}

		html.append("</label>");

		html.append("<div class='").append(inputCssClass).append("'>");

		html.append("<input type='").append(type).append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("value='").append(value).append(HTML_SPACE);
		html.append("readonly=''");
		html.append("class='form-control-plaintext");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		html.append("placeholder='").append(label).append("'>");

		appendErrorBlock(id, map, html);

		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputStaticField(String label, String fieldId, String labelCssClass, String inputCssClass) {

		final StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");
		html.append("<label class='").append(COL_FORM_LABEL);
		if (StringUtils.validString(labelCssClass)) {
			html.append(" ").append(labelCssClass);
		}
		html.append("'>").append(label);
		html.append("</label>");

		html.append("<div class='").append(inputCssClass).append(" mt-2' id='").append(fieldId).append("'>");
		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}
	
	public static String outputStaticField(String label, String fieldId, String value, String labelCssClass, String inputCssClass) {

		final StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("'>");
		html.append("<label class='").append(COL_FORM_LABEL);
		if (StringUtils.validString(labelCssClass)) {
			html.append(" ").append(labelCssClass);
		}
		html.append("'>").append(label);
		html.append("</label>");

		html.append("<div class='").append(inputCssClass).append(" mt-2' id='").append(fieldId).append("'>");
		html.append(value);
		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputStatisticsField(String id, String label, HashMap<String, String> map, String cssClass, String iconClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='row'>");
			html.append("<div class='col-xl-8'>");
				html.append("<a href='#' class='card text-decoration-none ").append(cssClass).append("'>");
					html.append("<div class='card-body d-flex align-items-center text-inverse m-5px bg-inverse bg-opacity-10'>");
						html.append("<div class='flex-fill'>");
							html.append("<div class='mb-1'>").append(label).append("</div>");
							html.append("<h2>").append(value).append("</h2>");
						html.append("</div>");
						html.append("<div class='opacity-5'>");
							html.append("<i class='").append(iconClass).append(" fa-3x'></i>");
						html.append("</div>");
					html.append("</div>");
					html.append(outputCardBodyArrows());
				html.append("</a>");
			html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String outputGoogleMapField(String id, String label, String css) {

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='card'>");
			html.append("<div class='m-1 bg-inverse bg-opacity-10'>");
				html.append("<div class='card-header fw-bold'>").append(label).append("</div>");
				html.append("<div id='").append(id).append("' style='border:0; width: 100%; ").append(css).append("'></div>");
			html.append("</div>");
			html.append(outputCardBodyArrows());
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String getRequiredField() {
		return "<span class='text-danger'>*</span>";
	}

	public static String outputTimePickerInputField(String id, String label, boolean isRequired, HashMap<String, String> map, String labelCssClass, String inputCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		final StringBuilder html = new StringBuilder();
		html.append("<div class='").append(COL_MAIN_LABEL).append("' ").append(" id='").append(id).append("Parent'").append(">");

		html.append("<label class='").append(COL_FORM_LABEL);

		if (StringUtils.validString(labelCssClass)) {
			html.append(" ").append(labelCssClass);
		}

		html.append(HTML_SPACE);
		html.append(" id='").append(id).append("Label'");
		html.append(">").append(label);

		if (isRequired) {
			html.append("<span class='text-danger'>*</span>");
		}

		html.append("</label>");

		html.append("<div class='bootstrap-timepicker timepicker ").append(inputCssClass).append("'>");

		html.append("<input type='").append("text").append(HTML_SPACE);
		html.append("id='").append(id).append(HTML_SPACE);
		html.append("name='").append(id).append(HTML_SPACE);
		html.append("value='").append(value).append(HTML_SPACE);
		html.append("readonly=''");
		html.append("class='form-control");

		if (StringUtils.validString(inputCssClass)) {
			html.append(" ").append(inputCssClass);
		}

		if (map.get(id + "Error") != null) {
			html.append(" ").append("is-invalid");
		}

		html.append(HTML_SPACE);

		html.append(">");

		html.append("</div>");
		html.append("</div>");

		return html.toString();
	}

	public static String outputCheckbox(String id, String label, HashMap<String, String> map) {

		String value = map.get(id);

		boolean flagChecked = true;

		if (value == null) {
			flagChecked = false;
			value = "";
		}

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='form-group mb-4'>");
			html.append("<div class='form-check'>");
				html.append("<input class='form-check-input' type='checkbox' value='").append(value).append("' ").append(flagChecked ? "checked" : "").append(" id='").append(id).append("' ").append("name='").append(id).append("'>");
				if (label != null) {
					html.append("<label class='form-check-label' for='defaultCheck1'>" + label + "</label>");
				}
			html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String getProductDetailsDiv(HttpServletRequest request, OrderItemModel orderItemModel, AdminSettingsModel adminSettingsModel) {

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='row align-items-center'>");
			html.append("<div class='col-lg-6 d-flex align-items-center'>");
				html.append("<div class='h-65px w-65px d-flex align-items-center justify-content-center position-relative bg-white p-1'>");
					html.append("<img src='").append(request.getContextPath()).append("/").append(orderItemModel.getProductImage()).append("' alt='' class='mw-100 mh-100'>");
					html.append("<span class='w-20px h-20px p-0 d-flex align-items-center justify-content-center badge bg-theme text-theme-color position-absolute end-0 top-0 fw-bold fs-12px rounded-pill mt-n2 me-n2'>").append(orderItemModel.getNumberOfItemsOrdered()).append("</span>");
				html.append("</div>");
				html.append("<div class='ps-3 flex-1'>");
					html.append("<div class=''><a href='#' class='text-decoration-none text-inverse'>").append(orderItemModel.getProductName()).append("</a></div>");
					html.append("<div class='text-inverse text-opacity-50 small'>");
						html.append(BusinessAction.messageForKeyAdmin("labelSKU")).append(" ").append(orderItemModel.getProductSku());
						html.append(orderItemModel.getProductInformation());
					html.append("</div>");
				html.append("</div>");
			html.append("</div>");
			html.append("<div class='col-lg-1 m-0 ps-lg-3'>");
				html.append(orderItemModel.getNumberOfItemsOrdered() + " * " + adminSettingsModel.getCurrencySymbol() + StringUtils.valueOf(orderItemModel.getProductDiscountedPrice()) + "<small> (<span class='text-decoration-line-through'>" + adminSettingsModel.getCurrencySymbol() + StringUtils.valueOf(orderItemModel.getProductActualPrice()) + "</span>)</small>");
			html.append("</div>");
			html.append("<div class='col-lg-1 text-inverse m-0 text-end'>");
				html.append(StringUtils.valueOf(orderItemModel.getProductWeight()) + " " + OrderUtils.getProductWeightUnit(orderItemModel.getProductWeightUnit()));
			html.append("</div>");
			html.append("<div class='col-lg-3 text-inverse m-0 text-end'>");
				html.append(orderItemModel.getProductSpecification());
			html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}

	public static String outputRatingFields(String id, String label, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		return "<input id='" + id + "' class='rating-loading' value='" + value + "'/>";
	}
	
	public static String getImageDiv(HttpServletRequest request, ProductImageModel productImageModel) {
		
		final StringBuilder html = new StringBuilder();
		
		html.append("<div class='row align-items-center'>");
			html.append("<div class='col-lg-6 d-flex align-items-center'>");
				html.append("<div class='h-65px w-65px d-flex align-items-center justify-content-center position-relative bg-white p-1'>");
					html.append("<img src='").append(request.getContextPath()).append("/").append(productImageModel.getProductImageUrl()).append("' alt='' class='mw-100 mh-100'>");
				html.append("</div>");
			html.append("</div>");
		html.append("</div>");
		
		return html.toString();
	}
	
	public static String dashboardDataTable(String id, String tableCssClass, List<String> columnNames, boolean restrictRows, String height, String width) {
		
		StringBuilder html = new StringBuilder();

		html.append("<div class='card' style='width: ").append(width).append("; height: ").append(height).append(";'>");
			html.append("<div class='card-body' style='height: 100%;'>");
				html.append("<div class='table-responsive'");
				
					if (restrictRows) {
						html.append(" style='max-height: calc(100% - 20px); overflow-y: auto;'"); // Keeping a fixed height minus some padding/margin
					} else {
						html.append(" style='height: 100%; overflow: hidden;'"); // Direct height setting when no scroll needed
					}
					html.append(">");
					
					html.append("<table id='").append(id).append("' class='table text-nowrap w-100 ").append(tableCssClass).append("'>");
						html.append("<thead>");
							html.append("<tr>");
								html.append("<th colspan='").append(columnNames.size()).append("' style='text-align: left;'>");
									html.append("<button id='enlargeModalBtn' class='btn' >");
										html.append("<i class='fa-solid fa-expand'></i>");
									html.append("</button>");
								html.append("</th>");
							html.append("</tr>");
							html.append("<tr>");
								for (String columnName : columnNames) {
									html.append("<th>").append(columnName).append("</th>");
								}
							html.append("</tr>");
						html.append("</thead>");
					html.append("</table>");
				html.append("</div>");
			html.append("</div>");
			html.append(outputCardBodyArrows());
		html.append("</div>");

		return html.toString();
	}

	public static String outputChart(String id, String chartTitle, String chartType) {
		
		StringBuilder html = new StringBuilder();
		/*
		html.append("<div class='card' style='width: ").append(1).append("; height: ").append(height).append(";'>");
			html.append("<div class='card-body' style='height: 100%;'>");
				html.append("<h5 class='card-title'>").append(chartTitle).append("</h5>");
					String canvasStyle = "width: 100%; height: calc(100% - 40px);"; // Adjusting height to fit within the card body
					html.append("<div style='width: 100%; height: calc(100% - 20px);'>");
						html.append("<canvas id='").append(id).append("' style='").append(canvasStyle).append("'></canvas>");
					html.append("</div>");
					html.append(outputCardBodyArrows());
			html.append("</div>");
		html.append("</div>");
		*/
		html.append("<div class='card' style='display: flex; flex-direction: column; height: 100%; width: 100%;'>");
	       	html.append("<div class='card-body' style='flex-grow: 1; display: flex; flex-direction: column;'>");
	       		html.append("<h5 class='card-title'>").append(chartTitle).append("</h5>");
	       			html.append("<div style='flex-grow: 1; display: flex; align-items: center; justify-content: center;'>");
	       				html.append("<canvas id='").append(id).append("' style='width: 100%; height: 100%;'></canvas>");
	       			html.append("</div>");
	       			html.append(outputCardBodyArrows());
	       	html.append("</div>");
	        html.append("</div>");
	        
		return html.toString();
	}
	
	public static String dashboarStatisticsField(String id, String label, HashMap<String, String> map, String cssClass, String iconClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		final StringBuilder html = new StringBuilder();

		//@formatter:off
		html.append("<div class='row'>");
			html.append("<div>");
				html.append("<a href='#' class='card text-decoration-none ").append(cssClass).append("'>");
					html.append("<div class='card-body d-flex align-items-center text-inverse m-5px bg-inverse bg-opacity-10'>");
						html.append("<div class='flex-fill'>");
							html.append("<div class='mb-1'>").append(label).append("</div>");
							html.append("<h2>").append(value).append("</h2>");
						html.append("</div>");
						html.append("<div class='opacity-5'>");
							html.append("<i class='").append(iconClass).append(" fa-3x'></i>");
						html.append("</div>");
					html.append("</div>");
					html.append(outputCardBodyArrows());
				html.append("</a>");
			html.append("</div>");
		html.append("</div>");
		//@formatter:on

		return html.toString();
	}
	
	public static void addQuantityColumn(List<String> columnNames) {
		    columnNames.add("Quantity");
	}
	public static String outputChart12(String id, String chartTitle, String chartType, boolean customSize, String customHeight, String customWidth) {

	    String widthStyle;
	    String heightStyle;

	    if (customSize) {
	        widthStyle = (customWidth != null) ? customWidth : "100%";
	        heightStyle = (customHeight != null) ? customHeight : "100%";
	    } else {
	        widthStyle = "100%";
	        heightStyle = "100%";
	    }

	    final StringBuilder html = new StringBuilder();

	    //@formatter:off
	    html.append("<div class='card' style='display: flex; flex-direction: column; height: ").append(heightStyle).append("; width: ").append(widthStyle).append("; '>");
	        html.append("<div class='card-body' style='flex-grow: 1; display: flex; flex-direction: column;'>");
	            html.append("<h5 class='card-title'>").append(chartTitle).append("</h5>");
	            html.append("<div style='flex-grow: 1; display: flex; align-items: center; justify-content: center;'>");
	                html.append("<canvas id='").append(id).append("' style='width: 100%; height: 100%;'></canvas>");
	            html.append("</div>");
	            html.append(outputCardBodyArrows());
	        html.append("</div>");
	        html.append("</div>");
	   
	    //@formatter:on

	    return html.toString();
	}
	
	public static String dashboardDataTable12(String id, String tableCssClass, List<String> columnNames, boolean restrictRows, String height, String width) {
	    StringBuilder html = new StringBuilder();

	    // Create the card container
	    html.append("<div class='card' style='width: ").append(width).append("; height: ").append(height).append(";'>");
	    html.append("<div class='card-body' style='height: 100%;'>");

	    // Add responsive styling for the table container
	    html.append("<div class='table-responsive'");
	    if (restrictRows) {
	        html.append(" style='max-height: calc(100% - 20px); overflow-y:hidden; overflow-x: auto;'"); // Enable vertical and horizontal scrollbars
	    } else {
	        html.append(" style='height: 100%; overflow: hidden;'");
	    }
	    html.append(">");

	    // Start the table
	    html.append("<table id='").append(id).append("' class='table text-nowrap w-100 ").append(tableCssClass).append("'>");
	    html.append("<thead>");
	    html.append("<tr>");
	    
	    // Insert the enlarge button in the top-right corner using absolute positioning
	    html.append("<th colspan='").append(columnNames.size()).append("' style='position: absolute; top: 5px; right: 5px; text-align: right; padding: 2px;'>");
	    html.append("<button id='enlargeModalBtn' class='btn p-0' style='font-size: 0.8em;'>");
	    html.append("<i class='fa-solid fa-expand'></i>");
	    html.append("</button>");
	    html.append("</th>");
	    
	    html.append("</tr>");
	    html.append("<tr>");
	    
	    // Add small space above the column headers
	    for (String columnName : columnNames) {
	        html.append("<th style='padding-top: 7px; white-space: normal;'>").append(columnName).append("</th>");
	    }
	    
	    html.append("</tr>");
	    html.append("</thead>");
	    html.append("</table>");
	    html.append("</div>");  // Close table-responsive div
	    html.append("</div>");  // Close card-body div
	    html.append(outputCardBodyArrows());
	    html.append("</div>");  // Close card div

	    return html.toString();
	}




	
	public static String outputChart1(String id, String chartTitle, String chartType, boolean customSize, String customHeight, String customWidth) {

	    String widthStyle;
	    String heightStyle;

	    if (customSize) {
	        widthStyle = (customWidth != null) ? customWidth : "100%";
	        heightStyle = (customHeight != null) ? customHeight : "100%";
	    } else {
	        widthStyle = "100%";
	        heightStyle = "100%";
	    }

	    final StringBuilder html = new StringBuilder();

	    //@formatter:off
	    html.append("<div class='card' style='display: flex; flex-direction: column; height: ").append(heightStyle).append("; width: ").append(widthStyle).append("; '>");
	        html.append("<div class='card-body' style='flex-grow: 1; display: flex; flex-direction: column;'>");
	            // Container for the title and date range picker
	            html.append("<div style='display: flex; justify-content: space-between; align-items: center;'>");
	                html.append("<h5 class='card-title'>").append(chartTitle).append("</h5>");
	                // Adding the date range picker at the top right corner
	                html.append(outputDatatableTimeRangePicker("customDateRange", "col-sm-3"));
	            html.append("</div>");
	            html.append("<div style='flex-grow: 1; display: flex; align-items: center; justify-content: center;'>");
	                html.append("<canvas id='").append(id).append("' style='width: 100%; height: 100%;'></canvas>");
	            html.append("</div>");
	            html.append(outputCardBodyArrows());
	        html.append("</div>");
	    html.append("</div>");
	    //@formatter:on

	    return html.toString();
	}
	
	public static String outputDatatable12(String id, String tableCssClass, List<String> columnNames) {
	    StringBuilder html = new StringBuilder();

	    html.append("<table id='").append(id).append("' class='table-bordered custom-table w-100 ").append(tableCssClass).append("'>");
	    html.append("<thead>");
	    html.append("<tr>");
	    for (String columnName : columnNames) {
	        html.append("<th>").append(columnName).append("</th>");
	    }
	    html.append("</tr>");
	    html.append("</thead>");
	    html.append("<tbody>");
	    html.append("</tbody>");
	    html.append("</table>");

	    return html.toString();
	}
	

	public static String outputInventoryChart(String id, String chartTitle, String chartType) {
	    StringBuilder html = new StringBuilder();

	    html.append("<div class='card' style='display: flex; flex-direction: column; height: 100%; width: 100%;'>");
	        html.append("<div class='card-body' style='flex-grow: 1; display: flex; flex-direction: column;'>");
	            html.append("<h5 class='card-title'>").append(chartTitle).append("</h5>");
	            html.append("<div id='inventoryInfo' style='text-align: right; margin-bottom: 10px;'></div>");  // Add div for label and value
	            html.append("<div style='flex-grow: 1; display: flex; align-items: center; justify-content: center;'>");
	                html.append("<canvas id='").append(id).append("' style='width: 100%; height: 100%;'></canvas>");
	            html.append("</div>");
	            html.append(outputCardBodyArrows());
	        html.append("</div>");
	    html.append("</div>");

	    return html.toString();
	}




}