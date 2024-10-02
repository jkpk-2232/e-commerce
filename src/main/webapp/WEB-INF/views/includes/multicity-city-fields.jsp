<%@page import="com.jeeutils.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="java.util.List"%>
<%@page import="com.webapp.models.CarTypeModel"%>
<%@page import="com.webapp.ProjectConstants"%>

<%
	HashMap itLocalMulticityFields = (HashMap) request.getAttribute("it");
	List<CarTypeModel> carTypeList = CarTypeModel.getAllCars();
%> 

<ul class="nav nav-tabs nav-tabs-v2">

    <li class="nav-item me-3">
        <a href="#tab1" class="nav-link active" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelCitySettings")%> </a>
    </li>

    <li class="nav-item me-3">
        <a href="#tab2" class="nav-link" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelTransportationFareSettings")%> </a>
    </li>
    
    <li class="nav-item me-3">
        <a href="#tab3" class="nav-link" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelCourierFareSettings")%> </a>
    </li>

	<li class="nav-item me-3">
        <a href="#tab4" class="nav-link" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelEcommerceFareSettings")%> </a>
    </li>

</ul>

<div class="tab-content pt-3">

    <div class="tab-pane fade show active" id="tab1">
    
    	<%=NewThemeUiUtils.outputGoogleMapField("dashboardMapCanvas", BusinessAction.messageForKeyAdmin("labelGoogleMap"), "height: 80vh !important;")%>
		
		<%=NewThemeUiUtils.outputLinSepartor()%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.AREA_DISPLAY_NAME, BusinessAction.messageForKeyAdmin("labelCityName"), true, 1, 30, itLocalMulticityFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputInputField(FieldConstants.AREA_NAME, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, 30, itLocalMulticityFields, "text", "col-sm-3", "col-sm-5")%>
		
		<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RADIUS, BusinessAction.messageForKeyAdmin("labelRadius"), true, 1, itLocalMulticityFields, "col-sm-3", "col-sm-5")%>
		
		<% 
			if (itLocalMulticityFields.containsKey(FieldConstants.IS_EDIT_CITY) && StringUtils.validString(itLocalMulticityFields.get(FieldConstants.IS_EDIT_CITY).toString()) && itLocalMulticityFields.get(FieldConstants.IS_EDIT_CITY).toString().equalsIgnoreCase("true")) {
		%>
				<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.OVERRIDE_FARE, BusinessAction.messageForKeyAdmin("labelOverrideVendorFare"), true, 1, itLocalMulticityFields, "col-sm-3", "col-sm-5")%>	
		<% 
			}
		%>
		
		<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
		
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.MULTICITY_REGION_ID, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_PLACE_ID, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LATITUDE, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LONGITUDE, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_RADIUS, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.QUERY_STRING, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_AVAILABLE_LIST, itLocalMulticityFields)%>
		<%=NewThemeUiUtils.outputFormHiddenField("errorDataAvailableCarList", itLocalMulticityFields)%>
        
    </div>
    
    <div class="tab-pane fade" id="tab2">
    
        <table id="carFareTableTransportation" class="w-100">
        
            <tr>
                <th><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelIsAvailable")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelInitialFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerKmFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerMinFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFreeDistance")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelKmToIncreaseFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFareAfterSpecificKm")%></th>
            </tr>
            
            <% 
				String TRANSPORTATION_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;
				for(int i = 0; i < carTypeList.size(); i+=1) {
						
					String carTypeId = carTypeList.get(i).getCarTypeId();
					String divId = carTypeId + "_" + TRANSPORTATION_ID + "_" + "Div";
			%>
			        <tr id="<%=divId%>" class="h-100px">      
			            <td>
			            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
			            		<img src="${pageContext.request.contextPath}<%=carTypeList.get(i).getIconPath()%>" style="height:30px;width:30px;">
			            		<%=carTypeList.get(i).getCarType()%> - (<%=carTypeList.get(i).getIconName()%>)
			            	</div>
			            </td>
			            
			            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + TRANSPORTATION_ID, null, itLocalMulticityFields)%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			        </tr>
		    <% 
		    	} 
		    %>
            
        </table>
        
    </div>
    
    <div class="tab-pane fade" id="tab3">
    
        <table id="carFareTableCourier" class="w-100">
        
            <tr>
                <th><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelIsAvailable")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelInitialFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerKmFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerMinFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFreeDistance")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelKmToIncreaseFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFareAfterSpecificKm")%></th>
            </tr>
            
            <% 
            	String COURIER_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID;
				for(int i = 0; i < carTypeList.size(); i+=1) {
						
					String carTypeId = carTypeList.get(i).getCarTypeId();
					String divId = carTypeId + "_" + COURIER_ID + "_" + "Div";
			%>
			        <tr id="<%=divId%>" class="h-100px">      
			            <td>
			            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
			            		<img src="${pageContext.request.contextPath}<%=carTypeList.get(i).getIconPath()%>" style="height:30px;width:30px;">
			            		<%=carTypeList.get(i).getCarType()%> - (<%=carTypeList.get(i).getIconName()%>)
			            	</div>
			            </td>
			            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + COURIER_ID, null, itLocalMulticityFields)%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			        </tr>
		    <% 
		    	} 
		    %>
            
        </table>
        
    </div>
    
    <div class="tab-pane fade" id="tab4">
    
        <table id="carFareTableEcommerce" class="w-100">
        
            <tr>
                <th><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelIsAvailable")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelInitialFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerKmFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelPerMinFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFreeDistance")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelKmToIncreaseFare")%></th>
                <th><%=BusinessAction.messageForKeyAdmin("labelFareAfterSpecificKm")%></th>
            </tr>
            
            <% 
            	String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;
				for(int i = 0; i < carTypeList.size(); i+=1) {
						
					String carTypeId = carTypeList.get(i).getCarTypeId();
					String divId = carTypeId + "_" + ECOMMERCE_ID + "_" + "Div";
			%>
			        <tr id="<%=divId%>" class="h-100px">      
			            <td>
			            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
			            		<img src="${pageContext.request.contextPath}<%=carTypeList.get(i).getIconPath()%>" style="height:30px;width:30px;">
			            		<%=carTypeList.get(i).getCarType()%> - (<%=carTypeList.get(i).getIconName()%>)
			            	</div>
			            </td>
			            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + ECOMMERCE_ID, null, itLocalMulticityFields)%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, null, true, 1, 30, itLocalMulticityFields, "number", null, "col-sm-10")%></td>
			        </tr>
		    <% 
		    	} 
		    %>
            
        </table>
        
    </div>
    
</div>