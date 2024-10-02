jQuery(document).ready(function() {
	
	setMenuActiveSub("28");
	
	initializeFileUpload("brandImage", "hiddenBrandImage_dummy", "hiddenBrandImage");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-brand").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
});