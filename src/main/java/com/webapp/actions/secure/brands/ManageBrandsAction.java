package com.webapp.actions.secure.brands;

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

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.BrandModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-brands")
public class ManageBrandsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_BRAND_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_BRAND_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBrandList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);


		
		
		List<String> userIdList = null;
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			userIdList = new ArrayList<>();
			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
		}
		
		int total = BrandModel.getBrandsCount(dtu.getStartDatelong(), dtu.getEndDatelong(), userIdList);
		
		List<BrandModel> brandList = BrandModel.getBrandSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), userIdList, null, "");

		int count = dtu.getStartInt();

		for (BrandModel brandModel : brandList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(brandModel.getBrandId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(brandModel.getBrandName());
			dtuInnerJsonArray.put(brandModel.getBrandDescription());
			dtuInnerJsonArray.put(brandModel.getVendorName());
			dtuInnerJsonArray.put(brandModel.getVendorBrandName());

			if (brandModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}


			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_BRAND_URL + "?brandId=" + brandModel.getBrandId())));

			if (brandModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_ACTIVE_DEACTIVE_URL + "?brandId=" + brandModel.getBrandId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_ACTIVE_DEACTIVE_URL + "?brandId=" + brandModel.getBrandId() + "&currentStatus=deactive")));
			}
			
			if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				if (brandModel.isPublic()) {
					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REJECT, messageForKeyAdmin("labelReject"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_APPROVE_REJECT_URL + "?brandId=" + brandModel.getBrandId() + "&currentStatus=approve")));
				} else {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.APPROVE, messageForKeyAdmin("labelApproved"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_APPROVE_REJECT_URL + "?brandId=" + brandModel.getBrandId() + "&currentStatus=reject")));
				}
				 
				
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = BrandModel.getBrandSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), userIdList);
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
	public Response acivateDeactivateBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.BRAND_ID) String brandId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		BrandModel brandModel = new BrandModel();
		brandModel.setBrandId(brandId);

		if ("active".equals(currentStatus)) {
			brandModel.setActive(false);
			brandModel.setDeleted(true);
		} else {
			brandModel.setActive(true);
			brandModel.setDeleted(false);
		}

		brandModel.updateBrandStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);
	}
	
	@Path("/approve-reject")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response approveRejectBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.BRAND_ID) String brandId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		BrandModel brandModel = new BrandModel();
		brandModel.setBrandId(brandId);

		if ("reject".equals(currentStatus)) {
			brandModel.setPublic(true);
		} else {
			brandModel.setPublic(false);
		}

		brandModel.updateBrandPublicStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_BRAND_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
