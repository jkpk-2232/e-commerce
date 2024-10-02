<%@page import="java.util.Map"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.ProjectConstants"%>
<%@page import="com.utils.myhub.SessionUtils"%>
<%@page import="java.util.List,java.util.ArrayList,java.util.Arrays"%>
<%@page import="com.webapp.models.ServiceModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>


<%
	HashMap itLocalFeedSettingsFields = (HashMap) request.getAttribute("it");
%>

<% 
	Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
	String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);

%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.FEED_SETTINGS_ID, itLocalFeedSettingsFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.QUERY_STRING, itLocalFeedSettingsFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("feedFareDetailsErrorHidden", itLocalFeedSettingsFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("feedFareAvailableList", itLocalFeedSettingsFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SERVICES_ACTIVE_LIST, itLocalFeedSettingsFields)%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalFeedSettingsFields, "col-sm-3", "col-sm-5")%>

<table id="carFareTableTransportation" class="w-100">

	<tr>
        <th><%=BusinessAction.messageForKeyAdmin("labelCategory")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelIsAvailable")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelBaseFare")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelPerMinuteFare")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelGSTPercentage")%></th>
    </tr>
    
    <% 
		String temp = itLocalFeedSettingsFields.containsKey(FieldConstants.SERVICES_ACTIVE_LIST) ? itLocalFeedSettingsFields.get(FieldConstants.SERVICES_ACTIVE_LIST).toString() : "";
		List<String> allowedServiceList = Arrays.asList(temp.split(",")); 
		
		boolean isCarTypeAllDisplay = false;
		
		if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
			isCarTypeAllDisplay = true;	
		}
		
		List<ServiceModel> serviceList = ServiceModel.getAllActiveServices();
		for(int i = 0; i < serviceList.size(); i+=1) {
			
			String serviceId = serviceList.get(i).getServiceId();
			if (isCarTypeAllDisplay || allowedServiceList.contains(serviceId)) {
				
			String divId = serviceId + "Div";
		
	%>
		<tr id="<%=divId%>" class="h-100px">
			<td>
            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
            		<%=serviceList.get(i).getServiceName()%> 
            	</div>
            </td>
            <td><%=NewThemeUiUtils.outputCheckbox(serviceId + "_IsAvailable", null, itLocalFeedSettingsFields)%> </td>
            <td><%=NewThemeUiUtils.outputInputField(serviceId + "_BaseFare", null, true, 1, 30, itLocalFeedSettingsFields, "number", null, "col-sm-10")%> </td>
            <td><%=NewThemeUiUtils.outputInputField(serviceId + "_PerMinuteFare", null, true, 1, 30, itLocalFeedSettingsFields, "number", null, "col-sm-10")%> </td>
            <td><%=NewThemeUiUtils.outputInputField(serviceId + "_GSTPercentage", null, true, 1, 30, itLocalFeedSettingsFields, "number", null, "col-sm-10")%> </td>
		</tr> 
	<% 	
			}
		}	
    %>
    
</table>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>