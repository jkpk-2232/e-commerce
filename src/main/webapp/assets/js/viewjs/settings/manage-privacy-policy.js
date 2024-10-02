jQuery(document).ready(function() {

    setMenuActiveSub("20");

    CKEDITOR.replace("privacyPolicy", {
        language: jQuery("#language").val()
    });

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-privacy-policy"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});