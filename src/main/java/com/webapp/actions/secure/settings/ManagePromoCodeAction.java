package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-promo-code")
public class ManagePromoCodeAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getPromoCode(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(true, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);

			String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(true, assignedRegionList, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_PROMO_CODE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PROMO_CODE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPromoCodeList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		DatatableUtils dtu = new DatatableUtils(request);

		String serviceTypeId = null;
		String vendorId = null;

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			serviceTypeId = dtu.getRequestParameter(FieldConstants.SERVICE_TYPE_ID);
			vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		} else if(UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))){
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceTypeId = vscm.getServiceTypeId();
		}

		double maxDiscountSearch = -1;
		try {
			maxDiscountSearch = Double.parseDouble(dtu.getGlobalSearchString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		serviceTypeId = DropDownUtils.parserForAllOptions(serviceTypeId);
		vendorId = DropDownUtils.parserForAllOptions(vendorId);
		
		int total = 0;
		List<PromoCodeModel> promoCodeList = null;
		
		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			total = PromoCodeModel.getPromoCodeCount(dtu.getStartDatelong(), dtu.getEndDatelong(), "-1", vendorId, assignedRegionList);
			promoCodeList = PromoCodeModel.getPromoCodeListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), "-1", vendorId, assignedRegionList,
						maxDiscountSearch);
		} else {
			total = PromoCodeModel.getPromoCodeCount(dtu.getStartDatelong(), dtu.getEndDatelong(), serviceTypeId, vendorId, assignedRegionList);
			promoCodeList = PromoCodeModel.getPromoCodeListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), serviceTypeId, vendorId, assignedRegionList,
						maxDiscountSearch);
		}

		

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (PromoCodeModel promoCode : promoCodeList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(promoCode.getPromoCodeId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(promoCode.getPromoCode());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(promoCode.getStartDate(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(promoCode.getEndDate(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			if (promoCode.getMode().equalsIgnoreCase(ProjectConstants.PERCENTAGE_ID)) {
				dtuInnerJsonArray.put(ProjectConstants.PERCENTAGE_TEXT);
			} else {
				dtuInnerJsonArray.put(ProjectConstants.AMOUNT_TEXT);
			}

			dtuInnerJsonArray.put(BusinessAction.df.format(promoCode.getDiscount()));
			dtuInnerJsonArray.put(BusinessAction.df.format(promoCode.getMaxDiscount()));

			dtuInnerJsonArray.put(promoCode.getServiceTypeName() != null ? promoCode.getServiceTypeName() : ProjectConstants.ALL_TEXT);
			dtuInnerJsonArray.put(promoCode.getVendorName() != null ? promoCode.getVendorName() : ProjectConstants.ALL_TEXT);

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_PROMO_CODE_URL + "?promoCodeId=" + promoCode.getPromoCodeId())));

			if (promoCode.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.PROMO_CODE_DEACTIVATE_URL + "?promoCodeId=" + promoCode.getPromoCodeId())));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.PROMO_CODE_ACTIVATE_URL + "?promoCodeId=" + promoCode.getPromoCodeId())));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.PROMO_CODE_DELETE_URL + "?promoCodeId=" + promoCode.getPromoCodeId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = PromoCodeModel.getTotalPromoCodeCountBySearch(dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), serviceTypeId, vendorId, assignedRegionList, maxDiscountSearch);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/activate-promo-code")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response activatePromoCode(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PROMO_CODE_ID) String promoCodeId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		PromoCodeModel promoCodeModel = new PromoCodeModel();
		promoCodeModel.setPromoCodeId(promoCodeId);
		promoCodeModel.activatePromoCode(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
	}

	@Path("/deactivate-promo-code")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deactivatePromoCode(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PROMO_CODE_ID) String promoCodeId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		PromoCodeModel promoCodeModel = new PromoCodeModel();
		promoCodeModel.setPromoCodeId(promoCodeId);
		promoCodeModel.deactivatePromoCode(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
	}

	@Path("/delete-promo-code")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deletePromoCode(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PROMO_CODE_ID) String promoCodeId
		) throws ServletException, IOException {
	//@formatter:on
		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		PromoCodeModel promoCodeModel = new PromoCodeModel();
		promoCodeModel.setPromoCodeId(promoCodeId);
		promoCodeModel.deletePromoCode(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PROMO_CODE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}