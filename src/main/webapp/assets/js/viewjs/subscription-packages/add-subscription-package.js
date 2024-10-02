jQuery(document).ready(function() {

    setMenuActiveSub("18");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        document.forms["add-subscription-package"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});