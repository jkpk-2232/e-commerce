<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>

<%
	HashMap itLocalVendorFields = (HashMap) request.getAttribute("it");
%>

<ul class="nav nav-tabs nav-tabs-v2">
	
	<li class="nav-item me-3">
		<a href="#tab1" class="nav-link active" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelBrandInformation")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab2" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelPersonalDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab3" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelFeeManagement")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab4" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelProductCategory")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab5" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelAccessManagement")%>
		</a>
	</li>
</ul>
<div class="tab-content pt-3">
	
	<div class="tab-pane fade show active" id="tab1">
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_ID, BusinessAction.messageForKeyAdmin("labelService"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelCategory"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.VENDOR_BRAND_NAME, BusinessAction.messageForKeyAdmin("labelBrandName"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputTextAreaField(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, BusinessAction.messageForKeyAdmin("labelSearchKeywords"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5", 5)%>
					
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.VENDOR_BRAND_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelBrandImage"), itLocalVendorFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN, itLocalVendorFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN_DUMMY, itLocalVendorFields)%>				
			
	</div>
	
	<div class="tab-pane fade" id="tab2">
		
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>	

		<%=NewThemeUiUtils.outputInputField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFirstName"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>

		<%=NewThemeUiUtils.outputInputField(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, itLocalVendorFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.PHONEPE_MERCHANT_ID, BusinessAction.messageForKeyAdmin("labelPhonepeMerchantId"), false, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>

		<%=NewThemeUiUtils.outputInputField(FieldConstants.PHONEPE_MERCHANT_NAME, BusinessAction.messageForKeyAdmin("labelPhonepeMerchantName"), false, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.SALT_KEY, BusinessAction.messageForKeyAdmin("labelSaltKey"), false, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.SALT_INDEX, BusinessAction.messageForKeyAdmin("labelSaltIndex"), false, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PROFILE_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelProfileImage"), itLocalVendorFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, itLocalVendorFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, itLocalVendorFields)%>
		
	</div>
	
	<div class="tab-pane fade" id="tab3">
	
		<%=NewThemeUiUtils.outputInputField(FieldConstants.MAXIMUM_MARKUP_FARE, BusinessAction.messageForKeyAdmin("labelMaximumMarkupFare"), true, 1, 30, itLocalVendorFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscriptionFee"), true, 1, 30, itLocalVendorFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_START_DATE_TIME, BusinessAction.messageForKeyAdmin("labelVendorCurrentSubscriptionStartDateTime"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputStaticField(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_END_DATE_TIME, BusinessAction.messageForKeyAdmin("labelVendorCurrentSubscriptionEndDateTime"), true, 1, 30, itLocalVendorFields, "text", "col-sm-3", "col-sm-5")%>

		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM, BusinessAction.messageForKeyAdmin("labelIsSelfDeliveryWithinXKm"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>	
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.SELF_DELIVERY_FEE, BusinessAction.messageForKeyAdmin("labelSelfDeliveryFee"), true, 1, 30, itLocalVendorFields, "number", "col-sm-3", "col-sm-5")%>
	
	</div>
	
	<div class="tab-pane fade" id="tab4">
	
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.PRODUCT_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelProductCategory"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>
		
	</div>
	
	<div class="tab-pane fade" id="tab5">
	
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.EXPORTACCESSLIST, BusinessAction.messageForKeyAdmin("labelExportAccess"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>	

		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.ACCESS_LIST, BusinessAction.messageForKeyAdmin("labelCapabilities"), true, 1, itLocalVendorFields, "col-sm-3", "col-sm-5")%>
	
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.USER_ID, itLocalVendorFields)%>
		
		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
	
	</div>

</div>