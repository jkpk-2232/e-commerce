package com.webapp.actions.secure.vendor.products;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;

import com.jeeutils.StringUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.ProductCategoryModel;
import com.webapp.models.ProductSubCategoryModel;
import com.webapp.models.VendorProductCategoryAssocModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorStoreModel;

@Path("/import-csv-products")
public class ImportCsvProductAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addProductGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		/*
		 * if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
		 * 
		 * VendorServiceCategoryModel vscm =
		 * VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(loginSessionMap
		 * .get(LoginUtils.USER_ID));
		 * 
		 * }
		 */

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, Arrays.asList(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		// String vendorIdOptions = DropDownUtils.getVendorListOptions("1",
		// ProjectConstants.VENDOR_ROLE_ID, assignedRegionList);
		// data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String isProductForAllVendorStoresOptions = DropDownUtils.getYesNoOption(ProjectConstants.YES);
		data.put(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORE_OPTIONS, isProductForAllVendorStoresOptions);

		data.put(FieldConstants.ADD_PRODUCT_FLOW, true + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);

		return loadView(UrlConstants.JSP_URLS.IMPORT_CSV_PRODUCTS_JSP);
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, "*/*" })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, "application/octet-stream" })
	//@formatter:off
	public Response importProducts(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, JSONException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.ADD_PRODUCT_FLOW, true + "");

		String tempStoragePath = "";
		String filename = "";
		File savedFile = null;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		tempStoragePath = WebappPropertyUtils.getWebAppProperty("csvDir");

		filename = request.getHeader("X-File-Name");
		String vendorId = null;
		String vendorStoreId = null;
		boolean isProductForAllVendorStores = false;

		if (isMultipart) {

			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (FileItem item : items) {

					if (item.isFormField()) {
						if (FieldConstants.VENDOR_ID.equals(item.getFieldName())) {
							vendorId = item.getString();
							String vendorIdOptions = DropDownUtils.getVendorListOptions(vendorId, ProjectConstants.UserRoles.VENDOR_ROLE_ID, assignedRegionList);
							data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
						} else if (FieldConstants.VENDOR_STORE_ID.equals(item.getFieldName())) {
							vendorStoreId = item.getString();
							String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(Arrays.asList(vendorStoreId), vendorId, assignedRegionList);
							data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
						} else if (FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORES.equals(item.getFieldName())) {
							isProductForAllVendorStores = Boolean.parseBoolean(item.getString());
							String isProductForAllVendorStoresOptions = DropDownUtils.getYesNoOption(item.getString());
							data.put(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORE_OPTIONS, isProductForAllVendorStoresOptions);
						}

					} else {
						String str = item.getName();
						String replace = str.replace("\\", ":");
						String spilt[] = replace.split(":");

						filename = spilt[spilt.length - 1];

						savedFile = new File(tempStoragePath, filename + StringUtils.getExtension(filename));

						item.write(savedFile);

						try (Reader reader = Files.newBufferedReader(Paths.get(tempStoragePath, filename + StringUtils.getExtension(filename)), StandardCharsets.UTF_8)) {

							@SuppressWarnings("unchecked")
							CsvToBean<VendorProductModel> csvToBean = new CsvToBeanBuilder(reader).withType(VendorProductModel.class).withSeparator(',').withIgnoreQuotations(false).withIgnoreLeadingWhiteSpace(true).withIgnoreEmptyLine(true)
										.withThrowExceptions(false).build();

							Iterator<VendorProductModel> csvUserIterator = csvToBean.iterator();

							List<VendorProductModel> addProductList = new ArrayList<>();
							List<VendorProductModel> editProductList = new ArrayList<>();
							List<VendorProductModel> deleteProductList = new ArrayList<>();

							while (csvUserIterator.hasNext()) {
								VendorProductModel vpModel = csvUserIterator.next();
								if (!vpModel.getProductCategory().isEmpty() && !vpModel.getProductName().isEmpty()) {

									ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryName(vpModel.getProductCategory().toUpperCase());

									if (productCategoryModel != null) {

										VendorProductCategoryAssocModel vendorProductCategoryAssocModel = VendorProductCategoryAssocModel.getVendorProductCategoryAssocByVendorIdAndProductCategoryId(vendorId,
													productCategoryModel.getProductCategoryId());

										ProductSubCategoryModel pSubCategoryModel = ProductSubCategoryModel.getProductSubCategoryDetailsByProductCategoryIdAndProductSubCategoryName(productCategoryModel.getProductCategoryId(),
													vpModel.getProductSubCategory() != null ? vpModel.getProductSubCategory().toUpperCase() : vpModel.getProductSubCategory());

										if (vendorProductCategoryAssocModel != null) {
											// Checking action field having value grate then zero
											if (vpModel.getAction() > 0) {

												vpModel.setVendorId(vendorId);
												vpModel.setProductForAllVendorStores(isProductForAllVendorStores);
												if (ProjectConstants.QuantityTypeConstants.LOOSE.equals(vpModel.getPrdQtyType())) {
													vpModel.setProductName("L " + vpModel.getProductName());
												}
												/*
												 * else { vpModel.setProductName("P "+vpModel.getProductName()); }
												 */
												vpModel.setGst(vpModel.getGst());
												vpModel.setProductSku(OrderUtils.formProductSKU(vpModel));

												if (vpModel.getAction() == 1) {

													/*
													 * vpModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
													 * vpModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
													 * vpModel.setCreatedAt(createdAt); vpModel.setUpdatedAt(createdAt);
													 * vpModel.setActive(true); vpModel.setDeleted(false);
													 */

													vpModel.setPaid(true);
													if (pSubCategoryModel != null) {
														vpModel.setProductSubCategoryId(pSubCategoryModel.getProductSubCategoryId());
													}
													if (vpModel.getNonVeg() != null) {
														if (vpModel.getNonVeg().toUpperCase().equals("NO"))
															vpModel.setIsNonVeg(false);
														else
															vpModel.setIsNonVeg(true);
													}

													vpModel.setProductCategoryId(productCategoryModel.getProductCategoryId());
													addProductList.add(vpModel);

												} else if (vpModel.getAction() == 2) {
													editProductList.add(vpModel);
												} else if (vpModel.getAction() == 3) {
													deleteProductList.add(vpModel);
												}

											}
										}

									}
								}

							}
							if (savedFile.exists()) {
								savedFile.delete();
							}
							if (isProductForAllVendorStores) {

								List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);
								for (VendorStoreModel vendorStoreModel : vendorStoreList) {
									// Add Products
									for (VendorProductModel vpModel : addProductList) {
										if (vpModel.getProductBarcode().isEmpty()) {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId());
											if (vpObject == null) {
												vpModel.setVendorStoreId(vendorStoreModel.getVendorStoreId());
												vpModel.insertVendorProducts(loginSessionMap.get(LoginUtils.USER_ID));
											}
										} else {
											boolean isExists = VendorProductModel.isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vendorStoreModel.getVendorStoreId(), vpModel.getProductBarcode());

											if (!isExists) {
												VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(),
															vpModel.getProductSku(), vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId(), vpModel.getProductBarcode());
												if (vpObject == null) {
													vpModel.setVendorStoreId(vendorStoreModel.getVendorStoreId());
													vpModel.insertVendorProducts(loginSessionMap.get(LoginUtils.USER_ID));
												}
											}

										}

									}
									// Edit products
									for (VendorProductModel vpModel : editProductList) {
										if (vpModel.getProductBarcode().isEmpty()) {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId());
											if (vpObject != null) {
												if (vpModel.getNonVeg() != null) {
													if (vpModel.getNonVeg().toUpperCase().equals("NO"))
														vpObject.setIsNonVeg(false);
													else
														vpObject.setIsNonVeg(true);
												}
												vpObject.setGst(vpModel.getGst());
												vpObject.setPrdQtyType(vpModel.getPrdQtyType());
												vpObject.setProductActualPrice(vpModel.getProductActualPrice());
												vpObject.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
												vpObject.setProductInventoryCount(vpModel.getProductInventoryCount());
												vpObject.setProductInformation(vpModel.getProductInformation());
												vpObject.setProductSpecification(vpModel.getProductSpecification());
												vpObject.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
											}
										} else {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId(), vpModel.getProductBarcode());
											if (vpObject != null) {
												if (vpModel.getNonVeg() != null) {
													if (vpModel.getNonVeg().toUpperCase().equals("NO"))
														vpObject.setIsNonVeg(false);
													else
														vpObject.setIsNonVeg(true);
												}
												vpObject.setGst(vpModel.getGst());
												vpObject.setPrdQtyType(vpModel.getPrdQtyType());
												vpObject.setProductActualPrice(vpModel.getProductActualPrice());
												vpObject.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
												vpObject.setProductInventoryCount(vpModel.getProductInventoryCount());
												vpObject.setProductInformation(vpModel.getProductInformation());
												vpObject.setProductSpecification(vpModel.getProductSpecification());
												vpObject.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
											} else {
												VendorProductModel vendorProduct = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(),
															vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId());
												boolean isExists = VendorProductModel.isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vendorStoreModel.getVendorStoreId(), vpModel.getProductBarcode());
												if (!isExists) {
													if (vendorProduct != null) {
														if (vpModel.getNonVeg() != null) {
															if (vpModel.getNonVeg().toUpperCase().equals("NO"))
																vendorProduct.setIsNonVeg(false);
															else
																vendorProduct.setIsNonVeg(true);
														}
														vendorProduct.setGst(vpModel.getGst());
														vendorProduct.setPrdQtyType(vpModel.getPrdQtyType());
														vendorProduct.setProductBarcode(vpModel.getProductBarcode());
														vendorProduct.setProductActualPrice(vpModel.getProductActualPrice());
														vendorProduct.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
														vendorProduct.setProductInventoryCount(vpModel.getProductInventoryCount());
														vendorProduct.setProductInformation(vpModel.getProductInformation());
														vendorProduct.setProductSpecification(vpModel.getProductSpecification());
														vendorProduct.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
													}
												}

											}

										}

									}
									// delete products
									for (VendorProductModel vpModel : deleteProductList) {
										if (vpModel.getProductBarcode().isEmpty()) {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId());
											if (vpObject != null) {
												vpObject.setActive(false);
												vpObject.setDeleted(true);
												vpObject.updateProductsStatus(loginSessionMap.get(LoginUtils.USER_ID));
											}
										} else {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreModel.getVendorStoreId(), vpModel.getProductBarcode());
											if (vpObject != null) {
												vpObject.setActive(false);
												vpObject.setDeleted(true);
												vpObject.updateProductsStatus(loginSessionMap.get(LoginUtils.USER_ID));
											}
										}

									}
								}

							} else {

								// Add products

								for (VendorProductModel vpModel : addProductList) {
									if (vpModel.getProductBarcode().isEmpty()) {
										VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(), vpModel.getProductName(),
													vpModel.getProductCategory(), vendorStoreId);
										if (vpObject == null) {
											vpModel.setVendorStoreId(vendorStoreId);
											vpModel.insertVendorProducts(loginSessionMap.get(LoginUtils.USER_ID));
										}
									} else {

										boolean isExists = VendorProductModel.isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vendorStoreId, vpModel.getProductBarcode());

										if (!isExists) {
											VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreId, vpModel.getProductBarcode());
											if (vpObject == null) {
												vpModel.setVendorStoreId(vendorStoreId);
												vpModel.insertVendorProducts(loginSessionMap.get(LoginUtils.USER_ID));
											}
										}
									}

								}

								// Edit products
								for (VendorProductModel vpModel : editProductList) {
									if (vpModel.getProductBarcode().isEmpty()) {
										VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(), vpModel.getProductName(),
													vpModel.getProductCategory(), vendorStoreId);
										if (vpObject != null) {
											if (vpModel.getNonVeg() != null) {
												if (vpModel.getNonVeg().toUpperCase().equals("NO"))
													vpObject.setIsNonVeg(false);
												else
													vpObject.setIsNonVeg(true);
											}
											vpObject.setGst(vpModel.getGst());
											vpObject.setPrdQtyType(vpModel.getPrdQtyType());
											vpObject.setProductActualPrice(vpModel.getProductActualPrice());
											vpObject.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
											vpObject.setProductInventoryCount(vpModel.getProductInventoryCount());
											vpObject.setProductInformation(vpModel.getProductInformation());
											vpObject.setProductSpecification(vpModel.getProductSpecification());
											vpObject.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
										}
									} else {
										VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vpModel.getProductSku(),
													vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreId, vpModel.getProductBarcode());
										if (vpObject != null) {
											if (vpModel.getNonVeg() != null) {
												if (vpModel.getNonVeg().toUpperCase().equals("NO"))
													vpObject.setIsNonVeg(false);
												else
													vpObject.setIsNonVeg(true);
											}
											vpObject.setGst(vpModel.getGst());
											vpObject.setPrdQtyType(vpModel.getPrdQtyType());
											vpObject.setProductActualPrice(vpModel.getProductActualPrice());
											vpObject.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
											vpObject.setProductInventoryCount(vpModel.getProductInventoryCount());
											vpObject.setProductInformation(vpModel.getProductInformation());
											vpObject.setProductSpecification(vpModel.getProductSpecification());
											vpObject.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
										} else {
											VendorProductModel vendorProduct = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(),
														vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreId);
											boolean isExists = VendorProductModel.isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vendorStoreId, vpModel.getProductBarcode());
											if (!isExists) {

												if (vendorProduct != null) {
													if (vpModel.getNonVeg() != null) {
														if (vpModel.getNonVeg().toUpperCase().equals("NO"))
															vendorProduct.setIsNonVeg(false);
														else
															vendorProduct.setIsNonVeg(true);
													}
													vendorProduct.setGst(vpModel.getGst());
													vendorProduct.setProductBarcode(vpModel.getProductBarcode());
													vendorProduct.setProductActualPrice(vpModel.getProductActualPrice());
													vendorProduct.setProductDiscountedPrice(vpModel.getProductDiscountedPrice());
													vendorProduct.setProductInventoryCount(vpModel.getProductInventoryCount());
													vendorProduct.setProductInformation(vpModel.getProductInformation());
													vendorProduct.setProductSpecification(vpModel.getProductSpecification());
													vendorProduct.updateVendorProductsFromCSV(loginSessionMap.get(LoginUtils.USER_ID));
												}
											}
										}
									}

								}
								// delete products
								for (VendorProductModel vpModel : deleteProductList) {
									if (vpModel.getProductBarcode().isEmpty()) {
										VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vpModel.getVendorId(), vpModel.getProductSku(), vpModel.getProductName(),
													vpModel.getProductCategory(), vendorStoreId);
										if (vpObject != null) {
											vpObject.setActive(false);
											vpObject.setDeleted(true);
											vpObject.updateProductsStatus(loginSessionMap.get(LoginUtils.USER_ID));
										}
									} else {
										VendorProductModel vpObject = VendorProductModel.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vpModel.getVendorId(), vpModel.getProductSku(),
													vpModel.getProductName(), vpModel.getProductCategory(), vendorStoreId, vpModel.getProductBarcode());
										if (vpObject != null) {
											vpObject.setActive(false);
											vpObject.setDeleted(true);
											vpObject.updateProductsStatus(loginSessionMap.get(LoginUtils.USER_ID));
										}
									}

								}
							}
						} catch (RuntimeException e) {
							e.printStackTrace();
							data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
							return loadView(UrlConstants.JSP_URLS.IMPORT_CSV_PRODUCTS_JSP);
						}

					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
				return loadView(UrlConstants.JSP_URLS.IMPORT_CSV_PRODUCTS_JSP);
			}

		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);

	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.IMPORT_CSV_PRODUCTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
