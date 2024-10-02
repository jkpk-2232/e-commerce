package com.webapp.actions.secure.productsubcategory;

import java.io.IOException;
import java.sql.SQLException;
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
import com.webapp.models.ProductSubCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-product-sub-category")
public class ManageProductSubCategoryAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getProductSubCategories(
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

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))
					|| UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID)) ) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_PRODUCT_SUB_CATEGORY_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PRODUCT_SUB_CATEGORY_JSP);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductSubCategoryList(
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


		int total = ProductSubCategoryModel.getProductSubCategoryCount(dtu.getStartDatelong(), dtu.getEndDatelong());
		List<ProductSubCategoryModel> productSubCategoryList = ProductSubCategoryModel.getProductSubCategorySearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt());

		int count = dtu.getStartInt();

		for (ProductSubCategoryModel productSubCategoryModel : productSubCategoryList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(productSubCategoryModel.getProductSubCategoryId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(productSubCategoryModel.getProductSubCategoryName());
			dtuInnerJsonArray.put(productSubCategoryModel.getProductSubCategoryDescription());
			dtuInnerJsonArray.put(productSubCategoryModel.getProductCategoryName());

			if (productSubCategoryModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}


			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_PRODUCT_SUB_CATEGORY_URL + "?productSubCategoryId=" + productSubCategoryModel.getProductSubCategoryId())));

			if (productSubCategoryModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_ACTIVE_DEACTIVE_URL + "?productSubCategoryId=" + productSubCategoryModel.getProductSubCategoryId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_ACTIVE_DEACTIVE_URL + "?productSubCategoryId=" + productSubCategoryModel.getProductSubCategoryId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = ProductSubCategoryModel.getProductSubCategorySearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage());
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
	public Response acivateDeactivateProductSubCategory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId,
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

		ProductSubCategoryModel productSubCategoryModel = new ProductSubCategoryModel();
		productSubCategoryModel.setProductSubCategoryId(productSubCategoryId);

		if ("active".equals(currentStatus)) {
			productSubCategoryModel.setActive(false);
			productSubCategoryModel.setDeleted(true);
		} else {
			productSubCategoryModel.setActive(true);
			productSubCategoryModel.setDeleted(false);
		}

		productSubCategoryModel.updateProductSubCategoryStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_URL);
	}
	
	@Path("/product-sub-categroy-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductSubCategoryList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String productSubCategoryIdOptions = DropDownUtils.getProductSubCategoryListOptionsByProductCategoryId(productCategoryId);
		
		output.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS, productSubCategoryIdOptions);

		return sendDataResponse(output);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PRODUCT_SUB_CATEGORY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
