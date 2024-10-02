<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>

<%
	HashMap<String, String> itLocalEncashRequest = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="encashRequestModel">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelEncash")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputStaticField("dName1", BusinessAction.messageForKeyAdmin("labelDriverName"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dEmail1", BusinessAction.messageForKeyAdmin("labelEmail"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dPhoneNumber1", BusinessAction.messageForKeyAdmin("labelPhoneNumber"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dCurrentBalence1", BusinessAction.messageForKeyAdmin("labelCurrentBalance"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dHoldBalence1", BusinessAction.messageForKeyAdmin("labelHoldBalance"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dApprovedBalence1", BusinessAction.messageForKeyAdmin("labelApprovedBalance"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField("dVendor1", BusinessAction.messageForKeyAdmin("labelVendorName"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputSelectInputField(FieldConstants.ENCASH_REQUEST_STATUS, BusinessAction.messageForKeyAdmin("labelStatus"), true, 1, itLocalEncashRequest, "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputInputField("requestedAmount", BusinessAction.messageForKeyAdmin("labelAmount"), true, 1, 30, itLocalEncashRequest, "number", "col-sm-3", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputInputField("encashRemark", BusinessAction.messageForKeyAdmin("labelRemark"), false, 1, 30, itLocalEncashRequest, "text", "col-sm-3", "col-sm-5")%>
				
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnEncashModelCancel" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
				<button type="button" class="btn btn-outline-theme" id="btnEncashModelEncash"><%=BusinessAction.messageForKeyAdmin("labelEncash")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->