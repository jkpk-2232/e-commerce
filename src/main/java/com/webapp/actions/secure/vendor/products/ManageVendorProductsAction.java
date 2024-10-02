package com.webapp.actions.secure.vendor.products;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-vendor-products")
public class ManageVendorProductsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorProducts(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(null, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
			
		} else {
			String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

			String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);
		}	

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_PRODUCTS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_PRODUCT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);
		serviceId = DropDownUtils.parserForAllOptions(serviceId);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		
		String vendorStoreId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);
		//System.out.println("*** vendor out side if store id ****"+vendorStoreId);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceId = vscm != null ? vscm.getServiceId() : null;
			
		} else {
			if ("0".equals(vendorId)) {
				vendorStoreId = null;
			}
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
			vendorStoreId = DropDownUtils.parserForAllOptions(vendorStoreId);
			if (vendorStoreId == null) {
				vendorStoreId = null;
			}
		}

		int total = VendorProductModel.getVendorProductsCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, serviceId, null, vendorStoreId);
		List<VendorProductModel> vendorProductList = VendorProductModel.getVendorProductsSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), null, vendorId, serviceId, null,
					"vp.updated_at DESC", vendorStoreId);

		int count = dtu.getStartInt();

		for (VendorProductModel vendorProductModel : vendorProductList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorProductModel.getVendorProductId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorProductModel.getProductCategory());
			dtuInnerJsonArray.put(vendorProductModel.getProductName());
			dtuInnerJsonArray.put(vendorProductModel.getVendorName());
			dtuInnerJsonArray.put(vendorProductModel.getStoreName());
			dtuInnerJsonArray.put(df.format(vendorProductModel.getProductActualPrice()));
			dtuInnerJsonArray.put(df.format(vendorProductModel.getProductDiscountedPrice()));
			dtuInnerJsonArray.put(df.format(vendorProductModel.getProductWeight()) + OrderUtils.getProductWeightUnit(vendorProductModel.getProductWeightUnit()));
			dtuInnerJsonArray.put(df.format(vendorProductModel.getProductInventoryCount()));

			if (vendorProductModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_VENDOR_PRODUCTS_URL + "?vendorProductId=" + vendorProductModel.getVendorProductId())));

			if (vendorProductModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_ACTIVE_DEACTIVE_URL + "?vendorProductId=" + vendorProductModel.getVendorProductId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_ACTIVE_DEACTIVE_URL + "?vendorProductId=" + vendorProductModel.getVendorProductId() + "&currentStatus=deactive")));
			}
			
			if (vendorProductModel.getProductVariantId() != null) {
				btnGroupStr.append("<span title='Updated' class='badge bg-success'>U</span>");
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = VendorProductModel.getVendorProductsSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, serviceId, null, vendorStoreId);
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
	public Response acivateDeactivateSurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_PRODUCT_ID) String vendorProductId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorProductModel vendorProductModel = new VendorProductModel();
		vendorProductModel.setVendorProductId(vendorProductId);

		if ("active".equals(currentStatus)) {
			vendorProductModel.setActive(false);
			vendorProductModel.setDeleted(true);
		} else {
			vendorProductModel.setActive(true);
			vendorProductModel.setDeleted(false);
		}

		vendorProductModel.updateProductsStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_PRODUCT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
