<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>
<%@page import="com.jeeutils.StringUtils"%>

<%
	HashMap itLocalProductFields = (HashMap) request.getAttribute("it");
%>

<% 
	Map<String, String> sessionAttributesProductFieldsLocal = SessionUtils.getSession(request, response, true);
	String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesProductFieldsLocal, LoginUtils.ROLE_ID);

	if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal)) {
%>

	<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>

<% 
	}
%>

<% 
	if (itLocalProductFields.containsKey(FieldConstants.ADD_PRODUCT_FLOW) && itLocalProductFields.get(FieldConstants.ADD_PRODUCT_FLOW).toString().equalsIgnoreCase("true")) {
%>

	<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORES, BusinessAction.messageForKeyAdmin("labelProductForAllStores"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>	
	
	<div id="vendorStoreIdDivOuterDiv">
		<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>	
	</div>

<% 
	}
%>

<% 
	if (itLocalProductFields.containsKey(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE) && StringUtils.validString(itLocalProductFields.get(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE).toString())) {
%>
	<%=NewThemeUiUtils.outputErrorDiv(itLocalProductFields.get(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE).toString())%>
<%
	}
%>

<%=NewThemeUiUtils.outputStaticField(FieldConstants.SERVICE_NAME, BusinessAction.messageForKeyAdmin("labelSuperService"), true, 1, 30, itLocalProductFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputStaticField(FieldConstants.CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelCategory"), true, 1, 30, itLocalProductFields, "text", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.BRAND_ID, BusinessAction.messageForKeyAdmin("labelBrand"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_TEMPLATE_ID, BusinessAction.messageForKeyAdmin("labelProductTemplate"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_VARIANT_ID, BusinessAction.messageForKeyAdmin("labelProductVariant"), true, 1, itLocalProductFields, "col-sm-3", "col-sm-5")%>

<%-- <%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_BARCODE, BusinessAction.messageForKeyAdmin("labelProductBarcode"), false, 1, 30, itLocalProductFields, "text", "col-sm-3", "col-sm-5")%> --%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_ACTUAL_PRICE, BusinessAction.messageForKeyAdmin("labelProductActualPrice"), true, 1, 30, itLocalProductFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_DISCOUNTED_PRICE, BusinessAction.messageForKeyAdmin("labelProductDiscountedPrice"), true, 1, 30, itLocalProductFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), true, 1, 30, itLocalProductFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_STORE_ID_LIST_HIDDEN, itLocalProductFields)%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_PRODUCT_ID, itLocalProductFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("isExists", itLocalProductFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	