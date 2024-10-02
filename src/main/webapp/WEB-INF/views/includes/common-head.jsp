<%@page import="com.jeeutils.FrameworkConstants"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.utils.dbsession.DbSession"%>
<meta name="format-detection" content="telephone=no">

${it.cssFile}

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath}';
</script>

<%
		HashMap it = (HashMap) request.getAttribute("it");
		DbSession sessionDBGlobal = DbSession.getSession(request, response, true);
		String languageGlobal = FrameworkConstants.LANGUAGE_ENGLISH;
%>

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it.get("google_map_key").toString()%>&sensor=false&libraries=places"></script>