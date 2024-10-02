jQuery(document).ready(function() {
	
	setMenuActiveSub("40");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-rack-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
});