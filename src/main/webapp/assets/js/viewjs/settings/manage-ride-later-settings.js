jQuery(document).ready(function() {

    setMenuActiveSub("14");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-ride-later-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });

    jQuery("#minBookingTime").timeEntry({
        show24Hours: true
    });
});