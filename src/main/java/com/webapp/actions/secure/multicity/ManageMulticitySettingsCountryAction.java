package com.webapp.actions.secure.multicity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.utils.LoginUtils;
import com.webapp.actions.BusinessAction;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.UserProfileModel;

@Path("/manage-multicity-settings-country")
public class ManageMulticitySettingsCountryAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@QueryParam("searchString") String searchString) 
		throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);

		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		data.put("searchBooking", searchString);

		data.put("labelCarFare", messageForKeyAdmin("labelCarFare"));
		data.put("labelPayablePercentage", messageForKeyAdmin("labelPayablePercentage"));
		data.put("labelCancellationCharges", messageForKeyAdmin("labelCancellationCharges"));
		data.put("labelNearbyDrivers", messageForKeyAdmin("labelNearbyDrivers"));
		data.put("labelSMSSettings", messageForKeyAdmin("labelSMSSettings"));
		data.put("labelAboutUs", messageForKeyAdmin("labelAboutUs"));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy"));
		data.put("labelTermsAndConditions", messageForKeyAdmin("labelTermsAndConditions"));
		data.put("labelCopyright", messageForKeyAdmin("labelCopyright"));
		data.put("labelFreeWaitingTime", messageForKeyAdmin("labelFreeWaitingTime"));
		data.put("labelCancelTripTime", messageForKeyAdmin("labelCancelTripTime"));
		data.put("labelPromoCode", messageForKeyAdmin("labelPromoCode"));
		data.put("labelBenefits", messageForKeyAdmin("labelBenefits"));
		data.put("labelPayfortTest", messageForKeyAdmin("labelPayfortTest"));
		data.put("labelSenderBenefits", messageForKeyAdmin("labelSenderBenefits"));
		data.put("labelReceiverBenefits", messageForKeyAdmin("labelReceiverBenefits"));

		return loadView("/secure/multicity/manage-multicity-settings-country.jsp");
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getannouncementDetailsList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res
			) {
	//@formatter:on

		String sStart = req.getParameter("iDisplayStart");
		String length = req.getParameter("iDisplayLength");
		String order = req.getParameter("sSortDir_0");
		String globalSearchString = req.getParameter("more_data");

		List<MulticityCountryModel> multicityCountryModelList = null;

		int total = MulticityCountryModel.getMulticityCountryCount();

		int lengthh = Integer.parseInt(length);
		int start = Integer.parseInt(sStart);

		globalSearchString = globalSearchString.trim();

		multicityCountryModelList = MulticityCountryModel.getMulticityCountrySearch(start, lengthh, order, "%" + globalSearchString + "%");

		JSONObject jsonObject = new JSONObject();
		JSONArray outerJsonArray = new JSONArray();
		JSONArray innerJsonArray = null;

		int count = start;

		for (MulticityCountryModel multicityCountryModel : multicityCountryModelList) {

			count++;
			innerJsonArray = new JSONArray();

			innerJsonArray.put(multicityCountryModel.getMulticityCountryId());
			innerJsonArray.put(count);
			innerJsonArray.put(multicityCountryModel.getCountryName());
			innerJsonArray.put(multicityCountryModel.getCountryShortName());
			innerJsonArray.put(""); // paymentTypeModel.getPaymentType());
			innerJsonArray.put(multicityCountryModel.getCurrencySymbol());

			if (multicityCountryModel.isActive()) {
				innerJsonArray.put("Active");
			} else {
				innerJsonArray.put("Deactive");
			}

			innerJsonArray.put("<div class='activeDeactive' style='width:22px;' id='" + multicityCountryModel.getMulticityCountryId() + "'> <img src=" + req.getContextPath() + "/assets/image/delete_icon.png" + "></div>");

			outerJsonArray.put(innerJsonArray);
		}

		int filterCount = 0;

		if (globalSearchString != null && !globalSearchString.equals("")) {

			filterCount = multicityCountryModelList.size();

		} else {

			filterCount = total;
		}

		try {

			jsonObject.put("iTotalRecords", total);
			jsonObject.put("iTotalDisplayRecords", filterCount);
			jsonObject.put("aaData", outerJsonArray);

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return sendDataResponse(jsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/multicity/manage-multicity-settings-country.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}