jQuery(document).ready(function() {

    jQuery("#btnSend").click(function(e) {

        e.preventDefault();

        forgotPasswordAjaxFunction();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();

        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});

function forgotPasswordAjaxFunction() {

    var forgotPasswordAjax = jQuery.ajax({

        url: basePath + "/forgot-password.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            email: jQuery("#email").val(),
            roleId: jQuery("#roleId").val()
        },
        error: function() {
            ajaxInProgress = false;
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {
                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + responseData.SUCCESS_PAGE_REDIRECT_URL);
            } else if (responseData.type == TYPE_ERROR) {
                displayBootstrapMessage(responseData.message);
            }
        }
    });
}