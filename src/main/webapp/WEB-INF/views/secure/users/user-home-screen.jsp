<!DOCTYPE html>
<%@page import="com.webapp.viewutils.ViewUtils"%>
<html>

	<head>
	
		<title>Home</title>
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<script type="text/javascript">
		
			var hasErrorsAlert = ${pageContext.request.contextPath};
			
			$(document).ready(function() {
		
				alert(hasErrorsAlert);
			});
		
		</script>
		
	</head>
	
	<body>
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
		
		<%= ViewUtils.outputFormHeading(request, "Home", "") %>
		
		<a href="${it.contextPath}/secure/users/admin/list-admins.do">Manage Admin</a><br>
		<a href="${it.contextPath}/secure/accesses/role-management.do">Role Management</a><br>
		<a href="${it.contextPath}/secure/accesses/role-access-management.do?roleId=1">Role Access Management</a><br>
		<a href="${it.contextPath}/secure/log/activity-log.do">Activity Logs</a><br>
		
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>

</html>