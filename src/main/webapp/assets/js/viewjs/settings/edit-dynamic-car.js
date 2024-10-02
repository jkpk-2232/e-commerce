jQuery(document).ready(function() {

    setMenuActiveSub("8");

	initializeFileUpload("carTypeIconImage", "hiddenCarTypeIconImage_dummy", "hiddenCarTypeIconImage");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
    	jQuery("#edit-dynamic-car-type").submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});