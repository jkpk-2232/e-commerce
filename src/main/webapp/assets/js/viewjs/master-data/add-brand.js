jQuery(document).ready(function() {
	
	setMenuActiveSub("28");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-brand").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	initializeFileUpload("brandImage", "hiddenBrandImage_dummy", "hiddenBrandImage");
	
});