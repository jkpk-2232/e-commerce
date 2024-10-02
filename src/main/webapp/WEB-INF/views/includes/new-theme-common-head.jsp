<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.actions.BusinessAction"%>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no">
<meta name="description" content="">
<meta name="author" content="">
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	HashMap<String, String> it = (HashMap) request.getAttribute("it");
%>

${it.cssFile}

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath}';
</script>