<!DOCTYPE html>
<%@page import="java.util.HashMap,com.utils.dbsession.DbSession"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>
	
    <head>
            
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <meta name="description" content="">
        <meta name="keywords" content="">

		<script type="text/javascript">
			var basePath = '${pageContext.request.contextPath}';	
		</script>
		 
		 <%
			HashMap it = (HashMap) request.getAttribute("it");
	     %>

 		<title><%=it.get("labelSignUp").toString()%></title>
	     
 		<link href='http://fonts.googleapis.com/css?family=Open+Sans:300,700,400,600' rel='stylesheet' type='text/css'>
 		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/bootstrap.min.css">

		<link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath}/assets/image/favicon.png"/>

		<style type="text/css">
			
			@font-face {
			    font-family: 'MYRIADPROREGULAR';
			    src: url('../fonts/MYRIADPROREGULAR.eot');
			    src: local('MYRIADPROREGULAR'), url('${pageContext.request.contextPath}/assets/fonts/MYRIADPROREGULAR.woff') format('woff'), url('${pageContext.request.contextPath}/assets/fonts/MYRIADPROREGULAR.ttf') format('truetype');
			}
			@font-face {
			    font-family: 'MYRIADPROlight';
			    src: local('MYRIADPROlight'), url('../fonts/myriadpro-light-webfont.woff') format('woff');
			}
			
			.container {
			    max-width: 1024px;
			}
			.pad-l-0{
			    padding-left: 0
			}
			.pad-r-0{
			    padding-right: 0
			}
			.pad0{
			    padding: 0;
			}
			.clear{
			    clear: both;
			}
			body {  background: #ebebeb;
			        font-family: 'MYRIADPROREGULAR' ,'Open Sans', sans-serif;
			        color:#000;
			        overflow-x: hidden;
			}
			.top-bar {
			    background: #5a5000;
			    padding: 15px 0;
			    color: #fff;
			}
			.call-text{
			    font-size: 24px;
			    margin: 8px 0;
			}
			section#block-careem-signup-signup {
			    padding: 78px 0 0 0;
			}
			h2#fbhead {
			    color: #5a5000;
			    text-align: center;
			    font-size: 50px;
			    font-weight: normal;
			    margin-bottom: 50px;
			}
			.top-bar .top-buttons>li {
			    display: inline;
			}
			.top-bar .top-buttons {
			    display: block;
			    list-style-type: none;
			    padding: 0;
			}
			a.downloadApp:hover, a.downloadApp:focus {
			    color: #54555D;
			    background: transparent;
			    border: 2px solid #54555D;
			    text-decoration: none;
			}
			.downloadApp {
			    float: right;
			    background-color: #54555D;
			    padding: 7px;
			    -webkit-border-radius: 5px;
			    border-radius: 5px;
			    color: #fff;
			    font-size: 12px;
			    text-decoration: none;
			}
			
			.form-control {
			    border-radius: 0;
			    padding: 10px 12px;
			    border: 1px solid #e0e0e0;
			    font-size: 20px;
			    color: #2e3292;
			    height: auto;
			}
			.form-group{
			    margin-bottom: 10px;
			}
			.form-control::-webkit-input-placeholder {
			    color: #2e3292;
			}
			
			.form-control:-moz-placeholder { /* Firefox 18- */
			    color: #2e3292;
			}
			
			.form-control::-moz-placeholder {  /* Firefox 19+ */
			    color: #2e3292;
			}
			
			.form-control:-ms-input-placeholder {  
			    color: #2e3292;
			}
			.error{
			    display: block;
			    color: #ff0000;
			    font-size: 12px;
			    height: 17px;
			}
			button#signup-submit-button {
			    background-color: #5a5000;
			    color: #fff;
			    border: 0;
			    width: 240px;
			    padding: 8px 0;
			    font-size: 26px;
			    border-radius: 5px;
			}
			.terms a{
			    color: #5a5000;
			    text-decoration: none;
			}
			.terms{
			    font-size: 17px;
			    color: black;
			    margin-top: 35px;
			    margin-bottom: 100px;
			    width: 100%;
			    float: left;
			}
			.mobileimg{
			    margin-top: 80px;
			}
			.bottom-container{
			    position: relative;
			}
			.bottom-container img{
			    z-index: 1;
			    position: relative;
			    display: inline-block;
			    text-align: center;
			    max-width: 100%;
			}
			.bottom-container:after{
			    position: absolute;
			    width: 100%;
			    height:6px;
/* 			    background-color: #ed008c; */
			    content: "";
			    top: 60px;
			    left: 0;
			    bottom: 0;
			    z-index: 0;
			}
			span.imglabel {
			    display: block;
			    color: #5a5000;
			    font-size: 22px;
			    margin-top: 20px;
			}
			.downcont{
			    background-color: #d2d2d2;
			    text-transform: uppercase;
			    color: #5a5000; font-size: 22px;
			    padding: 38px;
			    margin-top: 100px;
			}
			.downloadinner{
			    max-width: 600px;
			    margin: 0 auto;
			}
			footer {
			    color: #878787;
			    text-align: center;
			    font-size: 14px;
			    margin: 10px 0 40px 0;
			}
			@media (max-width:991px){
			    .spacing,.mobilecon {
			        display: none;
			    }
			    section#block-careem-signup-signup {
			        padding: 28px 0 0 0;
			    }
			   button#signup-submit-button{ display: block;
			    margin: 0 auto;}
			}
			@media (max-width:767px){
			    h2#fbhead {
			        font-size: 33px;
			        margin-bottom: 30px;
			    }
			    .form-group{
			        padding-left: 0;
			        padding-right: 0;
			    }
			    .bottom-container:after{
			        display: none;
			    }
			    .bottom-container div.col-md-3.col-sm-3.text-center{
			        width: 50%;
			        float: left;
			        margin: 0 0 40px 0;
			    }
			    .downcont{
			        margin-top: 0;
			    }
			    .terms{
			        margin-bottom: 40px;
			    }
			    .downloadinner div{
			        margin-bottom: 20px;
			    }
			    .downcont{
			        padding: 38px 38px 18px 38px;
			    }
			    .downloadtext{
			        width: 100%;
			        float: left;
			    }
			}
			@media (max-width:420px){
			    .form-control {
			        padding: 6px 12px;
			        font-size: 17px;
			    }
			    .form-group {
			        margin-bottom: 5px;
			    }
			    section#block-careem-signup-signup {
			        padding: 8px 0 0 0;
			    }
			    h2#fbhead {
			        font-size: 25px;
			        margin-bottom: 24px;
			    }
			    button#signup-submit-button {
			        width: 160px;
			        padding: 6px 0;
			        font-size: 18px;
			    }
			    .downcont{
			        font-size: 18px;
			    }
			}
		
			.errorMessage {
				color: red;
			}
		
		</style>

    </head>
    <body dir="<%=it.get("language").toString()%>">
        <div class="wrapper">
            <div class="top-bar">
                <div class="container-fluid">
                    <div class="container">
                        <div class="col-md-4 col-sm-5 col-xs-2 spacing">
                            <div class="brand-text">
                                <p class="call-text hidden-xs hidden-sm"><%=it.get("labelCallMasa").toString()%>&nbsp;<%=it.get("REFERRAL_CODE_PHONE").toString()%></p>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 col-xs-6 pad0">
                            <div class="navbar-header">
                                <a class="logo" href="#" title="Home">
                                    <img src="${pageContext.request.contextPath}/assets/images/logo_img.png" alt="CAREEM" class="img-responsive logo-img" style="height: 49px;">
                                </a>
                            </div>
                        </div>
                        <nav class="hidden-lg hidden-md">
                            <ul class="top-buttons">
                                <li>
                                    <a href="https://app.adjust.com/6un2k3_x7zt7f?deep_link=careem://" class="downloadApp"><%=it.get("labelDownload").toString()%></a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
			
			<form id="signup" name="signup" action="${pageContext.request.contextPath}/signup.do" method="post" >
			
