<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>

<!DOCTYPE html>

	<html>
		
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<meta charset="utf-8">
			<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
			
			<title>Multi City Settings</title>
			
			<style type="text/css">
			
				.errorMessage {
					padding-left: 204px;
				}
				
				.cancellationChargesDetailsPage .cancellationChargesInfo label {
					width: 194px;
				}
				
				.cancellationChargesDetailsPage .cancellationChargesDiv {
					width: 600px;
				}
				
				.cancellationChargesButtonDiv {
					left: 205px;
				}
			
			</style>
			
		</head>
		
		<body class="nav-md">
		
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
		<!-- page content -->
		<div class="right_col" role="main">
		
			<div class="">
	
				<div class="page-title">
					<div class="title_left">
						<h3>Multi City Settings</h3>
					</div>
				</div>
				<div class="clearfix"></div>
	
				<div class="row">
	
					<div class="col-md-9">
	
						<div class="x_panel">
						
							<div class="x_content">
							
								<div class="row">

									<div class="manageAnnouncementDetailsPageButtonDiv" style="margin-top: 10px;">
														
										<div class="searchBookings">			
											<%=ViewUtils.searchStringField("searchBooking", "Search", true, 6, 60, it, request)%>
										</div>
										
										<div class="searchButtonDiv" tabindex="7" style="float: left;">
											 <button type="button" id="btnSearchAnnouncement" class="buttonClass">Search</button>
										</div>
										<div class="clearfix"></div>
									</div>
										
									<div class="announcementMessageDiv">
										<table border="0" margin="0" padding="0" width="100%" class="dataTables_wrapper" id="adminAreaListTable" style="width:100%;">
											<thead>
												<tr>
													<th>Id</th>
													<th>Sr. No.</th>
													<th>Country Name</th>
													<th>Country Short Name</th>
													<th>Payment Gateway</th>
													<th>Currency Symbol</th>
													<th>Status</th>	
													<th>Action</th>	
												</tr>
											</thead>
											<tbody>
								
											</tbody>
										</table>
									</div>	

								</div>
							</div>
	
						</div>
	
					</div>
	
					<%@ include file="/WEB-INF/views/includes/settings-menu.jsp"%>
				</div>
			</div>
		</div>
		<!-- /page content -->
	
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
	</body>
</html> --%>