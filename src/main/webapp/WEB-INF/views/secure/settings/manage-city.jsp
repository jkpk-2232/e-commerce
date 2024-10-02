<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>City</title>
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<style type="text/css">
		
			#sucMessage label {
				width:300px;
			}
			
		</style>

	</head>
	
	<body>
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>

		<div class="tipDetailsPage">
			
			<div class="tipRightMenu">
			
				<div class="tipInfo">	
					
					<form id="addCity" name="addCity" action="${pageContext.request.contextPath}/manage-city.do" method="post" >
					
						<div class="clearfix"></div>
						
						<div class="pageTitleDiv">
							<h3>Add City</h3>
							<div class="pageTitleImgDiv" style="width: 700px;top: 10px;"></div>
						</div>
						
						<div class="clearfix"></div>
			
						
						<div align="left" id="sucMessage" style="width: 300px; float: left;padding-top: 11px;margin-left: 1px;">
							<%=ViewUtils.outputMessage(request, "Success")%>
						</div>
						<div class="clearfix"></div>
						<div style="margin-top:30px; margin-bottom:20px">
								<label id="stateLabel" class="">State <span style="color:#FF0000;">*</span></label>
								<%=ViewUtils.outputSelectFieldCellWithoutLabel("state", "", true, 28, it, "")%>
						</div>
								
						<div class="fieldDiv">
							
								<label id="tipLabel" class="">City <span style="color:#FF0000;">*</span></label>
								<%=ViewUtils.outputTextField("cityName", "", true, 1, 20, it, "")%>
							
						</div>
						
						<div class="clearfix"></div>
						
						<div class="tipButtonDiv">
							<div id="btnCancel" class="rightButtonClass">
								<div class="labelForbtnCancel rightCommonLabelCss" style="color:#636363">Cancel</div>
							</div>
							<div id="btnSave" class="rightButtonClass">
								<div class="labelForbtnSave leftCommonLabelCss">Save</div>
							</div>
						</div>
						<div class="clearfix"></div>
					</form>
				</div>
			</div>
		</div>
		
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>