<!-- 				<div class="form-group col-md-1  col-sm-3 pad-l-0" style="float:right;"> -->
<%-- 					<%=ViewUtils.outputSelectFieldCellWithoutLabel("language", "", false, 4, it, "form-control") %> --%>
<!-- 	            	<span id="signup-name-error" class="error "></span> -->
<!-- 	            </div> -->
	
	            <section id="block-careem-signup-signup" class="block block-careem-signup clearfix">
	            
	                <div class="container">
	                
	                    <div class="col-md-5 hidden-sm hidden-xs mobilecon">
	                        <img src="${pageContext.request.contextPath}/assets/image/imgpsh_fullsize.png" alt="" class="img-responsive mobileimg" style="margin-top: 130px;">
	                    </div>
	                    
	                    <div class="col-md-7 col-sm-12 col-xs-12 centered" id="signupDiv">
	                    
	                        <div class="email-signup">
	                        
	                            <h2 id="fbhead"><%=it.get("labelSignUp").toString()%></h2>
	                            
	                            <div align="left" id="sucMessage" style="width: 300px; float: left;padding-top: 4px;margin-left: 10px;">
									<%=ViewUtils.outputMessage(request, "Success")%>
								</div>
	                            
	                                <div class="form-group col-md-6 col-sm-6 pad-l-0">
	                                    <%=ViewUtils.outputTextFieldUserSignup("firstName", it.get("labelFirstName").toString(), true, 1, 30, it, "form-control prevent-first-space")%>
	                                    <span id="signup-name-error" class="error "></span>
	                                </div>
	                                                                
	                                <div class="form-group col-md-6 col-sm-6  pad-r-0">
	                                    <%=ViewUtils.outputTextFieldUserSignup("lastName", it.get("labelLastName").toString(), true, 2, 30, it, "form-control prevent-first-space")%>
	                                    <span id="signup-name-error" class="error "></span>
	                                </div>
	                                
	                                <div class="clear"></div>
	                                
	                                <div class="form-group">
	                                    <%=ViewUtils.outputTextFieldUserSignup("emailAddress", it.get("labelEmail").toString(), true, 3, 50, it, "form-control")%>
	                                    <span id="signup-email-error" class="error "></span>
	                                </div>
	                                
	                                <div class="clear"></div>
	                                
	                                <div class="form-group">
	                                    <div class="form-group col-md-3  col-sm-3 pad-l-0">
	                                        <%=ViewUtils.outputSelectFieldCellWithoutLabel("countryCode", "", false, 4, it, "form-control") %>
	                                        <span id="signup-name-error" class="error "></span>
	                                    </div>
	                                    <div class="form-group col-md-9  col-sm-9 pad-r-0">
	                                        <%=ViewUtils.outputTextFieldUserSignup("phone", it.get("labelMobileNo").toString(), true, 4, 17, it, "form-control")%>
	                                        <span id="signup-name-error" class="error "></span>
	                                    </div>
	                                </div>
	                                
	                                <div class="clear"></div>
	                                
	                                <div class="form-group">
	                                     <%=ViewUtils.outputPasswordFieldUserSignup("password", it.get("labelPassword").toString(), true, 5, 50, it, "form-control")%>
	                                    <span id="signup-password-error" class="error "></span>
	                                </div>
	                                
	                                <div class="clear"></div>
	                                
	                                <div class="form-group">
	                                    <%=ViewUtils.outputPasswordFieldUserSignup("confirmPassword", it.get("labelConfirmPassword").toString(), true, 6, 50, it, "form-control")%>
	                                    <span id="signup-confirm-password-error" class="error "></span>
	                                </div>
	
									<div class="clear"></div>
	
	                                <div class="form-group">
	                                    <%=ViewUtils.outputTextFieldUserSignup("referralCode", it.get("labelReferralCode").toString(), true, 7, 30, it, "form-control")%>
	                                    <span id="signup-password-error" class="error "></span>
	                                </div>
	
	                            <div class="col-xs-12"><div class="row"><button id="signup-submit-button" class="bbtn full"><%=it.get("labelSignUp").toString()%></button></div></div>
	                            <div class="terms"><%=it.get("labelAgree").toString()%>  <a href="#" target="_blank"><%=it.get("labelPrivacyPolicy").toString()%></a>  <%=it.get("labelAnd").toString()%>  <a href="#" target="_blank"><%=it.get("labelTermsofServices").toString()%></a></div>
	                            
	                        </div>
	                        
	                    </div>
	                    
	                </div>
	                
	                <div class="bottom-container">
	                
