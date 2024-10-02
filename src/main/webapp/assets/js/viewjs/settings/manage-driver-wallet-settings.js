jQuery(document).ready(function() {

    setMenuActiveSub("16");
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
    	document.forms["edit-driver-wallet-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});