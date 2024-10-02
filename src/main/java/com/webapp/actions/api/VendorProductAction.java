package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorProductUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;

@Path("/api/vendor-products")
public class VendorProductAction extends BusinessApiAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addVendorProduct(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		VendorProductModel vendorProductModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = vendorProductModelValidation(true, vendorProductModel, headerVendorId, request);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		boolean status = VendorProductUtils.insertProductIntoSystemViaAPI(vendorProductModel, loggedInUserId);

		if (status) {
			return sendSuccessMessage(messageForKey("successAddProduct", request));
		} else {
			return sendBussinessError(messageForKey("errorAddProduct", request));
		}
	}

	@PUT
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response editVendorProduct(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		VendorProductModel vendorProductModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = vendorProductModelValidation(false, vendorProductModel, headerVendorId, request);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		VendorProductUtils.updateProductIntoSystemViaAPI(vendorProductModel, loggedInUserId);

		return sendSuccessMessage(messageForKey("successEditProduct", request));
	}

	@Path("/{vendorProductId}/{isActivate}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response activeDeactiveProduct(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorProductId") String vendorProductId,
		@PathParam("isActivate") String isActivate
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorProductModel vendorProductModel = VendorProductModel.getProductsDetailsByProductId(vendorProductId);
		if (vendorProductModel == null) {
			return sendBussinessError(messageForKey("errorInvalidProduct", request));
		}

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

			List<String> vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInUserId, false);

			if (vendorStoreIdList.isEmpty()) {
				return sendBussinessError(messageForKey("errorNoStoresAssigned", request));
			}

			if (!vendorStoreIdList.contains(vendorProductModel.getVendorStoreId())) {
				return sendBussinessError(messageForKey("errorStoreNotAssignedToUser", request));
			}
		}

		if (Boolean.parseBoolean(isActivate)) {
			vendorProductModel.setActive(true);
			vendorProductModel.setDeleted(false);
		} else {
			vendorProductModel.setActive(false);
			vendorProductModel.setDeleted(true);
		}

		vendorProductModel.updateProductsStatus(loggedInUserId);

		if (Boolean.parseBoolean(isActivate)) {
			return sendSuccessMessage(messageForKey("successActivatedProduct", request));
		} else {
			return sendSuccessMessage(messageForKey("successDeactivatedProduct", request));
		}
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader
		) throws ServletException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		List<VendorProductModel> vendorProductList =  VendorProductModel.getProductListByVendorId(headerVendorId);
		
		Map<String, Object> finalOuput = new HashMap<>();
		finalOuput.put("vendorProductList", vendorProductList);

		return sendDataResponse(finalOuput);
	}
	
	@Path("/similar-products")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("vendorProductId") String vendorProductId,
		@QueryParam("productStatus") String productStatus
		) throws ServletException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		Map<String, Object> finalOuput = new HashMap<>();

		VendorProductModel vendorProduct =  VendorProductModel.getProductsDetailsByProductId(vendorProductId);
		
		finalOuput.put("productDetails", vendorProduct);
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		if (vendorProduct != null) {
			vendorProductList = VendorProductModel.getProductListWithOutPagination(headerVendorId, vendorProduct.getVendorStoreId(), productStatus, vendorProduct.getProductCategoryId(), "%%", vendorProduct.getProductSubCategory());
		}
		
		finalOuput.put("vendorProductList", vendorProductList);

		return sendDataResponse(finalOuput);
	}
	
	
	@Path("/similar-products-new")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getNewProductDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("vendorProductId") String vendorProductId,
		@QueryParam("productStatus") String productStatus
		) throws ServletException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		Map<String, Object> finalOuput = new HashMap<>();

		VendorProductModel vendorProduct =  VendorProductModel.getNewProductsDetailsByProductId(vendorProductId);
		
		finalOuput.put("productDetails", vendorProduct);
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		if (vendorProduct != null) {
			vendorProductList = VendorProductModel.getNewProductListWithOutPagination(headerVendorId, vendorProduct.getVendorStoreId(), productStatus, vendorProduct.getProductCategoryId(), "%%", vendorProduct.getProductSubCategory());
		}
		
		finalOuput.put("vendorProductList", vendorProductList);

		return sendDataResponse(finalOuput);
	}

	private List<String> vendorProductModelValidation(boolean isAddProduct, VendorProductModel vendorProductModel, String headerVendorId, HttpServletRequest request) throws IOException {

		Validator validator = new Validator();

		if (!isAddProduct) {
			validator.addValidationMapping("vendorProductId", messageForKey("labelVendorProductId", request), new RequiredValidationRule());
		}

		if (isAddProduct && !vendorProductModel.isProductForAllVendorStores()) {
			validator.addValidationMapping("vendorStoreId", messageForKey("labelVendorStoreId", request), new RequiredValidationRule());
		}

		validator.addValidationMapping("vendorId", messageForKey("labelVendorId", request), new RequiredValidationRule());
		validator.addValidationMapping("productCategory", messageForKey("labelProductCategory", request), new RequiredValidationRule());
		validator.addValidationMapping("productName", messageForKey("labelProductName", request), new RequiredValidationRule());
		validator.addValidationMapping("productInformation", messageForKey("labelProductInformation", request), new RequiredValidationRule());
		validator.addValidationMapping("productActualPrice", messageForKey("labelProductActualPrice", request), new DecimalValidationRule());
		validator.addValidationMapping("productDiscountedPrice", messageForKey("labelProductDiscountedPrice", request), new DecimalValidationRule());
		validator.addValidationMapping("productWeight", messageForKey("labelProductWeight", request), new DecimalValidationRule());
		validator.addValidationMapping("productWeight", messageForKey("labelProductWeight", request), new MinMaxValueValidationRule(0, 10000000));
		validator.addValidationMapping("productSpecification", messageForKey("labelProductSpecification", request), new RequiredValidationRule());
		validator.addValidationMapping("productInventoryCount", messageForKey("labelProductInventoryCount", request), new NumericValidationRule());
		validator.addValidationMapping("productInventoryCount", messageForKey("labelProductInventoryCount", request), new MinMaxValueValidationRule(0, 10000000));

		errorMessages = validator.validate(vendorProductModel);

		return errorMessages;
	}
}