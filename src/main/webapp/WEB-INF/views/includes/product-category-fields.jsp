<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalProductCategoryFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelProductCategory"), true, 1, 30, itLocalProductCategoryFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PRODUCT_CATEGORY_DESCRIPTION, BusinessAction.messageForKeyAdmin("labelProductCategoryDescription"), true, 1, 30, itLocalProductCategoryFields, "text", "col-sm-2", "col-sm-3")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_CATEGORY_ID, itLocalProductCategoryFields)%>

<%=NewThemeUiUtils.outputImageUploadField(FieldConstants.PRODUCT_CATEGORY_IMAGE, BusinessAction.messageForKeyAdmin("labelProductCategoryImage"), itLocalProductCategoryFields, "col-sm-3", "col-sm-10")%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN, itLocalProductCategoryFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN_DUMMY, itLocalProductCategoryFields)%>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-2")%>	