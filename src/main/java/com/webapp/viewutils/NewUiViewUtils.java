package com.webapp.viewutils;

import java.util.HashMap;

public class NewUiViewUtils {

	public static String outputFieldForLogin(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += " ' style='line-height: 34px !important; height: 56px; margin-bottom: 15px !important;'> ";

		//@formatter:off		
		html +=	"<label class='col-md-5 control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>"+
			    "<div class='col-md-12'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + label + "'>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputFieldForCustom(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String css) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div id='" + id + "Parent' class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += " ' style='line-height: 34px !important; height: 56px; margin-bottom: 15px !important;'> ";

		//@formatter:off		
		html +=	"<label class='col-md-6 control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>"+
			    "<div class='col-md-12' style='" + css + "'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + label + "'>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputSelectFieldCell(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div class='custom-dropdown' id='" + id + "1'>"
				+ "<div>"
				+ "<div class='form-group'>";
		
		if(label != null) {
			html += " <label class='col-md-2 control-label' for='criteria' style='width: 100px;'>Select Type</label>";
		}
		
		html += "<div class='col-md-2' style='margin-top: 1%;margin-left: 2%;'>"
			+ "<select id='" + id + "' name='" + id + "' class='form-control' data-live-search='true' size='1' style='" + css + "'>";
		html += options + "</select>"
			+ "</div></div></div></div>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputSelectFieldCellWithlabel(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div class='custom-dropdown' id='" + id + "1'>"
				+ "<div>"
				+ "<div class='form-group'>";
		
		if(label != null) {
			html += " <label class='col-md-3 control-label' for='criteria' style='line-height: 50px;'>" + label + "</label>";
		}
		
		html += "<div class='col-md-2' style='margin-top: 1%;'>"
			+ "<select id='" + id + "' name='" + id + "' class='form-control col-md-4' size='1' style='" + css + "'>";
		html += options + "</select>"
			+ "</div></div></div></div>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputFormButtonLink(String url, String type) {

		if (type == null) {
			return "";
		} else if (type.trim().equalsIgnoreCase("")) {
			return "";
		}

		String html = "";

		html = "<a data-toggle='tooltip' style='margin-right: 5%;' class='btn btn-xs btn-primary' ";

		if (type != null && type.trim().equalsIgnoreCase("view")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='View' > <i class='icon-eye-open'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("edit")) {
			html += " href=\"javascript:editItem('" + url + "');\" title='Edit' > <i class='icon-edit'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("reactivate")) {
			html += " href=\"javascript:reactivateItem('" + url + "');\" title='Reactivate' > <i class='glyphicon-retweet'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("deactivate")) {
			html += " href=\"javascript:deactivateItem('" + url + "');\" title='Deactivate' > <i class='halfling-trash'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("download")) {
			html += " href=\"javascript:downloadItem('" + url + "');\" title='Download' > <i class='icon-download'></i> ";
			html += "<audio src='" + url + "' preload=\"auto\" />";
		} else if (type != null && type.trim().equalsIgnoreCase("delete")) {
			html += " href=\"javascript:deleteItem('" + url + "');\" title='Delete' > <i class='icon-remove-sign'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("edit_callNonCmnJsFun")) {
			html += " href=\"javascript:edit('" + url + "');\" title='Edit' > <i class='icon-edit'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("benefit_view")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='View' > <i class='icon-gift'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("recharge")) {
			html += " href=\"javascript:rechargeAccount('" + url + "');\" title='Recharge' > <i class='icon-rupee'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("approve")) {
			html += " href=\"javascript:approveRequest('" + url + "');\" title='Approve' > <i class='icon-ok-sign'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("transfer")) {
			html += " href=\"javascript:transferRequest('" + url + "');\" title='Transfer' > <i class='glyphicon glyphicon-transfer'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("reject")) {
			html += " href=\"javascript:rejectRequest('" + url + "');\" title='Reject' > <i class='icon-remove-sign'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("encash")) {
			html += " href=\"javascript:encashRequest('" + url + "');\" title='Encash' > <i class='icon-download'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("assignCarTypeTransport")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Assign Car Type - Transportation' > <i class='glyphicon glyphicon-cars'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("assignCarTypeCourier")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Assign Car Type - Courier' > <i class='glyphicon glyphicon-truck'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("airportVendor")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Vendor Airport' > <i class='fa fa-plane'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscription")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Subscription History' > <i class='glyphicon glyphicon-money'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscribepackage")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Subscribe Package' > <i class='icon icon-dollar'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("transactionhistory")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Transaction History' > <i class='icon icon-exchange'></i> ";
		} else if (type != null && (type.trim().equalsIgnoreCase("take-ride") || type.trim().equalsIgnoreCase("untake-ride"))) {
			String title = "";
			if (type.trim().equalsIgnoreCase("take-ride")) {
				title = "Take Bookings";
			} else {
				title = "Untake Bookings";
			}
			html += " href=\"javascript:takeRide('" + url + "', '" + type + "');\" title='" + title + "' > <i class='glyphicon glyphicon-cars'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("view-categories")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='View Categories' > <i class='icon icon-fire'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("view-vendors-by-category")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='View Vendors By Categories' > <i class='glyphicon glyphicon-user'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("view-subscribed-drivers")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='View Subscribed Drivers' > <i class='glyphicon glyphicon-old_man'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("store-location")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Add Store Locations' > <i class='glyphicon glyphicon-shop'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("monthlySubscription")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Monthly Subscription' > <i class='icon icon-dollar'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("vendorMonthlySubscriptionHistory")) {
			html += " href=\"javascript:viewItem('" + url + "');\" title='Monthly Subscription History' > <i class='icon icon-exchange'></i> ";
		}
		
