jQuery(document).ready(function() {

    jQuery("#btnARMCancel").click(function(e) {

        e.preventDefault();
        
        jQuery("#accountRechargeModel").modal("hide"); 
        
        location.reload();
    });

    jQuery("#btnARMRecharge").click(function(e) {

        e.preventDefault();
        
        rechargeDriverAccount();
    });

    jQuery("#btnEncashModelCancel").click(function(e) {

        e.preventDefault();

        jQuery("#encashRequestModel").modal("hide");

        location.reload();
    });

    jQuery("#btnEncashModelEncash").click(function(e) {

        e.preventDefault();

        sendEncashRequest();
    });
});

function rechargeAccount(userId) {
    jQuery("#userIdHidden").val(userId);
    jQuery("#rechargeAmount").val(0);
    getDriverAccountDetails(userId);
}

function getDriverAccountDetails(userId) {

    jQuery.ajax({
        url: basePath + "/manage-drivers/account/details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "userId": userId
        },
        success: function(responseData) {

            if (responseData.type === TYPE_SUCCESS) {

                jQuery("#dName").val(responseData.name);
                jQuery("#dEmail").val(responseData.emailAddress);
                jQuery("#dPhoneNumber").val(responseData.phoneNumber);
                jQuery("#dCurrentBalence").val(responseData.currentBalance);
                jQuery("#dHoldBalence").val(responseData.holdBalance);
                jQuery("#dApprovedBalence").val(responseData.approvedBalance);
                jQuery("#dVendor").val(responseData.vendorName);

                jQuery("#accountRechargeModel").modal("show");

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}

function rechargeDriverAccount() {

    var userId = jQuery("#userIdHidden").val();
    var rechargeAmount = jQuery("#rechargeAmount").val();

    jQuery("#rechargeAmountError").text("");

    jQuery.ajax({
        url: basePath + "/manage-drivers/account/recharge.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            userId: userId,
            rechargeAmount: rechargeAmount
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + jQuery("#SUCCESS_PAGE_REDIRECT_URL").val());

            } else if (responseData.type == TYPE_ERROR) {

                displayBootstrapMessage(responseData.message);

            } else {

                validateFields(responseData);
            }
        }
    });
}

function validateFields(data) {

    var rechargeAmountError = "";

    if ('rechargeAmountError' in data) {
        rechargeAmountError = data.rechargeAmountError;
    }

    var arm1Error = errorMessageFunction("rechargeAmount", rechargeAmountError);

    if (arm1Error) {
        return false;
    }
}

function encashRequest(userId) {
    jQuery("#userIdHiddenForEncash").val(userId);
    jQuery("#requestedAmount").val(0);
    jQuery("#encashRemark").val("");
    getDriverAccountDetailsForEncash(userId);
}

function getDriverAccountDetailsForEncash(userId) {

    jQuery.ajax({
        url: basePath + "/manage-drivers/account/details.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "userId": userId
        },
        success: function(responseData) {

            if (responseData.type === TYPE_SUCCESS) {

            	jQuery("#dName1").val(responseData.name);
                jQuery("#dEmail1").val(responseData.emailAddress);
                jQuery("#dPhoneNumber1").val(responseData.phoneNumber);
                jQuery("#dCurrentBalence1").val(responseData.currentBalance);
                jQuery("#dHoldBalence1").val(responseData.holdBalance);
                jQuery("#dApprovedBalence1").val(responseData.approvedBalance);
                jQuery("#dVendor1").val(responseData.vendorName);

                jQuery("#encashRequestModel").modal("show");

            } else {

                displayBootstrapMessage(responseData.message);
            }
        }
    });
}

function sendEncashRequest() {

    var userId = jQuery("#userIdHiddenForEncash").val();
    var requestedAmount = jQuery("#requestedAmount").val();
    var encashRemark = jQuery("#encashRemark").val();
    var encashRequestStatus = jQuery("#encashRequestStatus").val();

    console.log(userId + " : " + encashRequestStatus + " : " + requestedAmount + " : " + encashRemark);

    jQuery("#requestedAmountError").text("");
    jQuery("#encashRemarkError").text("");

    jQuery.ajax({
        url: basePath + "/manage-drivers/account/encash.json",
        type: 'POST',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            userId: userId,
            encashRequestStatus: encashRequestStatus,
            requestedAmount: requestedAmount,
            encashRemark: encashRemark
        },
        success: function(responseData) {

            if (responseData.type == TYPE_SUCCESS) {

                displayBootstrapMessageWithRedirectionUrl(responseData.message, basePath + jQuery("#SUCCESS_PAGE_REDIRECT_URL").val());

            } else if (responseData.type == TYPE_ERROR) {

                displayBootstrapMessage(responseData.message);

            } else {

                validateFieldsForEncash(responseData);
            }
        }
    });
}

function validateFieldsForEncash(data) {

    var requestedAmountError = "";
    var encashRemarkError = "";

    if ('requestedAmountError' in data) {
        requestedAmountError = data.requestedAmountError;
    }

    if ('encashRemarkError' in data) {
        encashRemarkError = data.encashRemarkError;
    }

    var arm1Error = errorMessageFunction("requestedAmount", requestedAmountError);
    var arm2Error = errorMessageFunction("encashRemark", encashRemarkError);

    if (arm1Error || arm2Error) {
        return false;
    }
}