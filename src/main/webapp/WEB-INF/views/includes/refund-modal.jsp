<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalRefund = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="refundModal">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelRefund")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputInputField(FieldConstants.AMOUNT_REFUNDED, BusinessAction.messageForKeyAdmin("labelRefundAmount"), false, 1, 30, itLocalRefund, "text", "col-sm-3", "col-sm-5")%>
				
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnRefundCancelModal" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnRefundProcessModal"><%=BusinessAction.messageForKeyAdmin("labelRefund")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->