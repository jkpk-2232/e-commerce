<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="com.utils.myhub.UserRoleUtils"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.actions.BusinessApiAction"%>
<%@page import="com.utils.myhub.SessionUtils"%>
<%@page import="com.utils.myhub.WebappPropertyUtils"%>
<%@page import="com.restfb.types.Post.Action"%>
<%@page import="com.webapp.ProjectConstants"%>
<%@page import="com.webapp.ProjectConstants.UserRoles"%>
<%@page import="com.webapp.models.UrlSubCategoryModel"%>
<%@page import="com.utils.LoginUtils, com.webapp.models.UrlModel, java.util.*, org.apache.log4j.Logger, com.utils.dbsession.DbSession, com.webapp.models.UserProfileModel, com.webapp.models.TourModel"%>

<%
	Map<String, String> sessionAttributes = SessionUtils.getSession(request, response, true);

	String userName = SessionUtils.getAttributeValue(sessionAttributes, LoginUtils.USER_FULL_NAME);
	String role = SessionUtils.getAttributeValue(sessionAttributes, LoginUtils.ROLE);
	String roleId = SessionUtils.getAttributeValue(sessionAttributes, LoginUtils.ROLE_ID);
	String userId = SessionUtils.getAttributeValue(sessionAttributes, LoginUtils.USER_ID);
	String photoUrl = SessionUtils.getAttributeValue(sessionAttributes, LoginUtils.PHOTO_URL);
	String projectName = WebappPropertyUtils.PROJECT_NAME;

	boolean reportsFlag = false;

	boolean vendorFlag = UserRoleUtils.isVendorRole(roleId);
	boolean accountsFlag = false;
	boolean driverAccountsFlag = false;
	boolean vendorAccountsFlag = false;
	boolean vendorDriverAccountsFlag = false;
	boolean vendorMyAccountFlag = false;

	boolean encashRequestsFlag = false;
	boolean holdEncashRequestsFlag = false;
	boolean approvedEncashRequestsFlag = false;
	boolean transferredEncashRequestsFlag = false;
	boolean rejectedEncashRequestsFlag = false;
%>

