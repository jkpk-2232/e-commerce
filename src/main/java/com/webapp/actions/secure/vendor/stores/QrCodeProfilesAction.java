package com.webapp.actions.secure.vendor.stores;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.QrProfileModel;

@Path("/qr-code-profiles")
public class QrCodeProfilesAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getQrCodeProfilesList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
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
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_QR_CODE_PROFILE_URL + "?vendorId=" + vendorId +  "&vendorStoreId=" + vendorStoreId);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put(FieldConstants.VENDOR_ID, vendorId);
			data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_QR_CODE_PROFILE_URL + "?vendorId=" + vendorId + "&vendorStoreId=" + vendorStoreId);
		}

		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		


		return loadView(UrlConstants.JSP_URLS.QR_CODE_PROFILES_JSP);
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
		String vendorStoreId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);

		

		int total = QrProfileModel.getQrProfileCount(vendorStoreId);
		List<QrProfileModel> qrProfileModelList = QrProfileModel.getQrProfileSearch( dtu.getStartInt(), dtu.getLengthInt(), dtu.getGlobalSearchStringWithPercentage(), vendorStoreId);

		int count = dtu.getStartInt();

		for (QrProfileModel qrProfileModel : qrProfileModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(qrProfileModel.getQrProfileId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(qrProfileModel.getQrCodeId());
			dtuInnerJsonArray.put(qrProfileModel.getTerminalId());
			
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}
		
		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = QrProfileModel.getQrProfileSearchCount(dtu.getGlobalSearchStringWithPercentage(), vendorStoreId);
		} else {
			filterCount = total;
		}
		
		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.QR_CODE_PROFILES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
