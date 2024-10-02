package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.VendorProductCategoryAssocUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ProductCategoryModel;
import com.webapp.models.ProductSubCategoryModel;

@Path("/api/product-category")
public class ProductCategoryAction extends BusinessApiAction {
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getProductCategoryList (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader
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

		List<String>	productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(headerVendorId);
		
		List<ProductCategoryModel> productCategoryList =  ProductCategoryModel.getProductCategoryListByProductCategoryId(productCategoryIdList);
		
		for (ProductCategoryModel productCategoryModel : productCategoryList) {
			
			productCategoryModel.setProductSubCategoryList(ProductSubCategoryModel.getProductSubCategoryListByProductCategoryId(productCategoryModel.getProductCategoryId()));
		}
		
		//List<ProductCategoryModel> productCategoryModel = ProductCategoryModel.getProductCategoryList();
		
		Map<String, Object> finalOuput = new HashMap<>();
		finalOuput.put("productCategoryList", productCategoryList);

		return sendDataResponse(finalOuput);
	}	

}
