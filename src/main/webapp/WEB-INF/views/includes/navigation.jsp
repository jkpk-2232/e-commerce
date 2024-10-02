<%@page import="com.utils.LoginUtils, com.webapp.*, com.webapp.models.*, java.util.*, org.apache.log4j.Logger, com.utils.dbsession.DbSession, com.webapp.models.UserProfileModel, com.webapp.models.TourModel"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.ProjectConstants.UserRoles"%>
<%
	DbSession sessionDB1 = DbSession.getSession(request, response, true);
	String role1 = String.valueOf(sessionDB1.getAttribute(LoginUtils.ROLE));
	String userId1 = String.valueOf(sessionDB1.getAttribute(LoginUtils.USER_ID));
%>

 <div class="navbar navbar-default">
 
    <div class="container-fluid">
    
        <div class="row">
        
            <!-- Brand and toggle get grouped for better mobile display -->
            
            <div class="navbar-header">
            
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                
            </div>

			<%
			
				List<String> assignedRegionList = new ArrayList<String>();
	
				UserProfileModel userProfile = UserProfileModel.getUserAccountDetailsById(userId1);
        		    
        		if (!UserRoles.SUPER_ADMIN_ROLE.equalsIgnoreCase(userProfile.getUserRole())) {

	        		List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(userProfile.getUserId());
	
	        		if (multicityUserRegionModelList != null && multicityUserRegionModelList.size() > 0) {
	
		        		for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {
		        			assignedRegionList.add(multicityUserRegionModel.getMulticityCityRegionId());
		        		}
	        		}
        		}

        		if (assignedRegionList.size() <= 0) {
        			assignedRegionList = null;
        		}
        		
        		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
        		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

    			double minimumWalletAmount = 0.0;

    			if (driverWalletSettingsModel != null) {
    				minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
    			}
			
				Date date = new Date();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				
				long currentDayStartInMilliseconds = calendar.getTimeInMillis();
				
				int totalBookingsCount1 = 0;
				int totalPeopleOpeneedAppCount1 = 0;
				int availableDriversCount1 = 0;
				
				if(role1.equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {
					
					totalBookingsCount1 = TourModel.getVendorsTotalBookingsCountByTime(currentDayStartInMilliseconds, assignedRegionList, userId1);
										
					availableDriversCount1 = DriverGeoLocationModel.getVendorsTotalAvailableDriver(assignedRegionList, userId1, minimumWalletAmount, adminSettingsModel);
					
				} else {
				
					totalBookingsCount1 = TourModel.getTotalBookingsCountByTime(currentDayStartInMilliseconds, assignedRegionList);
				
					totalPeopleOpeneedAppCount1 = LoginUtils.getTotalPeopleOpeneedAppCount(assignedRegionList);
				
					availableDriversCount1 = DriverGeoLocationModel.getTotalAvailableDriver(assignedRegionList, minimumWalletAmount, userId1, adminSettingsModel);
				}
			%>

            <!-- Collect the nav links, forms, and other content for toggling -->
            
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            
                <ul class="nav navbar-nav">
					<li>
						<span class="glyphicon glyphicon-list dashboardWrapperLeftPanelTitleMenuButton" aria-hidden="true" style="top: 10px !important;cursor: pointer;"></span>
					</li>
                    <li>
                        <a href="javascript:void(0)" class="bookingWrapperMenuAllOuter bookingWrapperMenuItems">
                            <div class="bookingWrapperMenuAll">
                                <div class="bookingWrapperMenuAllInner">
                                    <div class="bookingWrapperMenuAllInnerCount bookingWrapperMenuItemsCount">
                                        <%=totalBookingsCount1%>
                                    </div>
                                    <div class="bookingWrapperMenuAllInnerText bookingWrapperMenuItemsText">
                                        Total Bookings By Day
                                    </div>
                                </div>
                            </div>
                        </a>
                    </li>

                    <li>
                        <a href="javascript:void(0)" class="bookingWrapperMenuAvailableOuter bookingWrapperMenuItems bookingWrapperMenuItemsActive">
                            <div class="bookingWrapperMenuAvailable">
                                <div class="bookingWrapperMenuAvailableInner">
                                    <div class="bookingWrapperMenuAvailableInnerCount bookingWrapperMenuItemsCount">
                                        <%=totalPeopleOpeneedAppCount1%>
                                    </div>
                                    <div class="bookingWrapperMenuAvailableInnerText bookingWrapperMenuItemsText">
                                        Connected Users
                                    </div>
                                </div>
                            </div>
                        </a>
                    </li>

                    <li>
                        <a href="javascript:void(0)" class="bookingWrapperMenuBookedOuter bookingWrapperMenuItems">
                            <div class="bookingWrapperMenuBooked">
                                <div class="bookingWrapperMenuBookedInner">
                                    <div id="availableDrivers" class="bookingWrapperMenuBookedInnerCount bookingWrapperMenuItemsCount">
                                        <%=availableDriversCount1%>
                                    </div>
                                    <div class="bookingWrapperMenuBookedInnerText bookingWrapperMenuItemsText">
                                        Available Drivers
                                    </div>
                                </div>
                            </div>
                        </a>
                    </li>
                    
                </ul>

                <ul class="nav navbar-nav navbar-right">
                
<!--                     <li> -->
<!--                         <a href="javascript:void(0)" class="bookingWrapperMenuItemsNewBooking "> -->
<!--                             <button id="btnNewBooking" type="button" class="btn btn-danger" data-toggle="modal"> -->
<!--                             	<i class="glyphicon glyphicon-cars" style="padding-left: 5px;padding-right: 9px;" aria-hidden="true"></i> -->
<!--                             	New Booking -->
<!--                             </button> -->
<!--                         </a> -->
<!--                     </li> -->
                    
                    <li class="dropdown">
                    
                        <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></a>
                    
                        <ul class="dropdown-menu">
                            
                            <% 
								if(role1.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE) || role1.equalsIgnoreCase(UserRoles.ADMIN_ROLE)) {
							%>
                            
                           	<li>
                            	<a href="${pageContext.request.contextPath}/admin-profile.do"><%=BusinessAction.messageForKeyAdmin("labelProfile")%></a>
                            </li>
                            
                            <%
								} else if(role1.equalsIgnoreCase(UserRoles.BUSINESS_OWNER_ROLE)) {
                            %>
                            
                            <li>
                            	<a href="${pageContext.request.contextPath}/business-owner-profile.do"><%=BusinessAction.messageForKeyAdmin("labelProfile")%></a>
                            </li>
                            
                            <%
								} else if(role1.equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {
		                     %>
		                            
		                     <li>
		                      	<a href="${pageContext.request.contextPath}/vendor-profile.do"><%=BusinessAction.messageForKeyAdmin("labelProfile")%></a>
		                     </li>
		                            
		                     <%
								} else {
                            %>
                            
                            <li>
                            	<a href="${pageContext.request.contextPath}/business-operator-profile.do"><%=BusinessAction.messageForKeyAdmin("labelProfile")%></a>
                            </li>
                            
                            <%
								}
                            %>
                            
                            <li role="separator" class="divider"></li>
                            
                            <li>
                            	<a href="${pageContext.request.contextPath}/logout.do"><%=BusinessAction.messageForKeyAdmin("labelLogout")%></a>
                            </li>
                            
                        </ul>
                        
                    </li>

                </ul>
            </div>
            
            <!-- /.navbar-collapse -->
            
        </div>
    </div>
    
    <!-- /.container-fluid -->
    
</div>