package com.webapp.viewutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class ViewUtils {

	protected static Logger logger = Logger.getLogger(ViewUtils.class);

	public static String outputFormHeading(HttpServletRequest request, String message, String optionalCssClass) {
		String html = "";
		html += "<div class='" + optionalCssClass + " form-heading' id='page_heading'>";
		html += message;
		html += "</div>";
		return html;
	}

	public static String outputImageButtonLink(String id, String label, int tabIndex, String linkUrl) {

		//@formatter:off
         
         String html = 
                 "<a href='" + linkUrl +"' tabindex='" + tabIndex + "' id='" + id + "' >"+ 
                         "<span class='button-left-bg'>&nbsp;</span> <span class='button-mid-bg'>" + label + "</span>" + 
                         "<span class='button-right-bg'>&nbsp;</span>" +  
                 "</a> ";
         //@formatter:on

		return html;
	}

	public static String outputUploadButton(String id, int tabIndex, HttpServletRequest request) {
		//@formatter:off
		
		String html = "<input tabindex='" + tabIndex + "'  id='" + id + "' type='image' src='" + request.getContextPath() + "/assets/images/but_upload.jpg' alt='Upload' />";
		
		//@formatter:on

		return html;
	}

	public static String requestAttributeValue(HttpServletRequest request, String attributeName) {

		Object attributeObj = request.getAttribute(attributeName);
		logger.debug("attributeObj" + attributeObj);
		if (attributeObj == null) {
			return "";
		} else {
			return attributeObj.toString();
		}
	}

	public static String requestAttributeValue(HashMap<String, String> map, String attributeName) {

		Object attributeObj = map.get(attributeName);
		logger.debug("attributeObj" + attributeObj);
		if (attributeObj == null) {
			return "";
		} else {
			return attributeObj.toString();
		}
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
				// Do Nothing
			}

			return longValue;
		}
	}

	public static List<String> requestAttributeValues(HttpServletRequest request, String attributeName) {

		@SuppressWarnings("unchecked")
		List<String> value = (List<String>) request.getAttribute(attributeName);
		logger.debug("attributeObj" + value);
		if (value == null) {
			List<String> empty = new ArrayList<String>();
			return empty;
		} else {
			return value;
		}
	}

	public static String outputTextFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input class='colField'  tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputNumberFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input style='margin-left: -10px;' min = '0' max = '50000' step = '500' class='colField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputCreditNumberFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
