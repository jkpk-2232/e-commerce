<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<meta charset="utf-8">
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<title><%=it.get("labelDriverPayablePercentage").toString()%></title>
		
	</head>
	
	<body class="nav-md">
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
		<!-- page content -->
		<div class="right_col" role="main">
			<div class="">
	
				<div class="page-title">
					<div class="title_left">
						<h3><%=it.get("labelDriverPayablePercentage").toString() %></h3>
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
	
												<form id="edit-admin-tip" name="edit-admin-tip"
													action="${pageContext.request.contextPath}/payable-percentage.do"
													method="post">
	
													<div class="clearfix"></div>
	
													<div class="clearfix"></div>
													<div align="left" id="sucMessage"
														style="width: 300px; float: left; padding-top: 11px; margin-left: 1px;">
														<%=ViewUtils.outputMessage(request, "Success")%>
													</div>
													<div class="clearfix"></div>
	
													<div style="margin-top: 30px;">
														<div class="tipDiv">
															<label id="tipLabel" class=""><%=it.get("labelPercentage").toString() %> (%)<span
																style="color: #FF0000;">*</span></label>
															<%=ViewUtils.outputPayablePercentageNumberFieldCell( "percentage", "", false, 1, 20, it)%>
														</div>
													</div>
													<div class="clearfix"></div>
	
													<div class="tipButtonDiv">
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