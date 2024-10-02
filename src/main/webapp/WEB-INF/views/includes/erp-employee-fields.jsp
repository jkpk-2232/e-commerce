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
	HashMap itLocalErpEmployeeFields = (HashMap) request.getAttribute("it");
%>

<ul class="nav nav-tabs nav-tabs-v2">
	
	<li class="nav-item me-3">
		<a href="#tab1" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelPersonalDetails")%>
		</a>
	</li>
	
	<li class="nav-item me-3">
		<a href="#tab2" class="nav-link" data-bs-toggle="tab">
			<%=BusinessAction.messageForKeyAdmin("labelAccessManagement")%>
		</a>
	</li>
</ul>
<div class="tab-content pt-3">
	
	<div class="tab-pane fade show active" id="tab1">
		
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalErpEmployeeFields, "col-sm-3", "col-sm-5")%>	

		<%=NewThemeUiUtils.outputInputField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelFirstName"), true, 1, 30, itLocalErpEmployeeFields, "text", "col-sm-3", "col-sm-5")%>

		<%=NewThemeUiUtils.outputInputField(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), true, 1, 30, itLocalErpEmployeeFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.EMAIL_ADDRESS, BusinessAction.messageForKeyAdmin("labelEmailAddress"), true, 1, 30, itLocalErpEmployeeFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, itLocalErpEmployeeFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.CITY, BusinessAction.messageForKeyAdmin("labelCity"), true, 1, 30, itLocalErpEmployeeFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputFieldForPhoneNumber(FieldConstants.PHONE, FieldConstants.COUNTRY_CODE, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), true, 1, 30, itLocalErpEmployeeFields, "number", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PROFILE_IMAGE_URL, BusinessAction.messageForKeyAdmin("labelProfileImage"), itLocalErpEmployeeFields, "col-sm-3", "col-sm-10")%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, itLocalErpEmployeeFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, itLocalErpEmployeeFields)%>
		
	</div>
	
	<div class="tab-pane fade" id="tab2">
	
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelBrands"), true, 1, itLocalErpEmployeeFields, "col-sm-3", "col-sm-5")%>	
	
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.ACCESS_LIST, BusinessAction.messageForKeyAdmin("labelCapabilities"), true, 1, itLocalErpEmployeeFields, "col-sm-3", "col-sm-5")%>
	
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.USER_ID, itLocalErpEmployeeFields)%>
		
		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
	
	</div>

</div>