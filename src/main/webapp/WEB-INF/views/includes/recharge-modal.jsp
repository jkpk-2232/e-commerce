<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalRecharge = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="accountRechargeModel">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelAccountRecharge")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputStaticField("dName", BusinessAction.messageForKeyAdmin("labelDriverName"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dEmail", BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dPhoneNumber", BusinessAction.messageForKeyAdmin("labelPhoneNumber"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dCurrentBalence", BusinessAction.messageForKeyAdmin("labelCurrentBalance"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dHoldBalence", BusinessAction.messageForKeyAdmin("labelHoldBalance"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dApprovedBalence", BusinessAction.messageForKeyAdmin("labelApprovedBalance"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dVendor", BusinessAction.messageForKeyAdmin("labelVendorName"), false, 1, 30, itLocalRecharge, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputInputField("rechargeAmount", BusinessAction.messageForKeyAdmin("labelAmount"), true, 1, 30, itLocalRecharge, "number", "col-sm-3", "col-sm-5")%>
				
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnARMCancel" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnARMRecharge"><%=BusinessAction.messageForKeyAdmin("labelRecharge")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->