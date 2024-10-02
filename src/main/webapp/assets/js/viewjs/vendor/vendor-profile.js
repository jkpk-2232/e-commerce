jQuery(document).ready(function() {
    
	initializeFileUpload("profileImgUrl", "hiddenPhotoUrl_dummy", "hiddenPhotoUrl", false);
	
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		document.forms["edit-vendor-profile"].submit();
	});
    
    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});