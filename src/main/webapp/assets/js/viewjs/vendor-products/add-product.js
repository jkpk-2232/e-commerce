jQuery(document).ready(function() {
	
	setMenuActiveSub("1");
    
    jQuery("#vendorStoreIdDivOuterDiv").hide();
    
    setMultiSelectPicker("vendorStoreId");

	clearErrorMessageFunction("serviceName");
    clearErrorMessageFunction("categoryName");
	
	jQuery("#serviceName").prop("readonly", true);
	jQuery("#categoryName").prop("readonly", true);
	
	getVendorServiceCategoryInformationWise("add");
	
	if (jQuery("#vendorId").val()) {
		getProductCategoriesByVendorId(jQuery("#vendorId").val());

		getVendorStoresByVendorId(false, true);
	}
	
	
	jQuery("#vendorId").change(function() {
		
		getVendorServiceCategoryInformationWise("add");
		
		getProductCategoriesByVendorId(jQuery("#vendorId").val());
		
		jQuery("#productSubCategoryId").children('option').remove();
		
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
		//getVendorStoresByVendorId(false, true);
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
    
	jQuery("#productCategoryId").change(function() { 
		getProductSubCategoriesByProductCategory(jQuery("#productCategoryId").val());
	});
	
});

function getProductCategoriesByVendorId(vendorId) {
	
	var productCategoryListAjax = jQuery.ajax({
		url: basePath + "/manage-product-category/product-categroy-assoc-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "vendorId": vendorId
        },
        success: function(responseData) {

			//jQuery("#vendorStoreId").trigger("chosen:updated");
        	
        	jQuery("#productCategoryId").children('option').remove();
  	       	jQuery("#productCategoryId").append(responseData.productCategoryIdOptions);
  	       
        }
	});

}

function getProductSubCategoriesByProductCategory(productCategoryId) {
	
	var productCategoryListAjax = jQuery.ajax({
		url: basePath + "/manage-product-sub-category/product-sub-categroy-list.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "productCategoryId": productCategoryId
        },
        success: function(responseData) {

			//jQuery("#vendorStoreId").trigger("chosen:updated");
        	
        	jQuery("#productSubCategoryId").children('option').remove();
  	       	jQuery("#productSubCategoryId").append(responseData.productSubCategoryIdOptions);
  	       
        }
	});

}