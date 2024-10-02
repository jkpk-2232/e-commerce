jQuery(document).ready(function() {
	
	setMenuActiveSub("32");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-product-image").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	initializeFileUpload("productImage", "hiddenProductImage_dummy", "hiddenProductImage");
	
});