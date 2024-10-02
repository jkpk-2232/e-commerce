<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalBrandAssociationFields = (HashMap) request.getAttribute("it");
%>

	
	<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
		
	<%=NewThemeUiUtils.outputLinSepartor()%>
		
	<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MULTICITY_REGION_ID, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, itLocalBrandAssociationFields, "col-sm-3", "col-sm-5")%>
	
	<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.BRAND_ID, BusinessAction.messageForKeyAdmin("labelBrand"), true, 1, itLocalBrandAssociationFields, "col-sm-3", "col-sm-5")%>
		
	<%=NewThemeUiUtils.outputInputField(FieldConstants.STORE_ADDRESS, BusinessAction.messageForKeyAdmin("labelAddress"), true, 1, 30, itLocalBrandAssociationFields, "text", "col-sm-3", "col-sm-5")%>
		
	<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.STORE_IMAGE, BusinessAction.messageForKeyAdmin("labelStoreImage"), itLocalBrandAssociationFields, "col-sm-3", "col-sm-10")%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_IMAGE_HIDDEN, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, itLocalBrandAssociationFields)%>
		
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_STORE_ID, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LAT, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LNG, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_ADDRESS_LAT, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_ADDRESS_LNG, itLocalBrandAssociationFields)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.STORE_PLACE_ID, itLocalBrandAssociationFields)%>
		
	<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