//			"<input onkeypress='return allowNegativeNumber(event);'  style='margin-left: -10px;' max = '50000' step = '500' class='colField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
			"<input style='margin-left: -240px;' max = '50000' step = '500' class='colField'  tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputPayablePercentageNumberFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input style='' min = '0' max = '100' step = '1' class='colField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputCancellationChargesNumberFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input style='' min = '0' max = '5000' step = '100' class='colField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputTextField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String optionalCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		// logger.info("Value:"+value);

		//@formatter:off
		
		String html = 
			"<input class=' "+optionalCssClass+" '"+
		          " tabindex = '" + tabIndex + "'"+
		          " type='text' name='" + id + "'"+
		          " id='" + id + "'"+
		          " value = '" + value + "'"+
		          " maxlength='" + maxlength + "'/>";
		//@formatter:on

		//		if (isRequired) {
		//			html += "<div class='compulsaryImgDiv'><div class='compulsaryImg'></div></div>";
		//		}

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputArtTextFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}
		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input class='artColField' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = \""+ value + "\" maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputSerchKeyTextFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}
		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
			"<input class='SearchKeyColField' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = \""+ value + "\" maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputPasswordFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<td class='col-text'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td class='col-field'>" +
				"<input class='textfield' tabindex = '" + tabIndex + "' type='password' name='" + id + "' id='" + id + "' value = '" + value + "' maxlength='" + maxlength + "'/>";
		
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputSelectFieldCell(String id, String label, boolean isRequired, int tabIndex, String options, HashMap<String, String> map) {

		//@formatter:off
		String html = 
			/*"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + */
			"<td>" +
				"<select class='colFieldSelect' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "'>";
		//@formatter:on

		html += options + "</select>";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputMultiSelectFieldCell(String id, String label, boolean isRequired, int tabIndex, String options, HashMap<String, String> map) {

		//@formatter:off
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td>" +
				"<select  multiple='multiple' class='colFieldSelect' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "'>";
		//@formatter:on

		html += options + "</select>";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String searchStringField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/><img class='searchImage' src=" + request.getContextPath() + "/assets/image/search.png>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputUsernameFieldForLogin(String id, String label, String classs, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = ""
//			"<img class=" + classs+" src=" + request.getContextPath() + "/assets/image/icon_email.jpg>
		+"<input class=" + classs+" tabindex = '" + tabIndex + "' type='text' border='0' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPassengerUsernameFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outPutImage(String id, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<img  name='" + id + "' id='" + id + "' style='height:200px; width:250px; border:1px solid #021a40;' src=" + request.getContextPath() +value+">";
		//@formatter:on

		return html;
	}

	public static String outPutImageAddDriver(String id, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<img  name='" + id + "' id='" + id + "' style='height:150px; width:210px; border:1px solid #021a40;' src=" + request.getContextPath() +value+">";
		//@formatter:on

		return html;
	}

	public static String outPutImageFancybox(String id, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<a id='placeImg' href=" + request.getContextPath() +value+"><img  name='" + id + "' id='" + id + "' style='height:200px; width:250px' src=" + request.getContextPath() +value+"></a>";
		//@formatter:on

		return html;
	}

	public static String outputUsernameFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPhoneFieldForPassenger(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text' tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputRememberMeField(String id, String label, int tabIndex, HashMap<String, String> map) {

		String value = map.get(id);
		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = "<input tabindex = '"+ tabIndex +"' type='checkbox' name='"+id+"' id='"+id+"' value='"+id+"' "+value+">" +	
		
				"<label for='" + id + "'>" + label +  "</label>" ;
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputPasswordFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = ""
//			"<img class='passwordImgae' src=" + request.getContextPath() + "/assets/image/icon_password.jpg>"
					+ "<input class='reqblue'   tabindex = '" + tabIndex + "' type='password' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPassengerPasswordFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text'   tabindex = '" + tabIndex + "' type='password' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPasswordFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input class='login-text'   tabindex = '" + tabIndex + "' type='password' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputErrorField(String id, HashMap<String, String> map) {

		String html = "";
		if (map.get(id + "Error") != null) {
			html += "<span class='imgerrorMessage'>" + map.get(id + "Error") + "</span>";
		}
		return html;
	}

	public static String outputFormHiddenField(String id, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}
		return "<input type='hidden' id='" + id + "' name='" + id + "' value='" + value + "' />";
	}

	public static String outputLabelField(String id, String label, HashMap<String, String> map) {

		String value = map.get(id);
		if (value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("")) {
			value = " - ";
		}
		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label  + "</label>" + 
			"</td>" + 
			"<td class='colText'>" +
			"<label for='" + id + "'>" +  value  + "</label>" ;
				
		
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputLabelField(String id, String label, HashMap<String, String> map, String css, String css1) {

		String value = map.get(id);
		if (value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("")) {
			value = " - ";
		}
		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
				"<label style='"+ css +"' for='" + id + "'>" + label  + "</label>" + 
			"</td>" + 
			"<td class='colText'>" +
			"<label id='" + id + "' style='"+ css1 +"' for='" + id + "'>" +  value  + "</label>" ;
				
		
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputLabelStatusField(String id, String label, HttpServletRequest request) {

		String value = requestAttributeValue(request, id);

		if (value == null) {
			value = "";
		}
		if (value.equals("1")) {
			value = "Active";

		} else if (value.equals("0")) {
			value = "Deactive";
		}

		//@formatter:off		
		String html = 
			"<td class='col-text'> " +
				"<label for='" + id + "'>" + label  + "</label>" + 
			"</td>" + 
			"<td class='col-text-view'>" +
			"<label for='" + id + "'>" +  value  + "</label>" ;			
		
		//@formatter:on
		if (value.equalsIgnoreCase("Active")) {
			html += "<img src=" + request.getContextPath() + "/assets/images/icon-active.png>";
		} else {
			html += "<img src=" + request.getContextPath() + "/assets/images/icon-inactive.png>";
		}
		if (!requestAttributeValue(request, id + "Error").equalsIgnoreCase("")) {
			html += "<span class='error-meesage'>" + request.getAttribute(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputTextAreaFieldCell(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, long rows, long cols) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
				"<label for='" + id + "'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td class='col-field'>" +
			"<textarea class='textareafield' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' data-maxsize='" + maxlength + "' rows='" + rows + "' cols='" + cols + "'>" + value + "</textarea>";
				
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputTextAreaFieldCellCustom(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, long rows, long cols) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
//				"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
			"</td>" + 
			"<td class='col-field'>" +
			"<textarea class='textareafield' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' data-maxsize='" + maxlength + "' rows='" + rows + "' cols='" + cols + "'>" + value + "</textarea>";
				
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputMessage(HttpServletRequest request, String attributeName) {

		String html = null;

		HttpSession session = ViewUtils.getSession(request);

		@SuppressWarnings("unchecked")
		List<String> messages = (List<String>) session.getAttribute(attributeName);
		session.removeAttribute(attributeName);
		if (messages == null) {
			return "";
		}

		for (String message : messages) {
			//@formatter:off
			
			html = 
				"<tr>" + 
				"<td>" +
					"<label style='color:red' >" + message  + "</label>" + 
				"</td> " +
				"</tr>" ;		
			
			//@formatter:on

		}

		return html;
	}

	public static String outputSelectFieldCellWithoutLabel(String id, String label, boolean isRequired, int tabIndex, String options, HashMap<String, String> map) {

		//@formatter:off
		String html = 
				"<td valign='top' class='col-field'>" +
				"<select tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "'>";
		//@formatter:on

		html += options + "</select>";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputSelectFieldCellWithoutLabel(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String optionalCssClass) {

		//@formatter:off
		String html = 
				"<td valign='top' class='col-field '>" +
				"<select tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' class=' " + optionalCssClass + "'>";
		//@formatter:on

		String options = map.get(id + "Options");

		html += options + "</select>";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputMultipleSelectFieldCellWithoutLabel(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String optionalCssClass) {

		//@formatter:off
		String html = 
				"<td valign='top' class='col-field '>" +
				"<select multiple='multiple' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' class=' " + optionalCssClass + "'>";
		//@formatter:on

		String options = map.get(id + "Options");

		html += options + "</select>";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String getStringWithMaxSize(String strValue, int maxLength) {

		String maxLengthStr = "-";
		/* logger.debug("stirng>>>>>>>"+strValue); */
		if (strValue != null) {
			if (strValue.length() > maxLength) {

				maxLengthStr = strValue.subSequence(0, (maxLength - 3)) + "...";

			} else {

				maxLengthStr = strValue;
			}

		}
		if (strValue != null && strValue.equals("")) {
			strValue = "-";
			return strValue;
		}
		return maxLengthStr;
	}

	public static String getStringWithSpace(String strValue) {

		StringBuilder newString = new StringBuilder();
		newString.append(strValue);
		int count = 1;
		int count2 = 0;

		if (strValue != null && !strValue.equals(null)) {
			for (int i = 0; i < strValue.length() - 1; i++) {
				count++;

				if (strValue.charAt(i) == ' ' && count < 25) {
					count = 1;

				}

				if (count == 25) {
					count2 = count2 + count;
					newString.insert(count2, " ");
					count = 1;
				}

			}
		}
		return newString.toString();
	}

	public static HttpSession getSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return null;
		}
		return session;
	}

	public static String outputTextAreaFieldCellCKEditor(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, long rows, long cols) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
        		
        String html = 
                "<td class='cKEditorText'> " +
                        "<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
                "</td>" + 
                "<td class='ckeditor-width'>" +
                "<textarea  maxlength = '"+maxlength+"' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' rows='" + rows + "' cols='" + cols + "'>" + value + "</textarea>";
                        
        //@formatter:on
		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputTextAreaFieldCellCKEditorWithoutLabel(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, long rows, long cols) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
        
        String html = 
                "<td class='ckeditor-width'>" +
                "<textarea  maxlength = '"+maxlength+"' tabindex = '" + tabIndex + "' name='" + id + "' id='" + id + "' rows='" + rows + "' cols='" + cols + "'>" + value + "</textarea>";
                        
        //@formatter:on
		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputCheckBox(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map) {

		String value = map.get(id);
		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<td class='colText'> " +
			"<label for='" + id + "'>" + label + (isRequired ? "<span>*</span>" : "") + "</label>" + 
		"</td>" + 
		
		"<td style='width:20px;'><input tabindex = '"+ tabIndex +"' type='checkbox' name='"+id+"' id='"+id+"' value='"+id+"' "+value+"></td>" ;		
		
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputRadioButton(String id, String label, int tabIndex, HashMap<String, String> map, String valueParam) {

		String value = map.get(id);
		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html =			
			"<td class='colText'>" +
				"<input tabindex = '" + tabIndex + "' type='radio' name='" + id + "' id='" + id + "' value = '" + valueParam + "'"; 
				
			if(value.equals(valueParam)) {
				
				html += " checked ";
			}
			
			html += " />" + label;
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputPassengerRadioButton(String id, String label, int tabIndex, HashMap<String, String> map, String valueParam) {

		String value = map.get(id);
		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html =			
			"<td class='colText'>" +
				"<input tabindex = '" + tabIndex + "' type='radio' name='" + id + "' id='" + id + "' value = '" + valueParam + "'"; 
				
			if(value.equals(valueParam)) {
				
				html += " checked ";
			}
			
			html += " />";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</td>";

		return html;
	}

	public static String outputTextFieldUserSignup(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String optionalCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = 
			"<input class=' "+optionalCssClass+" '"+
		          " tabindex = '" + tabIndex + "'"+
		          " type='text' name='" + id + "'"+
		          " id='" + id + "'"+
		          " value = '" + value + "'"+
		          " placeholder= '" + label + "'"+
		          " maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPasswordFieldUserSignup(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String optionalCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = 
			"<input class=' "+optionalCssClass+" '"+
		          " tabindex = '" + tabIndex + "'"+
		          " type='password' name='" + id + "'"+
		          " id='" + id + "'"+
		          " value = '" + value + "'"+
		          " placeholder= '" + label + "'"+
		          " maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputPasswordTextField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String optionalCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		// logger.info("Value:"+value);

		//@formatter:off
		
		String html = 
			"<input class=' "+optionalCssClass+" prevent-first-space'"+
		          " tabindex = '" + tabIndex + "'"+
		          " type='password' name='" + id + "'"+
		          " id='" + id + "'"+
		          " value = '" + value + "'"+
		          " maxlength='" + maxlength + "'/>";
		//@formatter:on

		//		if (isRequired) {
		//			html += "<div class='compulsaryImgDiv'><div class='compulsaryImg'></div></div>";
		//		}

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String searchStringFieldCustom(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, HttpServletRequest request) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		
		String html = 
			"<input onkeyup='javascript:searchSel();' class='login-text prevent-first-space' tabindex = '" + tabIndex + "' type='text' name='" + id + "' id='" + id + "' value = '" + value + "' placeholder='"+label+"' maxlength='" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputNumberFieldCellMultiCityCustom(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
					
		String html = 
			"<input style='' min = '0' max = '100' step = '1' class='colField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='"+value+"'  maxlength=\'" + maxlength + "'/>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html = "<input style='' min = '0' max = '100' step = '1' class='colField erroField'  tabindex = '" + tabIndex + "' type='number' name='" + id + "' id='" + id + "' value ='" + value + "'  maxlength=\'" + maxlength + "'/>";

		}

		return html;
	}
}