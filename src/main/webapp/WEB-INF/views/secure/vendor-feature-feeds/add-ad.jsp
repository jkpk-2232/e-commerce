<!DOCTYPE html>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAddAD")%></title>
	</head>
	
	<body>
	
		<%@include file="/WEB-INF/views/includes/new-theme-header.jsp"%>
		
		<!-- BEGIN #content -->
		<div id="content" class="app-content">
		
			<%=NewThemeUiUtils.outputPageHeader(BusinessAction.messageForKeyAdmin("labelAddAD"), UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_ICON)%>
			
			<!-- BEGIN #formGrid -->
			<div id="formGrid" class="mb-5">
			
				<div class="card">
					
					<div class="card-body">
						
						<% 
							Map<String, String> sessionAttributesADFieldsLocal = SessionUtils.getSession(request, response, true);
							String roleIdLocal = SessionUtils.getAttributeValue(sessionAttributesADFieldsLocal, LoginUtils.ROLE_ID);
							
							if (UserRoleUtils.isSuperAdminAndAdminRole(roleIdLocal)) {
						%>
						
							<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.VENDOR_ID, BusinessAction.messageForKeyAdmin("labelVendorName"), true, 1, it, "col-sm-3", "col-sm-5")%>
							
						<% 
							} else {
						%>
							<%=NewThemeUiUtils.outputFormHiddenField(FieldConstants.VENDOR_ID, it)%>
						<%
							}
						%>
						
						<form method="POST" id="add-ad" name="add-ad" >
							
							<div class="row">
								<!-- <div class="col-xl-3">
									<label class="form-label" for="exampleFormControlInput1">AD Title</label>
									<input type="text" class="form-control" id="ad_title" placeholder="AD Title">
								</div> -->
								<div class="col-xl-3">
									<%=NewThemeUiUtils.outputInputField("ad_title", "AD Title", true, 1, 30, it, "text", null, "col-sm-10")%>
									<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.RESOLUTION, BusinessAction.messageForKeyAdmin("labelResolution"), true, 1, it, null, "col-sm-10")%>
								</div>
								
								<div class="col-xl-3">
									<%=NewThemeUiUtils.outputInputField("media_title", "Media Title", true, 1, 30, it, "text", null, "col-sm-10")%>
									<div>
											 <input type="file" id="uploadImageVideo" accept="image/*,video/*" >
											 <input type="hidden" id="hiddenUploadImageVideo" value=''>
											 <input type="hidden" id="hiddenUploadImageVideo_dummy" value=''>
	  											<!-- <button id="uploadButton">Upload</button> -->
										</div>
								</div>
								
								<div class="col-xl-6">
								
									<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.MEDIA_TYPE, BusinessAction.messageForKeyAdmin("labelMediaType"), true, 1, it, null, "col-sm-5")%>
									
									<input type="button" value="Add" data-toggle="tooltip" title="Save"  style='margin:5px;' class='btn btn-outline-theme'  id="addObject" />
									
									<input type="button" value="Cancel" data-toggle="tooltip" title="Cancel"  style='margin:5px;' class='btn btn-danger'  id="btnCancel" />
								</div>
							
							</div>
							
							<%=NewThemeUiUtils.outputFormHiddenField("adModel", it)%>
							
						</form>
						
						<%=NewThemeUiUtils.outputLinSepartor()%>
						
						<div id="formGrid" class="mb-5">
							
							<div class="card" id="showADList">
								
								<div class="card-body">
									<table id="dataTable" class="display">
										<thead>
											<tr>
												<th width="200" >AD Title</th>
												<th width="200" >Media Title</th>
												<th width="200" >Media Type</th>
												<!-- <th width="200" >Resolution</th> -->
												<th width="200" >File Name</th>
												<th width="200" >Duration</th>
												<th width="200" >Action</th>
											</tr>
										</thead>
										<tbody>
											<!-- Table body will be dynamically generated -->
										</tbody>
									</table>
								</div>
							
								<%=NewThemeUiUtils.outputCardBodyArrows()%>
							
							</div>
							
						</div>
							
					</div>
						
						<div id="showSave" >
							<input type="submit" data-toggle="tooltip" title="Save" style="margin:5px;" class="btn btn-outline-theme" value="Save"  id="saveAll" />
						</div>
					
						<%=NewThemeUiUtils.outputCardBodyArrows()%>
				</div>
			</div>
			<!-- END #formGrid -->
			
		</div>
		<!-- END #content -->
			
			<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
	</body>
</html>