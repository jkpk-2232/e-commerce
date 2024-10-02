<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.ProjectConstants"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.utils.LoginUtils"%>
<%@page import="com.utils.myhub.SessionUtils"%>
<%@page import="com.utils.myhub.MyHubUtils"%>
<%@page import="java.util.List,java.util.ArrayList,java.util.Arrays"%>
<%@page import="com.webapp.models.CarTypeModel"%>

<%
	HashMap itLocalRentalPackageFields = (HashMap) request.getAttribute("it");
%>

<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.RENTAL_PACKAGE_ID, itLocalRentalPackageFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.QUERY_STRING, itLocalRentalPackageFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("carFareDetailsErrorHidden", itLocalRentalPackageFields)%>
<%=NewThemeUiUtils.outputFormHiddenField("fareAvailableCarList", itLocalRentalPackageFields)%>
<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_AVAILABLE_LIST, itLocalRentalPackageFields)%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.REGION_LIST, BusinessAction.messageForKeyAdmin("labelRegion"), true, 1, itLocalRentalPackageFields, "col-sm-3", "col-sm-5")%>	

<% 
	Map<String, String> sessionAttributesCarFieldsLocal = SessionUtils.getSession(request, response, true);
	String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesCarFieldsLocal, LoginUtils.ROLE_ID);

	if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, itLocalRentalPackageFields, "col-sm-3", "col-sm-5")%>

<% 
	}
%>

<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RENTAL_PACKAGE_TYPE, BusinessAction.messageForKeyAdmin("labelPackageType"), true, 1, itLocalRentalPackageFields, "col-sm-3", "col-sm-5")%>	

<%=NewThemeUiUtils.outputInputField(FieldConstants.PACKAGE_TIME, BusinessAction.messageForKeyAdmin("labelPackageTime"), true, 1, 30, itLocalRentalPackageFields, "number", "col-sm-3", "col-sm-5")%>

<%=NewThemeUiUtils.outputInputField(FieldConstants.PACKAGE_DISTANCE, BusinessAction.messageForKeyAdmin("labelPackageDistance"), true, 1, 30, itLocalRentalPackageFields, "number", "col-sm-3", "col-sm-5")%>

<table id="carFareTableTransportation" class="w-100">
    <tr>
        <th><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelIsAvailable")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelBaseFare")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelRentalPerKmFare")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelRentalPerMinuteFare")%></th>
        <th><%=BusinessAction.messageForKeyAdmin("labelRentalDriverPayablePercentage")%></th>
    </tr>
	<% 
		String temp = itLocalRentalPackageFields.containsKey(FieldConstants.CAR_AVAILABLE_LIST) ? itLocalRentalPackageFields.get(FieldConstants.CAR_AVAILABLE_LIST).toString() : "";
		List<String> allowedCarList = MyHubUtils.splitStringByCommaList(temp); 
		
		boolean isCarTypeAllDisplay = false;
		
		if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal))	{
			isCarTypeAllDisplay = true;	
		}
		
		List<CarTypeModel> carTypeList = CarTypeModel.getAllCars();
		for(int i = 0; i < carTypeList.size(); i+=1) {
			
			String carTypeId = carTypeList.get(i).getCarTypeId();
			if (isCarTypeAllDisplay || allowedCarList.contains(carTypeId)) {
				
			String divId = carTypeId + "Div";
	%>
        <tr id="<%=divId%>" class="h-100px">      
            <td>
            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
            		<img src="${pageContext.request.contextPath}<%=carTypeList.get(i).getIconPath()%>" style="height:30px;width:30px;">
            		<%=carTypeList.get(i).getCarType()%> - (<%=carTypeList.get(i).getIconName()%>)
            	</div>
            </td>
            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable", null, itLocalRentalPackageFields)%> </td>
            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_BaseFare", null, true, 1, 30, itLocalRentalPackageFields, "number", null, "col-sm-10")%> </td>
            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare", null, true, 1, 30, itLocalRentalPackageFields, "number", null, "col-sm-10")%> </td>
            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare", null, true, 1, 30, itLocalRentalPackageFields, "number", null, "col-sm-10")%> </td>
            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_DriverPayablePercentage", null, true, 1, 30, itLocalRentalPackageFields, "number", null, "col-sm-10")%> </td>
        </tr>
    <% 	
			}
    	} 
    %>
	
</table>

<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>	