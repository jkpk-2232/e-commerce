<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.models.CarTypeModel"%>
<%@page import="com.jeeutils.StringUtils"%>
<%@page import="com.utils.myhub.MyHubUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelEditCity")%></title>
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>
		
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelEditCity"), UrlConstants.JSP_URLS.MANAGE_MULTICITY_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="editMultiCity" name="editMultiCity" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.EDIT_VENDOR_CITY_SETTINGS_URL%>">
							
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
									
									<%=NewThemeUiUtils.outputStaticField(FieldConstants.AREA_DISPLAY_NAME, BusinessAction.messageForKeyAdmin("labelCityName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
		
									<%=NewThemeUiUtils.outputStaticField(FieldConstants.AREA_NAME, BusinessAction.messageForKeyAdmin("labelRegionName"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputStaticField(FieldConstants.RADIUS, BusinessAction.messageForKeyAdmin("labelRadius"), true, 1, 30, it, "text", "col-sm-3", "col-sm-5")%>
									
									<%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-3")%>
									
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.MULTICITY_REGION_ID, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_PLACE_ID, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LATITUDE, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_LONGITUDE, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.AREA_RADIUS, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.QUERY_STRING, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CAR_AVAILABLE_LIST, it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("errorDataAvailableCarList", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("dataAvailableCarListTransportation", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("dataAvailableCarListCourier", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("dataAvailableCarListEcommerce", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("fareAvailableCarListTransportation", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("fareAvailableCarListCourier", it)%>
									<%=NewThemeUiUtils.outputFormHiddenField("fareAvailableCarListEcommerce", it)%>
							       
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
											if (it.containsKey("dataAvailableCarListTransportation")) {
												String tempTransportation = it.get("dataAvailableCarListTransportation").toString();
												String[] tempArrayTransportation = MyHubUtils.splitStringByCommaArray(tempTransportation);
												List<String> allowedCarListTransportation = Arrays.asList(tempArrayTransportation); 
												List<CarTypeModel> carTypeListTransportation = CarTypeModel.getAllCars();
												for(int i = 0; i < carTypeListTransportation.size(); i+=1) {
													String carTypeId = carTypeListTransportation.get(i).getCarTypeId();
													if (allowedCarListTransportation.contains(carTypeId)) {
													String divId = carTypeId + "_" + TRANSPORTATION_ID + "_" + "Div";
									    %>
									        <tr id="<%=divId%>" class="h-100px">      
									            <td>
									            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
									            		<img src="${pageContext.request.contextPath}<%=carTypeListTransportation.get(i).getIconPath()%>" style="height:30px;width:30px;">
									            		<%=carTypeListTransportation.get(i).getCarType()%> - (<%=carTypeListTransportation.get(i).getIconName()%>)
									            	</div>
									            </td>
									            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + TRANSPORTATION_ID, null, it)%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									        </tr>
								    	<% 
													}
												}
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
											if (it.containsKey("dataAvailableCarListCourier")) {
												String tempCourier = it.get("dataAvailableCarListCourier").toString();
												String[] tempArrayCourier = MyHubUtils.splitStringByCommaArray(tempCourier);
												List<String> allowedCarListCourier = Arrays.asList(tempArrayCourier); 
												List<CarTypeModel> carTypeListCourier = CarTypeModel.getAllCars();
												for(int i = 0; i < carTypeListCourier.size(); i+=1) {
													String carTypeId = carTypeListCourier.get(i).getCarTypeId();
													if (allowedCarListCourier.contains(carTypeId)) {
													String divId = carTypeId + "_" + COURIER_ID + "_" + "Div";
										%>
									        <tr id="<%=divId%>" class="h-100px">      
									            <td>
									            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
									            		<img src="${pageContext.request.contextPath}<%=carTypeListCourier.get(i).getIconPath()%>" style="height:30px;width:30px;">
									            		<%=carTypeListCourier.get(i).getCarType()%> - (<%=carTypeListCourier.get(i).getIconName()%>)
									            	</div>
									            </td>
									            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + COURIER_ID, null, it)%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									        </tr>
									    <% 
													}
									    		} 
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
											if (it.containsKey("dataAvailableCarListEcommerce")) {
												String tempEcommerce = it.get("dataAvailableCarListEcommerce").toString();
												String[] tempArrayEcommerce = MyHubUtils.splitStringByCommaArray(tempEcommerce);
												List<String> allowedCarListEcommerce = Arrays.asList(tempArrayEcommerce); 
												List<CarTypeModel> carTypeListEcommerce = CarTypeModel.getAllCars();
												for(int i = 0; i < carTypeListEcommerce.size(); i+=1) {
													String carTypeId = carTypeListEcommerce.get(i).getCarTypeId();
													if (allowedCarListEcommerce.contains(carTypeId)) {
													String divId = carTypeId + "_" + ECOMMERCE_ID + "_" + "Div";
										%>
									        <tr id="<%=divId%>" class="h-100px">      
									            <td>
									            	<div style="width: 150px; height: 30px;margin-top: -11%;font-weight: 700;">
									            		<img src="${pageContext.request.contextPath}<%=carTypeListEcommerce.get(i).getIconPath()%>" style="height:30px;width:30px;">
									            		<%=carTypeListEcommerce.get(i).getCarType()%> - (<%=carTypeListEcommerce.get(i).getIconName()%>)
									            	</div>
									            </td>
									            <td><%=NewThemeUiUtils.outputCheckbox(carTypeId + "_IsAvailable_" + ECOMMERCE_ID, null, it)%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_InitialFare_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-10")%></td>
									        </tr>
									    <% 
													}
									    		} 
											}
									    %>
							            
							        </table>
							        
							    </div>
							    
							</div>
							
						</form>
						
					</div>
					
					<%=NewThemeUiUtils.outputCardBodyArrows()%>
					
				</div>

			</div>
			<!-- END #formGrid -->
			
		</div>
		<!-- END #content -->
			
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>