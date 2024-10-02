jQuery(document).ready(function() {
    
    setMenuActiveSub("50");

    
    setMultiSelectPicker("regionList");
    setMultiSelectPicker("accessList");
    setMultiSelectPicker("exportAccessList");
    setMultiSelectPicker("productCategoryId");

    initializeFileUpload("vendorBrandImage", "hiddenVendorBrandImage_dummy", "hiddenVendorBrandImage");
    initializeFileUpload("profileImgUrl", "hiddenPhotoUrl_dummy", "hiddenPhotoUrl", false);
    
    jQuery("#serviceId").change(function() {
    	getCategoryServiceWiseAddEdit(false, "1");
    });
    
    jQuery("#selfDeliveryFeeParent").hide();
    
    if (jQuery("#isSelfDeliveryWithinXKm").val() === "true") {
    	jQuery("#selfDeliveryFeeParent").show();
    } else {
    	jQuery("#selfDeliveryFeeParent").hide();
    }
    
    jQuery("#isSelfDeliveryWithinXKm").change(function() {

    	if (jQuery("#isSelfDeliveryWithinXKm").val() === "true") {
        	jQuery("#selfDeliveryFeeParent").show();
        } else {
        	jQuery("#selfDeliveryFeeParent").hide();
        }
    });
    
    jQuery("#btnSave").click(function(e) {

        e.preventDefault();

		if (jQuery("#serviceId").val() == null || jQuery("#serviceId").val() == "") {
			displayBootstrapMessage("Select at least one super service.");
            return;
		} else if (jQuery("#categoryId").val() == null || jQuery("#categoryId").val() == "") {
			displayBootstrapMessage("Select at least one category.");
            return;
		} else {
			document.forms["add-vendor"].submit();
		}
	});

    jQuery("#btnCancel").click(function(e) {

        e.preventDefault();
        
    	redirectToUrl(CANCEL_PAGE_REDIRECT_URL);
    });
});