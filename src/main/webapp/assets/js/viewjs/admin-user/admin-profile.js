jQuery(document).ready(function() {
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		document.forms["edit-admin-profile"].submit();
	});
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});