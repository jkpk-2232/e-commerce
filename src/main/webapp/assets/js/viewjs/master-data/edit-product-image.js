jQuery(document).ready(function() {
	
	setMenuActiveSub("32");
	
	initializeFileUpload("productImage", "hiddenProductImage_dummy", "hiddenProductImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-product-image").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
});