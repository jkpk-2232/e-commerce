jQuery(document).ready(function() {

    setMenuActiveSub("55");

    getVendorMonthlySubscriptionStatus();
    
    jQuery("#btnSave").hide();

    // initializeFileUpload("feedBaner", "hiddenFeedBanerImage_dummy", "hiddenFeedBanerImage");
    
    fileUpload("feedBaner", "hiddenFeedBanerImage_dummy", "hiddenFeedBanerImage", "feed", jQuery("#cdnUrl").val());
    
    jQuery("#btnSave").click(function() {
        jQuery("#add-feed").submit();
    });

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
        redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
    
    if (jQuery("#vendorId").val()) {

		getVendorStoresByVendorId(false, false);
	}

    jQuery("#vendorId").change(function(e) {

        e.preventDefault();
        
        getVendorMonthlySubscriptionStatus();
        getVendorStoresByVendorId(false, false);
    });
});

function getVendorMonthlySubscriptionStatus() {

    var updateDriverListAjax = jQuery.ajax({
        url: basePath + "/manage-vendor-feeds/vendor-monthly-subscription-status.json",
        type: 'GET',
        cache: false,
        dataType: 'json',
        timeout: 50000,
        data: {
            "vendorId": jQuery("#vendorId").val()
        },
        success: function(responseData) {

            if (responseData.type === "ERROR") {
                jQuery("#vendorMonthlySubscriptionExpiredMessage").show();
            } else {
                jQuery("#vendorMonthlySubscriptionExpiredMessage").hide();
            }

            if (responseData.restrictAddFeed === "Yes") {
                jQuery("#btnSave").hide();
            } else {
                jQuery("#btnSave").show();
            }
        }
    });
}