		html += " </a>";

		return html;
	}

	public static String outputDeleteFormButtonLink(String userId, String userType, String url, String type) {

		if (type == null) {
			return "";
		} else if (type.trim().equalsIgnoreCase("")) {
			return "";
		}

		String html = "";

		html = "<a data-toggle='tooltip' style='margin-right: 5%;' class='btn btn-xs btn-primary' ";

		if (type != null && type.trim().equalsIgnoreCase("userDeactivate")) {
			html += " href=\"javascript:actionUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Deactivate' > <i class='halfling-trash'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userReactivate")) {
			html += " href=\"javascript:actionUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Reactivate' > <i class='glyphicon-retweet'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userApproved")) {
			html += " href=\"javascript:approveUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Approve' > <i class='glyphicon glyphicon-check'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("userNotApproved")) {
			html += " href=\"javascript:approveUser('" + userId + "' ,'" + userType + "','" + url + "');\" title='Not Approved' > <i class='glyphicon-retweet'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscriptionDeactivate")) {
			html += " href=\"javascript:actionVendorAction('" + userId + "' ,'" + userType + "','" + url
						+ "', 'Are you sure you want to deactivate driver subscription in booking flow?');\" title='Deactivate Driver Subscription In Booking Flow' > <i class='halfling-trash'></i> ";
		} else if (type != null && type.trim().equalsIgnoreCase("subscriptionReactivate")) {
			html += " href=\"javascript:actionVendorAction('" + userId + "' ,'" + userType + "','" + url
						+ "', 'Are you sure you want to activate driver subscription in booking flow?');\" title='Reactivate Driver Subscription In Booking Flow' > <i class='glyphicon-retweet'></i> ";
		}

		html += " </a>";

		return html;
	}

	public static String outputGlobalInputFieldMain(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String valueCssClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div id='" + id + "Div' class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += " ' style='line-height: 34px !important; height: 56px; margin-bottom: 15px !important;'> ";

		//@formatter:off
		
		if(label != null) {
			html +=	"<label class='" + labelCssClass + " control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		
		html +=	 "<div class='" + valueCssClass + "'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + (label != null ? label : "") + "'>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}
	
	public static String outputGlobalInputField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String labelCssClass, String valueCssClass) {
		return outputGlobalInputFieldMain(id, label, isRequired, tabIndex, maxlength, map, type, labelCssClass, valueCssClass);
	}
	
	public static String outputGlobalInputField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {
		return outputGlobalInputFieldMain(id, label, isRequired, tabIndex, maxlength, map, type, "col-md-3", "col-md-4");
	}

	public static String outputSelectFieldCellWithlabelAndSearchField(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div id='" + id + "Div' class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		if(label != null) {
			html += "<label class='col-md-3 control-label' for='" + id + "' style='line-height: 35px;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		
		html += "<div class='" + css + "'>"
			+ "<select id='" + id + "' name='" + id + "' class='select-chosen' size='1' style=''>";
		html += options + "</select>";
			
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div> </div>";

		return html;
	}

	public static String outputSelectFieldCellWithlabelAndSearchFieldCustom(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css, String css1) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div id='" + id + "Div' class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		if(label != null) {
			html += "<label class='col-md-5 control-label' for='" + id + "' style='line-height: 35px;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		
		html += "<div class='" + css + "' style='" + css1 + "'>"
			+ "<select id='" + id + "' name='" + id + "' class='select-chosen' size='1' style=''>";
		html += options + "</select>";
			
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputSelectFieldCellWithlabelAndSearchFieldForFilters(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		if(label != null) {
			html += "<label class='col-md-3 control-label' for='" + id + "' style='line-height: 35px;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		
		html += "<div class='col-md-4' style='" + css + "'>"
			+ "<select id='" + id + "' name='" + id + "' class='select-chosen' size='1' style=''>";
		html += options + "</select>"
			+ "</div></div>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputSelectFieldCellWithlabelAndSearchFieldAndMultiSelect(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String css) {

		String options = map.get(id + "Options");

		//@formatter:off
		String html = "<div class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		if(label != null) {
			html += "<label class='col-md-3 control-label' for='" + id + "' style='line-height: 35px;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		
		html += "<div class='col-md-4'><select id='" + id + "' name='" + id + "' class='selectpicker form-control' multiple data-selected-text-format='count' data-live-search='true'>";
		html += options + "</select>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputGlobalInputFieldWithSelectAndMultiSelect(String id, String id1, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {

		String value = map.get(id);

		String options = map.get(id1 + "Options");

		if (value == null) {
			value = "";
		}

		if (options == null) {
			options = "";
		}

		String html = " <div class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += " ' style='line-height: 34px !important; height: 56px; margin-bottom: 15px !important;'> ";

		//@formatter:off		
		html +=	"<label class='col-md-3 control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		html += "<div class='col-md-2'><select id='" + id1 + "' name='" + id1 + "' class='selectpicker form-control' data-selected-text-format='count' data-live-search='true'>";
		html += options + "</select>"
			+ "</div>"+
			"<div class='col-md-2'>"+
		        "<input type='" + type + "' "
	               		+ "id='" + id + "' "
	               		+ "name='" + id + "' "
	               		+ "value='" + value + "' "
	               		+ "class='form-control' "
	               		+ "style='border-radius: 0px !important;'"
	               		+ "placeholder='" + label + "'>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputDateRangeField(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map) {

		//@formatter:off
		String html = "<div class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		html += "<label class='col-md-3 control-label' for='" + id + "' style='line-height: 53px !important;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		
		html += "<div class='col-md-4'>"+
			"<div class='controls'>"+
			"<div id='" + id + "' class='pull-left' style='width: 100%;'>"+
			"<span></span> <b class='caret' style='float: right;margin-top: 2%;'></b></div></div>"+	
			"</div></div>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputDateField(String id, String label, boolean isRequired, int tabIndex, HashMap<String, String> map, String dateFormat) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = "<div class='form-group";
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		html += "<label class='col-md-3 control-label' for='' style='line-height: 38px !important;'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		
		html += "<div class='col-md-4'>"+
			"<input value='" + value + "' id='" + id + "' type='text' class='form-control text-left' placeholder='" + dateFormat + "' style='border-radius: 0px !important;'>" + 
			"";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputImageUploadField(String id, String label, HashMap<String, String> map) {
		return outputImageUploadField(id, label, map, "col-md-6");
	}
	
	public static String outputImageUploadField(String id, String label, HashMap<String, String> map, String mainDivCssClass) {

		//@formatter:off
		String html = "<div class='form-group " + mainDivCssClass;
		
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += "'>";
		
		html += "<label class='control-label'>" + label + "</label>";
		
		html += "<input id='" + id + "' name='inputgly[]' type='file' multiple class='file-loading'>"+
			"</div>";
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}

	public static String outputStaticFields(String id, String label, HashMap<String, String> map, String mainDivCss, String subDivCss, String labelClass, String containClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = " <div class='form-group "+ mainDivCss +"'>";

		html += "<label class='" + labelClass + " control-label'>" + label + ":</label>"+
			"<div class='" + containClass + " "+ subDivCss +"'>"+
			"<p class='form-control-static'>" + value + "</p></div> </div>";
		
		return html;
	}
	
	public static String outputRatingFields(String id, String label, HashMap<String, String> map) {
		
		String value = map.get(id);
		
		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = "<input id='" + id + "' class='rating-loading' value='" + value + "' />";

		return html;
	}
	
	public static String outputButton(String id, String label, String class1) {
		
		//@formatter:off
		String html = " <div class='control-group'><div class='controls'>";

		html += "<input id='" + id + "' type='button' value='" + label + "' class='btn btn-danger " + class1 + "' />"+
			"</div> </div>";
		
		return html;
	}
	
	public static String outputFieldForLoginDisabled(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {
		
		String value = map.get(id);
		
		if (value == null) {
			value = "";
		}
		
		String html = " <div class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}
		
		html += " ' style='line-height: 34px !important; height: 56px; margin-bottom: 15px !important;'> ";
		
		//@formatter:off		
		html +=	"<label class='col-md-5 control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>"+
			    "<div class='col-md-12'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + label + "' disabled>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}

	public static String outputCheckBoxWithoutLabel(String id, int tabIndex, HashMap<String, String> map) {

		String value = map.get(id);
		if (value == null) {
			value = "";
		}

		String html = "<input tabindex = '" + tabIndex + "' type='checkbox' name='" + id + "' id='" + id + "' value='" + id + "' " + value + ">";

		if (map.get(id + "Error") != null) {
			html += "<span class='errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		return html;
	}
	
	public static String outputGlobalInputTextAreaField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type, String css1, String css2) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div id='" + id + "Div' class='form-group mainClass";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += " ' style='line-height: 34px !important; height: 115px; margin-bottom: 15px !important;'> ";

		//@formatter:off	
		if(label!=null && !label.equalsIgnoreCase(""))  {
			html +=	"<label class='" + css1 + " control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>";
		}
		html +=	"<div class='" + css2 + "'>"+
                	"<textarea id='" + id + "' name='" + id + "' rows='4' class='form-control' placeholder='" + ((label!=null)?label:id) + "'>" + value + "</textarea>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage' style='margin-top: -9px;'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div><div class='dashboardSeperator'></div>" + "<div class='dashboardSeperator'></div></div>";

		return html;
	}
	
	public static String outputStaticFieldsWithLabelOptional(String id, String label, boolean isLabelRequired, HashMap<String, String> map, String mainDivCss, String subDivCss, String labelClass, String containClass) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		//@formatter:off
		String html = " <div class='form-group "+ mainDivCss +"'>";

		html +=  (isLabelRequired ? ("<label class='" + labelClass + " control-label'>" + label + ":</label>") : "" )+
			"<div class='" + containClass + " "+ subDivCss +"'>"+
			"<p id='" + id + "Value' class='form-control-static'>" + value + "</p></div> </div>";
		//@formatter:on

		return html;
	}
	
	public static String outputTimePickerInputField(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div id='" + id + "Parent' class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += "'>";

		//@formatter:off		
		html +=	"<label id='" + id + "Label' class='col-md-3 control-label' for='example-password-input'>" + label + (isRequired ? "<span style='color:red;'>*</span>" : "") + "</label>"+
			    "<div class='col-md-4'> <div class='input-group bootstrap-timepicker timepicker'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control input-timepicker24 text-center customTimePicker' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + label + "'><span class='input-group-addon' style='border-radius: 0px;'><i class='icon-time'></i></span></div>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}
	
	public static String outputCheckbox(String id, String label, HashMap<String, String> map) {

		String value = map.get(id);

		boolean flagChecked = true;

		if (value == null) {
			flagChecked = false;
			value = "";
		}

		String content = "<input type='checkbox' id='" + id + "' name='" + id + "' value='" + value + "' " + (flagChecked ? "checked" : "") + " >" + label;

		return content;
	}
	
	public static String outputTimePickerInputField1(String id, String label, boolean isRequired, int tabIndex, int maxlength, HashMap<String, String> map, String type) {

		String value = map.get(id);

		if (value == null) {
			value = "";
		}

		String html = " <div id='" + id + "Parent' class='form-group";
		if (map.get(id + "Error") != null) {
			html += " has-error";
		}

		html += "'>";

		//@formatter:off		
		html +="<div class='col-md-3'> <div class='input-group bootstrap-timepicker timepicker'>"+
			        "<input type='" + type + "' "
		               		+ "id='" + id + "' "
		               		+ "name='" + id + "' "
		               		+ "value='" + value + "' "
		               		+ "class='form-control input-timepicker24 text-center customTimePicker' "
		               		+ "style='border-radius: 0px !important;'"
		               		+ "placeholder='" + label + "'><span class='input-group-addon' style='border-radius: 0px;'><i class='icon-time'></i></span></div>";
		           
		//@formatter:on

		if (map.get(id + "Error") != null) {
			html += "<span class='help-block errorMessage'>" + map.get(id + "Error") + "</span>";
		}

		html += "</div></div>";

		return html;
	}
	
	public static String outputDateRange(String fromId, String toId, String label, HashMap<String, String> map, String labelCss, String valueCss, String dateFormat) {

		String fromValue = map.get(fromId);

		if (fromValue == null) {
			fromValue = "";
		}

		String toValue = map.get(toId);

		if (toValue == null) {
			toValue = "";
		}

		//@formatter:off		
		String html = "<div class='form-group";
		
		if (map.get(fromId + "Error") != null || map.get(toId + "Error") != null) {
			html += " has-error";
		}
			      
		html += "'>";
			         
		html += "<label class='" + labelCss + " control-label' for='" + fromId + "'>Select Date Range<span style='color:red;'>*</span></label>"+
			         "<div class='" + valueCss + "'>"+
			         	"<div class='input-group input-daterange' data-date-format='MM/dd/yyyy'>"+
			                        "<input type='text' id='" + fromId + "' name='" + fromId + "' value='" + fromValue + "' class='form-control text-center' placeholder='From'>"+
			                        "<span class='input-group-addon'><i class='icon-angle-right'></i></span>"+
			                        "<input type='text' id='" + toId + "' name='" + toId + "' value='" + toValue + "' class='form-control text-center' placeholder='To'>"+
			                "</div>";
			         
	        if (map.get(fromId + "Error") != null) {
	        	html += "<span class='errorMessage'>" + map.get(fromId + "Error") + "</span>";
		}
	        
	        if (map.get(toId + "Error") != null) {
	        	html += "<span class='errorMessage'>" + map.get(toId + "Error") + "</span>";
		}
	        
	        html += "</div></div>";
	        
	        //@formatter:on

		return html;
	}
}