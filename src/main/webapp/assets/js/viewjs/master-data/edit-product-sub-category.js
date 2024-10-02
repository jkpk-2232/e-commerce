jQuery(document).ready(function() {
	
	setMenuActiveSub("27");
	
	initializeFileUpload("productSubCategoryImage", "hiddenProductSubCategoryImage_dummy", "hiddenProductSubCategoryImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-product-sub-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
});