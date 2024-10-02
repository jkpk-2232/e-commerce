jQuery(document).ready(function() {

    setMenuActiveSub("19");
    
    CKEDITOR.replace("aboutUs", {
        language: jQuery("#language").val()
    });

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();
        
        document.forms["add-about-us"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});