jQuery(document).ready(function() {

    setMenuActiveSub("6");
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-admin-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});