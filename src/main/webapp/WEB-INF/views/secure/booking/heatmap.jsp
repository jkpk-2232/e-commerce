<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.webapp.viewutils.*, com.jeeutils.*"%>
<%@page import="java.util.HashMap"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		
		${it.cssFile}
		
		<script type="text/javascript">
			var basePath = '${pageContext.request.contextPath}';
		</script>

		<%
			HashMap it1 = (HashMap) request.getAttribute("it");
		%>

		<title>HeatMap</title>
			
		<style type="text/css">
			
			#mapCanvas {
			    height: auto;
			    min-height: 800px;
			} 
			
			#tooggleHeatMap {
			    cursor: pointer;
			} 
				
		 </style>
	
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=it1.get("google_map_key").toString()%>&libraries=visualization">  </script>
		
	</head>
	
	<body>
	
		<div class="wrapper">
		
			<div class="top-bar clearfix" style="margin-bottom: 1%; margin-top: 1%;">
                
                <div class="col-md-6">
                	 <span id="tooggleHeatMap">Toggle HeatMap</span>
                </div>
                
                <div class="col-md-6">
                	 <%=NewUiViewUtils.outputSelectFieldCellWithlabelAndSearchFieldForFilters("timeFilter", null, true, 1, it1, "width: 50%;")%>
                </div>
                
	        </div>
	    	
	        <section id="block-careem-signup-signup" class="block block-careem-signup clearfix">
	        	<div id="mapCanvas"></div>
	         </section>
	        
		</div>
		
		<%@include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>