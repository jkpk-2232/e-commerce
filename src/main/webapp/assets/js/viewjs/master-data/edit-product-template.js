jQuery(document).ready(function() {
	
	setMenuActiveSub("30");
	
	initializeFileUpload("productImage", "hiddenProductImage_dummy", "hiddenProductImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-product-template").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	jQuery("#productCategoryId").change(function() { 
		getProductSubCategoriesByProductCategory(jQuery("#productCategoryId").val());
	});
	
	if (jQuery("#isExists").val() == 'true') {
		jQuery("#productNameAndUomIdCombinationAlreadyExists").show();
	} else {
		jQuery("#productNameAndUomIdCombinationAlreadyExists").hide();
	}
	
});

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