<!DOCTYPE html>
<%@page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List,java.util.ArrayList"%>

<html>

	<head>
	
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, minimal-ui" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">

		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>

		<title><%=it.get("labelDashboard").toString()%></title>
		
		<style type="text/css">
		
		</style>
	
	</head>
	
	<body>
	
	<div class="mainWrapper">
    
        <div class="mainWrapperInner">
    
            <div class="dashboardWrapper">
    
                <div class="dashboardWrapperInner">
    
                    <div class="container-fluid">

                        <div class="row">
							
							<%@ include file="/WEB-INF/views/includes/header.jsp"%>

                            <div class="dashboardWrapperRightPanel">

                                <!-- Navigation bar Starts from here -->
                                
                                <%@ include file="/WEB-INF/views/includes/navigation.jsp"%>

                                <!-- Navigation bar ends here -->

                                <div class="dashboardRightPanelMainOuter">

                                    <!-- Change of code from here -->

                                    <div class="dashboardWrapperRightPanelMapOuter">
                                        <div class="dashboardWrapperRightPanelMap">
                                            <div class="dashboardWrapperRightPanelMapInner">

                                                <div id="dashboardWrapperRightPanelMapInnerMap"></div>

                                                <div class="dashboardWrapperRightPanelMapInnerCustomOverlay" style="display:none">
                                                    <div class="dashboardWrapperRightPanelOverlayMain">
                                                        <div class="dashboardWrapperRightPanelOverlayMainInner">
                                                            <div class="dashboardWrapperRightPanelOverlayMainInnerContent container-fluid">

                                                                <div class="dashboardOverlayMain">


                                                                    <div class="dashboardOverlayMainClose">
                                                                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                                                    </div>

                                                                    <div class="dashboardOverlayMainTitle">
                                                                        <h3>New Booking</h3>
                                                                    </div>



                                                                    <div class="col-md-6">
                                                                        <div class="dashboardOverlayBookingDetails">
                                                                        <div class="dashboardOverlayBookingDetailsInner">
                                                                            <div class="dashboardOverlayBookingLocation">

                                                                                <div class="dashboardDottedLines">
                                                                                    <div class="dashboarddot dashboardGerydot">

                                                                                    </div>
                                                                                    <div class="dashboarddot dashboardGreendot">

                                                                                    </div>
                                                                                </div>

                                                                                <div class="dashboardOverlayBookindDetailsPickAndDrop">
                                                                                    <div class="dashboardOverlayBookindDetailsPick">
                                                                                        *Pickup location
                                                                                        <input type="text" class="form-control">
                                                                                    </div>

                                                                                    <div class="dashboardOverlayBookindDetailsDrop">
                                                                                        *Drop location
                                                                                        <input type="text" class="form-control">
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                            <div class="dashboardSeperator"></div>
                                                                            <div class="dashboardOverlayBookingCarAndDate">
                                                                                <div class="row">
                                                                                    <div class="col-lg-6">
                                                                                        *Cars type
                                                                                        <select class="form-control">
                                                                                            <option>1</option>
                                                                                            <option>2</option>
                                                                                            <option>3</option>
                                                                                            <option>4</option>
                                                                                        </select>
                                                                                    </div>
                                                                                    <!-- /.col-lg-6 -->
                                                                                    <div class="col-lg-6">
                                                                                        *Date and Time
                                                                                        <input type="date" class="form-control">
                                                                                    </div>
                                                                                    <!-- /.col-lg-6 -->
                                                                                </div>
                                                                                <!-- /.row -->
                                                                            </div>
                                                                            <div class="dashboardSeperator"></div>

                                                                            <div class="dashboardOverlayBookingLuggage">
                                                                                Add luggage (optional)
                                                                                <div class="input-group">
                                                                                    <button class="btn btn-default dashboardBookingLuggageMinus" type="button">-</button>
                                                                                    <input type="text" class="form-control dashboardBookingLuggageValue" disabled value="0">

                                                                                    <button class="btn btn-default dashboardBookingLuggagePlus" type="button">+</button>

                                                                                </div>
                                                                                <!-- /input-group -->
                                                                            </div>



                                                                            <div class="dashboardSeperator"></div>


                                                                            <div class="dashboardPassengerDetail">
																				 <div class="dashboardPassengerDetailTitle">
																					 Passange Detail
																				</div>
																				
																				<div class="dashboardPassengerDetailContent">
																					 *Full Name
                                                                                        <input type="text" class="form-control">
																				
																				  <div class="dashboardSeperator"></div>
																				
																				<div class="row">
																				  <div class="col-lg-6">
																					<div class="input-group">
																					  *Phone
																					  <input type="number" class="form-control" aria-label="...">
																					</div><!-- /input-group -->
																				  </div><!-- /.col-lg-6 -->
																				  <div class="col-lg-6">
																					<div class="input-group">
																					  Email
																					  <input type="email" class="form-control" aria-label="...">
																					</div><!-- /input-group -->
																				  </div><!-- /.col-lg-6 -->
																				</div><!-- /.row -->
																					<div class="dashboardSeperator"></div>
																						 Add Note
                                                                                        <input type="text" class="form-control">
																						<div class="dashboardSeperator"></div>
																					
																					<div class="dashboardCreatePersonalAccount">
																							<input type="checkbox" class="dashboardCreatePersonalAccountFlag">
																						Create personal account for this passenger
																					</div>
																					
																				</div>
																						</div>
																					
																			<div class="dashboardSeperator"></div>
																			</div>
																				<div class="dashboardOverlaySubmit">
																					<div class="inner-addon right-addon">
																						<i class="glyphicon glyphicon-chevron-right"></i>
																						
																						
																						<input type="button" value="Select Driver" class="form-control dashboardBookingSelectDriver" />
																					</div>
																					
																					
																					<div class="dashboardSeperator"></div>
																					
																					<input type="button" value="Cancel" class="btn btn-default dashboardBookingCancel" />
																					<input type="button" value="Submit & Request Driver" class="form-control dashboardBookingSubmit" />
																					<div class="dashboardSeperator"></div>
																					<div class="dashboardSeperator"></div>
																					<div class="dashboardSeperator"></div>
																					
																		
                                                                            </div>


                                                                        </div>


                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <div class="dashboardSearchDriverMain">
																			<div class="dashboardSearchDriverOuter">
																				<div class="dashboardSearchDriverInner">
																					<h4> Select Driver </h4>
																
																					<div class="inner-addon right-addon">
																						<i class="glyphicon glyphicon-search"></i>
																						
																						
																						<input type="text" placeholder="Search by name, driver id or near by location" class="form-control dashboardSearchDriver" />
																					</div>
																					
																					<div class="dashboardSeperator"></div>
																					
																					
																					<div class="dashboardSearchDriverResultBox">
																						
																						<ul class="dashboardSearchDriverResultBoxUL">
																							
																							
																							
																							
																							
																							<li> 
																								<div class="dashboardSearchDriverResultBoxDetail">
																									<div class="row">
																										<div class="col-sm-2">
																											<div class="searchDriverPicture">

																											</div>
																										</div>
																										
																										<div class="col-sm-6">
																											<div class="searchDriverDesc">
																												<div class="searchDriverDescInner">
																													<div class="searchDriverDescName">
																														Emily George (ID 536)
																													</div>
																													<div class="searchDriverDescTaxiNumber">
																														TX PL8S - Sedan
																													</div>
																												</div>
																												<div class="seacrhDriverDistance">
																													1.4 km	
																												</div>
																											</div>
																											
																										</div>
																										
																										
																										<div class="col-sm-4 selectThisDriverOuter">
																											<input type="button" class="btn btn-danger selectThisDriver" value="Assign">
																										</div>
																									</div>
																						
																								</div>
																							</li>
																							
																							
																							
																							
																							
																							
																																
																							<li> 
																								<div class="dashboardSearchDriverResultBoxDetail">
																									<div class="row">
																										<div class="col-sm-2">
																											<div class="searchDriverPicture">

																											</div>
																										</div>
																										
																										<div class="col-sm-6">
																											<div class="searchDriverDesc">
																												<div class="searchDriverDescInner">
																													<div class="searchDriverDescName">
																														Emily George (ID 536)
																													</div>
																													<div class="searchDriverDescTaxiNumber">
																														TX PL8S - Sedan
																													</div>
																												</div>
																												<div class="seacrhDriverDistance">
																													1.4 km	
																												</div>
																											</div>
																											
																										</div>
																										
																										
																										<div class="col-sm-4 selectThisDriverOuter">
																											<input type="button" class="btn btn-danger selectThisDriver" value="Assign">
																										</div>
																									</div>
																						
																								</div>
																							</li>
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																																
																							<li> 
																								<div class="dashboardSearchDriverResultBoxDetail">
																									<div class="row">
																										<div class="col-sm-2">
																											<div class="searchDriverPicture">

																											</div>
																										</div>
																										
																										<div class="col-sm-6">
																											<div class="searchDriverDesc">
																												<div class="searchDriverDescInner">
																													<div class="searchDriverDescName">
																														Emily George (ID 536)
																													</div>
																													<div class="searchDriverDescTaxiNumber">
																														TX PL8S - Sedan
																													</div>
																												</div>
																												<div class="seacrhDriverDistance">
																													1.4 km	
																												</div>
																											</div>
																											
																										</div>
																										
																										
																										<div class="col-sm-4 selectThisDriverOuter">
																											<input type="button" class="btn btn-danger selectThisDriver" value="Assign">
																										</div>
																									</div>
																						
																								</div>
																							</li>
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																																
																							<li> 
																								<div class="dashboardSearchDriverResultBoxDetail">
																									<div class="row">
																										<div class="col-sm-2">
																											<div class="searchDriverPicture">

																											</div>
																										</div>
																										
																										<div class="col-sm-6">
																											<div class="searchDriverDesc">
																												<div class="searchDriverDescInner">
																													<div class="searchDriverDescName">
																														Emily George (ID 536)
																													</div>
																													<div class="searchDriverDescTaxiNumber">
																														TX PL8S - Sedan
																													</div>
																												</div>
																												<div class="seacrhDriverDistance">
																													1.4 km	
																												</div>
																											</div>
																											
																										</div>
																										
																										
																										<div class="col-sm-4 selectThisDriverOuter">
																											<input type="button" class="btn btn-danger selectThisDriver" value="Assign">
																										</div>
																									</div>
																						
																								</div>
																							</li>
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							
																																
																							<li> 
																								<div class="dashboardSearchDriverResultBoxDetail">
																									<div class="row">
																										<div class="col-sm-2">
																											<div class="searchDriverPicture">

																											</div>
																										</div>
																										
																										<div class="col-sm-6">
																											<div class="searchDriverDesc">
																												<div class="searchDriverDescInner">
																													<div class="searchDriverDescName">
																														Emily George (ID 536)
																													</div>
																													<div class="searchDriverDescTaxiNumber">
																														TX PL8S - Sedan
																													</div>
																												</div>
																												<div class="seacrhDriverDistance">
																													1.4 km	
																												</div>
																											</div>
																											
																										</div>
																										
																										
																										<div class="col-sm-4 selectThisDriverOuter">
																											<input type="button" class="btn btn-danger selectThisDriver" value="Assign">
																										</div>
																									</div>
																						
																								</div>
																							</li>
																							
																							
																							
																							
																							
																						</ul>
																						
																					</div>
																				</div>
																			</div>
																			
																		</div>
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>




                                <!-- Change of code till here -->


                            </div>

                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    </div>
	
	
	
	
	</body>
	
	${it.jsFile}
	
</html>