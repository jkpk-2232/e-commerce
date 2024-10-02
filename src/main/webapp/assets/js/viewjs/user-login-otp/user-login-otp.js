 jQuery(document).ready(function() {

    jQuery("#btnVerifyOtp").click(function(event) {

        event.preventDefault();

        document.verifyOtpForm.submit();
    });

    jQuery("#btnResendOtp").click(function(event) {

        event.preventDefault();

        resendOtp();
    });
});

function resendOtp() {

    jQuery.ajax({
        url: basePath + "/resend-otp.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            userId: jQuery("#userId").val()
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {
                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/verify-otp/" + jQuery("#userLoginOtpId").val() + ".do");
            } else if (responseData.type == TYPE_ERROR) {
                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + "/login.do");
            }
        }
    });
}