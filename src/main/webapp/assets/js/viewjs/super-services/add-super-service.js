jQuery(document).ready(function() {
	
	//setMenuActive("36");
	setMenuActiveSub("57");
	
	jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
		jQuery("#add-super-service").submit();
	});
	
	jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
		redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
	});
	
	
	fileUpload("serviceImage", "hiddenServiceImage_dummy", "hiddenServiceImage", "services", jQuery("#cdnUrl").val());
});