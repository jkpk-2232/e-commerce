<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalTransferReject = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="transferRejectEncashModel">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelEncash")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputStaticField(FieldConstants.FIRST_NAME, BusinessAction.messageForKeyAdmin("labelName"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField(FieldConstants.EMAIL, BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField(FieldConstants.PHONE_NUMBER, BusinessAction.messageForKeyAdmin("labelPhoneNumber"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField(FieldConstants.REQUESTED_AMOUNT, BusinessAction.messageForKeyAdmin("labelCurrentBalance"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField(FieldConstants.REMAINING_BALANCE, BusinessAction.messageForKeyAdmin("labelRemainingBalance"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputInputField(FieldConstants.ENCASH_REQUEST_REMARK, BusinessAction.messageForKeyAdmin("labelRemark"), false, 1, 30, itLocalTransferReject, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputFormHiddenField("encashRequestIdHidden", itLocalTransferReject)%>
				
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnTRECancel" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnTRETransfer"><%=BusinessAction.messageForKeyAdmin("labelTransfer")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnTREReject"><%=BusinessAction.messageForKeyAdmin("labelReject")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->