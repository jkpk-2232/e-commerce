<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalRackCategoryFields = (HashMap) request.getAttribute("it");
%>


<%=NewThemeUiUtils.outputInputField(FieldConstants.CATEGORY_NAME, BusinessAction.messageForKeyAdmin("labelCategoryName"), true, 1, 30, itLocalRackCategoryFields, "text", "col-sm-3", "col-sm-5")%>


<%=NewThemeUiUtils.outputInputField(FieldConstants.SLOT_HEIGHT, BusinessAction.messageForKeyAdmin("labelSlotHeight"), false, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.SLOT_WIDTH, BusinessAction.messageForKeyAdmin("labelSlotWidth"), false, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.MAX_WEIGHT, BusinessAction.messageForKeyAdmin("labelMaxWeight"), false, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.CHARGE_PER_SLOT, BusinessAction.messageForKeyAdmin("labelChargePerSlot"), false, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputInputField(FieldConstants.NO_OF_SLOTS, BusinessAction.messageForKeyAdmin("labelNoOfSlots"), true, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.NUMBER_OF_DAYS, BusinessAction.messageForKeyAdmin("labelNumberOfDays"), true, 1, 30, itLocalRackCategoryFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CATEGORY_ID, itLocalRackCategoryFields)%>



<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	