<!-- 	                    <div class="col-md-3 col-sm-3 text-center"> -->
<%-- 	                        <img src="${pageContext.request.contextPath}/assets/image/img_go.png"/> --%>
<%-- 	                        <span class="imglabel"><%=it.get("labelGO").toString()%></span> --%>
<!-- 	                    </div> -->
	                    
<!-- 	                    <div class="col-md-3 col-sm-3 text-center"> -->
<%-- 	                        <img src="${pageContext.request.contextPath}/assets/image/img_economy.png"/> --%>
<%-- 	                        <span class="imglabel"><%=it.get("labelEconomy").toString()%></span> --%>
<!-- 	                    </div> -->
	                    
<!-- 	                    <div class="col-md-3 col-sm-3 text-center"> -->
<%-- 	                        <img src="${pageContext.request.contextPath}/assets/image/img_business.png"/> --%>
<%-- 	                        <span class="imglabel"><%=it.get("labelBusiness").toString()%></span> --%>
<!-- 	                    </div> -->
	                    
<!-- 	                    <div class="col-md-3 col-sm-3 text-center"> -->
<%-- 	                        <img src="${pageContext.request.contextPath}/assets/image/img_extra.png"/> --%>
<%-- 	                        <span class="imglabel"><%=it.get("labelExtra").toString()%></span> --%>
<!-- 	                    </div> -->
	                    
	                    <div class="clear"></div>
	                    
	                    <div class="downcont clearfix">
	                        <div class="downloadinner text-center">
	                            <div class="pull-left downloadtext" style="margin-right: 35px;padding-top: 8px;"><%=it.get("labelDOWNLOADTHEAPPHERE").toString()%></div>
	                            <div class="col-md-3  col-sm-3  col-xs-12"><a href="#"><img src="${pageContext.request.contextPath}/assets/image/btn_play_store.png"/></a></div>
	                            <div class="col-md-3  col-sm-3  col-xs-12"><a href="#"><img src="${pageContext.request.contextPath}/assets/image/btn_app_Store.png"/></a></div>
	                        </div>
	                    </div>
	                    
	                </div>
	                
	            </section>
            
            	<%=ViewUtils.outputFormHiddenField("labelPasswordandConfirmpasswordshouldbesame", it) %>
            	<%=ViewUtils.outputFormHiddenField("referralCodeFlag", it) %>
            	<%=ViewUtils.outputFormHiddenField("errorReferralCodeIsRquired", it) %>
             </form>
            
            <footer>
                <%=it.get("labelCopyright").toString()%>
            </footer>
            
        </div>
		 ${it.jsFile}
    </body>
</html>