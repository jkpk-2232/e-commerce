jQuery(document).ready(function() {

    setMenuActiveSub("17");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-order-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});