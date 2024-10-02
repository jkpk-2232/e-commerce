jQuery(document).ready(function() {
	
	setMenuActiveSub("66");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#edit-super-service").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	fileUpload("serviceImage", "hiddenServiceImage_dummy", "hiddenServiceImage", "services", jQuery("#cdnUrl").val());
});