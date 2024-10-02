<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%><!DOCTYPE html>

<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Dashboard</title>
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<script type="text/javascript">
		
			var basePath = '${pageContext.request.contextPath}';	
			
		</script>
		
	</head>
	
	<body>
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
		
		<div class="titleDiv">Dashboard</div>
		
		<div class="clearfix"></div>
		
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>