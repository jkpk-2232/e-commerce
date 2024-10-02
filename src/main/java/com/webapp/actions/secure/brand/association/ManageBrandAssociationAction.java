package com.webapp.actions.secure.brand.association;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.secure.vendor.stores.ManageVendorStoreAction;
import com.webapp.models.VendorStoreModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-brand-association")
public class ManageBrandAssociationAction extends BusinessAction {
	
	public static Logger logger = Logger.getLogger(ManageBrandAssociationAction.class);

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getBrandAssociation(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);

			data.put(FieldConstants.VENDOR_ID, vendorId);
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_BRAND_ASSOCIATION_URL + "?vendorId=" + vendorId);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put(FieldConstants.VENDOR_ID, vendorId);
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_BRAND_ASSOCIATION_URL + "?vendorId=" + vendorId);
		}

		return loadView(UrlConstants.JSP_URLS.MANAGE_BRAND_ASSOCIATION_JSP);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoresList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		/*
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		}
		
		List<String> vendorStoreIdList = new ArrayList<>();

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loginSessionMap.get(LoginUtils.USER_ID), true);

			// to avoid query error
			if (vendorStoreIdList.isEmpty()) {
				vendorStoreIdList.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			}

		} else {
			vendorStoreIdList = null;
		}

		*/
		int total = VendorStoreModel.getVendorStoreCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, null);
		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), vendorId, null);

		int count = dtu.getStartInt();

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorStoreModel.getVendorStoreId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorStoreModel.getVendorBrandName());
			dtuInnerJsonArray.put(vendorStoreModel.getStoreName() != null ? vendorStoreModel.getStoreName() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(vendorStoreModel.getStoreAddress());

			if (vendorStoreModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			// Store can be edited only by admin and super admin
			if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_BRAND_ASSOCIATION_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId())));
			}

			// no store activate or deactivate access to sub vendor
			if (!UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				if (vendorStoreModel.isActive()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_ACTIVATE_DEACTIVATE_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId() + "&currentStatus=active")));
				} else {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_ACTIVATE_DEACTIVATE_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId() + "&currentStatus=deactive")));
				}
			}
			
			if (btnGroupStr.length() <= 0) {
				btnGroupStr.append(messageForKey("labelAccessDenied"));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = VendorStoreModel.getVendorStoreSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, null);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateVendorStore(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);

		if ("active".equals(currentStatus)) {
			vendorStoreModel.setActive(false);
			vendorStoreModel.setDeleted(true);
		} else {
			vendorStoreModel.setActive(true);
			vendorStoreModel.setDeleted(false);
		}

		vendorStoreModel.updateVendorStoreStatus(loginSessionMap.get(LoginUtils.USER_ID));

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL + "?vendorId=" + vendorStoreModel.getVendorId());
		}
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_BRAND_ASSOCIATION_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
