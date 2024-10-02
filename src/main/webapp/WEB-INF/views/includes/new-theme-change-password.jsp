<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalChangePassword = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="changePasswordModal">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelChangePassword")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputInputField(FieldConstants.OLD_PASSWORD, BusinessAction.messageForKeyAdmin("labelOldPassword"), true, 1, 30, itLocalChangePassword, "password", "col-sm-10", "col-sm-10")%>
				
				<%=NewThemeUiUtils.outputInputField(FieldConstants.NEW_PASSWORD, BusinessAction.messageForKeyAdmin("labelNewPassword"), true, 1, 30, itLocalChangePassword, "password", "col-sm-10", "col-sm-10")%>
				
				<%=NewThemeUiUtils.outputInputField(FieldConstants.CONFIRM_PASSWORD, BusinessAction.messageForKeyAdmin("labelConfirmPassword"), true, 1, 30, itLocalChangePassword, "password", "col-sm-10", "col-sm-10")%>

			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnCancelPopUp" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnSubmitPopUp"><%=BusinessAction.messageForKeyAdmin("labelSave")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->