jQuery(document).ready(function() {
	
	setMenuActiveSub("27");
	setMenuActiveSub("btnProductSubCategory");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-product-sub-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	initializeFileUpload("productSubCategoryImage", "hiddenProductSubCategoryImage_dummy", "hiddenProductSubCategoryImage");
	
});