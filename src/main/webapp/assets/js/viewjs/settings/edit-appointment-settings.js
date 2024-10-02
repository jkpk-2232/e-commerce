jQuery(document).ready(function() {

    setMenuActiveSub("37");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["edit-appointment-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
    jQuery("#minBookingTime").timeEntry({
        show24Hours: true
    });
});