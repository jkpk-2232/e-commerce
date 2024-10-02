<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="com.webapp.models.CarTypeModel"%>
<%@page import="com.jeeutils.StringUtils"%>
<%@page import="com.utils.myhub.MyHubUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelSetCarPriority")%></title>
	</head>

	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
			
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelSetCarPriority"), UrlConstants.JSP_URLS.MANAGE_CAR_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
			
					<div class="card-body">
			
						<form method="POST" id="setVendorCarPriority" name="setVendorCarPriority" action="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL%>">
							
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.QUERY_STRING, it)%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
							
							<table id="carFareTableEcommerce" class="w-100">
					       
					           <tr>
					               <th><%=BusinessAction.messageForKeyAdmin("labelAvailableCarTypes")%></th>
					               <th><%=BusinessAction.messageForKeyAdmin("labelPriority")%></th>
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
							            	<div style="width: 150px; height: 30px;font-weight: 700;">
							            		<img src="${pageContext.request.contextPath}<%=carTypeListEcommerce.get(i).getIconPath()%>" style="height:30px;width:30px;">
							            		<%=carTypeListEcommerce.get(i).getCarType()%> - (<%=carTypeListEcommerce.get(i).getIconName()%>)
							            	</div>
							            </td>
							            <td><%=NewThemeUiUtils.outputInputField(carTypeId + "_Priority_" + ECOMMERCE_ID, null, true, 1, 30, it, "number", null, "col-sm-5")%></td>
							        </tr>
							    <% 
											}
							    		} 
									} else {
								%>
									<tr class="h-100px">      
							            <td><%=NewThemeUiUtils.outputErrorDiv(BusinessAction.messageForKeyAdmin("errorNoCarTypesAvailable"))%></td>
							            <td></td>
							        </tr>
								<%
									}
							    %>
					            
					        </table>
					        
					        <%=NewThemeUiUtils.outputButtonDoubleField("btnSave", BusinessAction.messageForKeyAdmin("labelSave"), NewThemeUiUtils.MAIN_BUTTON_CSS_CLASS, "btnCancel", BusinessAction.messageForKeyAdmin("labelCancel"), NewThemeUiUtils.OTHER_BUTTON_CSS_CLASS, "offset-sm-5")%>
														
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