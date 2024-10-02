<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalProductTemplateFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.BRAND_ID, BusinessAction.messageForKeyAdmin("labelBrand"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>
<!-- <button id="newButton" type="button" onclick="myFunction()">Click Me!</button>
<div id="brandFields">
	
</div> -->
<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelProductCategory"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_SUB_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelProductSubCategory"), false, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_NAME, BusinessAction.messageForKeyAdmin("labelProductName"), true, 1, 30, itLocalProductTemplateFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.HSN_CODE, BusinessAction.messageForKeyAdmin("labelHSNCode"), true, 1, 30, itLocalProductTemplateFields, "text", "col-sm-2", "col-sm-3")%>

<% 
	if (itLocalProductTemplateFields.containsKey(FieldConstants.ADD_PRODUCT_TEMPLATE_FLOW) && itLocalProductTemplateFields.get(FieldConstants.ADD_PRODUCT_TEMPLATE_FLOW).toString().equalsIgnoreCase("true")) {
%>

<%=NewThemeUiUtils.outputSelectMultiInputField(FieldConstants.UOM_ID, BusinessAction.messageForKeyAdmin("labelWeightUnits"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<% 
	} else { 
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.UOM_ID, BusinessAction.messageForKeyAdmin("labelWeightUnits"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<% 
	}
%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_INFORMATION, BusinessAction.messageForKeyAdmin("labelProductInformation"), true, 1, 30, itLocalProductTemplateFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_PRODUCT_TO_ALL, BusinessAction.messageForKeyAdmin("labelIsProductToAll"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.TAX_APPLICABLE, BusinessAction.messageForKeyAdmin("labelTaxApplicable"), true, 1, itLocalProductTemplateFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_SPECIFICATION, BusinessAction.messageForKeyAdmin("labelProductSpecification"), true, 1, 30, itLocalProductTemplateFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PRODUCT_IMAGE, BusinessAction.messageForKeyAdmin("labelProductImage"), itLocalProductTemplateFields, "col-sm-3", "col-sm-10")%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_IMAGE_HIDDEN, itLocalProductTemplateFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, itLocalProductTemplateFields)%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_TEMPLATE_ID, itLocalProductTemplateFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("isExists", itLocalProductTemplateFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	