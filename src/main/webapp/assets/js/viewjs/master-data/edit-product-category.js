jQuery(document).ready(function() {
	
	setMenuActiveSub("26");
	
	initializeFileUpload("productCategoryImage", "hiddenProductCategoryImage_dummy", "hiddenProductCategoryImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-product-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
});