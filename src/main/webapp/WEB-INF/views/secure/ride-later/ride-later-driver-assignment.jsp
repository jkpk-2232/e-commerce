<%@page import="java.util.HashMap"%>
<%@page import="com.webapp.FieldConstants"%>
<%@page import="com.webapp.actions.BusinessAction"%>
<%@page import="com.webapp.viewutils.NewThemeUiUtils"%>
<%@page import="java.util.List,java.util.ArrayList"%>

<%
	HashMap<String, String> itLocalRideLaterDriverAssignment = (HashMap) request.getAttribute("it");
%>

<!-- BEGIN #modalEdit -->
<div class="modal fade" id="rideLaterDriverAssignmentModal">

	<div class="modal-dialog modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title"><%=BusinessAction.messageForKeyAdmin("labelAssignDriver")%></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>

			<div class="modal-body">
			
				<%=NewThemeUiUtils.outputStaticField(BusinessAction.messageForKeyAdmin("labelSourceAddress"), "sourceAddressSpan", "col-sm-5", "col-sm-5")%>
				
				<%=NewThemeUiUtils.outputStaticField(BusinessAction.messageForKeyAdmin("labelDestinationAddress"), "destinationAddressSpan", "col-sm-5", "col-sm-5")%>
			
				<% 
					List<String> columnNamesRideLaterDriverAssignment = new ArrayList<>();
					columnNamesRideLaterDriverAssignment.add(BusinessAction.messageForKeyAdmin("labelId"));
					columnNamesRideLaterDriverAssignment.add(BusinessAction.messageForKeyAdmin("labelSr.No."));
					columnNamesRideLaterDriverAssignment.add(BusinessAction.messageForKeyAdmin("labelDriverName"));
					columnNamesRideLaterDriverAssignment.add(BusinessAction.messageForKeyAdmin("labelCurrentStatus"));
					columnNamesRideLaterDriverAssignment.add(BusinessAction.messageForKeyAdmin("labelAction"));
				%>
				
				<%=NewThemeUiUtils.outputDatatable("datatableRideLaterDriverAssignment", "", columnNamesRideLaterDriverAssignment)%>

			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-outline-default" id="btnCancelPopUp" data-bs-dismiss="modal"><%=BusinessAction.messageForKeyAdmin("labelClose")%></button>
			</div>

		</div>

	</div>

</div>
<!-- END #modalEdit -->