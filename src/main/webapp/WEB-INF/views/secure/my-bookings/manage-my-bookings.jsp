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
        <title><%=it.get("labelMyBookings").toString()%></title>
    
        <style type="text/css">
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
					        		<i class="glyphicon glyphicon-home"></i>
					        		<%=it.get("labelMyBookings").toString()%>
					        	</h2>
					        </div>
					        
					        <div class="taxiPageSepcificClass">
					        
					        	<!-- Date Content -->
						
								<div class="control-group">
				                	<div class="controls">
					                   	<div id="reportrange" class="pull-left">
			               				   	<span></span> <b class="caret"></b>
		            				   	</div>
			                   	   	</div>
		                       	</div>
			               		
								<div class="control-group">
				                	<div class="controls">
		                       			<input id="btnExport" type="button" value="<%=it.get("labelExport").toString() %>" class="btn btn-danger" />
									</div>
								</div>
								
								<!-- End Date Content -->
								
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
								<div class="dashboardSeperator"></div>
					        	
					        	<div class="table-responsive">
					        	
									<table id="adminBookingsListTable" border="0" margin="0" padding="0" width="100%" class="taxiCustomCssDatatableClass table table-bordered table-hover">
									
										<thead>
											<tr>
												<th><%=it.get("labelTripId").toString()%></th>
												<th><%=it.get("labelSrNo").toString()%></th>
												<th><%=it.get("labelTourId").toString()%></th>
												<th><%=it.get("labelPassengerName").toString()%></th>
												<th><%=it.get("labelPickUpTime").toString()%></th>
												<th><%=it.get("labelPickUpLocation").toString()%></th>
												<th><%=it.get("labelDropLocation").toString()%></th>
												<th><%=it.get("labelStatus").toString()%></th>				
												<th>Action</th>	
											</tr>
										</thead>
										
										<tbody>
										
										</tbody>
										
									</table>
									
								</div>
					        	
					        </div>
					        
					        <div class="dashboardSeperator"></div>
					        
					        <%=ViewUtils.outputFormHiddenField("userId", it) %>
					        
					    </div>
					    
					</div>
					
  				</div>
  							
  			<!-- page specific code end here -->
	                            
        </div>
        
    	<%@include file="/WEB-INF/views/includes/footer.jsp"%>
        
    </body>
    
</html>