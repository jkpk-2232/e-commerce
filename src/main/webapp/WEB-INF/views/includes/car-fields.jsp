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
	HashMap itLocalCarFields = (HashMap) request.getAttribute("it");
%>

<ul class="nav nav-tabs nav-tabs-v2">
	
	<li class="nav-item me-3">
		<a href="#tab1" class="nav-link active" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelCarDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab2" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelScannedDocuments")%>
		</a>
	</li>
	
	<% 
		if (itLocalCarFields.get(FieldConstants.PAGE_TYPE).toString().equalsIgnoreCase(FieldConstants.PAGE_TYPE_EDIT)) {
	%>
	
	<li class="nav-item me-3">
		<a href="#tab3" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelCarDrivers")%>
		</a>
	</li>
	
	<%
		}
	%>
	
</ul>
<div class="tab-content pt-3">
	
	<div class="tab-pane fade show active" id="tab1">
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CAR_MODEL_TYPE_ID, BusinessAction.messageForKeyAdmin("labelCarType"), true, 1, itLocalCarFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.CAR_COLOR, BusinessAction.messageForKeyAdmin("labelCarColor"), true, 1, 30, itLocalCarFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.MODEL_NAME, BusinessAction.messageForKeyAdmin("labelModelName"), true, 1, 30, itLocalCarFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.OWNER, BusinessAction.messageForKeyAdmin("labelOwnerNumber"), true, 1, 30, itLocalCarFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.MAKE, BusinessAction.messageForKeyAdmin("labelCarMake"), true, 1, 30, itLocalCarFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.CAR_PLATE_NUMBER, BusinessAction.messageForKeyAdmin("labelCarPlateNo"), true, 1, 30, itLocalCarFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.CAR_YEAR, BusinessAction.messageForKeyAdmin("labelCarYear"), true, 1, itLocalCarFields, "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.NUMBER_OF_PASSENGER, BusinessAction.messageForKeyAdmin("labelSeatingCapacity"), true, 1, itLocalCarFields, "col-sm-3", "col-sm-5")%>
		
		<% 
			Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
			String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);
		
			if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
		%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, itLocalCarFields, "col-sm-3", "col-sm-5")%>
		
		<% 
			}
		%>
		
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_ID, itLocalCarFields)%>

		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
		
	</div>
	
	<div class="tab-pane fade" id="tab2">
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_FRONT_IMG, BusinessAction.messageForKeyAdmin("labelFrontImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_FRONT_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_FRONT_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_BACK_IMG, BusinessAction.messageForKeyAdmin("labelBackImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_BACK_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_BACK_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_INSURANCE_IMG, BusinessAction.messageForKeyAdmin("labelInsuranceImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_INSURANCE_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_INSURANCE_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_INSPECTION_IMG, BusinessAction.messageForKeyAdmin("labelInspectionImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_INSPECTION_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_INSPECTION_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG, BusinessAction.messageForKeyAdmin("labelVehicleCommercialLicenseImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.CAR_REGISTRATION_IMG, BusinessAction.messageForKeyAdmin("labelVehicleRegistrationImage"), itLocalCarFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN, itLocalCarFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN_DUMMY, itLocalCarFields)%>
		
	</div>
	
	<% 
		if (itLocalCarFields.get(FieldConstants.PAGE_TYPE).toString().equalsIgnoreCase(FieldConstants.PAGE_TYPE_EDIT)) {
	%>
	
	<div class="tab-pane fade" id="tab3">
	
		<%=NewThemeUiUtils.outputLinSepartor()%>
						
		<% 
			List<String> columnNames = new ArrayList<>();
			columnNames.add(BusinessAction.messageForKeyAdmin("labelId"));
			columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
			columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverName"));
			columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverPhoneNumber"));
			columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));
		%>
		
		<%=NewThemeUiUtils.outputDatatable("datatableDefault", "", columnNames)%>
	
	</div>
	
	<%
		}
	%>
	
</div>