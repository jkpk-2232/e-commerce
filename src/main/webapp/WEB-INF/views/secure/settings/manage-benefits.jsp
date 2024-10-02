<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>

<html>
	
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<meta charset="utf-8">
			<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
			
			
			<style type="text/css">
			
				.errorMessage {
					padding-left: 161px;
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
						<h3><%=it.get("labelBenefits").toString() %></h3>
					</div>
				</div>
				<div class="clearfix"></div>
	
				<div class="row">
	
					<div class="col-md-9">
	
						<div class="x_panel">
							<div class="x_content">
								<div class="row">
	
									<div class="cancellationChargesDetailsPage">
	
										<div class="cancellationChargesRightMenu">
	
											<div class="cancellationChargesInfo">
	
												<form id="edit-benefits" name="edit-benefits" action="${pageContext.request.contextPath}/manage-benefits.do" method="post" >
	
													<div class="clearfix"></div>

													<div align="left" id="sucMessage"
														style="width: 300px; float: left; padding-top: 11px; margin-left: 1px;">
														<%=ViewUtils.outputMessage(request, "Success")%>
													</div>
													
													<div class="clearfix"></div>
	
													<div style="margin-top: 20px;">
														<div class="cancellationChargesDiv">
															<label id="cancellationChargesLabel" class=""><%=it.get("labelSenderBenefits").toString() %>  <span style="color: #FF0000;">*</span></label>
															<%=ViewUtils.outputCancellationChargesNumberFieldCell("senderBenefit", "", false, 1, 20, it)%>
														</div>
													</div>
	
													<div class="clearfix"></div>
													
													<div style="margin-top: 20px;">
														<div class="cancellationChargesDiv">
															<label id="cancellationChargesLabel" class=""><%=it.get("labelReceiverBenefits").toString() %>  <span style="color: #FF0000;">*</span></label>
															<%=ViewUtils.outputCancellationChargesNumberFieldCell("receiverBenefit", "", false, 1, 20, it)%>
														</div>
													</div>
	
													<div class="clearfix"></div>
	
													<div class="cancellationChargesButtonDiv">
														<div id="btnCancel" class="rightButtonClass">
															<div class="labelForbtnCancel rightCommonLabelCss"
																style="color: #636363"><%=it.get("labelCancel").toString() %></div>
														</div>
														<div id="btnSave" class="rightButtonClass">
															<div class="labelForbtnSave leftCommonLabelCss"><%=it.get("labelSave").toString() %></div>
														</div>
													</div>
	
													<div class="clearfix"></div>
	
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