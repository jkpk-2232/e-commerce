<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<meta charset="utf-8">
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<title><%=it.get("labelAdminFAQ").toString() %></title>
	
	</head>
	
	<body class="nav-md">
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
		<!-- page content -->
		<div class="right_col" role="main">
			<div class="">
	
				<div class="page-title">
					<div class="title_left">
						<h3><%=it.get("labelAdminFAQ").toString() %></h3>
					</div>
				</div>
				<div class="clearfix"></div>
	
				<div class="row">
	
					<div class="col-md-9">
	
						<div class="x_panel">
							<div class="x_content">
								<div class="row">
									<div class="tipDetailsPage">
												
												<div class="tipRightMenu">
												
													<div class="tipInfo">	
														
														<form id="adminFaq" name="adminFaq" action="${pageContext.request.contextPath}/manage-admin-faq.do" method="post" >
														
															<div class="clearfix"></div>
															
															<div id="placeContainer1" style="height:315px;width: 99%;">
																	
																	<div id="placeContainer2" style="height: 100%;float:left; width:96%;">
																	
																		<div class="discountDiv" style="margin-top: 20px;">
																			<label id="" style="width:115px;"><%=it.get("labelQuestion").toString() %><span style="color:#FF0000;">*</span></label>
																			<%=ViewUtils.outputTextAreaFieldCellCustom("question", "", true, 1, 100, it, 10,10)%>
																		</div>
																	
																		<div class="clearfix"></div>
																			
																		<div class="discountDiv" style="margin-top: 20px;">
																			<label id="" style="width:115px;"><%=it.get("labelAnswer").toString() %><span style="color:#FF0000;">*</span></label>
																			<%=ViewUtils.outputTextAreaFieldCellCustom("answer", "", true, 2, 100, it, 10,10)%>
																		</div>
																		
																		<div class="tipButtonDiv">
																			<div id="btnCancel" class="rightButtonClass">
																				<div class="labelForbtnCancel rightCommonLabelCss" style="color:#636363"><%=it.get("labelCancel").toString() %></div>
																			</div>
																			<div id="btnSave" class="rightButtonClass">
																				<div class="labelForbtnSave leftCommonLabelCss"><%=it.get("labelAddFaq").toString() %></div>
																			</div>
																		</div>
																	
																		<div style="height:30px;width:100%;font-size: 17px; margin-top: 14%;">
																			<span style="color:#FF0000;">*</span><%=it.get("InfoThisInformationIsAvailableOnWebsite").toString() %>										
																		</div>
																	
																	</div>
																						
															</div>
															
															<div id="placeContainer4" style="height:382px;width: 99%;">
																	
																<div class="clearfix"></div>
																
																<div class="manageAnnouncementDetailsPageButtonDiv" style="margin-top: 7px;">
																
																	<div class="searchBookings">			
																		<%=ViewUtils.searchStringField("searchBooking",it.get("labelSearch").toString(), true, 6, 60, it, request)%>
																	</div>
																	
																	<div class="searchButtonDiv" tabindex="7" style="float: left;">
																		 <button type="button" id="btnSearchAnnouncement" class="buttonClass"><%=it.get("labelSearch").toString() %></button>
																	</div>
																	
																</div>
																	
																<div class="announcementMessageDiv">
																	<table border="0" margin="0" padding="0" width="100%" class="dataTables_wrapper" id="adminFaqListTable">
																		<thead>
																			<tr>
																				<th>Id</th>
																				<th><%=it.get("labelSrNo").toString() %></th>
	 																			<th><%=it.get("labelQuestion").toString() %></th>
	 																			<th><%=it.get("labelAnswer").toString() %></th>
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
																				
														</form>
														
														<div class="updatePassengerCreditsDialog">
											
															<form id="editAdminFaq" name="editAdminFaq" action="${pageContext.request.contextPath}/manage-admin-faq/edit.do" method="post" >
															
																<div class="passwordDetails">
														
																	<div class="discountDiv" style="margin-top: 20px;">
																		<label id="" style="width:115px;"><%=it.get("labelQuestion").toString() %><span style="color:#FF0000;">*</span></label>
																		<%=ViewUtils.outputTextAreaFieldCellCustom("questionEdit", "", true, 1, 100, it, 10,10)%>
																		<div id="questionEditError" class="errorMessage" style="margin-left: 61px;"></div>
																	</div>
																	
																	<div class="clearfix"></div>
																			
																	<div class="discountDiv" style="margin-top: 20px;">
																		<label id="" style="width:115px;"><%=it.get("labelAnswer").toString() %><span style="color:#FF0000;">*</span></label>
																		<%=ViewUtils.outputTextAreaFieldCellCustom("answerEdit", "", true, 2, 100, it, 10,10)%>
																		<div id="answerEditError" class="errorMessage" style="margin-left: 61px;"></div>
																	</div>
																	
																	<%=ViewUtils.outputFormHiddenField("adminFaqId", it) %>
<%-- 																	<%=ViewUtils.outputFormHiddenField("language", it) %> --%>
																	<%=ViewUtils.outputFormHiddenField("labelOK", it) %>
																</div>
														        <div style="margin-top:10px; margin-left:240px" id="ajxBook"><img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif"> </div>
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
		</div>
		<!-- /page content -->
	
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>