jQuery(document).ready(function() {

    jQuery("#signup-submit-button").click(function() {

        var hasErrors = true;
        
        if (jQuery("#password").val() != "" && jQuery("#confirmPassword").val() != "") {

            if (jQuery("#password").val() != jQuery("#confirmPassword").val()) {

                errorMessageFunction("confirmPassword", jQuery("#labelPasswordandConfirmpasswordshouldbesame").val());

                hasErrors = false;
            }
        }

//        if (jQuery("#referralCode").val() == "") {
//            errorMessageFunction("referralCode", jQuery("#errorReferralCodeIsRquired").val());
//            hasErrors = false;
//        }

        if (!hasErrors) {
            return false;
        } else {
            document.forms["signup"].submit();
        }

    });

    setInterval(function() {
        jQuery("#sucMessage label").html("");
    }, 4000);

    jQuery("#language").change(function() {

        document.location = basePath + "/signup.do?referralCode=" + jQuery("#referralCode").val();
    });
});

function errorMessageFunction(fieldId, errorMessage) {

    var hassError = false;

    jQuery("#" + fieldId + "Error").html("");

    if (errorMessage !== null || errorMessage !== "") {
        jQuery("#" + fieldId).after("<span id='" + fieldId + "Error' class='errorMessage'>" + errorMessage + "</span>");
        hasError = true;
    }

    return hasError;
}