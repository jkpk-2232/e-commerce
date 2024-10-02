jQuery(document).ready(function() {
	
	setMenuActiveSub("33");
	
	setMultiSelectPicker("vendorStoreId");
	
	clearErrorMessageFunction("serviceName");
	clearErrorMessageFunction("categoryName");

	jQuery("#serviceName").prop("readonly", true);
	jQuery("#categoryName").prop("readonly", true);
	jQuery("#productName").prop("readonly", true);
	
	getVendorServiceCategoryInformationWise("edit");
	
	jQuery("#vendorId").change(function() {
		getVendorServiceCategoryInformationWise("edit");
    });
		
	initializeFileUpload("productImage", "hiddenProductImage_dummy", "hiddenProductImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-product").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
	jQuery('#brandId').change(function() { 
		getProductTemplatesByBrandIdAndProductCategoryId(jQuery("#brandId").val(), null);
		jQuery("#productVariantId").children('option').remove();
	});
	
	jQuery('#productTemplateId').change(function() { 
		getProductVariantsByProductTemplateId(jQuery("#productTemplateId").val());
	});
	
	
	if (jQuery("#isExists").val() == 'true') {
		jQuery("#storeAndProductErrorMessage").show();
	} else {
		jQuery("#storeAndProductErrorMessage").hide();
	}
	
});

function getProductTemplatesByBrandIdAndProductCategoryId(brandId, productCategoryId) {

	var updateProductTemplateListAjax = jQuery.ajax({
		url: basePath + "/manage-product-templates/product-template-list.json",
		type: 'GET',
		cache: false,
		dataType: 'json',
		timeout: 50000,
		data: {
			"brandId": brandId,
			"productCategoryId": productCategoryId
		},
		success: function(responseData) {

			jQuery("#productTemplateId").children('option').remove();
			jQuery("#productTemplateId").append(responseData.productTemplateIdOptions);
			
			if (jQuery("#productVariantId").val() == null && jQuery("#productTemplateId").val() != null) {
				getProductVariantsByProductTemplateId(jQuery("#productTemplateId").val());
			}

		}
	});
}

function getProductVariantsByProductTemplateId(productTemplateId) {

	var updateProductVariantListAjax = jQuery.ajax({
		url: basePath + "/manage-product-variants/product-variant-list.json",
		type: 'GET',
		cache: false,
		dataType: 'json',
		timeout: 50000,
		data: {
			"productTemplateId": productTemplateId
		},
		success: function(responseData) {

			jQuery("#productVariantId").children('option').remove();
			jQuery("#productVariantId").append(responseData.productVariantIdOptions);

		}
	});
}
