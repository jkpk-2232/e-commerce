package com.webapp.actions.secure.productVariants;

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
import com.webapp.models.ProductVariantModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-product-variants")
public class ManageProductVariantsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVariant(
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

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_PRODUCT_VARIANT_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PRODUCT_VARIANT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVariantList(
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

		int total = ProductVariantModel.getProductVariantsCount(dtu.getStartDatelong(), dtu.getEndDatelong(), userIdList);
		List<ProductVariantModel> variantList = ProductVariantModel.getProductVariantSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), userIdList);

		int count = dtu.getStartInt();

		for (ProductVariantModel productVariantModel : variantList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(productVariantModel.getProductVariantId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(productVariantModel.getBrandName());
			dtuInnerJsonArray.put(productVariantModel.getProductCategory());
			dtuInnerJsonArray.put(productVariantModel.getProductName());
			dtuInnerJsonArray.put(productVariantModel.getProductVariantName());
			dtuInnerJsonArray.put(productVariantModel.getProductVariantDescription());
			dtuInnerJsonArray.put(productVariantModel.getUomName());
			dtuInnerJsonArray.put(productVariantModel.getBarcode());

			if (productVariantModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_PRODUCT_VARIANT_URL + "?productVariantId=" + productVariantModel.getProductVariantId())));

			if (productVariantModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_ACTIVE_DEACTIVE_URL + "?productVariantId=" + productVariantModel.getProductVariantId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_ACTIVE_DEACTIVE_URL + "?productVariantId=" + productVariantModel.getProductVariantId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = ProductVariantModel.getProductVariantSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), userIdList);
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
	public Response acivateDeactivateVariant (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_VARIANT_ID) String productVariantId,
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

		ProductVariantModel productVariantModel = new ProductVariantModel();
		productVariantModel.setProductVariantId(productVariantId);

		if ("active".equals(currentStatus)) {
			productVariantModel.setActive(false);
			productVariantModel.setDeleted(true);
		} else {
			productVariantModel.setActive(true);
			productVariantModel.setDeleted(false);
		}

		productVariantModel.updateProductVariantStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_URL);
	}

	@Path("/product-variant-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductVariantList (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String productVariantIdOptions = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
			
			ProductTemplateModel productTemplateModel =  ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
			
			if (productTemplateModel != null) {
				
				BrandModel brandModel =  BrandModel.getBrandDetailsByBrandId(productTemplateModel.getBrandId());
				
				if (brandModel != null) {
					if (brandModel.isPublic()) {
						productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(null, "", productTemplateId);
					} else {
						productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, "", productTemplateId);
					}
				}
				
			} 
			

		} else {
			productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(null, "", productTemplateId);
		}

		output.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);

		return sendDataResponse(output);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PRODUCT_VARIANT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
