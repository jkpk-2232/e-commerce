jQuery(document).ready(function() {
	
	setMenuActiveSub("26");
	setMenuActiveSub("btnProductCategory");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-product-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	initializeFileUpload("productCategoryImage", "hiddenProductCategoryImage_dummy", "hiddenProductCategoryImage");
	
});