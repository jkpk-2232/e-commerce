package com.utils.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ViewUtils {

	protected static Logger logger = Logger.getLogger(ViewUtils.class);

	public static String requestAttributeValue(HttpServletRequest request, String attributeName) {

		String value = "";

		try {
			value = (String) request.getAttribute(attributeName);

			if (value == null) {
				value = "";
			}

		} catch (Exception e) {
			value = "";
			logger.error("Exception occurred:", e);
		}

		return value;
	}

	public static long requestLongAttributeValue(HttpServletRequest request, String attributeName) {

		Object attributeObj = request.getAttribute(attributeName);

		if (attributeObj == null) {
			return -1;
		} else {

			String strValue = attributeObj.toString();

			long longValue = -1;

			try {
				longValue = Long.parseLong(strValue);
			} catch (Exception e) {
				logger.error("Exception occurred:", e);
			}

			return longValue;
		}
	}

	public static String outputFormHeading(HttpServletRequest request, String message, String optionalCssClass) {

		String html = "";

		html += "<div class='" + optionalCssClass + " form-heading' id='page_heading'>";

		html += message;

		html += "</div>";

		return html;
	}

	public static String outputFormLableCell(HttpServletRequest request, String labelStr, String idFor, boolean isRequired, String optionalCssClass) {

		String html = "<td class='" + optionalCssClass + " form-label-cell'> " + "<label for='" + idFor + "'>" + labelStr;

		if (isRequired) {
			html += "<span class='required'>*</span>";
		}

		html += "</label>";

		html += "</td>";

		return html;
	}

	private static String outputFormTextWidget(HttpServletRequest request, String name, int tabIndex, int maxlength, String optionalCssClass, boolean isReadonly) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input ";

		if (isReadonly) {
			html += " Readonly ";
		}

		html += " type='text' name='" + name + "' id='" + name + "' value = '" + value + "' maxlength='" + maxlength + "' tabindex = '" + tabIndex + "'/>";

		html += "</td>";

		return html;
	}

	public static String outputFormTextWidget(HttpServletRequest request, String name, int tabIndex, int maxlength, String optionalCssClass) {
		return outputFormTextWidget(request, name, tabIndex, maxlength, optionalCssClass, false);
	}

	public static String outputFormTextWidget(HttpServletRequest request, String name, int tabIndex, int maxlength, boolean isReadonly, String followedStr, String optionalCssClass) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input ";

		if (isReadonly) {
			html += " Readonly ";
		}

		html += " type='text' name='" + name + "' id='" + name + "' value = '" + value + "' maxlength='" + maxlength + "' tabindex = '" + tabIndex + "'/>";

		html += "<label for='" + name + "'>" + followedStr + "</label></td>";

		return html;
	}

	public static String outputFormTextWidgetReadOnly(HttpServletRequest request, String name, int tabIndex, int maxlength, String optionalCssClass) {
		return outputFormTextWidget(request, name, tabIndex, maxlength, optionalCssClass, true);
	}

	public static String outputFormPasswordWidget(HttpServletRequest request, String name, int tabIndex, int maxlength, String optionalCssClass) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input tabindex = '" + tabIndex + "' type='password' name='" + name + "' id='" + name + "' value = '" + value + "' maxlength='" + maxlength + "'/>";

		html += "</td>";

		return html;
	}

	public static String outputFormHiddenField(String name, String value) {
		return "<input type='hidden' id='" + name + "' name='" + name + "' value='" + value + "' />";
	}

	public static String outputFormError(HttpServletRequest request, String fieldName) {

		String errorName = requestAttributeValue(request, fieldName + "Error");

		return "<td class='form-widget-error-cell' id='" + fieldName + "Error' >" + errorName + "</td>";
	}

	public static String outputFormCheckboxWidget(HttpServletRequest request, String name, String value, int tabIndex, String optionalCssClass) {

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input tabindex = '" + tabIndex + "' type='checkbox' name='" + name + "' id='" + name + "' value = '" + value + "'/>";

		html += "<label>" + value + "</label>";

		return html;
	}

	public static String outputFormRadioBox(HttpServletRequest request, String name, int tabIndex, String optionalCssClass, String RadioButtonMsg, String RadioButtonValue, boolean isChecked) {

		String msg = RadioButtonMsg;

		String html = "";

		if (isChecked)
			html += "<input tabindex = '" + tabIndex + "' type='radio' name='" + name + "' id='" + name + "' class='" + optionalCssClass + "' value = '" + RadioButtonValue + "' checked >" + msg + "</input>";
		else
			html += "<input tabindex = '" + tabIndex + "' type='radio' name='" + name + "' id='" + name + "' class='" + optionalCssClass + "' value = '" + RadioButtonValue + "' >" + msg + "</input>";

		return html;
	}

	public static String outputFormDateWidget(String optionalCssClass, String name, int tabIndex, HttpServletRequest request) {

		String error = requestAttributeValue(request, name + "Error");

		String value = requestAttributeValue(request, name);

		String html = "<td class='" + optionalCssClass + error + "'>";

		html += "<input  tabindex = '" + tabIndex + "' type='text' name='" + name + "' id='" + name + "' value = '" + value + "' READONLY/>";

		html += "</td>";

		return html;
	}

	public static String outputFormTimeWidget(String optionalCssClass, String hh, String mm, String ampm, int tabIndex, HttpServletRequest request, String onchangeFnName) {

		String options = requestAttributeValue(request, hh + "Options");

		String error = requestAttributeValue(request, hh + "Error");

		if (error.equals("")) {
			error = requestAttributeValue(request, mm + "Error");
		}

		if (error.equals("")) {
			error = requestAttributeValue(request, ampm + "Error");
		}

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<select class='small-combo-box' tabindex = '" + tabIndex + "' name='" + hh + "' id='" + hh + "' ";

		if (!(null == onchangeFnName || "".equals(onchangeFnName))) {

			if (onchangeFnName.endsWith(")")) {
				html += " onchange='" + onchangeFnName + "'";
			} else {
				html += " onchange='" + onchangeFnName + "(this.value)'";
			}
		}

		html += ">";
		html += options;
		html += "</select>";

		options = requestAttributeValue(request, mm + "Options");

		html += "<select class='small-combo-box' tabindex = '" + tabIndex + "' name='" + mm + "' id='" + mm + "' ";

		if (!(null == onchangeFnName || "".equals(onchangeFnName))) {

			if (onchangeFnName.endsWith(")")) {
				html += " onchange='" + onchangeFnName + "'";
			} else {
				html += " onchange='" + onchangeFnName + "(this.value)'";
			}
		}

		html += ">";
		html += options;
		html += "</select>";

		options = requestAttributeValue(request, ampm + "Options");

		html += "<select class='small-combo-box' tabindex = '" + tabIndex + "' name='" + ampm + "' id='" + ampm + "' ";

		if (!(null == onchangeFnName || "".equals(onchangeFnName))) {

			if (onchangeFnName.endsWith(")")) {
				html += " onchange='" + onchangeFnName + "'";
			} else {
				html += " onchange='" + onchangeFnName + "(this.value)'";
			}
		}

		html += ">";
		html += options;
		html += "</select>";
		html += "</td>";

		return html;
	}

	public static String outputFormTextAreaWidgetMaxChar(String optionalCssClass, String name, int tabIndex, int cols, int rows, int maxChar, HttpServletRequest request) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<textarea tabindex = '" + tabIndex + "' name='" + name + "' id='" + name + "' rows='" + rows + "' cols='" + cols + "'";

		html += "onKeyDown='viewUtilLimitTextArea(this.form." + name + "," + maxChar + "," + maxChar + ");'";

		html += "onKeyUp='viewUtilLimitTextArea(this.form." + name + "," + maxChar + "," + maxChar + ");'";

		html += "/>";

		html += value;

		html += "</textarea>";

		html += "</td>";

		return html;
	}

	public static String outputFormComboWidget(HttpServletRequest request, String name, int tabIndex, String optionalCssClass) {

		String options = requestAttributeValue(request, name + "Options");

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<select tabindex = '" + tabIndex + "' name='" + name + "' id='" + name + "'>";

		html += options;

		html += "</select>";

		html += "</td>";

		return html;
	}

	public static String outputFormComboWidgetNoTd(HttpServletRequest request, String name, int tabIndex, String followedStr, String optionalCssClass) {

		String options = requestAttributeValue(request, name + "Options");

		String html = "<select tabindex = '" + tabIndex + "' name='" + name + "' id='" + name + "' class='" + optionalCssClass + "'>";

		html += options;

		html += "</select>";

		html += "<label for='" + name + "'>" + followedStr + "</label>";

		return html;
	}

	public static String outputFormComboWidgetWithOnchangeFn(HttpServletRequest request, String name, int tabIndex, String onChangeFn, String optionalCssClass) {

		String options = requestAttributeValue(request, name + "Options");

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<select tabindex = '" + tabIndex + "' name='" + name + "' id='" + name + "'onchange='" + onChangeFn + "(this.value)'>";

		html += options;

		html += "</select>";

		html += "</td>";

		return html;
	}

	public static String getButtonLinkTable(String id, List<ButtonLink> left, List<ButtonLink> right, HttpServletRequest request) {

		String html = "<table class='form-table-buttons' id='" + id + "'>";

		html += "<tr>";

		html += "<td width='25%' class='left-buttons'>";

		for (ButtonLink btn : left) {

			String btnName = btn.getBtnName();

			if (btn.isButton()) {
				html += "<input type='image' name='" + btn.getId() + "' tabindex='" + btn.getTabIndex() + "'";
				html += " src='" + request.getContextPath() + "/assets/images/" + btnName + "'";
				html += "id='" + btn.getId() + "' />";
			} else {
				html += "<a href='#' name='" + btn.getId() + "' tabindex='" + btn.getTabIndex() + "' id='" + btn.getId() + "'>";
				html += "<img src='" + request.getContextPath() + "/assets/images/" + btnName + "' />";
				html += "</a>";
			}
		}

		html += "</td>";

		html += "<td width='75%' class='right-buttons'>";

		for (ButtonLink btn : right) {

			String btnName = btn.getBtnName();

			if (btn.isButton()) {
				html += "<input type='image' name='" + btn.getId() + "' tabindex='" + btn.getTabIndex() + "'";
				html += " src='" + request.getContextPath() + "/assets/images/" + btnName + "'";
				html += "id='" + btn.getId() + "' />";
			} else {
				html += "<a href='#' name='" + btn.getId() + "' tabindex='" + btn.getTabIndex() + "' id='" + btn.getId() + "'>";
				html += "<img src='" + request.getContextPath() + "/assets/images/" + btnName + "' />";
				html += "</a>";
			}
		}

		html += "</td>";
		html += "</tr>";
		html += "</table>";

		return html;
	}

	public static String outputFormViewCell(HttpServletRequest request, String msg, String cssClass) {

		String value = requestAttributeValue(request, msg);

		if (value == null) {
			value = "";
		}

		String html = "<td class='" + cssClass + "'> " + value;

		html += "</td>";

		return html;
	}

	public static String BrowseButtonWithTextbox(HttpServletRequest request, String name, int tabIndex, String optionalCssClass) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input tabindex = '" + tabIndex + "' type='file' name='" + name + "' id='" + name + "' class='" + optionalCssClass + " input-text' value = '" + value + "'/>";

		html += "</td>";

		return html;
	}

	public static String BrowseButtonWithTextboxWithOnChangeFn(HttpServletRequest request, String name, int tabIndex, String optionalCssClass, String functionName) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input tabindex = '" + tabIndex + "' type='file' name='" + name + "' id='" + name + "' class='" + optionalCssClass + " input-text' value = '" + value + "' onchange='" + functionName + "()'/>";

		html += "</td>";

		return html;
	}

	public static String outputFormTextWidgetWithSuffixLable(HttpServletRequest request, String name, int tabIndex, int maxlength, String optionalCssClass, boolean isReadonly, String suffix) {

		String value = requestAttributeValue(request, name);

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input ";

		if (isReadonly) {
			html += " Readonly ";
		}

		html += " type='text' name='" + name + "' id='" + name + "' value = '" + value + "' maxlength='" + maxlength + "' tabindex = '" + tabIndex + "'/>";

		html += "<span> " + suffix + "</span></td>";

		return html;
	}

	public static String outputFormViewCellWithSuffixLable(HttpServletRequest request, String msg, String cssClass, String suffix) {

		String value = requestAttributeValue(request, msg);

		if (value == null) {
			value = "";
		}

		String html = "<td class='" + cssClass + "'> " + value;

		html += "<span> " + suffix + "</span></td>";

		return html;
	}

	public static String outputFormCheckboxWidgetWithSameIdValue(HttpServletRequest request, String name, String value, int tabIndex, String optionalCssClass) {

		String error = requestAttributeValue(request, name + "Error");

		String html = "<td class='" + optionalCssClass + " form-widget-cell" + error + "'>";

		html += "<input tabindex = '" + tabIndex + "' type='checkbox' name='" + name + "' id='" + name + "' value = '" + name + "'/>";

		html += "<label>" + value + "</label>";

		return html;
	}

}