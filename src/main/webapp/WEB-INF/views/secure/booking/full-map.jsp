<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.webapp.viewutils.*, com.jeeutils.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		
		<title><%=it.get("labelFullMap").toString()%></title>
			
		<style type="text/css">
			
			#mapCanvas {
			    height: auto;
			    min-height: 800px;
			} 
			
			#closeSpan:hover, #showAllSpan:hover {
	 		    color: #3f4ca0 !important; 
			    cursor: pointer;
			}
			
			#showAllSpan, #closeSpan {
			    color: #191817;
			    font-size: 20px;
			    font-weight: bold;
			    margin-right: 5%;
			}
			
			#logoImg {
			    height: 64px;
    			width: 134px;
			}	
				
		 </style>
	
	</head>
	
	<body>
	
		<div class="wrapper">
		
			<div class="top-bar clearfix">
			
	        	<div class="col-md-4 col-sm-3 col-xs-4 logocon">
	        	
	                <div class="navbar-header" style="background-color: #fff">
	                    <a class="logo" href="#" title="Home">
	                        <img id="logoImg" src="${pageContext.request.contextPath}/assets/image/header_login_logo.png" alt="CAREEM" class="img-responsive logo-img">
	                    </a>
	                </div>
	                
	            </div>
	                
                <div class="col-md-8 col-sm-9 col-xs-8 text-right pull-right brand-text" style="padding-top: 1%">
                
                	<span class="pull-right" style="margin-right: 5%;">
                		<a id="closeSpan" href="javascript:close_window();">Close</a>
                	</span>
                	
                	<span id="showAllSpan" class="pull-right" >Show All</span>
                	
                </div>
                
	        </div>
	    	
	        <section id="block-careem-signup-signup" class="block block-careem-signup clearfix">
	        	<div id="mapCanvas"></div>
	         </section>
	        
		</div>
		
		${it.jsFile}
		
	</body>
	
</html>