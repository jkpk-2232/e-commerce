<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%
	HashMap itLocalVendorStoreTimeFields = (HashMap) request.getAttribute("it");
%> 
<div class="form-group">
    <table id="vendorStoreTimingsTable">
        <tr>
            <th><%=BusinessAction.messageForKeyAdmin("labelDaysOfWeek")%> </th>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek0", "Sunday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours0", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours0", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours0", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours0", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek1", "Monday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours1", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours1", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours1", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours1", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek2", "Tuesday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours2", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours2", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours2", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours2", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek3", "Wednesday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours3", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours3", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours3", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours3", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek4", "Thursday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours4", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours4", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours4", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours4", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek5", "Friday", itLocalVendorStoreTimeFields)%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours5", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours5", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours5", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours5", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
        <tr>
            <td><%=NewThemeUiUtils.outputCheckbox("daysOfWeek6", "Saturday", itLocalVendorStoreTimeFields)%>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningMorningHours6", BusinessAction.messageForKeyAdmin("labelMorningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingMorningHours6", BusinessAction.messageForKeyAdmin("labelMorningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateOpeningEveningHours6", BusinessAction.messageForKeyAdmin("labelEveningOpeningHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
            <td><%=NewThemeUiUtils.outputTimePickerInputField("dateClosingEveningHours6", BusinessAction.messageForKeyAdmin("labelEveningClosingHours"), true, itLocalVendorStoreTimeFields, "col-sm-5", "col-sm-5")%></td>
        </tr>
    </table>
</div>
