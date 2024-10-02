jQuery(document).ready(function() {

    setMenuActiveSub("7");
    
    setMultiSelectPicker("passengerSmsApp");
    setMultiSelectPicker("driverSmsApp");
    setMultiSelectPicker("adminSmsApp");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["admin-sms-settings"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});