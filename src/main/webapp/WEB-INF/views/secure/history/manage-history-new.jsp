<!DOCTYPE html>

<%@page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.utils.view.ButtonUtils"%>
<%@page import="com.utils.view.ActionButton"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="com.webapp.ProjectConstants.UserRoles"%>

<html>

    <head>
    
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, minimal-ui" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
    
        <%@include file="/WEB-INF/views/includes/common-head.jsp"%>
        
        <title>Manage New Orders</title>
    
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
					        		<i class="icon icon-fire"></i>
					        		Manage New Orders
					        	</h2>
					        	
					        </div>
					        
					        <div class="taxiPageSepcificClass">
					        
	                    		<div class="control-group">
				                	<div class="controls">
					                   	<div id="reportrange" class="pull-left">
			               				   	<span></span> <b class="caret"></b>
		            				   	</div>
			                   	   	</div>
		                       	</div>
		                       	
		                       	<% 
									if (role.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || role.equalsIgnoreCase(UserRoles.ADMIN_ROLE))	{
								%>
										<div class="control-group">
				                        	<%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("vendorId", null, true, 1, it, "margin-top: 10px;")%>
				                        
				                        	<%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("serviceId", null, true, 1, it, "margin-top: 10px;margin-left: -15%;")%>
				                    	
<%-- 				                    		<%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("categoryId", null, true, 1, it, "margin-top: 10px;margin-left: -15%;")%> --%>

											<%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("orderStatusFilter", null, true, 1, it, "margin-top: 10px;margin-left: -15%;")%>
			                    		</div>
			                        	
			                        	<div class="dashboardSeperator"></div>
										<div class="dashboardSeperator"></div>
										<div class="dashboardSeperator"></div>
		                        <%
		                        	} else {
		                    	%>
		                    		<div class="control-group">
			                    		<%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("orderStatusFilter", null, true, 1, it, "margin-top: 10px;")%>
		                        		<div class="dashboardSeperator"></div>
		                        		<div class="dashboardSeperator"></div>
		                        	</div>	
		                    	<% 
		                        	}
		                    	%>
								
					        	<div class="dashboardSeperator"></div>
					        	<div class="dashboardSeperator"></div>
					        	
					        	<div class="table-responsive">
					        	
									<table id="passangerListTable" border="0" margin="0" padding="0" width="100%" class="taxiCustomCssDatatableClass table table-bordered table-hover">
										<thead>
											<tr>
												<th>Id</th>
												<th>Sr. No.</th>
												<th>Order Id</th>
												<th>Order Time</th>
												<th>Customer Name</th>
												<th>Customer Address</th>
												<th>Vendor Name</th>
												<th>Total</th>
												<th>Delivery Charges</th>
												<th>Charges</th>
												<th>No Of Items</th>
												<th>Status</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									
								</div>
					        	
					        </div>
					        
					        <div class="dashboardSeperator"></div>

							<%=ViewUtils.outputFormHiddenField(ProjectConstants.STATUS_TYPE, it) %>

							<%=ViewUtils.outputFormHiddenField("startDateStringHidden", it) %>
					        <%=ViewUtils.outputFormHiddenField("dateFormatHidden", it) %>
					        <%=ViewUtils.outputFormHiddenField("startDateWithoutSeparatorHidden", it) %>
					        <%=ViewUtils.outputFormHiddenField("dateFormatWithoutSeparatorHidden", it) %>

					    </div>
					    
					</div>
					
  				</div>
  							
  			<!-- page specific code end here -->
	                            
        </div>
        
    	<%@include file="/WEB-INF/views/includes/footer.jsp"%>
        
    </body>
    
</html>