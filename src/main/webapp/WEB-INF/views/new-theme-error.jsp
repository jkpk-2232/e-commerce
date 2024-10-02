<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

	<head>
		<%@include file="/WEB-INF/views/includes/new-theme-common-head.jsp"%>
		<title><%=BusinessAction.messageForKeyAdmin("labelAccessDenied")%></title>
	</head>

	<body class='pace-top'>
	
		<!-- BEGIN #app -->
	
		<div id="app" class="app app-full-height app-without-header">
	
			<!-- BEGIN error -->
			<div class="error-page">
				<!-- BEGIN error-page-content -->
				<div class="error-page-content">
					<div class="card mb-5 mx-auto" style="max-width: 320px;">
						<div class="card-body">
							<div class="card">
								<div class="error-code">404</div>
								<div class="card-arrow">
									<div class="card-arrow-top-left"></div>
									<div class="card-arrow-top-right"></div>
									<div class="card-arrow-bottom-left"></div>
									<div class="card-arrow-bottom-right"></div>
								</div>
							</div>
						</div>
						<div class="card-arrow">
							<div class="card-arrow-top-left"></div>
							<div class="card-arrow-top-right"></div>
							<div class="card-arrow-bottom-left"></div>
							<div class="card-arrow-bottom-right"></div>
						</div>
					</div>
					<h1>Oops!</h1> 
					<h3>We can't seem to find the page you're looking for</h3>
					<h3><%=BusinessAction.messageForKeyAdmin("labelAccessDenied")%></h3>
					<hr>
					<a href="javascript:window.history.back();" class="btn btn-outline-theme px-3 rounded-pill"><i class="fa fa-arrow-left me-1 ms-n1"></i> Go Back</a>
				</div>
				<!-- END error-page-content -->
				
			</div>
			<!-- END error -->
	
			<%@include file="/WEB-INF/views/includes/new-theme-change-theme.jsp"%>
			
		</div>
		<!-- END #app -->
		
		<%@include file="/WEB-INF/views/includes/new-theme-footer.jsp"%>
		
	</body>
	
</html>
