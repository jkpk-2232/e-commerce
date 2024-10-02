jQuery(document).ready(function() {
	
	setMenuActiveSub("33");
    
    jQuery("#vendorStoreIdDivOuterDiv").hide();
    
    setMultiSelectPicker("vendorStoreId");

	clearErrorMessageFunction("serviceName");
    clearErrorMessageFunction("categoryName");
	
	jQuery("#serviceName").prop("readonly", true);
	jQuery("#categoryName").prop("readonly", true);
	
	getVendorServiceCategoryInformationWise("add");
	
	if (jQuery("#vendorId").val()) {

		getVendorStoresByVendorId(false, true);
	}
	
	
	jQuery("#vendorId").change(function() {
		
		getVendorServiceCategoryInformationWise("add");
		
		if (jQuery("#isProductForAllVendorStores").val() === "false") {
			getVendorStoresByVendorId(false, true);
		}
    });
    
    initializeFileUpload("productImage", "hiddenProductImage_dummy", "hiddenProductImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        console.log("vendorStoreId: " + jQuery("#vendorStoreId").picker("get"));
        
        jQuery("#vendorStoreIdListHidden").val(jQuery("#vendorStoreId").picker("get"));
        
		jQuery("#add-product").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	if (jQuery("#isProductForAllVendorStores").val() === "true") {
		jQuery("#vendorStoreIdDivOuterDiv").hide();
	} else {
		
		jQuery("#vendorStoreIdDivOuterDiv").show();
	}
	
	jQuery("#isProductForAllVendorStores").change(function() {
		
		if (jQuery("#isProductForAllVendorStores").val() === "true") {
			jQuery("#vendorStoreIdDivOuterDiv").hide();
		} else {
			getVendorStoresByVendorId(false, true);
			jQuery("#vendorStoreIdDivOuterDiv").show();
		}
    });
    
    /*
	if (jQuery("#brandId").val()) {
		
		getProductTemplatesByBrandIdAndProductCategoryId(jQuery("#brandId").val(), null);
	}*/
    
	jQuery('#brandId').change(function() { 
		getProductTemplatesByBrandIdAndProductCategoryId(jQuery("#brandId").val(), null);
		jQuery("#productVariantId").children('option').remove();
	});
	
	jQuery('#productTemplateId').change(function() { 
		getProductVariantsByProductTemplateId(jQuery("#productTemplateId").val());
	});	
	
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
