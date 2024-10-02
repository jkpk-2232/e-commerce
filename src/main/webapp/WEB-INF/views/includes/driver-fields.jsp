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
	HashMap itLocalDriverFields = (HashMap) request.getAttribute("it");
%>

<ul class="nav nav-tabs nav-tabs-v2">
	
	<li class="nav-item me-3">
		<a href="#tab1" class="nav-link active" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelPersonalDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab2" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelCarDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab3" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelMailingAddress")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab4" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelBankDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab5" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelScannedDocuments")%>
		</a>
	</li>
	
</ul>
<div class="tab-content pt-3">
	
	<div class="tab-pane fade show active" id="tab1">
	
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ID, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.INSURANCE_EFFECTIVE_DATE, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.INSURANCE_EXPIRATION_DATE, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFirstName"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, itLocalDriverFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.GENDER, BusinessAction.messageForKeyAdmin("labelGender"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.DOB, BusinessAction.messageForKeyAdmin("labelDateOfBirth"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.SOCIAL_SECURITY_NUMBER, BusinessAction.messageForKeyAdmin("labelDriverVoterId"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.AGENT_NUMBER, BusinessAction.messageForKeyAdmin("labelAgentNumber"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>	
		
		<% 
			Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
			String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);
		
			if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
		%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>
		
		<% 
			}
		%>
		
	</div>
	
	<div class="tab-pane fade" id="tab2">
	
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.SERVICE_TYPE, BusinessAction.messageForKeyAdmin("labelServiceType"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>

      	<div id="carTypesAndCarsDiv">
      		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.CAR_TYPE_LIST, BusinessAction.messageForKeyAdmin("labelCarType"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>	
	  		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CAR, BusinessAction.messageForKeyAdmin("labelCar"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>
     	</div>
     	
     	<div id="driverTypeDiv">
     		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.TRANSMISSION_TYPE_LIST, BusinessAction.messageForKeyAdmin("labelTransmissionType"), true, 1, itLocalDriverFields, "col-sm-3", "col-sm-5")%>	
     	</div>
     	
     	<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVING_LICENSE, BusinessAction.messageForKeyAdmin("labelDrivingLicense"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
     	
     	<%=NewThemeUiUtils.outputInputField(FieldConstants.REPORT_RANGE, BusinessAction.messageForKeyAdmin("labelInsuranceDateRange"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
     	
     	<%=NewThemeUiUtils.outputInputField(FieldConstants.LICENSE_EXPIRATION_DATE, BusinessAction.messageForKeyAdmin("labelDrivingLicenseExpiryDate"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
     	
     	<%=NewThemeUiUtils.outputInputField(FieldConstants.DRIVER_PAYABLE_PERCENTAGE, BusinessAction.messageForKeyAdmin("labelRentalDriverPayablePercentage"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
	
	</div>
	
	<div class="tab-pane fade" id="tab3">
	
		<%=NewThemeUiUtils.outputInputField(FieldConstants.STREET_1, BusinessAction.messageForKeyAdmin("labelStreet1"), false, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.STREET_2, BusinessAction.messageForKeyAdmin("labelStreet2"), false, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.COUNTRY, BusinessAction.messageForKeyAdmin("labelCountry"), false, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.STATE, BusinessAction.messageForKeyAdmin("labelState"), false, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), false, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
	
	</div>
	
	<div class="tab-pane fade" id="tab4">
	
		<%=NewThemeUiUtils.outputInputField(FieldConstants.BANK_NAME, BusinessAction.messageForKeyAdmin("labelBankName"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.ACCOUNT_NUMBER, BusinessAction.messageForKeyAdmin("labelAccountNumber"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.ACCOUNT_NAME, BusinessAction.messageForKeyAdmin("labelAccountName"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.ROUTING_NUMBER, BusinessAction.messageForKeyAdmin("labelRoutingNumber"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.TYPE, BusinessAction.messageForKeyAdmin("labelType"), true, 1, 30, itLocalDriverFields, "text", "col-sm-3", "col-sm-5")%>
	
		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	
	
	</div>
	
	<div class="tab-pane fade" id="tab5">

		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PROFILE_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelProfileImage"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.SOCIAL_SECURITY_IMG_URL, BusinessAction.messageForKeyAdmin("labelVoterOrAadharCard"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN_DUMMY, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.DRIVING_LICENSE_PHOTO, BusinessAction.messageForKeyAdmin("labelDrivingLicensePhoto"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN_DUMMY, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL, BusinessAction.messageForKeyAdmin("labelDrivingLicenseBackPhoto"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN_DUMMY, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL, BusinessAction.messageForKeyAdmin("labelAccreditationPhoto"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN_DUMMY, itLocalDriverFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL, BusinessAction.messageForKeyAdmin("labelCriminalReportPhoto"), itLocalDriverFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN, itLocalDriverFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN_DUMMY, itLocalDriverFields)%>
		
	</div>
	
</div>