jQuery(document).ready(function() {
	
    setMenuActiveSub("25");

    CKEDITOR.replace("driverFare", {
        language: jQuery("#language").val()
    });

    jQuery("#btnSave").click(function(e) {
		

        e.preventDefault();
        
        document.forms["add-driver-fare"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});