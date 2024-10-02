jQuery(document).ready(function() {
	
	//setMenuActive("37");
	//setMenuActiveSub("67");
	setMenuActiveSub("56");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-category").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
});