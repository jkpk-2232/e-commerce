<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.webapp.viewutils.*, com.jeeutils.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAkijiCe1MM9jR-krNgr5cLH3KNqiQtfE0&sensor=false&libraries=places">
		</script>
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>	<title><%=it.get("labelTrackMyLocation").toString()%></title>
	
	</head>
	
	<body>
	
		<div class="wrapper">
		
	            <div class="top-bar clearfix">
	            
	                <div class="col-md-4 col-sm-3 col-xs-4 logocon">
	                    <div class="navbar-header" style="background-color: #fff">
	                        <a class="logo" href="/" title="Home">
	                            <img style="height: 65px; width: 200px; margin-left: 10%" src="${pageContext.request.contextPath}/assets/image/header_login_logo.png" alt="CAREEM" class="img-responsive logo-img">
	                        </a>
	                    </div>
	                </div>
	                
	                <div class="col-md-8 col-sm-9 col-xs-8 text-right pull-right brand-text" style="padding-top: 5px">
	                        <p class="call-text"> <strong><%=it.get("labelContactNumber").toString()%>:</strong> <%=it.get("contactNumber").toString()%>  </p>
	                        <p class="call-text"> <strong><%=it.get("labelSupportEmail").toString()%>:</strong> <%=it.get("supportEmail").toString()%> </p>
	                </div>
	                
	            </div>
	            
	            <section id="block-careem-signup-signup" class="block block-careem-signup clearfix">
	            
	            <div id="errorMessageDiv">
	            	<span id="errorMessage"> <%=it.get("errorMessage").toString()%>  </span>
	            </div>
	            
	            <div id="trackingDetailsDiv">
	            
	                <div class="topDiv clearfix">
	                
	                    <div class="col-md-1">
	                    	<div class="profilephoto">
	                    			<img src="${pageContext.request.contextPath}/assets/image/icon_admin.png" class="img-responsive" style="height: 70px; width: 70px;" />
	                    	</div>
	                    </div>
	                    
	                    <div class="col-md-2 bor-right">
	                        <h2 class="title"><%=it.get("labelDriver").toString()%></h2>
	                        <a class="" href="#">
	                            <span class="pernm"><%=it.get("driverName").toString()%></span>
	                        </a>
	                        <a href="#">
	                            <span class="percall"><%=it.get("driverPhoneNo").toString()%></span>
	                        </a>
	                        <a href="#">
	                            <span class="peremail"><%=it.get("driverEmail").toString()%></span>
	                        </a>
	                        <a href="#">
	                            <span class="rating"> <img src="${pageContext.request.contextPath}/assets/emergency/images/star_icon.png"/></span>
	                        </a>
	                    </div>
	                    
	                    <div class="col-md-2 bor-right secDiv">
	                        <h2 class="title"><%=it.get("labelPassenger").toString()%></h2>
	                        <a class="" href="#">
	                            <span class="pernm"><%=it.get("passengerName").toString()%></span>
	                        </a>
	                        <a href="#">
	                            <span class="percall"><%=it.get("passengerPhoneNo").toString()%></span>
	                        </a>
	                        <a href="#">
	                            <span class="peremail"><%=it.get("passengerEmail").toString()%></span>
	                        </a>
	                    </div>
	                    
	                    <div class="col-md-2 bor-right lastDiv">
	                        <h2 class="title"><%=it.get("labelCarDetails").toString()%></h2>
	                        
	                        <div class="col-sm-12 col-md-12">
		                        <div class="col-md-6"><%=it.get("labelCarType").toString()%> - </div>
		                        <div class="col-md-6" style="color: #a2a2a2;"><%=it.get("carType").toString()%></div>
	                        </div>
	                        
	                        <div class="col-sm-12 col-md-12">
		                        <div class="col-md-6"><%=it.get("labelModel").toString()%> - </div>
		                        <div class="col-md-6" style="color: #a2a2a2;"><%=it.get("carModel").toString()%></div>
	                        </div>
	                        
	                        <div class="col-sm-12 col-md-12">
		                        <div class="col-md-6"><%=it.get("labelColor").toString()%> - </div>
		                        <div class="col-md-6" style="color: #a2a2a2;"><%=it.get("carColor").toString()%></div>
	                        </div>
	                        
	                        <div class="col-sm-12 col-md-12">
		                        <div class="col-md-6"><%=it.get("labelPlateNumber").toString()%> - </div>
		                        <div class="col-md-6" style="color: #a2a2a2;"> <%=it.get("plateNumber").toString()%> </div>
	                        </div>
	                        
	                    </div>
	                    
	                    <div class="col-md-5">
	                        <h2 class="title"><%=it.get("labelTourDetails").toString()%></h2>
	                        <div class="stepwizard">
	                            <div class="stepwizard-row">
	                                <div class="stepwizard-step">
	                                    <span class=" btn-square list1"></span>
	                                    <p><%=it.get("sourceAddress").toString()%></p>
	                                </div>
	                            </div>
	                            <div class="stepwizard-row">   
	                                <div class="stepwizard-step">
	                                    <span class=" btn-square list2"></span>
	                                    <p><%=it.get("destinationAddress").toString()%></p>
	                                </div>
	                            </div>
	                        </div>
	                        <div class="status"><%=it.get("labelStatus").toString()%> - <span class="greentext"><%=it.get("tourStatus").toString()%></span></div>
	                    </div>
	                    
	                </div>
	                
	                <div class="mapcontainer">
							<div id="mapCanvas" style="height:500px;width:100%"> </div>
	                </div>
	                
	            </div>
	
	                <div class="downcont clearfix">
	                    <div class="downloadinner text-center container-fluid">
	                        <div class="pull-left downloadtext" style="margin-right: 15px;">
	                            <p><%=it.get("emergencyDownloadMsg1").toString()%><br/><%=it.get("emergencyDownloadMsg2").toString()%></p>
	                            <span><%=it.get("emergencyDownloadMsg3").toString()%></span>
	                        </div>
	                        <div class="col-md-3  col-sm-3  col-xs-6 pull-right"><a href="#"><img src="${pageContext.request.contextPath}/assets/emergency/images/play_store.png" class="img-responsive"></a></div>
	                        <div class="col-md-3  col-sm-3  col-xs-6 pull-right"><a href="#"><img src="${pageContext.request.contextPath}/assets/emergency/images/app_store.png" class="img-responsive"></a></div>
	                    </div>
	                </div>
	                
	            </section>
	            
	            <footer class="clearfix">
	                <div class="container">
	                    <a href="#" class="col-xs-12 col-md-6 col-sm-6 copyright"><%=it.get("emergencyFooterRightsReservedMsg").toString()%></a>
	                    <div class="col-md-6 col-sm-6 col-xs-12 sociallinks">
	                        <ul>
	                           <!--  <li><a href="#" class="">Terms of Service</a></li>
	                            <li> <a href="#" class="">Privacy Policy</a></li> -->
	                            <li><a href="#" class=""><img src="${pageContext.request.contextPath}/assets/emergency/images/footer_fb_icon.png"/></a></li>
	                            <li><a href="#" class=""><img src="${pageContext.request.contextPath}/assets/emergency/images/footer_twitter_icon.png"/></a></li>
	                            <li><a href="#" class=""><img src="${pageContext.request.contextPath}/assets/emergency/images/footer_googleplus_icon.png"/></a></li>
	                        </ul>
	                    </div>
	                </div>
	            </footer>
	            
	        </div>
	        
	        <%=ViewUtils.outputFormHiddenField("trackLocationStatus", it) %>
	        <%=ViewUtils.outputFormHiddenField("passengerId", it) %>
	        <%=ViewUtils.outputFormHiddenField("currentLat", it) %>
	        <%=ViewUtils.outputFormHiddenField("currentLng", it) %>
	        
	        <%=ViewUtils.outputFormHiddenField("sourseLat", it) %>
	        <%=ViewUtils.outputFormHiddenField("sourseLng", it) %>
	        <%=ViewUtils.outputFormHiddenField("destLat", it) %>
	        <%=ViewUtils.outputFormHiddenField("destLng", it) %>
	        
	        <%=ViewUtils.outputFormHiddenField("driverId", it) %>
	        <%=ViewUtils.outputFormHiddenField("driverLat", it) %>
	        <%=ViewUtils.outputFormHiddenField("driverLng", it) %>
	        
		
		${it.jsFile}
		
	</body>
	
</html>