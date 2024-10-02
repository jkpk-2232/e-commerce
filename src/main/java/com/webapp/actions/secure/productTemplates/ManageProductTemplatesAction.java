package com.webapp.actions.secure.productTemplates;

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

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.BrandModel;
import com.webapp.models.ProductTemplateModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-product-templates")
public class ManageProductTemplatesAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getProductTemplate(
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

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_PRODUCT_TEMPLATE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PRODUCT_TEMPLATE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductTemplateList(
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

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))  || UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			userIdList = new ArrayList<>();
			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
		}
		
		int total = ProductTemplateModel.getProductTemplateCount(dtu.getStartDatelong(), dtu.getEndDatelong(), userIdList);

		List<ProductTemplateModel> productTemplateList = ProductTemplateModel.getProductTemplateSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), userIdList);

		int count = dtu.getStartInt();

		for (ProductTemplateModel productTemplateModel : productTemplateList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(productTemplateModel.getProductTemplateId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(productTemplateModel.getBrandName());
			dtuInnerJsonArray.put(productTemplateModel.getProductCategory());
			dtuInnerJsonArray.put(productTemplateModel.getProductName());
			dtuInnerJsonArray.put(productTemplateModel.getProductInformation());
			dtuInnerJsonArray.put(productTemplateModel.getUomName());

			if (productTemplateModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_PRODUCT_TEMPLATE_URL + "?productTemplateId=" + productTemplateModel.getProductTemplateId())));

			if (productTemplateModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_ACTIVE_DEACTIVE_URL + "?productTemplateId=" + productTemplateModel.getProductTemplateId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_ACTIVE_DEACTIVE_URL + "?productTemplateId=" + productTemplateModel.getProductTemplateId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = ProductTemplateModel.getProductTemplateSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), userIdList);
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
	public Response acivateDeactivateProductTemplate (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId,
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

		ProductTemplateModel productTemplateModel = new ProductTemplateModel();
		productTemplateModel.setProductTemplateId(productTemplateId);

		if ("active".equals(currentStatus)) {
			productTemplateModel.setActive(false);
			productTemplateModel.setDeleted(true);
		} else {
			productTemplateModel.setActive(true);
			productTemplateModel.setDeleted(false);
		}

		productTemplateModel.updateProductTemplateStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_URL);
	}

	@Path("/product-template-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductTemplateList (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.BRAND_ID) String brandId,
		@QueryParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		
		if (productCategoryId.isEmpty()) {
			productCategoryId = null;
		}
		
		String productTemplateIdOptions;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
			
			BrandModel brandModel =  BrandModel.getBrandDetailsByBrandId(brandId);
			
			if (brandModel != null) {
				if (brandModel.isPublic()) {
					productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(null, "", brandId, productCategoryId);
				} else {
					productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, "", brandId, productCategoryId);
				}
			}else {
				productTemplateIdOptions = null;
			}

		} else {
			productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(null, "", brandId, productCategoryId);
		}

		output.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		return sendDataResponse(output);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PRODUCT_TEMPLATE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
