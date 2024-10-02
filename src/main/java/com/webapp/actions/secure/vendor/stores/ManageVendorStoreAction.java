package com.webapp.actions.secure.vendor.stores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeutils.StringUtils;
import com.utils.HttpURLConnectionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.LocationModel;
import com.webapp.models.StoreCategorieModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-vendor-store")
public class ManageVendorStoreAction extends BusinessAction {

	public static Logger logger = Logger.getLogger(ManageVendorStoreAction.class);

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorStores(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);

			data.put(FieldConstants.VENDOR_ID, vendorId);
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_STORE_URL + "?vendorId=" + vendorId);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put(FieldConstants.VENDOR_ID, vendorId);
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_STORE_URL + "?vendorId=" + vendorId);
		}

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_STORE_JSP);
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

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

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

		int total = VendorStoreModel.getVendorStoreCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, vendorStoreIdList);
		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), vendorId, vendorStoreIdList);

		int count = dtu.getStartInt();

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorStoreModel.getVendorStoreId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorStoreModel.getVendorName());
			dtuInnerJsonArray.put(vendorStoreModel.getStoreName() != null ? vendorStoreModel.getStoreName() : ProjectConstants.NOT_AVAILABLE);
			String type = "";
			if (vendorStoreModel.getType() != null) {
				String[] typeArray = vendorStoreModel.getType().split(",");
				for (String typeId : typeArray) {
					if (type.length() > 0) {
						type = type + ", ";
					}
					if (typeId.equals(String.valueOf(ProjectConstants.TypeConstants.STORE_ID))) {
						type = type + ProjectConstants.TypeConstants.STORE ;
					} else {
						type = type + ProjectConstants.TypeConstants.WAREHOUSE ;
					}
				}
			}
			dtuInnerJsonArray.put(type);
			dtuInnerJsonArray.put(vendorStoreModel.getStoreAddress());

			if (vendorStoreModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			// Store can be edited only by admin and super admin
			if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_VENDOR_STORE_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId())));
			}

			// no store activate or deactivate access to sub vendor
			if (!UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				if (vendorStoreModel.isActive()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_ACTIVATE_DEACTIVATE_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId() + "&currentStatus=active")));
				} else {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_ACTIVATE_DEACTIVATE_URL + "?vendorStoreId=" + vendorStoreModel.getVendorStoreId() + "&currentStatus=deactive")));
				}
			}
			
			if (Boolean.TRUE.equals(vendorStoreModel.getLedDeviceForStore())) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelLedStoreDetails"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.LED_STORE_DETAILS_URL +"?vendorId=" + vendorStoreModel.getVendorId() + "&vendorStoreId=" + vendorStoreModel.getVendorStoreId()), UrlConstants.JSP_URLS.VENDOR_STORE_LOCATION_ICON));
			}
			
			if (vendorStoreModel.getType().contains(String.valueOf(ProjectConstants.TypeConstants.WAREHOUSE_ID))) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelRacks"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADD_RACK_URL +"?vendorId=" + vendorStoreModel.getVendorId() + "&vendorStoreId=" + vendorStoreModel.getVendorStoreId()), UrlConstants.JSP_URLS.RACK_ICON));
			}
			
			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelQrCode"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.QR_CODE_PROFILES_URL +"?vendorId=" + vendorStoreModel.getVendorId() + "&vendorStoreId=" + vendorStoreModel.getVendorStoreId()), UrlConstants.JSP_URLS.QR_CODE_ICON));
			

			if (btnGroupStr.length() <= 0) {
				btnGroupStr.append(messageForKey("labelAccessDenied"));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = VendorStoreModel.getVendorStoreSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, vendorStoreIdList);
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

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
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

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorStoreModel.getVendorId());
		}
	}
	
	@Path("/vendor-stores-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorServiceCategoryInformation(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@QueryParam("vendorId") String vendorId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		List<VendorStoreModel> vendorStoresList = VendorStoreModel.getVendorStoresListInfo(vendorId);
		
		return sendDataResponse(vendorStoresList);
	}
	
	@Path("/vendor-stores-info-by-service")
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response vendorStoresInfoBasedOnCategory(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("serviceIds") String serviceIds
		) throws ServletException, IOException {

		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		String serviceId [] = serviceIds.split(",");
		List<String> serviceIdList = new ArrayList<>();
		for (String sId : serviceId) {
			serviceIdList.add(sId);
		}
		List<VendorStoreModel> vendorStores =  VendorStoreModel.getVendorStoresBasedOnService(serviceIdList);

		 return sendDataResponse(vendorStores);
	}

	@Path("/store-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStoreList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId,
		@QueryParam(FieldConstants.SHOW_ALL) String showAll,
		@QueryParam(FieldConstants.MULTI_SELECT) String multiSelect
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String vendorStoreIdOptions;

		if (Boolean.parseBoolean(multiSelect)) {
			if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				vendorStoreIdOptions = DropDownUtils.getERPBrandsFilterListOptions(null, vendorId, assignedRegionList);
			}else {
				vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(null, vendorId, assignedRegionList);
			}
			
		} else {
			if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				vendorStoreIdOptions = DropDownUtils.getERPBrandsFilterListOptions(null, vendorId, assignedRegionList);
			}else {
				vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(Boolean.parseBoolean(showAll), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, assignedRegionList, vendorId);
			}
			
		}

		output.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		return sendDataResponse(output);
	}

	@Path("/store-list-region-wise")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStoreListRegionWise(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("regionIds") String regionIds,
		@QueryParam("vendorStoreIds") String vendorStoreIds
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		logger.info("\n\n\n\tregionIds\t" + regionIds);

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		System.out.println("\n\n\n\tregionIds\t" + regionIds);

		List<String> selectedRegionList = MyHubUtils.splitStringByCommaList(regionIds);
		List<String> vendorStoreIdsList = MyHubUtils.splitStringByCommaList(vendorStoreIds);

		System.out.println("\n\n\n\tselectedRegionList\t" + selectedRegionList);

		String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(vendorStoreIdsList, loginSessionMap.get(LoginUtils.USER_ID), selectedRegionList);
		output.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		return sendDataResponse(output);
	}
	
	@Path("/get-store-categories")
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response getStoreCategories (
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("categoryGroup") String categoryGroupId
		) throws ServletException, IOException {

		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		Map<String, String> output = new HashMap<>();
		try {
			ObjectMapper mapper = new ObjectMapper();

			
			Map<String, Object> storeCategoriesObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_STORE_CATEGORIES + "?cat_group_id=" + categoryGroupId);
			
			String storeCategoryOptions = "";
			
			if (storeCategoriesObjects != null) {
				List<StoreCategorieModel> storeCategoryList = mapper.convertValue(storeCategoriesObjects.get("store_categories"), new TypeReference<List<StoreCategorieModel>>() {});

				storeCategoryOptions = DropDownUtils.getStoreCategoryListOption(storeCategoryList);
			}
			
			output.put(FieldConstants.STORE_CATEGORY_OPTIONS, storeCategoryOptions);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendDataResponse(output);
	}
	
	@Path("/get-locations")
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response getLocations (
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("city") String cityId,
		@QueryParam("allOption") boolean allOption
		) throws ServletException, IOException {

		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		Map<String, String> output = new HashMap<>();
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_LOCATIONS + "?city_id=" + cityId);

			String locationOptions = "";
			
			if (locationsObjects != null) {
				List<LocationModel> locationList = mapper.convertValue(locationsObjects.get("locations"), new TypeReference<List<LocationModel>>() {
				});

				locationOptions = DropDownUtils.getLocationListOption(locationList,allOption, "");
			}

			output.put(FieldConstants.LOCATION_OPTIONS, locationOptions);
			 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendDataResponse(output);
	}
	
	@Path("/erp-brand-list-region-wise")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getErpBrandListRegionWise(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("regionIds") String regionIds,
		@QueryParam("vendorStoreIds") String vendorStoreIds
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		logger.info("\n\n\n\tregionIds\t" + regionIds);

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		System.out.println("\n\n\n\tregionIds\t" + regionIds);

		List<String> selectedRegionList = MyHubUtils.splitStringByCommaList(regionIds);
		List<String> vendorStoreIdsList = MyHubUtils.splitStringByCommaList(vendorStoreIds);

		System.out.println("\n\n\n\tselectedRegionList\t" + selectedRegionList);

		String vendorStoreIdOptions = DropDownUtils.getERPBrandsFilterListOptions(vendorStoreIdsList, loginSessionMap.get(LoginUtils.USER_ID), selectedRegionList);
		output.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		return sendDataResponse(output);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_STORE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}