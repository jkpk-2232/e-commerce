jQuery(document).ready(function() {

    setMenuActiveSub("22");
    
    CKEDITOR.replace("termsConditions", {
        language: jQuery("#language").val()
    });

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-terms-conditions"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});