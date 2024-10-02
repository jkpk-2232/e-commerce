<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<meta charset="utf-8">
			
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<title><%=it.get("labelLocations").toString() %></title>
	
	</head>
	
	<body class="nav-md">
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
		<!-- page content -->
		<div class="right_col" role="main">
			<div class="">
	
				<div class="page-title">
					<div class="title_left">
						<h3><%=it.get("labelLocations").toString() %></h3>
					</div>
				</div>
				<div class="clearfix"></div>
	
				<div class="row">
	
					<div class="col-md-9">
	
						<div class="x_panel">
							<div class="x_content">
								<div class="row">
	
	<!-- 								<div class="tipDetailsPage"> -->
										<div class="manageAreaPage">
										<div class="tipRightMenu">
	
											<div class="tipInfo">
	
												<form id="adminArea" name="adminArea"
													action="${pageContext.request.contextPath}/manage-admin-area.do"
													method="post">
	
													<div class="clearfix"></div>
	
													<div class="clearfix"></div>
													<div class="clearfix"></div>
	
													<div id="placeContainer1" style="height: 300px; width: 99%;">
	
														<div id="placeContainer2"
															style="height: 100%; width: 120px; float: left; width: 47%;">
	
															<div class="discountDiv" style="margin-top: 20px;">
																<label id="" style="width: 250px;"><%=it.get("labelDisplayName").toString() %><span
																	style="color: #FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("areaDisplayName", "", true, 1,100, it, "")%>
															</div>
	
															<div class="discountDiv" style="margin-top: 6px;">
																<label id="" style="width: 250px;"><%=it.get("labelAreaName").toString() %><span
																	style="color: #FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("areaName", "", true, 1, 100,it, "")%>
															</div>
	
															<div class="clearfix"></div>
	
															<div>
	
																<div class="manageSmsSendingDiv" id="driver2"
																	style="margin-top: 3%;">
																	<span id="driverSms2Span"><%=it.get("labelRadius").toString() %></span>
																	<%=ViewUtils.outputSelectFieldCellWithoutLabel("radius", "",false, 1, it, "languageSelectAdmin")%>
																	<span id="driverSms2Span">km</span>
																</div>
	
															</div>
	
															<div class="tipButtonDiv">
																<div id="btnCancel" class="rightButtonClass">
																	<div class="labelForbtnCancel rightCommonLabelCss"
																		style="color: #636363"><%=it.get("labelCancel").toString() %></div>
																</div>
																<div id="btnSave" class="rightButtonClass">
																	<div class="labelForbtnSave leftCommonLabelCss"><%=it.get("labelAddArea").toString() %></div>
																</div>
															</div>
	
															<div
																style="height: 30px; width: 100%; margin-top: 23%; font-size: 17px;">
																<span style="color: #FF0000;">*</span>	<%=it.get("InfoThisInformationIsAvailableOnWebsite").toString() %>
															</div>
	
														</div>
	
														<div id="placeContainer3"
															style="height: 100%; width: 120px; width: 50%; margin-left: 51%;">
															<div id="fareMap"
																style="height: 100%; width: 100%; overflow: auto;"></div>
														</div>
	
													</div>
	
													<div id="placeContainer4" style="height: 382px; width: 99%;">
	
														<div class="clearfix"></div>
	
														<div class="manageAnnouncementDetailsPageButtonDiv" style=""
															style="margin-top: 7px;">
	
															<div class="searchBookings">
																<%=ViewUtils.searchStringField("searchBooking", it.get("labelSearch").toString(),true, 6, 60, it, request)%>
															</div>
	
															<div class="searchButtonDiv" tabindex="7"
																style="float: left;">
																<button type="button" id="btnSearchAnnouncement"
																	class="buttonClass"><%=it.get("labelSearch").toString() %></button>
															</div>
	
														</div>
	
														<div class="announcementMessageDiv">
															<table border="0" margin="0" padding="0" width="100%"
																class="dataTables_wrapper" id="adminAreaListTable">
																<thead>
																	<tr>
																		<th>Id</th>
																		<th><%=it.get("labelSrNo").toString() %></th>
	 																	<th><%=it.get("labelDisplayName").toString() %></th>
	 																	<th><%=it.get("labelAreaName").toString() %></th>
	 																	<th><%=it.get("labelAreaCountry").toString() %></th>
	 																	<th><%=it.get("labelAreaRadius").toString() %></th>
	 																	<th><%=it.get("labelAction").toString() %></th>	
																	</tr>
																</thead>
																<tbody>
	
																</tbody>
															</table>
														</div>
	
													</div>
	
													<div class="clearfix"></div>
	
													<div class="clearfix"></div>
	
													<%=ViewUtils.outputFormHiddenField("areaPlaceId", it)%>
													<%=ViewUtils.outputFormHiddenField("areaLatitude", it)%>
													<%=ViewUtils.outputFormHiddenField("areaLongitude", it)%>
													<%=ViewUtils.outputFormHiddenField("areaRadius", it)%>
													<%=ViewUtils.outputFormHiddenField("areaCountry", it)%>
<%-- 													<%=ViewUtils.outputFormHiddenField("language", it)%> --%>
												</form>
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
		<!-- /page content -->
	
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>