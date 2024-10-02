<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalProductSubCategoryFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_SUB_CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelProductSubCategory"), true, 1, 30, itLocalProductSubCategoryFields, "text", "col-sm-3", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_SUB_CATEGORY_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelProductSubCategoryDescription"), true, 1, 30, itLocalProductSubCategoryFields, "text", "col-sm-3", "col-sm-3")%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.PRODUCT_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelProductCategory"), true, 1, itLocalProductSubCategoryFields, "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_SUB_CATEGORY_ID, itLocalProductSubCategoryFields)%>

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE, BusinessAction.messageForKeyAdmin("labelProductSubCategoryImage"), itLocalProductSubCategoryFields, "col-sm-3", "col-sm-10")%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN, itLocalProductSubCategoryFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN_DUMMY, itLocalProductSubCategoryFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	