<!-- BEGIN #sidebar -->
<div id="sidebar" class="app-sidebar">

	<!-- BEGIN scrollbar -->
	<div class="app-sidebar-content" data-scrollbar="true" data-height="100%">

		<!-- BEGIN menu -->
		<div class="menu">
		
			<div class="menu-header">Navigation</div>
		
			<%

				UrlModel urlModel;
		
	       		if (userId != null || !("".equals(userId.trim()))) {
					
	       			long urlGroupId;
	       			List<UrlModel> menuList = UrlModel.getPriorityMenus(userId, false);
	       			List<Map<String, List<UrlSubCategoryModel>>> outputMap = UrlSubCategoryModel.getSubPriorityMenus(userId);
	
					if (menuList != null && !menuList.isEmpty()) {
	
						for (int i = 0; i < menuList.size(); i++) {
	
							urlModel = menuList.get(i);
							urlGroupId = urlModel.getUrlGroupId();
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_REPORTS_ID) {
								reportsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_DRIVER_ACCOUNTS_ID) {
								driverAccountsFlag = true;
								accountsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_ACCOUNTS_ID) {
								vendorAccountsFlag = true;
								accountsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_HOLD_ENCASH_REQUESTS_ID) {
								holdEncashRequestsFlag = true;
								encashRequestsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_APPROVED_ENCASH_REQUESTS_ID) {
								approvedEncashRequestsFlag = true;
								encashRequestsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_TRANSFERRED_ENCASH_REQUESTS_ID) {
								transferredEncashRequestsFlag = true;
								encashRequestsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_REJECTED_ENCASH_REQUESTS_ID) {
								rejectedEncashRequestsFlag = true;
								encashRequestsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_DRIVER_ACCOUNTS_ID) {
								vendorDriverAccountsFlag = true;
								accountsFlag = true;
							}
							
							if (urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MY_ACCOUNT_ID) {
								vendorMyAccountFlag = true;
								accountsFlag = true;
							}
							
							if (!(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_REPORTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_DRIVER_ACCOUNTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_ACCOUNTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_HOLD_ENCASH_REQUESTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_APPROVED_ENCASH_REQUESTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_TRANSFERRED_ENCASH_REQUESTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_REJECTED_ENCASH_REQUESTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_DRIVER_ACCOUNTS_ID)
									&& !(urlGroupId == ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MY_ACCOUNT_ID)) {
								
								if (urlModel.isSubMenuOption()) {
	
								%>
									
									<div class="menu-item has-sub">
										<a href="#" class="menu-link">
											<span class="menu-icon">
												<i class="<%=urlModel.getUrlIcon()%>"></i>
											</span>
											<span class="menu-text"><%=BusinessAction.messageForKeyAdmin(urlModel.getUrlGroupName())%></span>
											<span class="menu-caret"><b class="caret"></b></span>
										</a>
										<div class="menu-submenu">
								<% 
										for (Map<String, List<UrlSubCategoryModel>> singleMap : outputMap) {
											
											if (singleMap.containsKey(urlModel.getUrlId())) {
												
												List<UrlSubCategoryModel> innerList = singleMap.get(urlModel.getUrlId());
												
												for (UrlSubCategoryModel urlSubCategoryModel: innerList) {
								%>
													
													<div class="menu-item" id="subMenu-<%=String.valueOf(urlSubCategoryModel.getUrlSubCategoryId())%>">
														<a href="${pageContext.request.contextPath}<%=urlSubCategoryModel.getUrl()%>" class="menu-link">
															<span class="menu-icon"><i class="<%=urlSubCategoryModel.getUrlIcon()%>"></i></span>
															<span class="menu-text"><%=BusinessAction.messageForKeyAdmin(urlSubCategoryModel.getUrlTitle())%></span>
														</a>
													</div>		
								<%												
												}
												
												break;
											}									
										}
								%>
										</div>
									</div>
								<%
								
								} else {
								
								%>
									<div class="menu-item" id="<%=String.valueOf(urlModel.getUrlGroupId())%>">
										<a href="${pageContext.request.contextPath}<%=urlModel.getUrl()%>" class="menu-link">
											<span class="menu-icon"><i class="<%=urlModel.getUrlIcon()%>"></i></span>
											<span class="menu-text"><%=BusinessAction.messageForKeyAdmin(urlModel.getUrlGroupName())%></span>
										</a>
									</div>	
								<%
									
								}
							}
						}
					}
				}
			%>
			
			<% 
				if (vendorFlag) {
			%>
				<div class="menu-item has-sub">
					<a href="#" class="menu-link">
						<span class="menu-icon">
							<i class="fas fa-cogs"></i>
						</span>
						<span class="menu-text">Vendor Settings</span>
						<span class="menu-caret"><b class="caret"></b></span>
					</a>
					<div class="menu-submenu">
						<div class="menu-item" id="subMenu-btnVendorCitySettings">
							<a href="${pageContext.request.contextPath}/manage-vendor-city-settings.do" class="menu-link">
								<span class="menu-text">Vendor City Settings</span>
							</a>
						</div>
						<!--<div class="menu-item" id="subMenu-btnVendorAirport">
							<a href="${pageContext.request.contextPath}/manage-vendor-airport-regions.do" class="menu-link">
								<span class="menu-text">Airport Regions</span>
							</a>
						</div>-->
						<div class="menu-item" id="subMenu-btnRentalPackageSettings">
							<a href="${pageContext.request.contextPath}/manage-rental-packages.do" class="menu-link">
								<span class="menu-text">Rental Packages</span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-btnAboutUs">
							<a href="${pageContext.request.contextPath}/manage-about-us.do" class="menu-link">
								<span class="menu-text"><%=BusinessAction.messageForKeyAdmin("labelAboutUs")%></span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-btnPrivacyPolicy">
							<a href="${pageContext.request.contextPath}/manage-privacy-policy.do" class="menu-link">
								<span class="menu-text"><%=BusinessAction.messageForKeyAdmin("labelPrivacyPolicy")%></span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-btnRefundPolicy">
							<a href="${pageContext.request.contextPath}/manage-refund-policy.do" class="menu-link">
								<span class="menu-text"><%=BusinessAction.messageForKeyAdmin("labelRefundPolicy")%></span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-btnTermsConditions">
							<a href="${pageContext.request.contextPath}/manage-terms-conditions.do" class="menu-link">
								<span class="menu-text"><%=BusinessAction.messageForKeyAdmin("labelTermsAndConditions")%></span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-btnAdminContactUs">
							<a href="${pageContext.request.contextPath}/manage-admin-contact-us.do" class="menu-link">
								<span class="menu-text"><%=BusinessAction.messageForKeyAdmin("labelAdminContactUs")%></span>
							</a>
						</div>
					</div>
				</div>
			<% 
				}
			%>
			
			<% 
			
				if (role.equalsIgnoreCase(UserRoles.SUB_VENDOR_ROLE)) {
					//do not show accounts to sub vendor role
					accountsFlag = false;
				}
			
				if (accountsFlag) {
			%>
				<div class="menu-item has-sub">
					<a href="#" class="menu-link">
						<span class="menu-icon">
							<i class="fas fa-credit-card"></i>
						</span>
						<span class="menu-text">Accounts</span>
						<span class="menu-caret"><b class="caret"></b></span>
					</a>
					<div class="menu-submenu">
						<% 
							if ((driverAccountsFlag) && ((role.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE)) || (role.equalsIgnoreCase(UserRoles.ADMIN_ROLE)))) {
						%>
							<div class="menu-item" id="subMenu-401">
								<a href="${pageContext.request.contextPath}/manage-drivers/account.do" class="menu-link">
									<span class="menu-text">Driver Accounts</span>
								</a>
							</div>
						<% 
							}
                    	
							if ((vendorAccountsFlag) && ((role.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE)) || (role.equalsIgnoreCase(UserRoles.ADMIN_ROLE)))) {
						%>
							<div class="menu-item" id="subMenu-402">
								<a href="${pageContext.request.contextPath}/manage-vendor/account.do" class="menu-link">
									<span class="menu-text">Vendor Accounts</span>
								</a>
							</div>
						<% 
							}
                    	
							if ((vendorMyAccountFlag) && (role.equalsIgnoreCase(UserRoles.VENDOR_ROLE))) {
						%>
							<div class="menu-item" id="subMenu-403">
								<a href="${pageContext.request.contextPath}/vendor/my-account.do" class="menu-link">
									<span class="menu-text">My Account</span>
								</a>
							</div>
						<% 
							}
							
							if ((vendorDriverAccountsFlag) && (role.equalsIgnoreCase(UserRoles.VENDOR_ROLE))) {
						%>
							<div class="menu-item" id="subMenu-404">
								<a href="${pageContext.request.contextPath}/manage-vendor-drivers/account.do" class="menu-link">
									<span class="menu-text">Driver Accounts</span>
								</a>
							</div>
						<% 
							}
						%>
					</div>
				</div>
			<% 
				}
			%>
			
			<% 
				if ((encashRequestsFlag) && ((role.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE)) || (role.equalsIgnoreCase(UserRoles.ADMIN_ROLE)))) {
			%>
				<div class="menu-item has-sub">
					<a href="#" class="menu-link">
						<span class="menu-icon">
							<i class="fas fa-dollar-sign"></i>
						</span>
						<span class="menu-text">Encash Requests</span>
						<span class="menu-caret"><b class="caret"></b></span>
					</a>
					<div class="menu-submenu">
						<% 
							if (holdEncashRequestsFlag) {
						%>
							<div class="menu-item" id="subMenu-301">
								<a href="${pageContext.request.contextPath}/encash-requests/hold.do" class="menu-link">
									<span class="menu-text">Hold Encash Requests</span>
								</a>
							</div>
						<% 
							}
                    	
							if (approvedEncashRequestsFlag) {
						%>
							<div class="menu-item" id="subMenu-302">
								<a href="${pageContext.request.contextPath}/encash-requests/approved.do" class="menu-link">
									<span class="menu-text">Approved Encash Requests</span>
								</a>
							</div>
						<% 
							}
                    	
							if (transferredEncashRequestsFlag) {
						%>
							<div class="menu-item" id="subMenu-303">
								<a href="${pageContext.request.contextPath}/encash-requests/transferred.do" class="menu-link">
									<span class="menu-text">Transferred Encash Requests</span>
								</a>
							</div>
						<% 
							}
							
							if (rejectedEncashRequestsFlag) {
						%>
							<div class="menu-item" id="subMenu-304">
								<a href="${pageContext.request.contextPath}/encash-requests/rejected.do" class="menu-link">
									<span class="menu-text">Rejected Encash Requests</span>
								</a>
							</div>
						<% 
							}
						%>
					</div>
				</div>
			<% 
				}
			%>
			
			<% 
				if (reportsFlag) {
			%>
				<div class="menu-item has-sub">
					<a href="#" class="menu-link">
						<span class="menu-icon">
							<i class="fas fa-file-pdf"></i>
						</span>
						<span class="menu-text">Reports</span>
						<span class="menu-caret"><b class="caret"></b></span>
					</a>
					<div class="menu-submenu">
						<div class="menu-item" id="subMenu-151">
							<a href="${pageContext.request.contextPath}/manage-driver-reports.do" class="menu-link">
								<span class="menu-text">Driver Reports</span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-153">
							<a href="${pageContext.request.contextPath}/manage-driver-duty-reports.do" class="menu-link">
								<span class="menu-text">Driver Duty Status</span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-155">
							<a href="${pageContext.request.contextPath}/manage-drivers-drive-reports.do" class="menu-link">
								<span class="menu-text">Drivers Drive Reports</span>
							</a>
						</div>
						<div class="menu-item" id="subMenu-157">
							<a href="${pageContext.request.contextPath}/manage-driver-subscription-package-reports.do" class="menu-link">
								<span class="menu-text">Subscription Package Reports</span>
							</a>
						</div>
						<%
							if (!role.equalsIgnoreCase(UserRoles.VENDOR_ROLE)) {
						%>	
							<div class="menu-item" id="subMenu-152">
								<a href="${pageContext.request.contextPath}/manage-refund-reports.do" class="menu-link">
									<span class="menu-text">Refund Reports</span>
								</a>
							</div>
							<div class="menu-item" id="subMenu-154">
								<a href="${pageContext.request.contextPath}/ccavenue/logs-reports.do" class="menu-link">
									<span class="menu-text">CCavenue Logs Report</span>
								</a>
							</div>
							<div class="menu-item" id="subMenu-156">
								<a href="${pageContext.request.contextPath}/driver/refer-benefit/reports.do" class="menu-link">
									<span class="menu-text">Drivers Refer Benefits Reports</span>
								</a>
							</div>
							<div class="menu-item" id="subMenu-158">
								<a href="${pageContext.request.contextPath}/manage-driver-transaction-history-reports.do" class="menu-link">
									<span class="menu-text">Driver Transaction History Reports</span>
								</a>
							</div>
							<div class="menu-item" id="subMenu-159">
								<a href="${pageContext.request.contextPath}/phonepe/logs-reports.do" class="menu-link">
									<span class="menu-text">PhonePe Logs Report</span>
								</a>
							</div>
						<% 
							}
						%>
					</div>
				</div>
			<%
				}
			%>	
		</div>
		<!-- END menu -->
		
	</div>
	<!-- END scrollbar -->
	
</div>
<!-- END #sidebar -->

<!-- BEGIN mobile-sidebar-backdrop -->
<button class="app-sidebar-mobile-backdrop" data-toggle-target=".app" data-toggle-class="app-sidebar-mobile-toggled"></button>
<!-- END mobile-sidebar-backdrop -->