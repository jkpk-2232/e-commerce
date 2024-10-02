jQuery(document).ready(function() {
	
	setMenuActiveDoubleTab("6", "23");
    
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#subscribe-package").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
});