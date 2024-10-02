package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.FieldConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ProductCategoryModel;
import com.webapp.models.ProductSubCategoryModel;
import com.webapp.models.VendorProductCategoryAssocModel;

@Path("/api/product-sub-category")
public class ProductSubCategoryAction extends BusinessApiAction {
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductSubCategoryList (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		ProductCategoryModel productCategoryModel = new ProductCategoryModel();
		
		ProductSubCategoryModel productSubCategoryModel = ProductSubCategoryModel.getProductSubCategoryDetailsByProductSubCategoryId(productSubCategoryId);
		
		if (productSubCategoryModel != null) {
			
			productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryName(productSubCategoryModel.getProductSubCategoryName().toUpperCase());
			
			if (productCategoryModel != null) {
				VendorProductCategoryAssocModel.getVendorProductCategoryAssocByVendorIdAndProductCategoryId(headerVendorId, productCategoryModel.getProductCategoryId());
				
				productCategoryModel.setProductSubCategoryList(ProductSubCategoryModel.getProductSubCategoryListByProductCategoryId(productCategoryModel.getProductCategoryId()));
			}
			
		}

		Map<String, Object> finalOuput = new HashMap<>();
		finalOuput.put("productCategory", productCategoryModel);

		return sendDataResponse(finalOuput);
	}

}
