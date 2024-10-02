package com.webapp.actions.secure.whmgmt.racks;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.BrandModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.WhmgmtRackCategoryModel;
import com.webapp.models.WhmgmtRackModel;
import com.webapp.models.WhmgmtRackSlotModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("add-rack")
public class AddRackAction  extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddRack (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("vendorId") String vendorId,
		@QueryParam("vendorStoreId") String vendorStoreId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}
		
		
		VendorStoreModel storeDetails = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
		
		List<WhmgmtRackModel> rackList =  WhmgmtRackModel.getRackListByvendorIdAndStoreId(vendorId,vendorStoreId, 0, -1);
		
		boolean isCreate = false;
		
		if (storeDetails.getNumberOfRacks() > 0 && rackList.size() == 0) {
			if (storeDetails.getNumberOfRacks() == rackList.size()) {
				isCreate = false;
			}else {
				isCreate = true;
			}
		}else {
			isCreate = false;
		}
		
		data.put("isCreate", String.valueOf(isCreate));
		
		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_RACK_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}
		
		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		
		VendorStoreModel storeDetails = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
		
		List<WhmgmtRackModel> rackList = new ArrayList<>();
		
		
		for (int i = 1; i <= storeDetails.getNumberOfRacks(); i++) {
			
			WhmgmtRackModel rackModel = new WhmgmtRackModel();
			
			rackModel.setRackId(UUIDGenerator.generateUUID());
			rackModel.setRackNumber(String.valueOf(i));
			rackModel.setRackStatus(true);
			rackModel.setVendorId(vendorId);
			rackModel.setRackCatId(storeDetails.getRackCategoryId());
			rackModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			rackModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			rackModel.setStoreId(vendorStoreId);
			rackModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
			rackModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
			
			rackList.add(rackModel);
			
			WhmgmtRackModel.insertRack(rackModel);
			
			List<WhmgmtRackSlotModel> whmgmtRackSlotList = new ArrayList<>();
			
			if (storeDetails.getRackCategoryId() != null) {
				WhmgmtRackCategoryModel categoryDetails =  WhmgmtRackCategoryModel.getRackCategoryDetailsByCategoryId(storeDetails.getRackCategoryId());
				if (categoryDetails != null) {
					for (int j = 1; j <= categoryDetails.getNoOfSlots() ; j++) {
						
						WhmgmtRackSlotModel whmgmtRackSlotModel = new WhmgmtRackSlotModel();
						whmgmtRackSlotModel.setSlotId(UUIDGenerator.generateUUID());
						whmgmtRackSlotModel.setSlotNumber(String.valueOf(j));
						whmgmtRackSlotModel.setSlotStatus("Available");
						whmgmtRackSlotModel.setRackId(rackModel.getRackId());
						whmgmtRackSlotList.add(whmgmtRackSlotModel);
					}
					
					if (whmgmtRackSlotList.size() > 0) {
						WhmgmtRackSlotModel.insertSlots(whmgmtRackSlotList);
					}
				}
				
			}
			
			
			
		}

		return loadView(UrlConstants.JSP_URLS.ADD_RACK_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRacks (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}
		DatatableUtils dtu = new DatatableUtils(request);
		
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		String vendorStoreId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);
		
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}
		
		List<WhmgmtRackModel> totalList  = WhmgmtRackModel.getRackListByvendorIdAndStoreId(vendorId, vendorStoreId, 0, -1);
		
		List<WhmgmtRackModel> rackList =  WhmgmtRackModel.getRackListByvendorIdAndStoreId(vendorId, vendorStoreId, dtu.getStartInt(), dtu.getLengthInt());
		
		int total = totalList.size();
		int count = 0;
		
		for (WhmgmtRackModel rackModel : rackList) {
			
			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(rackModel.getRackId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(rackModel.getRackNumber());
			dtuInnerJsonArray.put(rackModel.getRackStatus());
			dtuInnerJsonArray.put(rackModel.getVendorName());
			dtuInnerJsonArray.put(rackModel.getStoreName());

			if (true) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}


			
			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}
		
		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, total);
		
		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_RACK_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}
