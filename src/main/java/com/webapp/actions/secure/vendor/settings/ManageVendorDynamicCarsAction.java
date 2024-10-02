package com.webapp.actions.secure.vendor.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.utils.myhub.CourierUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorCarTypeModel;

@Path("/manage-vendor-dynamic-cars")
public class ManageVendorDynamicCarsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorDynamicCars(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId,
		@QueryParam(FieldConstants.SERVICE_TYPE_ID) String serviceTypeId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(vendorId, serviceTypeId);
		List<String> list = new ArrayList<String>();
		for (VendorCarTypeModel string : vendorCarTypeList) {
			list.add(string.getCarTypeId());
		}

		UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		String carTypeListOptions = DropDownUtils.getCarTypeListOptionsForMultiselect(list, true);
		data.put(FieldConstants.CAR_TYPE_LIST_OPTIONS, carTypeListOptions);

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_NAME, vendorModel.getFullName());
		data.put(FieldConstants.SERVICE_TYPE_ID, serviceTypeId);
		data.put(FieldConstants.SERVICE_NAME, CourierUtils.getServiceTypeName(serviceTypeId));
		data.put(FieldConstants.CAR_ICON, CourierUtils.getServiceTypeCarIcon(serviceTypeId));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_DYNAMIC_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response getVendorDynamicCarsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.SERVICE_TYPE_ID) String serviceTypeId,
		@FormParam(FieldConstants.CAR_TYPE_LIST) List<String> carTypeList
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorCarTypeModel tempModel = new VendorCarTypeModel();
		tempModel.setVendorId(vendorId);
		tempModel.setServiceTypeId(serviceTypeId);
		tempModel.deleteExistingCarTypes();

		int priority = 0;
		VendorCarTypeModel vendorCarTypeModel = new VendorCarTypeModel();

		for (String carTypeId : carTypeList) {

			priority++;

			vendorCarTypeModel = new VendorCarTypeModel();
			vendorCarTypeModel.setCarTypeId(carTypeId);
			vendorCarTypeModel.setVendorId(vendorId);
			vendorCarTypeModel.setServiceTypeId(serviceTypeId);
			vendorCarTypeModel.setPriority(priority);
			vendorCarTypeModel.insertVendorCarType(vendorId);
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_DYNAMIC_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}