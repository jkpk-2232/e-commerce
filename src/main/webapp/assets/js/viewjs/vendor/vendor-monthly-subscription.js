jQuery(document).ready(function() {

    setMenuActive("21");

    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

        document.forms["vendor-monthly-subscription"].submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});