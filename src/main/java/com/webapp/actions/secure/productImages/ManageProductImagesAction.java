package com.webapp.actions.secure.productImages;

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
import com.webapp.models.ProductImageModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-product-images")
public class ManageProductImagesAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getProductImage (
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

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_PRODUCT_IMAGE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PRODUCT_IMAGE_JSP);
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

		int total = ProductImageModel.getProductImageCount(dtu.getStartDatelong(), dtu.getEndDatelong(), userIdList);
		List<ProductImageModel> productImageList = ProductImageModel.getProductImageSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), userIdList);

		int count = dtu.getStartInt();

		for (ProductImageModel productImageModel : productImageList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(productImageModel.getProductImageId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(productImageModel.getBrandName());
			dtuInnerJsonArray.put(productImageModel.getProductName());
			dtuInnerJsonArray.put(productImageModel.getProductVariantName());
			dtuInnerJsonArray.put(productImageModel.getWeight());
			dtuInnerJsonArray.put(productImageModel.getUomName());
			dtuInnerJsonArray.put(NewThemeUiUtils.getImageDiv(request, productImageModel));
			//dtuInnerJsonArray.put(brandModel.getBrandName());
			//dtuInnerJsonArray.put(brandModel.getBrandDescription());

			if (productImageModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}


			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_PRODUCT_IMAGE_URL + "?productImageId=" + productImageModel.getProductImageId())));

			if (productImageModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_ACTIVE_DEACTIVE_URL + "?productImageId=" + productImageModel.getProductImageId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_ACTIVE_DEACTIVE_URL + "?productImageId=" + productImageModel.getProductImageId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = ProductImageModel.getProductImageSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), userIdList);
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
	public Response acivateDeactivateProductImage (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_IMAGE_ID) String productImageId,
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

		ProductImageModel productImageModel = new ProductImageModel();
		productImageModel.setProductImageId(productImageId);

		if ("active".equals(currentStatus)) {
			productImageModel.setActive(false);
			productImageModel.setDeleted(true);
		} else {
			productImageModel.setActive(true);
			productImageModel.setDeleted(false);
		}

		productImageModel.updateProductImageStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_URL);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PRODUCT_IMAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
