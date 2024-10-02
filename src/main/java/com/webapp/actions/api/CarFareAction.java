package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.InvoiceModel;

@Path("/api/car-fare")
public class CarFareAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<CarFareModel> carTypeLists = new ArrayList<CarFareModel>();

		InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(loggedInuserId);
		boolean isPaymentPaid = true;

		if (invoiceModel != null) {

			if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

				isPaymentPaid = false;
			}
		}

		for (CarFareModel carfareModel : CarFareModel.getCarFare(ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {

			carfareModel.setPaymentPaid(isPaymentPaid);

			carfareModel.setCancellationCharges(CancellationChargeModel.getAdminCancellationCharges().getCharge());
			carTypeLists.add(carfareModel);
		}

		return sendDataResponse(carTypeLists);
	}

}