<!DOCTYPE html>
<%@page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.utils.view.ButtonUtils"%>
<%@page import="com.utils.view.ActionButton"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<html>

    <head>
    
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, minimal-ui" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
    
        <%@include file="/WEB-INF/views/includes/common-head.jsp"%>
        <title><%=it.get("labelFareCalculator").toString()%></title>
    
        <style type="text/css">
        
        	.buttonDiv {
        		margin-left: 1.5% !important;
    			margin-top: 1% !important;
        	}
        
        </style>
    
    </head>
    
    <body>
    
		<%@include file="/WEB-INF/views/includes/header.jsp"%>
	
 		<div class="dashboardWrapperRightPanel">
    
	        <%@ include file="/WEB-INF/views/includes/navigation.jsp"%>
	
	        <%@ include file="/WEB-INF/views/includes/intermediate-header.jsp"%>
	                            	
          	<!-- page specific code starts here -->
                          	
            	<div class="dashboardWrapperRightPanelOverlayMainInner">   
  
					<div class="dashboardWrapperRightPanelOverlayMainInnerContent container-fluid">
					
						<div class="dashboardOverlayMain">
					
					        <div class="dashboardOverlayMainTitle">

								<h2>
									<i class="glyphicon-calculator"></i>
					        		<%=it.get("labelFareCalculator").toString()%>
					        	</h2>
					        	
					        </div>
					        
					        <div class="dashboardSeperator"></div>
					        
					        <div class="taxiPageSepcificClass">
							
								<!-- Start Map -->
							
								<div class="col-md-13">
									
									<!-- Map -->
										
									<div class="mapDetails">
										<div id="fareMap" style="height: 500px;"></div>
									</div>
									
									<!-- End Map -->
									
								</div>
								
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>				
								
								<div class="carTypeDiv">

									<div id="btnFirstVehicle" class="carClass leftButtonClass" style="float: left;">
										<img alt="" src="${pageContext.request.contextPath}/assets/image/First_cab.png">
									</div>
									
									<div id="btnSecondVehicle" class="carClass leftButtonClass" style="margin-left: 6px;float: left;">
										<img alt="" src="${pageContext.request.contextPath}/assets/image/Second_cab.png">
									</div>
									
									<div id="btnThirdVehicle" class="carClass leftButtonClass" style="float: left;">
										<img alt="" src="${pageContext.request.contextPath}/assets/image/Third_cab.png">
									</div>
									
									<div id="btnFourthVehicle" class="carClass leftButtonClass" style="float: left;">
										<img alt="" src="${pageContext.request.contextPath}/assets/image/Fourth_cab.png">
									</div>
									
								</div>				
								
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
		                                
                                <%=NewUiViewUtils.outputFieldForLogin("pickUpLocation", "Pickup location", true, 1, 30, it, "text") %>
	                                                            
	                            <%=NewUiViewUtils.outputFieldForLogin("destLocation", "Drop location", true, 1, 30, it, "text") %>
	                            
								<div class="dashboardSeperator"></div>
								
                                <div class="buttonDiv">
		
									<div class="control-group">
						               	<div class="controls">
						        			<input id="btnCancel" type="button" value="Cancel" class="btn btn-primary" />
										</div>
									</div>
									
									<div class="control-group">
						               	<div class="controls">
						                	<input id="btnSave" type="button" value="Calculate Fare" class="btn btn-danger" />
										</div>
									</div>
									
								</div>
								
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
	                            
	                            <!-- Start Total -->
								
								<div class="col-md-13" style="margin-top: 3%; height: 55px;">
								
		                            <div class="block full" style="border: 0px;box-shadow: 0 0px #f1f1f1;">

		                                <div class="block-title">
		                                
		                                    <h3 id="total" style="text-align: center; font-size: 26px !important;">
		                                    	Total - 0
		                                    </h3>
		                                    
		                                </div>
		                                
		                            </div>
								
								</div>
								
								<!-- End Total -->
								
								<!-- Start Total -->
								
								<div class="col-md-13" style="margin-top: 3%; height: 55px;">
								
		                            <div class="block full" style="border: 0px;box-shadow: 0 0px #f1f1f1;">

		                                <div class="block-title">
		                                
		                                    <h3 id="distance" style="text-align: center; font-size: 26px !important;">
		                                    	Distance - 0
		                                    </h3>
		                                    
		                                </div>
		                                
		                            </div>
								
								</div>
								
								<!-- End Total -->
								
								<%=ViewUtils.outputFormHiddenField("currentLat", it) %>
								<%=ViewUtils.outputFormHiddenField("currentLng", it) %>		
								<%=ViewUtils.outputFormHiddenField("themeColor", it) %>	
								<%=ViewUtils.outputFormHiddenField("carType", it) %>		
									
					        </div>
					        
					        <div class="dashboardSeperator"></div>
					        <div class="dashboardSeperator"></div>
					        <div class="dashboardSeperator"></div>
					        
					    </div>
					    
					</div>
					
  				</div>
  							
  			<!-- page specific code end here -->
	                            
        </div>
        
    	<%@include file="/WEB-INF/views/includes/footer.jsp"%>
        
    </body>
    
</html>