package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminFaqModel;
import com.webapp.models.UrlAccessesModel;

@Path("/manage-admin-faq")
public class ManageAdminFaqAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("searchString") String searchString
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		data.put("searchBooking", searchString);

		data.put("labelQuestion", messageForKeyAdmin("labelQuestion"));
		data.put("labelAnswer", messageForKeyAdmin("labelAnswer"));
		data.put("labelAddFaq", messageForKeyAdmin("labelAddFaq"));
		data.put("labelAdminFAQ", messageForKeyAdmin("labelAdminFAQ"));
		data.put("InfoThisInformationIsAvailableOnWebsite", messageForKeyAdmin("InfoThisInformationIsAvailableOnWebsite"));
		data.put("labelCancel", messageForKeyAdmin("labelCancel"));
		data.put("labelSave", messageForKeyAdmin("labelSave"));

		data.put("labelSearch", messageForKeyAdmin("labelSearch"));
		data.put("labelSrNo", messageForKeyAdmin("labelSrNo"));
		data.put("labelAction", messageForKeyAdmin("labelAction"));

		data.put("labelCarFare", messageForKeyAdmin("labelCarFare"));
		data.put("labelPayablePercentage", messageForKeyAdmin("labelPayablePercentage"));
		data.put("labelCancellationCharges", messageForKeyAdmin("labelCancellationCharges"));
		data.put("labelNearbyDrivers", messageForKeyAdmin("labelNearbyDrivers"));
		data.put("labelSMSSettings", messageForKeyAdmin("labelSMSSettings"));
		data.put("labelAboutUs", messageForKeyAdmin("labelAboutUs"));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy"));
		data.put("labelTermsAndConditions", messageForKeyAdmin("labelTermsAndConditions"));
		data.put("labelLocations", messageForKeyAdmin("labelLocations"));
		data.put("labelContactUs", messageForKeyAdmin("labelContactUs"));
		data.put("labelOK", messageForKeyAdmin("labelOK"));

		return loadView("/secure/settings/manage-admin-faq.jsp");
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postSettings(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@FormParam("question") String question,
			@FormParam("answer") String answer
			) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		if (!UrlAccessesModel.hasUserUrlAccess(UrlConstants.PAGE_URLS.ADMIN_BOOKING_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		data.put("question", question);
		data.put("answer", answer);

		data.put("labelQuestion", messageForKeyAdmin("labelQuestion"));
		data.put("labelAnswer", messageForKeyAdmin("labelAnswer"));
		data.put("labelAddFaq", messageForKeyAdmin("labelAddFaq"));
		data.put("labelAdminFAQ", messageForKeyAdmin("labelAdminFAQ"));
		data.put("InfoThisInformationIsAvailableOnWebsite", messageForKeyAdmin("InfoThisInformationIsAvailableOnWebsite"));
		data.put("labelCancel", messageForKeyAdmin("labelCancel"));
		data.put("labelSave", messageForKeyAdmin("labelSave"));

		data.put("labelSearch", messageForKeyAdmin("labelSearch"));
		data.put("labelSrNo", messageForKeyAdmin("labelSrNo"));
		data.put("labelAction", messageForKeyAdmin("labelAction"));

		data.put("labelCarFare", messageForKeyAdmin("labelCarFare"));
		data.put("labelPayablePercentage", messageForKeyAdmin("labelPayablePercentage"));
		data.put("labelCancellationCharges", messageForKeyAdmin("labelCancellationCharges"));
		data.put("labelNearbyDrivers", messageForKeyAdmin("labelNearbyDrivers"));
		data.put("labelSMSSettings", messageForKeyAdmin("labelSMSSettings"));
		data.put("labelAboutUs", messageForKeyAdmin("labelAboutUs"));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy"));
		data.put("labelTermsAndConditions", messageForKeyAdmin("labelTermsAndConditions"));
		data.put("labelLocations", messageForKeyAdmin("labelLocations"));
		data.put("labelContactUs", messageForKeyAdmin("labelContactUs"));
		data.put("labelOK", messageForKeyAdmin("labelOK"));

		boolean hasErrors = false;

		hasErrors = hasErrorsForEnglish();

		if (hasErrors) {
			return loadView("/secure/settings/manage-admin-faq.jsp");
		} else {

			AdminFaqModel adminFaqModel = new AdminFaqModel();

			adminFaqModel.setQuestion(question);
			adminFaqModel.setAnswer(answer);

			adminFaqModel.addAdminFaq(loggedInUserId);

			return redirectToPage("/manage-admin-faq.do");
		}
	}

	@Path("/edit")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateCredits(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@FormParam("question") String question,
			@FormParam("answer") String answer,
			@FormParam("adminFaqId") String adminFaqId
			) throws IOException, SQLException {
	//@formatter:on

		Map<String, String> outputMap = new HashMap<String, String>();

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		if (!UrlAccessesModel.hasUserUrlAccess(UrlConstants.PAGE_URLS.ADMIN_BOOKING_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		AdminFaqModel adminFaqModel = AdminFaqModel.getAdminFaqModelById(adminFaqId);

		adminFaqModel.setQuestion(question);
		adminFaqModel.setAnswer(answer);

		int status = adminFaqModel.editAdminFaq(loggedInUserId);

		if (status > 0) {
			outputMap.put("type", "SUCCESS");
			outputMap.put("message", messageForKeyAdmin("successFAQUpdatedSuccess"));
		} else {
			outputMap.put("type", "ERROR");
			outputMap.put("message", messageForKeyAdmin("erroeFAQFailedToUpdate"));
		}

		return sendDataResponse(outputMap);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminFAQList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res
			) {
	//@formatter:on

		String sStart = req.getParameter("iDisplayStart");
		String length = req.getParameter("iDisplayLength");
		String order = req.getParameter("sSortDir_0");
		String globalSearchString = req.getParameter("more_data");

		List<AdminFaqModel> adminFaqModelList = null;

		int total = AdminFaqModel.getAdminFaqCount();

		int lengthh = Integer.parseInt(length);
		int start = Integer.parseInt(sStart);

		globalSearchString = globalSearchString.trim();

		adminFaqModelList = AdminFaqModel.getAdminFaqListForSearch(start, lengthh, order, "%" + globalSearchString + "%");

		JSONObject jsonObject = new JSONObject();
		JSONArray outerJsonArray = new JSONArray();
		JSONArray innerJsonArray = null;

		int count = start;

		for (AdminFaqModel adminFaqModel : adminFaqModelList) {

			count++;
			innerJsonArray = new JSONArray();

			innerJsonArray.put(adminFaqModel.getAdminFaqId());
			innerJsonArray.put(count);
			innerJsonArray.put(MyHubUtils.getTrimmedTo(adminFaqModel.getQuestion(), 150));
			innerJsonArray.put(MyHubUtils.getTrimmedTo(adminFaqModel.getAnswer(), 150));
			//@formatter:off
			innerJsonArray.put(
						"<div style='float:left;margin-right:10px;' class='editAdminFaq' style='width:22px;' id='" + adminFaqModel.getAdminFaqId() + "'> <img src=" + req.getContextPath() + "/assets/image/edit_icon.png" + "></div>" +
						"<div style='margin-left: 34%;' class='deleteAdminFaq' style='width:22px;' id='" + adminFaqModel.getAdminFaqId() + "'> <img src=" + req.getContextPath() + "/assets/image/delete_icon.png" + "></div>"
					  );
			//@formatter:on

			outerJsonArray.put(innerJsonArray);
		}

		int filterCount = 0;

		if (globalSearchString != null && !globalSearchString.equals("")) {

			filterCount = AdminFaqModel.getTotalAdminFaqCountBySearch("%" + globalSearchString + "%");

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

	@Path("/admin-faq-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminFAQInfo(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("adminFaqId") String adminFaqId
			) throws IOException, SQLException {
	//@formatter:on

		AdminFaqModel adminFaqModel = AdminFaqModel.getAdminFaqModelById(adminFaqId);

		Map<String, Object> outputMap = new HashMap<String, Object>();

		if (adminFaqModel != null) {

			outputMap.put("type", "SUCCESS");
			outputMap.put("adminFaqId", adminFaqModel.getAdminFaqId());
			outputMap.put("question", adminFaqModel.getQuestion());
			outputMap.put("answer", adminFaqModel.getAnswer());

		} else {

			outputMap.put("type", "ERROR");
			outputMap.put("message", messageForKeyAdmin("errorNoFAQFound"));
		}

		return sendDataResponse(outputMap);
	}

	@Path("/delete-faq")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteFaq(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("adminFaqId") String adminFaqId
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		AdminFaqModel adminFaqModel = new AdminFaqModel();

		adminFaqModel.setAdminFaqId(adminFaqId);

		adminFaqModel.deleteAdminFaq(loggedInUserId);

		return redirectToPage("/manage-admin-faq.do");
	}

	public boolean hasErrorsForEnglish() {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping("question", "Question", new RequiredValidationRule());
		validator.addValidationMapping("answer", "Answer", new RequiredValidationRule());
		validator.addValidationMapping("question", "Question", new MaxLengthValidationRule(4500));
		validator.addValidationMapping("answer", "Answer", new MaxLengthValidationRule(4500));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs//settings/manage-admin-faq.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}