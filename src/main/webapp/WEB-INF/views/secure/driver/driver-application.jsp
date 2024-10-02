<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.utils.view.ButtonUtils, com.utils.view.ActionButton"%>
<%@ page import="com.webapp.viewutils.*,com.jeeutils.*,com.webapp.models.*"%>
<html>

	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		
		<%@ include file="/WEB-INF/views/includes/common-head.jsp"%>
		<title><%=it.get("labelDrivers").toString() %></title>
		
		<style type="text/css">
				.VerticalTop{
				vertical-align: top;
				}
				
				.driverDetails .driverImg{
				    height: 134px;
				}
	
				#car {
					width: 317px;
	/* 			    margin-left: 260px; */
				    margin-top: 2px;
				    height: 33px;
				}
			
				.searchBookings {
				  	height: 31px;
					width: 85px;
				    margin: auto;
				    position: relative;
				    top: 3px;
				    float: left;
				}
			
				.searchBookings #search1 {
				    background: none repeat scroll 0 0 rgba(0, 0, 0, 0);
				    height: 31px;
				    left: -3px;
				    padding: 0;
				    position: relative;
				    outline: none;
				    width: 80px;
				}
		</style>
		
	</head>
	
	<body class="nav-md">
	
		<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
		<!-- page content -->
		<div class="right_col" role="main">
			<div class="">
	
				<div class="page-title">
					<div class="title_left">
						<h3><%=it.get("labelDrivers").toString() %></h3>
					</div>
				</div>
				<div class="clearfix"></div>
	
				<div class="row">
	
					<div class="col-md-12">
	
						<div class="x_panel">
							<div class="x_content">
								<div class="row">
	
	
									<div class="driverDetailsPage">
										
											<div id="btnAddDriver" class="driverAddBtn">
												<div class="labelForbtnExport leftCommonLabelCss"><%=it.get("labelAddDriver").toString() %></div>
											</div>
										
											<div>
											<div class="driverRightMenu">
											
												<div class="driverRightButtonDiv">
												
													<div id="btnDetails" class="rightButtonClass">
														<div class="labelForbtnDetails rightCommonLabelCss" style="color:white;"><%=it.get("labelDetails").toString() %></div>
													</div>
													
												</div>
												
												<div style="margin-top:50%; margin-left:50%" id="ajxload">
													<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
												</div>
												
												<div class="driverInfo">		
												
													<form id="edit-driver" name="edit-driver" action="${pageContext.request.contextPath}/manage-drivers/edit-driver.do" method="post" >
													
														<div class="driverTitleDiv"><%=it.get("labelPersonalDetails").toString() %></div>
														
														<div style="margin-top: 10px;">
															<div class="firstNameDiv">
																<label id="firstNameLabel" class=""><%=it.get("labelFirstName").toString() %>  <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("firstName", "", true, 2, 30, it, "")%>
															</div>
															
															<div class="lastNameDiv">
																<label id="lastNameLabel" class=""><%=it.get("labelLastName").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("lastName", "",true, 3, 30, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="emailAddressDiv">
																<label id="emailAddressLabel" class=""><%=it.get("labelEmailAddress").toString() %></label>
																<%=ViewUtils.outputTextField("emailAddress", "", true, 50, 50, it, "")%>
															</div>
															
															<div class="phoneDiv">
																<label id="phoneLabel" class=""><%=it.get("labelPhone").toString() %> <span style="color:#FF0000;">*</span></label>
																<div class="" style="display: inline-block;">
																<%=ViewUtils.outputSelectFieldCellWithoutLabel("countryCode", "", false, 5, it, "") %>
																<%=ViewUtils.outputTextField("phone", "", true, 6, 17, it, "VerticalTop")%>
																</div>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="genderDiv">
																<label id="genderLabel" class=""><%=it.get("labelGender").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputSelectFieldCell("gender", "", false, 7, "<option value='Male'>" + it.get("labelMale").toString() + "</option><option value='Female'>" + it.get("labelFemale").toString() + "</option>", it) %>
															</div>
															
															<div class="lastNameDiv">
																<label id="lastNameLabel" class="adminPasswordDriver"><%=it.get("labelPassword").toString() %>  </label>
																<%=ViewUtils.outputTextField("password", "",true, 8, 30, it, "")%>
															</div>
															
														</div>
														
														<div class="clearfix"></div>
														
														<%-- <div class="driverTitleDiv"><%=it.get("labelCarDetails").toString() %></div>
														
														<div style="margin-top: 10px;">
														
															<div class="modelNameDiv">
																<label id="modelNameLabel" class=""><%=it.get("labelCarType").toString() %> <span style="color:#FF0000;">*</span></label>
																<%
																	String carOptions = ComboUtils.getCarModelOption(ViewUtils.requestAttributeValue(it, "carType"));
																	out.println(ViewUtils.outputSelectFieldCell("carType", "", false, 8, carOptions, it));											
																%>
															</div>
															
															<div class="colorDiv">
																<label id="colorLabel" class=""><%=it.get("labelColor").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("color", "", true, 9, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="plateNoDiv">
																<label id="plateNoLabel" class=""><%=it.get("labelPlateNo").toString() %>  <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("plateNo", "", true, 10, 20, it, "")%>
															</div>
															
															<div class="carYearDiv">
																<label id="carYearLabel" class=""><%=it.get("labelCarYear").toString() %> <span style="color:#FF0000;">*</span></label>
																<%
																	String yearFilterOptions = ComboUtils.getPastFifteenYearOptionForLocalization(ViewUtils.requestAttributeValue(it, "carYear"),it.get("labelYear").toString());
																	out.println(ViewUtils.outputSelectFieldCell("carYear", "", false, 11, yearFilterOptions, it));											
																%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="noOfPassengerDiv">
															
																<label id="noOfPassengerLabel" class=""><%=it.get("labelSeatingCapacity").toString() %> <span style="color:#FF0000;">*</span></label>
																<%
																	String noOfPassengerOptions = ComboUtils.getNumberOfPassangerOption(ViewUtils.requestAttributeValue(it, "noOfPassenger"));
																	out.println(ViewUtils.outputSelectFieldCell("noOfPassenger", "", false, 12, noOfPassengerOptions, it));											
																%>
															</div>
															
															<div class="ownerDiv">
																<label id="ownerLabel" class=""><%=it.get("labelModelName").toString() %><span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("modelName", "", true, 13, 30, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
																						
															<div class="ownerDiv">
																<label id="ownerLabel" class=""><%=it.get("labelOwner").toString() %>  <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("owner", "", true, 14, 30, it, "")%>
															</div>
															<div class="ownerDiv">
																<label id="ownerLabel" class=""><%=it.get("labelCarMake").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("make", "", true, 15, 30, it, "")%>
															</div>
														</div>
														 --%>
														 
														 <div style="margin-top: 8px;">
													
														<div class="genderDiv">
															<label id="companyDriverLabel" class=""><%=it.get("labelCompanyDriver").toString() %><span style="color:#FF0000;">*</span></label>
															<%=ViewUtils.outputSelectFieldCellWithoutLabel("companyDriver", "", false, 4, it, "") %>
														</div>
														
														<div class="lastNameDiv">
															<label id="abnLabel" class=""><%=it.get("labelAbnName").toString() %> </label>
															<%=ViewUtils.outputTextField("auBusinessNo", "",true, 9, 30, it, "")%>
														</div>
														
													</div>
													
													<div class="clearfix"></div>
														 
														 <div class="driverTitleDiv"><%=it.get("labelCarDetails").toString() %></div>
													
													<div class="carModelTypeIdDiv topMargin">
														<div style="float:left;">
															<label id="carModelTypeIdLabel" class="">Car <span style="color:#FF0000;">*</span></label>
														</div>
														<div id="searchBookingsDiv">
															<div class="searchBookings" style="">
																<%=ViewUtils.searchStringFieldCustom("search1", it.get("labelSearch").toString(), true, 1, 60, it, request)%>
															</div>
															<%=ViewUtils.outputSelectFieldCellWithoutLabel("car", "", false, 5, it, "") %>
														</div>
													</div>
														 
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
															<div class="insuranceEffectiveDateDiv">
																<label id="insuranceEffectiveDateLabel" class=""><%=it.get("labelInsuranceEffectiveDate").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("insuranceEffectiveDate", "", true, 16, 20, it, "")%>
															</div>
															
															<div class="insuranceExpirationDateDiv">
																<label id="insuranceExpirationDateLabel" class=""><%=it.get("labelInsuranceExpiryDate").toString() %><span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("insuranceExpirationDate", "", true, 17, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div class="driverTitleDiv"><%=it.get("labelDrivingLicenseInfo").toString() %> </div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
															<div class="dobDiv">
																<label id="dobLabel" class=""><%=it.get("labelDOB").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("dob", "",true, 18, 20, it, "")%>
															</div>
															
															<div class="drivingLicenseDiv">
																<label id="drivingLicenseLabel" class=""><%=it.get("labelDrivingLicenseNo").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("drivingLicense", "", true, 19, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														<div style="margin-top: 8px;">
															<div class="dobDiv">
																<label id="licenseExpirationLabel" class=""><%=it.get("labelLicenseExpiration").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("licenseExpiration", "",true, 20, 20, it, "")%>
															</div>
															
														</div>
														
														<div class="clearfix"></div>
														
														<div class="driverTitleDiv"><%=it.get("labelMailingAddress").toString() %></div>
														
														<div style="margin-top: 10px;">
															<div class="street1Div">
																<label id="street1Label" class=""><%=it.get("labelStreet1").toString() %></label>
																<%=ViewUtils.outputTextField("street1", "", true, 20, 50, it, "")%>
															</div>
															
															<div class="street2Div">
																<label id="street2Label" class=""><%=it.get("labelStreet2").toString() %></label>
																<%=ViewUtils.outputTextField("street2", "", true, 21, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="countryDiv">
																<label id="countryLabel" class=""><%=it.get("labelCountry").toString() %></label>
																<%=ViewUtils.outputTextField("country", "", true, 22, 50, it, "")%>
															</div>
															
															<div class="stateDiv">
																<label id="stateLabel" class=""><%=it.get("labelState").toString() %></label>
																<%=ViewUtils.outputTextField("state", "", true, 23, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="cityDiv">
																<label id="cityLabel" class=""><%=it.get("labelCity").toString() %></label>
																<%=ViewUtils.outputTextField("city", "", true, 24, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<%-- <div class="driverTitleDiv">
															Billing Address
															<div class="sameAsAboveCheckBoxDiv">
																<%=ViewUtils.outputCheckBox("checkBoxBusinessOwner", "", false, 26, it)%>
															</div>
															<div class="labelSameAsAboveDiv"><%=it.get("labelSameAsMailingAddress").toString() %></div>
														</div>
														
														<div style="margin-top: 10px;">
															<div class="bStreet1Div">
																<label id="bStreet1Label" class=""><%=it.get("labelStreet1").toString() %></label>
																<%=ViewUtils.outputTextField("bStreet1", "", true, 27, 50, it, "")%>
															</div>
															
															<div class="bStreet2Div">
																<label id="bStreet2Label" class=""><%=it.get("labelStreet2").toString() %></label>
																<%=ViewUtils.outputTextField("bStreet2", "", true, 28, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="bCountryDiv">
																<label id="bCountryLabel" class=""><%=it.get("labelCountry").toString() %></label>
																<%=ViewUtils.outputTextField("bCountry", "", true, 29, 50, it, "")%>
															</div>
															
															<div class="bStateDiv">
																<label id="bStateLabel" class=""><%=it.get("labelState").toString() %></label>
																<%=ViewUtils.outputTextField("bState", "", true, 30, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="bCityDiv">
																<label id="bCityLabel" class=""><%=it.get("labelCity").toString() %></label>
																<%=ViewUtils.outputTextField("bCity", "", true, 31, 50, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div> --%>
														
														<div class="driverTitleDiv"><%=it.get("labelBankDetails").toString() %></div>
														
														<div style="margin-top: 10px;">
														
															<div class="bankNameDiv">
																<label id="bankNameLabel" class=""><%=it.get("labelBankName").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("bankName", "", true, 33, 50, it, "")%>
															</div>
															
															<div class="accountNumberDiv">
																<label id="accountNumberLabel" class=""><%=it.get("labelAccountNumber").toString() %>  <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("accountNumber", "", true, 34, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
														
															<div class="accountNameDiv">
																<label id="accountNameLabel" class=""><%=it.get("labelAccountName").toString() %><span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("accountName", "", true, 35, 20, it, "")%>
															</div>
															
															<div class="routingNumberDiv">
																<label id="routingNumberLabel" class=""><%=it.get("labelRoutingNumber").toString() %><span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("routingNumber", "", true, 36, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div style="margin-top: 8px;">
															<div class="typeDiv">
																<label id="typeLabel" class=""><%=it.get("labelType").toString() %> <span style="color:#FF0000;">*</span></label>
																<%=ViewUtils.outputTextField("type", "", true, 37, 20, it, "")%>
															</div>
														</div>
														
														<div class="clearfix"></div>
														
														<div class="clearfix"></div>
														
														<div class="clearfix"></div>
														
														<div class="clearfix"></div>
														<%=ViewUtils.outputFormHiddenField("userStatus", it) %>
								                        <%=ViewUtils.outputFormHiddenField("userId", it) %>
														<%=ViewUtils.outputFormHiddenField("sameAsMailing", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenPhotoUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenDriverLicenseImage", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenInsuranceImgUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenFrontImgUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenBackImgUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("isSuperAdmin", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenCarTitleUrl", it) %>
														
														<%=ViewUtils.outputFormHiddenField("hiddenDriverAccreditationImgUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenDriverCriminalReportImgUrl", it) %>
														<%=ViewUtils.outputFormHiddenField("hiddenDriverLicenseBackImgUrl", it) %>
														
														<%=ViewUtils.outputFormHiddenField("labelImageUploadExtensionMsg", it) %>
														<%=ViewUtils.outputFormHiddenField("labelImageExtensionErrorMsg", it) %>
														<%=ViewUtils.outputFormHiddenField("labelFileWith", it) %>
														<%=ViewUtils.outputFormHiddenField("labelOK", it) %>
														<%=ViewUtils.outputFormHiddenField("labelNoDataAvailable", it) %>
							
														<%=ViewUtils.outputFormHiddenField("labelLicenceNo", it) %>
														<%=ViewUtils.outputFormHiddenField("labelName", it) %>
														<%=ViewUtils.outputFormHiddenField("labelEmail", it) %>
														<%=ViewUtils.outputFormHiddenField("labelPhone", it) %>
							
													</form>
													
													<div class="driverTitleDiv"><%=it.get("labelScannedDocuments").toString() %></div>
													
													<form action="javascript:;" method="post" enctype="multipart/form-data" name="licenseForm" id="licenseForm" class="col-md-6">
												
													<div style="margin-top:20px;float:left;width: 300px;">
													
														<label style="width:200px;left:62px;margin-bottom: 20px;"><%=it.get("labelDrivingLicensePhoto").toString() %></label>
														<br>
														<%=ViewUtils.outPutImage("drivingLicensePhoto", it, request)%>
														<div class="fileUpload btn btn-primary" style ="margin-top:10px;">
															<span><%=it.get("labelUpload").toString() %></span>
															<input type="file" id="licensePhoto" name="licensePhoto" class="upload">
														</div>
														<div id="ajaxLoad1" style="float:right;margin-right:165px; padding-top: 12px;">
															<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
														</div>
														
														<div class="clearfix"></div>
													</div>
												 </form>
												 
											     <form action="javascript:;" method="post" enctype="multipart/form-data" name="licenseBackForm" id="licenseBackForm" class="col-md-6">
												  
													<div style="margin-top:20px; float:left;width: 300px;">
													
														<label style="width:200px;left: 78px;margin-bottom: 20px;"><%=it.get("labelDrivingLicenseBackPhoto").toString() %></label>
														<br>
														<%=ViewUtils.outPutImage("driverLicenseBackPhotoUrl", it, request)%>
														<div class="fileUpload btn btn-primary" style ="margin-top:10px; margin-left: 50px;">
															<span><%=it.get("labelUpload").toString() %></span>
															<input style ="margin-left:45px" type="file" id="licenseBackPhoto" name="licenseBackPhoto" class="upload">
														</div>
														<div id="ajaxLoad2" style="float:right;margin-right:100px;padding-top: 12px;">
															<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
														</div>
													
														<div class="clearfix"></div>
													</div>
												</form>
												<div class="clearfix"></div>
												
												<form action="javascript:;" method="post" enctype="multipart/form-data" name="driverAccreditationForm" id="driverAccreditationForm" class="col-md-6">
												  
													<div style="margin-top:20px; float:left;width: 300px;">
													
														<label style="width:200px;left: 78px;margin-bottom: 20px;"><%=it.get("labelAccreditationPhoto").toString() %></label>
														<br>
														<%=ViewUtils.outPutImage("driverAccreditationPhotoUrl", it, request)%>
														<div class="fileUpload btn btn-primary" style ="margin-top:10px; margin-left: 50px;">
															<span><%=it.get("labelUpload").toString() %></span>
															<input style ="margin-left:45px" type="file" id="driverAccreditationPhoto" name="driverAccreditationPhoto" class="upload">
														</div>
														<div id="ajaxLoad3" style="float:right;margin-right:100px;padding-top: 12px;">
															<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
														</div>
													
														<div class="clearfix"></div>
													</div>
												</form>
												
												
												<form action="javascript:;" method="post" enctype="multipart/form-data" name="driverCriminalReportForm" id="driverCriminalReportForm" class="col-md-6">
												  
													<div style="margin-top:20px; float:left;width: 300px;">
													
														<label style="width:200px;left: 78px;margin-bottom: 20px;"><%=it.get("labelCriminalReportPhoto").toString() %></label>
														<br>
														<%=ViewUtils.outPutImage("driverCriminalReportPhotoUrl", it, request)%>
														<div class="fileUpload btn btn-primary" style ="margin-top:10px; margin-left: 50px;">
															<span><%=it.get("labelUpload").toString() %></span>
															<input style ="margin-left:45px" type="file" id="driverCriminalReportPhoto" name="driverCriminalReportPhoto" class="upload">
														</div>
														<div id="ajaxLoad4" style="float:right;margin-right:100px;padding-top: 12px;">
															<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
														</div>
													
														<div class="clearfix"></div>
													</div>
												</form>
												
												<div class="clearfix"></div>
												
												<form action="javascript:;" method="post" enctype="multipart/form-data" name="photoForm" id="photoForm" class="col-md-6">
													
													<div style="margin-top:20px; float:left; width:192px;">
													
														<label style="width:200px;left:79px; margin-bottom:20px;"><%=it.get("labelProfileImage").toString() %></label>
														<br>
														
														<%=ViewUtils.outPutImage("profileImgUrl", it, request)%>
														
														<div class="errorMessageProfileImgUrl" style ="color:red;"></div>
														
														<div class="fileUpload btn btn-primary" style ="margin-top:10px;margin-right: 20px; margin-bottom: 10px;">
															<span><%=it.get("labelUpload").toString() %></span>
															<input type="file" style ="margin-bottom:10px" id="profilePhoto" name="profilePhoto" class="upload">
														</div>
														
														<div id="ajaxLoad5" style="float:right;margin-right:150px;padding-top:12px;">
															<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif">
														</div>
														
														<div class="clearfix"></div>
														
													</div>
												</form>
													
													<div class="clearfix"></div>
													
													<div class="driverButtonDiv">
														<div id="btnAccept" class="rightButtonClass" tabindex="33">
															<div class="labelForbtnAccept rightCommonLabelCss"><%=it.get("labelAccept").toString() %></div>
														</div>
														<div id="btnReject" class="rightButtonClass" tabindex="34" style="margin-left:1%;">
														   <div class="labelForbtnReject leftCommonLabelCss"><%=it.get("labelReject").toString() %></div>
													    </div>				
														<div id="btnSave" class="rightButtonClass">
															<div class="labelForbtnSave leftCommonLabelCss"><%=it.get("labelSave").toString() %></div>
														</div>
														<div class="activeDeactive" style="width:22px;margin-left: 61%;padding-top: 13px;" id=""> 
															<img src="${pageContext.request.contextPath}/assets/image/inactive.png">
														</div>
													</div>
														
													<div class="clearfix"></div>
												</div>
											</div>
											
											<div class="driverLeftMenu">
											
												<div class="driverLeftButtonDiv">
													<div id="btnDriverDetails" class="leftButtonClass">
														<div class="labelForbtnDriverDetails leftCommonLabelCss"><%=it.get("labelDriver").toString() %></div>
													</div>
													<div id="btnViewDriverApplication" class="leftButtonClass">
														<div class="labelForbtnViewDriverApplication leftCommonLabelCss" style="color:white;"><%=it.get("labelApplications").toString() %></div>
													</div>
												</div>
												
												<div class="driverListDiv">
												
													<div class="searchDriver">
														<%=ViewUtils.searchStringField("search", it.get("labelSearch").toString(), true, 1, 60, it, request)%>
													</div>
													
													<div style="margin-top:300px; margin-left:155px" id="listLoader">
														<img alt="" src="${pageContext.request.contextPath}/assets/images/ajax-loader.gif"> 
													</div>
													
													<div class="driverList">
													</div>
												</div>
											</div>
										</div>	
										</div>
										
								
								</div>
							</div>
	
						</div>
	
					</div>
	
				</div>
			</div>
		</div>
		<!-- /page content -->
	
		<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
		
	</body>
	
</html>