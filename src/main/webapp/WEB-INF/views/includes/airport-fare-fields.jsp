<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap itLocalAirportFields = (HashMap) request.getAttribute("it");
%> 

<ul class="nav nav-tabs nav-tabs-v2">

    <li class="nav-item me-3">
        <a href="#tab1" class="nav-link active" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelAirportPickUp")%> </a>
    </li>

    <li class="nav-item me-3">
        <a href="#tab2" class="nav-link" data-bs-toggle="tab"><%=BusinessAction.messageForKeyAdmin("labelAirportDrop")%> </a>
    </li>

</ul>

<div class="tab-content pt-3">

    <div class="tab-pane fade show active" id="tab1">

        <table id="carFareTableAirportPickup" style="width: 100%">

            <tr>
                <th>
                    <div><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></div>
                </th>
                <th>
                    <div id="btnFirstVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Fifth_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnSecondVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Second_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnThirdVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Third_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnFourthVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Fourth_cab.png">
                    </div>
                </th>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelInitialFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelPerKmFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelPerMinFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelFreeDistance")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelKmToIncreaseFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelFareAfterSpecificKm")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmFirstVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmSecondVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmThirdVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmFourthVehicleAirportPickUp", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
        </table>
        
    </div>
    
    <div class="tab-pane fade" id="tab2">
    
        <table id="carFareTableAirportDrop" style="width: 100%">
        
            <tr>
                <th>
                    <div><%=BusinessAction.messageForKeyAdmin("labelCarFareFields")%></div>
                </th>
                <th>
                    <div id="btnFirstVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Fifth_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnSecondVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Second_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnThirdVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Third_cab.png">
                    </div>
                </th>
                <th>
                    <div id="btnFourthVehicle" class="carClass leftButtonClass">
                        <img alt="" src="${pageContext.request.contextPath}/assets/image/Fourth_cab.png">
                    </div>
                </th>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelInitialFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("initialFareFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelPerKmFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perKmFareFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelPerMinFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("perMinuteFareFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelFreeDistance")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("freeDistanceFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelKmToIncreaseFare")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("kmToIncreaseFareFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
            <tr>
                <td><%=BusinessAction.messageForKeyAdmin("labelFareAfterSpecificKm")%><%=NewThemeUiUtils.getRequiredField()%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmFirstVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmSecondVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmThirdVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
                <td><%=NewThemeUiUtils.outputInputField("fareAfterSpecificKmFourthVehicleAirportDrop", null, true, 1, 30, itLocalAirportFields, "number", null, "col-sm-10")%></td>
            </tr>
            
        </table>
        
    </div>
    
</div>