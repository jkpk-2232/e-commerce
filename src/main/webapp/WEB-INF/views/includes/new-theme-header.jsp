<%@page import="com.utils.LoginUtils"%>
<%@page import="com.webapp.UrlConstants"%>
<%@page import="com.utils.myhub.WebappPropertyUtils"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalThemeHeader = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #app -->
<div id="app" class="app <%=WebappPropertyUtils.NEW_THEME_FOOTER_DISPLAY_APP_CSS%>">
	
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CANCEL_PAGE_REDIRECT_URL, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DATATABLE_FORMAT_DATE, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DATATABLE_MIN_DATE, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.DATE_FORMAT, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LAT, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.CURRENT_LNG, itLocalThemeHeader)%>
	<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.KM_UNITS, itLocalThemeHeader)%>
	
	<!-- BEGIN #header -->
	<div id="header" class="app-header">
		
		<!-- BEGIN desktop-toggler -->
		
		<div class="desktop-toggler">
			<button type="button" class="menu-toggler" data-toggle-class="app-sidebar-collapsed" data-dismiss-class="app-sidebar-toggled" data-toggle-target=".app">
				<span class="bar"></span>
				<span class="bar"></span>
				<span class="bar"></span>
			</button>
		</div>
		<!-- BEGIN desktop-toggler -->
		
		<!-- BEGIN mobile-toggler -->
		<div class="mobile-toggler">
			<button type="button" class="menu-toggler" data-toggle-class="app-sidebar-mobile-toggled" data-toggle-target=".app">
				<span class="bar"></span>
				<span class="bar"></span>
				<span class="bar"></span>
			</button>
		</div>
		<!-- END mobile-toggler -->
		
		<!-- BEGIN brand -->
		<div class="brand">
			<a href="#" class="brand-logo">
				<span class="brand-img">
					<span class="brand-img-text text-theme">MH</span>
				</span>
				<span class="brand-text"><%=WebappPropertyUtils.PROJECT_NAME%></span>
			</a>
		</div>
		<!-- END brand -->
		
		<!-- BEGIN menu -->
		<div class="menu">
		
			<div class="menu-item dropdown dropdown-mobile-full">
		
				<a href="#" data-bs-toggle="dropdown" data-bs-display="static" class="menu-link">
		
					<div class="menu-img online">
					
						<% 
							
							Map<String, String> sessionAttributesHeadersLocal = SessionUtils.getSession(request, response, true);
							String roleIdHeadersLocal = SessionUtils.getAttributeValue(sessionAttributesHeadersLocal, LoginUtils.ROLE_ID);
							
							if (UserRoleUtils.isVendorRole(roleIdHeadersLocal)) {
								
								String photoUrl = itLocalThemeHeader.containsKey(LoginUtils.PHOTO_URL) ? itLocalThemeHeader.get(LoginUtils.PHOTO_URL).toString() : ProjectConstants.DEFAULT_IMAGE_ADMIN;
								
						%>
							<img src="${pageContext.request.contextPath}<%=photoUrl%>" alt="Profile" height="60">
						<% 
							} else {
						%>
							<img src="${pageContext.request.contextPath}<%=ProjectConstants.DEFAULT_IMAGE_ADMIN%>" alt="Profile" height="60">
						<% 
							}
				        %>
				        
					</div>
		
					<div class="menu-text d-sm-block d-none w-170px">
						<%=itLocalThemeHeader.get(LoginUtils.USER_FULL_NAME)%>
					</div>
		
				</a>
		
				<div class="dropdown-menu dropdown-menu-end me-lg-3 fs-11px mt-1">
				
					<% 
						if (UserRoleUtils.isVendorRole(roleIdHeadersLocal)) {
					%>	
						<a class="dropdown-item d-flex align-items-center" href="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.VENDOR_PROFILE_URL%>">
					<%
						} else {
					%>
						<a class="dropdown-item d-flex align-items-center" href="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.ADMIN_PROFILE_URL%>">	
					<%
						}
					%>
						<%=BusinessAction.messageForKeyAdmin("labelProfile")%>
						<i class="bi bi-person-circle ms-auto text-theme fs-16px my-n1"></i>
					</a>
				
					<div class="dropdown-divider"></div>
				
					<a class="dropdown-item d-flex align-items-center" href="${pageContext.request.contextPath}<%=UrlConstants.PAGE_URLS.LOGOUT_URL%>">
						<%=BusinessAction.messageForKeyAdmin("labelLogout")%>
						<i class="bi bi-toggle-off ms-auto text-theme fs-16px my-n1"></i>
					</a>
					
				</div>
				
			</div>
			
		</div>
		<!-- END menu -->
		
	</div>
	<!-- END #header -->
	
	<%@include file="/WEB-INF/views/includes/new-theme-sidebar.jsp"%>