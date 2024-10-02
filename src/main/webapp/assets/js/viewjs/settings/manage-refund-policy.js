jQuery(document).ready(function() {

    setMenuActiveSub("21");
    
    CKEDITOR.replace("refundPolicy", {
        language: jQuery("#language").val()
    });

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-refund-policy"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});