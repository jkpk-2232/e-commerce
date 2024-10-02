package com.webapp.actions.secure.rentalpackage;

import java.io.IOException;
import java.sql.SQLException;
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
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.RentalPackageModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-rental-packages")
public class ManageRentalPackagesAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadRentalPackages(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String vendorIdOptions = DropDownUtils.getUserAccessWiseVendorList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String rentalPackageTypeOptions = DropDownUtils.getRentalPackageTypeOptions(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, true);
		data.put(FieldConstants.RENTAL_PACKAGE_TYPE_OPTIONS, rentalPackageTypeOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_RENTAL_PACKAGE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_RENTAL_PACKAGE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadRentalPackagesList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		String regionList = dtu.getRequestParameter(FieldConstants.REGION_LIST);
		String rentalPackageType = dtu.getRequestParameter(FieldConstants.RENTAL_PACKAGE_TYPE);
		rentalPackageType = DropDownUtils.parserForAllOptions(rentalPackageType);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		String[] vendorIds = DropDownUtils.getUserAccessWiseVendorListDatatable(vendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));
		List<String> regionIds = DropDownUtils.getUserAccessWiseRegionListDatatable(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		int total = RentalPackageModel.getTotalRentalPackagesCount(regionIds, rentalPackageType, vendorIds);
		List<RentalPackageModel> rentalPackageModelList = RentalPackageModel.getRentalPackageListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), regionIds, rentalPackageType, vendorIds);

		int count = dtu.getStartInt();

		for (RentalPackageModel rentalPackageModel : rentalPackageModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(rentalPackageModel.getRentalPackageId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(rentalPackageModel.getPackageTime() + " hours, " + ((rentalPackageModel.getPackageDistance()) / adminSettingsModel.getDistanceUnits()) + " km");

			if (ProjectConstants.RENTAL_INTERCITY_ID.equals(rentalPackageModel.getRentalPackageType())) {
				dtuInnerJsonArray.put(ProjectConstants.RENTAL_INTERCITY_STRING);
			} else if (ProjectConstants.RENTAL_OUTSTATION_ID.equals(rentalPackageModel.getRentalPackageType())) {
				dtuInnerJsonArray.put(ProjectConstants.RENTAL_OUTSTATION_STRING);
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			List<RentalPackageFareModel> rentalPackageFareModelList = RentalPackageFareModel.getRentalPackageFareListByRentalPackageId(rentalPackageModel.getRentalPackageId());

			double secondBaseFare = 0;
			double thirdBaseFare = 0;
			double fourthBaseFare = 0;
			double fifthBaseFare = 0;

			for (RentalPackageFareModel rentalPackageFareModel : rentalPackageFareModelList) {

				if (ProjectConstants.Second_Vehicle_ID.equals(rentalPackageFareModel.getCarTypeId())) {

					secondBaseFare = rentalPackageFareModel.getBaseFare();

				} else if (ProjectConstants.Third_Vehicle_ID.equals(rentalPackageFareModel.getCarTypeId())) {

					thirdBaseFare = rentalPackageFareModel.getBaseFare();

				} else if (ProjectConstants.Fourth_Vehicle_ID.equals(rentalPackageFareModel.getCarTypeId())) {

					fourthBaseFare = rentalPackageFareModel.getBaseFare();

				} else if (ProjectConstants.Fifth_Vehicle_ID.equals(rentalPackageFareModel.getCarTypeId())) {

					fifthBaseFare = rentalPackageFareModel.getBaseFare();
				}
			}

			dtuInnerJsonArray.put(fifthBaseFare > 0 ? fifthBaseFare : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(secondBaseFare > 0 ? secondBaseFare : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(thirdBaseFare > 0 ? thirdBaseFare : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(fourthBaseFare > 0 ? fourthBaseFare : ProjectConstants.NOT_AVAILABLE);

			dtuInnerJsonArray.put(rentalPackageModel.getRegionName());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(rentalPackageModel.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW));

			if (rentalPackageModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_RENTAL_PACKAGE_URL + "?rentalPackageId=" + rentalPackageModel.getRentalPackageId())));

			if (rentalPackageModel.isActive()) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_ACTIVATE_DEACTIVATE_URL + "?rentalPackageId=" + rentalPackageModel.getRentalPackageId() + "&status=active")));

			} else {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_ACTIVATE_DEACTIVATE_URL + "?rentalPackageId=" + rentalPackageModel.getRentalPackageId() + "&status=deactive")));

			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_DELETE_URL + "?rentalPackageId=" + rentalPackageModel.getRentalPackageId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? rentalPackageModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateRental(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.RENTAL_PACKAGE_ID) String rentalPackageId,
		@QueryParam(FieldConstants.STATUS) String status
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		RentalPackageModel rentalPackageModel = new RentalPackageModel();

		rentalPackageModel.setRentalPackageId(rentalPackageId);

		if ("active".equals(status)) {
			rentalPackageModel.setActive(false);
		} else {
			rentalPackageModel.setActive(true);
		}

		rentalPackageModel.activeDeactiveRentalPackage(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_URL);
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteRental(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.RENTAL_PACKAGE_ID) String rentalPackageId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		RentalPackageModel rentalPackageModel = new RentalPackageModel();
		rentalPackageModel.setRentalPackageId(rentalPackageId);
		rentalPackageModel.setActive(false);
		rentalPackageModel.setRecordStatus(ProjectConstants.DEACTIVATE_STATUS);
		rentalPackageModel.deleteRentalPackage(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_RENTAL_PACKAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}