<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalProductVariantFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_TEMPLATE_ID, BusinessAction.messageForKeyAdmin("labelProductTemplate"), true, 1, itLocalProductVariantFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_VARIANT_NAME, BusinessAction.messageForKeyAdmin("labelProductVariantName"), true, 1, 30, itLocalProductVariantFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_VARIANT_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelProductVariantDescription"), true, 1, 30, itLocalProductVariantFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.IS_NON_VEG, BusinessAction.messageForKeyAdmin("labelIsNonVeg"), true, 1, itLocalProductVariantFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_QUANTITY_TYPE, BusinessAction.messageForKeyAdmin("labelProductQuantityType"), true, 1, itLocalProductVariantFields, "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_VARIANT_PRICE, BusinessAction.messageForKeyAdmin("labelProductVariantPrice"), true, 1, 30, itLocalProductVariantFields, "number", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.BARCODE, BusinessAction.messageForKeyAdmin("labelProductBarcode"), false, 1, 30, itLocalProductVariantFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.WEIGHT, BusinessAction.messageForKeyAdmin("labelProductWeight"), true, 1, 30, itLocalProductVariantFields, "number", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.COLOR, BusinessAction.messageForKeyAdmin("labelColor"), false, 1, 30, itLocalProductVariantFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_VARIANT_ID, itLocalProductVariantFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	