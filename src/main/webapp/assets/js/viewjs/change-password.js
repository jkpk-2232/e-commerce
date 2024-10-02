jQuery("#btnCancelPopUp").click(function() {
	hidePopUp();
});

jQuery("#btnSubmitPopUp").click(function() {
	postChangePasswordRequest();
});

function hidePopUp() {
	clearOldData();
	jQuery("#changePasswordModal").modal("hide"); 
}

function clearOldData() {
	
	jQuery("#oldPassword").val("");
    jQuery("#newPassword").val("");
    jQuery("#confirmPassword").val("");
	
	clearErrorMessageFunction("oldPassword");
    clearErrorMessageFunction("newPassword");
    clearErrorMessageFunction("confirmPassword");
}

function postChangePasswordRequest() {

    if (ajaxInProgress1 === true) {
        return;
    }
    
    ajaxInProgress1 = true;

    passwordChangeAjax = jQuery.ajax({

        url: basePath + "/admin-profile/change-password.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            oldPassword: jQuery("#oldPassword").val(),
            newPassword: jQuery("#newPassword").val(),
            confirmPassword: jQuery("#confirmPassword").val()
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {

            	displayBootstrapMessage(responseData.message);
            	
            	hidePopUp();

            } else if (responseData.type == TYPE_ERROR) {

            	displayBootstrapMessage(responseData.message);

            } else {

                validateFields(responseData);
            }

            ajaxInProgress1 = false;
        }
    });
}

function validateFields(data) {

    var oldPasswordError = "";
    var newPasswordError = "";
    var confirmPasswordError = "";

    if ('oldPasswordError' in data) {
        oldPasswordError = data.oldPasswordError;
    }

    if ('newPasswordError' in data) {
        newPasswordError = data.newPasswordError;
    }

    if ('confirmPasswordError' in data) {
        confirmPasswordError = data.confirmPasswordError;
    }

    var pulError = errorMessageFunction("oldPassword", oldPasswordError);
    var fnError = errorMessageFunction("newPassword", newPasswordError);
    var lnError = errorMessageFunction("confirmPassword", confirmPasswordError);

    if (pulError || fnError || lnError) {
        return false;
    }
}