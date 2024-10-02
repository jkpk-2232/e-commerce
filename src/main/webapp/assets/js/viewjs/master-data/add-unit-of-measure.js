jQuery(document).ready(function() {
	
	setMenuActiveSub("29");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-uom").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
});