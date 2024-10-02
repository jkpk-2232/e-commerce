<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.webapp.viewutils.*, com.jeeutils.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<title>Cab Indians</title>
		
	</head>
	
	<body style="background-color: #fff">
	
		<div class="row">
		
			<div style="text-align: center; margin-top: 5%">
				<span class="pull-center" style=""> <%=it.get("message").toString()%> </span>
			</div>
			
		</div>
	
		<div class="row clearfix" style="text-align: center; margin-top: 2%">
		
			<span class="pull-center" style=""> 
				<a id="closeSpan" style="color: #000; font-size: 20px;" href="javascript:close_window();"> <%=it.get("labelClose").toString()%> </a>
			</span>
			
		</div>
		
		${it.jsFile}
		
	</body>
	
</html>